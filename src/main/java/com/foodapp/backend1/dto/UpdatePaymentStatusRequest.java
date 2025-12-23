package com.foodapp.backend1.dto;

import com.foodapp.backend1.domain.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePaymentStatusRequest {
    @NotNull
    private PaymentStatus paymentStatus;

    private String transactionRef;
}
