package io.fi0x.languagegenerator.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import javafx.util.Pair;
import lombok.Data;

@Data
@Entity
@Table(name = "FORBCOM")
public class ForbiddenCombination
{
    @Id
    private Long id;
    private Long languageId;
    private Long letterId;
}
