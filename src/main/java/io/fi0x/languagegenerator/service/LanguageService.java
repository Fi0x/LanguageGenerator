package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.logic.FileLoader;
import io.fi0x.languagegenerator.rest.entities.Language;
import lombok.Getter;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LanguageService
{
    @Getter
    private static final List<Language> languages = new ArrayList<>();

    public void addLanguage(Language language)
    {
        //TODO: Add language correctly
        languages.add(language);
    }

    public List<Language> getUserLanguages(String username)
    {
        return languages;
        // TODO: Use filtered results, once languages are user-specific
//        return languages.stream().filter(language -> language.getName().equals(username)).toList();
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
