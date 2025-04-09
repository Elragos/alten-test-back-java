package fr.alten.test_back.repository;

import fr.alten.test_back.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * User repository.
 *
 * @author AMarechal
 */
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
