package io.fi0x.languagegenerator.rest;

import io.fi0x.languagegenerator.db.entities.Word;
import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.logic.dto.WordDto;
import io.fi0x.languagegenerator.service.LanguageService;
import io.fi0x.languagegenerator.service.TranslationService;
import io.github.fi0x.util.components.Authenticator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes({"amount", "englishTranslations", "language", "languages", "languageCreator", "languageName", "originalEndpoint", "originalLanguageData", "username", "savedWords", "translations", "word", "words"})
public class TranslationController
{
    private Authenticator authenticator;
    private LanguageService languageService;
    private TranslationService translationService;

    @Transactional
    @GetMapping("/word")
    public String showWord(ModelMap model, @RequestParam("languageId") long languageId, @RequestParam("word") String word)
    {
        log.info("showWord() called for word={} in language={}", word, languageId);

        WordDto wordDto = new WordDto(languageId, word);

        try {
            model.put("word", translationService.saveOrGetWord(wordDto));
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "To see translations for this word, it needs to be saved in the database, but you are not authorized to do so.");
        }

        model.put("username", authenticator.getAuthenticatedUsername());
        model.put("originalLanguageData", languageService.getLanguageData(languageId));
        model.put("translations", languageService.addLanguageNameToWords(translationService.getTranslations(wordDto)));
        model.put("languages", languageService.getUserAndPublicLanguages());

        return "word";
    }

    @Transactional
    @GetMapping("/dictionary")
    public String showDictionary(ModelMap model, @RequestParam("languageId") long languageId)
    {
        log.info("showDictionary() called for languageId={}", languageId);

        LanguageData languageData;
        try {
            languageData = languageService.getAuthenticatedLanguageData(languageId);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this language");
        }

        model.put("languageName", languageData.getName());
        model.put("languageCreator", languageData.getUsername());
        List<Word> savedWords = translationService.getAllWords(languageId);
        model.put("savedWords", savedWords);
        model.put("englishTranslations", translationService.getEnglishTranslations(savedWords));
        model.put("username", authenticator.getAuthenticatedUsername());
        model.put("language", languageId);
        model.put("originalEndpoint", "dictionary");

        return "dictionary";
    }

    @ModelAttribute("savedWords")
    public List<Word> getInitializedSavedWords() {
        return new ArrayList<>();
    }
}
