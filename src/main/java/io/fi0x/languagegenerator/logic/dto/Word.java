package io.fi0x.languagegenerator.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Word
{
    private Long languageId;
    private String word;

    //TODO: Add tests for this method
    public io.fi0x.languagegenerator.db.entities.Word toEntity()
    {
        io.fi0x.languagegenerator.db.entities.Word entity = new io.fi0x.languagegenerator.db.entities.Word();
        entity.setLanguageId(languageId);
        entity.setLetters(this.word);
        return entity;
    }
}
