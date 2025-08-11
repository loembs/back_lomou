package ism.atelier.atelier.web.controllers;

import ism.atelier.atelier.data.models.User;
import ism.atelier.atelier.security.JwtUtils;
import ism.atelier.atelier.services.UserService;
import ism.atelier.atelier.web.dto.request.LoginRequestDto;
import ism.atelier.atelier.web.dto.request.RegisterRequestDto;
import ism.atelier.atelier.web.dto.response.AuthResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
        "https://loumo-frontend.vercel.app",
        "http://localhost:5173",
        "http://localhost:3000",
        "http://localhost:8080"
}, allowedHeaders = "*", methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE,
        RequestMethod.OPTIONS,
        RequestMethod.HEAD,
        RequestMethod.PATCH
})
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth endpoint is working!");
    }

    @PostMapping("/test-login")
    public ResponseEntity<?> testLogin(@RequestBody LoginRequestDto request) {
        try {
            System.out.println("=== TEST LOGIN REQUEST ===");
            System.out.println("Email: " + request.getEmail());
            System.out.println("Password provided: " + (request.getPassword() != null && !request.getPassword().isEmpty()));

            // Vérifier si l'utilisateur existe
            boolean userExists = userService.existsByEmail(request.getEmail());
            System.out.println("User exists: " + userExists);

            if (userExists) {
                User user = userService.findByEmail(request.getEmail());
                System.out.println("User found: " + user.getEmail() + ", Role: " + user.getRole());
                return ResponseEntity.ok("{\"message\": \"Utilisateur trouvé\", \"email\": \"" + user.getEmail() + "\", \"role\": \"" + user.getRole() + "\"}");
            } else {
                return ResponseEntity.badRequest().body("{\"message\": \"Utilisateur non trouvé\"}");
            }
        } catch (Exception e) {
            System.err.println("Test login error: " + e.getMessage());
            return ResponseEntity.badRequest().body("{\"message\": \"Erreur: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/test-password")
    public ResponseEntity<?> testPassword(@RequestBody LoginRequestDto request) {
        try {
            System.out.println("=== TEST PASSWORD REQUEST ===");
            System.out.println("Email: " + request.getEmail());
            System.out.println("Password: " + request.getPassword());

            // Vérifier si l'utilisateur existe
            boolean userExists = userService.existsByEmail(request.getEmail());
            System.out.println("User exists: " + userExists);

            if (userExists) {
                User user = userService.findByEmail(request.getEmail());
                System.out.println("User found: " + user.getEmail() + ", Role: " + user.getRole());

                // Tenter l'authentification
                try {
                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
                    );
                    System.out.println("✅ Authentification réussie!");
                    return ResponseEntity.ok("{\"message\": \"Authentification réussie\", \"email\": \"" + user.getEmail() + "\", \"role\": \"" + user.getRole() + "\"}");
                } catch (Exception authError) {
                    System.out.println("❌ Échec de l'authentification: " + authError.getMessage());
                    return ResponseEntity.badRequest().body("{\"message\": \"Échec de l'authentification: " + authError.getMessage() + "\"}");
                }
            } else {
                return ResponseEntity.badRequest().body("{\"message\": \"Utilisateur non trouvé\"}");
            }
        } catch (Exception e) {
            System.err.println("Test password error: " + e.getMessage());
            return ResponseEntity.badRequest().body("{\"message\": \"Erreur: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/test-users")
    public ResponseEntity<String> testUsers() {
        try {
            long userCount = userService.countUsers();
            boolean adminExists = userService.existsByEmail("admin@loumo.com");
            boolean client1Exists = userService.existsByEmail("client1@example.com");
            boolean client2Exists = userService.existsByEmail("client2@example.com");

            String response = String.format(
                    "Total users: %d, Admin exists: %s, Client1 exists: %s, Client2 exists: %s",
                    userCount, adminExists, client1Exists, client2Exists
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok("Error checking users: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto request) {
        // Vérifier si l'email existe déjà
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        // Créer un nouvel utilisateur
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setRole(User.Role.CLIENT);

        User savedUser = userService.registerUser(user);

        // Générer le token
        String token = jwtUtils.generateToken(savedUser, savedUser.getRole().name());
        String refreshToken = jwtUtils.generateToken(savedUser);

        AuthResponseDto response = new AuthResponseDto(
                token, refreshToken, savedUser.getId(), savedUser.getEmail(),
                savedUser.getFirstName(), savedUser.getLastName(),
                savedUser.getRole().name(), savedUser.getAvatar()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        try {
            System.out.println("=== LOGIN REQUEST RECEIVED ===");
            System.out.println("Request body: " + request);
            System.out.println("Email: " + request.getEmail());
            System.out.println("Password length: " + (request.getPassword() != null ? request.getPassword().length() : "null"));

            // Validation des données d'entrée
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                System.out.println("❌ Email is null or empty");
                return ResponseEntity.badRequest().body("{\"message\": \"Email requis\"}");
            }

            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                System.out.println("❌ Password is null or empty");
                return ResponseEntity.badRequest().body("{\"message\": \"Mot de passe requis\"}");
            }

            // Vérifier si l'utilisateur existe
            boolean userExists = userService.existsByEmail(request.getEmail());
            System.out.println("User exists in database: " + userExists);

            if (!userExists) {
                System.out.println("❌ User not found in database: " + request.getEmail());
                return ResponseEntity.badRequest().body("{\"message\": \"Email ou mot de passe incorrect\"}");
            }

            System.out.println("🔐 Attempting authentication...");
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();

            System.out.println("User authenticated: " + user.getEmail());

            // Mettre à jour la dernière connexion
            userService.updateLastLogin(user.getId());

            // Générer les tokens
            String token = jwtUtils.generateToken(user, user.getRole().name());
            String refreshToken = jwtUtils.generateToken(user);

            AuthResponseDto response = new AuthResponseDto(
                    token, refreshToken, user.getId(), user.getEmail(),
                    user.getFirstName(), user.getLastName(),
                    user.getRole().name(), user.getAvatar()
            );

            System.out.println("Login successful for: " + user.getEmail());
            return ResponseEntity.ok(response);
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            System.err.println("❌ Bad credentials for user: " + request.getEmail());
            return ResponseEntity.badRequest().body("{\"message\": \"Email ou mot de passe incorrect\"}");
        } catch (org.springframework.security.core.userdetails.UsernameNotFoundException e) {
            System.err.println("❌ Username not found: " + request.getEmail());
            return ResponseEntity.badRequest().body("{\"message\": \"Email ou mot de passe incorrect\"}");
        } catch (Exception e) {
            System.err.println("❌ Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("{\"message\": \"Erreur lors de la connexion\"}");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String email = jwtUtils.extractUsername(token);
            User user = userService.findByEmail(email);

            if (jwtUtils.validateToken(token, user)) {
                String newToken = jwtUtils.generateToken(user, user.getRole().name());
                String newRefreshToken = jwtUtils.generateToken(user);

                AuthResponseDto response = new AuthResponseDto(
                        newToken, newRefreshToken, user.getId(), user.getEmail(),
                        user.getFirstName(), user.getLastName(),
                        user.getRole().name(), user.getAvatar()
                );

                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            // Token invalide
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponseDto> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String email = jwtUtils.extractUsername(token);
            User user = userService.findByEmail(email);

            if (jwtUtils.validateToken(token, user)) {
                AuthResponseDto response = new AuthResponseDto(
                        token, "", user.getId(), user.getEmail(),
                        user.getFirstName(), user.getLastName(),
                        user.getRole().name(), user.getAvatar()
                );

                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            // Token invalide
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/create-admin")
    public ResponseEntity<AuthResponseDto> createAdmin(@RequestBody RegisterRequestDto request) {
        try {
            // Vérifier si l'email existe déjà
            if (userService.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body(null);
            }

            // Créer un nouvel utilisateur admin
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setPhone(request.getPhone());
            user.setAddress(request.getAddress());
            user.setRole(User.Role.ADMIN);

            User savedUser = userService.registerUser(user);

            // Générer le token
            String token = jwtUtils.generateToken(savedUser, savedUser.getRole().name());
            String refreshToken = jwtUtils.generateToken(savedUser);

            AuthResponseDto response = new AuthResponseDto(
                    token, refreshToken, savedUser.getId(), savedUser.getEmail(),
                    savedUser.getFirstName(), savedUser.getLastName(),
                    savedUser.getRole().name(), savedUser.getAvatar()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/setup-test-users")
    public ResponseEntity<?> setupTestUsers() {
        try {
            System.out.println("=== SETUP TEST USERS ===");

            // Supprimer l'ancien admin s'il existe
            if (userService.existsByEmail("admin@loumo.com")) {
                System.out.println("Suppression de l'ancien admin...");
                // Note: Vous devrez ajouter une méthode deleteByEmail dans UserService
            }

            // Créer un nouvel admin
            User adminUser = new User();
            adminUser.setEmail("admin@loumo.com");
            adminUser.setPassword("admin123");
            adminUser.setFirstName("Admin");
            adminUser.setLastName("Loumo");
            adminUser.setRole(User.Role.ADMIN);

            User savedAdmin = userService.registerUser(adminUser);
            System.out.println("✅ Admin créé: " + savedAdmin.getEmail());

            // Créer un utilisateur client de test
            User clientUser = new User();
            clientUser.setEmail("test@example.com");
            clientUser.setPassword("password123");
            clientUser.setFirstName("Test");
            clientUser.setLastName("User");
            clientUser.setRole(User.Role.CLIENT);
            clientUser.setPhone("+221 77 123 45 67");
            clientUser.setAddress("Dakar, Sénégal");

            User savedClient = userService.registerUser(clientUser);
            System.out.println("✅ Client créé: " + savedClient.getEmail());

            return ResponseEntity.ok("{\"message\": \"Utilisateurs de test créés avec succès\", \"admin\": \"" + savedAdmin.getEmail() + "\", \"client\": \"" + savedClient.getEmail() + "\"}");

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la création des utilisateurs de test: " + e.getMessage());
            return ResponseEntity.badRequest().body("{\"message\": \"Erreur: " + e.getMessage() + "\"}");
        }
    }
}