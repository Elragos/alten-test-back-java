package fr.alten.test_back.dto.user;

/**
 * User registration DTO.
 *
 * @param username User name.
 * @param firstname User first name.
 * @param email User email.
 * @param password User plain password.
 */
public record RegisterUserDto(
    String username,
    String firstname,
    String email,
    String password
) {}