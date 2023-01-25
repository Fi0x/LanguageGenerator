package io.fi0x;

import io.fi0x.javalogger.logging.Logger;

public class Main
{
    public static void main(String[] args)
    {
        Setup.initializeLogger();
        Logger.log("Programm starting...", LOG.INFO);

        LanguageTraits.loadDefaultLanguage();
        Logger.log("Default language loaded", LOG.INFO);

        Menu.start();
    }
}
