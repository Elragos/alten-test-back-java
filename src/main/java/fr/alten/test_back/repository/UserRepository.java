package fr.alten.test_back.repository;

import fr.alten.test_back.entity.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * User repository.
 *
 * @author AMarechal
 */
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
