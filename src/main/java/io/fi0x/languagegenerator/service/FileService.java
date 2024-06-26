package io.fi0x.languagegenerator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fi0x.languagegenerator.logic.converter.LanguageConverter;
import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.logic.dto.LanguageJson;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class FileService
{
    public Resource getLanguageFile(LanguageData languageData) throws IllegalArgumentException, IOException
    {
        if (languageData == null || languageData.getId() == null)
            throw new IllegalArgumentException("LanguageData must not be null");

        File actualFile = Files.createTempFile(languageData.getId().toString(), ".json").toFile();
        actualFile.deleteOnExit();
        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(actualFile, LanguageConverter.convertToJson(languageData));

        return new UrlResource(actualFile.toURI());
    }

    public boolean isFileValid(LanguageJson languageJson)
    {
        if (languageJson.getNameLengths() == null || languageJson.getSpecialCharacterLengths() == null
                || languageJson.getSpecialCharacterChance() < 0 || languageJson.getSpecialCharacterChance() > 1)
        {
            return false;
        }

        return languageJson.getNameLengths().length == 2 && languageJson.getSpecialCharacterLengths().length == 4;
    }
}
