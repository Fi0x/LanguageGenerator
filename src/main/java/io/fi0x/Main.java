package io.fi0x;

import io.fi0x.javalogger.logging.Logger;

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

        LanguageTraits.loadDefaultLanguage();
        Logger.log("Default language loaded", LOG.INFO);

        Menu.start();
    }
}
