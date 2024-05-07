package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.db.LanguageRepository;
import io.fi0x.languagegenerator.db.entities.Language;
import io.fi0x.languagegenerator.logic.FileLoader;
import io.fi0x.languagegenerator.logic.LanguageTraits;
import io.fi0x.languagegenerator.logic.dto.Word;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class GenerationService
{
    LanguageRepository languageRepository;

    //TODO: Only use Language and no longer LanguageTraits or FileLoader
    public List<Word> generateWords(long languageId, int count) throws EntityNotFoundException
    {
        Optional<Language> result = languageRepository.findById(languageId);
        if (result.isEmpty())
            throw new EntityNotFoundException("Could not find language with id=" + languageId);

        Language language = result.get();
        FileLoader.loadLanguageFile(language.getName());

        ArrayList<Word> generatedWords = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Word word = generateWord(language);
            generatedWords.add(word);
        }

        return generatedWords;
    }

    private static Word generateWord(Language language)
    {
        StringBuilder name = new StringBuilder();
        int desiredLength = (int) (Math.random() * (language.getMaxWordLength() - language.getMinWordLength()) + language.getMinWordLength());
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
        return new Word(language.getId(), name.toString());
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
