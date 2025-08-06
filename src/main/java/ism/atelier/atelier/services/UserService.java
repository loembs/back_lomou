package ism.atelier.atelier.services;

import ism.atelier.atelier.data.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User registerUser(User user);
    User findByEmail(String email);
    User createOAuthUser(String email, String firstName, String lastName, String provider, String oauthId);
    User updateLastLogin(Long userId);
    boolean existsByEmail(String email);

    long countUsers();
}