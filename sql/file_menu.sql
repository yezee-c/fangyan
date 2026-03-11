-- ----------------------------
-- 文件管理菜单权限配置
-- ----------------------------

-- 文件管理菜单
INSERT INTO sys_menu VALUES(3000, '文件管理', 0, 8, 'file', NULL, '', '', 1, 0, 'M', '0', '0', '', 'documentation', 'admin', sysdate(), '', NULL, '文件管理目录');

-- 我的文件菜单
INSERT INTO sys_menu VALUES(3001, '我的文件', 3000, 1, 'myfile', 'system/file/myfile', '', '', 1, 0, 'C', '0', '0', '', 'upload', 'admin', sysdate(), '', NULL, '我的文件菜单');

-- 公共文件菜单
INSERT INTO sys_menu VALUES(3002, '公共文件', 3000, 2, 'publicfile', 'system/file/publicfile', '', '', 1, 0, 'C', '0', '0', '', 'download', 'admin', sysdate(), '', NULL, '公共文件菜单');

-- 公共展示菜单（所有人可访问）
INSERT INTO sys_menu VALUES(3013, '公共展示', 3000, 3, 'gallery', 'system/file/gallery', '', '', 1, 0, 'C', '0', '0', '', 'eye-open', 'admin', sysdate(), '', NULL, '公共文件展示菜单');

-- 文件配置菜单（仅管理员可见）
INSERT INTO sys_menu VALUES(3003, '文件配置', 3000, 4, 'fileconfig', 'system/file/config', '', '', 1, 0, 'C', '0', '0', 'system:fileconfig:list', 'edit', 'admin', sysdate(), '', NULL, '文件配置菜单');

-- 文件配置按钮权限
INSERT INTO sys_menu VALUES(3004, '文件配置查询', 3003, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:fileconfig:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(3005, '文件配置新增', 3003, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:fileconfig:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(3006, '文件配置修改', 3003, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:fileconfig:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(3007, '文件配置删除', 3003, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:fileconfig:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(3008, '文件配置导出', 3003, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:fileconfig:export', '#', 'admin', sysdate(), '', NULL, '');

-- 文件管理按钮权限
INSERT INTO sys_menu VALUES(3009, '文件查询', 3001, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:file:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(3010, '文件列表', 3001, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:file:list', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(3011, '文件修改', 3001, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:file:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(3012, '文件导出', 3001, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:file:export', '#', 'admin', sysdate(), '', NULL, '');
