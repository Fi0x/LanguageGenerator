package io.fi0x.languagegenerator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@Slf4j
@SpringBootApplication
public class MainApplication extends SpringBootServletInitializer
{
    public static void main(String[] args)
    {
        log.info("Initial loading complete");

        SpringApplication.run(MainApplication.class, args);
    }
}
