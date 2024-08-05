package io.fi0x.languagegenerator.rest;

import io.fi0x.languagegenerator.db.entities.Word;
import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.logic.dto.WordDto;
import io.fi0x.languagegenerator.service.AuthenticationService;
import io.fi0x.languagegenerator.service.LanguageService;
import io.fi0x.languagegenerator.service.TranslationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes({"amount", "language", "languageCreator", "languageName", "username", "savedWords", "words"})
public class TranslationController
{
    private AuthenticationService authenticationService;
    private LanguageService languageService;
    private TranslationService translationService;

    @Transactional
    @PostMapping("/word")
    public String saveWord(ModelMap model, @RequestParam("listIndex") Integer listIndex, @RequestParam(value = "word") String word)
    {
        log.info("saveWord() called for word={} with listIndex={}", word, listIndex);

        Object object = model.get("words");
        if (object instanceof List<?> someList && someList.size() > listIndex && someList.get(listIndex) instanceof WordDto wordDto) {
            wordDto.setWord(word);

            String languageCreator = languageService.getLanguageCreator(wordDto.getLanguageId());
            try {
                translationService.saveOrGetWord(wordDto, languageCreator);
            } catch (IllegalAccessException e) {
                log.info("User '{}' tried to save word '{}' in a language, owned by '{}', which is not allowed", authenticationService.getAuthenticatedUsername(), word, languageCreator);
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to save translations in this language");
            }
        }

        return "list-words";
    }

    @Transactional
    @GetMapping("/word")
    public String showWord(ModelMap model, @RequestParam("languageId") long languageId, @RequestParam("word") String word)
    {
        log.info("showWord() called for word={} in language={}", word, languageId);

        WordDto wordDto = new WordDto(languageId, word);

        try {
            model.put("word", translationService.saveOrGetWord(wordDto, languageService.getLanguageCreator(languageId)));
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "To see translations for this word, it needs to be saved in the database, but you are not authorized to do so.");
        }

        model.put("username", authenticationService.getAuthenticatedUsername());
        model.put("originalLanguageData", languageService.getLanguageData(languageId));
        model.put("translations", languageService.addLanguageNameToWords(translationService.getTranslations(wordDto)));

        return "word";
    }

    @Transactional
    @GetMapping("/delete-word")
    public String deleteWord(ModelMap model, @RequestParam("languageId") long languageId, @RequestParam("wordNumber") long wordNumber)
    {
        log.info("deleteWord() called with languageId={} nad wordNumber={}", languageId, wordNumber);

        Word word = new Word();
        word.setLanguageId(languageId);
        word.setWordNumber(wordNumber);

        try {
            translationService.deleteWord(word, languageService.getLanguageCreator(languageId));
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not allowed to delete any saved words of this language");
        }

        //TODO: remove the deleted word from the UI
        return "dictionary";
    }

    @Transactional
    @GetMapping("/dictionary")
    public String showDictionary(ModelMap model, @RequestParam("languageId") long languageId)
    {
        log.info("showDictionary() called for languageId={}", languageId);

        LanguageData languageData = languageService.getLanguageData(languageId);
        model.put("languageName", languageData.getName());
        model.put("languageCreator", languageData.getUsername());
        model.put("savedWords", translationService.getAllWords(languageId));
        model.put("username", authenticationService.getAuthenticatedUsername());

        return "dictionary";
    }

    @Transactional
    @GetMapping("/delete-translation")
    public String deleteTranslation(ModelMap model, @RequestParam("languageId1") long languageId1, @RequestParam("wordNumber1") long wordNumber1, @RequestParam("languageId2") long languageId2, @RequestParam("wordNumber2") long wordNumber2)
    {
        log.info("deleteTranslation() called for languageId={}, wordNumber={} and languageId={}, wordNumber={}", languageId1, wordNumber1, languageId2, wordNumber2);

        //TODO: Implement
        return "redirect:/";
    }
}
