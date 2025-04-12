package fr.alten.test_back.config.translation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * YAML message source loader.
 */
public class YamlMessageSource extends AbstractMessageSource {
    /**
     * Read messages.
     */
    private final Map<String, Map<String, String>> messagesByLang = new HashMap<>();

    /**
     * Used object mapper to read YAML files.
     */
    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    /**
     * Initialise message source.
     * @param yamlFiles Used YAML translation files.
     */
    public YamlMessageSource(String... yamlFiles) {
        // For each configured file path
        for(String path : yamlFiles) {
            // Load messages from YAML file
            Map<String, String> flattened = this.loadMessagesFromFile(path);
            // Extract lang from file name
            String lang = this.extractLangFromFilename(path);
            // Attach read messages to extracted lang
            this.messagesByLang.put(lang, flattened);
        }
    }

    /**
     * Load messages from YAML file.
     * @param path YAML file path.
     * @return Flattened read messages.
     */
    private Map<String, String> loadMessagesFromFile(String path) {
        // Open file
        try (InputStream inputStream = ResourceUtils.getURL(path).openStream()) {
            // If file not found
            if (inputStream == null) {
                // Throw error
                throw new IllegalArgumentException("Fichier YAML introuvable : " + path);
            }
            // Read data from file
            Map<String, Object> raw = this.yamlMapper.readValue(inputStream, new TypeReference<>() {});
            // Return flattened data
            return this.flattenYamlMap(raw);

        }
        // If error while reading file
        catch (IOException e) {
            // Throw error
            throw new RuntimeException("Erreur lecture fichier YAML : " + path, e);
        }
    }

    /**
     * Flatten read data mapping to make it compatible with Spring messages system.
     *
     * @param source Read data mapping.
     * @return Flattened data mapping.
     */
    private Map<String, String> flattenYamlMap(Map<String, Object> source) {
        Map<String, String> result = new LinkedHashMap<>();
        this.flattenMap("", source, result);
        return result;
    }

    /**
     * Flatten current mapping.
     * @param prefix Current prefix.
     * @param map Current mapping.
     * @param flatMap Final result.
     */
    private void flattenMap(String prefix, Map<String, Object> map, Map<String, String> flatMap) {
        // For each found entries
        for (var entry : map.entrySet()) {
            // Generate flattened key
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            // Get value
            Object value = entry.getValue();

            // If value is another mapping
            if (value instanceof Map<?, ?> nestedMap
                    && nestedMap.keySet().stream().allMatch(k -> k instanceof String)
                    && nestedMap.values().stream().allMatch(v -> v instanceof String || v instanceof Map)) {

                // Cast it
                Map<String, Object> casted = nestedMap.entrySet().stream()
                        .collect(Collectors.toMap(
                                e -> (String) e.getKey(),
                                Map.Entry::getValue
                        ));
                // Flatten it
                this.flattenMap(key, casted, flatMap);
            }
            // Else it is a string
            else {
                // Add it to final result
                flatMap.put(key, String.valueOf(value));
            }
        }
    }

    /**
     * Extract lang from filename.
     * @param path File name.
     * @return Found lang.
     */
    private String extractLangFromFilename(String path) {
        return path.replaceAll(".*messages_([a-z]{2})\\.yml$", "$1");
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        // Get locale
        String lang = locale.getLanguage();
        // Get desired message for locale (or use EN as default language)
        Map<String, String> messages = this.messagesByLang.getOrDefault(lang, this.messagesByLang.get("en"));
        if (messages == null) return null;
        // Get message
        String message = messages.get(code);
        // Parse message
        return (message != null) ? new MessageFormat(message, locale) : null;
    }
}
