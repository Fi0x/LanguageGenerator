package io.fi0x.languagegenerator.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@IdClass(Translation.TranslationId.class)
@Table(name = "TRANS")
public class Translation
{
    @Id
    private Long languageId;
    @Id
    private Long wordNumber;
    @Id
    private Long translatedLanguageId;
    @Id
    private Long translatedWordNumber;

    //TODO: Write test for this method
    public void swap()
    {
        Long tmp = languageId;
        languageId = translatedLanguageId;
        translatedLanguageId = tmp;
        tmp = wordNumber;
        wordNumber = translatedWordNumber;
        translatedWordNumber = tmp;
    }

    @AllArgsConstructor
    public class TranslationId implements Serializable
    {
        private Long languageId;
        private Long wordNumber;
        private Long translatedLanguageId;
        private Long translatedWordNumber;
    }
}
