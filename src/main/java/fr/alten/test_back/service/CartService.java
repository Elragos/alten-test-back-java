package fr.alten.test_back.service;

import fr.alten.test_back.entity.Product;
import fr.alten.test_back.helper.Cart;
import fr.alten.test_back.helper.Translator;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final ProductService productService;

    public CartService(
        ProductService productService
    ){
        this.productService = productService;
    }

    /**
     * Add product to cart. If final item quantity is 0 or below, remove it.
     *
     * @param cart User cart.
     * @param quantity Quantity to add.
     * @param productCode Referring product.
     */
    public void addItem(Cart cart, int quantity, String productCode) {
        // Get product by code
        Product product = this.productService.getByCode(productCode);
        // Find if product is already in cart
        Cart.CartItem item = cart.findProduct(product);
        // If not
        if (item == null) {
            // Add it
            item = new Cart.CartItem(quantity, product);
            cart.getItems().add(item);
        } else {
            // Add desired quantity to existing item
            item.addQuantity(quantity);
        }
        // Refresh cart
        this.refresh(cart);
    }

    /**
     * Remove product from cart.
     *
     * @param cart User cart.
     * @param productCode Product code to remove
     */
    public void removeItem(Cart cart, String productCode) {
        // Get product by code
        Product product = this.productService.getByCode(productCode);
        // Find product in cart
        cart.removeItem(product);
    }

    /**
     * Refresh cart with actual data.
     * @param cart User cart.
     */
    public void refresh(Cart cart) {
        // For each item in cart
        for(Cart.CartItem item : cart.getItems().stream().toList()){
            // Reload product
            Product product = this.productService.getByCode(item.getProduct().getCode());
            item.setProduct(product);
            // If desired quantity above available quantity
            if (item.getQuantity() > product.getQuantity()) {
                // Limit to available quantity
                item.setQuantity(product.getQuantity());
                // Indicate that product quantity was limited because of stock
                cart.getErrors().add(Translator.translate(
                        "error.cart.notEnoughStock",
                        new Object[]{
                                product.getCode(), product.getQuantity()
                        }
                ));
            }
            // If quantity is empty (equals or below zero)
            if (item.getQuantity() <= 0) {
                // Add item to be removed
                cart.removeItem(product);
                // Indicate that product was remove because of invalid quantity
                cart.getErrors().add(Translator.translate(
                        "error.cart.productRemoved",
                        new Object[]{product.getCode()}
                ));
            }
        }
    }
}
