package fr.alten.test_back.service;

import fr.alten.test_back.entity.User;
import fr.alten.test_back.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository){
        this.repository = repository;
    }

    public User getAuthenticatedUser(){
        // Get authenticated user email
        String userEmail = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        // Get user by email
        return this.repository.findByEmail(userEmail).orElseThrow();
    }
}
