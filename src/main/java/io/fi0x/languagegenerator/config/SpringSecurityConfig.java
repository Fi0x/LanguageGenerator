package io.fi0x.languagegenerator.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Slf4j
@Configuration
//@EnableWebSecurity
public class SpringSecurityConfig
{
    @Value("${spring.datasource.url}")
    private String database;
    @Value("${spring.datasource.username}")
    private String dbUsername;
    @Value("${spring.datasource.password}")
    private String dbPassword;
    @Value("${spring.datasource.driver-class-name}")
    private String dbDriver;
    @Value("${languagegenerator.username}")
    private String webUser;
    @Value("${languagegenerator.password}")
    private String webPassword;

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        log.debug("securityFilterChain() bean called");

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/register", "/custom-login", "/WEB-INF/jsp/login.jsp").permitAll();
//            auth.requestMatchers("/register", "/custom-login").permitAll();
            auth.anyRequest().authenticated();
        });

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

        http.formLogin(form -> {
            form.loginPage("/custom-login");
            form.loginProcessingUrl("/custom-login");
            form.defaultSuccessUrl("/", true);
            form.permitAll();
        });

        http.httpBasic(Customizer.withDefaults());

        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }

    @Bean
    public DataSource dataSource()
    {
        log.debug("dataSources() bean called");

        DataSourceBuilder<?> builder = DataSourceBuilder.create();

        builder.driverClassName(dbDriver);
        builder.url(database);
        builder.username(dbUsername);
        builder.password(dbPassword);

        return builder.build();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource)
    {
        log.debug("userDetailsManager() bean called");

        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        if (!manager.userExists(webUser))
            createUser(manager, webUser, webPassword, "USER", "ADMIN");

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    private void createUser(JdbcUserDetailsManager userManager, String username, String password, String... roles)
    {
        userManager.createUser(User.builder().passwordEncoder(input -> passwordEncoder().encode(input)).username(username).password(password).roles(roles).build());
    }
}
