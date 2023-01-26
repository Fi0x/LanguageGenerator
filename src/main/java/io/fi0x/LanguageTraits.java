package io.fi0x;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class LanguageTraits
{
    public static int minNameLength = 3;
    public static int maxNameLength = 10;
    public static final ArrayList<String> vocals = new ArrayList<>();
    public static final ArrayList<String> consonants = new ArrayList<>();
    public static final ArrayList<String> vocalConsonant = new ArrayList<>();
    public static final ArrayList<String> consonantVocals = new ArrayList<>();

    public static void loadDefaultLanguage()
    {
        String fileName = "languages/kaiserreich_revin.json";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File languageFile = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
        FileLoader.loadLanguageFile(languageFile);
    }

    public static void clearCurrentTraits()
    {
        vocals.clear();
        consonants.clear();
        vocalConsonant.clear();
        consonantVocals.clear();
    }
}
