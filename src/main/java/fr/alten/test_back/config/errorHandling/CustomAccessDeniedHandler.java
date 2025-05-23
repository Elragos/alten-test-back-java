package fr.alten.test_back.config.errorHandling;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.alten.test_back.helper.Translator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom access denied handler.
 *
 * @author Amarechal
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        // Set content type to JSON
        response.setContentType("application/json");
        // Set 403 error
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // Set error message
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", Translator.translate(
            "error.auth.accessDenied.title"
        ));
        errorDetails.put("message", Translator.translate(
            "error.auth.accessDenied.message"
        ));
        errorDetails.put("path", request.getRequestURI());

        // Write response
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorDetails));
    }
}
