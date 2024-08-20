package io.fi0x.languagegenerator.db.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class TestTranslation
{
    private static final Long ID1 = 1L;
    private static final Long ID2 = 2L;
    private static final Long WORD1 = 11L;
    private static final Long WORD2 = 22L;
    private static final Long WORD3 = 33L;

    @Test
    @Tag("UnitTest")
    void test_swap()
    {
        Translation translation = getTranslation();

        Assertions.assertDoesNotThrow(translation::swap);

        Assertions.assertEquals(ID1, translation.getTranslatedLanguageId());
        Assertions.assertEquals(ID2, translation.getLanguageId());
        Assertions.assertEquals(WORD1, translation.getTranslatedWordNumber());
        Assertions.assertEquals(WORD2, translation.getWordNumber());
    }

    @Test
    @Tag("UnitTest")
    void test_getCombinedId()
    {
        Translation translation = getTranslation();
        Translation.TranslationId wrongId = translation.getCombinedId();
        translation.setWordNumber(WORD3);
        Translation.TranslationId id = translation.getCombinedId();

        Assertions.assertEquals(id, translation.getCombinedId());
        Assertions.assertNotEquals(wrongId, translation.getCombinedId());
    }

    @Test
    @Tag("UnitTest")
    void test_idEquals()
    {
        Translation translation1 = getTranslation();
        Translation translation2 = getTranslation();
        translation2.setWordNumber(WORD3);
        Translation translation3 = getTranslation();
        Translation.TranslationId id1 = translation1.getCombinedId();
        Translation.TranslationId id2 = translation2.getCombinedId();
        Translation.TranslationId id3 = translation3.getCombinedId();

        boolean firstThird = id1.equals(id3);
        boolean firstSecond = id1.equals(id2);
        boolean firstObject = id1.equals(new Object());

        Assertions.assertTrue(firstThird);
        Assertions.assertFalse(firstSecond);
        Assertions.assertFalse(firstObject);
    }

    private Translation getTranslation()
    {
        Translation translation = new Translation();
        translation.setLanguageId(ID1);
        translation.setTranslatedLanguageId(ID2);
        translation.setWordNumber(WORD1);
        translation.setTranslatedWordNumber(WORD2);
        return translation;
    }
}
