package io.fi0x.languagegenerator.logic;

import io.fi0x.languagegenerator.Main;

import java.io.File;
import java.util.ArrayList;

@Deprecated
public class LanguageTraits
{
    public static int minNameLength = 3;
    public static int maxNameLength = 10;
    public static final ArrayList<String> vocals = new ArrayList<>();
    public static final ArrayList<String> consonants = new ArrayList<>();
    public static final ArrayList<String> vocalConsonant = new ArrayList<>();
    public static final ArrayList<String> consonantVocals = new ArrayList<>();
    public static final ArrayList<String> forbiddenCombinations = new ArrayList<>();

    public static void loadTemplateLanguage()
    {
        String fileName = Main.languageFolder.getPath() + File.separator + "Language Template.json";
        File languageFile = new File(fileName);
        FileLoader.loadLanguageFile(languageFile);
    }

    public static void clearCurrentTraits()
    {
        vocals.clear();
        consonants.clear();
        vocalConsonant.clear();
        consonantVocals.clear();
        forbiddenCombinations.clear();
    }

    public static void setNameLengths(int minLength, int maxLength)
    {
        if(minLength > maxLength)
        {
            minNameLength = maxLength;
            maxNameLength = minLength;
        }
        else
        {
            minNameLength = minLength;
            maxNameLength = maxLength;
        }
    }
}
