package io.fi0x.languagegenerator.rest;

import io.fi0x.languagegenerator.logic.dto.UserDto;
import io.fi0x.languagegenerator.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@AllArgsConstructor
public class UserController
{
    //TODO: Make the signup page available when the user is not logged in

    private AuthenticationService authenticationService;

    @GetMapping("/register")
    public String createUser(ModelMap model)
    {
        log.info("createUser() called");

        model.put("userDto", new UserDto());

        return "signup";
    }

    @PostMapping("/register")
    public String registerUser(ModelMap model, @Valid UserDto userDto)
    {
        //TODO: Create a validation exception page that notifies the user that the pw does not match
        log.info("registerUser() called with userDto={}", userDto);

        try {
            authenticationService.registerUser(userDto);
        } catch (DuplicateKeyException e)
        {
            //TODO: Show an error that the user already exists
            model.put("userDto", userDto);
            return "redirect:register";
        }

        return "redirect:/";
    }
}
