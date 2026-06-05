package com.repair.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.repair.backend.dto.LoginRequest;
import com.repair.backend.dto.LoginResponse;
import com.repair.backend.dto.RegisterRequest;
import com.repair.backend.entity.User;
import com.repair.backend.mapper.UserMapper;
import com.repair.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public LoginResponse login(LoginRequest request) {
        // 1. 查询用户
        User user = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername()));
        
        if (user == null) {
            throw new RuntimeException("账号不存在");
        }
        
        // 2. 校验密码
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        // 3. 校验角色
        if (request.getRole() != null && !request.getRole().equals(user.getRole())) {
            throw new RuntimeException("角色不匹配");
        }

        // 4. 生成 Token
        String token = UUID.randomUUID().toString().replace("-", "");
        
        // 5. 存入 Redis (24小时过期)
        redisTemplate.opsForValue().set("token:" + token, user, 24, TimeUnit.HOURS);

        // 6. 返回结果
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setId(user.getId());
        response.setUsername(user.getUsername()); // 这里返回账号(手机号)
        response.setRole(user.getRole());
        return response;
    }

    @Override
    public User getUserByToken(String token) {
        return (User) redisTemplate.opsForValue().get("token:" + token);
    }

    @Override
    public boolean register(RegisterRequest request) {
        // 1. 检查账号(手机号)是否存在
        long count = this.count(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername()));
        if (count > 0) {
            throw new RuntimeException("该手机号已注册");
        }

        // 2. 创建用户
        User user = new User();
        user.setUsername(request.getUsername()); // 手机号作为账号
        user.setName(request.getName());         // 用户名称/昵称
        user.setPassword(request.getPassword()); 
        user.setRole(request.getRole());
        user.setCreateTime(LocalDateTime.now());
        user.setDeleted(0);
        
        return this.save(user);
    }
}
