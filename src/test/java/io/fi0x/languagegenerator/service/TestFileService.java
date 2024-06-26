package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.logic.dto.LanguageJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.UrlResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.mockito.Mockito.mockStatic;

@RunWith(SpringRunner.class)
public class TestFileService
{
    private static final Long LANGUAGE_ID = 923487L;
    private static final String FILE_SUFFIX = ".json";

    @InjectMocks
    private FileService service;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Tag("UnitTest")
    void test_getLanguageFile_null()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.getLanguageFile(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.getLanguageFile(LanguageData.builder().build()));
    }

    @Test
    @Tag("UnitTest")
    void test_getLanguageFile_success() throws IOException
    {
        File file = new File(LANGUAGE_ID + FILE_SUFFIX);
        Path path = file.toPath();
        MockedStatic<Files> staticMock = mockStatic(Files.class);
        staticMock.when(() -> Files.createTempFile(String.valueOf(LANGUAGE_ID), FILE_SUFFIX)).thenReturn(path);
        UrlResource expectedResult = new UrlResource(file.toURI());

        Assertions.assertEquals(expectedResult, service.getLanguageFile(LanguageData.builder().id(LANGUAGE_ID).specialCharacterChance(1D).build()));

        staticMock.close();
    }

    @Test
    @Tag("UnitTest")
    void test_isFileValid_true()
    {
        Assertions.assertTrue(service.isFileValid(getValidJson()));
    }

    @Test
    @Tag("UnitTest")
    void test_isFileValid_false()
    {
        LanguageJson json1 = getValidJson();
        json1.setNameLengths(null);
        LanguageJson json2 = getValidJson();
        json2.setSpecialCharacterLengths(null);
        LanguageJson json3 = getValidJson();
        json3.setSpecialCharacterChance(-1);
        LanguageJson json4 = getValidJson();
        json4.setSpecialCharacterChance(2);
        LanguageJson json5 = getValidJson();
        json5.setNameLengths(new int[]{0});
        LanguageJson json6 = getValidJson();
        json6.setSpecialCharacterLengths(new int[]{0});

        Assertions.assertFalse(service.isFileValid(json1));
        Assertions.assertFalse(service.isFileValid(json2));
        Assertions.assertFalse(service.isFileValid(json3));
        Assertions.assertFalse(service.isFileValid(json4));
        Assertions.assertFalse(service.isFileValid(json5));
        Assertions.assertFalse(service.isFileValid(json6));
    }

    private LanguageJson getValidJson()
    {
        LanguageJson languageJson = new LanguageJson();

        languageJson.setNameLengths(new int[]{0, 0});
        languageJson.setSpecialCharacterLengths(new int[]{0, 0, 0, 0});
        languageJson.setSpecialCharacterChance(0);
        languageJson.setVocals(new ArrayList<>());
        languageJson.setConsonants(new ArrayList<>());
        languageJson.setVocalConsonant(new ArrayList<>());
        languageJson.setConsonantVocals(new ArrayList<>());
        languageJson.setForbiddenCombinations(new ArrayList<>());
        languageJson.setSpecialCharacters(new ArrayList<>());
        languageJson.setStartingCombinations(new ArrayList<>());
        languageJson.setEndingCombinations(new ArrayList<>());

        return languageJson;
    }
}
