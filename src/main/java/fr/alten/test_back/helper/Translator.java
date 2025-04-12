package fr.alten.test_back.helper;

import fr.alten.test_back.config.translation.YamlMessageSource;
import jakarta.annotation.PostConstruct;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Application translator.
 *
 * @author Amarechal
 */
@Component
public class Translator {

    /**
     * Used message source for translator.
     */
    private static YamlMessageSource messageSource;

    /**
     * Injected message source from Spring
     */
    private final YamlMessageSource injectMessageSource;

    /**
     * Create translator.
     * @param messageSource Used message source.
     */
    public Translator(YamlMessageSource messageSource) {
        this.injectMessageSource = messageSource;
    }

    /**
     * Initialize translator.
     */
    @PostConstruct
    public void init(){
        Translator.messageSource = this.injectMessageSource;
    }

    /**
     * Translate message.
     *
     * @param messageCode Message code.
     * @return Translated message.
     */
    public static String translate(String messageCode){
        return Translator.translate(messageCode, new Object[]{}, LocaleContextHolder.getLocale());
    }

    /**
     * Translate message.
     *
     * @param messageCode Message code
     * @param locale Specified locale.
     * @return Translated message.
     */
    public static String translate(String messageCode, Locale locale){
        return Translator.translate(messageCode, new Object[]{}, locale);
    }

    /**
     * Translate message.
     *
     * @param messageCode Message code.
     * @param params Message parameters.
     * @return Translated message.
     */
    public static String translate(String messageCode, Object[] params) {
        return Translator.translate(messageCode, params, LocaleContextHolder.getLocale());
    }

    /**
     * Translate message.
     *
     * @param messageCode Message code.
     * @param params Message parameters.
     * @param locale Specified locale.
     * @return Translated message.
     */
    public static String translate(String messageCode, Object[] params, Locale locale) {
        return messageSource.getMessage(messageCode, params, locale);
    }
}
