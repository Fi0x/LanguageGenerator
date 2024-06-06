package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.db.*;
import io.fi0x.languagegenerator.db.entities.*;
import io.fi0x.languagegenerator.logic.converter.LanguageConverter;
import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.logic.dto.LanguageJson;
import io.fi0x.languagegenerator.logic.dto.Word;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InvalidObjectException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TestLanguageService
{
    private static final Long HIGHEST_ID = 3L;
    private static final String USERNAME = "Klaus Detlef";
    private static final String LANGUAGE_NAME = "Geheimsprache";

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

    @InjectMocks
    private LanguageService service;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);

        doReturn(Optional.of(HIGHEST_ID)).when(languageRepository).getHighestId();
        doReturn(USERNAME).when(authenticationService).getAuthenticatedUsername();
        doReturn(getLanguageList(2,0)).when(languageRepository).getAllByUsername(eq(USERNAME));
        doReturn(getLanguageList(3, 2)).when(languageRepository).getAllByVisible(eq(true));
    }

    @Test
    @Tag("UnitTest")
    void test_addLanguage_success()
    {
        LanguageData languageData = getLanguageData();

        Assertions.assertDoesNotThrow(() -> service.addLanguage(languageData));
    }

    @Test
    @Tag("UnitTest")
    void test_addLanguage_json()
    {
        LanguageJson languageJson = new LanguageJson();
        mockStatic(LanguageConverter.class)
                .when(() -> LanguageConverter.convertToData(eq(languageJson), eq(HIGHEST_ID + 1), eq(LANGUAGE_NAME), eq(USERNAME), eq(false)))
                .thenReturn(LanguageData.builder().id(HIGHEST_ID + 1).build());

        Assertions.assertThrows(InvalidObjectException.class, () -> service.addLanguage(languageJson, LANGUAGE_NAME, false));
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

        Assertions.assertEquals(LanguageData.builder().username(USERNAME).build(), service.getLanguageData(HIGHEST_ID));
    }

    @Test
    @Tag("UnitTest")
    void test_getLanguageCreator()
    {
        doReturn(Optional.of(LanguageConverter.convertToEntity(getLanguageData()))).when(languageRepository).findById(HIGHEST_ID);

        Assertions.assertEquals(USERNAME, service.getLanguageCreator(HIGHEST_ID));
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
                .minWordLength(2)
                .maxWordLength(2).build();
    }
    private List<Language> getLanguageList(int size, int startId)
    {
        List<Language> result = new ArrayList<>();
        for(int i = 0; i < size; i++)
        {
            Language language = new Language();
            language.setId((long) startId + i);
            result.add(language);
        }
        return result;
    }
}
