package fr.alten.test_back.config;

import fr.alten.test_back.config.translation.MessageProperties;
import fr.alten.test_back.config.translation.YamlMessageSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application configuration.
 */
@Configuration
@EnableConfigurationProperties(MessageProperties.class) // Allow usage of custom properties for translation
public class AppConfig {

    /**
     * Defined translation files in configuration.
     */
    private final MessageProperties messageProperties;

    /**
     * Initialize configuration.
     * @param messageProperties Used translation files.
     */
    public AppConfig(MessageProperties messageProperties) {
        this.messageProperties = messageProperties;
    }

    /**
     * Use YML files for translation.
     * @return Generated message source.
     */
    @Bean
    public MessageSource messageSource() {
        return new YamlMessageSource(this.messageProperties.getLocales().toArray(new String[0]));
    }
}