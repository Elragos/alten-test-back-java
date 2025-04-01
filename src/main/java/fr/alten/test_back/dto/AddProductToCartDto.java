package fr.alten.test_back.dto;

/**
 * DTO used when adding product to cart.
 * 
 * @author AMarechal
 */
public class AddProductToCartDto {
    /**
     * Desired quantity.
     */
    private Integer quantity;
    
    /**
     * Get desired quantity.
     * @return Desired quantity.
     */
    public Integer getQuantity(){
        return this.quantity;
    }
    
    /**
     * Set desired quantity.
     * @return self
     */
    public AddProductToCartDto setQuantity(Integer quantity){
        this.quantity = quantity;
        return this;
    }
}
