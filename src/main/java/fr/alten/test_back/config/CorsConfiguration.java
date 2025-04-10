package fr.alten.test_back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfiguration {
    /**
     * Define CORS configuration source
     *
     * @return CORS configuration source.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        // Define authorized HTTP methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE"));
        // Define authorized headers
        configuration.setAllowedHeaders(List.of(
                // Bearer token
                "Authorization",
                // Locale to use
                "Accept-Language",
                // Content type (to communicate in JSON)
                "Content-Type"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
