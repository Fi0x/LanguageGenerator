package io.fi0x;

import io.fi0x.javalogger.logging.LogColor;
import io.fi0x.javalogger.logging.Logger;

public class Setup
{
    public static void initializeLogger()
    {
        Logger.getInstance().setDebug(true);
        Logger.getInstance().setVerbose(true);
        Logger.createNewTemplate(LOG.QUESTION, LogColor.CYAN_BRIGHT, "", false, false, false, true);
        Logger.createNewTemplate(LOG.OUTPUT, LogColor.PURPLE_BRIGHT, "", false, false, false, true);
        Logger.createNewTemplate(LOG.ERROR, LogColor.RED_BRIGHT, "", false, false, false, true);
        Logger.createNewTemplate(LOG.INFO, LogColor.WHITE_BRIGHT, "", false, false, false, true);
    }
}
