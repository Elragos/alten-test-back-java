package fr.alten.test_back.controller;

import fr.alten.test_back.dto.AddProductToCartDto;
import fr.alten.test_back.entity.Product;
import fr.alten.test_back.error.InvalidPayloadException;
import fr.alten.test_back.helper.AppRoutes;
import fr.alten.test_back.helper.Cart;
import fr.alten.test_back.helper.ProductHelper;
import fr.alten.test_back.helper.Translator;
import fr.alten.test_back.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller handling user cart interactions.
 *
 * @author AMarechal
 */
@RestController
@RequestMapping(AppRoutes.CART)
public class CartController {

    /**
     * Used product repository.
     */
    @Autowired
    private ProductRepository productRepository;

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
     * @param id Product DB ID to add.
     * @param payload Payload.
     * @return Updated cart item list.
     */
    @PostMapping("/{id}")
    public ResponseEntity<Cart> addProduct(
            HttpSession session,
            @RequestBody AddProductToCartDto payload,
            @PathVariable int id
    ) {
        Cart cart = Cart.getSessionCart(session);
        Product toAdd = ProductHelper.findProduct(id, this.productRepository);
        cart.addItem(payload.getQuantity(), toAdd);
        cart.saveInSession(session);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Cart> removeProduct(
            HttpSession session,
            @PathVariable int id
    ) {
        Cart cart = Cart.getSessionCart(session);
        Product toRemove = ProductHelper.findProduct(id, this.productRepository);
        cart.removeItem(toRemove);
        cart.saveInSession(session);
        return ResponseEntity.ok(cart);
    }
}
