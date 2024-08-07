package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.logic.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TestAuthenticationService
{
    private static final String USERNAME = "Authenticated Username";

    @Mock
    private UserDetailsManager userDetailsManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AuthenticationService service;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);
        doNothing().when(userDetailsManager).createUser(any());
    }

    @Test
    @Tag("UnitTest")
    void test_getAuthenticatedUsername_success()
    {
        MockedStatic<SecurityContextHolder> staticMock = mockStatic(SecurityContextHolder.class);
        staticMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        doReturn(authentication).when(securityContext).getAuthentication();
        doReturn(USERNAME).when(authentication).getName();

        Assertions.assertEquals(USERNAME, service.getAuthenticatedUsername());

        staticMock.close();
    }

    @Test
    @Tag("UnitTest")
    void test_registerUser_success()
    {
        doReturn(false).when(userDetailsManager).userExists(any());
        doReturn("!@#QWE234retfvg").when(passwordEncoder).encode(any());

        Assertions.assertDoesNotThrow(() -> service.registerUser(getUserDto()));
    }

    @Test
    @Tag("UnitTest")
    void test_registerUser_error()
    {
        doReturn(true).when(userDetailsManager).userExists(any());

        Assertions.assertThrows(DuplicateKeyException.class, () -> service.registerUser(getUserDto()));
    }

    private UserDto getUserDto()
    {
        UserDto dto = new UserDto();
        dto.setUsername("AAA a d ddd ds");
        dto.setPassword("AAAA");
        dto.setMatchingPassword("AAAA");
        return dto;
    }
}
