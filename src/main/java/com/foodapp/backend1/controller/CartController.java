package com.foodapp.backend1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodapp.backend1.domain.Cart;
import com.foodapp.backend1.dto.ApiResponse;
import com.foodapp.backend1.dto.CartUpdateRequest;
import com.foodapp.backend1.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
@Validated
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Cart>> getCart(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        Cart cart = cartService.getOrCreateCart(userId);
        return ResponseEntity.ok(ApiResponse.<Cart>builder()
                .success(true)
                .data(cart)
                .message("Cart fetched")
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Cart>> updateCart(Authentication authentication,
            @Valid @RequestBody CartUpdateRequest request) {
        String userId = (String) authentication.getPrincipal();
        Cart cart = cartService.updateCart(userId, request);
        return ResponseEntity.ok(ApiResponse.<Cart>builder()
                .success(true)
                .data(cart)
                .message("Cart updated")
                .build());
    }
}
