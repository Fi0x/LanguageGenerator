package io.fi0x.languagegenerator.rest.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import javafx.util.Pair;
import lombok.Data;

@Data
@Entity
public class VocalCombination
{
    /**
     * The combination of the {@link Language} and {@link Letter} ids, with the {@link Language} id coming first.
     */
    @Id
    private Pair<Long, Long> id;
}
