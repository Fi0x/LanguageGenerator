package io.fi0x.languagegenerator.logic.dto;

import io.fi0x.languagegenerator.db.entities.Language;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LanguageData
{
    private Long id;
    private String name;
    private String username;
    private boolean visible;
    private int minWordLength;
    private int maxWordLength;
    private int charsBeforeSpecial;
    private int charsAfterSpecial;
    private int minSpecialChars;
    private int maxSpecialChars;
    private Double specialCharacterChance;

    // TODO: Change lists to weighted ones
    private List<String> vocals;
    private List<String> consonants;
    private List<String> vocalConsonant;
    private List<String> consonantVocals;
    private List<String> forbiddenCombinations;
    private List<String> specialCharacters;
    private List<String> startingCombinations;
    private List<String> endingCombinations;

    public static LanguageData getFromEntity(Language language)
    {
        return LanguageData.builder().id(language.getId()).name(language.getName()).username(language.getUsername())
                .minWordLength(language.getMinWordLength()).maxWordLength(language.getMaxWordLength())
                .charsBeforeSpecial(language.getCharsBeforeSpecial()).charsAfterSpecial(language.getCharsAfterSpecial())
                .minSpecialChars(language.getMinSpecialChars()).maxSpecialChars(language.getMaxSpecialChars())
                .specialCharacterChance(language.getSpecialCharacterChance()).visible(language.getVisible()).build();
    }

    public boolean invalid()
    {
        if (id == null || name == null || username == null)
            return true;
        if (vocals == null || consonants == null || vocalConsonant == null || consonantVocals == null)
            return true;

        if (name.isBlank() || username.isBlank() || minWordLength <= 0 || maxWordLength <= 0)
            return true;
        return vocals.isEmpty() && consonants.isEmpty() && vocalConsonant.isEmpty() && consonantVocals.isEmpty();
    }
}