package io.fi0x.languagegenerator.rest;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes({"words"})
public class TranslationController
{
    private AuthenticationService authenticationService;
    private LanguageService languageService;
    private TranslationService translationService;

    @Transactional
    @PostMapping("word")
    public String saveWord(ModelMap model, @RequestParam("listIndex") Integer listIndex, @RequestParam(value = "languageId") long languageId, @RequestParam(value = "word") String word)
    {
        log.info("saveWord() called for word={} with languageId={}", word, languageId);

        String languageCreator = languageService.getLanguageCreator(languageId);
        String currentUser = authenticationService.getAuthenticatedUsername();

        if (languageCreator != null && languageCreator.equals(currentUser))
            translationService.saveOrGetWord(new WordDto(languageId, word));
        else {
            log.info("User '{}' tried to save word '{}' in a language, owned by '{}', which is not allowed", currentUser, word, languageCreator);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to save translations in this language");
        }

        Object object = model.get("words");
        if(object instanceof List<?> someList && someList.size() > listIndex && someList.get(listIndex) instanceof WordDto wordDto)
            wordDto.setWord(word);

        return "list-words";
    }
}
