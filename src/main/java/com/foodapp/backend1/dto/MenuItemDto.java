package com.foodapp.backend1.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MenuItemDto {
    private String id;
    private String vendorId;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean available;
}
