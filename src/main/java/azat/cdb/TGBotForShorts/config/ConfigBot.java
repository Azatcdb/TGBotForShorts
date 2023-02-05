package azat.cdb.TGBotForShorts.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource(value = "classpath:application.properties")
public class ConfigBot {

    @Value("${bot.name}")
    String botName;

    @Value("${bot.key}")
    String token;
}
