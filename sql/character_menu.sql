-- ----------------------------
-- 人物画廊菜单权限配置
-- 注意：若已执行过此SQL，请先删除旧数据再执行
-- DELETE FROM sys_menu WHERE menu_id BETWEEN 3100 AND 3107;
-- ----------------------------

-- 人物画廊菜单（顶级菜单）
INSERT INTO sys_menu VALUES(3100, '人物画廊', 0, 9, 'character', NULL, '', '', 1, 0, 'M', '0', '0', '', 'peoples', 'admin', sysdate(), '', NULL, '人物画廊目录');

-- 人物管理菜单（列表页）
INSERT INTO sys_menu VALUES(3101, '人物管理', 3100, 1, 'info', 'system/character/info', '', '', 1, 0, 'C', '0', '0', 'system:character:list', 'user', 'admin', sysdate(), '', NULL, '人物管理菜单');

-- 人物编辑页面（隐藏菜单，不在侧边栏显示）
INSERT INTO sys_menu VALUES(3107, '人物编辑', 3100, 2, 'edit', 'system/character/edit', NULL, '', 1, 1, 'C', '0', '0', 'system:character:edit', 'edit', 'admin', sysdate(), '', NULL, '人物编辑页面');

-- 人物管理按钮权限
INSERT INTO sys_menu VALUES(3102, '人物查询', 3101, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:character:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(3103, '人物新增', 3101, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:character:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(3104, '人物修改', 3101, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:character:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(3105, '人物删除', 3101, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:character:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(3106, '人物导出', 3101, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:character:export', '#', 'admin', sysdate(), '', NULL, '');
