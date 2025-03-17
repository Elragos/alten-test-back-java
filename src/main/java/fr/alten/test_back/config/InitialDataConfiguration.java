package fr.alten.test_back.config;

import fr.alten.test_back.entity.Authority;
import fr.alten.test_back.entity.AuthorityEnum;
import fr.alten.test_back.entity.User;
import fr.alten.test_back.repository.AuthorityRepository;
import fr.alten.test_back.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Initial data configuration. This ensures that all authorities are registered
 * in database and default admin account exists.
 *
 * @author amarechal
 */
@Configuration
public class InitialDataConfiguration {

    /**
     * Used user repository.
     */
    private final UserRepository userRepository;

    /**
     * Used authority repository.
     */
    private final AuthorityRepository authorityRepository;

    /**
     * Used password encoder.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Initiate data configuration.
     *
     * @param userRepository Used user repository.
     * @param authorityRepository Used authority repository.
     * @param passwordEncoder Used password encoder.
     */
    public InitialDataConfiguration(
            UserRepository userRepository,
            AuthorityRepository authorityRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Generate initial data after application contruction.
     */
    @PostConstruct
    public void postConstruct() {
        // For all authorities available
        for (AuthorityEnum authority : AuthorityEnum.values()) {
            // Create in DB
            this.generateAuthority(authority);
        }
        // Create admin account
        createAdminAccount();
    }

    /**
     * Create authority in DB if not exists.
     *
     * @param authority Desired authority.
     */
    private void generateAuthority(AuthorityEnum authority) {
        Optional<Authority> data = this.authorityRepository
                .findByAuthority(authority);

        if (data.isEmpty()) {
            Authority toCreate = new Authority()
                    .setAuthority(authority);
            this.authorityRepository.save(toCreate);
        }
    }

    /**
     * Create default admin account with pwd 123456 if not exists.
     */
    private void createAdminAccount() {
        Optional<User> admin = this.userRepository
                .findByEmail("admin@admin.com");

        if (admin.isEmpty()) {
            User toCreate = new User()
                    .setEmail("admin@admin.com")
                    .setUsername("admin")
                    .setFirstname("admin")
                    .setPassword(this.passwordEncoder.encode("123456"))
                    .setAuthorities(List.of(
                            this.authorityRepository
                                    .findByAuthority(AuthorityEnum.ROLE_ADMIN).get()
                    ));
            this.userRepository.save(toCreate);
        }
    }
}
