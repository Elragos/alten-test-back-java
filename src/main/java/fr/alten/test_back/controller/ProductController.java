package fr.alten.test_back.controller;

import fr.alten.test_back.dto.ProductDto;
import fr.alten.test_back.entity.Product;
import fr.alten.test_back.helper.AppRoutes;
import fr.alten.test_back.helper.ProductHelper;
import fr.alten.test_back.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping(AppRoutes.PRODUCT)
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
        // Return product info.
        return ProductHelper.findProduct(id, this.productRepository);
    }

    /**
     * Add product to DB, only for admin users.
     *
     * @param newProductData Product Info.
     * @return Created product.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Product addProduct(@RequestBody ProductDto newProductData) {
        // Check if user admin before proceed
        //.checkAdminAuthenticated();
        // Create new product from DTO
        Product createdProduct = new Product(newProductData);
        // Save created product to DB
        createdProduct = this.productRepository.save(createdProduct);
        // Return created product
        return createdProduct;
    }

    /**
     * Update product to DB, only for admin users.
     *
     * @param id Product DB ID to update.
     * @param newProductData Product updated Info.
     * @return Updated product.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public Product updateProduct(@PathVariable Integer id,
            @RequestBody ProductDto newProductData) {
        // Check if user admin before proceed
        // ControllerHelper.checkAdminAuthenticated();
        // Get product to update
        Product update = ProductHelper.findProduct(id, this.productRepository);
        // Update product info from DTO
        update.updateFromDto(newProductData);
        // Save updated product to DB
        this.productRepository.save(update);
        // Return updated product
        return update;
    }

    /**
     * Remove product to DB, only for admin users.
     *
     * @param id Product DB ID to update.
     * @return Deleted product.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Product removeProduct(@PathVariable Integer id) {
        // Check if user admin before proceed
        //ControllerHelper.checkAdminAuthenticated();
        // Find product in DB
        Product toRemove = ProductHelper.findProduct(id, this.productRepository);
        // Remove product from DB
        this.productRepository.delete(toRemove);
        // Return deleted product info.
        return toRemove;
    }

}
