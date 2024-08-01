package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Word.WordId>
{
    List<Word> getAllByLanguageId(Long languageId);

    List<Word> getAllByLetters(String letters);

    Optional<Word> getByLanguageIdAndLetters(Long languageId, String letters);

    Word getByLanguageIdAndWordNumber(Long languageId, Long wordNumber);

    void deleteAllByLanguageId(Long languageId);

    @Query(value = "SELECT MAX(wordNumber) FROM Word WHERE languageId = :languageId")
    Optional<Long> getHighestId(@Param("languageId") Long languageId);
}
