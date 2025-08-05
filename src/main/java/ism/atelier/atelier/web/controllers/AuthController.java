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
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "https://loumo-frontend.vercel.app")
                .header("Access-Control-Allow-Credentials", "true")
                .body("Auth endpoint is working!");
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
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        try {
            System.out.println("Login attempt for email: " + request.getEmail());

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
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
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
}