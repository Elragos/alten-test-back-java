package fr.alten.test_back.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

/**
 * JSON factory, used to parse json file
 * @author Amarechal
 */
public class JsonPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(
            String name,
            EncodedResource resource
    ) throws IOException {
        Map<String, Object> readValue = new ObjectMapper()
                .readValue(resource.getInputStream(), Map.class);

        String filename = resource.getResource().getFilename();
        if (filename == null){
            filename = "json-property";
        }
        
        return new MapPropertySource(filename, readValue);
    }
    
}
