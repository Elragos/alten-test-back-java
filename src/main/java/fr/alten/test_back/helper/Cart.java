package fr.alten.test_back.helper;

import fr.alten.test_back.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class managing user cart.
 *
 * @author Amarechal
 */
public class Cart {

    /**
     * Cart items;
     */
    private final List<CartItem> items;

    /**
     * Cart errors.
     */
    private final List<String> errors;

    /**
     * Initialize cart.
     */
    public Cart() {
        this.items = new ArrayList<>();
        this.errors = new ArrayList<>();
    }

    /**
     * Initialize cart with items.
     * @param items Initial items.
     */
    public Cart(List<CartItem> items){
        this();
        this.items.addAll(items);
    }

    /**
     * .
     * Get cart items.
     *
     * @return Cart items.
     */
    public List<CartItem> getItems() {
        return this.items;
    }

    /**
     * Get cart errors.
     *
     * @return Cart errors.
     */
    public List<String> getErrors() {
        return this.errors;
    }

    /**
     * Find product in cart.
     *
     * @param p Product to fing
     * @return Found cart item, or null if not found.
     */
    public CartItem findProduct(Product p) {
        // For each item in cart
        for (CartItem item : this.items) {
            // If item id match desired product id
            if (Objects.equals(item.getProduct().getId(), p.getId())) {
                // Return item
                return item;
            }
        }
        // If not found
        return null;
    }

    /**
     * Remove product from cart.
     *
     * @param toRemove Product to remove
     */
    public void removeItem(Product toRemove) {
        // Find product in cart
        Cart.CartItem item = this.findProduct(toRemove);
        // If found
        if (item != null) {
            // Remove it from cart
            this.items.remove(item);
        }
    }

    /**
     * Internal class representing a cart item
     */
    public static class CartItem {

        /**
         * Item quantity.
         */
        private int quantity;
        /**
         * Item product.
         */
        private Product product;

        public CartItem(int quantity, Product product) {
            this.product = product;
            this.quantity = quantity;
        }

        /**
         * Get item quantity.
         *
         * @return Item quantity.
         */
        public int getQuantity() {
            return this.quantity;
        }

        /**
         * Set item quantity.
         *
         * @param quantity New item quantity
         * @return self
         */
        public CartItem setQuantity(int quantity){
            this.quantity = quantity;
            return this;
        }

        /**
         * Add quantity to cart item
         * @param quantity Quantity to add.
         */
        public void addQuantity(int quantity) {
            this.quantity += quantity;
        }

        /**
         * Get item product.
         *
         * @return Item product.
         */
        public Product getProduct() {
            return this.product;
        }

        /**
         * Set item product.
         *
         * @param product New item product.
         * @return self
         */
        public CartItem setProduct(Product product) {
            this.product = product;
            return this;
        }
    }
}
