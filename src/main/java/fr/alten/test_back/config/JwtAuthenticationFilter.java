package fr.alten.test_back.config;

import fr.alten.test_back.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT authentication filter.
 *
 * @author Amarechal
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Used JWT service.
     */
    private final JwtService jwtService;

    /**
     * Used user details service.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Create filter.
     *
     * @param jwtService Used JWT service.
     * @param userDetailsService Used user details service.
     */
    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Get authorization header from request
        final String authHeader = request.getHeader("Authorization");

        // If found and is a bearer token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extract token
            String jwt = authHeader.substring(7);

            try {
                // Extract username from token
                String username = jwtService.extractUsername(jwt);
                // If found and context authentication not created
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Find user by email
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    // If token valid
                    if (this.jwtService.isTokenValid(jwt, userDetails)) {
                        // Create authentication
                        UsernamePasswordAuthenticationToken authToken
                                = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // Save authentication in security context
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } // If token expired
            catch (ExpiredJwtException ex) {
                // Do nothing, user will be unauthorized
            }
        }
        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}
