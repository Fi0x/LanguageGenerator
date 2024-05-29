package io.fi0x.languagegenerator.rest;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
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
    public String showError(ModelMap model, HttpServletRequest request)
    {
        log.info("showError() called");

        model.put("errorCode", request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));

        return "error";
    }
}
