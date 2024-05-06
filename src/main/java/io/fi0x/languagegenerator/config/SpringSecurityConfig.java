package io.fi0x.languagegenerator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SpringSecurityConfig
{
    @Bean
    public InMemoryUserDetailsManager createUserDetails()
    {
        return new InMemoryUserDetailsManager(createUser("fi0x", "123"),
                createUser("dummy1", "456"),
                createUser("dummy2", "789"));
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    private UserDetails createUser(String username, String password)
    {
        return User.builder().passwordEncoder(input -> passwordEncoder().encode(input)).username(username).password(password).roles("USER", "ADMIN").build();
    }
}
