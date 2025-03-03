/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package fr.alten.test_back.helper;

/**
 * Enum represtenting product inventory status.
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
