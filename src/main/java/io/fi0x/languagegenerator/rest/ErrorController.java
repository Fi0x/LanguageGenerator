package io.fi0x.languagegenerator.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@AllArgsConstructor
public class ErrorController
{
    @GetMapping("/error")
    public String showError(ModelMap model)
    {
        log.info("showError() called");

        return "error";
    }
}
