package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.VocalConsonantCombination;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VocalConsonantRepository extends JpaRepository<VocalConsonantCombination, Long>
{
    List<VocalConsonantCombination> getAllByLanguageId(Long languageId);

    @Transactional
    void deleteAllByLanguageId(Long languageId);

    @Query(value = "SELECT MAX(ID) FROM vocconcom", nativeQuery = true)
    Optional<Long> getHighestId();
}
