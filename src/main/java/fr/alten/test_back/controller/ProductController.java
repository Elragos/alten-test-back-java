package fr.alten.test_back.controller;

import fr.alten.test_back.dto.product.ProductDto;
import fr.alten.test_back.entity.Product;
import fr.alten.test_back.helper.AppRoutes;
import fr.alten.test_back.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
    private final ProductService service;

    public ProductController(ProductService service){
        this.service = service;
    }

    /**
     * List all available products.
     *
     * @return Available product list.
     */
    @GetMapping
    public ResponseEntity<Iterable<Product>> listProducts() {
        return ResponseEntity.ok(this.service.getAll());
    }

    /**
     * Get product info.
     *
     * @param code Product code.
     * @return Product info, or 404 error if not found.
     */
    @GetMapping("/{code}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String code) {
        // Return product info.
        return ResponseEntity.ok(
                ProductDto.generate(this.service.getByCode(code))
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
    public ResponseEntity<?> addProduct(
            @RequestBody
            ProductDto newProductData
    ) {
        // Create product
        Product created = this.service.create(newProductData);
        // Return created product URL
        return ResponseEntity.created(
                URI.create(AppRoutes.PRODUCT + "/" + URLEncoder.encode(created.getCode(), StandardCharsets.UTF_8))
        ).build();
    }

    /**
     * Update product to DB, only for admin users.
     *
     * @param code Product code.
     * @param newProductData Product updated Info.
     * @return Updated product.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{code}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable
            String code,
            @RequestBody
            ProductDto newProductData
    ) {
        // Update product
        Product updated = this.service.update(code, newProductData);
        // Return updated product
        return ResponseEntity.ok(ProductDto.generate(updated));
    }

    /**
     * Remove product to DB, only for admin users.
     *
     * @param code Product DB ID to update.
     * @return Deleted product.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{code}")
    public ResponseEntity<ProductDto> removeProduct(@PathVariable String code) {
        Product deleted = this.service.delete(code);
        // Return deleted product
        return ResponseEntity.ok(ProductDto.generate(deleted));
    }
}
