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
    private int minWordLength;
    private int maxWordLength;

    private List<String> vocals;
    private List<String> consonants;
    private List<String> vocalConsonant;
    private List<String> consonantVocals;
    private List<String> forbiddenCombinations;

    public static LanguageData getFromEntity(Language language)
    {
        return LanguageData.builder().id(language.getId()).name(language.getName()).username(language.getUsername())
                .minWordLength(language.getMinWordLength()).maxWordLength(language.getMaxWordLength()).build();
    }

    public boolean invalid()
    {
        if(vocals == null || consonants == null || vocalConsonant == null || consonantVocals == null)
            return true;
        return vocals.isEmpty() && consonants.isEmpty() && vocalConsonant.isEmpty() && consonantVocals.isEmpty();
    }
}