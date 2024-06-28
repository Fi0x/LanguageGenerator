package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.db.*;
import io.fi0x.languagegenerator.db.entities.*;
import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.logic.dto.Word;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class GenerationService
{
    private final LanguageRepository languageRepository;
    private final LetterRepository letterRepository;
    private final ConsonantRepository cRepository;
    private final ConsonantVocalRepository cvRepository;
    private final VocalRepository vRepository;
    private final VocalConsonantRepository vcRepository;
    private final ForbiddenRepository fRepository;
    private final SpecialCharacterRepository speRepository;
    private final StartingRepository staRepository;
    private final EndingRepository endRepository;

    public List<Word> generateWords(long languageId, int count) throws EntityNotFoundException, InvalidObjectException
    {
        Optional<Language> result = languageRepository.findById(languageId);
        if (result.isEmpty())
            throw new EntityNotFoundException("Could not find language with id=" + languageId);

        LanguageData language = LanguageData.getFromEntity(result.get());
        language.setConsonants(getLetters(cRepository.getAllByLanguageId(languageId).stream().map(ConsonantCombination::getLetterId).collect(Collectors.toList())));
        language.setConsonantVocals(getLetters(cvRepository.getAllByLanguageId(languageId).stream().map(ConsonantVocalCombination::getLetterId).collect(Collectors.toList())));
        language.setVocals(getLetters(vRepository.getAllByLanguageId(languageId).stream().map(VocalCombination::getLetterId).collect(Collectors.toList())));
        language.setVocalConsonant(getLetters(vcRepository.getAllByLanguageId(languageId).stream().map(VocalConsonantCombination::getLetterId).collect(Collectors.toList())));
        language.setForbiddenCombinations(getLetters(fRepository.getAllByLanguageId(languageId).stream().map(ForbiddenCombination::getLetterId).collect(Collectors.toList())));
        language.setSpecialCharacters(getLetters(speRepository.getAllByLanguageId(languageId).stream().map(SpecialCharacterCombinations::getLetterId).collect(Collectors.toList())));
        language.setStartingCombinations(getLetters(staRepository.getAllByLanguageId(languageId).stream().map(StartingCombinations::getLetterId).collect(Collectors.toList())));
        language.setEndingCombinations(getLetters(endRepository.getAllByLanguageId(languageId).stream().map(EndingCombinations::getLetterId).collect(Collectors.toList())));

        if (language.invalid())
            throw new InvalidObjectException("Can't generate words with the settings of language: " + languageId);

        ArrayList<Word> generatedWords = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Word word = generateWord(language);
            generatedWords.add(word);
        }

        return generatedWords;
    }

    private Word generateWord(LanguageData language)
    {
        StringBuilder name = new StringBuilder();
        List<String> forbiddenCombinations = language.getForbiddenCombinations();

        name.append(getBeginning(language));
        String ending = getNewRandom(forbiddenCombinations, name.toString(), language.getEndingCombinations(), "");

        int desiredLength = (int) (Math.random() * (language.getMaxWordLength() - language.getMinWordLength()) + language.getMinWordLength());
        desiredLength -= ending.length();

        for (int i = (int) (Math.random() * 4); name.length() < desiredLength && i < desiredLength + 4; i++) {
            List<String> selectedList;
            switch (i % 4) {
                case 0 -> selectedList = language.getConsonantVocals();
                case 1 -> selectedList = language.getConsonants();
                case 2 -> selectedList = language.getVocalConsonant();
                default -> selectedList = language.getVocals();
            }
            name.append(getNewRandom(forbiddenCombinations, name.toString(), selectedList, ending));
        }

        name.append(ending);

        if (!name.isEmpty())
            name.setCharAt(0, String.valueOf(name.charAt(0)).toUpperCase(Locale.ROOT).charAt(0));
        return new Word(language.getId(), addSpecialCharacters(name.toString(), language));
    }

    private String getBeginning(LanguageData languageData)
    {
        return getNewRandom(languageData.getForbiddenCombinations(), "", languageData.getStartingCombinations(), "");
    }

    private String getNewRandom(List<String> forbiddenCombinations, String previousLetters, List<String> newPossibilities, String ending)
    {
        int tries = 0;
        int randomIdx = (int) (Math.random() * newPossibilities.size());

        while (tries < newPossibilities.size()) {
            String nextPossiblePart = newPossibilities.get((randomIdx + tries) % newPossibilities.size());

            if (isAllowed(forbiddenCombinations, previousLetters, nextPossiblePart, ending))
                return nextPossiblePart;

            tries++;
        }
        return "";
    }

    private boolean isAllowed(List<String> forbiddenCombinations, String originalPart, String newPart, String ending)
    {
        return forbiddenCombinations.stream().noneMatch((" " + originalPart + newPart + ending)::contains);
    }

    private List<String> getLetters(List<Long> letterIds)
    {
        List<String> letterList = new ArrayList<>();

        letterIds.forEach(letterId -> letterRepository.findById(letterId).ifPresent(value -> letterList.add(value.getLetters())));

        return letterList;
    }

    // TODO: Test if this method works as expected and add unit tests for it
    private String addSpecialCharacters(String currentWord, LanguageData languageData)
    {
        if(currentWord.length() >= languageData.getMaxWordLength())
            return currentWord;
        if(Math.random() >= languageData.getSpecialCharacterChance())
            return currentWord;

        int lastSpecialCharIdx = 0;
        while (currentWord.length() < languageData.getMaxWordLength())
        {
            int nextSpecialCharIdx = lastSpecialCharIdx;
            if(lastSpecialCharIdx == 0)
                nextSpecialCharIdx = languageData.getCharsBeforeSpecial() - 1;
            nextSpecialCharIdx += (int) (Math.random() * (currentWord.length() - nextSpecialCharIdx - languageData.getCharsAfterSpecial() + 1));
            if(nextSpecialCharIdx <= lastSpecialCharIdx)
                break;

            StringBuilder start = new StringBuilder(currentWord.substring(0, nextSpecialCharIdx));
            String end = currentWord.substring(nextSpecialCharIdx);

            start.append(getNewRandom(languageData.getForbiddenCombinations(), start.toString(), languageData.getSpecialCharacters(), end));
            start.append(end);
            currentWord = start.toString();

            lastSpecialCharIdx = nextSpecialCharIdx;
        }

        return currentWord;
    }
}
