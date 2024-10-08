package io.fi0x.languagegenerator.rest;

import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.service.AuthenticationService;
import io.fi0x.languagegenerator.service.GenerationService;
import io.fi0x.languagegenerator.service.LanguageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.io.InvalidObjectException;
import java.util.Objects;

@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes({"amount", "language", "languages", "languageCreator", "languageName", "originalEndpoint", "username", "words"})
public class LanguageController
{
    private GenerationService generationService;
    private LanguageService languageService;
    private AuthenticationService authenticationService;

    @Transactional
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

        LanguageData languageData = languageService.getLanguageData(language);
        try {
            model.put("words", generationService.generateWords(languageData, amount));

            model.put("username", authenticationService.getAuthenticatedUsername());
            model.put("languageName", languageData.getName());
            model.put("languageCreator", languageData.getUsername());
            model.put("originalEndpoint", "list-words");
        } catch (EntityNotFoundException e) {
            log.warn("Could not find a language with id={}", language);
            return "redirect:/";
        } catch (InvalidObjectException e) {
            log.warn("The constraints for the language with id={} are not valid to generate a word\n{}", language, e.getLocalizedMessage());
            return "redirect:/";
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getLocalizedMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getLocalizedMessage());
        }

        return "list-words";
    }

    @Transactional
    @GetMapping("/*")
    public String redirectWrongUrl()
    {
        log.info("redirectWrongUrl() called");

        return "redirect:/";
    }

    @Transactional
    @GetMapping("/")
    public String listLanguages(ModelMap model)
    {
        log.info("listLanguages() called");

        model.put("languages", languageService.getUserAndPublicLanguages());
        model.put("username", authenticationService.getAuthenticatedUsername());

        return "list-languages";
    }

    @Transactional
    @GetMapping("/language")
    public String editLanguage(ModelMap model, @RequestParam(value = "languageId", defaultValue = "-1", required = false) long languageId)
    {
        log.info("editLanguage() called");

        LanguageData languageData = languageService.getLanguageData(languageId);

        if (!languageData.getUsername().equals(authenticationService.getAuthenticatedUsername())) {
            log.info("User '{}' tried to edit language {}, to which he has no access to", authenticationService.getAuthenticatedUsername(), languageData.getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to edit the selected language");
        }

        model.put("languageData", languageData);

        return "language";
    }

    @Transactional
    @PostMapping("/language")
    public String addLanguage(@Valid LanguageData languageData)
    {
        log.info("addLanguage() called");

        try {
            languageService.addLanguage(languageData);
        } catch (InvalidObjectException e) {
            log.info("Could not save the language because it was not complete.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ("Could not save the language.\n" + e.getLocalizedMessage()).replace("\n", "<br />"));
        } catch (IllegalAccessException e) {
            log.info("User '{}' tried to update language {}, to which he has no access to", authenticationService.getAuthenticatedUsername(), languageData.getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getLocalizedMessage());
        }

        return "redirect:/";
    }

    @Transactional
    @GetMapping("/delete-language")
    public String deleteLanguage(@RequestParam(value = "languageId") long languageId)
    {
        log.info("deleteLanguage() called for languageId={}", languageId);

        try {
            languageService.deleteLanguage(languageId);
        } catch (IllegalAccessException e) {
            log.info("User '{}' tried to delete language {}, to which he has no access to", authenticationService.getAuthenticatedUsername(), languageId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getLocalizedMessage());
        }

        return "redirect:/";
    }
}
