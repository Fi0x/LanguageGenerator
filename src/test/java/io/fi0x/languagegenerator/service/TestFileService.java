package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.logic.dto.LanguageData;
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

        Assertions.assertEquals(expectedResult, service.getLanguageFile(LanguageData.builder().id(LANGUAGE_ID).build()));

        staticMock.close();
    }
}
