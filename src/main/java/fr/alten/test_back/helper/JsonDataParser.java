package fr.alten.test_back.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.alten.test_back.dto.product.ProductDto;
import fr.alten.test_back.dto.user.CreateUserDto;

import java.util.List;

/**
 *
 * @author User
 */
public class JsonDataParser {

    /**
     * Parsed users.
     */
    private final List<CreateUserDto> users;

    /**
     * Parsed products.
     */
    private final List<ProductDto> products;

    /**
     * Parse JSON data.
     *
     * @param mapper Object mapper.
     * @param jsonContent JSON data file content.
     * @throws RuntimeException If something went wrong while parsing JSON.
     */
    public JsonDataParser(
            ObjectMapper mapper,
            String jsonContent
    ) throws RuntimeException {
        try {
            InitialDataContainer container = mapper.readValue(jsonContent, InitialDataContainer.class);
            this.users = container.getUsers();
            this.products = container.getProducts();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du parsing du JSON d'initialisation", e);
        }
    }

    /**
     * Get parsed users.
     *
     * @return Parsed users.
     */
    public List<CreateUserDto> getUsers() {
        return this.users;
    }

    /**
     * Get parsed products.
     *
     * @return Parsed products.
     */
    public List<ProductDto> getProducts() {
        return this.products;
    }
}
