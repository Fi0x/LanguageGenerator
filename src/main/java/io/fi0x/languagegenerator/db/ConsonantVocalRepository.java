package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.ConsonantCombination;
import io.fi0x.languagegenerator.db.entities.ConsonantVocalCombination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConsonantVocalRepository extends JpaRepository<ConsonantVocalCombination, Long>
{
    List<ConsonantVocalCombination> findByLanguageId(Long languageId);

    @Query(value = "SELECT MAX(ID) FROM CONVOCCOM", nativeQuery = true)
    Optional<Long> getHighestId();
}
