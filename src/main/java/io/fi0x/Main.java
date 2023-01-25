package io.fi0x;

import io.fi0x.javalogger.logging.Logger;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Setup.initializeLogger();

        Logger.log("Programm starting...", LOG.INFO);

        new LanguageTraits().loadDefaultLanguage();

        Logger.log("How many names would you like to generate?", LOG.QUESTION);
        Scanner sc = new Scanner(System.in);
        int count = 5;
        try
        {
            count = sc.nextInt();
            Logger.log("Generating " + count + " names...", LOG.SUCCESS);
        } catch(InputMismatchException ignored)
        {
            Logger.log("Input was not a valid number. Using default amount.", LOG.ERROR);
        }

        for(; count > 0; count--)
            Logger.log(Randomizer.generateName(), LOG.OUTPUT);

        Logger.log("All names generated. Exiting Program...", LOG.INFO);
    }
}
