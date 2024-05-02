package io.fi0x.languagegenerator.rest.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    private int minWordLength = 3;
    private int maxWordLength = 10;
}
