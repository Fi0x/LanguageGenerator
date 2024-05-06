package io.fi0x.languagegenerator.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Language
{
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String username;
    private int minWordLength = 3;
    private int maxWordLength = 10;
}
