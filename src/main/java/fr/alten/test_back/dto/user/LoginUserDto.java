package fr.alten.test_back.dto.user;

/**
 * Login user DTO, used to authenticate user in API .
 *
 * @author Amarechal
 */
public record LoginUserDto (String email, String password) {

}
