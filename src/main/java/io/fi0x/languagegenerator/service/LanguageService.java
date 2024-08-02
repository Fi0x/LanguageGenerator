package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.db.*;
import io.fi0x.languagegenerator.db.entities.*;
import io.fi0x.languagegenerator.logic.converter.LanguageConverter;
import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.logic.dto.LanguageJson;
import io.fi0x.languagegenerator.logic.dto.WordDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.InvalidObjectException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class LanguageService
{
    private final AuthenticationService authenticationService;

    private final LanguageRepository languageRepository;
    private final LetterRepository letterRepository;
    private final ConsonantRepository cRepo;
    private final ConsonantVocalRepository cvRepo;
    private final VocalRepository vRepo;
    private final VocalConsonantRepository vcRepo;
    private final ForbiddenRepository fRepo;
    private final SpecialCharacterRepository speRepo;
    private final StartingRepository staRepo;
    private final EndingRepository endRepo;

    public void addLanguage(LanguageData languageData) throws InvalidObjectException
    {
        log.trace("addLanguage() called with languageData={}", languageData);

        if (languageData.getId() == null)
        {
            Optional<Long> id = languageRepository.getHighestId();
            languageData.setId((id.isPresent() ? id.get() : -1) + 1);
        }

        if (languageData.invalid())
            throw new InvalidObjectException("Can't save language with the provided settings");

        languageRepository.save(LanguageConverter.convertToEntity(languageData));

        cRepo.deleteAllByLanguageId(languageData.getId());
        languageData.getConsonants().forEach(letterCombination -> {
            long letterId = getLetterIdOrSaveIfNew(letterCombination);
            ConsonantCombination combination = new ConsonantCombination();
            Optional<Long> id = cRepo.getHighestId();
            combination.setId((id.isPresent() ? id.get() : 0) + 1);
            combination.setLanguageId(languageData.getId());
            combination.setLetterId(letterId);
            cRepo.save(combination);
        });

        vRepo.deleteAllByLanguageId(languageData.getId());
        languageData.getVocals().forEach(letterCombination -> {
            long letterId = getLetterIdOrSaveIfNew(letterCombination);
            VocalCombination combination = new VocalCombination();
            Optional<Long> id = vRepo.getHighestId();
            combination.setId((id.isPresent() ? id.get() : 0) + 1);
            combination.setLanguageId(languageData.getId());
            combination.setLetterId(letterId);
            vRepo.save(combination);
        });

        vcRepo.deleteAllByLanguageId(languageData.getId());
        languageData.getVocalConsonant().forEach(letterCombination -> {
            long letterId = getLetterIdOrSaveIfNew(letterCombination);
            VocalConsonantCombination combination = new VocalConsonantCombination();
            Optional<Long> id = vcRepo.getHighestId();
            combination.setId((id.isPresent() ? id.get() : 0) + 1);
            combination.setLanguageId(languageData.getId());
            combination.setLetterId(letterId);
            vcRepo.save(combination);
        });

        cvRepo.deleteAllByLanguageId(languageData.getId());
        languageData.getConsonantVocals().forEach(letterCombination -> {
            long letterId = getLetterIdOrSaveIfNew(letterCombination);
            ConsonantVocalCombination combination = new ConsonantVocalCombination();
            Optional<Long> id = cvRepo.getHighestId();
            combination.setId((id.isPresent() ? id.get() : 0) + 1);
            combination.setLanguageId(languageData.getId());
            combination.setLetterId(letterId);
            cvRepo.save(combination);
        });

        fRepo.deleteAllByLanguageId(languageData.getId());
        languageData.getForbiddenCombinations().forEach(letterCombination -> {
            long letterId = getLetterIdOrSaveIfNew(letterCombination);
            ForbiddenCombination combination = new ForbiddenCombination();
            Optional<Long> id = fRepo.getHighestId();
            combination.setId((id.isPresent() ? id.get() : 0) + 1);
            combination.setLanguageId(languageData.getId());
            combination.setLetterId(letterId);
            fRepo.save(combination);
        });

        speRepo.deleteAllByLanguageId(languageData.getId());
        languageData.getSpecialCharacters().forEach(letterCombination -> {
            long letterId = getLetterIdOrSaveIfNew(letterCombination);
            SpecialCharacterCombinations combination = new SpecialCharacterCombinations();
            Optional<Long> id = speRepo.getHighestId();
            combination.setId((id.isPresent() ? id.get() : 0) + 1);
            combination.setLanguageId(languageData.getId());
            combination.setLetterId(letterId);
            speRepo.save(combination);
        });

        staRepo.deleteAllByLanguageId(languageData.getId());
        languageData.getStartingCombinations().forEach(letterCombination -> {
            long letterId = getLetterIdOrSaveIfNew(letterCombination);
            StartingCombinations combination = new StartingCombinations();
            Optional<Long> id = staRepo.getHighestId();
            combination.setId((id.isPresent() ? id.get() : 0) + 1);
            combination.setLanguageId(languageData.getId());
            combination.setLetterId(letterId);
            staRepo.save(combination);
        });

        endRepo.deleteAllByLanguageId(languageData.getId());
        languageData.getEndingCombinations().forEach(letterCombination -> {
            long letterId = getLetterIdOrSaveIfNew(letterCombination);
            EndingCombinations combination = new EndingCombinations();
            Optional<Long> id = endRepo.getHighestId();
            combination.setId((id.isPresent() ? id.get() : 0) + 1);
            combination.setLanguageId(languageData.getId());
            combination.setLetterId(letterId);
            endRepo.save(combination);
        });
    }

    public void addLanguage(LanguageJson languageJson, String name, boolean visible) throws InvalidObjectException
    {
        log.trace("addLanguage() called with name={}, visibility={} and languageJson={}", name, visible, languageJson);

        addLanguage(LanguageConverter.convertToData(languageJson, languageRepository.getHighestId().orElse(0L) + 1, name, authenticationService.getAuthenticatedUsername(), visible));
    }

    public List<Language> getUserAndPublicLanguages()
    {
        log.trace("getUserAndPublicLanguages() called");

        List<Language> result = languageRepository.getAllByUsername(authenticationService.getAuthenticatedUsername());
        List<Language> publicLanguages = languageRepository.getAllByVisible(true);
        result.removeAll(publicLanguages);
        result.addAll(publicLanguages);

        return result;
    }

    public List<WordDto> addLanguageNameToWords(List<Word> words)
    {
        log.trace("addLanguageNameToWords() called for words={}", words);

        List<WordDto> results = words.stream().map(word -> new WordDto(word.getLanguageId(), word.getLetters())).toList();
        results.forEach(wordDto -> {
            wordDto.setLanguageName(getLanguageData(wordDto.getLanguageId()).getName());
        });

        return results;
    }

    public LanguageData getLanguageData(long languageId)
    {
        log.trace("getLanguageData() called for languageId={}", languageId);

        Optional<Language> languageEntity = languageRepository.findById(languageId);

        return languageEntity.map(language -> addLettersToLanguage(LanguageData.getFromEntity(language)))
                .orElseGet(() -> LanguageData.builder().username(authenticationService.getAuthenticatedUsername()).build());
    }

    @Nullable
    public String getLanguageCreator(long languageId)
    {
        log.trace("getLangaugeCreator() called for languageId={}", languageId);

        Optional<Language> data = languageRepository.findById(languageId);
        return data.map(Language::getUsername).orElse(null);
    }

    public boolean deleteLanguage(long languageId)
    {
        log.trace("deleteLanguage() called for languageId={}", languageId);

        Optional<Language> languageEntity = languageRepository.findById(languageId);
        if (languageEntity.isEmpty())
            return false;

        languageRepository.deleteById(languageId);
        return true;
    }

    private long getLetterIdOrSaveIfNew(String letterCombination)
    {
        List<Letter> letters = letterRepository.getAllByLetters(letterCombination);
        if (letters.isEmpty())
        {
            Letter letter = new Letter();
            Optional<Long> id = letterRepository.getHighestId();
            letter.setId((id.isPresent() ? id.get() : 0) + 1);
            letter.setLetters(letterCombination);
            letterRepository.save(letter);
            return letter.getId();
        }
        return letters.get(0).getId();
    }

    private LanguageData addLettersToLanguage(LanguageData languageData)
    {
        List<VocalCombination> vCom = vRepo.getAllByLanguageId(languageData.getId());
        List<String> vLetters = vCom.stream().map(com -> letterRepository.getReferenceById(com.getLetterId()).getLetters()).toList();
        languageData.setVocals(vLetters);

        List<ConsonantCombination> cCom = cRepo.getAllByLanguageId(languageData.getId());
        List<String> cLetters = cCom.stream().map(com -> letterRepository.getReferenceById(com.getLetterId()).getLetters()).toList();
        languageData.setConsonants(cLetters);

        List<VocalConsonantCombination> vcCom = vcRepo.getAllByLanguageId(languageData.getId());
        List<String> vcLetters = vcCom.stream().map(com -> letterRepository.getReferenceById(com.getLetterId()).getLetters()).toList();
        languageData.setVocalConsonant(vcLetters);

        List<ConsonantVocalCombination> cvCom = cvRepo.getAllByLanguageId(languageData.getId());
        List<String> cvLetters = cvCom.stream().map(com -> letterRepository.getReferenceById(com.getLetterId()).getLetters()).toList();
        languageData.setConsonantVocals(cvLetters);

        List<ForbiddenCombination> fCom = fRepo.getAllByLanguageId(languageData.getId());
        List<String> fLetters = fCom.stream().map(com -> letterRepository.getReferenceById(com.getLetterId()).getLetters()).toList();
        languageData.setForbiddenCombinations(fLetters);

        List<SpecialCharacterCombinations> speCom = speRepo.getAllByLanguageId(languageData.getId());
        List<String> speLetters = speCom.stream().map(com -> letterRepository.getReferenceById(com.getLetterId()).getLetters()).toList();
        languageData.setSpecialCharacters(speLetters);

        List<StartingCombinations> staCom = staRepo.getAllByLanguageId(languageData.getId());
        List<String> staLetters = staCom.stream().map(com -> letterRepository.getReferenceById(com.getLetterId()).getLetters()).toList();
        languageData.setStartingCombinations(staLetters);

        List<EndingCombinations> endCom = endRepo.getAllByLanguageId(languageData.getId());
        List<String> endLetters = endCom.stream().map(com -> letterRepository.getReferenceById(com.getLetterId()).getLetters()).toList();
        languageData.setEndingCombinations(endLetters);

        return languageData;
    }
}
