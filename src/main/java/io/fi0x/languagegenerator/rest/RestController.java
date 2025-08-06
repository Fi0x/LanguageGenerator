package io.fi0x.languagegenerator.rest;

import io.fi0x.languagegenerator.db.entities.Translation;
import io.fi0x.languagegenerator.db.entities.Word;
import io.fi0x.languagegenerator.logic.converter.WordConverter;
import io.fi0x.languagegenerator.logic.dto.LanguageData;
import io.fi0x.languagegenerator.logic.dto.TranslationDto;
import io.fi0x.languagegenerator.logic.dto.WordDto;
import io.fi0x.languagegenerator.service.LanguageService;
import io.fi0x.languagegenerator.service.TranslationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestController
{
	private final TranslationService translationService;
	private final LanguageService languageService;

	@Transactional
	@PostMapping("/word/save")
	public long saveWord(@RequestBody WordDto wordDto)
	{
		log.debug("saveWord() called for dto: {}", wordDto);

		try
		{
			return translationService.saveOrGetWord(wordDto).getWordNumber();
		} catch (IllegalAccessException e)
		{
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"You are not allowed to save words in this language, only the owner can");
		}
	}

	@Transactional
	@PostMapping("/word/delete")
	public void deleteWord(@RequestBody WordDto wordDto)
	{
		log.info("deleteWord() called with word: {}", wordDto);

		Word word = new Word();
		word.setLanguageId(wordDto.getLanguageId());
		word.setWordNumber(wordDto.getWordNumber());

		try
		{
			translationService.deleteWord(word);
		} catch (IllegalAccessException e)
		{
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"User is not allowed to delete any saved words of this language");
		}
	}

	@Transactional
	@PostMapping("/translation/save")
	public WordDto saveTranslation(@RequestBody TranslationDto translation)
	{
		log.info("saveTranslation() called for translation: {}", translation);

		try
		{
			WordDto newDto = WordConverter.convertToDto(
					translationService.linkWords(new WordDto(translation.getLanguageId(), translation.getWord()),
							new WordDto(translation.getTranslatedLanguageId(), translation.getTranslatedWord())));
			LanguageData languageData = languageService.getLanguageData(newDto.getLanguageId());
			newDto.setLanguageName(languageData.getName());
			return newDto;
		} catch (IllegalAccessException e)
		{
			log.info("User tried to save word '{}' in a language, which is not allowed", translation.getWordNumber());
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"You are not allowed to save translations in this language");
		}
	}

	@Transactional
	@PostMapping("/translation/delete")
	public void deleteTranslation(@RequestBody Translation translation)
	{
		log.debug("deleteTranslation() called for translation: {}", translation);

		try
		{
			translationService.deleteTranslation(translation.getLanguageId(), translation.getWordNumber(),
					translation.getTranslatedLanguageId(), translation.getTranslatedWordNumber());
		} catch (IllegalAccessException e)
		{
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"You are not allowed to delete this translation. You need to own both languages in order to do so" + ".");
		}
	}
}
