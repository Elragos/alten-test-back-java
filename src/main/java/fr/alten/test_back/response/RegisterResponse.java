package fr.alten.test_back.response;

/**
 * User registration response.
 *
 * @author Amarechal
 */
public class RegisterResponse {

    /**
     * Created user name.
     */
    private String username;
    /**
     * Created user first name.
     */
    private String firstname;
    /**
     * Created user email.
     */
    private String email;

    /**
     * Get created user name.
     *
     * @return Created user name.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set created user name.
     *
     * @param username New created user name.
     * @return self
     */
    public RegisterResponse setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * Get created user name.
     *
     * @return Created user name.
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Set created user first name.
     *
     * @param firstname New created user first name.
     * @return self
     */
    public RegisterResponse setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    /**
     * Get created user name.
     *
     * @return Created user name.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set created user email.
     *
     * @param email New created user email.
     * @return self
     */
    public RegisterResponse setEmail(String email) {
        this.email = email;
        return this;
    }

}
