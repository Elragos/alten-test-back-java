package fr.alten.test_back.repository;

import fr.alten.test_back.entity.Product;
import fr.alten.test_back.entity.Wishlist;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * Wishlist repository.
 *
 * @author Amarechal
 */
public interface WishlistRepository extends CrudRepository<Wishlist, Integer> {
    List<Wishlist> findByProductsContaining(Product product);
}
