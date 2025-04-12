package fr.alten.test_back.helper;

import fr.alten.test_back.dto.product.ProductDto;
import fr.alten.test_back.dto.user.CreateUserDto;

import java.util.List;

/**
 * Class used to represent JSON initial data.
 */
public class InitialDataContainer {
    /**
     * Read users.
     */
    private List<CreateUserDto> users;
    /**
     * Read products.
     */
    private List<ProductDto> products;

    /**
     * Get read users.
     * @return Read users.
     */
    public List<CreateUserDto> getUsers() {
        return users;
    }

    /**
     * Set read users.
     * @param users Read users.
     */
    public void setUsers(List<CreateUserDto> users) {
        this.users = users;
    }

    /**
     * Get read users.
     * @return Read users.
     */
    public List<ProductDto> getProducts() {
        return products;
    }

    /**
     * Set read products.
     * @param products Read products.
     */
    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }
}