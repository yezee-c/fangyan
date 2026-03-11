# 普通话学习与测评平台

基于若依框架开发的普通话学习与测评系统，为学生和教师提供一体化的普通话学习和教学管理平台。

## 📝 项目简介

普通话学习与测评平台是一个基于若依（RuoYi-Vue）框架开发的教育类工具平台，旨在帮助学生提升普通话水平，辅助教师高效开展普通话教学管理工作。

### 核心定位
- **学生端**：提供方言识别、普通话测评、趣味练习、语音合成等学习服务
- **教师端**：提供班级管理、任务发布、学习数据查看等教学管理功能
- **文化传承**：通过大别山名人名言展示，传递地域文化价值

## ✨ 主要功能

### 已实现功能

#### 1. 名人名言展示
- ✅ 大别山名人名言后台管理（CRUD）
- ✅ 首页双模式展示
  - 沉浸式滚动模式
  - 侧边联动模式
- ✅ 名人照片上传与预览
- ✅ 显示顺序控制

#### 2. 班级管理
- ✅ 教师创建班级
- ✅ 自动生成邀请码（6位字母数字）
- ✅ 邀请码唯一性校验
- ✅ 学生加入班级
- ✅ 班级信息管理
- ✅ 学生人数统计

#### 3. 权限管理
- ✅ 学生角色配置
- ✅ 教师角色配置
- ✅ 管理员权限
- ✅ 菜单和按钮权限控制

#### 4. 第三方接口预留
- ✅ FunASR方言识别接口（待实现）
- ✅ 科大讯飞语音合成接口（待实现）

### 待实现功能

#### 学生端
- [ ] 方言识别功能
- [ ] 普通话测评
- [ ] 小游戏模块
- [ ] 语音合成功能

#### 教师端
- [ ] 任务发布与管理
- [ ] 任务批改
- [ ] 数据统计图表

## 🛠️ 技术栈

### 后端
- Spring Boot 2.x
- MyBatis
- Spring Security + JWT
- MySQL 5.7+
- Redis

### 前端
- Vue 2.x
- Element UI
- Axios
- Vuex

### 第三方服务
- FunASR - 方言识别
- 科大讯飞 - 语音合成

## 📦 项目结构

```
RuoYi-Vue-master/
├── sql/                          # 数据库脚本
│   ├── ry_20250522.sql          # 若依基础表
│   ├── pth_tables.sql           # 普通话平台表
│   └── pth_menu.sql             # 菜单权限配置
├── ruoyi-admin/                 # 后端主模块
│   └── src/main/java/com/ruoyi/web/controller/pth/
│       ├── PthCelebrityQuoteController.java
│       └── PthClassController.java
├── ruoyi-system/                # 系统模块
│   └── src/main/java/com/ruoyi/pth/
│       ├── domain/              # 实体类
│       │   ├── PthClass.java
│       │   ├── PthCelebrityQuote.java
│       │   ├── PthAssessmentRecord.java
│       │   └── PthTask.java
│       ├── mapper/              # Mapper接口
│       │   ├── PthCelebrityQuoteMapper.java
│       │   └── PthClassMapper.java
│       └── service/             # 业务逻辑
│           ├── IPthCelebrityQuoteService.java
│           ├── IPthClassService.java
│           ├── IDialectRecognitionService.java
│           └── ISpeechSynthesisService.java
└── ruoyi-ui/                    # 前端项目
    └── src/
        ├── api/pth/             # API接口
        │   ├── quote.js
        │   └── class.js
        └── views/pth/           # 页面组件
            ├── home/            # 首页
            ├── admin/           # 管理员页面
            ├── teacher/         # 教师端页面
            └── student/         # 学生端页面
```

## 🚀 快速开始

### 环境要求
- JDK 1.8+
- MySQL 5.7+
- Redis 3.0+
- Node.js 14+
- Maven 3.6+

### 1. 克隆项目
```bash
git clone [your-repository-url]
cd RuoYi-Vue-master
```

### 2. 数据库配置

#### 创建数据库
```sql
CREATE DATABASE ry_vue DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

#### 导入SQL脚本
```bash
mysql -u root -p ry_vue < sql/ry_20250522.sql
mysql -u root -p ry_vue < sql/pth_tables.sql
mysql -u root -p ry_vue < sql/pth_menu.sql
```

### 3. 修改配置

编辑 `ruoyi-admin/src/main/resources/application-druid.yml`：
```yaml
spring:
  datasource:
    druid:
      master:
        url: jdbc:mysql://localhost:3306/ry_vue?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
        username: root
        password: your_password
