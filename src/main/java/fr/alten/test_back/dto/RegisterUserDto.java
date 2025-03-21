package fr.alten.test_back.dto;

/**
 * User registration DTO.
 *
 * @author Amarechal
 */
public class RegisterUserDto {

    /**
     * User name.
     */
    private String username;
    /**
     * USer first name.
     */
    private String firstname;
    /**
     * User email.
     */
    private String email;
    /**
     * User plain password.
     */
    private String password;

    /**
     * Get user name.
     *
     * @return User name.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set user name.
     *
     * @param username New user name.
     * @return self
     */
    public RegisterUserDto setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * Get user first name.
     *
     * @return User first name.
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Set user first name.
     *
     * @param firstname New user first name.
     * @return self
     */
    public RegisterUserDto setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

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
    public RegisterUserDto setEmail(String email) {
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
     * @param password New plain password.
     * @return self
     */
    public RegisterUserDto setPassword(String password) {
        this.password = password;
        return this;
    }

}
