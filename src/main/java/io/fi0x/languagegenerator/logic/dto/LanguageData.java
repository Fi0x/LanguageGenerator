package io.fi0x.languagegenerator.logic.dto;

import io.fi0x.languagegenerator.db.entities.Language;
import lombok.Builder;
import lombok.Data;

import java.io.InvalidObjectException;
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
    private double specialCharacterChance;

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

    public void validate() throws InvalidObjectException
    {
        String errors = "The following errors occured:";
        boolean errorFound = false;

        if (id == null || name == null || username == null || name.isBlank() || username.isBlank()) {
            errors += "\nAn internal field was null or empty";
            errorFound = true;
        }
        if (vocals == null || consonants == null || vocalConsonant == null || consonantVocals == null) {
            errors += "\nA character-list was null";
            errorFound = true;
        } else if (vocals.isEmpty() && consonants.isEmpty() && vocalConsonant.isEmpty() && consonantVocals.isEmpty()) {
            errors += "\nAt least one character-list must not be empty";
            errorFound = true;
        }

        if (minWordLength <= 0) {
            errors += "\nThe min-word-length must be greater than 0";
            errorFound = true;
        }
        if (maxWordLength < minWordLength) {
            errors += "\nThe max-word-length must be at least as high as the min-word-length";
            errorFound = true;
        }

        if(errorFound)
            throw new InvalidObjectException(errors);
    }
}