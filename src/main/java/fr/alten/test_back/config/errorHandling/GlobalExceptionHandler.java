package fr.alten.test_back.config.errorHandling;

import fr.alten.test_back.helper.Translator;
import fr.alten.test_back.response.ErrorResponse;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.InvalidParameterException;

/**
 * Global exception handler
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle invalid parameter exceptions.
     * @param ex Generated exception.
     * @return Corresponding bad request ResponseEntity.
     */
    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<ErrorResponse> handleInvalidParameter(InvalidParameterException ex){
        String invalidParameterTitle = Translator.translate(
                "error.generic.invalidParameter"
        );
        ErrorResponse error = new ErrorResponse(invalidParameterTitle, ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Handle resource not found exceptions.
     * @param ex Generated exception.
     * @return Corresponding not found ResponseEntity.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleInvalidParameter(ResourceNotFoundException ex){
        String resourceNotFoundTitle = Translator.translate(
                "error.generic.notFound"
        );
        ErrorResponse error = new ErrorResponse(resourceNotFoundTitle, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
