package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.SpecialCharacterCombinations;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SpecialCharacterRepository extends JpaRepository<SpecialCharacterCombinations, Long>
{
    List<SpecialCharacterCombinations> getAllByLanguageId(Long languageId);

    @Transactional
    void deleteAllByLanguageId(Long languageId);

    @Query(value = "SELECT MAX(ID) FROM spechacom", nativeQuery = true)
    Optional<Long> getHighestId();
}
