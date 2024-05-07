package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.ConsonantCombination;
import io.fi0x.languagegenerator.db.entities.ForbiddenCombination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForbiddenRepository extends JpaRepository<ForbiddenCombination, Long>
{
    List<ForbiddenCombination> findByLanguageId(Long languageId);
}
