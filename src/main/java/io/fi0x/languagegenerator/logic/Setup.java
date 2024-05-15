package io.fi0x.languagegenerator.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fi0x.languagegenerator.Main;
import io.fi0x.languagegenerator.logic.dto.LanguageJson;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

@Slf4j
public class Setup
{
    public static void loadLanguagesFromFiles()
    {
        loadLanguageToDb(Main.class.getResourceAsStream("/languages/Elfensprache.json"), "Elven-Language");
        loadLanguageToDb(Main.class.getResourceAsStream("/languages/Gnomsprache.json"), "Gnome-Language");
        loadLanguageToDb(Main.class.getResourceAsStream("/languages/Goblinsprache.json"), "Goblin-Language");
        loadLanguageToDb(Main.class.getResourceAsStream("/languages/Halblingsprache.json"), "Halfling-Language");
        loadLanguageToDb(Main.class.getResourceAsStream("/languages/templateLanguage.json"), "Template-Language");
        loadLanguageToDb(Main.class.getResourceAsStream("/languages/Tieflingsprache.json"), "Tiefling-Language");
        loadLanguageToDb(Main.class.getResourceAsStream("/languages/Zwergensprache.json"), "Dwarf-Language");
    }

    @Deprecated
    public static boolean createDefaultFileStructure() throws Exception
    {
        if(!Main.languageFolder.exists())
        {
            if(Main.languageFolder.mkdirs())
                return true;
            else
                throw new Exception("Could not create the language folder");
        }
        else
            return false;
    }
    @Deprecated
    public static boolean createTemplateLanguageFile() throws IOException
    {
        File template = new File(Main.languageFolder.getPath() + File.separator + "Language Template.json");

        boolean result = template.createNewFile();
        InputStream jsonInput = Main.class.getResourceAsStream("/languages/templateLanguage.json");
        assert jsonInput != null;
        List<String> content = new BufferedReader(new InputStreamReader(jsonInput, StandardCharsets.UTF_8)).lines().toList();
        Files.write(template.toPath(), content, StandardCharsets.UTF_8);

        return result;
    }

    private static void loadLanguageToDb(InputStream languageFileStream, String name)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            LanguageJson json = mapper.readValue(languageFileStream, LanguageJson.class);
//            languageService.addLanguage(json, name, true);//TODO: Fix this
        } catch (IOException e)
        {
            log.warn("Could not convert an InputStream to a LanguageJson-Object", e);
        }
    }
}
