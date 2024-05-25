package io.fi0x.languagegenerator.logic.dto;

import io.fi0x.languagegenerator.rest.validation.PasswordMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatch
public class UserDto
{
    //TODO: Adjust max-lengths for all values to fit in the database

    @NotBlank(message = "Username must not be empty")
    private String username;
    @Size(min = 4, message = "Password must have at least 4 characters")
    private String password;
    @Size(min = 4, message = "Password must have at least 4 characters")
    private String matchingPassword;
}
