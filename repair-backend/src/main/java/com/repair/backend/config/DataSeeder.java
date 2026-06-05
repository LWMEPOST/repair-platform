package com.repair.backend.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.repair.backend.entity.User;
import com.repair.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private IUserService userService;

    @Override
    public void run(String... args) throws Exception {
        createUserIfNotExists("admin", "管理员", "123456", "ADMIN");
        createUserIfNotExists("13800138000", "张三", "123456", "USER");
        createUserIfNotExists("13900139000", "李师傅", "123456", "WORKER");
    }

    private void createUserIfNotExists(String username, String name, String password, String role) {
        long count = userService.count(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (count == 0) {
            User user = new User();
            user.setUsername(username);
            user.setName(name);
            user.setPassword(password);
            user.setRole(role);
            user.setCreateTime(LocalDateTime.now());
            user.setDeleted(0);
            userService.save(user);
            System.out.println("Initialized user: " + username);
        }
    }
}
