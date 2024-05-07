package io.fi0x.languagegenerator.db;

import io.fi0x.languagegenerator.db.entities.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterRepository extends JpaRepository<Letter, Long>
{
}
