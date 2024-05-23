package io.fi0x.languagegenerator.rest;

import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.service.GenerationService;
import io.fi0x.languagegenerator.service.LanguageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.io.InvalidObjectException;
import java.util.Objects;

@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes({"language", "amount", "words"})
public class LanguageController
{
    private GenerationService generationService;
    private LanguageService languageService;

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

        model.put("languageName", languageService.getLanguageData(language).getName());

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

        return "list-languages";
    }

    @Transactional
    @GetMapping("/language")
    public String editLanguage(ModelMap model, @RequestParam(value = "languageId", defaultValue = "-1", required = false) long languageId)
    {
        log.info("editLanguage() called");

        model.put("languageData", languageService.getLanguageData(languageId));

        return "language";
    }

    @Transactional
    @PostMapping("/language")
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
            return "redirect:language";
        }

        return "redirect:/";
    }

    @Transactional
    @GetMapping("/delete-language")
    public String deleteLanguage(ModelMap model, @RequestParam(value = "languageId") long languageId)
    {
        log.info("deleteLanguage() called for languageId={}", languageId);

        if(languageService.deleteLanguage(languageId))
            log.info("Language with id={} successfully deleted", languageId);
        else
            log.warn("Could not delete language with id={}", languageId);

        return "redirect:/";
    }
}
