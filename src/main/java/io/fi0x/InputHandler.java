package io.fi0x;

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
}
