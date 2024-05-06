package io.fi0x.languagegenerator.rest.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import javafx.util.Pair;
import lombok.Data;

@Data
@Entity
public class VocalCombination
{
    @Id
    private Long languageId;

    @Id
    private Long letterId;
}
