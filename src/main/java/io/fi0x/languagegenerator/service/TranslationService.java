package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.db.TranslationRepository;
import io.fi0x.languagegenerator.db.WordRepository;
import io.fi0x.languagegenerator.db.entities.Translation;
import io.fi0x.languagegenerator.db.entities.Word;
import io.fi0x.languagegenerator.logic.Parallelization;
import io.fi0x.languagegenerator.logic.converter.WordConverter;
import io.fi0x.languagegenerator.logic.dto.WordDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@AllArgsConstructor
public class TranslationService
{
    private final WordRepository wordRepo;
    private final TranslationRepository translationRepo;

    public List<Word> getTranslations(WordDto originalWord)
    {
        Word word = getWord(originalWord);
        if (word == null)
            return Collections.emptyList();

        CompletableFuture<List<Translation>> normalTranslationFuture = Parallelization.runAndGetFuture(translationRepo.getAllByLanguageIdAndWordNumber(word.getLanguageId(), word.getWordNumber()));
        CompletableFuture<List<Translation>> invertedTranslationFuture = Parallelization.runAndGetFuture(translationRepo.getAllByTranslatedLanguageIdAndTranslatedWordNumber(word.getLanguageId(), word.getWordNumber()));

        return getWordsFromFutures(normalTranslationFuture, invertedTranslationFuture);
    }

    public List<Word> getTranslations(WordDto originalWord, Long desiredLanguageId)
    {
        Word word = getWord(originalWord);
        if (word == null)
            return Collections.emptyList();

        CompletableFuture<List<Translation>> normalTranslationFuture = Parallelization.runAndGetFuture(translationRepo.getAllByLanguageIdAndWordNumberAndTranslatedLanguageId(word.getLanguageId(), word.getWordNumber(), desiredLanguageId));
        CompletableFuture<List<Translation>> invertedTranslationFuture = Parallelization.runAndGetFuture(translationRepo.getAllByTranslatedLanguageIdAndTranslatedWordNumberAndLanguageId(word.getLanguageId(), word.getWordNumber(), desiredLanguageId));

        return getWordsFromFutures(normalTranslationFuture, invertedTranslationFuture);
    }

    private List<Word> getWordsFromFutures(CompletableFuture<List<Translation>> normalTranslationFuture, CompletableFuture<List<Translation>> invertedTranslationFuture)
    {
        CompletableFuture.allOf(normalTranslationFuture, invertedTranslationFuture).join();

        List<Translation> translations = Collections.emptyList();
        List<Translation> invertedTranslations = Collections.emptyList();
        try {
            translations = Objects.requireNonNullElse(Parallelization.getWithoutExecutionException(normalTranslationFuture), Collections.emptyList());
            invertedTranslations = Objects.requireNonNullElse(Parallelization.getWithoutExecutionException(invertedTranslationFuture), Collections.emptyList());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return getTranslatedWords(translations, invertedTranslations);
    }

    public void linkWords(WordDto firstDto, WordDto secondDto)
    {
        Word firstWord = saveOrGetWord(firstDto);
        Word secondWord = saveOrGetWord(secondDto);
        if (isNotLinked(firstWord, secondWord) && isNotLinked(secondWord, firstWord)) {
            Translation translation = WordConverter.convertToTranslation(firstWord, secondWord);
            translationRepo.save(translation);
        }
    }

    public void saveWords(List<WordDto> words)
    {
        words.forEach(this::saveOrGetWord);
    }

    public void saveWords(Long languageId, List<String> words)
    {
        words.forEach(wordLetters -> saveOrGetWord(WordConverter.convertToDto(languageId, wordLetters)));
    }

    public Word saveOrGetWord(WordDto word)
    {
        Word result = getWord(word);
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

    private Long saveOrOverwriteWord(Word word) throws IllegalIdentifierException
    {
        if (word.getWordNumber() == null) {
            Optional<Long> id = wordRepo.getHighestId(word.getLanguageId());
            word.setWordNumber((id.isPresent() ? id.get() : -1) + 1);
        }

        wordRepo.save(word);
        return word.getWordNumber();
    }

    @Nullable
    private Word getWord(WordDto word)
    {
        return wordRepo.getByLanguageIdAndLetters(word.getLanguageId(), word.getWord()).orElse(null);
    }

    private List<Word> getTranslatedWords(List<Translation> translations, List<Translation> invertedTranslations)
    {
        invertedTranslations.forEach(Translation::swap);
        translations.addAll(invertedTranslations);

        List<Word> translatedWords = new ArrayList<>();
        List<CompletableFuture<Word>> wordFutures = new ArrayList<>();

        translations.forEach(translation -> wordFutures.add(Parallelization.runAndGetFuture(wordRepo.getByLanguageIdAndWordNumber(translation.getTranslatedLanguageId(), translation.getTranslatedWordNumber()))));

        wordFutures.forEach(CompletableFuture::join);

        wordFutures.forEach(future -> {
            Word translatedWord = Parallelization.getWithoutException(future);
            if(translatedWord != null)
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
