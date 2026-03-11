-- ----------------------------
-- 普通话学习与测评平台菜单权限配置
-- ----------------------------

-- 一级菜单：普通话学习
INSERT INTO sys_menu VALUES(2000, '普通话学习', 0, 5, 'pth', NULL, '', '', 1, 0, 'M', '0', '0', '', 'education', 'admin', sysdate(), '', NULL, '普通话学习目录');

-- 二级菜单：学生功能
INSERT INTO sys_menu VALUES(2001, '方言识别', 2000, 1, 'dialect', 'pth/student/dialect/index', '', '', 1, 0, 'C', '0', '0', 'pth:dialect:view', 'mic', 'admin', sysdate(), '', NULL, '方言识别菜单');
INSERT INTO sys_menu VALUES(2002, '普通话测评', 2000, 2, 'assessment', 'pth/student/assessment/index', '', '', 1, 0, 'C', '0', '0', 'pth:assessment:view', 'edit', 'admin', sysdate(), '', NULL, '普通话测评菜单');
INSERT INTO sys_menu VALUES(2003, '小游戏', 2000, 3, 'game', 'pth/student/game/index', '', '', 1, 0, 'C', '0', '0', 'pth:game:view', 'trophy', 'admin', sysdate(), '', NULL, '普通话小游戏菜单');
INSERT INTO sys_menu VALUES(2004, '语音合成', 2000, 4, 'synthesis', 'pth/student/synthesis/index', '', '', 1, 0, 'C', '0', '0', 'pth:synthesis:view', 'component', 'admin', sysdate(), '', NULL, '语音合成菜单');
INSERT INTO sys_menu VALUES(2005, '我的班级', 2000, 5, 'myclass', 'pth/student/class/index', '', '', 1, 0, 'C', '0', '0', 'pth:class:student', 'peoples', 'admin', sysdate(), '', NULL, '学生班级菜单');

-- 一级菜单：普通话教学
INSERT INTO sys_menu VALUES(2010, '普通话教学', 0, 6, 'teaching', NULL, '', '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', sysdate(), '', NULL, '普通话教学目录');

-- 二级菜单：教师功能
INSERT INTO sys_menu VALUES(2011, '班级管理', 2010, 1, 'class', 'pth/teacher/class/index', '', '', 1, 0, 'C', '0', '0', 'pth:class:list', 'tree', 'admin', sysdate(), '', NULL, '班级管理菜单');
INSERT INTO sys_menu VALUES(2012, '任务管理', 2010, 2, 'task', 'pth/teacher/task/index', '', '', 1, 0, 'C', '0', '0', 'pth:task:list', 'skill', 'admin', sysdate(), '', NULL, '任务管理菜单');
INSERT INTO sys_menu VALUES(2013, '数据统计', 2010, 3, 'statistics', 'pth/teacher/statistics/index', '', '', 1, 0, 'C', '0', '0', 'pth:statistics:view', 'chart', 'admin', sysdate(), '', NULL, '数据统计菜单');

-- 在系统管理下添加名人名言管理
INSERT INTO sys_menu VALUES(2020, '名人名言', 1, 10, 'quote', 'pth/admin/quote/index', '', '', 1, 0, 'C', '0', '0', 'pth:quote:list', 'documentation', 'admin', sysdate(), '', NULL, '名人名言管理菜单');

