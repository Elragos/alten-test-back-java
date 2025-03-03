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
     * JWT token expiration time.
     */
    private long expiresIn;

    public String getToken() {
        return token;
    }

    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

}
