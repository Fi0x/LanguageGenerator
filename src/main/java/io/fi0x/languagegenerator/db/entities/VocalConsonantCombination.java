package io.fi0x.languagegenerator.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "LANGVOCCONCOM")
public class VocalConsonantCombination
{
    @Id
    private Long id;
    private Long languageId;
    private Long letterId;
}
