package fr.alten.test_back.config.errorHandling;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.alten.test_back.helper.Translator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Enable 401 errors in API.
 *
 * @author AMarechal
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // Set content type to JSON
        response.setContentType("application/json");
        // Set 401 error
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Set error message
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", Translator.translate(
            "error.auth.accessDenied.title"
        ));
        errorDetails.put("message", authException.getMessage());
        errorDetails.put("path", request.getRequestURI());

        // Write response
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorDetails));
    }
}
