package com.foodapp.backend1.service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.foodapp.backend1.config.JwtProperties;
import com.foodapp.backend1.domain.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final JwtProperties properties;
    private final Key key;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
        this.key = Keys.hmacShaKeyFor(properties.getSecret().getBytes());
    }

    public String generateToken(String userId, String email, Role role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(properties.getExpiration())))
                .addClaims(Map.of(
                        "email", email,
                        "role", role == null ? Role.USER.name() : role.name()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public JwtPayload parseToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String roleValue = claims.get("role", String.class);
        Role role = roleValue == null ? Role.USER : Role.valueOf(roleValue);
        String email = claims.get("email", String.class);
        return new JwtPayload(claims.getSubject(), email, role);
    }

    public record JwtPayload(String userId, String email, Role role) { }
}
