USE repair_db;

-- ----------------------------
-- 文章表 (维修指南/科普)
-- ----------------------------
DROP TABLE IF EXISTS `sys_article`;
CREATE TABLE `sys_article` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(100) NOT NULL COMMENT '文章标题',
  `category` varchar(50) NOT NULL COMMENT '分类(维修指南/家电科普)',
  `cover_img` varchar(255) DEFAULT NULL COMMENT '封面图片URL',
  `content` text COMMENT '文章内容(HTML/Markdown)',
  `author_id` bigint DEFAULT NULL COMMENT '作者ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int DEFAULT '0' COMMENT '逻辑删除(0正常 1删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统文章表';

-- 插入一些测试数据
INSERT INTO `sys_article` (`title`, `category`, `cover_img`, `content`, `author_id`) VALUES 
('空调不制冷怎么办？', '维修指南', 'https://fastly.jsdelivr.net/npm/@vant/assets/apple-1.jpeg', '空调不制冷通常有以下几个原因：1. 缺氟；2. 过滤网脏堵；3. 压缩机故障...', 1),
('冰箱省电小妙招', '家电科普', 'https://fastly.jsdelivr.net/npm/@vant/assets/apple-2.jpeg', '冰箱摆放位置要通风，食物不要塞得太满...', 1),
('洗衣机清洗教程', '维修指南', 'https://fastly.jsdelivr.net/npm/@vant/assets/apple-3.jpeg', '定期使用洗衣机槽清洗剂...', 1);
