-- ----------------------------
-- 文件管理相关表
-- ----------------------------

-- ----------------------------
-- 1、用户文件配置表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_file_config`;
CREATE TABLE `sys_user_file_config` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `max_file_size` bigint(20) DEFAULT 10485760 COMMENT '单个文件最大大小（字节），默认10MB',
  `max_storage` bigint(20) DEFAULT 1073741824 COMMENT '最大存储容量（字节），默认1GB',
  `used_storage` bigint(20) DEFAULT 0 COMMENT '已使用存储（字节）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户文件配置表';

-- ----------------------------
-- 2、文件信息表
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_info`;
CREATE TABLE `sys_file_info` (
  `file_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `user_id` bigint(20) NOT NULL COMMENT '上传用户ID',
  `file_name` varchar(200) NOT NULL COMMENT '文件名称',
  `original_name` varchar(200) NOT NULL COMMENT '原始文件名',
  `file_path` varchar(500) NOT NULL COMMENT '文件路径（MinIO中的对象名）',
  `file_size` bigint(20) NOT NULL COMMENT '文件大小（字节）',
  `file_type` varchar(100) DEFAULT NULL COMMENT '文件类型',
  `content_type` varchar(100) DEFAULT NULL COMMENT 'MIME类型',
  `file_ext` varchar(50) DEFAULT NULL COMMENT '文件扩展名',
  `bucket_name` varchar(100) NOT NULL COMMENT '存储桶名称',
  `is_public` char(1) DEFAULT '0' COMMENT '是否公共文件（0私有 1公共）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`file_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_public` (`is_public`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='文件信息表';

-- ----------------------------
-- 3、文件下载记录表
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_download_log`;
CREATE TABLE `sys_file_download_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `file_id` bigint(20) NOT NULL COMMENT '文件ID',
  `user_id` bigint(20) NOT NULL COMMENT '下载用户ID',
  `download_time` datetime DEFAULT NULL COMMENT '下载时间',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  PRIMARY KEY (`log_id`),
  KEY `idx_file_id` (`file_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_download_time` (`download_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='文件下载记录表';

-- ----------------------------
-- 4、初始化管理员用户文件配置（user_id=1为管理员）
-- ----------------------------
INSERT INTO `sys_user_file_config` VALUES (1, 104857600, 10737418240, 0, sysdate(), NULL);
