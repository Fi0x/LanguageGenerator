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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.util.Objects;

@Slf4j
@Controller
@AllArgsConstructor
@SessionAttributes({"language", "amount", "words", "username"})
public class FileController
{
    LanguageService languageService;
    FileService fileService;

    @Transactional
    @GetMapping("/upload")
    public String showUploadPage()
    {
        log.info("showUploadPage() called");

        return "upload";
    }

    @Transactional
    @PostMapping("/upload")
    public String uploadLanguage(@RequestParam("languageFile") MultipartFile multipartFile)
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

        } catch (IllegalArgumentException e) {
            log.warn("LanguageJson was malformed.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The provided language-file does not contain all of the required values");
        } catch (InvalidObjectException e) {
            log.warn("Could not save the language because it was not complete.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Language was not saved, because the language-object was not complete", e);
        } catch (IOException e) {
            log.warn("Could not create an InputStream of the uploaded file '{}'", multipartFile.getName(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong when uploading the file", e);
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A language with id=" + languageId + " could not be found", e);
        } catch (IOException e) {
            log.warn("Could not write language with id={} to internal file, or file could not get converted to resource", languageData.getId(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not write selected language to a downloadable file", e);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getLocalizedMessage());
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + languageData.getName() + ".json\"").body(resource);
    }
}
