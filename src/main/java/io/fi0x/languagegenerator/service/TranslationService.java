package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.db.TranslationRepository;
import io.fi0x.languagegenerator.db.WordRepository;
import io.fi0x.languagegenerator.db.entities.Translation;
import io.fi0x.languagegenerator.db.entities.Word;
import io.fi0x.languagegenerator.logic.converter.WordConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TranslationService
{
    // TODO: Add tests for this class

    private final WordRepository wordRepo;
    private final TranslationRepository translationRepo;

    public List<Word> getTranslations(io.fi0x.languagegenerator.logic.dto.Word originalWord)
    {
        Word word = getWord(originalWord);
        if (word == null)
            return Collections.emptyList();

        List<Translation> translations = translationRepo.getAllByLanguageIdAndWordNumber(word.getLanguageId(), word.getWordNumber());
        return getTranslatedWords(translations);
    }

    public List<Word> getTranslations(io.fi0x.languagegenerator.logic.dto.Word originalWord, Long desiredLanguageId)
    {
        Word word = getWord(originalWord);
        if (word == null)
            return Collections.emptyList();

        List<Translation> translations = translationRepo.getAllByLanguageIdAndWordNumberAndTranslatedLanguageId(word.getLanguageId(), word.getWordNumber(), desiredLanguageId);
        return getTranslatedWords(translations);
    }

    public void linkWords(io.fi0x.languagegenerator.logic.dto.Word first, io.fi0x.languagegenerator.logic.dto.Word second)
    {
        Word firstWord = saveOrGetWord(first);
        Word secondWord = saveOrGetWord(second);
        if (isNotLinked(firstWord, secondWord) && isNotLinked(secondWord, firstWord))
        {
            Translation translation = WordConverter.convertToTranslation(firstWord, secondWord);
            translationRepo.save(translation);
        }
    }

    public void saveWords(List<io.fi0x.languagegenerator.logic.dto.Word> words)
    {
        words.forEach(this::saveOrGetWord);
    }

    public void saveWords(Long languageId, List<String> words)
    {
        words.forEach(wordLetters -> saveOrGetWord(WordConverter.convertToDto(languageId, wordLetters)));
    }

    public Word saveOrGetWord(io.fi0x.languagegenerator.logic.dto.Word word)
    {
        Word result = getWord(word);
        if (result == null) {
            Long id = saveOrOverwriteWord(word.toEntity());

            result = new Word();
            result.setLanguageId(word.getLanguageId());
            result.setWordNumber(id);
            result.setLetters(word.getWord());
        }

        return result;
    }

    private Long saveOrOverwriteWord(Word word) throws IllegalIdentifierException
    {
        if (word.getWordNumber() == null) {
            Optional<Long> id = wordRepo.getHighestId(word.getLanguageId());
            word.setWordNumber((id.isPresent() ? id.get() : -1) + 1);
        }

        wordRepo.save(word);
        return word.getWordNumber();
    }

    private Word getWord(io.fi0x.languagegenerator.logic.dto.Word word)
    {
        return wordRepo.getByLanguageIdAndLetters(word.getLanguageId(), word.getWord());
    }

    private List<Word> getTranslatedWords(List<Translation> translations)
    {
        List<Word> translatedWords = new ArrayList<>();
        translations.forEach(translation -> {
            Word translatedWord = wordRepo.getByLanguageIdAndWordNumber(translation.getTranslatedLanguageId(), translation.getTranslatedWordNumber());
            if (translatedWord != null)
                translatedWords.add(translatedWord);
        });

        return translatedWords;
    }

    private boolean isNotLinked(Word word1, Word word2)
    {
        Translation translation = translationRepo.getByLanguageIdAndWordNumberAndTranslatedLanguageIdAndTranslatedWordNumber(word1.getLanguageId(), word1.getWordNumber(), word2.getLanguageId(), word2.getWordNumber());
        return translation == null;
    }
}
