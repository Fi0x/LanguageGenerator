package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.ConsonantCombination;
import io.fi0x.languagegenerator.db.entities.ConsonantVocalCombination;
import io.fi0x.languagegenerator.db.entities.VocalCombination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VocalRepository extends JpaRepository<VocalCombination, Long>
{
    List<VocalCombination> findByLanguageId(Long languageId);
}
