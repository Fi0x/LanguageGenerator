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
