package fr.alten.test_back.controller;

import fr.alten.test_back.dto.ProductDto;
import fr.alten.test_back.entity.Product;
import fr.alten.test_back.entity.Wishlist;
import fr.alten.test_back.error.InvalidPayloadException;
import fr.alten.test_back.helper.AppRoutes;
import fr.alten.test_back.helper.ProductHelper;
import fr.alten.test_back.helper.Translator;
import fr.alten.test_back.repository.ProductRepository;
import fr.alten.test_back.repository.WishlistRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
     * Used Wishlist repository.
     */
    @Autowired
    private WishlistRepository wishlistRepository;

    /**
     * List all available products.
     *
     * @return Available product list.
     */
    @GetMapping
    public ResponseEntity<Iterable<Product>> listProducts() {
        return ResponseEntity.ok(this.productRepository.findAll());
    }

    /**
     * Get product info.
     *
     * @param id Product DB ID.
     * @return Product info, or 404 error if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer id) {
        // Return product info.
        return ResponseEntity.ok(
            ProductHelper.findProduct(id, this.productRepository)
        );
    }

    /**
     * Add product to DB, only for admin users.
     *
     * @param newProductData Product Info.
     * @return Created product.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<URI> addProduct(@RequestBody ProductDto newProductData) {
        // Try to find same product code
        Optional<Product> existing = this.productRepository
            .findByCode(newProductData.getCode());
        // If found
        if (existing.isPresent()){
            // Send bad request error
            throw new InvalidPayloadException(
                Translator.translate(
                    "error.product.codeAlreadyUsed", 
                    new Object[]{newProductData.getCode()}
                )
            );
        }
        
        // Else create new product from DTO
        Product createdProduct = new Product(newProductData);
        // Save created product to DB
        createdProduct = this.productRepository.save(createdProduct);
        // Return created product URL
        return ResponseEntity.created(
            URI.create(AppRoutes.PRODUCT + "/" + createdProduct.getId())
        ).build();
        
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
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id,
            @RequestBody ProductDto newProductData) {
        // Get product by sending product code
        Optional<Product> collidingCode = this.productRepository
            .findByCode(newProductData.getCode());
        
        // If found and not same product ID
        if (collidingCode.isPresent() && !collidingCode.get().getId().equals(id)){
            // Throw error
            throw new InvalidPayloadException(Translator.translate(
                "error.product.codeAlreadyUsed", 
                new Object[]{ newProductData.getCode() }
            ));
        }        
        
        // Get product to update
        Product update = ProductHelper.findProduct(id, this.productRepository);
        // Update product info from DTO
        update.updateFromDto(newProductData);
        // Save updated product to DB
        this.productRepository.save(update);
        // Return updated product
        return ResponseEntity.ok(update);
    }

    /**
     * Remove product to DB, only for admin users.
     *
     * @param id Product DB ID to update.
     * @return Deleted product.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> removeProduct(@PathVariable Integer id) {
        // Find product in DB
        Product toRemove = ProductHelper.findProduct(id, this.productRepository);
        // Remove it from all wishlists
        List<Wishlist> wishlists = this.wishlistRepository
            .findByProductsContaining(toRemove);
        for (Wishlist current : wishlists){
            current.removeProduct(toRemove);
            this.wishlistRepository.save(current);
        }
        
        // Remove product from DB
        this.productRepository.delete(toRemove);
        // Return deleted product info.
        return ResponseEntity.ok(toRemove);
    }
}
