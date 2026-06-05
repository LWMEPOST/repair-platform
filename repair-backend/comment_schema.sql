USE repair_db;

DROP TABLE IF EXISTS `sys_comment`;
CREATE TABLE `sys_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `user_id` bigint NOT NULL COMMENT '评价用户ID',
  `worker_id` bigint NOT NULL COMMENT '被评价师傅ID',
  `score` int NOT NULL DEFAULT 5 COMMENT '评分(1-5)',
  `content` varchar(500) DEFAULT NULL COMMENT '评价内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';
