package com.repair.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username; // 账号（手机号）
    private String name;     // 姓名/昵称
    private String password;
    private String role; // USER, WORKER, ADMIN
    private String avatar;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;
}
