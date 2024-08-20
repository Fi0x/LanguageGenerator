package io.fi0x.languagegenerator.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@Entity
@IdClass(Word.WordId.class)
@Table(name = "WORD")
public class Word
{
    @Id
    private Long languageId;
    @Id
    private Long wordNumber;

    private String letters;

    //TODO: Test this method
    public WordId getCombinedId()
    {
        WordId id = new WordId();
        id.languageId = this.languageId;
        id.wordNumber = this.wordNumber;
        return id;
    }

    public static class WordId implements Serializable
    {
        public Long languageId;
        public Long wordNumber;

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WordId wordId = (WordId) o;
            return Objects.equals(languageId, wordId.languageId) && Objects.equals(wordNumber, wordId.wordNumber);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(languageId, wordNumber);
        }
    }
}
