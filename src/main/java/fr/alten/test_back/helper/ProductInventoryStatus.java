package fr.alten.test_back.helper;

/**
 * Enumeration represtenting product inventory status.
 * 
 * @author AMarechal
 */
public enum ProductInventoryStatus {
    /**
     * Product has sufficient stock.
     */
    INSTOCK,
    /**
     * Product has low stock.
     */
    LOWSTOCK,
    /**
     * Product is out of stock (stock = 0).
     */
    OUTOFSTOCK
}
