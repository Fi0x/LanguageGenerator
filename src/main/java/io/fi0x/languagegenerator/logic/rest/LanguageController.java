package io.fi0x.languagegenerator.logic.rest;

import io.fi0x.languagegenerator.logic.rest.entities.Language;
import io.fi0x.languagegenerator.logic.service.GenerationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes("selectedLanguage")
public class LanguageController
{
    // TODO: Add user accounts that can be created for free and are stored in a local db

    private GenerationService generationService;

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
        log.info("generateWordFromLanguage() called");

        model.put("words", generationService.generateWords(language, amount));

        return "wordView";
    }

    @GetMapping("/generate")
    public String generateWords(ModelMap model, @RequestParam(value = "amount", defaultValue = "1", required = false) int amount)
    {
        log.info("generateWords() called");

        if (!model.containsKey("selectedLanguage"))
            model.put("selectedLanguage", "languagetemplate");

        return generateWordFromLanguage(model, (String) model.get("selectedLanguage"), amount);
    }

    @GetMapping("/list-languages")
    public String showPossibleLanguages(ModelMap model)
    {
        log.info("showPossibleLanguages() called");

        // TODO: Return a page, that shows all loaded languages

        return null;
    }

    @PostMapping("/set-language/{language}")
    public String setDefaultLanguage(ModelMap model, @PathVariable(value = "language") String language)
    {
        log.info("setDefaultLanguage() called");

        model.put("selectedLanguage", language);

        //TODO: Return to the previous page
        return null;
    }

    @GetMapping("/add-language")
    public String createLanguage(ModelMap model)
    {
        log.info("createLanguage() called");

        // TODO: Show a page, where a user can create a new language or edit an existing one

        return null;
    }

    @PostMapping("/add-language")
    public String addLanguage(ModelMap model, Language language, BindingResult result)
    {
        log.info("addLanguage() called");

        if(result.hasErrors())
        {
            // TODO: Return to the add-language GET site
            return null;
        }

        // TODO: Save the language

        return "redirect:list-languages";
    }
}
