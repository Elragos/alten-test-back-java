package fr.alten.test_back.service;

import fr.alten.test_back.dto.LoginUserDto;
import fr.alten.test_back.dto.RegisterUserDto;
import fr.alten.test_back.entity.User;
import fr.alten.test_back.repository.UserRepository;
import java.util.Optional;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
     * Initialize service.
     *
     * @param userRepository Used user repository.
     * @param authenticationManager Used authentication manager.
     * @param passwordEncoder Used password encoder.
     */
    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        Optional<User> exisiting = this.userRepository.findByEmail(input.getEmail());
        // If found
        if (exisiting.isPresent()) {
            // Throw error
            throw new ResponseStatusException(HttpStatusCode.valueOf(400),
                    "User already registered with this email");
        }

        // Else create user
        User user = new User()
                .setUsername(input.getUsername())
                .setFirstname(input.getFirstname())
                .setEmail(input.getEmail())
                .setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Authenticate user.
     *
     * @param input User authentication info.
     * @return Found user.
     * @throws AuthenticationException If authentication failed.
     * @throws ResponseStatusException If user not found.
     */
    public User authenticate(LoginUserDto input)
            throws AuthenticationException, ResponseStatusException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(400),
                "Invalid Email or password"));
    }
}
