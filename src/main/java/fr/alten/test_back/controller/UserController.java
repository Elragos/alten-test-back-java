package fr.alten.test_back.controller;

import fr.alten.test_back.dto.user.LoginUserDto;
import fr.alten.test_back.dto.user.RegisterUserDto;
import fr.alten.test_back.entity.User;
import fr.alten.test_back.helper.AppRoutes;
import fr.alten.test_back.helper.JwtToken;
import fr.alten.test_back.response.RegisterResponse;
import fr.alten.test_back.service.AuthenticationService;
import fr.alten.test_back.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

        // Send response
        return ResponseEntity.ok(new RegisterResponse(
            registeredUser.getRealUsername(),
            registeredUser.getFirstname(),
            registeredUser.getEmail()
        ));
    }

    /**
     * Authenticate user.
     *
     * @param loginUserDto User login info.
     * @return API response.
     */
    @PostMapping(AppRoutes.LOGIN)
    public ResponseEntity<JwtToken> authenticate(
            @RequestBody LoginUserDto loginUserDto) {
        // Authenticate user
        User authenticatedUser = this.authenticationService.authenticate(loginUserDto);

        // Generate JWT token
        JwtToken jwtToken = this.jwtService.generateToken(authenticatedUser);

        // Send response
        return ResponseEntity.ok(jwtToken);
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
