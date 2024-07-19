package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Word.WordId>
{
    List<Word> getAllByLanguageId(Long languageId);

    List<Word> getAllByLetters(String letters);

    Word getByLanguageIdAndLetters(Long languageId, String letters);

    Word getByLanguageIdAndWordNumber(Long languageId, Long wordNumber);

    @Transactional
    void deleteAllByLanguageId(Long languageId);

    @Query(value = "SELECT MAX(WORD_NUMBER) FROM word WHERE LANGUAGE_ID = ${languageId}", nativeQuery = true)
    Optional<Long> getHighestId(Long languageId);
}
