package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.Translation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TranslationRepository extends JpaRepository<Translation, Translation.TranslationId>
{
    List<Translation> getAllByLanguageId(Long languageId);

    List<Translation> getAllByTranslatedLanguageId(Long languageId);

    List<Translation> getAllByLanguageIdAndWordNumber(Long languageId, Long wordNumber);

    List<Translation> getAllByLanguageIdAndWordNumberAndTranslatedLanguageId(Long languageId, Long wordNumber, Long translatedLanguageId);

    Translation getByLanguageIdAndWordNumberAndTranslatedLanguageIdAndTranslatedWordNumber(Long languageId, Long wordNumber, Long translatedLanguageId, Long translatedWordNumber);

    List<Translation> getAllByTranslatedLanguageIdAndTranslatedWordNumber(Long translatedLanguageId, Long translatedWordNumber);
}
