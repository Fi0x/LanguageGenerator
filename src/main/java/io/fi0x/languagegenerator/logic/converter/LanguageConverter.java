package io.fi0x.languagegenerator.logic.converter;

import io.fi0x.languagegenerator.db.entities.Language;
import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.logic.dto.LanguageJson;

public class LanguageConverter
{
    public static Language convertToEntity(final LanguageData languageData)
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

    public static LanguageJson convertToJson(LanguageData languageData)
    {
        LanguageJson languageJson = new LanguageJson();
        languageJson.setNameLengths(new int[]{languageData.getMinWordLength(), languageData.getMaxWordLength()});
        languageJson.setVocals(languageData.getVocals());
        languageJson.setConsonants(languageData.getConsonants());
        languageJson.setVocalConsonant(languageData.getVocalConsonant());
        languageJson.setConsonantVocals(languageData.getConsonantVocals());
        languageJson.setForbiddenCombinations(languageData.getForbiddenCombinations());
        return languageJson;
    }

    public static LanguageData convertToData(LanguageJson languageJson, long id, String name, String username, boolean isPublic)
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
