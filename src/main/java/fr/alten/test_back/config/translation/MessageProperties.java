package fr.alten.test_back.config.translation;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Custom message properties definition to allow them in application.yml.
 */
@Configuration
@ConfigurationProperties(prefix = "custom.messages")
public class MessageProperties {

    /**
     * Defined locales
     */
    private List<String> locales;

    /**
     * Get defined locales.
     * @return Defined locales.
     */
    public List<String> getLocales() {
        return locales;
    }

    /**
     * Set defined locales.
     * @param locales Defined locales.
     */
    public void setLocales(List<String> locales) {
        this.locales = locales;
    }
}
