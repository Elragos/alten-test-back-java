package fr.alten.test_back.dto;

/**
 * Create user DTO, used for create initial users.
 * 
 * @author amarechal
 */
public class CreateUserDTO extends RegisterUserDto {

    /**
     * User role
     */
    private String role;
    
       /**
     * Get user role.
     *
     * @return User role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Set user role.
     *
     * @param role New user role.
     * @return self
     */
    public CreateUserDTO setRole(String role) {
        this.role = role;
        return this;
    }
}
