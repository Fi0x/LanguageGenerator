package io.fi0x.languagegenerator.logic.dto;

import io.fi0x.languagegenerator.db.entities.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestLanguageData
{
    private static final Long ID = 42L;
    private static final String NAME = "Lang Name";
    private static final String USERNAME = "Some User";
    private static final boolean VISIBLE = true;
    private static final int MIN_WORD_LENGTH = 6;
    private static final int MAX_WORD_LENGTH = 23;
    private static final Double SPECIAL_CHANCE = 0.0;
    private static final List<String> VOCALS = new ArrayList<>();
    private static final List<String> CONSONANTS = new ArrayList<>();
    private static final List<String> VOCAL_CONSONANT = new ArrayList<>();
    private static final List<String> CONSONANT_VOCALS = new ArrayList<>();
    private static final List<String> FORBIDDEN_COMBINATIONS = new ArrayList<>();

    static void fillLists() {
        VOCALS.add("a");
        CONSONANTS.add("b");
        VOCAL_CONSONANT.add("ab");
        CONSONANT_VOCALS.add("ba");
        FORBIDDEN_COMBINATIONS.add("c");
    }

    @BeforeEach
    void setup()
    {
        fillLists();
    }

    @Test
    @Tag("UnitTest")
    void test_getFromEntity()
    {
        LanguageData expected = getData();
        expected.setVocals(null);
        expected.setConsonants(null);
        expected.setVocalConsonant(null);
        expected.setConsonantVocals(null);
        expected.setForbiddenCombinations(null);
        assertThat(LanguageData.getFromEntity(getLang())).usingRecursiveComparison().isEqualTo(expected);
    }

    private LanguageData getData()
    {
        return LanguageData.builder().id(ID).name(NAME).username(USERNAME).visible(VISIBLE).minWordLength(MIN_WORD_LENGTH)
                .maxWordLength(MAX_WORD_LENGTH).vocals(VOCALS).consonants(CONSONANTS).vocalConsonant(VOCAL_CONSONANT)
                .consonantVocals(CONSONANT_VOCALS).forbiddenCombinations(FORBIDDEN_COMBINATIONS).build();
    }
    private Language getLang()
    {
        Language language = new Language();
        language.setId(ID);
        language.setName(NAME);
        language.setUsername(USERNAME);
        language.setVisible(VISIBLE);
        language.setMinWordLength(MIN_WORD_LENGTH);
        language.setMaxWordLength(MAX_WORD_LENGTH);
        language.setSpecialCharacterChance(SPECIAL_CHANCE);
        return language;
    }
}
