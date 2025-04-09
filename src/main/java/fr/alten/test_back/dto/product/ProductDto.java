package fr.alten.test_back.dto.product;

import fr.alten.test_back.entity.Product;
import fr.alten.test_back.helper.ProductInventoryStatus;

/**
 * Product DTO, used to create or update product. If a field is null, then it
 * will be ignored.
 *
 * @param code Product code.
 * @param name Product name.
 * @param description Product description.
 * @param image Product image.
 * @param category Product category.
 * @param price Product price.
 * @param quantity Product quantity.
 * @param internalReference Product internal reference.
 * @param shellId Product shell ID.
 * @param inventoryStatus Product inventory status
 * @param rating Product rating.
 */
public record ProductDto (
    String code,
    String name,
    String description,
    String image,
    String category,
    Float price,
    Integer quantity,
    String internalReference,
    Integer shellId,
    ProductInventoryStatus inventoryStatus,
    Float rating
){
    /**
     * Generate DTO from product
     * @param product Desired product.
     * @return Generated DTO.
     */
    public static ProductDto generate(Product product){
        return new ProductDto(
            product.getCode(),
            product.getName(),
            product.getDescription(),
            product.getImage(),
            product.getCategory(),
            product.getPrice(),
            product.getQuantity(),
            product.getInternalReference(),
            product.getShellId(),
            product.getInventoryStatus(),
            product.getRating()
        );
    }
}
