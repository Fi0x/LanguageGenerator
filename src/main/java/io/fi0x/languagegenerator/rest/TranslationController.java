package io.fi0x.languagegenerator.rest;

import io.fi0x.languagegenerator.db.entities.Word;
import io.fi0x.languagegenerator.logic.converter.WordConverter;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes({"amount", "englishTranslations", "language", "languages", "languageCreator", "languageName", "originalEndpoint", "originalLanguageData", "username", "savedWords", "translations", "word", "words"})
public class TranslationController
{
    private AuthenticationService authenticationService;
    private LanguageService languageService;
    private TranslationService translationService;

    @Transactional
    @PostMapping("/word")
    public String saveWord(ModelMap model, @RequestParam("listIndex") Integer listIndex, @RequestParam(value = "word") String word, @ModelAttribute("savedWords") List<Word> savedWords)
    {
        log.info("saveWord() called for word={} with listIndex={} and savedWords={}", word, listIndex, savedWords);

        if (listIndex == -1) {
            WordDto wordDto = new WordDto();
            wordDto.setLanguageId((Long) model.get("language"));
            wordDto.setWord(word);
            try {
                savedWords.add(translationService.saveOrGetWord(wordDto));
            } catch (IllegalAccessException e) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to save words in this language, only the owner can");
            }
        } else {
            Object object = model.get("words");
            if (object instanceof List<?> someList && listIndex >= 0 && someList.size() > listIndex && someList.get(listIndex) instanceof WordDto wordDto) {
                wordDto.setWord(word);

                try {
                    Word savedWord = translationService.saveOrGetWord(wordDto);
                    wordDto.setWordNumber(savedWord.getWordNumber());
                } catch (IllegalAccessException e) {
                    log.info("User '{}' tried to save word '{}' in a language, which is not allowed", authenticationService.getAuthenticatedUsername(), word);
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to save translations in this language");
                }
            }
        }

        return (String) model.getAttribute("originalEndpoint");
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
        model.put("languages", languageService.getUserAndPublicLanguages());

        return "word";
    }

    @Transactional
    @GetMapping("/delete-word")
    public String deleteWord(ModelMap model, @RequestParam("languageId") long languageId, @RequestParam("wordNumber") long wordNumber)
    {
        log.info("deleteWord() called with languageId={} and wordNumber={}", languageId, wordNumber);

        Word word = new Word();
        word.setLanguageId(languageId);
        word.setWordNumber(wordNumber);

        try {
            translationService.deleteWord(word);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not allowed to delete any saved words of this language");
        }

        String originalEndpoint = (String) model.get("originalEndpoint");
        if (originalEndpoint == null)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No return page found for delete-word operation");

        //TODO: Fix unchecked casts
        if (originalEndpoint.equals("dictionary")) {
            List<Word> wordList = ((List<Word>) model.get("savedWords")).stream()
                    .filter(word1 -> word1.getLanguageId() != languageId || word1.getWordNumber() == null || word1.getWordNumber() != wordNumber).toList();
            model.put("savedWords", wordList);
        } else if (originalEndpoint.equals("list-words")) {
            List<WordDto> wordList = ((List<WordDto>) model.get("words")).stream()
                    .peek(word1 -> {
                        if (word1.getLanguageId() == languageId && word1.getWordNumber() != null && word1.getWordNumber() == wordNumber)
                            word1.setSavedInDb(false);
                    }).toList();
            model.put("words", wordList);
        }

        return originalEndpoint;
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
        model.put("username", authenticationService.getAuthenticatedUsername());
        model.put("language", languageId);
        model.put("originalEndpoint", "dictionary");

        return "dictionary";
    }

    @Transactional
    @PostMapping("/translation")
    public String saveTranslation(ModelMap model, @RequestParam("languageId") Integer language1, @RequestParam("word") String word1, @RequestParam("translationLanguageId") Integer language2, @RequestParam(value = "translationWord") String word2)
    {
        log.info("saveTranslation() called for languageId={}, word={}, language2={} and word2={}", language1, word1, language2, word2);

        try {
            Word newWord = translationService.linkWords(new WordDto(language1.longValue(), word1), new WordDto(language2.longValue(), word2));

            Object translationsObject = model.get("translations");
            if (translationsObject instanceof List<?> translationsList && (translationsList.isEmpty() || translationsList.get(0) instanceof WordDto)) {
                List<WordDto> dtoList = translationsList.stream().map(object -> (WordDto) object).collect(Collectors.toList());
                WordDto newWordDto = WordConverter.convertToDto(newWord);
                newWordDto.setLanguageName(languageService.getLanguageData(newWordDto.getLanguageId()).getName());
                dtoList.add(newWordDto);
                model.put("translations", dtoList);
            }
        } catch (IllegalAccessException e) {
            log.info("User '{}' tried to save word '{}' in a language, which is not allowed", authenticationService.getAuthenticatedUsername(), word1);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to save translations in this language");
        }

        return "word";
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

    @ModelAttribute("savedWords")
    public List<Word> getInitializedSavedWords() {
        return new ArrayList<>();
    }
}
