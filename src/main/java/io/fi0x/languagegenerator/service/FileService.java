package io.fi0x.languagegenerator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fi0x.languagegenerator.logic.converter.LanguageConverter;
import io.fi0x.languagegenerator.logic.dto.LanguageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class FileService
{
    public Resource getLanguageFile(LanguageData languageData) throws IllegalArgumentException, IOException
    {
        if(languageData == null || languageData.getId() == null)
            throw new IllegalArgumentException("LanguageData must not be null");

        File actualFile = new File(languageData.getId() + ".json");
        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(actualFile, LanguageConverter.convertToJson(languageData));

        return new UrlResource(actualFile.toURI());


    }
}
