package io.fi0x.languagegenerator.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MainApplication {
    public static void main(String[] args) throws Exception
    {
        Setup.createDefaultFileStructure();
        Setup.createTemplateLanguageFile();
        FileLoader.loadAllLanguageFiles();
        LanguageTraits.loadTemplateLanguage();
        log.info("Initial loading complete");

        SpringApplication.run(MainApplication.class, args);
    }
}