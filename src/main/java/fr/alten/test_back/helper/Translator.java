package fr.alten.test_back.helper;

import jakarta.annotation.PostConstruct;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
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
    private static ResourceBundleMessageSource messageSource;

    /**
     * Injected message source from Spring
     */
    private final ResourceBundleMessageSource injectMessageSource;

    /**
     * Create translator.
     * @param resourceBundleMessageSource Used message source.
     */
    public Translator(ResourceBundleMessageSource resourceBundleMessageSource) {
        this.injectMessageSource = resourceBundleMessageSource;
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
        return messageSource.getMessage(messageCode, params, LocaleContextHolder.getLocale());
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
