package io.fi0x.languagegenerator.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService
{
    public String getAuthenticatedUsername()
    {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
