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

    private static final String SESSION_ATTRIBUTE = "cart";

    /**
     * Initialize cart.
     */
    public Cart() {
        this.items = new ArrayList<>();
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
     * Add product to cart. If item quantity is 0 or below, remove it.
     *
     * @param quantity Quantity to add.
     * @param product Referring product.
     */
    public void addItem(int quantity, Product product) {
        CartItem item = this.findProduct(product);

        if (item == null) {
            item = new CartItem(quantity, product);
            this.items.add(item);
        } else {
            item.quantity += quantity;
            if (item.quantity <= 0) {
                this.removeItem(product);
            }
        }
    }

    /**
     * Remove product from cart.
     *
     * @param toRemove Product to remove
     */
    public void removeItem(Product toRemove) {
        CartItem item = this.findProduct(toRemove);
        if (item != null) {
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
        for (CartItem item : this.items) {
            if (Objects.equals(item.getProduct().getId(), p.getId())) {
                return item;
            }
        }
        return null;
    }

    /**
     * Get cart from HTTP session.
     *
     * @param session HTTP session.
     * @return User cart, or new cart if not initialized.
     */
    public static Cart getSessionCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute(SESSION_ATTRIBUTE);
        if (cart == null) {
            cart = new Cart();
        }
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
        public void setProduct(Product product) {
            this.product = product;
        }
    }
}
