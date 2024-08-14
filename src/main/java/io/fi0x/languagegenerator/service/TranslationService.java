package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.db.LanguageRepository;
import io.fi0x.languagegenerator.db.TranslationRepository;
import io.fi0x.languagegenerator.db.WordRepository;
import io.fi0x.languagegenerator.db.entities.Language;
import io.fi0x.languagegenerator.db.entities.Translation;
import io.fi0x.languagegenerator.db.entities.Word;
import io.fi0x.languagegenerator.logic.converter.WordConverter;
import io.fi0x.languagegenerator.logic.dto.WordDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TranslationService
{
    private static final String ENGLISH_LANGUAGE_NAME = "English";

    private final WordRepository wordRepo;
    private final TranslationRepository translationRepo;
    private final LanguageRepository languageRepository;

    public Map<Long, String> getEnglishTranslations(List<Word> wordList)
    {
        List<Language> englishEntities = languageRepository.getAllByName(ENGLISH_LANGUAGE_NAME);

        if (englishEntities.isEmpty()) {
            Language english = new Language();
            english.setName(ENGLISH_LANGUAGE_NAME);
            english.setRealLanguage(true);
            english.setVisible(true);
            english.setSpecialCharacterChance(0.0);

            Optional<Long> id = languageRepository.getHighestId();
            english.setId((id.isPresent() ? id.get() : -1) + 1);

            languageRepository.save(english);
            return wordList.stream().collect(Collectors.toMap(Word::getWordNumber, word -> ""));
        }
        if (englishEntities.size() > 1)
            throw new IllegalStateException("There were multiple english languages found in the database");

        Map<Long, String> resultMap = new HashMap<>();
        for (Word word : wordList) {
            List<Word> translations = getTranslations(WordConverter.convertToDto(word), englishEntities.get(0).getId());
            resultMap.put(word.getWordNumber(), translations.toString().replace("[", "").replace("]", ""));
        }

        return resultMap;
    }

    public List<Word> getTranslations(WordDto originalWord)
    {
        log.trace("getTranslations() called with wordDto={}", originalWord);

        Word word = getSavedWord(originalWord);
        if (word == null)
            return Collections.emptyList();

        List<Translation> translations = translationRepo.getAllByLanguageIdAndWordNumber(word.getLanguageId(), word.getWordNumber());
        List<Translation> invertedTranslations = translationRepo.getAllByTranslatedLanguageIdAndTranslatedWordNumber(word.getLanguageId(), word.getWordNumber());
        return getTranslatedWords(translations, invertedTranslations);
    }

    public List<Word> getTranslations(WordDto originalWord, Long desiredLanguageId)
    {
        log.trace("getTranslations() called with wordDto={} and languageId={}", originalWord, desiredLanguageId);

        Word word = getSavedWord(originalWord);
        if (word == null)
            return Collections.emptyList();

        List<Translation> translations = translationRepo.getAllByLanguageIdAndWordNumberAndTranslatedLanguageId(word.getLanguageId(), word.getWordNumber(), desiredLanguageId);
        List<Translation> invertedTranslations = translationRepo.getAllByTranslatedLanguageIdAndTranslatedWordNumberAndLanguageId(word.getLanguageId(), word.getWordNumber(), desiredLanguageId);
        return getTranslatedWords(translations, invertedTranslations);
    }

    public void deleteTranslation(long firstLanguage, long firstWordNumber, long secondLanguage, long secondWordNumber) throws IllegalAccessException
    {
        log.trace("deleteTranslation() called with language={} / word={} and language={} / word={}", firstLanguage, firstWordNumber, secondLanguage, secondWordNumber);

        String firstLanguageCreator = getLanguageCreator(firstLanguage);
        String secondLanguageCreator = getLanguageCreator(secondLanguage);

        if (firstLanguageCreator == null || !firstLanguageCreator.equals(secondLanguageCreator) || !secondLanguageCreator.equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            throw new IllegalAccessException();

        Translation translation = new Translation();
        translation.setLanguageId(firstLanguage);
        translation.setWordNumber(firstWordNumber);
        translation.setTranslatedLanguageId(secondLanguage);
        translation.setTranslatedWordNumber(secondWordNumber);
        translationRepo.deleteById(translation.getCombinedId());
        translation.swap();
        translationRepo.deleteById(translation.getCombinedId());
    }

    public Word linkWords(WordDto originalWordDto, WordDto translatedWordDto) throws IllegalAccessException
    {
        log.trace("linkWords() called with wordDtos={} and {}", originalWordDto, translatedWordDto);

        Word originalWord = saveOrGetWord(originalWordDto);
        Word translatedWord = saveOrGetWord(translatedWordDto);
        if (isNotLinked(originalWord, translatedWord) && isNotLinked(translatedWord, originalWord)) {
            Translation translation = WordConverter.convertToTranslation(originalWord, translatedWord);
            translationRepo.save(translation);
        }

        return translatedWord;
    }

    public void saveWords(List<WordDto> words) throws IllegalAccessException
    {
        log.trace("saveWords() called with wordDtos={}", words);

        for (WordDto word : words)
            saveOrGetWord(word);
    }

    public void saveWords(Long languageId, List<String> words) throws IllegalAccessException
    {
        log.trace("saveWords() called with languageId={} and words={}", languageId, words);

        for (String word : words)
            saveOrGetWord(new WordDto(languageId, word));
    }

    public Word saveOrGetWord(WordDto wordDto) throws IllegalAccessException
    {
        log.trace("saveOrGetWord() called with wordDto={}", wordDto);

        Word result = getSavedWord(wordDto);
        if (result != null)
            return result;

        Optional<Language> languageEntity = languageRepository.findById(wordDto.getLanguageId());
        if (languageEntity.isEmpty() || (!languageEntity.get().getRealLanguage() &&
                (languageEntity.get().getUsername() == null || !languageEntity.get().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName()))))
            throw new IllegalAccessException();

        Long id = saveOrOverwriteWord(wordDto.toEntity());
        wordDto.setSavedInDb(true);
        wordDto.setWordNumber(id);

        return wordDto.toEntity();
    }

    public void deleteWord(Word word) throws IllegalAccessException
    {
        log.trace("deleteWord() called with word={}", word);

        String languageCreator = getLanguageCreator(word.getLanguageId());
        if (languageCreator == null || !languageCreator.equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            throw new IllegalAccessException();

        wordRepo.deleteById(word.getCombinedId());
    }

    public List<Word> getAllWords(long languageId)
    {
        log.trace("getAllWords() called with languageId={}", languageId);

        return wordRepo.getAllByLanguageId(languageId);
    }

    @Nullable
    private Word getSavedWord(WordDto word)
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

    private List<Word> getTranslatedWords(List<Translation> translations, List<Translation> invertedTranslations)
    {
        List<Word> translatedWords = new ArrayList<>();
        translations.forEach(translation -> {
            Word translatedWord = wordRepo.getByLanguageIdAndWordNumber(translation.getTranslatedLanguageId(), translation.getTranslatedWordNumber());
            if (translatedWord != null)
                translatedWords.add(translatedWord);
        });
        invertedTranslations.forEach(translation -> {
            Word translatedWord = wordRepo.getByLanguageIdAndWordNumber(translation.getLanguageId(), translation.getWordNumber());
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

    @Nullable
    private String getLanguageCreator(long languageId)
    {
        Optional<Language> data = languageRepository.findById(languageId);
        return data.map(Language::getUsername).orElse(null);
    }
}
