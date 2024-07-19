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
@IdClass(Word.WordId.class)
@Table(name = "WORD")
public class Word
{
    @Id
    private Long languageId;
    @Id
    private Long wordNumber;

    private String letters;

    @AllArgsConstructor
    public class WordId implements Serializable
    {
        public Long languageId;
        public Long wordNumber;
    }
}