```

### 4. 启动后端服务
```bash
cd ruoyi-admin
mvn clean install
mvn spring-boot:run
```

### 5. 启动前端服务
```bash
cd ruoyi-ui
npm install
npm run dev
```

### 6. 访问系统
- 前端地址：http://localhost:80
- 后端地址：http://localhost:8080
- 默认账号：admin / admin123

## 📖 使用说明

### 管理员操作

1. 使用admin账号登录
2. 进入"系统管理" → "名人名言"
3. 添加大别山名人名言
4. 上传名人照片
5. 设置显示顺序和状态

### 教师操作

1. 使用教师账号登录
2. 进入"普通话教学" → "班级管理"
3. 点击"新增班级"
4. 填写班级信息（系统自动生成邀请码）
5. 将邀请码分享给学生

### 学生操作

1. 访问首页欣赏名人名言
2. 使用学生账号登录
3. 进入"普通话学习" → "我的班级"
4. 输入教师提供的邀请码
5. 确认班级信息后加入

## 📊 数据库设计

### 核心数据表

| 表名 | 说明 | 状态 |
|-----|------|------|
| pth_class | 班级表 | ✅ |
| pth_class_student | 班级学生关联表 | ✅ |
| pth_assessment_record | 测评记录表 | ✅ |
| pth_dialect_record | 方言识别记录表 | ✅ |
| pth_task | 任务表 | ✅ |
| pth_task_submission | 任务提交表 | ✅ |
| pth_game_score | 游戏积分表 | ✅ |
| pth_celebrity_quote | 名人名言表 | ✅ |

## 🔧 开发指南

### 代码生成

若依框架提供代码生成器，可快速生成CRUD代码：
1. 登录系统
2. 进入"系统工具" → "代码生成"
3. 导入数据表
4. 生成代码并复制到项目

### 开发规范

1. **命名规范**
   - 实体类：PthXxx
   - Mapper：PthXxxMapper
   - Service：IPthXxxService
   - Controller：PthXxxController

2. **包结构**
   - domain：实体类
   - mapper：数据访问层
   - service：业务逻辑层
   - controller：控制层

3. **权限标识**
   - 格式：pth:module:action
   - 示例：pth:class:add

### 接口规范

所有API遵循RESTful设计：
- GET：查询
- POST：新增
- PUT：修改
- DELETE：删除

## 🔌 第三方服务集成

### FunASR方言识别

接口位置：`IDialectRecognitionService.java`

```java
// 实现方言识别
RecognitionResult result = dialectRecognitionService.recognize(audioFile, "大别山方言");

// 方言转普通话
String mandarin = dialectRecognitionService.convertToMandarin(dialectText, "大别山方言");
```

### 科大讯飞语音合成

接口位置：`ISpeechSynthesisService.java`

```java
// 文本转语音
SynthesisResult result = speechSynthesisService.synthesize(text, "xiaoyan", 50, 50, 50);

// 使用默认参数
SynthesisResult result = speechSynthesisService.synthesize(text);
```

## 📝 更新日志

### v1.0.0 (2025-01-12)

#### 新增
- ✅ 名人名言管理模块
- ✅ 班级管理模块
- ✅ 首页双模式展示
- ✅ 权限角色配置
- ✅ 第三方接口预留

#### 数据库
- ✅ 8张核心数据表设计
- ✅ 菜单权限配置
- ✅ 示例数据导入

## 🤝 贡献指南

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 开源协议

本项目基于 MIT 协议开源

## 👥 作者

- 基于若依框架开发
- 普通话学习与测评平台团队

## 📞 技术支持

- 详细开发文档：`DEVELOPMENT_GUIDE.md`
- 需求文档：`demand.md`

## ⚠️ 注意事项

1. **第三方服务**：需要单独申请FunASR和科大讯飞的API密钥
2. **文件上传**：需要配置文件上传路径
3. **数据库字符集**：建议使用utf8mb4
4. **Redis配置**：确保Redis服务已启动
5. **端口配置**：确保8080和80端口未被占用

## 🎯 未来规划

### 短期目标（v1.1.0）
- [ ] 完成测评管理模块
- [ ] 完成任务管理模块
- [ ] 实现数据统计功能

### 长期目标（v2.0.0）
- [ ] 集成FunASR方言识别
- [ ] 集成科大讯飞语音合成
- [ ] 开发小游戏模块
- [ ] 移动端适配

## 🙏 鸣谢

- [若依管理系统](http://ruoyi.vip/)
- [Element UI](https://element.eleme.io/)
- [Vue.js](https://vuejs.org/)

---

**欢迎Star ⭐ 和 Fork 🍴！**
