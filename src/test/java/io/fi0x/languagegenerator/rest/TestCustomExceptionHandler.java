package io.fi0x.languagegenerator.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;

// TODO: Add working unit-tests
public class TestCustomExceptionHandler
{
    private static final String REDIRECT = "go here";

    @Mock
    private BindingResult bindingResult;
    @Mock
    private HttpServletRequest servletRequest;

    private CustomExceptionHandler handler = new CustomExceptionHandler();

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Tag("UnitTest")
    void test_handleValidationErrors()
    {
    }
}
