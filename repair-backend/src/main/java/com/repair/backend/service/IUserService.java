package com.repair.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.repair.backend.dto.LoginRequest;
import com.repair.backend.dto.LoginResponse;
import com.repair.backend.dto.RegisterRequest;
import com.repair.backend.entity.User;

public interface IUserService extends IService<User> {
    LoginResponse login(LoginRequest request);
    User getUserByToken(String token);
    boolean register(RegisterRequest request);
}
