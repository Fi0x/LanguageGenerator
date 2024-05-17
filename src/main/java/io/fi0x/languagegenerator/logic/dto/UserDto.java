package io.fi0x.languagegenerator.logic.dto;

import io.fi0x.languagegenerator.logic.dto.validation.PasswordMatch;
import lombok.Data;

@Data
@PasswordMatch
public class UserDto
{
    private String username;
    private String password;
    private String matchingPassword;
}
