package fr.alten.test_back.dto.user;

/**
 * Create user DTO, used for create initial users.
 *
 * @param username User name.
 * @param firstname User first name.
 * @param email User email.
 * @param password User plain password.
 * @param role User role.
 */
public record CreateUserDto(
        String username,
        String firstname,
        String email,
        String password,
        String role
) {}
