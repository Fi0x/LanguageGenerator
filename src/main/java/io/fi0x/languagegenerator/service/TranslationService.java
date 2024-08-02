package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.db.TranslationRepository;
import io.fi0x.languagegenerator.db.WordRepository;
import io.fi0x.languagegenerator.db.entities.Translation;
import io.fi0x.languagegenerator.db.entities.Word;
import io.fi0x.languagegenerator.logic.converter.WordConverter;
import io.fi0x.languagegenerator.logic.dto.WordDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.lang.Nullable;
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
    private final WordRepository wordRepo;
    private final TranslationRepository translationRepo;

    public List<Word> getTranslations(WordDto originalWord)
    {
        log.trace("getTranslations() called with wordDto={}", originalWord);

        Word word = getSavedWord(originalWord);
        if (word == null)
            return Collections.emptyList();

        List<Translation> translations = translationRepo.getAllByLanguageIdAndWordNumber(word.getLanguageId(), word.getWordNumber());
        List<Translation> invertedTranslations = translationRepo.getAllByTranslatedLanguageIdAndTranslatedWordNumber(word.getLanguageId(), word.getWordNumber());
        invertedTranslations.forEach(Translation::swap);
        translations.addAll(invertedTranslations);
        return getTranslatedWords(translations);
    }

    public List<Word> getTranslations(WordDto originalWord, Long desiredLanguageId)
    {
        log.trace("getTranslations() called with wordDto={} and languageId={}", originalWord, desiredLanguageId);

        Word word = getSavedWord(originalWord);
        if (word == null)
            return Collections.emptyList();

        List<Translation> translations = translationRepo.getAllByLanguageIdAndWordNumberAndTranslatedLanguageId(word.getLanguageId(), word.getWordNumber(), desiredLanguageId);
        List<Translation> invertedTranslations = translationRepo.getAllByTranslatedLanguageIdAndTranslatedWordNumberAndLanguageId(word.getLanguageId(), word.getWordNumber(), desiredLanguageId);
        invertedTranslations.forEach(Translation::swap);
        translations.addAll(invertedTranslations);
        return getTranslatedWords(translations);
    }

    public void linkWords(WordDto firstDto, WordDto secondDto)
    {
        log.trace("linkWords() called with wordDtos={} and {}", firstDto, secondDto);

        Word firstWord = saveOrGetWord(firstDto);
        Word secondWord = saveOrGetWord(secondDto);
        if (isNotLinked(firstWord, secondWord) && isNotLinked(secondWord, firstWord))
        {
            Translation translation = WordConverter.convertToTranslation(firstWord, secondWord);
            translationRepo.save(translation);
        }
    }

    public void saveWords(List<WordDto> words)
    {
        log.trace("saveWords() called with wordDtos={}", words);

        words.forEach(this::saveOrGetWord);
    }

    public void saveWords(Long languageId, List<String> words)
    {
        log.trace("saveWords() called with languageId={} and words={}", languageId, words);

        words.forEach(wordLetters -> saveOrGetWord(new WordDto(languageId, wordLetters)));
    }

    public Word saveOrGetWord(WordDto word)
    {
        log.trace("saveOrGetWord() called with wordDto={}", word);

        Word result = getSavedWord(word);
        if (result == null) {
            Long id = saveOrOverwriteWord(word.toEntity());
            word.setSavedInDb(true);

            result = new Word();
            result.setLanguageId(word.getLanguageId());
            result.setWordNumber(id);
            result.setLetters(word.getWord());
        }

        return result;
    }

    @Nullable
    public Word getSavedWord(WordDto word)
    {
        log.trace("getSavedWord() called with wordDto={}", word);

        return wordRepo.getByLanguageIdAndLetters(word.getLanguageId(), word.getWord()).orElse(null);
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
