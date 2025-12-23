package com.foodapp.backend1.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartUpdateRequest {
    private List<Item> items;

    @Data
    public static class Item {
        @NotBlank
        private String itemId;
        @NotBlank
        private String name;
        @NotBlank
        private String vendorId;
        @NotNull
        @Min(1)
        private Integer quantity;
        @NotNull
        private BigDecimal price;
    }
}
