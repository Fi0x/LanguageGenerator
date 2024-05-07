package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.ConsonantCombination;
import io.fi0x.languagegenerator.db.entities.VocalConsonantCombination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VocalConsonantRepository extends JpaRepository<VocalConsonantCombination, Long>
{
    List<VocalConsonantCombination> findByLanguageId(Long languageId);
}
