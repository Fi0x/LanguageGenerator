package io.fi0x.languagegenerator.logic.converter;

import io.fi0x.languagegenerator.db.entities.Language;
import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.logic.dto.LanguageJson;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestLanguageConverter
{
    private static final Long ID = 42L;
    private static final String NAME = "Lang Name";
    private static final String USERNAME = "Some User";
    private static final boolean VISIBLE = true;
    private static final int MIN_WORD_LENGTH = 6;
    private static final int MAX_WORD_LENGTH = 23;
    private static final List<String> VOCALS = new ArrayList<>();
    private static final List<String> CONSONANTS = new ArrayList<>();
    private static final List<String> VOCAL_CONSONANT = new ArrayList<>();
    private static final List<String> CONSONANT_VOCALS = new ArrayList<>();
    private static final List<String> FORBIDDEN_COMBINATIONS = new ArrayList<>();

    @Test
    @Tag("UnitTest")
    void test_convertToEntity()
    {
        assertThat(LanguageConverter.convertToEntity(getData())).usingRecursiveComparison().isEqualTo(getExpectedLang());
    }

    @Test
    @Tag("UnitTest")
    void test_convertToJson()
    {
        assertThat(LanguageConverter.convertToJson(getData())).usingRecursiveComparison().isEqualTo(getJson());
    }

    @Test
    @Tag("UnitTest")
    void test_convertToData()
    {
        assertThat(LanguageConverter.convertToData(getJson(), ID, NAME, USERNAME, VISIBLE)).usingRecursiveComparison().isEqualTo(getData());
    }

    private LanguageData getData()
    {
        return LanguageData.builder().id(ID).name(NAME).username(USERNAME).visible(VISIBLE).minWordLength(MIN_WORD_LENGTH)
                .maxWordLength(MAX_WORD_LENGTH).vocals(VOCALS).consonants(CONSONANTS).vocalConsonant(VOCAL_CONSONANT)
                .consonantVocals(CONSONANT_VOCALS).forbiddenCombinations(FORBIDDEN_COMBINATIONS).build();
    }

    private Language getExpectedLang()
    {
        Language language = new Language();
        language.setId(ID);
        language.setName(NAME);
        language.setUsername(USERNAME);
        language.setVisible(VISIBLE);
        language.setMinWordLength(MIN_WORD_LENGTH);
        language.setMaxWordLength(MAX_WORD_LENGTH);
        return language;
    }
    private LanguageJson getJson()
    {
        LanguageJson json = new LanguageJson();
        json.setNameLengths(new int[]{MIN_WORD_LENGTH, MAX_WORD_LENGTH});
        json.setVocals(VOCALS);
        json.setConsonants(CONSONANTS);
        json.setVocalConsonant(VOCAL_CONSONANT);
        json.setConsonantVocals(CONSONANT_VOCALS);
        json.setForbiddenCombinations(FORBIDDEN_COMBINATIONS);
        return json;
    }
}
