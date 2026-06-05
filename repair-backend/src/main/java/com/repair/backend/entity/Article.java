package com.repair.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_article")
public class Article {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String category; // 维修指南, 家电科普
    private String coverImg;
    private String content;
    private Long authorId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;
}
