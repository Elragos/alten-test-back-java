package fr.alten.test_back.controller;

import fr.alten.test_back.dto.LoginUserDto;
import fr.alten.test_back.dto.RegisterUserDto;
import fr.alten.test_back.entity.User;
import fr.alten.test_back.response.LoginResponse;
import fr.alten.test_back.response.RegisterResponse;
import fr.alten.test_back.service.AuthenticationService;
import fr.alten.test_back.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controller handling user interactions.
 *
 * @author AMarechal
 */
@RestController
public class LoginController {

    /**
     * Used JWT service.
     */
    private final JwtService jwtService;

    /**
     * Used authentication service.
     */
    private final AuthenticationService authenticationService;

    /**
     * Initialize controller.
     *
     * @param jwtService Used JWT service.
     * @param authenticationService Used authentication service.
     */
    public LoginController(JwtService jwtService,
            AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    /**
     * Create user account.
     *
     * @param registerUserDto User info.
     * @return API response.
     */
    @PostMapping("/account")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterUserDto registerUserDto) {
        // Create user
        User registeredUser = this.authenticationService.signup(registerUserDto);
        // Generate response
        RegisterResponse response = new RegisterResponse()
                .setEmail(registeredUser.getEmail())
                .setUsername(registeredUser.getUsername())
                .setFirstname(registeredUser.getFirstname());
        // Send response
        return ResponseEntity.ok(response);
    }

    /**
     * Authenticate user.
     *
     * @param loginUserDto User login info.
     * @return API response.
     */
    @PostMapping("/token")
    public ResponseEntity<LoginResponse> authenticate(
            @RequestBody LoginUserDto loginUserDto) {
        // Authenticate user
        User authenticatedUser = this.authenticationService.authenticate(loginUserDto);

        // Generate JWT token
        String jwtToken = this.jwtService.generateToken(authenticatedUser);

        // Generate response
        LoginResponse loginResponse = new LoginResponse()
                .setToken(jwtToken)
                .setExpiresIn(this.jwtService.getExpirationTime());

        // Send response
        return ResponseEntity.ok(loginResponse);
    }

}
