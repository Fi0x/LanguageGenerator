package io.fi0x.languagegenerator.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "LANG")
public class Language
{
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String username;
    private Boolean isPublic;
    private int minWordLength = 3;
    private int maxWordLength = 10;
}
