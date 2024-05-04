package io.fi0x.languagegenerator.rest;

import io.fi0x.languagegenerator.logic.FileLoader;
import io.fi0x.languagegenerator.rest.entities.Language;
import io.fi0x.languagegenerator.service.GenerationService;
import io.fi0x.languagegenerator.service.LanguageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes("username")
public class LanguageController
{
    // TODO: Add user accounts that can be created for free and are stored in a local db

    private GenerationService generationService;
    private LanguageService languageService;

    /**
     * Generates random words in the provided language
     *
     * @param model    Internal use to display the webpage
     * @param language The desired language
     * @param amount   The amount of words that should get generated
     * @return A webpage that displays the requested words
     */
    @GetMapping("/generate/{language}")
    public String generateWordFromLanguage(ModelMap model, @PathVariable(value = "language") String language,
                                           @RequestParam(value = "amount", defaultValue = "1", required = false) int amount)
    {
        //TODO: remove this in the future and only use /generate with model-information
        log.info("generateWordFromLanguage() called");

        model.put("words", generationService.generateWords(language, amount));

        return "list-words";
    }

    @GetMapping("/generate")
    public String generateWords(ModelMap model, @RequestParam(value = "language", defaultValue = "language template") String language,
                                @RequestParam(value = "amount", defaultValue = "1", required = false) int amount)
    {
        log.info("generateWords() called with language={}, amount={}", language, amount);

        model.put("words", generationService.generateWords(language, amount));

        return "list-words";
    }

    @GetMapping("/list-languages")
    public String showPossibleLanguages(ModelMap model)
    {
        log.info("showPossibleLanguages() called");

        model.put("languages", FileLoader.getLoadedLanguageNames(false));

        // TODO: make the languages selectable to generate words in the selected language

        return "list-languages";
    }

    @GetMapping("/add-language")
    public String createLanguage(ModelMap model)
    {
        log.info("createLanguage() called");

        Language language = new Language();
        language.setId(0L);
        language.setName("");
        language.setUsername((String) model.get("username"));
        model.put("language", language);

        return "language";
    }

    @PostMapping("/add-language")
    public String addLanguage(ModelMap model, Language language)
    {
        log.info("addLanguage() called");

        languageService.addLanguage(language);

        return "redirect:list-languages";
    }
}
