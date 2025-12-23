package com.foodapp.backend1.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.foodapp.backend1.domain.Cart;

public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserId(String userId);
}
