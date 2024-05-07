package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.ConsonantCombination;
import io.fi0x.languagegenerator.db.entities.ConsonantVocalCombination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsonantVocalRepository extends JpaRepository<ConsonantVocalCombination, Long>
{
    List<ConsonantVocalCombination> findByLanguageId(Long languageId);
}
