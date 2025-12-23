package com.foodapp.backend1.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.foodapp.backend1.domain.Notification;
import com.foodapp.backend1.domain.Order;
import com.foodapp.backend1.repository.NotificationRepository;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void notifyStatus(Order order, String message) {
        Notification n = Notification.builder()
                .orderId(order.getId())
                .type("ORDER_STATUS")
                .message(message)
                .createdAt(Instant.now())
                .build();
        notificationRepository.save(n);
    }
}
