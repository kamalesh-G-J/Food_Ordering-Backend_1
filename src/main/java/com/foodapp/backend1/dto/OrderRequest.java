package com.foodapp.backend1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.foodapp.backend1.domain.PaymentMethod;
import lombok.Data;

@Data
public class OrderRequest {
    @NotBlank
    private String cartId;

    @NotBlank
    private String deliveryAddressId;

    @NotNull
    private PaymentMethod paymentMethod;

    private String transactionRef;
}
