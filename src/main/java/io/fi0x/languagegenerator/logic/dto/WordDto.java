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
    private String languageName;
    private String word;
    private Integer listIndex;
    private Boolean savedInDb;

    public WordDto(Long languageId, String word)
    {
        this.languageId = languageId;
        this.word = word;
    }

    public Word toEntity()
    {
        Word entity = new Word();
        entity.setLanguageId(languageId);
        entity.setLetters(this.word);
        return entity;
    }
}
