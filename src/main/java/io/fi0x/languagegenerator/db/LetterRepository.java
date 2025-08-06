package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.Letter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LetterRepository extends JpaRepository<Letter, Long>
{
    List<Letter> getAllByLetters(String letters);

    @Query(value = "SELECT MAX(ID) FROM langlttrs", nativeQuery = true)
    Optional<Long> getHighestId();
}
