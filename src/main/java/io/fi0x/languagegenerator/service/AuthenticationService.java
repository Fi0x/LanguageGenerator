package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.logic.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService
{
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public String getAuthenticatedUsername()
    {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public void registerUser(UserDto userDto) throws DuplicateKeyException
    {
        if(userDetailsManager.userExists(userDto.getUsername()))
            throw new DuplicateKeyException("A user with that username already exists");

        userDetailsManager.createUser(User.builder()
                .passwordEncoder(passwordEncoder::encode)
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .roles("USER").build());
    }
}
