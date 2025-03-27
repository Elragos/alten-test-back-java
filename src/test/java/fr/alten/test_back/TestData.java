package fr.alten.test_back;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.alten.test_back.config.JsonPropertySourceFactory;
import fr.alten.test_back.dto.CreateUserDto;
import fr.alten.test_back.dto.ProductDto;
import fr.alten.test_back.helper.InitialDataParser;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Object loading initialData.json for testing.
 *
 * @author amarechal
 */
@Component
@PropertySource(
        value = "classpath:initialData.json",
        factory = JsonPropertySourceFactory.class
)
public class TestData {

    /**
     * Used environment to load JSON file.
     */
    @Autowired
    private Environment env;

    /**
     * Loaded user list.
     */
    private List<CreateUserDto> users;

    /**
     * Loaded product list.
     */
    private List<ProductDto> products;

    /**
     * Load data from JSON file.
     *
     * @throws JsonProcessingException
     */
    public void loadData() throws JsonProcessingException {
        // Load JSON data
        InitialDataParser parser = new InitialDataParser(
            new ObjectMapper(),
            this.env.getProperty("users", Object.class),
            this.env.getProperty("products", Object.class)
        );
        // Retrive loaded data.
        this.users = parser.getUsers();
        this.products = parser.getProducts();
    }

    /**
     * Get loaded users.
     *
     * @return loaded users.
     */
    public List<CreateUserDto> getUsers() {
        return this.users;
    }

    /**
     * Get loaded products.
     *
     * @return loaded products.
     */
    public List<ProductDto> getProducts() {
        return this.products;
    }
}
