USE repair_db;

-- ----------------------------
-- 师傅信息表 (实名认证/余额)
-- ----------------------------
DROP TABLE IF EXISTS `sys_worker_info`;
CREATE TABLE `sys_worker_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '关联用户ID',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `id_card_front` varchar(255) DEFAULT NULL COMMENT '身份证正面',
  `id_card_back` varchar(255) DEFAULT NULL COMMENT '身份证背面',
  `auth_status` varchar(20) DEFAULT 'UNAUTH' COMMENT '认证状态: UNAUTH(未认证), AUDITING(审核中), AUTHED(已认证), REJECTED(已驳回)',
  `balance` decimal(10,2) DEFAULT '0.00' COMMENT '账户余额',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='师傅信息表';

-- ----------------------------
-- 师傅技能表
-- ----------------------------
DROP TABLE IF EXISTS `sys_worker_skill`;
CREATE TABLE `sys_worker_skill` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '关联用户ID',
  `skill_name` varchar(50) NOT NULL COMMENT '技能名称',
  `certificate_img` varchar(255) DEFAULT NULL COMMENT '证书图片',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='师傅技能表';
