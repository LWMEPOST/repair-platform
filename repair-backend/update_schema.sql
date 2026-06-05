USE repair_db;

ALTER TABLE `sys_user` ADD COLUMN `avatar` varchar(255) DEFAULT NULL COMMENT '头像';
