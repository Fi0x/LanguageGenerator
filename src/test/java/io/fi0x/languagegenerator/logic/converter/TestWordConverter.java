package io.fi0x.languagegenerator.logic.converter;

import io.fi0x.languagegenerator.db.entities.Translation;
import io.fi0x.languagegenerator.db.entities.Word;
import io.fi0x.languagegenerator.logic.dto.WordDto;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestWordConverter
{
    private static final Long LANGUAGE1 = 42L;
    private static final Long LANGUAGE2 = 43L;
    private static final Long WORD_NUMBER1 = 44L;
    private static final Long WORD_NUMBER2 = 45L;
    private static final String WORD1 = "bla";
    private static final String WORD2 = "blub";

    @Test
    @Tag("UnitTest")
    void test_convertToTranslation()
    {
        Word word1 = getFirstWord();
        Word word2 = getSecondWord();

        assertThat(WordConverter.convertToTranslation(word1, word2)).usingRecursiveComparison().isEqualTo(getExpectedTranslation());
    }

    @Test
    @Tag("UnitTest")
    void test_convertToDto()
    {
        assertThat(WordConverter.convertToDto(LANGUAGE1, WORD1)).usingRecursiveComparison().isEqualTo(getExpectedDto());
    }

    private Word getFirstWord()
    {
        Word word = new Word();
        word.setLanguageId(LANGUAGE1);
        word.setWordNumber(WORD_NUMBER1);
        word.setLetters(WORD1);
        return word;
    }

    private Word getSecondWord()
    {
        Word word = new Word();
        word.setLanguageId(LANGUAGE2);
        word.setWordNumber(WORD_NUMBER2);
        word.setLetters(WORD2);
        return word;
    }

    private Translation getExpectedTranslation()
    {
        Translation translation = new Translation();
        translation.setLanguageId(LANGUAGE1);
        translation.setWordNumber(WORD_NUMBER1);
        translation.setTranslatedLanguageId(LANGUAGE2);
        translation.setTranslatedWordNumber(WORD_NUMBER2);
        return translation;
    }

    private WordDto getExpectedDto()
    {
        return new WordDto(LANGUAGE1, WORD1);
    }
}
