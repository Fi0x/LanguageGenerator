package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.VocalCombination;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VocalRepository extends JpaRepository<VocalCombination, Long>
{
    List<VocalCombination> getAllByLanguageId(Long languageId);

    @Transactional
    void deleteAllByLanguageId(Long languageId);

    @Query(value = "SELECT MAX(ID) FROM voccom", nativeQuery = true)
    Optional<Long> getHighestId();
}
