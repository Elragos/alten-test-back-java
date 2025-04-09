package fr.alten.test_back.repository;

import fr.alten.test_back.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Product repository.
 *
 * @author Amarechal
 */
public interface ProductRepository extends CrudRepository<Product, Integer> {
    Optional<Product> findByCode(String code);
}
