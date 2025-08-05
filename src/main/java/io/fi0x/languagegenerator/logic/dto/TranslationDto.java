package io.fi0x.languagegenerator.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranslationDto
{
	private Long languageId;
	private Long wordNumber;
	private Long translatedLanguageId;
	private Long translatedWordNumber;
	private String word;
	private String translatedWord;
}
