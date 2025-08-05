package io.fi0x.languagegenerator.rest;

import io.fi0x.languagegenerator.db.entities.Word;
import io.fi0x.languagegenerator.logic.dto.WordDto;
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
}
