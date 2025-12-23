package com.foodapp.backend1.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private String itemId;
    private String name;
    private int quantity;
    private BigDecimal price;
    private String vendorId;
}
