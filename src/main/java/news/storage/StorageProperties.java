package news.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@ConfigurationProperties
@PropertySource("classpath:service.properties")
public class StorageProperties {

    @Autowired
    Environment env;

    @Bean
    public String[] getSources() {
        return env.getProperty("sources").split(",");
    }

    @Bean
    public String getValidNewsSourceApi() {
        return String.format("%s?apiKey=%s",
                env.getProperty("newsapi"),
                env.getProperty("key"));
    }
}
