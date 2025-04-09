package fr.alten.test_back.service;

import fr.alten.test_back.entity.Product;
import fr.alten.test_back.entity.User;
import fr.alten.test_back.entity.Wishlist;
import fr.alten.test_back.repository.UserRepository;
import fr.alten.test_back.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    /**
     * Used wishlist repository.
     */
    private final UserRepository userRepository;

    /**
     * Used user repository.
     */
    private final WishlistRepository wishlistRepository;

    /**
     * Initialize service.
     * @param userRepository Used user repository.
     * @param wishlistRepository Used wishlist repository.
     */
    public WishlistService(
        UserRepository userRepository,
        WishlistRepository wishlistRepository
    ){
        this.userRepository = userRepository;
        this.wishlistRepository = wishlistRepository;
    }

    /**
     * Get user wishlist product.
     * @param user Desired user.
     * @return User wishlist product, or empty list if user has no wishlist.
     */
    public Wishlist getWishlist(User user){
        if (user.getWishlist() == null){
            return new Wishlist();
        }
        return user.getWishlist();
    }

    /**
     * Add product to user wishlist.
     * @param user Desired user.
     * @param product Product to add.
     * @return Updated wishlist.
     */
    public Wishlist addProduct(User user, Product product){
        // Get user wishlist
        Wishlist wishlist = user.getWishlist();
        // If wishlist not created
        if (wishlist == null) {
            // Create wishlist
            wishlist = new Wishlist();
            // Save wishlist to DB
            wishlist = this.wishlistRepository.save(wishlist);
            // Register wishlist with user
            user.setWishlist(wishlist);
            // Save user in DB
            this.userRepository.save(user);
        }
        // Add product to wishlist
        wishlist.addProduct(product);
        // Save wishlist to DB
        wishlist = this.wishlistRepository.save(wishlist);

        return wishlist;
    }

    /**
     * Remove product from user wishlist.
     * @param user Desired user.
     * @param product Product to remove.
     * @return Updated wishlist, or empty wishlist if user has no wishlist.
     */
    public Wishlist removeProduct(User user, Product product){
        // Get user wishlist
        Wishlist wishlist = user.getWishlist();
        // If wishlist not created
        if (wishlist == null) {
            // Return empty wishlist
            return new Wishlist();
        }
        // Remove product from wishlist
        wishlist.removeProduct(product);
        // Save wishlist to DB
        wishlist = this.wishlistRepository.save(wishlist);
        // Return updated wishlist
        return wishlist;
    }

    /**
     * Remove product from all wishlists.
     * @param product Product to remove.
     */
    public void removeProductFromAll(Product product) {
        List<Wishlist> wishlists = this.wishlistRepository
                .findByProductsContaining(product);
        for (Wishlist current : wishlists){
            current.removeProduct(product);
            this.wishlistRepository.save(current);
        }
    }
}
