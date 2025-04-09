package fr.alten.test_back.repository;

import fr.alten.test_back.entity.Product;
import fr.alten.test_back.entity.Wishlist;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Wishlist repository.
 *
 * @author Amarechal
 */
public interface WishlistRepository extends CrudRepository<Wishlist, Integer> {
    List<Wishlist> findByProductsContaining(Product product);
}
