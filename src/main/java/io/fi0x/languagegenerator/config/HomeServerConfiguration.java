package io.fi0x.languagegenerator.config;

import io.github.fi0x.util.config.ApiConfig;
import io.github.fi0x.util.config.HubRegisterConfig;
import io.github.fi0x.util.config.LoginConfig;
import io.github.fi0x.util.config.UtilLoggingConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ConditionalOnBooleanProperty(name = "homeserver.use-hub")
@Configuration
@Import({HubRegisterConfig.class, UtilLoggingConfig.class, LoginConfig.class, ApiConfig.class})
public class HomeServerConfiguration
{
}
