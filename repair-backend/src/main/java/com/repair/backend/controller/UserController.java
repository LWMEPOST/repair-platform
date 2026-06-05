package com.repair.backend.controller;

import com.repair.backend.entity.User;
import com.repair.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

    // 管理端分页查询
    @GetMapping("/list")
    public Page<User> listUsers(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer size,
                                @RequestParam(required = false) String username) {
        return userService.page(new Page<>(page, size), new LambdaQueryWrapper<User>()
                .like(StringUtils.isNotBlank(username), User::getUsername, username)
                .orderByDesc(User::getCreateTime));
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PutMapping
    public boolean update(@RequestBody User user) {
        User exist = userService.getById(user.getId());
        if (exist != null) {
            if (user.getName() != null) exist.setName(user.getName());
            if (user.getAvatar() != null) exist.setAvatar(user.getAvatar());
            // 允许修改手机号(username)，实际应校验唯一性，这里简化
            if (user.getUsername() != null) exist.setUsername(user.getUsername());
            
            return userService.updateById(exist);
        }
        return false;
    }
}
