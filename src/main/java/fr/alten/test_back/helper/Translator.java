package fr.alten.test_back.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

/**
 * Application translator.
 *
 * @author Amarechal
 */
@Component
public class Translator {

    /**
     * Used messages.
     */
    private static ResourceBundleMessageSource messageSource;

    @Autowired
    Translator(ResourceBundleMessageSource resourceBundleMessageSource) {
        Translator.messageSource = resourceBundleMessageSource;
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
}
