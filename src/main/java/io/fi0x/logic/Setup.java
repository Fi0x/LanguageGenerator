package io.fi0x.logic;

import io.fi0x.Main;
import io.fi0x.javalogger.logging.LogColor;
import io.fi0x.javalogger.logging.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class Setup
{
    public static void initializeLogger()
    {
        Logger.getInstance().setDebug(true);
        Logger.getInstance().setVerbose(true);
        Logger.createNewTemplate(LOG.QUESTION, LogColor.CYAN_BRIGHT, "", "INF", false, false, 0, false, true, false, false, "", "");
        Logger.createNewTemplate(LOG.OUTPUT, LogColor.PURPLE_BRIGHT, "", "INF", false, false, 0, false, true, false, false, "", "");
        Logger.createNewTemplate(LOG.SUCCESS, LogColor.GREEN_BRIGHT, "", "INF", false, false, 0, false, true, false, false, "", "");
        Logger.createNewTemplate(LOG.ERROR, LogColor.RED_BRIGHT, "", "ERR", false, false, 0, false, true, false, false, "", "");
        Logger.createNewTemplate(LOG.INFO, LogColor.WHITE_BRIGHT, "", "INF", false, false, 0, false, true, false, false, "", "");
        Logger.createNewTemplate(LOG.MENU, LogColor.BLUE_BRIGHT, "", "INF", false, false, 0, false, true, false, false, "", "");
    }

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
