package io.fi0x;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class LanguageTraits
{
    public static final ArrayList<String> vocals = new ArrayList<>();
    public static final ArrayList<String> consonants = new ArrayList<>();
    public static final ArrayList<String> vocalConsonant = new ArrayList<>();
    public static final ArrayList<String> consonantVocals = new ArrayList<>();

    public static void loadDefaultLanguage()
    {
        String fileName = "languages/kaiserreich_revin.json";
        loadLanguageFile(fileName);
    }

    public static void loadLanguageFile(String filePath)
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File languageFile = new File(Objects.requireNonNull(classLoader.getResource(filePath)).getFile());
        StringBuilder fileContent = new StringBuilder();
        try
        {
            Scanner fileReader = new Scanner(languageFile);
            while(fileReader.hasNext())
                fileContent.append(fileReader.next());
            fileReader.close();
        } catch(FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        JSONObject jsonObject = new JSONObject(fileContent.toString());

        for(Object entry : jsonObject.getJSONArray("vocals"))
            vocals.add((String) entry);

        for(Object entry : jsonObject.getJSONArray("vocalConsonant"))
            vocalConsonant.add((String) entry);

        for(Object entry : jsonObject.getJSONArray("consonants"))
            consonants.add((String) entry);

        for(Object entry : jsonObject.getJSONArray("consonantVocals"))
            consonantVocals.add((String) entry);
    }
}
