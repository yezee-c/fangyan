-- ----------------------------
-- 普通话学习与测评平台数据表
-- ----------------------------

-- ----------------------------
-- 1、普通话班级表
-- ----------------------------
DROP TABLE IF EXISTS pth_class;
CREATE TABLE pth_class (
  class_id          BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '班级ID',
  class_name        VARCHAR(100)    NOT NULL                   COMMENT '班级名称',
  grade             VARCHAR(50)     DEFAULT ''                 COMMENT '年级',
  semester          VARCHAR(50)     DEFAULT ''                 COMMENT '学期',
  invitation_code   VARCHAR(20)     NOT NULL                   COMMENT '邀请码',
  teacher_id        BIGINT(20)      NOT NULL                   COMMENT '教师ID',
  teacher_name      VARCHAR(30)     DEFAULT ''                 COMMENT '教师姓名',
  student_count     INT(11)         DEFAULT 0                  COMMENT '学生人数',
  status            CHAR(1)         DEFAULT '0'                COMMENT '状态（0正常 1停用）',
  del_flag          CHAR(1)         DEFAULT '0'                COMMENT '删除标志（0代表存在 2代表删除）',
  create_by         VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
  create_time       DATETIME                                   COMMENT '创建时间',
  update_by         VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
  update_time       DATETIME                                   COMMENT '更新时间',
  remark            VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
  PRIMARY KEY (class_id),
  UNIQUE KEY uk_invitation_code (invitation_code)
) ENGINE=INNODB AUTO_INCREMENT=1000 COMMENT = '普通话班级表';

-- ----------------------------
-- 2、班级学生关联表
-- ----------------------------
DROP TABLE IF EXISTS pth_class_student;
CREATE TABLE pth_class_student (
  id                BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '关联ID',
  class_id          BIGINT(20)      NOT NULL                   COMMENT '班级ID',
  student_id        BIGINT(20)      NOT NULL                   COMMENT '学生ID',
  student_name      VARCHAR(30)     DEFAULT ''                 COMMENT '学生姓名',
  join_time         DATETIME                                   COMMENT '加入时间',
  status            CHAR(1)         DEFAULT '0'                COMMENT '状态（0正常 1停用）',
  PRIMARY KEY (id),
  UNIQUE KEY uk_student_class (student_id),
  KEY idx_class_id (class_id)
) ENGINE=INNODB AUTO_INCREMENT=1000 COMMENT = '班级学生关联表';

-- ----------------------------
-- 3、普通话测评记录表
-- ----------------------------
DROP TABLE IF EXISTS pth_assessment_record;
CREATE TABLE pth_assessment_record (
  record_id         BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '记录ID',
  student_id        BIGINT(20)      NOT NULL                   COMMENT '学生ID',
  student_name      VARCHAR(30)     DEFAULT ''                 COMMENT '学生姓名',
  class_id          BIGINT(20)      DEFAULT NULL               COMMENT '班级ID',
  assessment_type   VARCHAR(20)     NOT NULL                   COMMENT '测评类型（single_word单字朗读 word词语朗读 article短文朗读 speech命题说话）',
  assessment_title  VARCHAR(200)    DEFAULT ''                 COMMENT '测评标题',
  content           TEXT                                       COMMENT '测评内容',
  audio_url         VARCHAR(500)    DEFAULT ''                 COMMENT '音频地址',
  total_score       DECIMAL(5,2)    DEFAULT 0.00               COMMENT '总分',
  pronunciation_score DECIMAL(5,2)  DEFAULT 0.00               COMMENT '语音标准度得分',
  grammar_score     DECIMAL(5,2)    DEFAULT 0.00               COMMENT '词汇语法得分',
  fluency_score     DECIMAL(5,2)    DEFAULT 0.00               COMMENT '流畅度得分',
  weak_points       VARCHAR(500)    DEFAULT ''                 COMMENT '薄弱环节（JSON格式）',
  suggestions       TEXT                                       COMMENT '提升建议',
  assessment_time   DATETIME                                   COMMENT '测评时间',
  create_time       DATETIME                                   COMMENT '创建时间',
  PRIMARY KEY (record_id),
  KEY idx_student_id (student_id),
  KEY idx_class_id (class_id),
  KEY idx_assessment_time (assessment_time)
) ENGINE=INNODB AUTO_INCREMENT=1000 COMMENT = '普通话测评记录表';

-- ----------------------------
-- 4、方言识别记录表
-- ----------------------------
DROP TABLE IF EXISTS pth_dialect_record;
CREATE TABLE pth_dialect_record (
  record_id         BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '记录ID',
  student_id        BIGINT(20)      NOT NULL                   COMMENT '学生ID',
  student_name      VARCHAR(30)     DEFAULT ''                 COMMENT '学生姓名',
  dialect_type      VARCHAR(50)     DEFAULT ''                 COMMENT '方言类型',
  audio_url         VARCHAR(500)    DEFAULT ''                 COMMENT '方言音频地址',
  dialect_text      TEXT                                       COMMENT '方言识别文字',
  mandarin_text     TEXT                                       COMMENT '普通话对照文字',
  mandarin_audio    VARCHAR(500)    DEFAULT ''                 COMMENT '普通话音频地址',
  create_time       DATETIME                                   COMMENT '创建时间',
  PRIMARY KEY (record_id),
  KEY idx_student_id (student_id),
  KEY idx_create_time (create_time)
) ENGINE=INNODB AUTO_INCREMENT=1000 COMMENT = '方言识别记录表';

