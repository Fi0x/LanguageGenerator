package io.fi0x.languagegenerator.rest;

import io.fi0x.languagegenerator.logic.dto.WordDto;
import io.fi0x.languagegenerator.service.TranslationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@AllArgsConstructor
public class TranslationController
{
    private TranslationService translationService;

    @Transactional
    @PostMapping("word")
    public String saveWord(ModelMap model, HttpServletRequest request, @Valid WordDto singleWord)
    {
        log.info("saveWord() called for word={}", singleWord);

        translationService.saveOrGetWord(singleWord);

        return "redirect:" + request.getHeader("Referer");
    }
}
