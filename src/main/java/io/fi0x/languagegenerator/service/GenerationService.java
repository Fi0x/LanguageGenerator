package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.logic.FileLoader;
import io.fi0x.languagegenerator.logic.LanguageTraits;
import io.fi0x.languagegenerator.logic.dto.Word;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class GenerationService
{
    public List<Word> generateWords(String languageName, int count)
    {
        FileLoader.loadLanguageFile(languageName);

        ArrayList<Word> generatedWords = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Word word = generateWord(languageName);
            generatedWords.add(word);
        }

        return generatedWords;
    }

    private static Word generateWord(String language)
    {
        StringBuilder name = new StringBuilder();
        int desiredLength = (int) (Math.random() * (LanguageTraits.maxNameLength - LanguageTraits.minNameLength) + LanguageTraits.minNameLength);
        for (int i = (int) (Math.random() * 4); name.length() < desiredLength && i < desiredLength + 4; i++) {
            ArrayList<String> selectedList;
            switch (i % 4) {
                case 0 -> selectedList = LanguageTraits.consonantVocals;
                case 1 -> selectedList = LanguageTraits.consonants;
                case 2 -> selectedList = LanguageTraits.vocalConsonant;
                default -> selectedList = LanguageTraits.vocals;
            }
            name.append(getNewRandom(name.toString(), selectedList));
        }

        name.setCharAt(0, String.valueOf(name.charAt(0)).toUpperCase(Locale.ROOT).charAt(0));
        return new Word(language, name.toString());
    }

    private static String getNewRandom(String previousLetters, ArrayList<String> newPossibilities)
    {
        int tries = 0;
        int randomIdx = (int) (Math.random() * newPossibilities.size());

        while (tries < newPossibilities.size()) {
            String nextPossiblePart = newPossibilities.get((randomIdx + tries) % newPossibilities.size());

            if (isAllowed(previousLetters, nextPossiblePart))
                return nextPossiblePart;

            tries++;
        }
        return "";
    }

    private static boolean isAllowed(String originalPart, String newPart)
    {
        return LanguageTraits.forbiddenCombinations.stream().noneMatch((" " + originalPart + newPart)::contains);
    }
}
