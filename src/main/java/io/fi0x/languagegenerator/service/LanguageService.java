package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.db.*;
import io.fi0x.languagegenerator.db.entities.*;
import io.fi0x.languagegenerator.logic.dto.LanguageData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.io.InvalidObjectException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LanguageService
{
    private AuthenticationService authenticationService;

    private LanguageRepository languageRepository;
    private LetterRepository letterRepository;
    private ConsonantRepository cRepo;
    private ConsonantVocalRepository cvRepo;
    private VocalRepository vRepo;
    private VocalConsonantRepository vcRepo;
    private ForbiddenRepository fRepo;

    public void addLanguage(LanguageData languageData) throws InvalidObjectException
    {
        if (languageData.getId() == null)
        {
            Optional<Long> id = languageRepository.getHighestId();
            languageData.setId((id.isPresent() ? id.get() : 0) + 1);
        }

        if (languageData.invalid())
            throw new InvalidObjectException("Can't save language with the provided settings");

        languageRepository.save(languageData.toLanguageEntity());

        languageData.getConsonants().forEach(letterCombination -> {
            long letterId = getLetterIdOrSaveIfNew(letterCombination);
            ConsonantCombination combination = new ConsonantCombination();
            Optional<Long> id = cRepo.getHighestId();
            combination.setId((id.isPresent() ? id.get() : 0) + 1);
            combination.setLanguageId(languageData.getId());
            combination.setLetterId(letterId);
            cRepo.save(combination);
        });

        languageData.getVocals().forEach(letterCombination -> {
            long letterId = getLetterIdOrSaveIfNew(letterCombination);
            VocalCombination combination = new VocalCombination();
            Optional<Long> id = vRepo.getHighestId();
            combination.setId((id.isPresent() ? id.get() : 0) + 1);
            combination.setLanguageId(languageData.getId());
            combination.setLetterId(letterId);
            vRepo.save(combination);
        });

        languageData.getVocalConsonant().forEach(letterCombination -> {
            long letterId = getLetterIdOrSaveIfNew(letterCombination);
            VocalConsonantCombination combination = new VocalConsonantCombination();
            Optional<Long> id = vcRepo.getHighestId();
            combination.setId((id.isPresent() ? id.get() : 0) + 1);
            combination.setLanguageId(languageData.getId());
            combination.setLetterId(letterId);
            vcRepo.save(combination);
        });

        languageData.getConsonantVocals().forEach(letterCombination -> {
            long letterId = getLetterIdOrSaveIfNew(letterCombination);
            ConsonantVocalCombination combination = new ConsonantVocalCombination();
            Optional<Long> id = cvRepo.getHighestId();
            combination.setId((id.isPresent() ? id.get() : 0) + 1);
            combination.setLanguageId(languageData.getId());
            combination.setLetterId(letterId);
            cvRepo.save(combination);
        });

        languageData.getForbiddenCombinations().forEach(letterCombination -> {
            long letterId = getLetterIdOrSaveIfNew(letterCombination);
            ForbiddenCombination combination = new ForbiddenCombination();
            Optional<Long> id = fRepo.getHighestId();
            combination.setId((id.isPresent() ? id.get() : 0) + 1);
            combination.setLanguageId(languageData.getId());
            combination.setLetterId(letterId);
            fRepo.save(combination);
        });
    }

    public List<Language> getUserLanguages(String username)
    {
        return languageRepository.getAllByUsername(username);
    }

    public List<Language> getPublicLanguages()
    {
        return languageRepository.getAllByIsPublic(true);
    }

    public LanguageData getLanguageData(long languageId)
    {
        Optional<Language> languageEntity = languageRepository.findById(languageId);

        if(languageEntity.isEmpty())
            return LanguageData.builder().username(authenticationService.getAuthenticatedUsername()).build();

        // TODO: Fill letter-combinations in Dto to show them in edit-page
        return LanguageData.getFromEntity(languageEntity.get());
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
}
