package io.fi0x.languagegenerator.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fi0x.languagegenerator.logic.dto.LanguageJson;
import io.fi0x.languagegenerator.service.LanguageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.util.Objects;

@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes({"language", "amount", "words"})
public class FileController
{
    LanguageService languageService;

    @GetMapping("/upload")
    public String showUploadPage(ModelMap model)
    {
        log.info("showUploadPage() called");

        return "upload";
    }

    @PostMapping("/upload")
    public String uploadLanguage(ModelMap model, @RequestParam("languageFile") MultipartFile multipartFile)
    {
        log.info("uploadLanguage() called");

        try {
            InputStream is = multipartFile.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            LanguageJson languageJson = mapper.readValue(is, LanguageJson.class);
            languageService.addLanguage(
                    languageJson,
                    Objects.requireNonNullElse(multipartFile.getOriginalFilename(), "New Language").split("\\.")[0],
                    false);

        } catch (InvalidObjectException e) {
            log.warn("Could not save the language because it was not complete.");
        } catch (IOException e) {
            log.warn("Could not create an InputStream of the uploaded file '{}'", multipartFile.getName(), e);
        }
        return "redirect:/";
    }
}
