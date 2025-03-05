package fr.alten.test_back.config;

import fr.alten.test_back.error.CustomAccessDeniedHandler;
import fr.alten.test_back.error.CustomAuthenticationEntryPoint;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * App security configuration.
 *
 * @author AMarechal
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    /**
     * Used authentication provider.
     */
    private final AuthenticationProvider authenticationProvider;
    /**
     * Used JWT authentication filter.
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    /**
     * Used custom authentication entry point (for 401 errors).
     */
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    /**
     * Used custom access denied handler (for 403 errors).
     */
    private final CustomAccessDeniedHandler accessDeniedHandler;

    /**
     * Initialize configuration.
     *
     * @param jwtAuthenticationFilter Used JWT authentication filter.
     * @param authenticationProvider Used authentication provider.
     * @param authenticationEntryPoint Used custom authentication entry point
     * (for 401 errors).
     * @param accessDeniedHandler Used custom access denied handler (for 403
     * errors).
     */
    public SecurityConfiguration(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider,
            CustomAuthenticationEntryPoint authenticationEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    /**
     * Define security rules.
     *
     * @param http Used security.
     * @return Generated security filter chain.
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable CSRF because JWT is used
        http.csrf(csrf -> csrf.disable())
            // Configure routes authorization
            .authorizeHttpRequests(auth -> auth
                // Product -> need authenticated
                .requestMatchers("/product", "/product/[0-9]+").authenticated()
                // Wishlist -> need authenticated
                .requestMatchers("/wishlist", "/wishlist/[0-9]+").authenticated()
                // Other routes -> allow all
                .anyRequest().permitAll()
            )
            // Enable custom error handlinfs
            .exceptionHandling(exception -> exception
                // 401 Unauthorized
                .authenticationEntryPoint(authenticationEntryPoint)
                // 403 Forbidden
                .accessDeniedHandler(accessDeniedHandler)
            )
            // Set authentication provider
            .authenticationProvider(authenticationProvider)
            // Configure stateless session
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Add JWT filter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Define CORS configuration source
     *
     * @return CORS configuration source.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
