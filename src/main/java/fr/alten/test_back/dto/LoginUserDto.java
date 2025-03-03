package fr.alten.test_back.dto;

/**
 * Login user DTO.
 * @author Amarechal
 */
public class LoginUserDto {
    private String email;
    
    private String password;
    
    // getters and setters here...

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