-- 班级管理按钮权限
INSERT INTO sys_menu VALUES(2021, '班级查询', 2011, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'pth:class:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(2022, '班级新增', 2011, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'pth:class:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(2023, '班级修改', 2011, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'pth:class:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(2024, '班级删除', 2011, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'pth:class:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(2025, '班级导出', 2011, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'pth:class:export', '#', 'admin', sysdate(), '', NULL, '');

-- 任务管理按钮权限
INSERT INTO sys_menu VALUES(2031, '任务查询', 2012, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'pth:task:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(2032, '任务新增', 2012, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'pth:task:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(2033, '任务修改', 2012, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'pth:task:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(2034, '任务删除', 2012, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'pth:task:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(2035, '任务批改', 2012, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'pth:task:review', '#', 'admin', sysdate(), '', NULL, '');

-- 名人名言管理按钮权限
INSERT INTO sys_menu VALUES(2041, '名言查询', 2020, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'pth:quote:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(2042, '名言新增', 2020, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'pth:quote:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(2043, '名言修改', 2020, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'pth:quote:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(2044, '名言删除', 2020, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'pth:quote:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(2045, '名言导出', 2020, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'pth:quote:export', '#', 'admin', sysdate(), '', NULL, '');

-- 创建学生角色
INSERT INTO sys_role VALUES(100, '学生', 'student', 3, 2, 1, 1, '0', '0', 'admin', sysdate(), '', NULL, '普通话学习学生角色');

-- 数字馆菜单
INSERT INTO sys_menu VALUES(2050, '数字馆', 0, 7, 'digital', NULL, '', '', 1, 0, 'M', '0', '0', '', 'guide', 'admin', sysdate(), '', NULL, '数字馆目录');

-- 二级菜单：数字馆功能
INSERT INTO sys_menu VALUES(2051, '数字资源', 2050, 1, 'resource', 'pth/digital/resource/index', '', '', 1, 0, 'C', '0', '0', 'pth:digital:view', 'documentation', 'admin', sysdate(), '', NULL, '数字资源菜单');

-- 数字馆管理按钮权限
INSERT INTO sys_menu VALUES(2052, '资源查询', 2051, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'pth:digital:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(2053, '资源新增', 2051, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'pth:digital:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(2054, '资源修改', 2051, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'pth:digital:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(2055, '资源删除', 2051, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'pth:digital:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES(2056, '资源导出', 2051, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'pth:digital:export', '#', 'admin', sysdate(), '', NULL, '');

-- 创建教师角色
INSERT INTO sys_role VALUES(101, '教师', 'teacher', 4, 2, 1, 1, '0', '0', 'admin', sysdate(), '', NULL, '普通话教学教师角色');

-- 学生角色菜单权限
INSERT INTO sys_role_menu VALUES(100, 2000); -- 普通话学习
INSERT INTO sys_role_menu VALUES(100, 2001); -- 方言识别
INSERT INTO sys_role_menu VALUES(100, 2002); -- 普通话测评
INSERT INTO sys_role_menu VALUES(100, 2003); -- 小游戏
INSERT INTO sys_role_menu VALUES(100, 2004); -- 语音合成
INSERT INTO sys_role_menu VALUES(100, 2005); -- 我的班级
INSERT INTO sys_role_menu VALUES(100, 2050); -- 数字馆
INSERT INTO sys_role_menu VALUES(100, 2051); -- 数字资源
INSERT INTO sys_role_menu VALUES(100, 2052); -- 资源查询
INSERT INTO sys_role_menu VALUES(100, 2053); -- 资源新增
INSERT INTO sys_role_menu VALUES(100, 2054); -- 资源修改
INSERT INTO sys_role_menu VALUES(100, 2055); -- 资源删除
INSERT INTO sys_role_menu VALUES(100, 2056); -- 资源导出

-- 教师角色菜单权限
INSERT INTO sys_role_menu VALUES(101, 2010); -- 普通话教学
INSERT INTO sys_role_menu VALUES(101, 2011); -- 班级管理
INSERT INTO sys_role_menu VALUES(101, 2012); -- 任务管理
INSERT INTO sys_role_menu VALUES(101, 2013); -- 数据统计
INSERT INTO sys_role_menu VALUES(101, 2021); -- 班级查询
INSERT INTO sys_role_menu VALUES(101, 2022); -- 班级新增
INSERT INTO sys_role_menu VALUES(101, 2023); -- 班级修改
INSERT INTO sys_role_menu VALUES(101, 2024); -- 班级删除
INSERT INTO sys_role_menu VALUES(101, 2025); -- 班级导出
INSERT INTO sys_role_menu VALUES(101, 2031); -- 任务查询
INSERT INTO sys_role_menu VALUES(101, 2032); -- 任务新增
INSERT INTO sys_role_menu VALUES(101, 2033); -- 任务修改
INSERT INTO sys_role_menu VALUES(101, 2034); -- 任务删除
INSERT INTO sys_role_menu VALUES(101, 2035); -- 任务批改
