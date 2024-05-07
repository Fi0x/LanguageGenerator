package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, Long>
{
    List<Language> findByUsername(String username);
}
