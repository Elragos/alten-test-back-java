/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.alten.test_back.helper;

import fr.alten.test_back.entity.AuthorityEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author User
 */
public class ControllerHelper {
    public static boolean userHasAuthority(AuthorityEnum authority){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream().anyMatch(
                a -> a.getAuthority().equals(authority.name())
        );
    }
    
    public static void checkAdminAuthenticated(){
        if (!userHasAuthority(AuthorityEnum.ROLE_ADMIN)){
            throw new AccessDeniedException("Operation not allowed");
        }
    }
}
