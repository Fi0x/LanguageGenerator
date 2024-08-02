package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.db.*;
import io.fi0x.languagegenerator.db.entities.*;
import io.fi0x.languagegenerator.logic.dto.WordDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;

@RunWith(SpringRunner.class)
public class TestGenerationService
{
    private static final Long VALID_LANGUAGE_ID = 13245L;
    private static final Long INVALID_LANGUAGE_ID = 132L;
    private static final String NAME = "Language Name";
    private static final String USERNAME = "Karl Heinz";
    private static final boolean VISIBLE = false;
    private static final int NORMAL_WORD_LENGTH = 2;
    private static final int BEGINNING_END_WORD_LENGTH = 4;
    private static final Double SPECIAL_CHARACTER_CHANCE = 1D;
    private static final String DEFAULT_LETTER = "a";
    private static final Long DEFAULT_LETTER_ID = 4L;
    private static final String BEGINNING_LETTER = "b";
    private static final Long BEGINNING_LETTER_ID = 8L;
    private static final String END_LETTER = "c";
    private static final Long END_LETTER_ID = 32L;

    @Mock
    private LanguageRepository languageRepository;
    @Mock
    private LetterRepository letterRepository;
    @Mock
    private ConsonantRepository cRepository;
    @Mock
    private ConsonantVocalRepository cvRepository;
    @Mock
    private VocalRepository vRepository;
    @Mock
    private VocalConsonantRepository vcRepository;
    @Mock
    private ForbiddenRepository fRepository;
    @Mock
    private SpecialCharacterRepository speRepository;
    @Mock
    private StartingRepository staRepository;
    @Mock
    private EndingRepository endRepository;
    @Mock
    private WordRepository wordRepository;

