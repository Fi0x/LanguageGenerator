package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.db.*;
import io.fi0x.languagegenerator.db.entities.Language;
import io.fi0x.languagegenerator.db.entities.Letter;
import io.fi0x.languagegenerator.db.entities.Word;
import io.fi0x.languagegenerator.logic.converter.LanguageConverter;
import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.logic.dto.LanguageJson;
import io.fi0x.languagegenerator.logic.dto.WordDto;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InvalidObjectException;
import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TestLanguageService
{
    private static final Long HIGHEST_ID = 3L;
    private static final Long NEXT_FREE_ID = 4L;
    private static final String USERNAME = "Klaus Detlef";
    private static final String LANGUAGE_NAME = "Geheimsprache";
    private static final String EXISTING_LETTERS = "kkk";
    private static final Long RANDOM_ID = 17L;
    private static final Long LANGUAGE_ID = 18L;

    private MockedStatic<SecurityContextHolder> staticMock;

    @Mock
    private LanguageRepository languageRepository;
    @Mock
    private LetterRepository letterRepository;
    @Mock
    private ConsonantRepository cRepo;
    @Mock
    private ConsonantVocalRepository cvRepo;
    @Mock
    private VocalRepository vRepo;
    @Mock
    private VocalConsonantRepository vcRepo;
    @Mock
    private ForbiddenRepository fRepo;
    @Mock
    private SpecialCharacterRepository speRepo;
    @Mock
    private StartingRepository staRepo;
    @Mock
    private EndingRepository endRepo;
    @Mock
    private WordRepository wordRepo;

    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private LanguageService service;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);
        staticMock = mockStatic(SecurityContextHolder.class);

        staticMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        doReturn(authentication).when(securityContext).getAuthentication();
        doReturn(USERNAME).when(authentication).getName();

        doReturn(Optional.of(HIGHEST_ID)).when(languageRepository).getHighestId();
        doNothing().when(languageRepository).deleteById(anyLong());
        Letter letter = new Letter();
        letter.setId(354L);
        doReturn(List.of(letter)).when(letterRepository).getAllByLetters(eq(EXISTING_LETTERS));
        doReturn(getLanguageList(2, 0)).when(languageRepository).getAllByUsername(eq(USERNAME));
        doReturn(getLanguageList(3, 2)).when(languageRepository).getAllByVisible(eq(true));
        doReturn(Optional.of(LanguageConverter.convertToEntity(getLanguageData()))).when(languageRepository).findById(HIGHEST_ID);
        doNothing().when(wordRepo).deleteAllByLanguageId(anyLong());
    }

    @AfterEach
    void teardown()
    {
        staticMock.close();
    }

    @Test
    @Tag("UnitTest")
    void test_addLanguage_invalid()
    {
        LanguageData languageData = getLanguageData();
        languageData.setConsonantVocals(new ArrayList<>());

        Assertions.assertThrows(InvalidObjectException.class, () -> service.addLanguage(languageData));
    }

    @Test
    @Tag("UnitTest")
    void test_addLanguage_success()
    {
        LanguageData languageData = getLanguageData();
        fillLanguageData(languageData);

        Assertions.assertDoesNotThrow(() -> service.addLanguage(languageData));
    }

    @Test
    @Tag("UnitTest")
    void test_addLanguage_successWithExistingIds()
    {
        LanguageData languageData = getLanguageData();
        fillLanguageData(languageData);
        doReturn(Optional.of(RANDOM_ID)).when(languageRepository).getHighestId();
        doReturn(Optional.of(RANDOM_ID)).when(cRepo).getHighestId();
        doReturn(Optional.of(RANDOM_ID)).when(vRepo).getHighestId();
        doReturn(Optional.of(RANDOM_ID)).when(vcRepo).getHighestId();
        doReturn(Optional.of(RANDOM_ID)).when(cvRepo).getHighestId();
        doReturn(Optional.of(RANDOM_ID)).when(fRepo).getHighestId();
        doReturn(Optional.of(RANDOM_ID)).when(speRepo).getHighestId();
        doReturn(Optional.of(RANDOM_ID)).when(staRepo).getHighestId();
        doReturn(Optional.of(RANDOM_ID)).when(endRepo).getHighestId();

        Assertions.assertDoesNotThrow(() -> service.addLanguage(languageData));
    }

    @Test
    @Tag("UnitTest")
    void test_addLanguage_unauthorized()
    {
        LanguageData languageData = getLanguageData();
        fillLanguageData(languageData);
        languageData.setUsername("Wrong User");

        Assertions.assertThrows(IllegalAccessException.class, () -> service.addLanguage(languageData));
    }

    @Test
    @Tag("UnitTest")
    void test_addLanguage_success_newId()
    {
        LanguageData languageData = getLanguageData();
        fillLanguageData(languageData);
        doReturn(Optional.empty()).when(languageRepository).getHighestId();

        Assertions.assertDoesNotThrow(() -> service.addLanguage(languageData));
    }

    @Test
    @Tag("UnitTest")
    void test_addLanguage_success_realLanguage()
    {
        LanguageData languageData = getLanguageData();
        languageData.setRealLanguage(true);
        doReturn(Collections.emptyList()).when(languageRepository).getAllByName(eq(LANGUAGE_NAME));

        Assertions.assertDoesNotThrow(() -> service.addLanguage(languageData));
    }

    @Test
    @Tag("UnitTest")
    void test_addLanguage_unauthorized_realLanguage()
    {
        LanguageData languageData = getLanguageData();
        languageData.setRealLanguage(true);
        doReturn(getLanguageList(1, 0)).when(languageRepository).getAllByName(eq(LANGUAGE_NAME));

        Assertions.assertThrows(IllegalAccessException.class, () -> service.addLanguage(languageData));
    }

    @Test
    @Tag("UnitTest")
    void test_addLanguage_json()
    {
        LanguageJson languageJson = new LanguageJson();
        languageJson.setNameLengths(new int[]{1, 2});
        languageJson.setSpecialCharacterLengths(new int[]{1, 2, 3, 4});

        Assertions.assertThrows(InvalidObjectException.class, () -> service.addLanguage(languageJson, LANGUAGE_NAME, false));
    }

    @Test
    @Tag("UnitTest")
    void test_addLanguage_invalidJson()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.addLanguage(null, LANGUAGE_NAME, false));
    }

    @Test
    @Tag("UnitTest")
    void test_getUserAndPublicLanguages()
    {
        Assertions.assertEquals(5, service.getUserAndPublicLanguages().size());
    }

    @Test
    @Tag("UnitTest")
    void test_addLanguageNameToWords()
    {
        List<Word> words = new ArrayList<>();
        Word word = new Word();
        word.setLanguageId(LANGUAGE_ID);
        word.setLetters(EXISTING_LETTERS);
        words.add(word);
        List<WordDto> wordDtos = new ArrayList<>();
        wordDtos.add(new WordDto(LANGUAGE_ID, LANGUAGE_NAME, null, EXISTING_LETTERS, null, null));

        Language language = new Language();
        language.setName(LANGUAGE_NAME);
        language.setSpecialCharacterChance(0.0);
        language.setVisible(false);
        doReturn(Optional.of(language)).when(languageRepository).findById(eq(LANGUAGE_ID));

        Assertions.assertEquals(wordDtos, service.addLanguageNameToWords(words));
    }

    @Test
    @Tag("UnitTest")
    void test_getLanguageData()
    {
        Assertions.assertEquals(LanguageData.builder().username(USERNAME).build(), service.getLanguageData(NEXT_FREE_ID));

        LanguageData expectedData = getLanguageData();
        expectedData.setConsonantVocals(new ArrayList<>());
        Assertions.assertEquals(expectedData, service.getLanguageData(HIGHEST_ID));
    }

    @Test
    @Tag("UnitTest")
    void test_getAuthenticatedLanguageData() throws IllegalAccessException
    {
        Assertions.assertEquals(LanguageData.builder().username(USERNAME).build(), service.getAuthenticatedLanguageData(NEXT_FREE_ID));

        LanguageData expectedData = getLanguageData();
        expectedData.setConsonantVocals(new ArrayList<>());
        Assertions.assertEquals(expectedData, service.getAuthenticatedLanguageData(HIGHEST_ID));

        Language language = new Language();
        language.setVisible(false);
        doReturn(Optional.of(language)).when(languageRepository).findById(eq(NEXT_FREE_ID));
        Assertions.assertThrows(IllegalAccessException.class, () -> service.getAuthenticatedLanguageData(NEXT_FREE_ID));
    }

    @Test
    @Tag("UnitTest")
    void test_deleteLanguage_success()
    {
        Assertions.assertDoesNotThrow(() -> service.deleteLanguage(HIGHEST_ID));
        verify(wordRepo, atLeastOnce()).deleteAllByLanguageId(anyLong());
    }

    @Test
    @Tag("UnitTest")
    void test_deleteLanguage_unauthorized()
    {
        Assertions.assertThrows(IllegalAccessException.class, () -> service.deleteLanguage(NEXT_FREE_ID));
    }

    @Test
    @Tag("UnitTest")
    void test_getLanguageCreator()
    {
        Assertions.assertEquals(USERNAME, service.getLanguageCreator(HIGHEST_ID));
        Assertions.assertNull(service.getLanguageCreator(NEXT_FREE_ID));
    }

    private LanguageData getLanguageData()
    {
        return LanguageData.builder()
                .name(LANGUAGE_NAME)
                .username(USERNAME)
                .vocals(new ArrayList<>())
                .consonants(new ArrayList<>())
                .vocalConsonant(new ArrayList<>())
                .consonantVocals(Arrays.asList("a", "aa"))
                .forbiddenCombinations(new ArrayList<>())
                .specialCharacters(new ArrayList<>())
                .startingCombinations(new ArrayList<>())
                .endingCombinations(new ArrayList<>())
                .minWordLength(2)
                .maxWordLength(2).build();
    }

    private void fillLanguageData(LanguageData data)
    {
        data.setVocals(Arrays.asList("a", "aa"));
        data.setConsonants(Arrays.asList("a", "aa"));
        data.setVocalConsonant(Arrays.asList("a", "aa"));
        data.setConsonantVocals(Arrays.asList("a", "aa", EXISTING_LETTERS));
        data.setForbiddenCombinations(Arrays.asList("b", "bb"));
        data.setSpecialCharacters(List.of("'"));
        data.setStartingCombinations(List.of("c"));
        data.setEndingCombinations(List.of("d"));
    }

    private List<Language> getLanguageList(int size, int startId)
    {
        List<Language> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Language language = new Language();
            language.setId((long) startId + i);
            result.add(language);
        }
        return result;
    }
}
