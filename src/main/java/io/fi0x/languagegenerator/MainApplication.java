package io.fi0x.languagegenerator;

import io.fi0x.languagegenerator.logic.FileLoader;
import io.fi0x.languagegenerator.logic.LanguageTraits;
import io.fi0x.languagegenerator.logic.Setup;
import io.fi0x.languagegenerator.service.LanguageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MainApplication
{
    public static void main(String[] args) throws Exception
    {
        Setup.createDefaultFileStructure();
        Setup.createTemplateLanguageFile();
        FileLoader.loadAllLanguageFiles();
        LanguageTraits.loadTemplateLanguage();
        LanguageService.loadInitialLanguages();
        log.info("Initial loading complete");

        SpringApplication.run(MainApplication.class, args);
    }
}
