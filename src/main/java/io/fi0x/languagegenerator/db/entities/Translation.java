package io.fi0x.languagegenerator.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

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

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TranslationId that = (TranslationId) o;
            return Objects.equals(languageId, that.languageId) && Objects.equals(wordNumber, that.wordNumber) && Objects.equals(translatedLanguageId, that.translatedLanguageId) && Objects.equals(translatedWordNumber, that.translatedWordNumber);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(languageId, wordNumber, translatedLanguageId, translatedWordNumber);
        }
    }
}
