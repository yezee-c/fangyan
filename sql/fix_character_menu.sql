-- ====================================
-- 修复人物画廊菜单路由问题
-- 执行步骤：
-- 1. 先删除旧数据
-- 2. 重新插入正确配置
-- 3. 退出登录重新登录
-- ====================================

-- 步骤1: 删除旧菜单数据（如果存在）
DELETE FROM sys_menu WHERE menu_id BETWEEN 3100 AND 3107;

-- 步骤2: 重新插入菜单配置

-- 人物画廊菜单（顶级目录）
INSERT INTO sys_menu VALUES(3100, '人物画廊', 0, 9, 'character', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'peoples', 'admin', sysdate(), '', NULL, '人物画廊目录');

-- 人物管理菜单（列表页）
INSERT INTO sys_menu VALUES(3101, '人物管理', 3100, 1, 'info', 'system/character/info', NULL, '', 1, 0, 'C', '0', '0', 'system:character:list', 'user', 'admin', sysdate(), '', NULL, '人物管理菜单');

-- 人物编辑页面（隐藏菜单，关键！）
INSERT INTO sys_menu VALUES(3107, '人物编辑', 3100, 2, 'edit', 'system/character/edit', NULL, '', 1, 1, 'C', '0', '0', 'system:character:edit', 'edit', 'admin', sysdate(), '', NULL, '人物编辑页面');

-- 人物管理按钮权限
INSERT INTO sys_menu VALUES(3102, '人物查询', 3101, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:character:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(3103, '人物新增', 3101, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:character:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(3104, '人物修改', 3101, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:character:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(3105, '人物删除', 3101, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:character:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(3106, '人物导出', 3101, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'system:character:export', '#', 'admin', sysdate(), '', NULL, '');

-- 步骤3: 验证菜单是否插入成功
SELECT menu_id, menu_name, path, component, visible, menu_type 
FROM sys_menu 
WHERE menu_id BETWEEN 3100 AND 3107 
ORDER BY menu_id;

-- ====================================
-- 重要说明：
-- menu_id=3107 是编辑页面的路由配置
-- visible=1 表示隐藏（不在侧边栏显示）
-- component='system/character/edit' 对应文件：
--   views/system/character/edit/index.vue
-- ====================================
