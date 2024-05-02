package io.fi0x.languagegenerator.logic;

import io.fi0x.languagegenerator.Main;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

@Slf4j
public class FileLoader
{
    private static final ArrayList<File> languageFiles = new ArrayList<>();
    @Getter
    private static String activeLanguage;
    public static boolean loadLanguageFile(String nameOrIdx)
    {
        if(InputHandler.isInt(nameOrIdx))
        {
            try
            {
                loadLanguageFile(languageFiles.get(InputHandler.getInt(nameOrIdx) - 1));
            } catch(Exception e)
            {
                return false;
            }
        }
        else
            loadLanguageFile(new File(Main.languageFolder + File.separator + nameOrIdx + ".json"));

        return true;
    }
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
            log.warn("Selected language could not be found: {}", languageFile, e);
            return;
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

        LanguageTraits.forbiddenCombinations.clear();
        for(Object entry : jsonObject.getJSONArray("forbiddenCombinations"))
            LanguageTraits.forbiddenCombinations.add((String) entry);

        activeLanguage = languageFile.getName().replace(".json", "");
        log.debug("Active language is now '{}'", activeLanguage);
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

        JSONArray forbiddenCombinations = new JSONArray();
        forbiddenCombinations.put(LanguageTraits.forbiddenCombinations);
        jsonObject.put("forbiddenCombinations", forbiddenCombinations);

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
            if(f.isFile() && !languageFiles.contains(f))
                languageFiles.add(f);
        }
        return languageFiles.size() - 1;
    }
    public static ArrayList<String> getLoadedLanguageNames(boolean showNumber)
    {
        loadAllLanguageFiles();
        ArrayList<String> names = new ArrayList<>();

        for(int i = 0; i < languageFiles.size(); i++)
            names.add((showNumber ? (i + 1) + ") " : "") + languageFiles.get(i).getName().replace(".json", ""));

        return names;
    }
}
