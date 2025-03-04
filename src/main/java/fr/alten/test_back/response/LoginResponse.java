package fr.alten.test_back.response;

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
    private long expiresIn;

    /**
     * Get JWT token.
     *
     * @return JWT token.
     */
    public String getToken() {
        return token;
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
     * Get JWT token expiration time (in milliseconds).
     *
     * @return JWT token expiration time (in milliseconds).
     */
    public long getExpiresIn() {
        return expiresIn;
    }

    /**
     * Set JWT token expiration time (in milliseconds).
     *
     * @param expiresIn New JWT token expiration time (in milliseconds).
     * @return self
     */
    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

}
