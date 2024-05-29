package io.fi0x.languagegenerator.rest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@SessionAttributes({"redirect", "userDto"})
public class CustomExceptionHandler
{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationErrors(MethodArgumentNotValidException error, HttpServletRequest request)
    {
        log.info("handleValidationErrors() called with error={}, request={}", error, request);

        Collections.list(request.getSession().getAttributeNames()).stream().filter(a -> a.contains("Error"))
                .forEach(a -> request.getSession().removeAttribute(a));

        String redirect = (String) request.getSession().getAttribute("redirect");
        Map<String, String> fieldErrors = error.getBindingResult().getFieldErrors().stream().filter(e -> e.getDefaultMessage() != null)
                .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage, (a, b) -> a));
        List<ObjectError> globalErrors = error.getBindingResult().getGlobalErrors();

        for (Map.Entry<String, String> entry : fieldErrors.entrySet())
            request.getSession().setAttribute(entry.getKey() + "Error", entry.getValue());

        Optional<ObjectError> passwordError = globalErrors.stream().findAny();
        passwordError.ifPresent(objectError -> request.getSession().setAttribute("passwordMatchError", objectError.getDefaultMessage()));

        return redirect;
    }
}
