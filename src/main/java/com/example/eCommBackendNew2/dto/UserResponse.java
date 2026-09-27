package com.example.eCommBackendNew2.dto;

public record UserResponse(
        Long id,
        String email,
        String username,
        String role
//        String password
) {
}
