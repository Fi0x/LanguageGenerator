package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long>
{
    List<Language> getAllByUsername(String username);

    List<Language> getAllByIsPublic(boolean isPublic);

    @Query(value = "SELECT MAX(ID) FROM LANG", nativeQuery = true)
    Optional<Long> getHighestId();
}
