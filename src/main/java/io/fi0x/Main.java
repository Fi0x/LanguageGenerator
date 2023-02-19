package io.fi0x;

import io.fi0x.javalogger.logging.Logger;
import io.fi0x.logic.FileLoader;
import io.fi0x.logic.LOG;
import io.fi0x.logic.LanguageTraits;
import io.fi0x.logic.Setup;
import io.fi0x.ui.tui.Menu;

import java.io.File;
import java.io.IOException;

public class Main
{
    public static File languageFolder = new File(System.getenv("APPDATA") + File.separator + "LanguageGenerator");
    public static void main(String[] args)
    {
        Setup.initializeLogger();
        Logger.log("Programm starting...", LOG.INFO);

        try
        {
            if(Setup.createDefaultFileStructure())
                Logger.log("Language folder created at " + languageFolder.getPath(), LOG.INFO);
            else
                Logger.log("Language folder exists at " + languageFolder.getPath(), LOG.INFO);
        } catch(Exception e)
        {
            Logger.log("Could not create language folder at " + languageFolder.getPath(), LOG.INFO);
        }
        try
        {
            if(Setup.createTemplateLanguageFile())
                Logger.log("Template for language file created in language folder", LOG.INFO);
            else
                Logger.log("Template for language file already exists", LOG.INFO);
        } catch(IOException e)
        {
            Logger.log("Could not create template for language file in language folder", LOG.INFO);
        }

        int langAmount = FileLoader.loadAllLanguageFiles();
        Logger.log(langAmount + " language" + (langAmount > 1 ? "s" : "") + " loaded", LOG.INFO);

        LanguageTraits.loadTemplateLanguage();
        Logger.log("Template language is now active", LOG.INFO);

        Menu.start();
    }
}
