package io.fi0x.languagegenerator.logic.converter;

import io.fi0x.languagegenerator.db.entities.Translation;
import io.fi0x.languagegenerator.db.entities.Word;
import io.fi0x.languagegenerator.logic.dto.WordDto;

public class WordConverter
{
    public static Translation convertToTranslation(Word word1, Word word2)
    {
        Translation translation = new Translation();
        translation.setLanguageId(word1.getLanguageId());
        translation.setWordNumber(word1.getWordNumber());
        translation.setTranslatedLanguageId(word2.getLanguageId());
        translation.setTranslatedWordNumber(word2.getWordNumber());
        return translation;
    }

    public static WordDto convertToDto(Long languageId, String letters)
    {
        return new WordDto(languageId, letters);
    }
}
