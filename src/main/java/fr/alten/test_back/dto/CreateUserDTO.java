package fr.alten.test_back.dto;

/**
 * Create user DTO, used for create initial users.
 * 
 * @author amarechal
 */
public class CreateUserDTO extends RegisterUserDto {

    /**
     * User authority
     */
    private String authority;
    
       /**
     * Get user authority.
     *
     * @return User authority.
     */
    public String getAuthority() {
        return authority;
    }

    /**
     * Set user authority.
     *
     * @param authority New user authority.
     * @return self
     */
    public CreateUserDTO setAuthority(String authority) {
        this.authority = authority;
        return this;
    }
}
