package fr.alten.test_back.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception generating a 400 bad request error. Used when an invalid
 * payload was sent.
 *
 * @author Amarechal.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidPayloadException extends RuntimeException {

    public InvalidPayloadException(String message) {
        super(message);
    }
}
