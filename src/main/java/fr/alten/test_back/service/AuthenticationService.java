package fr.alten.test_back.service;

import fr.alten.test_back.dto.user.LoginUserDto;
import fr.alten.test_back.dto.user.RegisterUserDto;
import fr.alten.test_back.entity.Role;
import fr.alten.test_back.entity.RoleEnum;
import fr.alten.test_back.entity.User;
import fr.alten.test_back.helper.Translator;
import fr.alten.test_back.repository.RoleRepository;
import fr.alten.test_back.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

/**
 * Application authentication service.
 *
 * @author Amarechal
 */
@Service
public class AuthenticationService {

    /**
     * Used user repository.
     */
    private final UserRepository userRepository;

    /**
     * Used password encoder.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Used authentication manager.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Used role repository.
     */
    private final RoleRepository roleRepository;

    /**
     * Initialize service.
     *
     * @param userRepository Used user repository.
     * @param authenticationManager Used authentication manager.
     * @param passwordEncoder Used password encoder.
     * @param roleRepository Used role repository.
     */
    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    /**
     * Create new user.
     *
     * @param input User registration info.
     * @return Created user.
     * @throws ResponseStatusException If user email already used.
     */
    public User signup(RegisterUserDto input) throws ResponseStatusException {
        // Test if user email already exists in DB
        Optional<User> exisiting = this.userRepository.findByEmail(input.email());
        // If found
        if (exisiting.isPresent()) {
            // Throw error
            throw new InvalidParameterException(Translator.translate(
                    "error.auth.emailAlreadyExists",
                    new Object[]{input.email()}
            ));
        }

        // Else create user
        User user = new User()
                .setUsername(input.username())
                .setFirstname(input.firstname())
                .setEmail(input.email())
                .setPassword(this.passwordEncoder.encode(input.password()))
                // Set user role by default
                .setRoles(List.of(getUserRole()));

        return this.userRepository.save(user);
    }

    /**
     * Authenticate user.
     *
     * @param input User authentication info.
     * @return Found user.
     * @throws AuthenticationException If authentication failed.
     * @throws InvalidParameterException If user not found.
     */
    public User authenticate(LoginUserDto input)
            throws AuthenticationException, ResponseStatusException {
        this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                input.email(),
                input.password()
            )
        );

        return this.userRepository.findByEmail(input.email())
            .orElseThrow(() -> new InvalidParameterException(Translator.translate(
                "error.auth.failed"
            )));
    }

    /**
     * Get application user role.
     *
     * @return User role.
     */
    private Role getUserRole() {
        return this.roleRepository.findByRole(RoleEnum.ROLE_USER).orElseThrow();
    }
}
