package com.foodapp.backend1.dto;

import java.util.List;

import lombok.Data;

@Data
public class VendorDto {
    private String id;
    private String name;
    private String description;
    private String address;
    private String locationId;
    private String locationName;
    private boolean active;
    private List<String> cuisines;
    private List<String> tags;
}
