package com.repair.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
@TableName("sys_order")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long userId;
    private Long workerId;
    private String serviceContent;
    private String address;
    private String contactPhone;
    private BigDecimal price;
    private String status; // CREATED, PAID, WAITING_GRAB, SERVICE_ING, FINISHED, RATED
    private LocalDateTime createTime;
    private LocalDateTime payTime;
    private LocalDateTime grabTime;
    private LocalDateTime finishTime;
    
    @Version
    private Integer version;
}
