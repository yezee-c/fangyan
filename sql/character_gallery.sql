-- ----------------------------
-- 人物画廊数据库表
-- ----------------------------

-- 人物信息表
DROP TABLE IF EXISTS `sys_character_info`;
CREATE TABLE `sys_character_info` (
  `character_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '人物ID',
  `character_name` varchar(100) NOT NULL COMMENT '人物姓名',
  `character_title` varchar(200) DEFAULT NULL COMMENT '人物头衔/职位',
  `character_intro` text COMMENT '人物简介',
  `character_description` longtext COMMENT '详细介绍',
  `birth_date` date DEFAULT NULL COMMENT '出生日期',
  `avatar_file_id` bigint(20) DEFAULT NULL COMMENT '头像文件ID',
  `cover_file_id` bigint(20) DEFAULT NULL COMMENT '封面图片文件ID',
  `sort_order` int(4) DEFAULT '0' COMMENT '显示顺序',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`character_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人物信息表';

-- 人物文件关联表
DROP TABLE IF EXISTS `sys_character_file`;
CREATE TABLE `sys_character_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `character_id` bigint(20) NOT NULL COMMENT '人物ID',
  `file_id` bigint(20) NOT NULL COMMENT '文件ID',
  `file_type` varchar(20) NOT NULL COMMENT '文件类型（image图片/video视频/document文档）',
  `file_category` varchar(50) DEFAULT NULL COMMENT '文件分类（照片集/视频集/文档集等）',
  `sort_order` int(4) DEFAULT '0' COMMENT '显示顺序',
  `description` varchar(500) DEFAULT NULL COMMENT '文件描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_character_id` (`character_id`),
  KEY `idx_file_id` (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人物文件关联表';

-- 添加外键约束
ALTER TABLE `sys_character_file` 
  ADD CONSTRAINT `fk_character_file_character` 
  FOREIGN KEY (`character_id`) REFERENCES `sys_character_info` (`character_id`) ON DELETE CASCADE;

ALTER TABLE `sys_character_file` 
  ADD CONSTRAINT `fk_character_file_file` 
  FOREIGN KEY (`file_id`) REFERENCES `sys_file_info` (`file_id`) ON DELETE CASCADE;
