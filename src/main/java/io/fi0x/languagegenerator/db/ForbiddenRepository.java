package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.ConsonantCombination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForbiddenRepository extends JpaRepository<ConsonantCombination, Long>
{
    List<ConsonantCombination> findByLanguageId(String languageId);
}
