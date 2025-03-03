/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.alten.test_back.controller;

import fr.alten.test_back.dto.ProductDto;
import fr.alten.test_back.entity.Product;
import fr.alten.test_back.repository.ProductRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller handling product interactions.
 *
 * @author Amarechal
 */
@RestController
public class ProductController {

    /**
     * Product repository.
     */
    @Autowired
    private ProductRepository productRepository;

    /**
     * List all available products.
     *
     * @return Available product list.
     */
    @GetMapping("/product")
    public Iterable<Product> listProducts() {
        return this.productRepository.findAll();
    }

    /**
     * Add product to DB.
     *
     * @param newProductData Product Info.
     * @return Created product.
     */
    @PostMapping("/product")
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
    @PatchMapping("/product/{id}")
    public Product updateProduct(@RequestParam int id, @RequestBody ProductDto newProductData) {
        Optional<Product> update = this.productRepository.findById(id);
        if (update.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Unable to find product");
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
    @DeleteMapping("/product/{id}")
    public Product removeProduct(@RequestParam int id) {
        // Match product with DB
        Optional<Product> update = this.productRepository.findById(id);
        // If not found
        if (update.isEmpty()) {
            //Throw 404 error
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Unable to find product");
        }
        // Remove product from DB
        this.productRepository.delete(update.get());

        // Return deleted product info.
        return update.get();
    }
}
