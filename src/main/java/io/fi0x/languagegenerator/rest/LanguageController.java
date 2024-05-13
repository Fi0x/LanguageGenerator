package io.fi0x.languagegenerator.rest;

import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.service.AuthenticationService;
import io.fi0x.languagegenerator.service.GenerationService;
import io.fi0x.languagegenerator.service.LanguageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.InvalidObjectException;
import java.util.Objects;

@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes({"language", "amount", "words"})
public class LanguageController
{
    // TODO: Add user accounts that can be created for free and are stored in a local db

    // TODO: Add the possibility to import and export language-files

    // TODO: Add the option to delete a language

    private AuthenticationService authenticationService;
    private GenerationService generationService;
    private LanguageService languageService;

    @GetMapping("/generate")
    public String generateWords(ModelMap model, @RequestParam(value = "language", defaultValue = "-1", required = false) long language,
                                @RequestParam(value = "amount", defaultValue = "-1", required = false) int amount)
    {
        log.info("generateWords() called with language={}, amount={}", language, amount);

        if (language < 0)
            language = Long.parseLong(Objects.requireNonNullElse(model.get("language"), "0").toString());
        else
            model.put("language", language);

        if (amount < 0)
            amount = Integer.parseInt(Objects.requireNonNullElse(model.get("amount"), "10").toString());
        else
            model.put("amount", amount);

        try
        {
            model.put("words", generationService.generateWords(language, amount));
        } catch (EntityNotFoundException e)
        {
            log.warn("Could not find a language with id={}", language);
            return "redirect:/";
        } catch (InvalidObjectException e)
        {
            log.warn("The constraints for the language with id={} are not valid to generate a word", language);
            return "redirect:/";
        }

        return "list-words";
    }

    @GetMapping("/*")
    public String redirectWrongUrl()
    {
        return "redirect:/";
    }

    @GetMapping("/")
    public String listLanguages(ModelMap model)
    {
        log.info("listLanguages() called");

        //TODO: Also add public languages to every user
        model.put("languages", languageService.getUserLanguages(authenticationService.getAuthenticatedUsername()));

        return "list-languages";
    }

    @GetMapping("/add-language")
    public String createLanguage(ModelMap model, @RequestParam(value = "languageId", defaultValue = "-1", required = false) long languageId)
    {
        log.info("createLanguage() called");

        model.put("languageData", languageService.getLanguageData(languageId));

        return "language";
    }

    @PostMapping("/add-language")
    public String addLanguage(ModelMap model, @Valid LanguageData languageData)
    {
        log.info("addLanguage() called");
        log.debug("arguments for addLanguage() call are model={}, languageData={}", model, languageData);

        try
        {
            languageService.addLanguage(languageData);
        } catch (InvalidObjectException e)
        {
            log.info("Could not save the language because it was not complete.");
            return "redirect:add-language";
        }

        return "redirect:/";
    }
}
