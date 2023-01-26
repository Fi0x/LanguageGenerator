package io.fi0x;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileLoader
{
    public static void loadLanguageFile(File languageFile)
    {
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

        LanguageTraits.vocals.clear();
        for(Object entry : jsonObject.getJSONArray("vocals"))
            LanguageTraits.vocals.add((String) entry);

        LanguageTraits.vocalConsonant.clear();
        for(Object entry : jsonObject.getJSONArray("vocalConsonant"))
            LanguageTraits.vocalConsonant.add((String) entry);

        LanguageTraits.consonants.clear();
        for(Object entry : jsonObject.getJSONArray("consonants"))
            LanguageTraits.consonants.add((String) entry);

        LanguageTraits.consonantVocals.clear();
        for(Object entry : jsonObject.getJSONArray("consonantVocals"))
            LanguageTraits.consonantVocals.add((String) entry);
    }
}
