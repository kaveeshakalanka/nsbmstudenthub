package com.example.studenthub.service;

import com.example.studenthub.entity.Role;
import com.example.studenthub.entity.User;
import com.example.studenthub.repository.RoleRepository;
import com.example.studenthub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService {

        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final PasswordEncoder passwordEncoder;

        @Autowired
        public UserService(UserRepository userRepository, RoleRepository roleRepository,
                        PasswordEncoder passwordEncoder) {
                this.userRepository = userRepository;
                this.roleRepository = roleRepository;
                this.passwordEncoder = passwordEncoder;
        }

        // Register a new user with default USER role
        public User registerUser(String username, String password, String email) {
                if (userRepository.existsByUsername(username)) {
                        throw new RuntimeException("Username already exists: " + username);
                }
                if (userRepository.existsByEmail(email)) {
                        throw new RuntimeException("Email already exists: " + email);
                }

                User user = new User();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode(password)); // Encrypt password
                user.setEmail(email);

                // Assign default USER role
                Role userRole = roleRepository.findByName("ROLE_USER")
                                .orElseThrow(() -> new RuntimeException("Default role not found"));
                Set<Role> roles = new HashSet<>();
                roles.add(userRole);
                user.setRoles(roles);

                return userRepository.save(user);
        }

        // Register a new user with specific roles
        public User registerUserWithRoles(String username, String password, String email, Set<String> roleNames) {
                if (userRepository.existsByUsername(username)) {
                        throw new RuntimeException("Username already exists: " + username);
                }
                if (userRepository.existsByEmail(email)) {
                        throw new RuntimeException("Email already exists: " + email);
                }

                User user = new User();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode(password)); // Encrypt password
                user.setEmail(email);

                Set<Role> roles = new HashSet<>();
                for (String roleName : roleNames) {
                        Role role = roleRepository.findByName(roleName)
                                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                        roles.add(role);
                }
                user.setRoles(roles);

                return userRepository.save(user);
        }

        // Find user by username
        public Optional<User> findByUsername(String username) {
                return userRepository.findByUsername(username);
        }

        // Check if username exists
        public boolean existsByUsername(String username) {
                return userRepository.existsByUsername(username);
        }

        // Check if email exists
        public boolean existsByEmail(String email) {
                return userRepository.existsByEmail(email);
        }

        // Initialize default roles
        public void initializeRoles() {
                if (roleRepository.findByName("ROLE_USER").isEmpty()) {
                        Role userRole = new Role();
                        userRole.setName("ROLE_USER");
                        roleRepository.save(userRole);
                }
                if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
                        Role adminRole = new Role();
                        adminRole.setName("ROLE_ADMIN");
                        roleRepository.save(adminRole);
                }
        }
}
