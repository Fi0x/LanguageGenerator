package io.fi0x.languagegenerator.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CONVOCCOM")
public class ConsonantVocalCombination
{
    @Id
    private Long id;
    private Long languageId;
    private Long letterId;
}
