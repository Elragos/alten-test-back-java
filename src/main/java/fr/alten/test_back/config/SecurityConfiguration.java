package fr.alten.test_back.config;

import fr.alten.test_back.config.errorHandling.CustomAccessDeniedHandler;
import fr.alten.test_back.config.errorHandling.CustomAuthenticationEntryPoint;
import fr.alten.test_back.helper.AppRoutes;
import fr.alten.test_back.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * App security configuration.
 *
 * @author AMarechal
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

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
     * @param authenticationEntryPoint Used custom authentication entry point
     * (for 401 errors).
     * @param accessDeniedHandler Used custom access denied handler (for 403
     * errors).
     */
    public SecurityConfiguration(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomAuthenticationEntryPoint authenticationEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    /**
     * Define security rules.
     *
     * @param http Used security.
     * @return Generated security filter chain.
     * @throws Exception If building security went wrong.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable CSRF because JWT is used
        http.csrf(AbstractHttpConfigurer::disable)
            // Configure routes authorization
            .authorizeHttpRequests(auth -> auth
                // Account creation & authentication -> allow all
                .requestMatchers(
                        AppRoutes.CREATE_ACCOUNT,
                        AppRoutes.LOGIN
                ).permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                // Generated errors -> allow all
                .requestMatchers("/error").permitAll()
                // Other routes -> need authentication
                .anyRequest().authenticated()
            )
            // Enable custom error handling
            .exceptionHandling(exception -> exception
                // 401 Unauthorized
                .authenticationEntryPoint(this.authenticationEntryPoint)
                // 403 Forbidden
                .accessDeniedHandler(this.accessDeniedHandler)
            )
            // Set authentication provider
            .authenticationProvider(authenticationProvider(null, null))
            // Configure stateless session
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Set logout page
            .logout(logout
                -> // Define logout URL
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
     * Define application role hierarchy.
     *
     * @return Defined hierarchy.
     */
    @Bean
    public static RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withRolePrefix("ROLE_")
                .role("ADMIN").implies("USER")
                .role("USER").implies("GUEST")
                .build();
    }

    /**
     * Define authentication manager.
     *
     * @param config Used authentication configuration.
     * @return Used authentication manager.
     * @throws Exception If something went wrong
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Define authentication provider.
     *
     * @param userDetailsService Used user details service.
     * @param passwordEncoder Used password encoder.
     * @return Authentication provider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

