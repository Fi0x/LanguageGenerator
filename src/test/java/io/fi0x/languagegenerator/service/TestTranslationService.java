package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.db.TranslationRepository;
import io.fi0x.languagegenerator.db.WordRepository;
import io.fi0x.languagegenerator.db.entities.Translation;
import io.fi0x.languagegenerator.db.entities.Word;
import io.fi0x.languagegenerator.logic.dto.WordDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TestTranslationService
{
    private static final Long LANGUAGE_ID1 = 132452343L;
    private static final Long LANGUAGE_ID2 = 1324523433L;
    private static final Long LANGUAGE_ID3 = 1112333L;
    private static final Long WORD_NUMBER11 = 123L;
    private static final Long WORD_NUMBER21 = 1234L;
    private static final Long WORD_NUMBER22 = 1233L;
    private static final Long WORD_NUMBER23 = 1243L;
    private static final Long WORD_NUMBER31 = 1243L;
    private static final Long WORD_NUMBER32 = 221133223L;
    private static final String WORD11 = "hi";
    private static final String WORD21 = "hihi";
    private static final String WORD22 = "hey";
    private static final String WORD23 = "hello";
    private static final String WORD31 = "blue";
    private static final String WORD32 = "huh";
    private static final String USERNAME = "Dietmar";
    private MockedStatic<SecurityContextHolder> staticMock;

    @Mock
    private WordRepository wordRepository;
    @Mock
    private TranslationRepository translationRepository;

    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private TranslationService service;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);

        doReturn(null).when(translationRepository).save(any());
    }

    @Test
    @Tag("UnitTest")
    void test_getTranslations_unknownWord()
    {
        doReturn(Optional.empty()).when(wordRepository).getByLanguageIdAndLetters(eq(LANGUAGE_ID1), eq(WORD11));

        Assertions.assertEquals(Collections.emptyList(), service.getTranslations(getFirstWordDto()));
    }

    @Test
    @Tag("UnitTest")
    void test_getTranslations_noTranslation()
    {
        doReturn(Optional.of(getFirstWordEntity())).when(wordRepository).getByLanguageIdAndLetters(eq(LANGUAGE_ID1), eq(WORD11));
        doReturn(Collections.emptyList()).when(translationRepository).getAllByLanguageIdAndWordNumber(eq(LANGUAGE_ID1), eq(WORD_NUMBER11));

        Assertions.assertEquals(Collections.emptyList(), service.getTranslations(getFirstWordDto()));
    }

    @Test
    @Tag("UnitTest")
    void test_getTranslations_manyResults()
    {
        doReturn(Optional.of(getFirstWordEntity())).when(wordRepository).getByLanguageIdAndLetters(eq(LANGUAGE_ID1), eq(WORD11));
        doReturn(getTranslations(false)).when(translationRepository).getAllByLanguageIdAndWordNumber(eq(LANGUAGE_ID1), eq(WORD_NUMBER11));
        doReturn(getInvertedTranslation(false)).when(translationRepository).getAllByTranslatedLanguageIdAndTranslatedWordNumber(eq(LANGUAGE_ID1), eq(WORD_NUMBER11));
        setupWordReturns();

        List<Word> expectedResult = getAllWords(false);
        List<Word> actualResult = service.getTranslations(getFirstWordDto());
        expectedResult.sort(this::wordCompare);
        actualResult.sort(this::wordCompare);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @Tag("UnitTest")
    void test_getTranslations_targetLanguage_unknownWord()
    {
        doReturn(Optional.empty()).when(wordRepository).getByLanguageIdAndLetters(eq(LANGUAGE_ID1), eq(WORD11));

        Assertions.assertEquals(Collections.emptyList(), service.getTranslations(getFirstWordDto(), LANGUAGE_ID3));
    }

    @Test
    @Tag("UnitTest")
    void test_getTranslations_targetLanguage_noTranslation()
    {
        doReturn(Optional.of(getFirstWordEntity())).when(wordRepository).getByLanguageIdAndLetters(eq(LANGUAGE_ID1), eq(WORD11));
        doReturn(Collections.emptyList()).when(translationRepository).getAllByLanguageIdAndWordNumberAndTranslatedLanguageId(eq(LANGUAGE_ID1), eq(WORD_NUMBER11), eq(LANGUAGE_ID3));

        Assertions.assertEquals(Collections.emptyList(), service.getTranslations(getFirstWordDto(), LANGUAGE_ID3));
    }

    @Test
    @Tag("UnitTest")
    void test_getTranslations_targetLanguage_multipleResults()
    {
        doReturn(Optional.of(getFirstWordEntity())).when(wordRepository).getByLanguageIdAndLetters(eq(LANGUAGE_ID1), eq(WORD11));
        doReturn(getTranslations(true)).when(translationRepository).getAllByLanguageIdAndWordNumberAndTranslatedLanguageId(eq(LANGUAGE_ID1), eq(WORD_NUMBER11), eq(LANGUAGE_ID3));
        doReturn(getInvertedTranslation(true)).when(translationRepository).getAllByTranslatedLanguageIdAndTranslatedWordNumberAndLanguageId(eq(LANGUAGE_ID1), eq(WORD_NUMBER11), eq(LANGUAGE_ID3));
        setupWordReturns();

        List<Word> expectedResult = getAllWords(true);
        List<Word> actualResult = service.getTranslations(getFirstWordDto(), LANGUAGE_ID3);
        expectedResult.sort(this::wordCompare);
        actualResult.sort(this::wordCompare);

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @Tag("UnitTest")
    void test_linkWords_null()
    {
        Assertions.assertThrows(NullPointerException.class, () -> service.linkWords(null, null));
        Assertions.assertThrows(NullPointerException.class, () -> service.linkWords(getFirstWordDto(), null));
        Assertions.assertThrows(NullPointerException.class, () -> service.linkWords(null, getFirstWordDto()));
    }

    @Test
    @Tag("UnitTest")
    void test_linkWords_alreadyLinked()
    {
        setupAuthentication();

        doReturn(Optional.of(getWord(LANGUAGE_ID1, WORD_NUMBER11, WORD11))).when(wordRepository).getByLanguageIdAndLetters(LANGUAGE_ID1, WORD11);
        doReturn(Optional.of(getWord(LANGUAGE_ID2, WORD_NUMBER21, WORD21))).when(wordRepository).getByLanguageIdAndLetters(LANGUAGE_ID2, WORD21);
        WordDto word1 = getFirstWordDto();
        WordDto word2 = getSecondWordDto();

        doReturn(null).when(translationRepository).getByLanguageIdAndWordNumberAndTranslatedLanguageIdAndTranslatedWordNumber(eq(LANGUAGE_ID1), eq(WORD_NUMBER11), eq(LANGUAGE_ID2), eq(WORD_NUMBER21));
        doReturn(new Translation()).when(translationRepository).getByLanguageIdAndWordNumberAndTranslatedLanguageIdAndTranslatedWordNumber(eq(LANGUAGE_ID2), eq(WORD_NUMBER21), eq(LANGUAGE_ID1), eq(WORD_NUMBER11));
        Assertions.assertDoesNotThrow(() -> service.linkWords(word1, word2));

        doReturn(new Translation()).when(translationRepository).getByLanguageIdAndWordNumberAndTranslatedLanguageIdAndTranslatedWordNumber(eq(LANGUAGE_ID1), eq(WORD_NUMBER11), eq(LANGUAGE_ID2), eq(WORD_NUMBER21));
        doReturn(null).when(translationRepository).getByLanguageIdAndWordNumberAndTranslatedLanguageIdAndTranslatedWordNumber(eq(LANGUAGE_ID2), eq(WORD_NUMBER21), eq(LANGUAGE_ID1), eq(WORD_NUMBER11));
        Assertions.assertDoesNotThrow(() -> service.linkWords(word1, word2));

        doReturn(new Translation()).when(translationRepository).getByLanguageIdAndWordNumberAndTranslatedLanguageIdAndTranslatedWordNumber(eq(LANGUAGE_ID1), eq(WORD_NUMBER11), eq(LANGUAGE_ID2), eq(WORD_NUMBER21));
        doReturn(new Translation()).when(translationRepository).getByLanguageIdAndWordNumberAndTranslatedLanguageIdAndTranslatedWordNumber(eq(LANGUAGE_ID2), eq(WORD_NUMBER21), eq(LANGUAGE_ID1), eq(WORD_NUMBER11));
        Assertions.assertDoesNotThrow(() -> service.linkWords(word1, word2));

        verify(translationRepository, never()).save(any());

        staticMock.close();
    }

    @Test
    @Tag("UnitTest")
    void text_linkWords_success()
    {
        setupAuthentication();

        doReturn(Optional.of(getWord(LANGUAGE_ID1, WORD_NUMBER11, WORD11))).when(wordRepository).getByLanguageIdAndLetters(LANGUAGE_ID1, WORD11);
        doReturn(Optional.of(getWord(LANGUAGE_ID2, WORD_NUMBER21, WORD21))).when(wordRepository).getByLanguageIdAndLetters(LANGUAGE_ID2, WORD21);
        WordDto word1 = getFirstWordDto();
        WordDto word2 = getSecondWordDto();

        doReturn(null).when(translationRepository).getByLanguageIdAndWordNumberAndTranslatedLanguageIdAndTranslatedWordNumber(eq(LANGUAGE_ID1), eq(WORD_NUMBER11), eq(LANGUAGE_ID2), eq(WORD_NUMBER21));
        doReturn(null).when(translationRepository).getByLanguageIdAndWordNumberAndTranslatedLanguageIdAndTranslatedWordNumber(eq(LANGUAGE_ID2), eq(WORD_NUMBER21), eq(LANGUAGE_ID1), eq(WORD_NUMBER11));
        Assertions.assertDoesNotThrow(() -> service.linkWords(word1, word2));

        verify(translationRepository, times(1)).save(any());

        staticMock.close();
    }

    @Test
    @Tag("UnitTest")
    void text_saveWords_withDtoList()
    {
        setupAuthentication();

        Assertions.assertDoesNotThrow(() -> service.saveWords(getAllWordDtoList()));
        verify(wordRepository, times(1)).getByLanguageIdAndLetters(eq(LANGUAGE_ID1), eq(WORD11));
        verify(wordRepository, times(1)).getByLanguageIdAndLetters(eq(LANGUAGE_ID2), eq(WORD21));
        verify(wordRepository, times(1)).getByLanguageIdAndLetters(eq(LANGUAGE_ID2), eq(WORD22));
        verify(wordRepository, times(1)).getByLanguageIdAndLetters(eq(LANGUAGE_ID2), eq(WORD23));
        verify(wordRepository, times(1)).getByLanguageIdAndLetters(eq(LANGUAGE_ID3), eq(WORD31));
        verify(wordRepository, times(1)).getByLanguageIdAndLetters(eq(LANGUAGE_ID3), eq(WORD32));

        staticMock.close();
    }

    @Test
    @Tag("UnitTest")
    void text_saveWords_withStrings()
    {
        setupAuthentication();

        Assertions.assertDoesNotThrow(() -> service.saveWords(LANGUAGE_ID2, getSecondLanguageStrings()));
        verify(wordRepository, times(1)).getByLanguageIdAndLetters(eq(LANGUAGE_ID2), eq(WORD21));
        verify(wordRepository, times(1)).getByLanguageIdAndLetters(eq(LANGUAGE_ID2), eq(WORD22));
        verify(wordRepository, times(1)).getByLanguageIdAndLetters(eq(LANGUAGE_ID2), eq(WORD23));

        staticMock.close();
    }

    private void setupAuthentication()
    {
        staticMock = mockStatic(SecurityContextHolder.class);
        staticMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        doReturn(authentication).when(securityContext).getAuthentication();
        doReturn(USERNAME).when(authentication).getName();
    }

    private void setupWordReturns()
    {
        doReturn(getWord(LANGUAGE_ID1, WORD_NUMBER11, WORD11)).when(wordRepository).getByLanguageIdAndWordNumber(eq(LANGUAGE_ID1), eq(WORD_NUMBER11));
        doReturn(getWord(LANGUAGE_ID2, WORD_NUMBER21, WORD21)).when(wordRepository).getByLanguageIdAndWordNumber(eq(LANGUAGE_ID2), eq(WORD_NUMBER21));
        doReturn(getWord(LANGUAGE_ID2, WORD_NUMBER22, WORD22)).when(wordRepository).getByLanguageIdAndWordNumber(eq(LANGUAGE_ID2), eq(WORD_NUMBER22));
        doReturn(getWord(LANGUAGE_ID2, WORD_NUMBER23, WORD23)).when(wordRepository).getByLanguageIdAndWordNumber(eq(LANGUAGE_ID2), eq(WORD_NUMBER23));
        doReturn(getWord(LANGUAGE_ID3, WORD_NUMBER31, WORD31)).when(wordRepository).getByLanguageIdAndWordNumber(eq(LANGUAGE_ID3), eq(WORD_NUMBER31));
        doReturn(getWord(LANGUAGE_ID3, WORD_NUMBER32, WORD32)).when(wordRepository).getByLanguageIdAndWordNumber(eq(LANGUAGE_ID3), eq(WORD_NUMBER32));
    }

    private WordDto getFirstWordDto()
    {
        return new WordDto(LANGUAGE_ID1, WORD11);
    }

    private WordDto getSecondWordDto()
    {
        return new WordDto(LANGUAGE_ID2, WORD21);
    }

    private Word getFirstWordEntity()
    {
        return getWord(LANGUAGE_ID1, WORD_NUMBER11, WORD11);
    }

    private List<Translation> getTranslations(boolean justLanguage3)
    {
        List<Translation> results = new ArrayList<>();
        if (!justLanguage3)
        {
            results.add(getTranslation(LANGUAGE_ID1, WORD_NUMBER11, LANGUAGE_ID2, WORD_NUMBER21));
            results.add(getTranslation(LANGUAGE_ID1, WORD_NUMBER11, LANGUAGE_ID2, WORD_NUMBER22));
        }
        results.add(getTranslation(LANGUAGE_ID1, WORD_NUMBER11, LANGUAGE_ID3, WORD_NUMBER31));
        return results;
    }

    private List<Translation> getInvertedTranslation(boolean justLanguage3)
    {
        List<Translation> results = new ArrayList<>();
        if (!justLanguage3)
            results.add(getTranslation(LANGUAGE_ID2, WORD_NUMBER23, LANGUAGE_ID1, WORD_NUMBER11));
        results.add(getTranslation(LANGUAGE_ID3, WORD_NUMBER32, LANGUAGE_ID1, WORD_NUMBER11));
        return results;
    }

    private Translation getTranslation(Long l1, Long w1, Long l2, Long w2)
    {
        Translation translation = new Translation();
        translation.setLanguageId(l1);
        translation.setWordNumber(w1);
        translation.setTranslatedLanguageId(l2);
        translation.setTranslatedWordNumber(w2);
        return translation;
    }

    private List<Word> getAllWords(boolean justLanguage3)
    {
        List<Word> wordList = new ArrayList<>();
        if (!justLanguage3)
        {
            wordList.add(getWord(LANGUAGE_ID2, WORD_NUMBER21, WORD21));
            wordList.add(getWord(LANGUAGE_ID2, WORD_NUMBER22, WORD22));
            wordList.add(getWord(LANGUAGE_ID2, WORD_NUMBER23, WORD23));
        }
        wordList.add(getWord(LANGUAGE_ID3, WORD_NUMBER31, WORD31));
        wordList.add(getWord(LANGUAGE_ID3, WORD_NUMBER32, WORD32));
        return wordList;
    }

    private List<WordDto> getAllWordDtoList()
    {
        List<WordDto> dtoList = new ArrayList<>();
        dtoList.add(new WordDto(LANGUAGE_ID1, WORD11));
        dtoList.add(new WordDto(LANGUAGE_ID2, WORD21));
        dtoList.add(new WordDto(LANGUAGE_ID2, WORD22));
        dtoList.add(new WordDto(LANGUAGE_ID2, WORD23));
        dtoList.add(new WordDto(LANGUAGE_ID3, WORD31));
        dtoList.add(new WordDto(LANGUAGE_ID3, WORD32));
        return dtoList;
    }

    private List<String> getSecondLanguageStrings()
    {
        List<String> wordList = new ArrayList<>();
        wordList.add(WORD21);
        wordList.add(WORD22);
        wordList.add(WORD23);
        return wordList;
    }

    private Word getWord(Long languageId, Long wordNumber, String text)
    {
        Word word = new Word();
        word.setLanguageId(languageId);
        word.setWordNumber(wordNumber);
        word.setLetters(text);
        return word;
    }

    private int wordCompare(Word first, Word second)
    {
        int langComp = first.getLanguageId().compareTo(second.getLanguageId());
        if (langComp == 0)
            return first.getWordNumber().compareTo(second.getWordNumber());
        return langComp;
    }
}
