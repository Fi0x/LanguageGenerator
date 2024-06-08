package io.fi0x.languagegenerator.rest.validation;

import io.fi0x.languagegenerator.logic.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class TestPasswordMatchValidator
{
    private static final String USERNAME = "User xy";
    private static final String PASSWORD = "very secure";
    private static final String PASSWORD_MATCH = "very secure";
    private static final String PASSWORD_NO_MATCH = "not so secure";

    private PasswordMatchValidator validator = new PasswordMatchValidator();

    @Test
    @Tag("UnitTest")
    void test_isValid_true()
    {
        UserDto dto = getUserDto();
        dto.setMatchingPassword(PASSWORD_MATCH);
        Assertions.assertTrue(validator.isValid(dto, null));
    }
    @Test
    @Tag("UnitTest")
    void test_isValid_false()
    {
        UserDto dto = getUserDto();
        dto.setMatchingPassword(PASSWORD_NO_MATCH);
        Assertions.assertFalse(validator.isValid(dto, null));
    }

    @Test
    @Tag("UnitTest")
    void test_initialize_no_exception()
    {
        Assertions.assertDoesNotThrow(() -> validator.initialize(null));
    }

    private UserDto getUserDto()
    {
        UserDto dto = new UserDto();
        dto.setUsername(USERNAME);
        dto.setPassword(PASSWORD);
        return dto;
    }
}
