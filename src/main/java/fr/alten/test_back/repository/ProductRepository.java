package fr.alten.test_back.repository;

import fr.alten.test_back.entity.Product;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author User
 */
public interface ProductRepository extends CrudRepository<Product, Integer>  {
    
}
