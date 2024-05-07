package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.ConsonantCombination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConsonantRepository extends JpaRepository<ConsonantCombination, Long>
{
    List<ConsonantCombination> findByLanguageId(Long languageId);

    @Query(value = "SELECT MAX(ID) FROM CONCOM", nativeQuery = true)
    Optional<Long> getHighestId();
}
