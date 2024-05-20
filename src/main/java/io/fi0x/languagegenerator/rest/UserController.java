package io.fi0x.languagegenerator.rest;

import io.fi0x.languagegenerator.logic.dto.UserDto;
import io.fi0x.languagegenerator.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes({"registerError", "redirect"})
public class UserController
{
    //TODO: Make the signup page available when the user is not logged in
    //TODO: Disable the signup page and login page when the user is logged in

    private AuthenticationService authenticationService;

    @GetMapping("/register")
    public String createUser(ModelMap model, HttpServletRequest request)
    {
        log.info("createUser() called");

        model.put("userDto", new UserDto());
        request.getSession().setAttribute("redirect", "redirect:register");

        return "signup";
    }

    @PostMapping("/register")
    public String registerUser(ModelMap model, HttpServletRequest request, @Valid UserDto userDto)
    {
        log.info("registerUser() called with userDto={}", userDto);

        try {
            authenticationService.registerUser(userDto);
        } catch (DuplicateKeyException e)
        {
            model.put("userDto", userDto);
            model.put("registerError", "A user with this name already exists.");
            return "redirect:register";
        }

        model.remove("registerError");
        request.getSession().removeAttribute("redirect");

        return "redirect:/";
    }
}
