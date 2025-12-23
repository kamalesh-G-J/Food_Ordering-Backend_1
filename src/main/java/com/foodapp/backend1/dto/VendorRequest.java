package com.foodapp.backend1.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VendorRequest {
    @NotBlank
    private String name;
    private String description;
    private String address;
    private String locationId;
    private boolean active = true;
    private List<String> cuisines;
    private List<String> tags;
}