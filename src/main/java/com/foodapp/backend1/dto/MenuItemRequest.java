package com.foodapp.backend1.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenuItemRequest {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal price;
    private boolean available = true;
}