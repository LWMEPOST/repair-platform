package com.repair.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_worker_skill")
public class WorkerSkill {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String skillName;
    private String certificateImg;
    private LocalDateTime createTime;
}