-- ----------------------------
-- 5、任务表
-- ----------------------------
DROP TABLE IF EXISTS pth_task;
CREATE TABLE pth_task (
  task_id           BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '任务ID',
  task_name         VARCHAR(200)    NOT NULL                   COMMENT '任务名称',
  task_type         VARCHAR(20)     NOT NULL                   COMMENT '任务类型（assessment测评任务 dialect方言识别 game游戏任务 custom自定义）',
  task_desc         TEXT                                       COMMENT '任务描述',
  task_content      TEXT                                       COMMENT '任务内容（JSON格式）',
  teacher_id        BIGINT(20)      NOT NULL                   COMMENT '发布教师ID',
  teacher_name      VARCHAR(30)     DEFAULT ''                 COMMENT '教师姓名',
  class_ids         VARCHAR(500)    DEFAULT ''                 COMMENT '班级ID列表（逗号分隔）',
  deadline          DATETIME                                   COMMENT '截止时间',
  requirements      VARCHAR(500)    DEFAULT ''                 COMMENT '完成要求',
  scoring_criteria  TEXT                                       COMMENT '评分标准',
  total_students    INT(11)         DEFAULT 0                  COMMENT '总学生数',
  completed_students INT(11)        DEFAULT 0                  COMMENT '已完成学生数',
  status            CHAR(1)         DEFAULT '0'                COMMENT '状态（0进行中 1已结束 2已撤回）',
  del_flag          CHAR(1)         DEFAULT '0'                COMMENT '删除标志（0代表存在 2代表删除）',
  create_by         VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
  create_time       DATETIME                                   COMMENT '创建时间',
  update_by         VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
  update_time       DATETIME                                   COMMENT '更新时间',
  remark            VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
  PRIMARY KEY (task_id),
  KEY idx_teacher_id (teacher_id),
  KEY idx_create_time (create_time)
) ENGINE=INNODB AUTO_INCREMENT=1000 COMMENT = '任务表';

-- ----------------------------
-- 6、任务提交表
-- ----------------------------
DROP TABLE IF EXISTS pth_task_submit;
CREATE TABLE pth_task_submit (
  submit_id         BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '提交ID',
  task_id           BIGINT(20)      NOT NULL                   COMMENT '任务ID',
  task_name         VARCHAR(200)    DEFAULT ''                 COMMENT '任务名称',
  student_id        BIGINT(20)      NOT NULL                   COMMENT '学生ID',
  student_name      VARCHAR(30)     DEFAULT ''                 COMMENT '学生姓名',
  class_id          BIGINT(20)      DEFAULT NULL               COMMENT '班级ID',
  submit_content    TEXT                                       COMMENT '提交内容（JSON格式）',
  audio_url         VARCHAR(500)    DEFAULT ''                 COMMENT '音频地址',
  score             DECIMAL(5,2)    DEFAULT NULL               COMMENT '得分',
  review_status     CHAR(1)         DEFAULT '0'                COMMENT '批改状态（0待批改 1已批改）',
  review_comment    TEXT                                       COMMENT '批改评语',
  reviewer_id       BIGINT(20)      DEFAULT NULL               COMMENT '批改教师ID',
  reviewer_name     VARCHAR(30)     DEFAULT ''                 COMMENT '批改教师姓名',
  review_time       DATETIME                                   COMMENT '批改时间',
  submit_time       DATETIME                                   COMMENT '提交时间',
  create_time       DATETIME                                   COMMENT '创建时间',
  PRIMARY KEY (submit_id),
  KEY idx_task_id (task_id),
  KEY idx_student_id (student_id),
  KEY idx_submit_time (submit_time)
) ENGINE=INNODB AUTO_INCREMENT=1000 COMMENT = '任务提交表';

-- ----------------------------
-- 7、游戏积分表
-- ----------------------------
DROP TABLE IF EXISTS pth_game_score;
CREATE TABLE pth_game_score (
  id                BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT 'ID',
  student_id        BIGINT(20)      NOT NULL                   COMMENT '学生ID',
  student_name      VARCHAR(30)     DEFAULT ''                 COMMENT '学生姓名',
  class_id          BIGINT(20)      DEFAULT NULL               COMMENT '班级ID',
  game_type         VARCHAR(50)     NOT NULL                   COMMENT '游戏类型',
  total_score       INT(11)         DEFAULT 0                  COMMENT '总积分',
  level             INT(11)         DEFAULT 1                  COMMENT '等级',
  play_count        INT(11)         DEFAULT 0                  COMMENT '游戏次数',
  best_score        INT(11)         DEFAULT 0                  COMMENT '最高分',
  update_time       DATETIME                                   COMMENT '更新时间',
  create_time       DATETIME                                   COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_student_game (student_id, game_type),
  KEY idx_class_id (class_id),
  KEY idx_total_score (total_score)
) ENGINE=INNODB AUTO_INCREMENT=1000 COMMENT = '游戏积分表';

