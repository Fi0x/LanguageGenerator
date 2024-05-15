package io.fi0x.languagegenerator.logic;

import io.fi0x.languagegenerator.Main;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

@Slf4j
public class Setup
{
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
}
