# 普通话学习与测评平台开发指南

## 一、项目概述
本项目基于若依框架开发，实现普通话学习与测评平台的完整功能。

## 二、已完成内容

### 1. 数据库设计
- ✅ `sql/pth_tables.sql` - 包含8张核心数据表及初始数据

### 2. 后端实体类（Domain）
- ✅ `PthClass` - 班级实体
- ✅ `PthCelebrityQuote` - 名人名言实体
- ✅ `PthAssessmentRecord` - 测评记录实体
- ✅ `PthTask` - 任务实体

### 3. 名人名言模块（完整）
- ✅ Mapper接口：`PthCelebrityQuoteMapper.java`
- ✅ Mapper XML：`PthCelebrityQuoteMapper.xml`
- ✅ Service接口：`IPthCelebrityQuoteService.java`
- ✅ Service实现：`PthCelebrityQuoteServiceImpl.java`
- ✅ Controller：`PthCelebrityQuoteController.java`

## 三、待完成内容

### 后端模块

#### 1. 班级管理模块
**文件位置**：
- Mapper: `ruoyi-system/src/main/java/com/ruoyi/pth/mapper/PthClassMapper.java`
- Mapper XML: `ruoyi-system/src/main/resources/mapper/pth/PthClassMapper.xml`
- Service: `ruoyi-system/src/main/java/com/ruoyi/pth/service/IPthClassService.java`
- ServiceImpl: `ruoyi-system/src/main/java/com/ruoyi/pth/service/impl/PthClassServiceImpl.java`
- Controller: `ruoyi-admin/src/main/java/com/ruoyi/web/controller/pth/PthClassController.java`

**核心功能**：
- 班级CRUD
- 生成邀请码
- 学生加入班级
- 班级数据统计

#### 2. 测评管理模块
**文件位置**：
- Mapper: `ruoyi-system/src/main/java/com/ruoyi/pth/mapper/PthAssessmentRecordMapper.java`
- Mapper XML: `ruoyi-system/src/main/resources/mapper/pth/PthAssessmentRecordMapper.xml`
- Service: `ruoyi-system/src/main/java/com/ruoyi/pth/service/IPthAssessmentRecordService.java`
- ServiceImpl: `ruoyi-system/src/main/java/com/ruoyi/pth/service/impl/PthAssessmentRecordServiceImpl.java`
- Controller: `ruoyi-admin/src/main/java/com/ruoyi/web/controller/pth/PthAssessmentController.java`

**核心功能**：
- 创建测评
- 提交测评
- 评分算法（预留接口）
- 历史记录查询

#### 3. 任务管理模块
**文件位置**：
- Mapper: `ruoyi-system/src/main/java/com/ruoyi/pth/mapper/PthTaskMapper.java`
- Mapper XML: `ruoyi-system/src/main/resources/mapper/pth/PthTaskMapper.xml`
- Service: `ruoyi-system/src/main/java/com/ruoyi/pth/service/IPthTaskService.java`
- ServiceImpl: `ruoyi-system/src/main/java/com/ruoyi/pth/service/impl/PthTaskServiceImpl.java`
- Controller: `ruoyi-admin/src/main/java/com/ruoyi/web/controller/pth/PthTaskController.java`

**核心功能**：
- 任务发布
- 任务提交
- 任务批改
- 完成情况统计

#### 4. 第三方服务接口
**文件位置**：
- `ruoyi-system/src/main/java/com/ruoyi/pth/service/IDialectRecognitionService.java` - 方言识别服务（FunASR预留）
- `ruoyi-system/src/main/java/com/ruoyi/pth/service/ISpeechSynthesisService.java` - 语音合成服务（科大讯飞预留）

### 前端模块

#### 1. 首页
**文件位置**：`ruoyi-ui/src/views/pth/home/index.vue`

**功能**：
- 名人名言展示（沉浸式滚动/侧边联动模式）
- 登录入口
- 平台简介

#### 2. 学生端页面
**目录结构**：
```
ruoyi-ui/src/views/pth/student/
├── dialect/          # 方言识别
│   └── index.vue
├── assessment/       # 普通话测评
│   ├── index.vue
│   └── detail.vue
├── game/            # 小游戏
│   └── index.vue
├── synthesis/       # 语音合成
│   └── index.vue
└── class/           # 班级信息
    └── index.vue
```

#### 3. 教师端页面
**目录结构**：
```
ruoyi-ui/src/views/pth/teacher/
├── class/           # 班级管理
│   └── index.vue
├── task/            # 任务管理
│   ├── index.vue
│   └── review.vue
└── statistics/      # 数据统计
    └── index.vue
```

