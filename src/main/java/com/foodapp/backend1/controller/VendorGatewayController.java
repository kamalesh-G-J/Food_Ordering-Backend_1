package com.foodapp.backend1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodapp.backend1.client.Backend2Client;
import com.foodapp.backend1.dto.ApiResponse;
import com.foodapp.backend1.dto.MenuItemDto;
import com.foodapp.backend1.dto.VendorDto;

@RestController
@RequestMapping("/api/vendors")
@Validated
public class VendorGatewayController {
    private final Backend2Client backend2Client;

    public VendorGatewayController(Backend2Client backend2Client) {
        this.backend2Client = backend2Client;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VendorDto>>> listVendors(@RequestParam(required = false) String locationId) {
        List<VendorDto> vendors = backend2Client.getVendors(locationId);
        return ResponseEntity.ok(ApiResponse.<List<VendorDto>>builder()
                .success(true)
                .data(vendors)
                .message("Vendors fetched")
                .build());
    }

    @GetMapping("/{vendorId}/menu")
    public ResponseEntity<ApiResponse<List<MenuItemDto>>> listMenu(@PathVariable String vendorId) {
        List<MenuItemDto> items = backend2Client.getMenu(vendorId);
        return ResponseEntity.ok(ApiResponse.<List<MenuItemDto>>builder()
                .success(true)
                .data(items)
                .message("Menu fetched")
                .build());
    }
}
