package io.fi0x.languagegenerator.rest;

import io.fi0x.languagegenerator.logic.dto.LanguageJson;
import io.fi0x.languagegenerator.service.LanguageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.io.InvalidObjectException;

@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes({"language", "amount", "words"})
public class FileController
{
    LanguageService languageService;

    @PostMapping("/upload-language")
    public String uploadLanguage(ModelMap model, LanguageJson languageJson)
    {
        log.info("uploadLanguage() called");

        try
        {
            languageService.addLanguage(languageJson, "New Language", false);
        } catch (InvalidObjectException e)
        {
            log.warn("Could not save the language because it was not complete.");
        }
        return "redirect:/";
    }
}
