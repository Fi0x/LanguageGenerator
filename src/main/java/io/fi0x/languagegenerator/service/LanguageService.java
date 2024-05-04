package io.fi0x.languagegenerator.service;

import io.fi0x.languagegenerator.rest.entities.Language;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LanguageService
{
    private static final List<Language> languages = new ArrayList<>();

    public void addLanguage(Language language)
    {
        //TODO: Add language correctly
        languages.add(language);
    }
}
