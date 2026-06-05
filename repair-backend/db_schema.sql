CREATE DATABASE IF NOT EXISTS repair_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE repair_db;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像URL',
    role VARCHAR(20) NOT NULL COMMENT '角色(USER:用户, WORKER:师傅, ADMIN:管理员)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除(0:正常, 1:删除)',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) COMMENT '系统用户表';

-- 师傅信息表
CREATE TABLE IF NOT EXISTS sys_worker_info (
    id BIGINT AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '关联用户ID',
    real_name VARCHAR(50) COMMENT '真实姓名',
    id_card VARCHAR(20) COMMENT '身份证号',
    skill_tags VARCHAR(255) COMMENT '技能标签(逗号分隔)',
    audit_status VARCHAR(20) DEFAULT 'PENDING' COMMENT '审核状态(PENDING:待审核, PASSED:通过, REJECTED:拒绝)',
    service_area VARCHAR(100) COMMENT '服务区域',
    score DECIMAL(2,1) DEFAULT 5.0 COMMENT '评分',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_id (user_id)
) COMMENT '师傅详细信息表';

-- 订单表
CREATE TABLE IF NOT EXISTS sys_order (
    id BIGINT AUTO_INCREMENT COMMENT '主键ID',
    order_no VARCHAR(64) NOT NULL COMMENT '订单编号',
    user_id BIGINT NOT NULL COMMENT '下单用户ID',
    worker_id BIGINT COMMENT '接单师傅ID',
    service_content VARCHAR(500) NOT NULL COMMENT '服务内容描述',
    address VARCHAR(255) NOT NULL COMMENT '服务地址',
    contact_phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    price DECIMAL(10,2) COMMENT '订单金额',
    status VARCHAR(20) NOT NULL DEFAULT 'CREATED' COMMENT '订单状态(CREATED:已创建, PAID:已支付, WAITING_GRAB:待抢单, SERVICE_ING:服务中, FINISHED:已完工, RATED:已评价, CANCELLED:已取消)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    pay_time DATETIME COMMENT '支付时间',
    grab_time DATETIME COMMENT '抢单时间',
    finish_time DATETIME COMMENT '完工时间',
    version INT DEFAULT 1 COMMENT '乐观锁版本号',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_user_id (user_id),
    KEY idx_worker_id (worker_id),
    KEY idx_status (status)
) COMMENT '订单表';

-- 订单日志表
CREATE TABLE IF NOT EXISTS sys_order_log (
    id BIGINT AUTO_INCREMENT COMMENT '主键ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    old_status VARCHAR(20) COMMENT '原状态',
    new_status VARCHAR(20) NOT NULL COMMENT '新状态',
    operator_id BIGINT COMMENT '操作人ID',
    operate_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    remark VARCHAR(255) COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_order_id (order_id)
) COMMENT '订单操作日志表';

-- 评价表
CREATE TABLE IF NOT EXISTS sys_comment (
    id BIGINT AUTO_INCREMENT COMMENT '主键ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    user_id BIGINT NOT NULL COMMENT '评价人ID',
    worker_id BIGINT NOT NULL COMMENT '被评价师傅ID',
    score INT NOT NULL COMMENT '评分(1-5)',
    content VARCHAR(500) COMMENT '评价内容',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
    PRIMARY KEY (id),
    KEY idx_order_id (order_id),
    KEY idx_worker_id (worker_id)
) COMMENT '订单评价表';

-- 初始化数据
INSERT IGNORE INTO sys_user (id, username, password, role) VALUES (1, 'admin', '123456', 'ADMIN');
INSERT IGNORE INTO sys_user (id, username, password, role) VALUES (2, 'user1', '123456', 'USER');
INSERT IGNORE INTO sys_user (id, username, password, role) VALUES (3, 'worker1', '123456', 'WORKER');
INSERT IGNORE INTO sys_worker_info (user_id, real_name, audit_status) VALUES (3, '张师傅', 'PASSED');
