package io.fi0x;

import io.fi0x.javalogger.logging.Logger;

import java.util.Locale;
import java.util.Scanner;

public class Menu
{
    private static Scanner scanner;
    public static void start()
    {
        scanner = new Scanner(System.in);
        mainMenu();
    }

    private static void mainMenu()
    {
        Logger.log("\n---Main Menu---", LOG.MENU);
        Logger.log("-(L)oad new language file", LOG.MENU);
        Logger.log("-(G)enerate random names with current language (You can also just enter a number)", LOG.MENU);
        Logger.log("-(E)xit program", LOG.MENU);

        String userInput = scanner.next();

        switch(userInput.toUpperCase(Locale.ROOT))
        {
            case "L":
                loadNewFile();
                break;
            case "G":
                generateNames();
                break;
            case "E":
                Logger.log("Exiting program...", LOG.SUCCESS);
                System.exit(0);
                break;
            default:
                if(InputHandler.isInt(userInput))
                {
                    try
                    {
                        int amount = InputHandler.getInt(userInput);
                        Logger.log("Generating " + amount + " names", LOG.SUCCESS);
                        for(; amount > 0; amount--)
                            Logger.log(Randomizer.generateName(), LOG.OUTPUT);
                        Logger.log("Names generated", LOG.SUCCESS);
                        mainMenu();
                    } catch(Exception e)
                    {
                        Logger.log("Invalid input", LOG.ERROR);
                        mainMenu();
                    }
                }
                else
                {
                    Logger.log("Invalid input", LOG.ERROR);
                    mainMenu();
                }
        }
    }
    private static void loadNewFile()
    {
        mainMenu();
    }
    private static void generateNames()
    {
        Logger.log("\n---Generate Names---", LOG.MENU);
        Logger.log("-How many names would you like to generate?", LOG.MENU);
        Logger.log("-(A)bort and go back to main-menu", LOG.MENU);
        Logger.log("-(E)xit program", LOG.MENU);

        String userInput = scanner.next();
        switch(userInput.toUpperCase(Locale.ROOT))
        {
            case "A":
                Logger.log("Returning to main-menu...", LOG.SUCCESS);
                mainMenu();
                break;
            case "E":
                Logger.log("Exiting program...", LOG.SUCCESS);
                System.exit(0);
                break;
            default:
                try
                {
                    int amount = InputHandler.getInt(userInput);
                    Logger.log("Generating " + amount + " names", LOG.SUCCESS);
                    for(; amount > 0; amount--)
                        Logger.log(Randomizer.generateName(), LOG.OUTPUT);
                    Logger.log("Names generated", LOG.SUCCESS);
                } catch(Exception e)
                {
                    Logger.log("Invalid input", LOG.ERROR);
                    generateNames();
                }
        }
        mainMenu();
    }
}
