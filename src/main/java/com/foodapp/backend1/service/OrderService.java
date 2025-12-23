package com.foodapp.backend1.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.foodapp.backend1.domain.Cart;
import com.foodapp.backend1.domain.CartItem;
import com.foodapp.backend1.domain.Order;
import com.foodapp.backend1.domain.OrderStatus;
import com.foodapp.backend1.domain.PaymentMethod;
import com.foodapp.backend1.domain.PaymentStatus;
import com.foodapp.backend1.dto.OrderRequest;
import com.foodapp.backend1.repository.CartRepository;
import com.foodapp.backend1.repository.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final NotificationService notificationService;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.notificationService = notificationService;
    }

        public List<Order> placeOrder(String userId, OrderRequest request) {
        Cart cart = cartRepository.findById(request.getCartId())
            .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        if (!cart.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Cart does not belong to user");
        }
        List<CartItem> items = cart.getItems();
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }
        Map<String, List<CartItem>> grouped = items.stream()
            .collect(Collectors.groupingBy(CartItem::getVendorId, LinkedHashMap::new, Collectors.toList()));
        PaymentMethod method = request.getPaymentMethod();
        PaymentStatus paymentStatus = method == PaymentMethod.COD ? PaymentStatus.PENDING : PaymentStatus.PAID;
        Instant paidAt = paymentStatus == PaymentStatus.PAID ? Instant.now() : null;

        List<Order> created = new ArrayList<>();
        for (Map.Entry<String, List<CartItem>> entry : grouped.entrySet()) {
            List<CartItem> vendorItems = entry.getValue();
            BigDecimal total = vendorItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            Order order = Order.builder()
                .userId(userId)
                .items(vendorItems)
                .status(OrderStatus.CREATED)
                .total(total)
                .paymentMethod(method)
                .paymentStatus(paymentStatus)
                .transactionRef(request.getTransactionRef())
                .paidAt(paidAt)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .vendorId(entry.getKey())
                .build();
            Order saved = orderRepository.save(order);
            created.add(saved);
            notificationService.notifyStatus(saved, "Order created");
        }

        cart.setItems(new ArrayList<>());
        cart.setUpdatedAt(Instant.now());
        cartRepository.save(cart);
        return created;
        }

    public List<Order> listOrders(String userId) {
        return orderRepository.findByUserId(userId);
    }
    public Order updateStatus(String userId, String orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (!order.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Order does not belong to user");
        }
        order.setStatus(newStatus);
        order.setUpdatedAt(Instant.now());
        Order saved = orderRepository.save(order);
        notificationService.notifyStatus(saved, "Order status -> " + newStatus.name());
        return saved;
    }

    public Order updatePaymentStatus(String userId, String orderId, PaymentStatus paymentStatus, String transactionRef) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (!order.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Order does not belong to user");
        }
        order.setPaymentStatus(paymentStatus);
        order.setTransactionRef(transactionRef);
        if (paymentStatus == PaymentStatus.PAID && order.getPaidAt() == null) {
            order.setPaidAt(Instant.now());
        } else if (paymentStatus != PaymentStatus.PAID) {
            order.setPaidAt(null);
        }
        order.setUpdatedAt(Instant.now());
        Order saved = orderRepository.save(order);
        notificationService.notifyStatus(saved, "Payment status -> " + paymentStatus.name());
        return saved;
    }

    public Order cancelOrder(String userId, String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (!order.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Order does not belong to user");
        }
        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.DELIVERED) {
            return order;
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(Instant.now());
        Order saved = orderRepository.save(order);
        notificationService.notifyStatus(saved, "Order cancelled");
        return saved;
    }

    public Order assignDelivery(String userId, String orderId, String partnerName) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (!order.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Order does not belong to user");
        }
        order.setDeliveryPartner(partnerName == null ? "Mock Rider" : partnerName);
        order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
        order.setUpdatedAt(Instant.now());
        Order saved = orderRepository.save(order);
        notificationService.notifyStatus(saved, "Delivery assigned: " + order.getDeliveryPartner());
        return saved;
    }
}
