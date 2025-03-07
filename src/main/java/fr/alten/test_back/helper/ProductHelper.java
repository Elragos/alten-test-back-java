package fr.alten.test_back.helper;

import fr.alten.test_back.entity.Product;
import fr.alten.test_back.error.ResourceNotFoundException;
import fr.alten.test_back.repository.ProductRepository;

/**
 * Class containing common methods on product.
 *
 * @author Amarechal
 */
public class ProductHelper {

    /**
     * Find product by DB id.
     *
     * @param productId Product DB ID.
     * @param repo Used product repository.
     * @return Found product.
     * @throws ResourceNotFoundException If product not found.
     */
    public static Product findProduct(Integer productId, ProductRepository repo)
            throws ResourceNotFoundException {
        return repo.findById(productId).orElseThrow(()->
            new ResourceNotFoundException("Unable to find product")
        );
    }
}
