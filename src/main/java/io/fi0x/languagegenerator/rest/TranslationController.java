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

            try {
                translationService.saveOrGetWord(wordDto);
            } catch (IllegalAccessException e) {
                log.info("User '{}' tried to save word '{}' in a language, which is not allowed", authenticationService.getAuthenticatedUsername(), word);
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
            model.put("word", translationService.saveOrGetWord(wordDto));
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
    public String deleteWord(@RequestParam("languageId") long languageId, @RequestParam("wordNumber") long wordNumber)
    {
        log.info("deleteWord() called with languageId={} nad wordNumber={}", languageId, wordNumber);

        Word word = new Word();
        word.setLanguageId(languageId);
        word.setWordNumber(wordNumber);

        try {
            translationService.deleteWord(word);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not allowed to delete any saved words of this language");
        }

        //TODO: remove the deleted word from the UI
        //TODO: Return to the correct page (where the call came from)
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
    @PostMapping("/translation")
    public String saveTranslation(@RequestParam("languageId") Integer language1, @RequestParam("word") String word1, @RequestParam("translationLanguageId") Integer language2, @RequestParam(value = "translationWord") String word2)
    {
        log.info("saveTranslation() called for languageId={}, word={}, language2={} and word2={}", language1, word1, language2, word2);

        //TODO: Verify this method works correctly
        try {
            translationService.linkWords(new WordDto(language1.longValue(), word1), new WordDto(language2.longValue(), word2));
        } catch (IllegalAccessException e) {
            log.info("User '{}' tried to save word '{}' in a language, which is not allowed", authenticationService.getAuthenticatedUsername(), word1);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to save translations in this language");
        }

        return "list-words";
    }

    @Transactional
    @GetMapping("/delete-translation")
    public String deleteTranslation(@RequestParam("languageId1") long languageId1, @RequestParam("wordNumber1") long wordNumber1, @RequestParam("languageId2") long languageId2, @RequestParam("wordNumber2") long wordNumber2)
    {
        log.info("deleteTranslation() called for languageId={}, wordNumber={} and languageId={}, wordNumber={}", languageId1, wordNumber1, languageId2, wordNumber2);

        try {
            translationService.deleteTranslation(languageId1, wordNumber1, languageId2, wordNumber2);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this translation. You need to own both languages in order to do so.");
        }

        return "redirect:/";
    }
}
