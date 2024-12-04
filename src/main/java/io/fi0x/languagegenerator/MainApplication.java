package io.fi0x.languagegenerator;

import io.github.fi0x.util.config.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootApplication
//TODO:Disable the config-imports, when homeserver.use-hub=false
@Import({HomeServerUtilConfig.class, HubRegisterConfig.class, UtilLoggingConfig.class, LoginConfig.class,
        ApiConfig.class})
public class MainApplication extends SpringBootServletInitializer
{
	public static void main(String[] args)
	{
		log.info("Initial loading complete");

		SpringApplication.run(MainApplication.class, args);
	}
}
