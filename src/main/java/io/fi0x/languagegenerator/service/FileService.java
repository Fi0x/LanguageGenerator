package io.fi0x.languagegenerator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fi0x.languagegenerator.logic.converter.LanguageConverter;
import io.fi0x.languagegenerator.logic.dto.LanguageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@Service
public class FileService
{
    public Resource getLanguageFile(LanguageData languageData) throws IllegalArgumentException, IOException, IllegalAccessException
    {
        log.trace("getLanguageFile() called for languageData={}", languageData);

        if (languageData == null || languageData.getId() == null)
            throw new IllegalArgumentException("LanguageData must not be null");

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!currentUser.equals(languageData.getUsername()))
        {
            log.info("User '{}' tried to get language file with languageId={}, but does not have access to it", currentUser, languageData.getId());
            throw new IllegalAccessException("User is not allowed to download the selected language");
        }

        File actualFile = Files.createTempFile(languageData.getId().toString(), ".json").toFile();
        actualFile.deleteOnExit();
        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(actualFile, LanguageConverter.convertToJson(languageData));

        return new UrlResource(actualFile.toURI());
    }
}
