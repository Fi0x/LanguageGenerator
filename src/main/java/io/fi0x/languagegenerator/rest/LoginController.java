package io.fi0x.languagegenerator.rest;

import io.fi0x.languagegenerator.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@AllArgsConstructor
@Controller
@SessionAttributes("username")
public class LoginController
{
    private AuthenticationService authenticationService;

    @GetMapping("login")
    public String showLogin()
    {
        return "login";
    }

    @PostMapping("login")
    public String login(ModelMap model, @RequestParam String username, @RequestParam String password)
    {
        if(authenticationService.authenticate(username, password))
        {
            model.put("username", username);
            return "languageView";
        }

        model.put("exception", "Invalid Credentials! Please try again");

        return "login";
    }
}