-- ----------------------------
-- 8、大别山名人名言表
-- ----------------------------
DROP TABLE IF EXISTS pth_celebrity_quote;
CREATE TABLE pth_celebrity_quote (
  quote_id          BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '名言ID',
  celebrity_name    VARCHAR(100)    NOT NULL                   COMMENT '名人姓名',
  photo_url         VARCHAR(500)    DEFAULT ''                 COMMENT '名人照片URL',
  quote_content     TEXT            NOT NULL                   COMMENT '名言内容',
  biography         TEXT                                       COMMENT '名人简介',
  achievements      TEXT                                       COMMENT '主要成就',
  connection        TEXT                                       COMMENT '与大别山的关联',
  display_order     INT(11)         DEFAULT 0                  COMMENT '显示顺序',
  status            CHAR(1)         DEFAULT '0'                COMMENT '状态（0正常 1停用）',
  del_flag          CHAR(1)         DEFAULT '0'                COMMENT '删除标志（0代表存在 2代表删除）',
  create_by         VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
  create_time       DATETIME                                   COMMENT '创建时间',
  update_by         VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
  update_time       DATETIME                                   COMMENT '更新时间',
  remark            VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
  PRIMARY KEY (quote_id),
  KEY idx_display_order (display_order)
) ENGINE=INNODB AUTO_INCREMENT=1000 COMMENT = '大别山名人名言表';

-- ----------------------------
-- 初始化-名人名言示例数据
-- ----------------------------
INSERT INTO pth_celebrity_quote VALUES
(1, '李先念', '', '革命战争时期的艰苦奋斗，为新中国的建立奠定了基础。', '李先念（1909-1992），湖北红安人，无产阶级革命家、政治家、军事家，曾任中华人民共和国主席。', '参加过长征，新中国成立后担任过多个重要职务，为国家建设作出巨大贡献。', '李先念出生于大别山区红安县，在大别山区开展革命斗争多年，是大别山革命根据地的重要领导人之一。', 1, '0', '0', 'admin', sysdate(), '', NULL, '示例数据'),
(2, '董必武', '', '学习要持之以恒，不可半途而废。', '董必武（1886-1975），湖北黄安（今红安）人，中国共产党创始人之一，法学家。', '参与创建中国共产党，新中国成立后长期从事政法工作，为社会主义法制建设做出重要贡献。', '董必武是大别山区红安县人，青年时期在大别山区接受革命思想教育，后走上革命道路。', 2, '0', '0', 'admin', sysdate(), '', NULL, '示例数据'),
(3, '徐向前', '', '打仗要勇敢，学习也要勇敢，勇于克服困难。', '徐向前（1901-1990），山西五台人，中国人民解放军创建人和领导人之一，十大元帅之一。', '在革命战争时期屡建战功，新中国成立后担任国防部长等重要职务。', '徐向前元帅曾在大别山区指挥作战，建立革命根据地，与大别山人民结下深厚情谊。', 3, '0', '0', 'admin', sysdate(), '', NULL, '示例数据');

-- ----------------------------
-- 9、系统配置-展示模式配置
-- ----------------------------
INSERT INTO sys_config VALUES 
(100, '名人名言展示模式', 'pth.quote.display.mode', 'immersive', 'N', 'admin', sysdate(), '', NULL, '名人名言展示模式：immersive沉浸式滚动，sidebar侧边联动');

-- ----------------------------
-- 10、数字馆资源表
-- ----------------------------
DROP TABLE IF EXISTS pth_digital_resource;
CREATE TABLE pth_digital_resource (
  id                BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '主键ID',
  name              VARCHAR(255)    NOT NULL                   COMMENT '资源名称',
  type              VARCHAR(50)     NOT NULL                   COMMENT '资源类型（image/video/audio/document）',
  url               VARCHAR(500)    NOT NULL                   COMMENT '资源URL',
  size              BIGINT(20)      DEFAULT 0                  COMMENT '资源大小（字节）',
  extension         VARCHAR(20)     DEFAULT ''                 COMMENT '文件扩展名',
  description       VARCHAR(500)    DEFAULT ''                 COMMENT '描述信息',
  create_by_id      BIGINT(20)      DEFAULT NULL               COMMENT '创建者ID',
  create_by_name    VARCHAR(50)     DEFAULT ''                 COMMENT '创建者姓名',
  create_time       DATETIME                                   COMMENT '创建时间',
  update_time       DATETIME                                   COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT = '数字馆资源表';
