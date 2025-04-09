package fr.alten.test_back.service;

import fr.alten.test_back.dto.product.ProductDto;
import fr.alten.test_back.entity.Product;
import fr.alten.test_back.helper.Translator;
import fr.alten.test_back.repository.ProductRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Optional;

@Service
public class ProductService {

    /**
     * Used product repository.
     */
    private final ProductRepository repository;

    /**
     * Used wishlist service.
     */
    private final WishlistService wishlistService;

    /**
     * Initialize service.
     * @param repository Used product repository.
     * @param wishlistService Used wishlist service.
     */
    public ProductService(ProductRepository repository, WishlistService wishlistService){
        this.repository = repository;
        this.wishlistService = wishlistService;
    }

    /**
     * Get all available products.
     * @return Available products.
     */
    public Iterable<Product> getAll(){
        return this.repository.findAll();
    }

    /**
     * Get specific product bu code
     * @param code Product code.
     * @return Found product
     * @throws ResourceNotFoundException If product not found
     */
    public Product getByCode(String code) throws  ResourceNotFoundException {
        return this.repository.findByCode(code).orElseThrow(() ->
                new ResourceNotFoundException(Translator.translate(
                        "error.product.codeNotFound",
                      new Object[] { code }
                ))
        );
    }

    /**
     * Create product from dto
     * @param productDto Product dto.
     * @return Created product.
     * @throws InvalidParameterException If product code already used
     */
    public Product create(ProductDto productDto) throws InvalidParameterException {
        // Try to find same product code
        Optional<Product> existing = this.repository
                .findByCode(productDto.code());
        // If found
        if (existing.isPresent()){
            // Send bad request error
            throw new InvalidParameterException(
                    Translator.translate(
                            "error.product.codeAlreadyUsed",
                            new Object[]{productDto.code()}
                    )
            );
        }

        // Else create new product from DTO
        Product createdProduct = new Product(productDto);
        // Save created product to DB
        createdProduct = this.repository.save(createdProduct);

        return createdProduct;
    }

    /**
     * Update product by code.
     * @param code Updated product code.
     * @param productDto New product data.
     * @throws ResourceNotFoundException If product code not found.
     * @throws InvalidParameterException If new code already used by another product.
     */
    public Product update(String code, ProductDto productDto)
            throws ResourceNotFoundException, InvalidParameterException {
        // Get updated product
        Product updated = this.getByCode(code);

        try {
            // Get colliding product by new code
            Product collidingProduct = this.getByCode(productDto.code());
            // If colliding not same product as update
            if (!collidingProduct.getId().equals(updated.getId())){
                // Throw error
                throw new InvalidParameterException(Translator.translate(
                        "error.product.codeAlreadyUsed",
                        new Object[]{ productDto.code() }
                ));
            }
        }
        catch (ResourceNotFoundException _){
            // Do nothing, since code is not used
        }

        // Update product info from DTO
        updated.updateFromDto(productDto);
        // Save updated product to DB
        updated = this.repository.save(updated);

        return updated;
    }

    /**
     * Delete product.
     * @param code Product code.
     * @return Deleted product.
     * @throws ResourceNotFoundException If product code not found.
     */
    public Product delete(String code) throws ResourceNotFoundException{
        Product deleted = this.getByCode(code);
        this.wishlistService.removeProductFromAll(deleted);
        this.repository.delete(deleted);
        return deleted;
    }
}
