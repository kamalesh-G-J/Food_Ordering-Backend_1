package com.foodapp.backend1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodapp.backend1.client.Backend2Client;
import com.foodapp.backend1.dto.ApiResponse;
import com.foodapp.backend1.dto.MenuItemDto;
import com.foodapp.backend1.dto.MenuItemRequest;
import com.foodapp.backend1.dto.VendorDto;
import com.foodapp.backend1.dto.VendorRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/vendors")
@Validated
public class AdminVendorController {
    private final Backend2Client backend2Client;

    public AdminVendorController(Backend2Client backend2Client) {
        this.backend2Client = backend2Client;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<VendorDto>> createVendor(@Valid @RequestBody VendorRequest request) {
        VendorDto vendor = backend2Client.createVendor(request);
        return ResponseEntity.ok(ApiResponse.<VendorDto>builder()
                .success(true)
                .data(vendor)
                .message("Vendor created")
                .build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{vendorId}")
    public ResponseEntity<ApiResponse<VendorDto>> updateVendor(@PathVariable String vendorId,
            @Valid @RequestBody VendorRequest request) {
        VendorDto vendor = backend2Client.updateVendor(vendorId, request);
        return ResponseEntity.ok(ApiResponse.<VendorDto>builder()
                .success(true)
                .data(vendor)
                .message("Vendor updated")
                .build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{vendorId}/menu")
    public ResponseEntity<ApiResponse<MenuItemDto>> createMenuItem(@PathVariable String vendorId,
            @Valid @RequestBody MenuItemRequest request) {
        MenuItemDto item = backend2Client.createMenuItem(vendorId, request);
        return ResponseEntity.ok(ApiResponse.<MenuItemDto>builder()
                .success(true)
                .data(item)
                .message("Menu item created")
                .build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{vendorId}/menu/{itemId}")
    public ResponseEntity<ApiResponse<MenuItemDto>> updateMenuItem(@PathVariable String vendorId,
            @PathVariable String itemId,
            @Valid @RequestBody MenuItemRequest request) {
        MenuItemDto item = backend2Client.updateMenuItem(vendorId, itemId, request);
        return ResponseEntity.ok(ApiResponse.<MenuItemDto>builder()
                .success(true)
                .data(item)
                .message("Menu item updated")
                .build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{vendorId}/menu/{itemId}")
    public ResponseEntity<ApiResponse<Void>> deleteMenuItem(@PathVariable String vendorId,
            @PathVariable String itemId) {
        backend2Client.deleteMenuItem(vendorId, itemId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Menu item deleted")
                .data(null)
                .build());
    }
}