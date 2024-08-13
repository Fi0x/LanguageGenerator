package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long>
{
    List<Language> getAllByName(String name);
    List<Language> getAllByUsername(String username);

    List<Language> getAllByVisible(boolean visible);

    @Query(value = "SELECT MAX(ID) FROM lang", nativeQuery = true)
    Optional<Long> getHighestId();
}
