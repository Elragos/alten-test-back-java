package fr.alten.test_back.dto.cart;

/**
 * DTO used to add product to cart.
 * @param productCode Desired product code.
 * @param quantity Desired quantity.
 */
public record AddProductToCartDto (String productCode, Integer quantity){

}
