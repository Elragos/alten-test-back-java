package fr.alten.test_back.response;

/**
 * Record used to send error to client.
 *
 * @param error   Error title.
 * @param message Error message.
 */
public record ErrorResponse(String error, String message) { }
