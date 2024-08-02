package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.db.*;
import io.fi0x.languagegenerator.db.entities.Language;
import io.fi0x.languagegenerator.db.entities.Letter;
import io.fi0x.languagegenerator.logic.converter.LanguageConverter;
import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.logic.dto.LanguageJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @Mock
    private AuthenticationService authenticationService;
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

    @InjectMocks
    private LanguageService service;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);

        doReturn(Optional.of(HIGHEST_ID)).when(languageRepository).getHighestId();
        doNothing().when(languageRepository).deleteById(anyLong());
        doReturn(USERNAME).when(authenticationService).getAuthenticatedUsername();
        Letter letter = new Letter();
        letter.setId(354L);
        doReturn(List.of(letter)).when(letterRepository).getAllByLetters(eq(EXISTING_LETTERS));
        doReturn(getLanguageList(2, 0)).when(languageRepository).getAllByUsername(eq(USERNAME));
        doReturn(getLanguageList(3, 2)).when(languageRepository).getAllByVisible(eq(true));
        doReturn(Optional.of(LanguageConverter.convertToEntity(getLanguageData()))).when(languageRepository).findById(HIGHEST_ID);
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
    void test_addLanguage_json()
    {
        LanguageJson languageJson = new LanguageJson();
        MockedStatic<LanguageConverter> staticMock = mockStatic(LanguageConverter.class);
        staticMock.when(() -> LanguageConverter.convertToData(eq(languageJson), eq(NEXT_FREE_ID), eq(LANGUAGE_NAME), eq(USERNAME), eq(false)))
                .thenReturn(LanguageData.builder().id(NEXT_FREE_ID).build());

        Assertions.assertThrows(InvalidObjectException.class, () -> service.addLanguage(languageJson, LANGUAGE_NAME, false));
        //TODO: Also test invalid json

        staticMock.close();
    }

    @Test
    @Tag("UnitTest")
    void test_getUserAndPublicLanguages()
    {
        Assertions.assertEquals(5, service.getUserAndPublicLanguages().size());
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
    void test_getLanguageCreator()
    {
        Assertions.assertEquals(USERNAME, service.getLanguageCreator(HIGHEST_ID));
        Assertions.assertNull(service.getLanguageCreator(NEXT_FREE_ID));
    }

    @Test
    @Tag("UnitTest")
    void test_deleteLanguage()
    {
        Assertions.assertFalse(service.deleteLanguage(NEXT_FREE_ID));
        Assertions.assertTrue(service.deleteLanguage(HIGHEST_ID));
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
