package io.fi0x.languagegenerator.logic;

import java.io.File;

@Deprecated
public class InputHandler
{
    public static boolean isInt(String userInput)
    {
        try
        {
            Integer.parseInt(userInput);
        } catch(Exception e)
        {
            return false;
        }
        return true;
    }
    public static int getInt(String userInput) throws Exception
    {
        if(!isInt(userInput))
            throw new Exception("User Input is not a number");
        return Integer.parseInt(userInput);
    }

    public static boolean doesFileExist(String pathToFile)
    {
        File file = new File(pathToFile);
        return file.exists();
    }
    public static File getFile(String pathToFile) throws Exception
    {
        if(!doesFileExist(pathToFile))
            throw new Exception("File does not exist");
        return new File(pathToFile);
    }
}
