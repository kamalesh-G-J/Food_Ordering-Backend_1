package com.foodapp.backend1.dto;

import jakarta.validation.constraints.NotNull;
import com.foodapp.backend1.domain.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    @NotNull
    private OrderStatus status;
}
