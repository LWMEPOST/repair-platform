package com.repair.backend.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username; // 账号（手机号）
    private String password;
    private String role; // 可选，用于校验角色
}
