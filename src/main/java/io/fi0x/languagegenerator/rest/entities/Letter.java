package io.fi0x.languagegenerator.rest.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Letter
{
    @Id
    @GeneratedValue
    private Long id;
    private String letters;
}
