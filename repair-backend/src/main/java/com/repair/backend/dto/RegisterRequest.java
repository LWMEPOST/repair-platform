package com.repair.backend.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username; // 账号（手机号）
    private String name;     // 姓名/昵称
    private String password;
    private String role; // USER, WORKER
}
