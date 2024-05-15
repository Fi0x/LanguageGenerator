package io.fi0x.languagegenerator.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fi0x.languagegenerator.logic.dto.LanguageJson;
import io.fi0x.languagegenerator.service.LanguageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
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

    // TODO: Make this method work correctly
    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<Resource> downloadLanguageFile(@RequestParam(value = "languageId") long languageId)
    {
        //TODO: Move more of this into a service and fill the file with the correct content and name
        Resource file;
        try {
            File actualFile = new File("blaBlub");
            List<String> content = new ArrayList<>();
            content.add("Test Line");
            Files.write(actualFile.toPath(), content , StandardCharsets.UTF_8);
            file = new UrlResource(actualFile.toURI());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //TODO: Add null-check
        String languageName = "Placeholder";//TODO: Get the actual language name from the table
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + languageName + "\"").body(file);
    }
}
