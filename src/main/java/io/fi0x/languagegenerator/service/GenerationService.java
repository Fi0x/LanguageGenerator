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
import java.util.*;
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

        if(language.invalid())
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
        int desiredLength = (int) (Math.random() * (language.getMaxWordLength() - language.getMinWordLength()) + language.getMinWordLength());

        StringBuilder name = new StringBuilder();
        for (int i = (int) (Math.random() * 4); name.length() < desiredLength && i < desiredLength + 4; i++) {
            List<String> selectedList;
            switch (i % 4) {
                case 0 -> selectedList = language.getConsonantVocals();
                case 1 -> selectedList = language.getConsonants();
                case 2 -> selectedList = language.getVocalConsonant();
                default -> selectedList = language.getVocals();
            }
            name.append(getNewRandom(language, name.toString(), selectedList));
        }

        if(!name.isEmpty())
            name.setCharAt(0, String.valueOf(name.charAt(0)).toUpperCase(Locale.ROOT).charAt(0));
        return new Word(language.getId(), name.toString());
    }

    private String getNewRandom(LanguageData languageData, String previousLetters, List<String> newPossibilities)
    {
        int tries = 0;
        int randomIdx = (int) (Math.random() * newPossibilities.size());

        while (tries < newPossibilities.size()) {
            String nextPossiblePart = newPossibilities.get((randomIdx + tries) % newPossibilities.size());

            if (isAllowed(languageData, previousLetters, nextPossiblePart))
                return nextPossiblePart;

            tries++;
        }
        return "";
    }

    private boolean isAllowed(LanguageData languageData, String originalPart, String newPart)
    {
        return languageData.getForbiddenCombinations().stream().noneMatch((" " + originalPart + newPart)::contains);
    }

    private List<String> getLetters(List<Long> letterIds)
    {
        List<String> letterList = new ArrayList<>();

        letterIds.forEach(letterId -> letterRepository.findById(letterId).ifPresent(value -> letterList.add(value.getLetters())));

        return letterList;
    }
}
