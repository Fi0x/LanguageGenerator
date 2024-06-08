package io.fi0x.languagegenerator.rest;

import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.service.AuthenticationService;
import io.fi0x.languagegenerator.service.FileService;
import io.fi0x.languagegenerator.service.LanguageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.MalformedURLException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FileController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class TestFileController
{
    private static final String UPLOAD_URL = "/upload";
    private static final String DOWNLOAD_URL = "/download";
    private static final Long LANGUAGE_ID = 3L;
    private static final String USERNAME = "Otto";
    private static final String INVALID_USERNAME = "Fake-Otto";
    private static final String LANGUAGE_NAME = "Chinesisch";

    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private LanguageService languageService;
    @MockBean
    private FileService fileService;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Tag("UnitTest")
    void test_showError() throws Exception
    {
        mvc.perform(get(UPLOAD_URL)).andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/upload.jsp"));
    }

    @Test
    @Tag("UnitTest")
    void test_uploadLanguage_success() throws Exception
    {
        File realFile = ResourceUtils.getFile("classpath:testLanguageValid.json");
        InputStream content = new FileInputStream(realFile);
        MockMultipartFile file = new MockMultipartFile("languageFile", "filename.txt", "application/json", content);
        doNothing().when(languageService).addLanguage(any(), anyString(), eq(false));

        mvc.perform(MockMvcRequestBuilders.multipart(UPLOAD_URL).file(file))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @Tag("UnitTest")
    void test_uploadLanguage_400() throws Exception
    {
        File realFile = ResourceUtils.getFile("classpath:testLanguageValid.json");
        InputStream content = new FileInputStream(realFile);
        MockMultipartFile file = new MockMultipartFile("languageFile", "filename.txt", "application/json", content);
        doThrow(InvalidObjectException.class).when(languageService).addLanguage(any(), anyString(), eq(false));

        mvc.perform(MockMvcRequestBuilders.multipart(UPLOAD_URL).file(file))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(forwardedUrl(null));
    }

    @Test
    @Tag("UnitTest")
    void test_uploadLanguage_500() throws Exception
    {
        MockMultipartFile file = new MockMultipartFile("languageFile", "filename.txt", "application/json", "invalid content".getBytes());

        mvc.perform(MockMvcRequestBuilders.multipart(UPLOAD_URL).file(file))
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(forwardedUrl(null));
    }

    @Test
    @Tag("UnitTest")
    void test_downloadLanguageFile_success() throws Exception
    {
        LanguageData languageData = getLanguageData();
        doReturn(languageData).when(languageService).getLanguageData(eq(LANGUAGE_ID));
        doReturn(USERNAME).when(authenticationService).getAuthenticatedUsername();
        doReturn(getFileResource()).when(fileService).getLanguageFile(eq(languageData));

        mvc.perform(get(DOWNLOAD_URL).param("languageId", String.valueOf(LANGUAGE_ID)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + LANGUAGE_NAME + ".json\""))
                .andExpect(content().string("{}"));
    }

    @Test
    @Tag("UnitTest")
    void test_downloadLanguageFile_forbidden() throws Exception
    {
        LanguageData languageData = getLanguageData();
        doReturn(languageData).when(languageService).getLanguageData(eq(LANGUAGE_ID));
        doReturn(INVALID_USERNAME).when(authenticationService).getAuthenticatedUsername();

        mvc.perform(get(DOWNLOAD_URL).param("languageId", String.valueOf(LANGUAGE_ID)))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @Tag("UnitTest")
    void test_downloadLanguageFile_notFound() throws Exception
    {
        LanguageData languageData = getLanguageData();
        doReturn(languageData).when(languageService).getLanguageData(eq(LANGUAGE_ID));
        doReturn(USERNAME).when(authenticationService).getAuthenticatedUsername();
        doThrow(IllegalArgumentException.class).when(fileService).getLanguageFile(eq(languageData));

        mvc.perform(get(DOWNLOAD_URL).param("languageId", String.valueOf(LANGUAGE_ID)))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    @Tag("UnitTest")
    void test_downloadLanguageFile_500() throws Exception
    {
        LanguageData languageData = getLanguageData();
        doReturn(languageData).when(languageService).getLanguageData(eq(LANGUAGE_ID));
        doReturn(USERNAME).when(authenticationService).getAuthenticatedUsername();
        doThrow(IOException.class).when(fileService).getLanguageFile(eq(languageData));

        mvc.perform(get(DOWNLOAD_URL).param("languageId", String.valueOf(LANGUAGE_ID)))
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    private LanguageData getLanguageData()
    {
        return LanguageData.builder().username(USERNAME).name(LANGUAGE_NAME).build();
    }
    private Resource getFileResource() throws MalformedURLException, FileNotFoundException
    {
        File file = ResourceUtils.getFile("classpath:emptyFile.json");
        return new UrlResource(file.toURI());
    }
}
