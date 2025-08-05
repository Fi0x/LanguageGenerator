package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.ForbiddenCombination;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ForbiddenRepository extends JpaRepository<ForbiddenCombination, Long>
{
    List<ForbiddenCombination> getAllByLanguageId(Long languageId);

    @Transactional
    void deleteAllByLanguageId(Long languageId);

    @Query(value = "SELECT MAX(ID) FROM langforbcom", nativeQuery = true)
    Optional<Long> getHighestId();
}
