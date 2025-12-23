package com.foodapp.backend1.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.foodapp.backend1.domain.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserId(String userId);
}
