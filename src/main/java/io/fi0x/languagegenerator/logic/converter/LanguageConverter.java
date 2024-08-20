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
        language.setRealLanguage(languageData.getRealLanguage());
        language.setUsername(languageData.getUsername());
        language.setVisible(languageData.isVisible());
        language.setMinWordLength(languageData.getMinWordLength());
        language.setMaxWordLength(languageData.getMaxWordLength());
        language.setCharsBeforeSpecial(languageData.getCharsBeforeSpecial());
        language.setCharsAfterSpecial(languageData.getCharsAfterSpecial());
        language.setMinSpecialChars(languageData.getMinSpecialChars());
        language.setMaxSpecialChars(languageData.getMaxSpecialChars());
        language.setSpecialCharacterChance(languageData.getSpecialCharacterChance());

        return language;

    }

    //TODO: Complete test-coverage
    public static LanguageJson convertToJson(LanguageData languageData)
    {
        if(Boolean.TRUE.equals(languageData.getRealLanguage()))
            return new LanguageJson();
        LanguageJson languageJson = new LanguageJson();
        languageJson.setNameLengths(new int[]{languageData.getMinWordLength(), languageData.getMaxWordLength()});
        languageJson.setSpecialCharacterLengths(new int[]{languageData.getCharsBeforeSpecial(), languageData.getCharsAfterSpecial(),
                languageData.getMinSpecialChars(), languageData.getMaxSpecialChars()});
        languageJson.setSpecialCharacterChance(languageData.getSpecialCharacterChance());
        languageJson.setVocals(languageData.getVocals());
        languageJson.setConsonants(languageData.getConsonants());
        languageJson.setVocalConsonant(languageData.getVocalConsonant());
        languageJson.setConsonantVocals(languageData.getConsonantVocals());
        languageJson.setForbiddenCombinations(languageData.getForbiddenCombinations());
        languageJson.setSpecialCharacters(languageData.getSpecialCharacters());
        languageJson.setStartingCombinations(languageData.getStartingCombinations());
        languageJson.setEndingCombinations(languageData.getEndingCombinations());
        return languageJson;
    }

    public static LanguageData convertToData(LanguageJson languageJson, long id, String name, String username, boolean visible)
    {
        return LanguageData.builder()
                .id(id)
                .name(name)
                .username(username)
                .visible(visible)
                .minWordLength(languageJson.getNameLengths()[0])
                .maxWordLength(languageJson.getNameLengths()[1])
                .charsBeforeSpecial(languageJson.getSpecialCharacterLengths()[0])
                .charsAfterSpecial(languageJson.getSpecialCharacterLengths()[1])
                .minSpecialChars(languageJson.getSpecialCharacterLengths()[2])
                .maxSpecialChars(languageJson.getSpecialCharacterLengths()[3])
                .specialCharacterChance(languageJson.getSpecialCharacterChance())
                .vocals(languageJson.getVocals())
                .consonants(languageJson.getConsonants())
                .vocalConsonant(languageJson.getVocalConsonant())
                .consonantVocals(languageJson.getConsonantVocals())
                .forbiddenCombinations(languageJson.getForbiddenCombinations())
                .specialCharacters(languageJson.getSpecialCharacters())
                .startingCombinations(languageJson.getStartingCombinations())
                .endingCombinations(languageJson.getEndingCombinations())
                .build();
    }
}
