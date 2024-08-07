package io.fi0x.languagegenerator.rest;


import io.fi0x.languagegenerator.db.entities.Word;
import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.logic.dto.WordDto;
import io.fi0x.languagegenerator.service.AuthenticationService;
import io.fi0x.languagegenerator.service.LanguageService;
import io.fi0x.languagegenerator.service.TranslationService;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TranslationController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class TestTranslationController
{
    private static final String WORD_URL = "/word";
    private static final String WORD = "bla";
    private static final Long WORD_NUMBER = 3L;
    private static final long LANGUAGE_ID = 3;
    private static final String LANGUAGE_NAME = "Test language";
    private static final boolean SAVED = false;
    private static final String USERNAME = "Heinrich";

    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private LanguageService languageService;
    @MockBean
    private TranslationService translationService;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Tag("UnitTest")
    void test_saveWord_success() throws Exception
    {
        doReturn(USERNAME).when(languageService).getLanguageCreator(eq(LANGUAGE_ID));

        mvc.perform(post(WORD_URL).param("listIndex", "1").param("word", WORD).sessionAttr("words", getWords(2)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/list-words.jsp"));
    }

    @Test
    @Tag("UnitTest")
    void test_saveWord_unauthorized() throws Exception
    {
        doReturn(USERNAME).when(languageService).getLanguageCreator(eq(LANGUAGE_ID));
        doThrow(IllegalAccessException.class).when(translationService).saveOrGetWord(any());

        mvc.perform(post(WORD_URL).param("listIndex", "1").param("word", WORD).sessionAttr("words", getWords(2)))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andExpect(forwardedUrl(null));
    }

    @Test
    @Tag("UnitTest")
    void test_saveWord_wrongDto() throws Exception
    {
        mvc.perform(post(WORD_URL).param("listIndex", "1").param("word", WORD).sessionAttr("words", Collections.emptyList()))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/list-words.jsp"));

        List<WordDto> list = getWords(3);
        list.set(1, null);
        mvc.perform(post(WORD_URL).param("listIndex", "1").param("word", WORD).sessionAttr("words", Collections.emptyList()))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/list-words.jsp"));

        mvc.perform(post(WORD_URL).param("listIndex", "1").param("word", WORD).sessionAttr("words", new WordDto()))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/list-words.jsp"));

        List<Word> wrongListType = new ArrayList<>();
        wrongListType.add(new Word());
        mvc.perform(post(WORD_URL).param("listIndex", "0").param("word", WORD).sessionAttr("words", wrongListType))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/list-words.jsp"));
    }

    @Test
    @Tag("UnitTest")
    void test_showWord_success() throws Exception
    {
        doReturn(USERNAME).when(languageService).getLanguageCreator(eq(LANGUAGE_ID));
        doReturn(new Word()).when(translationService).saveOrGetWord(any());
        doReturn(USERNAME).when(authenticationService).getAuthenticatedUsername();
        doReturn(LanguageData.builder().build()).when(languageService).getLanguageData(eq(LANGUAGE_ID));
        doReturn(Collections.emptyList()).when(languageService).addLanguageNameToWords(any());
        doReturn(Collections.emptyList()).when(translationService).getTranslations(any());

        mvc.perform(get(WORD_URL).param("languageId", String.valueOf(LANGUAGE_ID)).param("word", WORD))
                .andExpect(model().attributeExists("word"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("originalLanguageData"))
                .andExpect(model().attributeExists("translations"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/word.jsp"));
    }

    @Test
    @Tag("UnitTest")
    void test_showWord_unauthorized() throws Exception
    {
        doReturn(USERNAME).when(languageService).getLanguageCreator(eq(LANGUAGE_ID));
        doThrow(IllegalAccessException.class).when(translationService).saveOrGetWord(any());

        mvc.perform(get(WORD_URL).param("languageId", String.valueOf(LANGUAGE_ID)).param("word", WORD))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andExpect(forwardedUrl(null));
    }

    private List<WordDto> getWords(int listSize)
    {
        List<WordDto> wordList = new ArrayList<>();

        for (int i = 0; i < listSize - 1; i++)
            wordList.add(new WordDto());
        wordList.add(getValidWord(listSize - 1));

        return wordList;
    }
    private WordDto getValidWord(int listIdx)
    {
        return new WordDto(LANGUAGE_ID, LANGUAGE_NAME, WORD_NUMBER, WORD, listIdx, SAVED);
    }
}
