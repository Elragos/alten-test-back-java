package fr.alten.test_back.controller;

import fr.alten.test_back.entity.Product;
import fr.alten.test_back.entity.User;
import fr.alten.test_back.entity.Wishlist;
import fr.alten.test_back.error.ResourceNotFoundException;
import fr.alten.test_back.repository.UserRepository;
import fr.alten.test_back.repository.ProductRepository;
import fr.alten.test_back.repository.WishlistRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling wishlist interactions.
 *
 * @author AMarechal
 */
@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    /**
     * Used product repository.
     */
    @Autowired
    private ProductRepository productRepository;

    /**
     * Used user repository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Used wishlist repository.
     */
    @Autowired
    private WishlistRepository wishlistRepository;

    /**
     * List all available products in current user's wishlist.
     *
     * @return Current user's wishlist product list.
     */
    @GetMapping
    public Iterable<Product> listProducts() {
        Wishlist wishlist = getCurrentUserWishlist();

        // If user has no wishlist yet
        if (wishlist == null) {
            // Return empty list
            return List.of();
        }
        // Else return wishlist product list
        return wishlist.getProducts();
    }

    /**
     * Add product to wishlist.
     *
     * @param id Product DB ID too add.
     * @return Updated wishlist product list.
     */
    @PostMapping("/{id}")
    public Iterable<Product> addProduct(@PathVariable Integer id) {
        // Get user wishlist        
        Wishlist wishlist = this.getCurrentUserWishlist();
        // If wishlist not created
        if (wishlist == null) {
            // Get owner
            User owner = this.getCurrentUser();
            // Create wishlist
            wishlist = new Wishlist().setOwner(owner);
            // Save wishlist to DB
            wishlist = this.wishlistRepository.save(wishlist);
            // Register wishlist with user
            owner.setWishlist(wishlist);
            // Save user in DB
            this.userRepository.save(owner);
        }
        // Find adding product
        Product toAdd = this.findProduct(id);
        // Add product to wishlist
        wishlist.addProduct(toAdd);
        // Save wishlist to DB
        wishlist = this.wishlistRepository.save(wishlist);
        // Return updated wishlist
        return wishlist.getProducts();
    }

    /**
     * Remove product to DB.
     *
     * @param id Product DB ID to update.
     * @return Deleted product.
     */
    @DeleteMapping("/{id}")
    public Iterable<Product> removeProduct(@PathVariable Integer id) {
        // Get user wishlist        
        Wishlist wishlist = this.getCurrentUserWishlist();
        // If wishlist not created
        if (wishlist == null) {
            // Return empty list
            return List.of();
        }
        // Find product to remove
        Product toRemove = this.findProduct(id);
        // Remove product from wishlist
        wishlist.removeProduct(toRemove);
        // Save wishlist to DB
        wishlist = this.wishlistRepository.save(wishlist);
        // Return updated wishlist
        return wishlist.getProducts();

    }

    /**
     * Get current user's wishlist.
     *
     * @return Current user's wishlist.
     */
    private Wishlist getCurrentUserWishlist() {
        // Get current user's wishlist
        return this.getCurrentUser().getWishlist();
    }

    /**
     * Get current user.
     *
     * @return Current user.
     */
    private User getCurrentUser() {
        // Get authenticated user email
        String userEmail = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        // Get user by email
        Optional<User> user = this.userRepository.findByEmail(userEmail);
        // Get user wishlist
        return user.get();
    }

    /**
     * Find product by DB id.
     *
     * @param id Product DB ID.
     * @return Found product.
     * @throws ResourceNotFoundException If no product found.
     */
    private Product findProduct(Integer id) throws ResourceNotFoundException {
        Optional<Product> product = this.productRepository.findById(id);
        // If not found
        if (product.isEmpty()) {
            //Throw 404 error
            throw new ResourceNotFoundException("Unable to find product");
        }

        return product.get();
    }
}
