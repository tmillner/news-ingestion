package news.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

@Configuration
@ConfigurationProperties
@PropertySource("classpath:service.properties")
public class StorageProperties {

    @Autowired
    Environment env;

    @Bean
    public String getValidNewsSourcesApi() {
        return String.format("%s",
                env.getProperty("newsapi_sources"));
    }

    @Bean
    public String getValidNewsArticlesApi() {
        return String.format("%s?apiKey=%s",
                env.getProperty("newsapi_articles"),
                env.getProperty("key"));
    }

    @Bean
    public List<String> getKeywordBlacklist() {
        return Arrays.asList(env.getProperty("keyword_blacklist").split(","));
    }
}
