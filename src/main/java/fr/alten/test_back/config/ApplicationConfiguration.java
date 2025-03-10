package fr.alten.test_back.config;

import fr.alten.test_back.helper.Translator;
import fr.alten.test_back.repository.UserRepository;
import java.util.List;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

/**
 * Application global configuration.
 *
 * @author Amarechal
 */
@Configuration
public class ApplicationConfiguration {

    /**
     * Used user repository.
     */
    private final UserRepository userRepository;

    /**
     * Initialize configuration.
     *
     * @param userRepository Used user repository.
     */
    public ApplicationConfiguration(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    /**
     * Activate i18n configuration.
     *
     * @return I18n messages.
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("lang/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * Define locale resolver with HTTP Accept-language header.
     *
     * @return Generated locale resolver.
     */
    @Bean
    public AcceptHeaderLocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        // Default to english
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        // Set supported languages
        localeResolver.setSupportedLocales(List.of(
            // English
            Locale.ENGLISH,
            // French
            Locale.FRENCH
        ));
        return localeResolver;
    }

    /**
     * Define user service.
     *
     * @return User service.
     */
    @Bean
    UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException(
            Translator.translate(
                "error.auth.userNotFound",
                new Object[]{username}
            )
        ));
    }

    /**
     * Define password encoder.
     *
     * @return Password encoder.
     */
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define authentication manager.
     *
     * @param config Used authentication configuration.
     * @return Used autentication manager.
     * @throws Exception
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
     * @return Authentication provider.
     */
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}
