package io.fi0x.languagegenerator.rest.validation;

import io.fi0x.languagegenerator.logic.dto.UserDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object>
{
    @Override
    public void initialize(PasswordMatch constraintAnnotation)
    {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext)
    {
        UserDto userDto = (UserDto) o;
        return userDto.getPassword().equals(userDto.getMatchingPassword());
    }
}
