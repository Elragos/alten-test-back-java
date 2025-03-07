package fr.alten.test_back.repository;

import fr.alten.test_back.entity.Authority;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * User authorization repository.
 *
 * @author Amarechal
 */
public interface AuthorityRepository extends CrudRepository<Authority, Long> {
    Optional<Authority> findByAuthority(String authority);
}
