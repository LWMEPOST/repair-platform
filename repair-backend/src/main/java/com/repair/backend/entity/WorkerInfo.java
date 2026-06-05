package com.repair.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("sys_worker_info")
public class WorkerInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String realName;
    private String idCard;
    private String idCardFront;
    private String idCardBack;
    private String authStatus; // UNAUTH, AUDITING, AUTHED, REJECTED
    private BigDecimal balance;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
