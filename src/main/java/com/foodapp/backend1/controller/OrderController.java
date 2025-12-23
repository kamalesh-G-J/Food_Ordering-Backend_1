package com.foodapp.backend1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodapp.backend1.domain.Order;
import com.foodapp.backend1.dto.ApiResponse;
import com.foodapp.backend1.dto.OrderRequest;
import com.foodapp.backend1.dto.UpdatePaymentStatusRequest;
import com.foodapp.backend1.dto.UpdateOrderStatusRequest;
import com.foodapp.backend1.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
        public ResponseEntity<ApiResponse<List<Order>>> placeOrder(Authentication authentication,
            @Valid @RequestBody OrderRequest request) {
        String userId = (String) authentication.getPrincipal();
        List<Order> orders = orderService.placeOrder(userId, request);
        return ResponseEntity.ok(ApiResponse.<List<Order>>builder()
                .success(true)
            .data(orders)
            .message("Order placed")
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> listOrders(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        List<Order> orders = orderService.listOrders(userId);
        return ResponseEntity.ok(ApiResponse.<List<Order>>builder()
                .success(true)
                .data(orders)
                .message("Orders fetched")
                .build());
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<Order>> updateStatus(Authentication authentication,
            @PathVariable String orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        String userId = (String) authentication.getPrincipal();
        Order order = orderService.updateStatus(userId, orderId, request.getStatus());
        return ResponseEntity.ok(ApiResponse.<Order>builder()
                .success(true)
                .data(order)
                .message("Order status updated")
                .build());
    }

        @PatchMapping("/{orderId}/payment")
        public ResponseEntity<ApiResponse<Order>> updatePayment(Authentication authentication,
            @PathVariable String orderId,
            @Valid @RequestBody UpdatePaymentStatusRequest request) {
        String userId = (String) authentication.getPrincipal();
        Order order = orderService.updatePaymentStatus(userId, orderId, request.getPaymentStatus(), request.getTransactionRef());
        return ResponseEntity.ok(ApiResponse.<Order>builder()
            .success(true)
            .data(order)
            .message("Payment status updated")
            .build());
        }

            @PatchMapping("/{orderId}/cancel")
            public ResponseEntity<ApiResponse<Order>> cancel(Authentication authentication,
                @PathVariable String orderId) {
            String userId = (String) authentication.getPrincipal();
            Order order = orderService.cancelOrder(userId, orderId);
            return ResponseEntity.ok(ApiResponse.<Order>builder()
                .success(true)
                .data(order)
                .message("Order cancelled")
                .build());
            }

    @PostMapping("/{orderId}/assign-delivery")
    public ResponseEntity<ApiResponse<Order>> assignDelivery(Authentication authentication,
            @PathVariable String orderId) {
        String userId = (String) authentication.getPrincipal();
        Order order = orderService.assignDelivery(userId, orderId, null);
        return ResponseEntity.ok(ApiResponse.<Order>builder()
                .success(true)
                .data(order)
                .message("Delivery assigned")
                .build());
    }
}
