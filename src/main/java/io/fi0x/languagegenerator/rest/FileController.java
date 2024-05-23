package io.fi0x.languagegenerator.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.logic.dto.LanguageJson;
import io.fi0x.languagegenerator.service.FileService;
import io.fi0x.languagegenerator.service.LanguageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    FileService fileService;

    @Transactional
    @GetMapping("/upload")
    public String showUploadPage(ModelMap model)
    {
        log.info("showUploadPage() called");

        return "upload";
    }

    @Transactional
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

    @Transactional
    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<Resource> downloadLanguageFile(@RequestParam(value = "languageId") long languageId)
    {
        LanguageData languageData = languageService.getLanguageData(languageId);

        Resource resource;
        try {
            resource = fileService.getLanguageFile(languageData);
        } catch (IllegalArgumentException e) {
            log.warn("Could not generate language file for download, because languageData or id is null", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.warn("Could not write language with id={} to internal file, or file could not get converted to resource", languageData.getId(), e);
            throw new RuntimeException(e);
        }
        
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + languageData.getName() + ".json\"").body(resource);
    }
}
