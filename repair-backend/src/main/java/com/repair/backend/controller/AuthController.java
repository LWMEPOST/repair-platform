package com.repair.backend.controller;

import com.repair.backend.dto.LoginRequest;
import com.repair.backend.dto.LoginResponse;
import com.repair.backend.dto.RegisterRequest;
import com.repair.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
    
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        userService.register(request);
        return "注册成功";
    }

    // 简单的退出登录
    @PostMapping("/logout")
    public String logout(@RequestHeader(value = "Authorization", required = false) String token) {
        // 实际应删除 Redis 中的 token，这里简化
        return "退出成功";
    }
}
