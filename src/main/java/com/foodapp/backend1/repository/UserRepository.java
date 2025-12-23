package com.foodapp.backend1.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.foodapp.backend1.domain.Role;
import com.foodapp.backend1.domain.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByRole(Role role);
}
