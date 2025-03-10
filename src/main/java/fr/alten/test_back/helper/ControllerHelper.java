package fr.alten.test_back.helper;

import fr.alten.test_back.entity.AuthorityEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Controller helpers.
 *
 * @author AMarechal
 */
public class ControllerHelper {

    /**
     * Check if user has authority.
     *
     * @param authority Checked authority.
     * @return <code>true</code> if user has the authority, <code>false</code>
     * otherwise.
     */
    public static boolean userHasAuthority(AuthorityEnum authority) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream().anyMatch(
                a -> a.getAuthority().equals(authority.name())
        );
    }

    /**
     * Check if user has admin authority.
     *
     * @throws AccessDeniedException If user is not admin.
     */
    public static void checkAdminAuthenticated() throws AccessDeniedException {
        // If user has not admin authority.
        if (!userHasAuthority(AuthorityEnum.ROLE_ADMIN)) {
            // Throw access denied exception
            throw new AccessDeniedException(Translator.translate(
                    "error.auth.unauthorized", null
            ));
        }
    }
}
