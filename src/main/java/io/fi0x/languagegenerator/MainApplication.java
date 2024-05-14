package io.fi0x.languagegenerator;

import io.fi0x.languagegenerator.logic.Setup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MainApplication
{
    public static void main(String[] args) throws Exception
    {
        //TODO: This might need to be done after initial loading is complete
        Setup.loadLanguagesFromFiles();
        log.info("Initial loading complete");

        SpringApplication.run(MainApplication.class, args);
    }
}
