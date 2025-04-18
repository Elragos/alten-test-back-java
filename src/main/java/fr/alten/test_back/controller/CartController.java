package fr.alten.test_back.controller;

import fr.alten.test_back.dto.cart.AddProductToCartDto;
import fr.alten.test_back.helper.AppRoutes;
import fr.alten.test_back.helper.Cart;
import fr.alten.test_back.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Controller handling user cart interactions.
 *
 * @author AMarechal
 */
@RestController
@RequestMapping(AppRoutes.CART)
public class CartController {

    /**
     * Used cart service.
     */
    private final CartService service;

    /**
     * Cart items session attribute.
     */
    private final String SESSION_ATTRIBUTE = "CART_ITEMS";

    /**
     * Initialize service.
     * @param service Used cart service
     */
    public CartController(CartService service) {
        this.service = service;
    }

    /**
     * Get user cart items.
     *
     * @param session User session.
     * @return Cart item list.
     */
    @GetMapping
    public ResponseEntity<Cart> getCart(HttpSession session) {
        // Get cart from session
        Cart cart = this.getSessionCart(session);
        // Refresh cart
        this.service.refresh(cart);
        // Return cart
        return ResponseEntity.ok(cart);
    }

    /**
     * Add product to user cart. Required payload : { "quantity": integer }
     *
     * @param session User session.
     * @param payload Payload.
     * @return Updated cart item list.
     */
    @PostMapping
    public ResponseEntity<Cart> addProduct(
            HttpSession session,
            @RequestBody AddProductToCartDto payload
    ) {
        Cart cart = this.getSessionCart(session);
        this.service.addItem(cart, payload.quantity(), payload.productCode());
        this.saveCart(session, cart);
        return ResponseEntity.ok(cart);
    }

    /**
     * Delete product from cart.
     * @param session User http session.
     * @param code Desired product code.
     * @return Updated cart.
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Cart> removeProduct(
            HttpSession session,
            @PathVariable String code
    ) {
        Cart cart = this.getSessionCart(session);
        this.service.removeItem(cart, code);
        this.saveCart(session, cart);
        return ResponseEntity.ok(cart);
    }

    /**
     * Get user cart from session.
     * @param session User session.
     * @return User cart
     */
    private Cart getSessionCart(HttpSession session) {
        // Get cart items from session
        List<Cart.CartItem> items = (List<Cart.CartItem>) session.getAttribute(SESSION_ATTRIBUTE);
        // If session has no items
        if (items == null){
            // Return empty cart
            return new Cart();
        }
        // Else return cart with found items.
        return new Cart(items);
    }

    /**
     * Save cart in session.
     * @param session User session.
     * @param cart User cart.
     */
    private void saveCart(HttpSession session, Cart cart) {
        session.setAttribute(SESSION_ATTRIBUTE, cart.getItems());
    }

}
