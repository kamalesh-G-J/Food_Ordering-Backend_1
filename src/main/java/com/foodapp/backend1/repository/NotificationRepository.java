package com.foodapp.backend1.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.foodapp.backend1.domain.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