#### 4. 管理员页面
**目录结构**：
```
ruoyi-ui/src/views/pth/admin/
└── quote/           # 名人名言管理
    └── index.vue
```

#### 5. API接口封装
**文件位置**：
```
ruoyi-ui/src/api/pth/
├── quote.js         # 名人名言API
├── class.js         # 班级API
├── assessment.js    # 测评API
├── task.js          # 任务API
├── dialect.js       # 方言识别API
├── synthesis.js     # 语音合成API
└── game.js          # 游戏API
```

### 权限配置

#### 菜单权限SQL
**文件位置**：`sql/pth_menu.sql`

需要添加的菜单：
1. 普通话学习（一级菜单）
   - 方言识别（学生）
   - 普通话测评（学生）
   - 小游戏（学生）
   - 语音合成（学生）
   - 我的班级（学生）

2. 普通话教学（一级菜单）
   - 班级管理（教师）
   - 任务管理（教师）
   - 数据统计（教师）

3. 系统管理（扩展）
   - 名人名言管理（管理员）

## 四、开发优先级

### P0（必须实现）
1. ✅ 数据库设计
2. ✅ 名人名言后端
3. ⏳ 班级管理后端
4. ⏳ 测评管理后端
5. ⏳ 首页前端
6. ⏳ 菜单权限配置

### P1（建议实现）
1. ⏳ 任务管理
2. ⏳ 学生端页面
3. ⏳ 教师端页面
4. ⏳ 第三方接口预留

### P2（可选实现）
1. 游戏模块
2. 高级UI效果
3. 数据统计图表

## 五、技术要点

### 后端
- Spring Boot 2.x
- MyBatis
- Spring Security + JWT
- Redis缓存

### 前端
- Vue 2.x
- Element UI
- Axios
- Vuex

### 第三方服务
- FunASR：方言识别（预留接口）
- 科大讯飞：语音合成（预留接口）

## 六、启动步骤

1. 导入数据库脚本
```sql
mysql -u root -p < sql/ry_20250522.sql
mysql -u root -p < sql/pth_tables.sql
mysql -u root -p < sql/pth_menu.sql
```

2. 配置数据库连接
编辑 `ruoyi-admin/src/main/resources/application-druid.yml`

3. 启动后端
```bash
cd ruoyi-admin
mvn spring-boot:run
```

4. 启动前端
```bash
cd ruoyi-ui
npm install
npm run dev
```

5. 访问系统
- 前端地址：http://localhost:80
- 后端地址：http://localhost:8080
- 默认账号：admin/admin123

## 七、注意事项

1. 所有实体类需继承`BaseEntity`
2. Mapper需要MyBatis XML映射文件
3. Controller需要权限注解`@PreAuthorize`
4. 前端API请求需要统一错误处理
5. 文件上传使用若依提供的上传组件
6. 音频处理需要前端录音组件

## 八、下一步工作

立即完成：
1. 创建班级管理完整后端
2. 创建测评管理完整后端
3. 创建首页前端
4. 添加菜单权限配置
5. 创建前端API封装
6. 测试核心功能

## 九、项目文件清单

### 已创建
- [x] sql/pth_tables.sql
- [x] ruoyi-system/src/main/java/com/ruoyi/pth/domain/PthClass.java
- [x] ruoyi-system/src/main/java/com/ruoyi/pth/domain/PthCelebrityQuote.java
- [x] ruoyi-system/src/main/java/com/ruoyi/pth/domain/PthAssessmentRecord.java
- [x] ruoyi-system/src/main/java/com/ruoyi/pth/domain/PthTask.java
- [x] ruoyi-system/src/main/java/com/ruoyi/pth/mapper/PthCelebrityQuoteMapper.java
- [x] ruoyi-system/src/main/resources/mapper/pth/PthCelebrityQuoteMapper.xml
- [x] ruoyi-system/src/main/java/com/ruoyi/pth/service/IPthCelebrityQuoteService.java
- [x] ruoyi-system/src/main/java/com/ruoyi/pth/service/impl/PthCelebrityQuoteServiceImpl.java
- [x] ruoyi-admin/src/main/java/com/ruoyi/web/controller/pth/PthCelebrityQuoteController.java

### 待创建（核心）
- [ ] 班级管理完整后端（5个文件）
- [ ] 测评管理完整后端（5个文件）
- [ ] 任务管理完整后端（5个文件）
- [ ] 首页前端（1个文件）
- [ ] 学生端前端（5个文件）
- [ ] 教师端前端（3个文件）
- [ ] 前端API（7个文件）
- [ ] 菜单权限SQL（1个文件）
