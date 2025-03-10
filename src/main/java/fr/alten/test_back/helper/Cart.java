package fr.alten.test_back.helper;

import fr.alten.test_back.entity.Product;
import jakarta.servlet.http.HttpSession;
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

    private static final String SESSION_ATTRIBUTE = "cart";

    /**
     * Initialize cart.
     */
    public Cart() {
        this.items = new ArrayList<>();
        this.errors = new ArrayList<>();
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
     * Add product to cart. If final item quantity is 0 or below, remove it.
     *
     * @param quantity Quantity to add.
     * @param product Referring product.
     */
    public void addItem(int quantity, Product product) {
        // Find if product is already in cart
        CartItem item = this.findProduct(product);
        // If not
        if (item == null) {
            // Add it
            item = new CartItem(quantity, product);
            this.items.add(item);
        } else {
            // Add desired quantity to existing item
            item.quantity += quantity;
        }
        // If quantity is empty (equals or below zero)
        if (item.quantity <= 0) {
            // Remove item from cart
            this.removeItem(product);
            // Indicate that product was remove because of invalid quantity
            this.errors.add(Translator.translate(
                "error.cart.productRemoved",
                new Object[]{product.getName()}
            ));
        }
        // If desired quantity above available quantity
        if (item.quantity > product.getQuantity()) {
            // Limit to available quantity
            item.quantity = product.getQuantity();
            // Indicate that product quantity was limited because of stock
            this.errors.add(Translator.translate(
                "error.cart.notEnoughStock",
                new Object[]{
                    product.getName(), product.getQuantity()
                }
            ));
        }
    }

    /**
     * Remove product from cart.
     *
     * @param toRemove Product to remove
     */
    public void removeItem(Product toRemove) {
        // Find product in cart
        CartItem item = this.findProduct(toRemove);
        // If found
        if (item != null) {
            // Remove it from cart
            this.items.remove(item);
        }
    }

    /**
     * Find product in cart.
     *
     * @param p Product to fing
     * @return Found cart item, or null if not found.
     */
    private CartItem findProduct(Product p) {
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
     * Get cart from HTTP session.
     *
     * @param session HTTP session.
     * @return User cart, or new cart if not initialized.
     */
    public static Cart getSessionCart(HttpSession session) {
        // Get session cart
        Cart cart = (Cart) session.getAttribute(SESSION_ATTRIBUTE);
        // If not initialized
        if (cart == null) {
            // Create it
            cart = new Cart();
        }
        // Clear previous cart errors
        cart.clearErrors();
        // Return cart
        return cart;
    }

    /**
     * Save cart in session.
     *
     * @param session HTTP session.
     */
    public void saveInSession(HttpSession session) {
        session.setAttribute(SESSION_ATTRIBUTE, this);
    }

    /**
     * Clear cart errors.
     */
    public void clearErrors() {
        // Clear error list
        this.errors.clear();
    }

    /**
     * Internal class representing a cart item
     */
    public class CartItem {

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
        public CartItem setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
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
