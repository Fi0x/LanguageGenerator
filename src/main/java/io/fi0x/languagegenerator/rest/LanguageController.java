package io.fi0x.languagegenerator.rest;

import io.fi0x.languagegenerator.db.LanguageRepository;
import io.fi0x.languagegenerator.db.entities.Language;
import io.fi0x.languagegenerator.service.AuthenticationService;
import io.fi0x.languagegenerator.service.GenerationService;
import io.fi0x.languagegenerator.service.LanguageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@AllArgsConstructor
public class LanguageController
{
    // TODO: Add user accounts that can be created for free and are stored in a local db

    private AuthenticationService authenticationService;
    private GenerationService generationService;
    private LanguageService languageService;

    //TODO: Use id instead of language name
    @GetMapping("/generate")
    public String generateWords(ModelMap model, @RequestParam(value = "language", defaultValue = "language template") String language,
                                @RequestParam(value = "amount", defaultValue = "1", required = false) int amount)
    {
        log.info("generateWords() called with language={}, amount={}", language, amount);

        model.put("words", generationService.generateWords(language, amount));

        return "list-words";
    }

    @GetMapping("/")
    public String listLanguages(ModelMap model)
    {
        log.info("listLanguages() called");

        model.put("languages", languageService.getUserLanguages(authenticationService.getAuthenticatedUsername()));

        return "list-languages";
    }

    @GetMapping("/add-language")
    public String createLanguage(ModelMap model)
    {
        log.info("createLanguage() called");

        Language language = new Language();
        language.setName("");
        language.setUsername(authenticationService.getAuthenticatedUsername());
        model.put("language", language);

        return "language";
    }

    @PostMapping("/add-language")
    public String addLanguage(ModelMap model, Language language)
    {
        log.info("addLanguage() called");
        log.debug("arguments for addLanguage() call are model={}, language={}", model, language);

        languageService.addLanguage(language);

        return "redirect:/";
    }
}
