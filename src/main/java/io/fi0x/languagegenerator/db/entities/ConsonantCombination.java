package io.fi0x.languagegenerator.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import javafx.util.Pair;
import lombok.Data;

@Data
@Entity
@Table(name = "CONCOM")
public class ConsonantCombination
{
    @Id
    private Long languageId;

    @Id
    private Long letterId;
}
