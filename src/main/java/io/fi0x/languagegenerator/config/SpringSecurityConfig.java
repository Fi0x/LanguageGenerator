package io.fi0x.languagegenerator.config;

import io.github.fi0x.util.config.HomeServerUtilConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig
{
	private static final String[] PUBLIC_URLS = new String[]{"/", "/*", "/WEB-INF/jsp/list-languages.jsp", "/error",
			"/WEB-INF/jsp/error.jsp", "/download", "/generate", "/WEB-INF/jsp/list-words.jsp", "/webjars/bootstrap" +
			"/*/css/*", "/webjars/bootstrap/*/js/*", "/webjars/jquery/*/*", "/css/design.css", "/images/*",
			"/dictionary", "/WEB-INF/jsp/dictionary.jsp", "/word", "/WEB-INF/jsp/word.jsp", "/js/functions.js"};
	private static final String[] ANONYMOUS_URLS = new String[]{"/register", "/WEB-INF/jsp/signup.jsp", "/custom-login"
			, "/WEB-INF/jsp/login.jsp"};

	private static final String[] PRIVATE_URLS = new String[]{};

	//TODO: Add all the private urls
	@Bean
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	public SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception
	{
		log.debug("securityFilterChain() bean called");

		return HomeServerUtilConfig.securityFilterChainSetup(http, PUBLIC_URLS, ANONYMOUS_URLS, PRIVATE_URLS);
	}

	//TODO: Re-Enable this config when service is not using a hub
//	@Bean
//	@Order(SecurityProperties.BASIC_AUTH_ORDER)
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
//	{
//		log.debug("securityFilterChain() bean called");
//
//		http.authorizeHttpRequests(auth -> {
//			auth.requestMatchers(PUBLIC_URLS).permitAll();
//			auth.requestMatchers(ANONYMOUS_URLS).anonymous();
//			auth.anyRequest().authenticated();
//		});
//
//		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
//
//		http.formLogin(form -> {
//			form.loginPage("/custom-login");
//			form.loginProcessingUrl("/login");
//			form.defaultSuccessUrl("/", true);
//			form.permitAll();
//		});
//
//		http.csrf(AbstractHttpConfigurer::disable);
//		http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
//
//		return http.build();
//	}
}
