package fr.alten.test_back.controller;

import fr.alten.test_back.dto.LoginUserDto;
import fr.alten.test_back.dto.RegisterUserDto;
import fr.alten.test_back.entity.User;
import fr.alten.test_back.helper.AppRoutes;
import fr.alten.test_back.helper.JwtToken;
import fr.alten.test_back.response.LoginResponse;
import fr.alten.test_back.response.RegisterResponse;
import fr.alten.test_back.service.AuthenticationService;
import fr.alten.test_back.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controller handling user interactions.
 *
 * @author AMarechal
 */
@RestController
public class UserController {

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
    public UserController(
        JwtService jwtService,
        AuthenticationService authenticationService
    ) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    /**
     * Create user account.
     *
     * @param registerUserDto User info.
     * @return API response.
     */
    @PostMapping(AppRoutes.CREATE_ACCOUNT)
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterUserDto registerUserDto) {
        // Create user
        User registeredUser = this.authenticationService.signup(registerUserDto);
        // Generate response
        RegisterResponse response = new RegisterResponse()
                .setEmail(registeredUser.getEmail())
                .setUsername(registeredUser.getRealUsername())
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
    @PostMapping(AppRoutes.LOGIN)
    public ResponseEntity<LoginResponse> authenticate(
            @RequestBody LoginUserDto loginUserDto) {
        // Authenticate user
        User authenticatedUser = this.authenticationService.authenticate(loginUserDto);

        // Generate JWT token
        JwtToken jwtToken = this.jwtService.generateToken(authenticatedUser);

        // Generate response
        LoginResponse loginResponse = new LoginResponse()
            .setToken(jwtToken.getValue())
            .setExpiresAt(jwtToken.getExpirationDate());

        // Send response
        return ResponseEntity.ok(loginResponse);
    }

    /**
     * Get user information.
     *
     * @return User Information.
     */
    @GetMapping(AppRoutes.USER_INFO)
    public ResponseEntity<Authentication> getAuthenticatedUserInfo() {
        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication());
    }
}
