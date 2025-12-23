package com.foodapp.backend1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodapp.backend1.client.Backend2Client;
import com.foodapp.backend1.dto.ApiResponse;
import com.foodapp.backend1.dto.LocationDto;
import com.foodapp.backend1.dto.LocationRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@Validated
public class LocationGatewayController {
    private final Backend2Client backend2Client;

    public LocationGatewayController(Backend2Client backend2Client) {
        this.backend2Client = backend2Client;
    }

    @GetMapping("/locations")
    public ResponseEntity<ApiResponse<List<LocationDto>>> list() {
        List<LocationDto> locations = backend2Client.getLocations();
        return ResponseEntity.ok(ApiResponse.<List<LocationDto>>builder()
                .success(true)
                .data(locations)
                .message("Locations fetched")
                .build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/locations")
    public ResponseEntity<ApiResponse<LocationDto>> create(@Valid @RequestBody LocationRequest request) {
        LocationDto location = backend2Client.createLocation(request);
        return ResponseEntity.ok(ApiResponse.<LocationDto>builder()
                .success(true)
                .data(location)
                .message("Location created")
                .build());
    }
}
