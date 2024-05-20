package io.fi0x.languagegenerator.client.logic;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

@Deprecated
@Slf4j
public class FileLoader
{
    private static final ArrayList<File> languageFiles = new ArrayList<>();
    @Getter
    private static String activeLanguage;
    public static void loadLanguageFile(File languageFolder, String nameOrIdx)
    {
        if(InputHandler.isInt(nameOrIdx))
        {
            try
            {
                loadLanguageFile(languageFiles.get(InputHandler.getInt(nameOrIdx) - 1));
            } catch(Exception ignored)
            {
            }
        }
        else
            loadLanguageFile(new File(languageFolder + File.separator + nameOrIdx + ".json"));

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

    public static int loadAllLanguageFiles(File languageFolder)
    {
        File[] fileList = languageFolder.listFiles();
        assert fileList != null;
        for(File f : fileList)
        {
            if(f.isFile() && !languageFiles.contains(f))
                languageFiles.add(f);
        }
        return languageFiles.size() - 1;
    }
    public static ArrayList<String> getLoadedLanguageNames(File languageFolder, boolean showNumber)
    {
        loadAllLanguageFiles(languageFolder);
        ArrayList<String> names = new ArrayList<>();

        for(int i = 0; i < languageFiles.size(); i++)
            names.add((showNumber ? (i + 1) + ") " : "") + languageFiles.get(i).getName().replace(".json", ""));

        return names;
    }
}
