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

    public LoginController(JwtService jwtService, AuthenticationService authenticationService) {
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
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        RegisterResponse response = new RegisterResponse()
                .setEmail(registeredUser.getEmail())
                .setUsername(registeredUser.getUsername())
                .setFirstname(registeredUser.getFirstname());

        return ResponseEntity.ok(response);
    }

    /**
     * Authenticate user.
     *
     * @param loginUserDto User login info.
     * @return API response.
     */
    @PostMapping("/token")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse()
                .setToken(jwtToken)
                .setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

}
