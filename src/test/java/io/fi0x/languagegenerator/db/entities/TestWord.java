package io.fi0x.languagegenerator.db.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class TestWord
{
    private static final Long ID1 = 1L;
    private static final Long ID2 = 2L;
    private static final Long ID3 = 3L;
    private static final Long WORD1 = 11L;
    private static final Long WORD2 = 22L;
    private static final Long WORD3 = 33L;
    private static final String LETTERS1 = "A";
    private static final String LETTERS2 = "B";
    private static final String LETTERS3 = "C";

    @Test
    @Tag("UnitTest")
    void test_getCombinedId()
    {
        Word word = getWord(ID1, WORD1, LETTERS1);
        Word.WordId wrongId = word.getCombinedId();
        word.setWordNumber(WORD2);
        Word.WordId id = word.getCombinedId();
        word.setLetters("B");

        Assertions.assertEquals(id, word.getCombinedId());
        Assertions.assertNotEquals(wrongId, word.getCombinedId());
    }

    @Test
    @Tag("UnitTest")
    void test_idEquals()
    {
        Word word1 = getWord(ID1, WORD1, LETTERS1);
        Word word2 = getWord(ID2, WORD2, LETTERS2);
        Word word11 = getWord(ID1, WORD1, LETTERS2);
        Word.WordId id1 = word1.getCombinedId();
        Word.WordId id2 = word2.getCombinedId();
        Word.WordId id11 = word11.getCombinedId();

        boolean firstThird = id1.equals(id11);
        boolean firstSecond = id1.equals(id2);
        boolean firstObject = id1.equals(new Object());

        Assertions.assertTrue(firstThird);
        Assertions.assertFalse(firstSecond);
        Assertions.assertFalse(firstObject);
    }

    private Word getWord(Long languageId, Long wordNumber, String letters)
    {
        Word word = new Word();
        word.setLanguageId(languageId);
        word.setWordNumber(wordNumber);
        word.setLetters(letters);
        return word;
    }
}
