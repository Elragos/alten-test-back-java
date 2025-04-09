package fr.alten.test_back.repository;

import fr.alten.test_back.entity.Role;
import fr.alten.test_back.entity.RoleEnum;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * User authorization repository.
 *
 * @author Amarechal
 */
public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByRole(RoleEnum role);
}
