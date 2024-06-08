package io.fi0x.languagegenerator.rest;

import io.fi0x.languagegenerator.db.entities.Language;
import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.logic.dto.Word;
import io.fi0x.languagegenerator.service.AuthenticationService;
import io.fi0x.languagegenerator.service.GenerationService;
import io.fi0x.languagegenerator.service.LanguageService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = LanguageController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class TestLanguageController
{
    private static final String GENERATE_URL = "/generate";
    private static final String WRONG_URL = "/dfghdölkjh";
    private static final String DEFAULT_URL = "/";
    private static final String LANGUAGE_URL = "/language";
    private static final String DELETE_LANGUAGE_URL = "/delete-language";
    private static final String USERNAME = "Mampfred";
    private static final Long LANGUAGE_ID = 2345L;
    private static final int WORD_AMOUNT = 13;
    private static final String LANGUAGE_NAME = "Klingon";
    private static final Long FORBIDDEN_LANGUAGE = 498L;

    @MockBean
    private GenerationService generationService;
    @MockBean
    private LanguageService languageService;
    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);

        doReturn(USERNAME).when(authenticationService).getAuthenticatedUsername();
        doReturn(getLanguageData()).when(languageService).getLanguageData(eq(LANGUAGE_ID));
    }

    @Test
    @Tag("UnitTest")
    void test_generateWords_success() throws Exception
    {
        List<Word> wordList = getWordList();
        doReturn(wordList).when(generationService).generateWords(eq(LANGUAGE_ID), eq(WORD_AMOUNT));

        mvc.perform(get(GENERATE_URL).param("language", String.valueOf(LANGUAGE_ID)).param("amount", String.valueOf(WORD_AMOUNT)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(model().attribute("language", LANGUAGE_ID))
                .andExpect(model().attribute("amount", WORD_AMOUNT))
                .andExpect(model().attribute("languageName", LANGUAGE_NAME))
                .andExpect(model().attribute("words", wordList))
                .andExpect(forwardedUrl("/WEB-INF/jsp/list-words.jsp"));
    }

    @Test
    @Tag("UnitTest")
    void test_generateWords_success_noAmount() throws Exception
    {
        List<Word> wordList = getWordList();
        doReturn(wordList).when(generationService).generateWords(eq(LANGUAGE_ID), eq(10));

        mvc.perform(get(GENERATE_URL).param("language", String.valueOf(LANGUAGE_ID)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(model().attribute("language", LANGUAGE_ID))
                .andExpect(model().attributeDoesNotExist("amount"))
                .andExpect(model().attribute("languageName", LANGUAGE_NAME))
                .andExpect(model().attribute("words", wordList))
                .andExpect(forwardedUrl("/WEB-INF/jsp/list-words.jsp"));
    }

    @Test
    @Tag("UnitTest")
    void test_generateWords_success_noLanguage() throws Exception
    {
        List<Word> wordList = getWordList();
        doReturn(wordList).when(generationService).generateWords(eq(0L), eq(WORD_AMOUNT));
        doReturn(getLanguageData()).when(languageService).getLanguageData(eq(0L));

        mvc.perform(get(GENERATE_URL).param("amount", String.valueOf(WORD_AMOUNT)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(model().attributeDoesNotExist("language"))
                .andExpect(model().attribute("amount", WORD_AMOUNT))
                .andExpect(model().attribute("languageName", LANGUAGE_NAME))
                .andExpect(model().attribute("words", wordList))
                .andExpect(forwardedUrl("/WEB-INF/jsp/list-words.jsp"));
    }

    @Test
    @Tag("UnitTest")
    void test_generateWords_success_wrongUser() throws Exception
    {
        List<Word> wordList = getWordList();
        doReturn(wordList).when(generationService).generateWords(eq(LANGUAGE_ID), eq(WORD_AMOUNT));
        LanguageData languageData = getLanguageData();
        languageData.setUsername("Wrong User");
        languageData.setVisible(true);
        doReturn(languageData).when(languageService).getLanguageData(eq(LANGUAGE_ID));

        mvc.perform(get(GENERATE_URL).param("language", String.valueOf(LANGUAGE_ID)).param("amount", String.valueOf(WORD_AMOUNT)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(model().attribute("language", LANGUAGE_ID))
                .andExpect(model().attribute("amount", WORD_AMOUNT))
                .andExpect(model().attribute("languageName", LANGUAGE_NAME))
                .andExpect(model().attribute("words", wordList))
                .andExpect(forwardedUrl("/WEB-INF/jsp/list-words.jsp"));
    }

    @Test
    @Tag("UnitTest")
    void test_generateWords_forbidden() throws Exception
    {
        LanguageData languageData = getLanguageData();
        languageData.setUsername("Wrong User");
        doReturn(languageData).when(languageService).getLanguageData(eq(FORBIDDEN_LANGUAGE));

        mvc.perform(get(GENERATE_URL).param("language", String.valueOf(FORBIDDEN_LANGUAGE)).param("amount", String.valueOf(WORD_AMOUNT)))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @Tag("UnitTest")
    void test_generateWords_noLanguage() throws Exception
    {
        doThrow(EntityNotFoundException.class).when(generationService).generateWords(eq(LANGUAGE_ID), eq(WORD_AMOUNT));

        mvc.perform(get(GENERATE_URL).param("language", String.valueOf(LANGUAGE_ID)).param("amount", String.valueOf(WORD_AMOUNT)))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(model().attributeDoesNotExist("language"))
                .andExpect(model().attributeDoesNotExist("amount"))
                .andExpect(model().attributeDoesNotExist("languageName"))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @Tag("UnitTest")
    void test_generateWords_invalidLanguage() throws Exception
    {
        doThrow(InvalidObjectException.class).when(generationService).generateWords(eq(LANGUAGE_ID), eq(WORD_AMOUNT));

        mvc.perform(get(GENERATE_URL).param("language", String.valueOf(LANGUAGE_ID)).param("amount", String.valueOf(WORD_AMOUNT)))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(model().attributeDoesNotExist("language"))
                .andExpect(model().attributeDoesNotExist("amount"))
                .andExpect(model().attributeDoesNotExist("languageName"))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @Tag("UnitTest")
    void test_redirectWrongUrl() throws Exception
    {
        mvc.perform(get(WRONG_URL))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @Tag("UnitTest")
    void test_listLanguages() throws Exception
    {
        List<Language> languages = getLanguages();
        doReturn(languages).when(languageService).getUserAndPublicLanguages();

        mvc.perform(get(DEFAULT_URL))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(model().attribute("languages", languages))
                .andExpect(model().attribute("username", USERNAME))
                .andExpect(forwardedUrl("/WEB-INF/jsp/list-languages.jsp"));
    }

    @Test
    @Tag("UnitTest")
    void test_editLanguage_success() throws Exception
    {
        LanguageData languageData = getLanguageData();
        doReturn(languageData).when(languageService).getLanguageData(eq(LANGUAGE_ID));

        mvc.perform(get(LANGUAGE_URL).param("languageId", String.valueOf(LANGUAGE_ID)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(model().attribute("languageData", languageData))
                .andExpect(forwardedUrl("/WEB-INF/jsp/language.jsp"));
    }

    @Test
    @Tag("UnitTest")
    void test_editLanguage_forbidden() throws Exception
    {
        doReturn("Wrong User").when(authenticationService).getAuthenticatedUsername();

        mvc.perform(get(LANGUAGE_URL).param("languageId", String.valueOf(LANGUAGE_ID)))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    //TODO: Get this test working and add variants
//    @Test
//    @Tag("UnitTest")
//    void test_addLanguage_success() throws Exception
//    {
//        ObjectMapper mapper = new ObjectMapper();
//        String content = mapper.writeValueAsString(getLanguageData());
//
//        mvc.perform(post(LANGUAGE_URL).contentType(MediaType.APPLICATION_JSON).content(content))
//                .andExpect(status().is(HttpStatus.OK.value()))
//                .andExpect(redirectedUrl("/"));
//    }

    @Test
    @Tag("UnitTest")
    void test_deleteLanguage_success() throws Exception
    {
        doReturn(USERNAME).when(languageService).getLanguageCreator(eq(LANGUAGE_ID));
        doReturn(true).when(languageService).deleteLanguage(eq(LANGUAGE_ID));

        mvc.perform(get(DELETE_LANGUAGE_URL).param("languageId", String.valueOf(LANGUAGE_ID)))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @Tag("UnitTest")
    void test_deleteLanguage_forbidden() throws Exception
    {
        doReturn("Wrong User").when(languageService).getLanguageCreator(eq(LANGUAGE_ID));

        mvc.perform(get(DELETE_LANGUAGE_URL).param("languageId", String.valueOf(LANGUAGE_ID)))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @Tag("UnitTest")
    void test_deleteLanguage_notFound() throws Exception
    {
        doReturn(USERNAME).when(languageService).getLanguageCreator(eq(LANGUAGE_ID));
        doReturn(false).when(languageService).deleteLanguage(eq(LANGUAGE_ID));

        mvc.perform(get(DELETE_LANGUAGE_URL).param("languageId", String.valueOf(LANGUAGE_ID)))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    private LanguageData getLanguageData()
    {
        return LanguageData.builder().username(USERNAME).name(LANGUAGE_NAME).visible(false).build();
    }

    private List<Word> getWordList()
    {
        List<Word> list = new ArrayList<>();
        list.add(new Word(LANGUAGE_ID, "sdfalökjh"));
        list.add(new Word(LANGUAGE_ID, "sghffghjghjjgh"));
        list.add(new Word(LANGUAGE_ID, "d   dfg  dsf sdf"));
        return list;
    }

    private List<Language> getLanguages()
    {
        List<Language> languages = new ArrayList<>();
        for (long i = 0; i < 3; i++)
        {
            Language language = new Language();
            language.setId(i);
            languages.add(language);
        }
        return languages;
    }
}
