package fr.alten.test_back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
     * Used object mapper to write JSON in session.
     */
    private final ObjectMapper mapper;

    /**
     * Cart items session attribute.
     */
    private final String SESSION_ATTRIBUTE = "CART_ITEMS";

    /**
     * Initialize service.
     * @param service Used cart service
     */
    public CartController(
            CartService service,
            ObjectMapper mapper
    ) {
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * Get user cart items.
     *
     * @param session User session.
     * @return Cart item list.
     */
    @GetMapping
    public ResponseEntity<Cart> getCart(HttpSession session) throws JsonProcessingException {
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
    ) throws JsonProcessingException {
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
    ) throws JsonProcessingException {
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
    private Cart getSessionCart(HttpSession session) throws JsonProcessingException {
        // Get cart items from session
        Object sessionData = session.getAttribute(SESSION_ATTRIBUTE);

        // If session has no items
        if (sessionData == null){
            // Return empty cart
            return new Cart();
        }

        List<Cart.CartItem> items = this.mapper.readValue(
                sessionData.toString(),
                new TypeReference<>() {}
        );
        // Else return cart with found items.
        return new Cart(items);
    }

    /**
     * Save cart in session.
     * @param session User session.
     * @param cart User cart.
     */
    private void saveCart(HttpSession session, Cart cart) throws JsonProcessingException {
        String json = mapper.writeValueAsString(cart.getItems());
        session.setAttribute(SESSION_ATTRIBUTE, json);
    }

}
