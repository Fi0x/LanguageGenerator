package io.fi0x;

import io.fi0x.logic.FileLoader;
import io.fi0x.logic.LanguageTraits;
import io.fi0x.logic.Setup;
import io.fi0x.ui.gui.MainWindow;
import io.fi0x.ui.tui.Menu;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class Main
{
    public static File languageFolder = new File(System.getenv("APPDATA") + File.separator + "LanguageGenerator");
    public static void main(String[] args)
    {
        Menu.initializeLogger();
        log.info("Programm is starting...");

        try
        {
            if(Setup.createDefaultFileStructure())
                log.info("Language folder created at {}", languageFolder.getPath());
            else
                log.info("Language folder exists at {}", languageFolder.getPath());
        } catch(Exception e)
        {
            log.warn("Could not create language folder at {}", languageFolder.getPath(), e);
        }
        try
        {
            if(Setup.createTemplateLanguageFile())
                log.info("Template for language file created in language folder");
            else
                log.info("Template for language file already exists in language folder");
        } catch(IOException e)
        {
            log.warn("Could not create template for language file in language folder", e);
        }

        int langAmount = FileLoader.loadAllLanguageFiles();
        log.info("{} language{} loaded", langAmount, (langAmount != 1 ? "s" : ""));

        LanguageTraits.loadTemplateLanguage();
        log.info("Template language is now active");

        new Thread(Menu::start).start();
        MainWindow.launchGUI(args);
    }
}