package fr.alten.test_back.dto;

/**
 * Login user DTO, used to configure API authentication.
 *
 * @author Amarechal
 */
public class LoginUserDto {

    /**
     * User email.
     */
    private String email;

    /**
     * User plain password.
     */
    private String password;

    /**
     * Get user email.
     *
     * @return User email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set user email.
     *
     * @param email New user email.
     * @return self
     */
    public LoginUserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * Get user plain password.
     *
     * @return User plain password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set user plain password.
     *
     * @param password New user plain password.
     * @return self
     */
    public LoginUserDto setPassword(String password) {
        this.password = password;
        return this;
    }

}
