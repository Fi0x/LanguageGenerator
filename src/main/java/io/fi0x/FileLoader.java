package io.fi0x;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class FileLoader
{
    private static final ArrayList<File> languageFiles = new ArrayList<>();
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

        JSONArray lengths = jsonObject.getJSONArray("nameLengths");
        LanguageTraits.setNameLengths(lengths.getInt(0), lengths.getInt(1));

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

    public static boolean storeCurrentLanguage(String name) throws IOException
    {
        File file = new File(Main.languageFolder.getPath() + File.separator + name);

        if(!file.createNewFile())
            return false;

        JSONObject jsonObject = new JSONObject();

        JSONArray lengths = new JSONArray();
        lengths.put(LanguageTraits.minNameLength);
        lengths.put(LanguageTraits.maxNameLength);
        jsonObject.put("nameLengths", lengths);

        JSONArray vocals = new JSONArray();
        vocals.put(LanguageTraits.vocals);
        jsonObject.put("vocals", vocals);

        JSONArray vocalConsonants = new JSONArray();
        vocalConsonants.put(LanguageTraits.vocalConsonant);
        jsonObject.put("vocalConsonant", vocalConsonants);

        JSONArray consonants = new JSONArray();
        consonants.put(LanguageTraits.consonants);
        jsonObject.put("consonants", consonants);

        JSONArray consonantVocals = new JSONArray();
        consonantVocals.put(LanguageTraits.consonantVocals);
        jsonObject.put("consonantVocals", consonantVocals);

        String content = jsonObject.toString();
        Files.write(file.toPath(), Collections.singleton(content), StandardCharsets.UTF_8);

        return true;
    }

    public static int loadAllLanguageFiles()
    {
        File[] fileList = Main.languageFolder.listFiles();
        assert fileList != null;
        for(File f : fileList)
        {
            if(f.isFile())
                languageFiles.add(f);
        }
        return languageFiles.size() - 1;
    }
    public static ArrayList<String> getLoadedLanguageNames()
    {
        ArrayList<String> names = new ArrayList<>();

        for(File f : languageFiles)
            names.add(f.getName());

        return names;
    }
}
