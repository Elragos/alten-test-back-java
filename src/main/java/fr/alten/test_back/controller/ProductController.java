package fr.alten.test_back.controller;

import fr.alten.test_back.dto.ProductDto;
import fr.alten.test_back.entity.Product;
import fr.alten.test_back.error.ResourceNotFoundException;
import fr.alten.test_back.repository.ProductRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling product interactions.
 *
 * @author Amarechal
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    /**
     * Used product repository.
     */
    @Autowired
    private ProductRepository productRepository;

    /**
     * List all available products.
     *
     * @return Available product list.
     */
    @GetMapping
    public Iterable<Product> listProducts() {
        return this.productRepository.findAll();
    }

    /**
     * Get product info.
     *
     * @param id Product DB ID.
     * @return Product info, or 404 error if not found.
     */
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Integer id) {
        // Match product with DB
        Optional<Product> product = this.productRepository.findById(id);
        // If not found
        if (product.isEmpty()) {
            //Throw 404 error
            throw new ResourceNotFoundException("Unable to find product");
        }

        // Return product info.
        return product.get();
    }

    /**
     * Add product to DB.
     *
     * @param newProductData Product Info.
     * @return Created product.
     */
    @PostMapping
    public Product addProduct(@RequestBody ProductDto newProductData) {
        Product createdProduct = new Product(newProductData);

        createdProduct = this.productRepository.save(createdProduct);

        return createdProduct;
    }

    /**
     * Update product to DB.
     *
     * @param id Product DB ID to update.
     * @param newProductData Product updated Info.
     * @return Updated product.
     */
    @PatchMapping("/{id}")
    public Product updateProduct(@PathVariable Integer id,
            @RequestBody ProductDto newProductData) {
        Optional<Product> update = this.productRepository.findById(id);
        if (update.isEmpty()) {
            throw new ResourceNotFoundException("Unable to find product");
        }

        update.get().updateFromDto(newProductData);

        this.productRepository.save(update.get());

        return update.get();
    }

    /**
     * Remove product to DB.
     *
     * @param id Product DB ID to update.
     * @return Deleted product.
     */
    @DeleteMapping("/{id}")
    public Product removeProduct(@PathVariable Integer id) {
        // Match product with DB
        Optional<Product> update = this.productRepository.findById(id);
        // If not found
        if (update.isEmpty()) {
            //Throw 404 error
            throw new ResourceNotFoundException("Unable to find product");
        }
        // Remove product from DB
        this.productRepository.delete(update.get());

        // Return deleted product info.
        return update.get();
    }
}
