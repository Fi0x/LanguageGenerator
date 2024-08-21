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

import java.util.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TranslationController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class TestTranslationController
{
    private static final String WORD_URL = "/word";
    private static final String DELETE_WORD_URL = "/delete-word";
    private static final String DICTIONARY_URL = "/dictionary";
    private static final String TRANSLATION_URL = "/translation";
    private static final String DELETE_TRANSLATION_URL = "/delete-translation";
    private static final String WORD = "bla";
    private static final String TRANSLATED_WORD = "blub";
    private static final Long WORD_NUMBER = 3L;
    private static final Long TRANSLATED_WORD_NUMBER = 12L;
    private static final long LANGUAGE_ID = 6;
    private static final long TRANSLATED_LANGUAGE_ID = 4;
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
    void test_saveWord_success_normalListIndex() throws Exception
    {
        doReturn(getValidWord(0).toEntity()).when(translationService).saveOrGetWord(any());

        mvc.perform(post(WORD_URL).param("listIndex", "1").param("word", WORD)
                        .sessionAttr("originalEndpoint", "list-words").sessionAttr("words", getWords(2)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/list-words.jsp"));
    }

    @Test
    @Tag("UnitTest")
    void test_saveWord_success_negativeListIndex() throws Exception
    {
        doReturn(getValidWord(0).toEntity()).when(translationService).saveOrGetWord(any());

        mvc.perform(post(WORD_URL).param("listIndex", "-1").param("word", WORD)
                        .sessionAttr("originalEndpoint", "list-words").sessionAttr("words", getWords(2)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/list-words.jsp"));
    }

    @Test
    @Tag("UnitTest")
    void test_saveWord_unauthorized() throws Exception
    {
        doThrow(IllegalAccessException.class).when(translationService).saveOrGetWord(any());

        mvc.perform(post(WORD_URL).param("listIndex", "1").param("word", WORD).sessionAttr("words", getWords(2)))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andExpect(forwardedUrl(null));

        mvc.perform(post(WORD_URL).param("listIndex", "-1").param("word", WORD).sessionAttr("words", getWords(2)))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andExpect(forwardedUrl(null));
    }

    @Test
    @Tag("UnitTest")
    void test_saveWord_wrongDto() throws Exception
    {
        mvc.perform(post(WORD_URL).param("listIndex", "1").param("word", WORD)
                        .sessionAttr("originalEndpoint", "list-words").sessionAttr("words", Collections.emptyList()))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/list-words.jsp"));

        List<WordDto> list = getWords(3);
        list.set(1, null);
        mvc.perform(post(WORD_URL).param("listIndex", "1").param("word", WORD)
                        .sessionAttr("originalEndpoint", "list-words").sessionAttr("words", Collections.emptyList()))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/list-words.jsp"));

        mvc.perform(post(WORD_URL).param("listIndex", "1").param("word", WORD)
                        .sessionAttr("originalEndpoint", "otherEndpoint").sessionAttr("words", new WordDto()))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/otherEndpoint.jsp"));

        List<Word> wrongListType = new ArrayList<>();
        wrongListType.add(new Word());
        mvc.perform(post(WORD_URL).param("listIndex", "0").param("word", WORD)
                        .sessionAttr("originalEndpoint", "").sessionAttr("words", wrongListType))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/.jsp"));
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
        doThrow(IllegalAccessException.class).when(translationService).saveOrGetWord(any());

        mvc.perform(get(WORD_URL).param("languageId", String.valueOf(LANGUAGE_ID)).param("word", WORD))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andExpect(forwardedUrl(null));
    }

    @Test
    @Tag("UnitTest")
    void test_deleteWord_success() throws Exception
    {
        doNothing().when(translationService).deleteWord(any());

        mvc.perform(get(DELETE_WORD_URL).param("languageId", String.valueOf(LANGUAGE_ID)).param("wordNumber", String.valueOf(WORD_NUMBER))
                        .sessionAttr("originalEndpoint", "dictionary"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/dictionary.jsp"));

        mvc.perform(get(DELETE_WORD_URL).param("languageId", String.valueOf(LANGUAGE_ID)).param("wordNumber", String.valueOf(WORD_NUMBER))
                        .sessionAttr("originalEndpoint", "list-words")
                        .sessionAttr("words", List.of(getValidWord(0))))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(model().attribute("words", List.of(getValidWord(0))))
                .andExpect(forwardedUrl("/WEB-INF/jsp/list-words.jsp"));
    }

    @Test
    @Tag("UnitTest")
    void test_deleteWord_unauthorized() throws Exception
    {
        doThrow(IllegalAccessException.class).when(translationService).deleteWord(any());

        mvc.perform(get(DELETE_WORD_URL).param("languageId", String.valueOf(LANGUAGE_ID)).param("wordNumber", String.valueOf(WORD_NUMBER))
                        .sessionAttr("originalEndpoint", "endpointToReturnTo"))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andExpect(forwardedUrl(null));
    }

    @Test
    @Tag("UnitTest")
    void test_deleteWord_noOriginalEndpoint() throws Exception
    {
        doNothing().when(translationService).deleteWord(any());

        mvc.perform(get(DELETE_WORD_URL).param("languageId", String.valueOf(LANGUAGE_ID)).param("wordNumber", String.valueOf(WORD_NUMBER)))
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(forwardedUrl(null));
    }

    @Test
    @Tag("UnitTest")
    void test_showDictionary_success() throws Exception
    {
        List<Word> wordList = getWordList();
        Map<Long, String> englishTranslations = getEnglishTranslations();
        doReturn(LanguageData.builder().id(LANGUAGE_ID).name(LANGUAGE_NAME).username(USERNAME).build()).when(languageService).getAuthenticatedLanguageData(eq(LANGUAGE_ID));
        doReturn(wordList).when(translationService).getAllWords(eq(LANGUAGE_ID));
        doReturn(englishTranslations).when(translationService).getEnglishTranslations(eq(wordList));
        doReturn(USERNAME).when(authenticationService).getAuthenticatedUsername();

        mvc.perform(get(DICTIONARY_URL).param("languageId", String.valueOf(LANGUAGE_ID)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(model().attribute("languageName", LANGUAGE_NAME))
                .andExpect(model().attribute("languageCreator", USERNAME))
                .andExpect(model().attribute("savedWords", wordList))
                .andExpect(model().attribute("englishTranslations", englishTranslations))
                .andExpect(model().attribute("username", USERNAME))
                .andExpect(model().attribute("language", LANGUAGE_ID))
                .andExpect(model().attribute("originalEndpoint", "dictionary"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/dictionary.jsp"));
    }

    @Test
    @Tag("UnitTest")
    void test_showDictionary_unauthorized() throws Exception
    {
        doThrow(IllegalAccessException.class).when(languageService).getAuthenticatedLanguageData(eq(LANGUAGE_ID));

        mvc.perform(get(DICTIONARY_URL).param("languageId", String.valueOf(LANGUAGE_ID)))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andExpect(forwardedUrl(null));
    }

    @Test
    @Tag("UnitTest")
    void test_saveTranslation_success() throws Exception
    {
        doReturn(getValidWordEntity()).when(translationService).linkWords(any(), any());
        doReturn(LanguageData.builder().build()).when(languageService).getLanguageData(eq(LANGUAGE_ID));

        mvc.perform(post(TRANSLATION_URL).param("languageId", String.valueOf(LANGUAGE_ID)).param("word", WORD)
                        .param("translationLanguageId", String.valueOf(TRANSLATED_LANGUAGE_ID)).param("translationWord", TRANSLATED_WORD)
                        .sessionAttr("translations", getTranslationsList()))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(model().attributeExists("translations"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/word.jsp"));
    }

    @Test
    @Tag("UnitTest")
    void test_saveTranslation_unauthorized() throws Exception
    {
        doThrow(IllegalAccessException.class).when(translationService).linkWords(any(), any());

        mvc.perform(post(TRANSLATION_URL).param("languageId", String.valueOf(LANGUAGE_ID)).param("word", WORD)
                        .param("translationLanguageId", String.valueOf(TRANSLATED_LANGUAGE_ID)).param("translationWord", TRANSLATED_WORD)
                        .sessionAttr("translations", getTranslationsList()))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andExpect(forwardedUrl(null));
    }

    @Test
    @Tag("UnitTest")
    void test_deleteTranslation_success() throws Exception
    {
        doNothing().when(translationService).deleteTranslation(eq(LANGUAGE_ID), eq(WORD_NUMBER), eq(TRANSLATED_LANGUAGE_ID), eq(TRANSLATED_WORD_NUMBER));

        mvc.perform(get(DELETE_TRANSLATION_URL).param("languageId1", String.valueOf(LANGUAGE_ID)).param("wordNumber1", String.valueOf(WORD_NUMBER))
                        .param("languageId2", String.valueOf(TRANSLATED_LANGUAGE_ID)).param("wordNumber2", String.valueOf(TRANSLATED_WORD_NUMBER)))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @Tag("UnitTest")
    void test_deleteTranslation_unauthorized() throws Exception
    {
        doThrow(IllegalAccessException.class).when(translationService).deleteTranslation(eq(LANGUAGE_ID), eq(WORD_NUMBER), eq(TRANSLATED_LANGUAGE_ID), eq(TRANSLATED_WORD_NUMBER));

        mvc.perform(get(DELETE_TRANSLATION_URL).param("languageId1", String.valueOf(LANGUAGE_ID)).param("wordNumber1", String.valueOf(WORD_NUMBER))
                        .param("languageId2", String.valueOf(TRANSLATED_LANGUAGE_ID)).param("wordNumber2", String.valueOf(TRANSLATED_WORD_NUMBER)))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andExpect(redirectedUrl(null));
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

    private List<Word> getWordList()
    {
        List<Word> list = new ArrayList<>();
        Word word = new Word();
        word.setLanguageId(LANGUAGE_ID);
        list.add(word);
        return list;
    }
    private Map<Long, String> getEnglishTranslations()
    {
        Map<Long, String> map = new HashMap<>();
        map.put(WORD_NUMBER, WORD);
        return map;
    }

    private List<WordDto> getTranslationsList()
    {
        List<WordDto> list = new ArrayList<>();
        list.add(new WordDto());
        return list;
    }

    private Word getValidWordEntity()
    {
        Word word = new Word();
        word.setLanguageId(LANGUAGE_ID);
        return word;
    }
}
