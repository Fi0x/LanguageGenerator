package io.fi0x.languagegenerator.client.logic;

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

    public static void loadTemplateLanguage(File languageFolder)
    {
        String fileName = languageFolder.getPath() + File.separator + "Language Template.json";
        File languageFile = new File(fileName);
        FileLoader.loadLanguageFile(languageFile);
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
