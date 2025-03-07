package fr.alten.test_back.repository;

import fr.alten.test_back.entity.User;
import fr.alten.test_back.entity.Wishlist;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * Wishlist repository.
 *
 * @author Amarechal
 */
public interface WishlistRepository extends CrudRepository<Wishlist, Integer> {
    
}
