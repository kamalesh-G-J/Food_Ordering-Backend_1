package com.foodapp.backend1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LocationRequest {
    @NotBlank
    private String name;
    private boolean active = true;
}
