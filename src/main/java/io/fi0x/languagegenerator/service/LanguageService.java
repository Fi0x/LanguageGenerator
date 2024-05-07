package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.db.LanguageRepository;
import io.fi0x.languagegenerator.db.entities.Language;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@AllArgsConstructor
public class LanguageService
{
    private LanguageRepository languageRepository;

    public void addLanguage(Language language)
    {
        languageRepository.save(language);
    }

    public List<Language> getUserLanguages(String username)
    {
        return languageRepository.findByUsername(username);
    }
}
