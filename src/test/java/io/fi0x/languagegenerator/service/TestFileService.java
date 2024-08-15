package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.logic.dto.LanguageData;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;

@RunWith(SpringRunner.class)
public class TestFileService
{
    private static final Long LANGUAGE_ID = 923487L;
    private static final String FILE_SUFFIX = ".json";
    private static final String USERNAME = "Olaf";

    MockedStatic<SecurityContextHolder> staticSecurityMoc;

    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private FileService service;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);

        staticSecurityMoc = mockStatic(SecurityContextHolder.class);
        staticSecurityMoc.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        doReturn(authentication).when(securityContext).getAuthentication();
        doReturn(USERNAME).when(authentication).getName();
    }

    @AfterEach
    void teardown()
    {
        staticSecurityMoc.close();
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
    void test_getLanguageFile_success() throws IOException, IllegalAccessException
    {
        File file = new File(LANGUAGE_ID + FILE_SUFFIX);
        Path path = file.toPath();
        MockedStatic<Files> staticMock = mockStatic(Files.class);
        staticMock.when(() -> Files.createTempFile(String.valueOf(LANGUAGE_ID), FILE_SUFFIX)).thenReturn(path);

        UrlResource expectedResult = new UrlResource(file.toURI());

        Assertions.assertEquals(expectedResult, service.getLanguageFile(LanguageData.builder().id(LANGUAGE_ID).specialCharacterChance(1D).username(USERNAME).build()));

        staticMock.close();
    }

    @Test
    @Tag("UnitTest")
    void test_getLanguageFile_unauthorized()
    {
        doReturn("Wrong user").when(authentication).getName();

        Assertions.assertThrows(IllegalAccessException.class, () -> service.getLanguageFile(LanguageData.builder().id(LANGUAGE_ID).specialCharacterChance(1D).username(USERNAME).build()));

    }

    @Test
    @Tag("UnitTest")
    void test_getLanguageFile_public() throws IOException, IllegalAccessException
    {
        File file = new File(LANGUAGE_ID + FILE_SUFFIX);
        Path path = file.toPath();
        MockedStatic<Files> staticMock = mockStatic(Files.class);
        staticMock.when(() -> Files.createTempFile(String.valueOf(LANGUAGE_ID), FILE_SUFFIX)).thenReturn(path);
        doReturn("Wrong user").when(authentication).getName();

        UrlResource expectedResult = new UrlResource(file.toURI());
        Assertions.assertEquals(expectedResult, service.getLanguageFile(LanguageData.builder().id(LANGUAGE_ID).specialCharacterChance(1D).username(USERNAME).visible(true).build()));

        staticMock.close();
    }
}
