package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.db.LanguageRepository;
import io.fi0x.languagegenerator.logic.FileLoader;
import io.fi0x.languagegenerator.db.entities.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class LanguageService
{
    private LanguageRepository languageRepository;

    @Getter
    private static final List<Language> languages = new ArrayList<>();

    public void addLanguage(Language language)
    {
        //TODO: Add language correctly
        languages.add(language);
    }

    public List<Language> getUserLanguages(String username)
    {
        return languageRepository.findByUsername(username);
    }

    public static void loadInitialLanguages()
    {
        FileLoader.getLoadedLanguageNames(false).forEach(name -> {
            Language language = new Language();
            language.setName(name);
            languages.add(language);
        });
    }
}
