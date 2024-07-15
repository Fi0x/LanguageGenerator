package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.Translation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TranslationRepository extends JpaRepository<Translation, Translation.TranslationId>
{
    List<Translation> getAllByLanguageId(Long languageId);

    List<Translation> getAllByTranslatedLanguageId(Long languageId);

    List<Translation> getAllByLanguageIdAndWordNumber(Long languageId, Long wordNumber);

    List<Translation> getAllByTranslatedLanguageIdAndTranslatedWordNumber(Long translatedLanguageId, Long translatedWordNumber);
}
