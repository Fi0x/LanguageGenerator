package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.StartingCombinations;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StartingRepository extends JpaRepository<StartingCombinations, Long>
{
    List<StartingCombinations> getAllByLanguageId(Long languageId);

    @Transactional
    void deleteAllByLanguageId(Long languageId);

    @Query(value = "SELECT MAX(ID) FROM langstartcom", nativeQuery = true)
    Optional<Long> getHighestId();
}
