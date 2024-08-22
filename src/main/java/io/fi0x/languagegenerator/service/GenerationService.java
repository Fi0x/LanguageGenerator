package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.db.*;
import io.fi0x.languagegenerator.db.entities.*;
import io.fi0x.languagegenerator.logic.ControlledRandom;
import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.logic.dto.WordDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final WordRepository wordRepo;

    private final ControlledRandom random;

    public List<WordDto> generateWords(@NonNull LanguageData languageData, int count) throws EntityNotFoundException, InvalidObjectException, IllegalAccessException, IllegalArgumentException
    {
        log.trace("generateWords() called for language={} with amount={}", languageData, count);

        if (!languageData.getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName()) && !languageData.isVisible()) {
            log.info("User '{}' tried to generate words with language {}, to which he has no access to", SecurityContextHolder.getContext().getAuthentication().getName(), languageData.getId());
            throw new IllegalAccessException("You are not allowed to access the selected language");
        }

        Optional<Language> result = languageRepository.findById(languageData.getId());
        if (result.isEmpty())
            throw new EntityNotFoundException("Could not find language with id=" + languageData.getId());

        if (Boolean.TRUE.equals(result.get().getRealLanguage()))
            throw new IllegalArgumentException("Language '" + result.get().getName() + "' is not designed to generate words, but rather a placeholder for translations.");

        LanguageData language = LanguageData.getFromEntity(result.get());
        language.setConsonants(getLetters(cRepository.getAllByLanguageId(languageData.getId()).stream().map(ConsonantCombination::getLetterId).collect(Collectors.toList())));
        language.setConsonantVocals(getLetters(cvRepository.getAllByLanguageId(languageData.getId()).stream().map(ConsonantVocalCombination::getLetterId).collect(Collectors.toList())));
        language.setVocals(getLetters(vRepository.getAllByLanguageId(languageData.getId()).stream().map(VocalCombination::getLetterId).collect(Collectors.toList())));
        language.setVocalConsonant(getLetters(vcRepository.getAllByLanguageId(languageData.getId()).stream().map(VocalConsonantCombination::getLetterId).collect(Collectors.toList())));
        language.setForbiddenCombinations(getLetters(fRepository.getAllByLanguageId(languageData.getId()).stream().map(ForbiddenCombination::getLetterId).collect(Collectors.toList())));
        language.setSpecialCharacters(getLetters(speRepository.getAllByLanguageId(languageData.getId()).stream().map(SpecialCharacterCombinations::getLetterId).collect(Collectors.toList())));
        language.setStartingCombinations(getLetters(staRepository.getAllByLanguageId(languageData.getId()).stream().map(StartingCombinations::getLetterId).collect(Collectors.toList())));
        language.setEndingCombinations(getLetters(endRepository.getAllByLanguageId(languageData.getId()).stream().map(EndingCombinations::getLetterId).collect(Collectors.toList())));

        language.validate();

        ArrayList<WordDto> generatedWords = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            WordDto word = generateWord(language);
            word.setListIndex(i);
            Optional<Word> savedWord = wordRepo.getByLanguageIdAndLetters(languageData.getId(), word.getWord());
            if (savedWord.isPresent()) {
                word.setSavedInDb(true);
                word.setWordNumber(savedWord.get().getWordNumber());
            }
            generatedWords.add(word);
        }

        return generatedWords;
    }

    private WordDto generateWord(LanguageData language)
    {
        StringBuilder name = new StringBuilder();
        List<String> forbiddenCombinations = language.getForbiddenCombinations();

        name.append(getBeginning(language));
        String ending = getNewRandom(forbiddenCombinations, name.toString(), language.getEndingCombinations(), "");

        int desiredLength = random.randomInt(language.getMinWordLength(), language.getMaxWordLength());
        desiredLength -= ending.length();

        for (int i = random.randomInt(0, 3); name.length() < desiredLength && i < desiredLength + 4; i++) {
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
        return new WordDto(language.getId(), addSpecialCharacters(name.toString(), language));
    }

    private String getBeginning(LanguageData languageData)
    {
        return getNewRandom(languageData.getForbiddenCombinations(), "", languageData.getStartingCombinations(), "");
    }

    private String getNewRandom(List<String> forbiddenCombinations, String previousLetters, List<String> newPossibilities, String ending)
    {
        int tries = 0;
        int randomIdx = random.randomInt(0, newPossibilities.size());

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

    private String addSpecialCharacters(String currentWord, LanguageData languageData)
    {
        if (currentWord.length() >= languageData.getMaxWordLength())
            return currentWord;
        if (random.randomDouble() >= languageData.getSpecialCharacterChance() && languageData.getMaxSpecialChars() <= 0)
            return currentWord;

        int lastSpecialCharIdx = 0;
        for (int specCharCount = 0; currentWord.length() < languageData.getMaxWordLength() && specCharCount < languageData.getMaxSpecialChars(); specCharCount++) {
            int nextSpecialCharIdx = lastSpecialCharIdx;
            if (lastSpecialCharIdx == 0)
                nextSpecialCharIdx = languageData.getCharsBeforeSpecial() - 1;
            nextSpecialCharIdx += random.randomInt(0, currentWord.length() - nextSpecialCharIdx - languageData.getCharsAfterSpecial());
            if (nextSpecialCharIdx <= lastSpecialCharIdx)
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
