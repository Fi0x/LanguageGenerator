package io.fi0x.languagegenerator.config;

import lombok.extern.slf4j.Slf4j;
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
public class SpringSecurityConfig
{
    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        log.debug("securityFilterChain() bean called");

        http.authorizeHttpRequests(auth -> {
//            auth.requestMatchers("/register").permitAll();
            auth.requestMatchers("/register").anonymous();
//            auth.requestMatchers("/register").authenticated();
            auth.anyRequest().authenticated();
        });
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

        http.formLogin(Customizer.withDefaults());
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

        builder.driverClassName("com.mysql.cj.jdbc.Driver");
        builder.url("jdbc:mysql://localhost:3306/languages");
        builder.username("dummyUser");
        builder.password("123");

        return builder.build();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource)
    {
        log.debug("userDetailsManager() bean called");

        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        createUser(manager, "fi0x", "123", "USER", "ADMIN");
        createUser(manager, "dummy1", "456", "USER");

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
