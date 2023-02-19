package io.fi0x.logic;

import java.util.ArrayList;
import java.util.Locale;

public class Randomizer
{
    public static String generateName()
    {
        StringBuilder name = new StringBuilder();
        int desiredLength = (int) (Math.random() * (LanguageTraits.maxNameLength - LanguageTraits.minNameLength) + LanguageTraits.minNameLength);
        for(int i = (int) (Math.random() * 4); name.length() < desiredLength && i < desiredLength + 4; i++)
        {
            ArrayList<String> selectedList;
            switch(i % 4)
            {
                case 0 -> selectedList = LanguageTraits.consonantVocals;
                case 1 -> selectedList = LanguageTraits.consonants;
                case 2 -> selectedList = LanguageTraits.vocalConsonant;
                default -> selectedList = LanguageTraits.vocals;
            }
            name.append(getNewRandom(name.toString(), selectedList));
        }

        name.setCharAt(0, String.valueOf(name.charAt(0)).toUpperCase(Locale.ROOT).charAt(0));
        return name.toString();
    }

    private static String getNewRandom(String previousLetters, ArrayList<String> newPossibilities)
    {
        int tries = 0;
        int randomIdx = (int) (Math.random() * newPossibilities.size());

        while(tries < newPossibilities.size())
        {
            String nextPossiblePart = newPossibilities.get((randomIdx + tries) % newPossibilities.size());

            if(isAllowed(previousLetters, nextPossiblePart))
                return nextPossiblePart;

            tries++;
        }
        return "";
    }
    private static boolean isAllowed(String originalPart, String newPart)
    {
        for(String forbidden : LanguageTraits.forbiddenCombinations)
        {
            if((originalPart + newPart).contains(forbidden))
                return false;
        }
        return true;
    }
}
