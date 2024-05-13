package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.ConsonantCombination;
import io.fi0x.languagegenerator.db.entities.ConsonantVocalCombination;
import io.fi0x.languagegenerator.db.entities.VocalCombination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VocalRepository extends JpaRepository<VocalCombination, Long>
{
    List<VocalCombination> getAllByLanguageId(Long languageId);

    @Query(value = "SELECT MAX(ID) FROM VOCCOM", nativeQuery = true)
    Optional<Long> getHighestId();
}
