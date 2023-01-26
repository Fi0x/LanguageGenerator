package io.fi0x;

import java.util.Locale;

public class Randomizer
{
    public static String generateName()
    {
        StringBuilder name = new StringBuilder();
        int desiredLength = (int) (Math.random() * (LanguageTraits.maxNameLength - LanguageTraits.minNameLength) + LanguageTraits.minNameLength);
        for(int i = (int) (Math.random() * 4); name.length() < desiredLength; i++)
        {
            switch(i % 4)
            {
                case 0 -> name.append(vokalEnd());
                case 1 -> name.append(randomKonsonant());
                case 2 -> name.append(vokalStart());
                default -> name.append(randomVokal());
            }
        }

        name.setCharAt(0, String.valueOf(name.charAt(0)).toUpperCase(Locale.ROOT).charAt(0));
        return name.toString();
    }

    private static String vokalEnd()
    {
        return LanguageTraits.consonantVocals.get((int) (Math.random() * LanguageTraits.consonantVocals.size()));
    }
    private static String vokalStart()
    {
        return LanguageTraits.vocalConsonant.get((int) (Math.random() * LanguageTraits.vocalConsonant.size()));
    }
    private static String randomVokal()
    {
        return LanguageTraits.vocals.get((int) (Math.random() * LanguageTraits.vocals.size()));
    }
    private static String randomKonsonant()
    {
        return LanguageTraits.consonants.get((int) (Math.random() * LanguageTraits.consonants.size()));
    }
}
