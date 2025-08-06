package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.ConsonantVocalCombination;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConsonantVocalRepository extends JpaRepository<ConsonantVocalCombination, Long>
{
    List<ConsonantVocalCombination> getAllByLanguageId(Long languageId);

    @Transactional
    void deleteAllByLanguageId(Long languageId);

    @Query(value = "SELECT MAX(ID) FROM langconvoccom", nativeQuery = true)
    Optional<Long> getHighestId();
}
