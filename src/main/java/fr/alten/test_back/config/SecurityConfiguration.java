package fr.alten.test_back.config;

import fr.alten.test_back.error.CustomAccessDeniedHandler;
import fr.alten.test_back.error.CustomAuthenticationEntryPoint;
import fr.alten.test_back.helper.AppRoutes;
import jakarta.servlet.http.HttpServletResponse;
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
                // Account creation & authentication -> allow all
                .requestMatchers(
                    AppRoutes.CREATE_ACCOUNT,
                    AppRoutes.LOGIN
                ).permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                // Genereated errors -> allow all
                .requestMatchers("/error").permitAll()
                // Other routes -> need authentication
                .anyRequest().authenticated()
            )
            // Enable custom error handlinfs
            .exceptionHandling(exception -> exception
                // 401 Unauthorized
                .authenticationEntryPoint(this.authenticationEntryPoint)
                // 403 Forbidden
                .accessDeniedHandler(this.accessDeniedHandler)
            )
            // Set authentication provider
            .authenticationProvider(this.authenticationProvider)
            // Configure stateless session
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Set logout page
            .logout(logout-> 
                // Define logout URL
                logout.logoutUrl(AppRoutes.LOGOUT)
                // Clear authentication context
                .clearAuthentication(true)
                // Invalidate session
                .invalidateHttpSession(true)
                // Remove session cookie
                .deleteCookies("JSESSIONID")
                // Disable logout redirection 
                .logoutSuccessHandler((request, response, authentication) -> {
                    // Just send HTTP 200
                    response.setStatus(HttpServletResponse.SC_OK);
                })
                // Allow all to access logout page
                .permitAll()
            )
            // Add JWT filter
            .addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Build security rules
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
        // Define authorized HTTP methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE"));
        // Define authorized headers
        configuration.setAllowedHeaders(List.of(
            // Bearer token
            "Authorization", 
            // Locale to use
            "Accept-Language",
            // Content type (to communicate in JSON
            "Content-Type"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
