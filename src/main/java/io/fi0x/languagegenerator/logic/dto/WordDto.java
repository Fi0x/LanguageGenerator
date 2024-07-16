package io.fi0x.languagegenerator.logic.dto;

import io.fi0x.languagegenerator.db.entities.Word;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordDto
{
    private Long languageId;
    private String word;

    public Word toEntity()
    {
        Word entity = new Word();
        entity.setLanguageId(languageId);
        entity.setLetters(this.word);
        return entity;
    }
}
