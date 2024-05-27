package io.fi0x.languagegenerator.rest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Slf4j
@ControllerAdvice
@SessionAttributes({"registerError", "redirect"})
public class CustomExceptionHandler
{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationErrors(MethodArgumentNotValidException error, HttpServletRequest request)
    {
        log.info("handleValidationErrors() called with error={}, request={}", error, request);

        String redirect = (String) request.getSession().getAttribute("redirect");
        List<String> errors = error.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
        request.getSession().setAttribute("registerError", errors);

        return redirect;
    }

}
