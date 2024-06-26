package io.fi0x.languagegenerator.logic.dto;

import lombok.Data;

import java.util.List;

@Data
public class LanguageJson
{
    private int[] nameLengths;
    private int[] specialCharacterLengths;
    private double specialCharacterChance;
    private List<String> vocals;
    private List<String> consonants;
    private List<String> vocalConsonant;
    private List<String> consonantVocals;
    private List<String> forbiddenCombinations;
    private List<String> specialCharacters;
    private List<String> startingCombinations;
    private List<String> endingCombinations;
}