    @InjectMocks
    private GenerationService service;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);

        doReturn(getLanguage(NORMAL_WORD_LENGTH)).when(languageRepository).findById(eq(VALID_LANGUAGE_ID));
    }

    @Test
    @Tag("UnitTest")
    void test_generateWords_success() throws InvalidObjectException
    {
        doReturn(getConsonantCombinations()).when(cRepository).getAllByLanguageId(eq(VALID_LANGUAGE_ID));
        doReturn(getVocalCombinations()).when(vRepository).getAllByLanguageId(eq(VALID_LANGUAGE_ID));
        doReturn(getConsonantVocalCombinations()).when(cvRepository).getAllByLanguageId(eq(VALID_LANGUAGE_ID));
        doReturn(getVocalConsonantCombinations()).when(vcRepository).getAllByLanguageId(eq(VALID_LANGUAGE_ID));
        doReturn(getDefaultLetter()).when(letterRepository).findById(eq(DEFAULT_LETTER_ID));

        Assertions.assertEquals(getNormalWordList(), service.generateWords(VALID_LANGUAGE_ID, 4));
    }

    @Test
    @Tag("UnitTest")
    void test_generateWords_noLanguage()
    {
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.generateWords(INVALID_LANGUAGE_ID, 4));
    }

    @Test
    @Tag("UnitTest")
    void test_generateWords_invalidLanguage()
    {
        Assertions.assertThrows(InvalidObjectException.class, () -> service.generateWords(VALID_LANGUAGE_ID, 4));
    }

    @Test
    @Tag("UnitTest")
    void test_generateWords_allForbidden() throws InvalidObjectException
    {
        doReturn(getConsonantCombinations()).when(cRepository).getAllByLanguageId(eq(VALID_LANGUAGE_ID));
        doReturn(getVocalCombinations()).when(vRepository).getAllByLanguageId(eq(VALID_LANGUAGE_ID));
        doReturn(getConsonantVocalCombinations()).when(cvRepository).getAllByLanguageId(eq(VALID_LANGUAGE_ID));
        doReturn(getVocalConsonantCombinations()).when(vcRepository).getAllByLanguageId(eq(VALID_LANGUAGE_ID));
        doReturn(getDefaultLetter()).when(letterRepository).findById(eq(DEFAULT_LETTER_ID));
        doReturn(getForbiddenCombinations()).when(fRepository).getAllByLanguageId(eq(VALID_LANGUAGE_ID));

        Assertions.assertEquals(getEmptyWordList(),  service.generateWords(VALID_LANGUAGE_ID, 4));
    }

    @Test
    @Tag("UnitTest")
    void test_generateWords_BeginningAndEnd() throws InvalidObjectException
    {
        doReturn(getConsonantCombinations()).when(cRepository).getAllByLanguageId(eq(VALID_LANGUAGE_ID));
        doReturn(getVocalCombinations()).when(vRepository).getAllByLanguageId(eq(VALID_LANGUAGE_ID));
        doReturn(getConsonantVocalCombinations()).when(cvRepository).getAllByLanguageId(eq(VALID_LANGUAGE_ID));
        doReturn(getVocalConsonantCombinations()).when(vcRepository).getAllByLanguageId(eq(VALID_LANGUAGE_ID));
        doReturn(getBeginningCombinations()).when(staRepository).getAllByLanguageId(eq(VALID_LANGUAGE_ID));
        doReturn(getEndCombinations()).when(endRepository).getAllByLanguageId(eq(VALID_LANGUAGE_ID));
        doReturn(getDefaultLetter()).when(letterRepository).findById(eq(DEFAULT_LETTER_ID));
        doReturn(getBeginningLetter()).when(letterRepository).findById(eq(BEGINNING_LETTER_ID));
        doReturn(getEndLetter()).when(letterRepository).findById(eq(END_LETTER_ID));
        doReturn(getLanguage(BEGINNING_END_WORD_LENGTH)).when(languageRepository).findById(eq(VALID_LANGUAGE_ID));

        Assertions.assertEquals(getBeginningEndWordList(), service.generateWords(VALID_LANGUAGE_ID, 4));
    }

    private List<WordDto> getNormalWordList()
    {
        List<WordDto> words = new ArrayList<>();
        words.add(new WordDto(VALID_LANGUAGE_ID, null, "Aa", 0, false));
        words.add(new WordDto(VALID_LANGUAGE_ID, null, "Aa", 1, false));
        words.add(new WordDto(VALID_LANGUAGE_ID, null, "Aa", 2, false));
        words.add(new WordDto(VALID_LANGUAGE_ID, null, "Aa", 3, false));
        return words;
    }

    private List<WordDto> getBeginningEndWordList()
    {
        List<WordDto> words = new ArrayList<>();
        words.add(new WordDto(VALID_LANGUAGE_ID, null, "Baac", 0, false));
        words.add(new WordDto(VALID_LANGUAGE_ID, null, "Baac", 1, false));
        words.add(new WordDto(VALID_LANGUAGE_ID, null, "Baac", 2, false));
        words.add(new WordDto(VALID_LANGUAGE_ID, null, "Baac", 3, false));
        return words;
    }

    private List<WordDto> getEmptyWordList()
    {
        List<WordDto> words = new ArrayList<>();
        words.add(new WordDto(VALID_LANGUAGE_ID, null, "", 0, false));
        words.add(new WordDto(VALID_LANGUAGE_ID, null, "", 1, false));
        words.add(new WordDto(VALID_LANGUAGE_ID, null, "", 2, false));
        words.add(new WordDto(VALID_LANGUAGE_ID, null, "", 3, false));
        return words;
    }

    private Optional<Language> getLanguage(int wordLength)
    {
        Language language = new Language();
        language.setId(VALID_LANGUAGE_ID);
        language.setName(NAME);
        language.setUsername(USERNAME);
        language.setVisible(VISIBLE);
        language.setMinWordLength(wordLength);
        language.setMaxWordLength(wordLength);
        language.setSpecialCharacterChance(SPECIAL_CHARACTER_CHANCE);
        return Optional.of(language);
    }

    private List<ConsonantCombination> getConsonantCombinations()
    {
        List<ConsonantCombination> result = new ArrayList<>();
        ConsonantCombination combination = new ConsonantCombination();
        combination.setLetterId(DEFAULT_LETTER_ID);
        result.add(combination);
        return result;
    }

    private List<VocalCombination> getVocalCombinations()
    {
        List<VocalCombination> result = new ArrayList<>();
        VocalCombination combination = new VocalCombination();
        combination.setLetterId(DEFAULT_LETTER_ID);
        result.add(combination);
        return result;
    }

    private List<ConsonantVocalCombination> getConsonantVocalCombinations()
    {
        List<ConsonantVocalCombination> result = new ArrayList<>();
        ConsonantVocalCombination combination = new ConsonantVocalCombination();
        combination.setLetterId(DEFAULT_LETTER_ID);
        result.add(combination);
        return result;
    }

    private List<VocalConsonantCombination> getVocalConsonantCombinations()
    {
        List<VocalConsonantCombination> result = new ArrayList<>();
        VocalConsonantCombination combination = new VocalConsonantCombination();
        combination.setLetterId(DEFAULT_LETTER_ID);
        result.add(combination);
        return result;
    }

    private List<ForbiddenCombination> getForbiddenCombinations()
    {
        List<ForbiddenCombination> result = new ArrayList<>();
        ForbiddenCombination combination = new ForbiddenCombination();
        combination.setLetterId(DEFAULT_LETTER_ID);
        result.add(combination);
        return result;
    }

    private List<StartingCombinations> getBeginningCombinations()
    {
        List<StartingCombinations> result = new ArrayList<>();
        StartingCombinations combination = new StartingCombinations();
        combination.setLetterId(BEGINNING_LETTER_ID);
        result.add(combination);
        return result;
    }

    private List<EndingCombinations> getEndCombinations()
    {
        List<EndingCombinations> result = new ArrayList<>();
        EndingCombinations combination = new EndingCombinations();
        combination.setLetterId(END_LETTER_ID);
        result.add(combination);
        return result;
    }

    private Optional<Letter> getDefaultLetter()
    {
        Letter letter = new Letter();
        letter.setLetters(DEFAULT_LETTER);
        return Optional.of(letter);
    }

    private Optional<Letter> getBeginningLetter()
    {
        Letter letter = new Letter();
        letter.setLetters(BEGINNING_LETTER);
        return Optional.of(letter);
    }

    private Optional<Letter> getEndLetter()
    {
        Letter letter = new Letter();
        letter.setLetters(END_LETTER);
        return Optional.of(letter);
    }
}
