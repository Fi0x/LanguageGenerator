package io.fi0x.languagegenerator.logic.dto;

import io.fi0x.languagegenerator.db.entities.Word;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestWordDto
{
    private static final Long ID = 42L;
    private static final Long WORD_NUMBER = 84L;
    private static final String LETTERS = "bla";

    @Test
    @Tag("UnitTest")
    void test_toEntity()
    {
        Word expected = getEntity();
        assertThat(getDto().toEntity()).usingRecursiveComparison().isEqualTo(expected);
    }

    private WordDto getDto()
    {
        return new WordDto(ID, LETTERS);
    }
    private Word getEntity()
    {
        Word word = new Word();
        word.setLanguageId(ID);
        word.setLetters(LETTERS);
        return word;
    }
}
