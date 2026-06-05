package com.repair.backend.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private Long id;
    private String username;
    private String role;
}
