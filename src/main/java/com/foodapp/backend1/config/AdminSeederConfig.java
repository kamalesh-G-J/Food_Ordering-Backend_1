package com.foodapp.backend1.config;

import java.time.Instant;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.foodapp.backend1.domain.Role;
import com.foodapp.backend1.domain.User;
import com.foodapp.backend1.repository.UserRepository;

@Configuration
public class AdminSeederConfig {

    @Bean
    CommandLineRunner seedDefaultAdmin(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        return args -> {
            String email = "admin123@gmail.com";
            String password = "AdminPass123";
            if (!userRepository.existsByEmail(email.toLowerCase())) {
                User admin = User.builder()
                        .email(email.toLowerCase())
                        .fullName("Admin")
                        .phone("6381678783")
                        .passwordHash(encoder.encode(password))
                        .role(Role.ADMIN)
                        .createdAt(Instant.now())
                        .build();
                userRepository.save(admin);
                System.out.println("Default admin created with email: " + email);
            } else {
                System.out.println("Default admin already present: " + email);
            }
        };
    }
}