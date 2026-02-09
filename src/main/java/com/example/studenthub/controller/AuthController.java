package com.example.studenthub.controller;

import com.example.studenthub.entity.User;
import com.example.studenthub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        try {
            User user;
            if (request.getRoles() != null && !request.getRoles().isEmpty()) {
                user = userService.registerUserWithRoles(
                        request.getUsername(),
                        request.getPassword(),
                        request.getEmail(),
                        request.getRoles());
            } else {
                user = userService.registerUser(
                        request.getUsername(),
                        request.getPassword(),
                        request.getEmail());
            }

            return new ResponseEntity<>(Map.of(
                    "message", "User registered successfully",
                    "username", user.getUsername(),
                    "email", user.getEmail()), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));

            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "username", request.getUsername(),
                    "authenticated", authentication.isAuthenticated()));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(Map.of("error", "Invalid username or password"),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    // registration
    public static class RegisterRequest {
        private String username;
        private String password;
        private String email;
        private Set<String> roles;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Set<String> getRoles() {
            return roles;
        }

        public void setRoles(Set<String> roles) {
            this.roles = roles;
        }
    }

    // login
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
