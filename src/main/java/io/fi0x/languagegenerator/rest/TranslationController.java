package io.fi0x.languagegenerator.rest;

import io.fi0x.languagegenerator.db.entities.Word;
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
@SessionAttributes({"words", "username"})
public class TranslationController
{

    //TODO: Move all verifications to services
    private AuthenticationService authenticationService;
    private LanguageService languageService;
    private TranslationService translationService;

    @Transactional
    @PostMapping("word")
    public String saveWord(ModelMap model, @RequestParam("listIndex") Integer listIndex, @RequestParam(value = "word") String word)
    {
        log.info("saveWord() called for word={} with listIndex={}", word, listIndex);

        Object object = model.get("words");
        if(object instanceof List<?> someList && someList.size() > listIndex && someList.get(listIndex) instanceof WordDto wordDto)
        {
            wordDto.setWord(word);

            String languageCreator = languageService.getLanguageCreator(wordDto.getLanguageId());
            String currentUser = authenticationService.getAuthenticatedUsername();

            if (languageCreator != null && languageCreator.equals(currentUser))
                translationService.saveOrGetWord(wordDto);
            else {
                log.info("User '{}' tried to save word '{}' in a language, owned by '{}', which is not allowed", currentUser, word, languageCreator);
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to save translations in this language");
            }
        }

        return "list-words";
    }

    @Transactional
    @GetMapping("word")
    public String showWord(ModelMap model, @RequestParam("languageId") long languageId, @RequestParam("word") String word)
    {
        log.info("showWord() called for word={} in language={}", word, languageId);

        String languageCreator = languageService.getLanguageCreator(languageId);
        String currentUser = authenticationService.getAuthenticatedUsername();

        WordDto wordDto = new WordDto(languageId, word);

        Word wordEntity = translationService.getSavedWord(wordDto);
        if(wordEntity == null)
        {
            if(languageCreator == null || !languageCreator.equals(currentUser))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "To see translations for this word, it needs to be saved in the database, but you are not authorized to do so.");

            wordEntity = translationService.saveOrGetWord(wordDto);
        }

        model.put("username", currentUser);
        model.put("word", wordEntity);
        model.put("originalLanguageData", languageService.getLanguageData(languageId));

        List<WordDto> translations = languageService.addLanguageNameToWords(translationService.getTranslations(wordDto));

        model.put("translations", translations);

        return "word";
    }
}
