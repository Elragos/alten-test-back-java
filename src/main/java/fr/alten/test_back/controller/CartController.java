package fr.alten.test_back.controller;

import fr.alten.test_back.dto.cart.AddProductToCartDto;
import fr.alten.test_back.entity.Product;
import fr.alten.test_back.helper.AppRoutes;
import fr.alten.test_back.helper.Cart;
import fr.alten.test_back.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controller handling user cart interactions.
 *
 * @author AMarechal
 */
@RestController
@RequestMapping(AppRoutes.CART)
public class CartController {

    /**
     * Used product service.
     */
    private final ProductService service;

    /**
     * Initialize service.
     * @param service Used product service
     */
    public CartController(ProductService service) {
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
        return ResponseEntity.ok(Cart.getSessionCart(session));
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
        Cart cart = Cart.getSessionCart(session);
        Product toAdd = this.service.getByCode(payload.productCode());
        cart.addItem(payload.quantity(), toAdd);
        cart.saveInSession(session);
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
        Cart cart = Cart.getSessionCart(session);
        Product toRemove = this.service.getByCode(code);
        cart.removeItem(toRemove);
        cart.saveInSession(session);
        return ResponseEntity.ok(cart);
    }
}
