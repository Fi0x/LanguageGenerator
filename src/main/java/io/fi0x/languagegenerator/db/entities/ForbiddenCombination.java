package io.fi0x.languagegenerator.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import javafx.util.Pair;
import lombok.Data;

@Data
@Entity
public class ForbiddenCombination
{
    @Id
    private Long languageId;

    @Id
    private Long letterId;
}
