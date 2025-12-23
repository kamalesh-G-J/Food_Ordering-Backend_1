package com.foodapp.backend1.dto;

import lombok.Builder;
import lombok.Data;

import com.foodapp.backend1.domain.Role;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String userId;
    private String email;
    private String fullName;
    private Role role;
}
