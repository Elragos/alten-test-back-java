package fr.alten.test_back.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.alten.test_back.dto.product.ProductDto;
import fr.alten.test_back.dto.user.CreateUserDto;

import java.util.List;

/**
 *
 * @author User
 */
public class JsonDataParser {

    private List<CreateUserDto> users;

    private List<ProductDto> products;

    /**
     * Parse JSON data.
     *
     * @param mapper Object mapper.
     * @param usersObj Object containing users.
     * @param productsObj Object containing products.
     * @throws JsonProcessingException If JSON is malformed.
     */
    public JsonDataParser(
            ObjectMapper mapper,
            Object usersObj,
            Object productsObj
    ) throws JsonProcessingException {
        // If usersObj is a list
        if (usersObj instanceof List) {
            // Parse it to JSON string
            String usersJson = mapper.writeValueAsString(usersObj);
            // Read user list from file
            this.users = mapper.readValue(usersJson,
                new TypeReference<>() {
            });
        }

        // If productsObj is a list
        if (productsObj instanceof List) {
            // Parse it to JSON string
            String productsJson = mapper.writeValueAsString(productsObj);
            // Read user list from file
            this.products = mapper.readValue(productsJson,
                new TypeReference<>() {
            });
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
