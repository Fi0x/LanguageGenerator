package io.fi0x.languagegenerator.logic.converter;

import io.fi0x.languagegenerator.db.entities.Language;
import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.logic.dto.LanguageJson;

import java.util.Arrays;

public class LanguageConverter
{
    public static Language convert(final LanguageData languageData)
    {
        Language language = new Language();
        language.setId(languageData.getId());
        language.setName(languageData.getName());
        language.setUsername(languageData.getUsername());
        language.setIsPublic(languageData.getIsPublic());
        language.setMinWordLength(languageData.getMinWordLength());
        language.setMaxWordLength(languageData.getMaxWordLength());
        return language;

    }

    public static LanguageData convert(final Language language)
    {
        return LanguageData.builder()
                .id(language.getId())
                .name(language.getName())
                .username(language.getUsername())
                .isPublic(language.getIsPublic())
                .minWordLength(language.getMinWordLength())
                .maxWordLength(language.getMaxWordLength()).build();
    }

    public static LanguageData convert(LanguageJson languageJson, long id, String name, String username, boolean isPublic)
    {
        return LanguageData.builder()
                .id(id)
                .name(name)
                .username(username)
                .isPublic(isPublic)
                .minWordLength(languageJson.getNameLengths()[0])
                .maxWordLength(languageJson.getNameLengths()[1])
                .vocals(languageJson.getVocals())
                .consonants(languageJson.getConsonants())
                .vocalConsonant(languageJson.getVocalConsonant())
                .consonantVocals(languageJson.getConsonantVocals())
                .forbiddenCombinations(languageJson.getForbiddenCombinations())
                .build();
    }
}
