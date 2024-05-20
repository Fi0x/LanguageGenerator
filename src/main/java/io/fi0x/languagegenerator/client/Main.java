package io.fi0x.languagegenerator.client;

import io.fi0x.languagegenerator.client.gui.MainWindow;
import io.fi0x.languagegenerator.client.logic.FileLoader;
import io.fi0x.languagegenerator.client.logic.LanguageTraits;
import io.fi0x.languagegenerator.client.logic.Setup;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
@Deprecated
public class Main
{
    public static File languageFolder = new File(System.getenv("APPDATA") + File.separator + "LanguageGenerator");
    public static void main(String[] args)
    {
        log.info("Programm is starting...");

        try
        {
            if(Setup.createDefaultFileStructure(languageFolder))
                log.info("Language folder created at {}", languageFolder.getPath());
            else
                log.info("Language folder exists at {}", languageFolder.getPath());
        } catch(Exception e)
        {
            log.warn("Could not create language folder at {}", languageFolder.getPath(), e);
        }
        try
        {
            if(Setup.createTemplateLanguageFile(languageFolder))
                log.info("Template for language file created in language folder");
            else
                log.info("Template for language file already exists in language folder");
        } catch(IOException e)
        {
            log.warn("Could not create template for language file in language folder", e);
        }

        int langAmount = FileLoader.loadAllLanguageFiles(languageFolder);
        log.info("{} language{} loaded", langAmount, (langAmount != 1 ? "s" : ""));

        LanguageTraits.loadTemplateLanguage(languageFolder);
        log.info("Template language is now active");

        MainWindow.launchGUI(args);
    }
}