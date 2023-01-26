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

        //TODO: Create a default folder where languages can be stored (See EDCT) and allow use of these with a list view

        Menu.start();
    }
}
