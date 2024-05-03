package io.fi0x.languagegenerator.service;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationService
{
    public boolean authenticate(String username, String password)
    {
        if(!"fi0x".equals(username))
            return false;

        return "123".equals(password);
    }
}
