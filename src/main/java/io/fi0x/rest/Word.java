package io.fi0x.rest;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Word
{
    @Id
    @GeneratedValue
    private Long id;
    private String language;
    private String word;

    public Word(String language, String word)
    {
        this.language = language;
        this.word = word;
    }
}
