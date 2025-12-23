package com.foodapp.backend1.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.foodapp.backend1.domain.Role;
import com.foodapp.backend1.domain.User;
import com.foodapp.backend1.dto.AuthResponse;
import com.foodapp.backend1.dto.LoginRequest;
import com.foodapp.backend1.dto.SignupRequest;
import com.foodapp.backend1.repository.UserRepository;

@Service
@Validated
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        String email = request.getEmail().trim().toLowerCase();
        String password = request.getPassword().trim();

        Role role = request.getRole() == null ? Role.USER : request.getRole();
        if (role == Role.ADMIN && userRepository.existsByRole(Role.ADMIN)) {
            throw new IllegalArgumentException("Admin already exists");
        }

        User user = User.builder()
            .email(email)
            .passwordHash(passwordEncoder.encode(password))
                .fullName(request.getFullName())
                .phone(request.getPhone())
            .role(role)
                .createdAt(Instant.now())
                .build();
        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved.getId(), saved.getEmail(), saved.getRole());
        return AuthResponse.builder()
                .token(token)
                .userId(saved.getId())
                .email(saved.getEmail())
                .fullName(saved.getFullName())
            .role(saved.getRole())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        String password = request.getPassword().trim();

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty() || password.isEmpty() || !passwordEncoder.matches(password, user.get().getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        User u = user.get();
        Role role = u.getRole() == null ? Role.USER : u.getRole();
        u.setRole(role);
        String token = jwtService.generateToken(u.getId(), u.getEmail(), role);
        return AuthResponse.builder()
                .token(token)
                .userId(u.getId())
                .email(u.getEmail())
                .fullName(u.getFullName())
            .role(role)
                .build();
    }
}
