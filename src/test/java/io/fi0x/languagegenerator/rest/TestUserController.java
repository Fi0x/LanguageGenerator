package io.fi0x.languagegenerator.rest;

import io.fi0x.languagegenerator.logic.dto.LoginDto;
import io.fi0x.languagegenerator.logic.dto.UserDto;
import io.fi0x.languagegenerator.logic.dto.Word;
import io.fi0x.languagegenerator.service.AuthenticationService;
import io.fi0x.languagegenerator.service.GenerationService;
import io.fi0x.languagegenerator.service.LanguageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class TestUserController
{
    private static final String LOGIN_URL = "/custom-login";
    private static final String REGISTER_URL = "/register";

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Tag("UnitTest")
    void test_showLogin_success() throws Exception
    {
        mvc.perform(get(LOGIN_URL))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(model().attribute("loginDto", new LoginDto()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/login.jsp"));
    }

    @Test
    @Tag("UnitTest")
    void test_showRegister_success() throws Exception
    {
        mvc.perform(get(REGISTER_URL))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(model().attribute("userDto", new UserDto()))
                .andExpect(request().sessionAttribute("redirect", "redirect:register"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/signup.jsp"));
    }

    @Test
    @Tag("UnitTest")
    void test_registerUser_success() throws Exception
    {
        doNothing().when(authenticationService).registerUser(any());

        mvc.perform(post(REGISTER_URL).formFields(getUserDtoFields()))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(model().attributeDoesNotExist("registerError"))
                .andExpect(request().sessionAttributeDoesNotExist("redirect"))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @Tag("UnitTest")
    void test_registerUser_fail() throws Exception
    {
        doThrow(DuplicateKeyException.class).when(authenticationService).registerUser(any());

        mvc.perform(post(REGISTER_URL).formFields(getUserDtoFields()))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("register"));
    }

    private MultiValueMap<String, String> getUserDtoFields()
    {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", "Batman");
        map.add("password", "aaaa");
        map.add("matchingPassword", "aaaa");
        return map;
    }
}
