package io.fi0x.languagegenerator.rest;

import io.fi0x.languagegenerator.logic.dto.WordDto;
import io.fi0x.languagegenerator.service.AuthenticationService;
import io.fi0x.languagegenerator.service.LanguageService;
import io.fi0x.languagegenerator.service.TranslationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Controller
@AllArgsConstructor
public class TranslationController
{
    private AuthenticationService authenticationService;
    private LanguageService languageService;
    private TranslationService translationService;

    @Transactional
    @PostMapping("word")
    public String saveWord(HttpServletRequest request, @RequestParam(value = "languageId") long languageId, @RequestParam(value = "word") String word)
    {
        log.info("saveWord() called for word={} with languageId={}", word, languageId);

        String languageCreator = languageService.getLanguageCreator(languageId);
        String currentUser = authenticationService.getAuthenticatedUsername();

        if(languageCreator != null && languageCreator.equals(currentUser))
            translationService.saveOrGetWord(new WordDto(languageId, word));
        else
        {
            log.info("User '{}' tried to save word '{}' in a language, owned by '{}', which is not allowed", currentUser, word, languageCreator);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to save translations in this language");
        }

        return "list-words";
    }
}
