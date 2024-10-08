package io.fi0x.languagegenerator.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "LANG")
public class Language
{
    @Id
    private Long id;

    private String name;
    private Boolean realLanguage;
    private String username;
    private Boolean visible;
    private int minWordLength = 3;
    private int maxWordLength = 10;
    private int charsBeforeSpecial;
    private int charsAfterSpecial;
    private int minSpecialChars;
    private int maxSpecialChars;
    private Double specialCharacterChance;
}
