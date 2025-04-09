package fr.alten.test_back.response;

/**
 * User registration response.
 * @param email Registered email.
 * @param firstname Registered first name.
 * @param username Registered user name.
 */
public record RegisterResponse(String username, String firstname, String email) {}
