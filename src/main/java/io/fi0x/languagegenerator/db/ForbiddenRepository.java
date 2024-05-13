package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.ConsonantCombination;
import io.fi0x.languagegenerator.db.entities.ForbiddenCombination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ForbiddenRepository extends JpaRepository<ForbiddenCombination, Long>
{
    List<ForbiddenCombination> getAllByLanguageId(Long languageId);

    @Query(value = "SELECT MAX(ID) FROM FORBCOM", nativeQuery = true)
    Optional<Long> getHighestId();
}
