package fr.alten.test_back.response;

import java.util.Date;

/**
 * User login response.
 *
 * @author Amarechal
 */
public class LoginResponse {

    /**
     * User JWT token.
     */
    private String token;

    /**
     * JWT token expiration time (in milliseconds).
     */
    private Date expiresAt;

    /**
     * Get JWT token.
     *
     * @return JWT token.
     */
    public String getToken() {
        return this.token;
    }

    /**
     * Set JWT token.
     *
     * @param token New JWT token.
     * @return self
     */
    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    /**
     * Get JWT token expiration date.
     *
     * @return JWT token expiration date.
     */
    public Date getExpiresAt() {
        return this.expiresAt;
    }

    /**
     * Set JWT token expiration date.
     *
     * @param expiresAt New JWT token expiration date.
     * @return self
     */
    public LoginResponse setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

}
