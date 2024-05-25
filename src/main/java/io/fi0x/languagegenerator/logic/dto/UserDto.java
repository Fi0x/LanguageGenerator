package io.fi0x.languagegenerator.logic.dto;

import io.fi0x.languagegenerator.rest.validation.PasswordMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatch
public class UserDto
{
    @NotBlank(message = "Username must not be empty")
    @Size(max = 50, message = "Username must not be longer than 50 characters")
    private String username;
    @Size(min = 4, max = 72, message = "Password must have 4 to 72 characters")
    private String password;
    @Size(min = 4, max = 72, message = "Matching password must have 4 to 72 characters")
    private String matchingPassword;
}
