package io.fi0x.ui.tui;

import io.fi0x.javalogger.logging.LogEntry;
import io.fi0x.javalogger.logging.Logger;
import io.fi0x.logic.*;

import java.util.ArrayList;
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
        Logger.log("-(G)enerate random names with current language (You can also just enter a number)", LOG.MENU);
        Logger.log("-(S)how a list of all loaded languages", LOG.MENU);
        Logger.log("-(A)ctivate loaded language", LOG.MENU);
        Logger.log("-(L)oad new language file", LOG.MENU);
        Logger.log("-(T)ype in new language traits", LOG.MENU);
        Logger.log("-(E)xit program", LOG.MENU);

        String userInput = scanner.nextLine();

        switch(userInput.toUpperCase(Locale.ROOT))
        {
            case "L":
                loadNewFile();
                break;
            case "T":
                typeInLanguageTraits();
                break;
            case "G":
                generateNames();
                break;
            case "S":
                Logger.log("Loaded Languages:", LOG.SUCCESS);
                for(String name : FileLoader.getLoadedLanguageNames(true))
                    Logger.log(name, LOG.OUTPUT);
                mainMenu();
                break;
            case "A":
                Logger.log("Please type in the name of the language you want to change to or its number from the list", LOG.QUESTION);
                if(!FileLoader.loadLanguageFile(scanner.nextLine()))
                    Logger.log("Could not find the specified language file", LOG.ERROR);
                mainMenu();
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
                        Randomizer.generateWords(amount);
                        Logger.log("Names generated", LOG.SUCCESS);
                    } catch(Exception e)
                    {
                        Logger.log("Invalid input", LOG.ERROR);
                    }
                }
                else
                    Logger.log("Invalid input", LOG.ERROR);
                mainMenu();
                break;
        }
    }
    private static void loadNewFile()
    {
        Logger.log("\n---Load Language File---", LOG.MENU);
        Logger.log("-Type in the complete path and the name with extension of the language file", LOG.MENU);
        Logger.log("-(A)bort and go back to main-menu", LOG.MENU);
        Logger.log("-(E)xit program", LOG.MENU);

        String userInput = scanner.nextLine();
        switch(userInput.toUpperCase(Locale.ROOT))
        {
            case "A" -> Logger.log("Returning to main-menu...", LOG.SUCCESS);
            case "E" ->
            {
                Logger.log("Exiting program...", LOG.SUCCESS);
                System.exit(0);
            }
            default ->
            {
                Logger.log("Loading file...", LOG.SUCCESS);
                if(InputHandler.doesFileExist(userInput))
                {
                    try
                    {
                        FileLoader.loadLanguageFile(InputHandler.getFile(userInput));
                    } catch(Exception e)
                    {
                        LogEntry log = new LogEntry("Could not load file", LOG.ERROR).EXCEPTION(e);
                        Logger.log(log);
                    }
                } else
                {
                    Logger.log("Could not find the specified file", LOG.ERROR);
                    loadNewFile();
                }
            }
        }

        mainMenu();
    }
    private static void typeInLanguageTraits()
    {
        Logger.log("\n---Setup Language Traits---", LOG.MENU);
        Logger.log("-(N)ew frequently used letters or forbidden combinations", LOG.MENU);
        Logger.log("-(R)ules for this language", LOG.MENU);
        Logger.log("-(C)lear all current settings and remove stored information", LOG.MENU);
        Logger.log("-(A)bort and go back to main-menu", LOG.MENU);
        Logger.log("-(E)xit program", LOG.MENU);

        String userInput = scanner.nextLine();
        switch(userInput.toUpperCase(Locale.ROOT))
        {
            case "A" -> Logger.log("Returning to main-menu...", LOG.SUCCESS);
            case "E" ->
            {
                Logger.log("Exiting program...", LOG.SUCCESS);
                System.exit(0);
            }
            case "N" ->
            {
                addFrequentlyUsedLetters();
                return;
            }
            case "R" ->
            {
                changeLanguageRules();
                return;
            }
            case "C" ->
            {
                Logger.log("Removing all information of the current language...", LOG.SUCCESS);
                LanguageTraits.clearCurrentTraits();
                typeInLanguageTraits();
                return;
            }
            default ->
            {
                Logger.log("Invalid input", LOG.ERROR);
                typeInLanguageTraits();
                return;
            }
        }

        mainMenu();
    }
    private static void addFrequentlyUsedLetters()
    {
        Logger.log("\n---Add frequently used letters or forbidden combinations---", LOG.MENU);
        Logger.log("-(V)ocals", LOG.MENU);
        Logger.log("-(C)onsonants", LOG.MENU);
        Logger.log("-(VO)cal start, consonant end", LOG.MENU);
        Logger.log("-(CO)nsonant start, vocal end", LOG.MENU);
        Logger.log("-(F)orbidden combinations", LOG.MENU);
        Logger.log("-(A)bort and go back to main-menu", LOG.MENU);
        Logger.log("-(E)xit program", LOG.MENU);

        String userInput = scanner.nextLine();
        switch(userInput.toUpperCase(Locale.ROOT))
        {
            case "A" -> Logger.log("Returning to main-menu...", LOG.SUCCESS);
            case "E" ->
            {
                Logger.log("Exiting program...", LOG.SUCCESS);
                System.exit(0);
            }
            case "V" ->
            {
                Logger.log("Type each vocal in a new line. Add an empty line to finish", LOG.QUESTION);
                LanguageTraits.vocals.addAll(getFullLines());
                addFrequentlyUsedLetters();
                return;
            }
            case "C" ->
            {
                Logger.log("Type each consonant in a new line. Add an empty line to finish", LOG.QUESTION);
                LanguageTraits.consonants.addAll(getFullLines());
                addFrequentlyUsedLetters();
                return;
            }
            case "VO" ->
            {
                Logger.log("Type each vocal-consonant-combination in a new line. Add an empty line to finish", LOG.QUESTION);
                LanguageTraits.vocalConsonant.addAll(getFullLines());
                addFrequentlyUsedLetters();
                return;
            }
            case "CO" ->
            {
                Logger.log("Type each consonant-vocal-combination in a new line. Add an empty line to finish", LOG.QUESTION);
                LanguageTraits.consonantVocals.addAll(getFullLines());
                addFrequentlyUsedLetters();
                return;
            }
            case "F" ->
            {
                Logger.log("Type each combination of letters in a new line. Add an empty line to finish", LOG.QUESTION);
                LanguageTraits.forbiddenCombinations.addAll(getFullLines());
                addFrequentlyUsedLetters();
                return;
            }
            default ->
            {
                Logger.log("Invalid input", LOG.ERROR);
                addFrequentlyUsedLetters();
                return;
            }
        }
        mainMenu();
    }
    private static void changeLanguageRules()
    {
        Logger.log("\n---Change Language Rules---", LOG.MENU);
        Logger.log("-(N)ame lengths", LOG.MENU);
        Logger.log("-(A)bort and go back to main-menu", LOG.MENU);
        Logger.log("-(E)xit program", LOG.MENU);

        String userInput = scanner.nextLine();
        switch(userInput.toUpperCase(Locale.ROOT))
        {
            case "A" -> Logger.log("Returning to main-menu...", LOG.SUCCESS);
            case "E" ->
            {
                Logger.log("Exiting program...", LOG.SUCCESS);
                System.exit(0);
            }
            case "N" ->
            {
                Logger.log("How long should the shortest names be?", LOG.QUESTION);
                userInput = scanner.nextLine();
                int min = 3;
                if(InputHandler.isInt(userInput))
                {
                    try
                    {
                        min = InputHandler.getInt(userInput);
                    } catch(Exception e)
                    {
                        Logger.log("Invalid input", LOG.ERROR);
                        changeLanguageRules();
                        return;
                    }
                }
                Logger.log("How long should the longest names be?", LOG.QUESTION);
                userInput = scanner.nextLine();
                int max = 10;
                if(InputHandler.isInt(userInput))
                {
                    try
                    {
                        max = InputHandler.getInt(userInput);
                    } catch(Exception e)
                    {
                        Logger.log("Invalid input", LOG.ERROR);
                        changeLanguageRules();
                        return;
                    }
                }
                LanguageTraits.setNameLengths(min, max);
                changeLanguageRules();
                return;
            }
            default ->
            {
                Logger.log("Invalid input", LOG.ERROR);
                changeLanguageRules();
                return;
            }
        }
        mainMenu();
    }
    private static void generateNames()
    {
        Logger.log("\n---Generate Names---", LOG.MENU);
        Logger.log("-How many names would you like to generate?", LOG.MENU);
        Logger.log("-(A)bort and go back to main-menu", LOG.MENU);
        Logger.log("-(E)xit program", LOG.MENU);

        String userInput = scanner.nextLine();
        switch(userInput.toUpperCase(Locale.ROOT))
        {
            case "A":
                Logger.log("Returning to main-menu...", LOG.SUCCESS);
                break;
            case "E":
                Logger.log("Exiting program...", LOG.SUCCESS);
                System.exit(0);
            default:
                try
                {
                    int amount = InputHandler.getInt(userInput);
                    Logger.log("Generating " + amount + " names", LOG.SUCCESS);
                    Randomizer.generateWords(amount);
                    Logger.log("Names generated", LOG.SUCCESS);
                } catch(Exception e)
                {
                    Logger.log("Invalid input", LOG.ERROR);
                    generateNames();
                }
        }
        mainMenu();
    }

    private static ArrayList<String> getFullLines()
    {
        ArrayList<String> lines = new ArrayList<>();

        String line;
        do
        {
            line = scanner.nextLine();
            lines.add(line);
        } while(!line.equals(""));
        lines.remove(lines.size() - 1);

        return lines;
    }
}
