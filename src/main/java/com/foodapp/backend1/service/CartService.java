package com.foodapp.backend1.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.foodapp.backend1.domain.Cart;
import com.foodapp.backend1.domain.CartItem;
import com.foodapp.backend1.dto.CartUpdateRequest;
import com.foodapp.backend1.repository.CartRepository;

@Service
@Validated
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart getOrCreateCart(String userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(Cart.builder()
                        .userId(userId)
                        .updatedAt(Instant.now())
                        .build()));
    }

    public Cart updateCart(String userId, CartUpdateRequest request) {
        Cart cart = getOrCreateCart(userId);
        List<CartItem> items = request.getItems() == null ? List.of() : request.getItems().stream()
            .map(item -> CartItem.builder()
                .itemId(item.getItemId())
                .name(item.getName())
                .vendorId(item.getVendorId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build())
            .collect(Collectors.toList());
        cart.setItems(items);
        cart.setUpdatedAt(Instant.now());
        return cartRepository.save(cart);
    }
}
