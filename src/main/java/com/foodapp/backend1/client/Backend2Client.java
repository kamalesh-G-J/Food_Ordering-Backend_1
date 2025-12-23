package com.foodapp.backend1.client;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.foodapp.backend1.dto.LocationDto;
import com.foodapp.backend1.dto.LocationRequest;
import com.foodapp.backend1.dto.MenuItemDto;
import com.foodapp.backend1.dto.MenuItemRequest;
import com.foodapp.backend1.dto.VendorDto;
import com.foodapp.backend1.dto.VendorRequest;

@Component
public class Backend2Client {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public Backend2Client(RestTemplate restTemplate, @Value("${backend2.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<VendorDto> getVendors(String locationId) {
        String url = String.format("%s/api/vendors%s", baseUrl, locationId != null && !locationId.isBlank() ? "?locationId=" + locationId : "");
        try {
            ResponseEntity<ApiEnvelope<List<VendorDto>>> response = restTemplate
                    .exchange(url, HttpMethod.GET, null, ApiEnvelopeType.VENDOR_LIST);
            ApiEnvelope<List<VendorDto>> body = response.getBody();
            if (body == null || body.data == null) {
                return Collections.emptyList();
            }
            return body.data;
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    public List<MenuItemDto> getMenu(String vendorId) {
        String url = String.format("%s/api/vendors/%s/menu", baseUrl, vendorId);
        try {
            ResponseEntity<ApiEnvelope<List<MenuItemDto>>> response = restTemplate
                    .exchange(url, HttpMethod.GET, null, ApiEnvelopeType.MENU_LIST);
            ApiEnvelope<List<MenuItemDto>> body = response.getBody();
            if (body == null || body.data == null) {
                return Collections.emptyList();
            }
            return body.data;
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    public VendorDto createVendor(VendorRequest request) {
        String url = String.format("%s/api/vendors", baseUrl);
        ResponseEntity<ApiEnvelope<VendorDto>> response = restTemplate
                .exchange(url, HttpMethod.POST, jsonEntity(request), ApiEnvelopeType.VENDOR);
        return response.getBody() == null ? null : response.getBody().data;
    }

    public VendorDto updateVendor(String vendorId, VendorRequest request) {
        String url = String.format("%s/api/vendors/%s", baseUrl, vendorId);
        ResponseEntity<ApiEnvelope<VendorDto>> response = restTemplate
                .exchange(url, HttpMethod.PUT, jsonEntity(request), ApiEnvelopeType.VENDOR);
        return response.getBody() == null ? null : response.getBody().data;
    }

    public MenuItemDto createMenuItem(String vendorId, MenuItemRequest request) {
        String url = String.format("%s/api/vendors/%s/menu", baseUrl, vendorId);
        ResponseEntity<ApiEnvelope<MenuItemDto>> response = restTemplate
                .exchange(url, HttpMethod.POST, jsonEntity(request), ApiEnvelopeType.MENU_ITEM);
        return response.getBody() == null ? null : response.getBody().data;
    }

    public MenuItemDto updateMenuItem(String vendorId, String itemId, MenuItemRequest request) {
        String url = String.format("%s/api/vendors/%s/menu/%s", baseUrl, vendorId, itemId);
        ResponseEntity<ApiEnvelope<MenuItemDto>> response = restTemplate
                .exchange(url, HttpMethod.PUT, jsonEntity(request), ApiEnvelopeType.MENU_ITEM);
        return response.getBody() == null ? null : response.getBody().data;
    }

    public void deleteMenuItem(String vendorId, String itemId) {
        String url = String.format("%s/api/vendors/%s/menu/%s", baseUrl, vendorId, itemId);
        restTemplate.exchange(url, HttpMethod.DELETE, null, ApiEnvelopeType.VOID);
    }

    public List<LocationDto> getLocations() {
        String url = String.format("%s/api/locations", baseUrl);
        try {
            ResponseEntity<ApiEnvelope<List<LocationDto>>> response = restTemplate
                    .exchange(url, HttpMethod.GET, null, ApiEnvelopeType.LOCATION_LIST);
            ApiEnvelope<List<LocationDto>> body = response.getBody();
            if (body == null || body.data == null) {
                return Collections.emptyList();
            }
            return body.data;
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    public LocationDto createLocation(LocationRequest request) {
        String url = String.format("%s/api/locations", baseUrl);
        ResponseEntity<ApiEnvelope<LocationDto>> response = restTemplate
                .exchange(url, HttpMethod.POST, jsonEntity(request), ApiEnvelopeType.LOCATION);
        return response.getBody() == null ? null : response.getBody().data;
    }

    private <T> HttpEntity<T> jsonEntity(T body) {
        var headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    public record ApiEnvelope<T>(boolean success, T data, String message) { }

    public static final class ApiEnvelopeType {
        private ApiEnvelopeType() { }

        public static final ParameterizedTypeReference<ApiEnvelope<List<VendorDto>>> VENDOR_LIST =
                new ParameterizedTypeReference<>() {};
        public static final ParameterizedTypeReference<ApiEnvelope<List<MenuItemDto>>> MENU_LIST =
                new ParameterizedTypeReference<>() {};
        public static final ParameterizedTypeReference<ApiEnvelope<VendorDto>> VENDOR =
            new ParameterizedTypeReference<>() {};
        public static final ParameterizedTypeReference<ApiEnvelope<MenuItemDto>> MENU_ITEM =
            new ParameterizedTypeReference<>() {};
        public static final ParameterizedTypeReference<ApiEnvelope<Void>> VOID =
            new ParameterizedTypeReference<>() {};
        public static final ParameterizedTypeReference<ApiEnvelope<List<LocationDto>>> LOCATION_LIST =
            new ParameterizedTypeReference<>() {};
        public static final ParameterizedTypeReference<ApiEnvelope<LocationDto>> LOCATION =
            new ParameterizedTypeReference<>() {};
    }
}
