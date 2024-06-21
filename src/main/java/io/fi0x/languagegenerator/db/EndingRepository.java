package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.EndingCombinations;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EndingRepository extends JpaRepository<EndingCombinations, Long>
{
    List<EndingCombinations> getAllByLanguageId(Long languageId);

    @Transactional
    void deleteAllByLanguageId(Long languageId);

    @Query(value = "SELECT MAX(ID) FROM endcom", nativeQuery = true)
    Optional<Long> getHighestId();
}
