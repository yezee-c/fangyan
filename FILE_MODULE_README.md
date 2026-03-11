# 文件上传下载功能模块部署说明

## 功能概述

本模块基于若依框架实现了完整的文件上传下载功能，使用MinIO作为文件存储服务。

### 主要功能
1. **文件存储**：使用MinIO作为文件存储服务
2. **权限控制**：
   - 管理员可以控制用户最大上传文件大小与最大容量
   - 用户可以上传和下载自己的私有文件
   - 用户可以将文件上传到公共区域供所有用户下载
3. **上传功能**：
   - 支持批量上传，单次最多50个文件
   - 上传完成后自动刷新文件列表
4. **文件展示**：列表中显示文件图标，不预览具体内容
5. **文件管理**：支持文件删除和修改操作

## 部署步骤

### 1. 安装和配置MinIO

#### Windows环境
1. 下载MinIO: https://dl.min.io/server/minio/release/windows-amd64/minio.exe
2. 创建数据目录，例如：`D:\minio\data`
3. 启动MinIO服务：
```bash
minio.exe server D:\minio\data --console-address ":9001"
```

#### Linux环境
```bash
wget https://dl.min.io/server/minio/release/linux-amd64/minio
chmod +x minio
./minio server /data/minio --console-address ":9001"
```

#### Docker方式
```bash
docker run -p 9000:9000 -p 9001:9001 \
  --name minio \
  -v /data/minio:/data \
  -e "MINIO_ROOT_USER=minioadmin" \
  -e "MINIO_ROOT_PASSWORD=minioadmin" \
  minio/minio server /data --console-address ":9001"
```

MinIO启动后：
- API端口：9000
- 控制台：http://localhost:9001
- 默认账号：minioadmin
- 默认密码：minioadmin

### 2. 数据库配置

执行以下SQL脚本创建表和菜单：

```bash
# 1. 创建文件管理相关表
sql/file_management.sql

# 2. 创建菜单数据
sql/file_menu.sql
```

### 3. 后端配置

#### 修改application.yml
已在 `ruoyi-admin/src/main/resources/application.yml` 中添加了MinIO配置：
```yaml
# MinIO配置
minio:
  # MinIO服务地址
  endpoint: http://localhost:9000
  # 访问密钥
  accessKey: minioadmin
  # 密钥
  secretKey: minioadmin
  # 存储桶名称
  bucketName: ruoyi
```

根据实际情况修改endpoint、accessKey、secretKey。

#### Maven依赖
已在pom.xml中添加MinIO依赖，执行Maven更新：
```bash
mvn clean install
```

### 4. 前端部署

前端文件已创建在以下位置：
```
ruoyi-ui/src/
├── api/system/
│   ├── file.js           # 文件管理API
│   └── fileconfig.js     # 文件配置API
└── views/system/file/
    ├── myfile.vue        # 我的文件页面
    ├── publicfile.vue    # 公共文件页面
    └── config.vue        # 文件配置页面（管理员）
```

### 5. 启动项目

#### 后端启动
```bash
cd ruoyi-admin
mvn spring-boot:run
```

或使用启动脚本：
```bash
# Windows
bin\run.bat

# Linux
sh bin/run.sh
```

#### 前端启动
```bash
cd ruoyi-ui
npm install
npm run dev
```

### 6. 访问系统

1. 访问前端：http://localhost:80
2. 登录系统（默认账号：admin/admin123）
3. 在左侧菜单找到"文件管理"模块

## 功能使用说明

### 用户操作

#### 我的文件
- 上传私有文件：点击"上传文件"按钮，选择文件上传
- 下载文件：点击文件列表中的"下载"按钮
- 修改文件：修改文件名称和备注
- 删除文件：删除不需要的文件

#### 公共文件
- 上传公共文件：点击"上传公共文件"按钮
- 浏览下载：所有用户可以查看和下载公共文件

### 管理员操作

#### 文件配置
- 查看用户配置：查看所有用户的文件配置和使用情况
- 新增配置：为用户设置文件大小和容量限制
- 修改配置：调整用户的配置参数
- 删除配置：删除用户的文件配置

配置参数说明：
- **单文件大小限制**：用户单次上传文件的最大大小（MB）
- **总存储容量限制**：用户可使用的总存储空间（GB）
- **已使用容量**：用户当前已使用的存储空间
- **使用率**：存储空间使用百分比

## 技术架构

### 后端
- **框架**：Spring Boot 2.5.15
- **存储**：MinIO 8.5.2
- **数据库**：MySQL
- **ORM**：MyBatis

### 前端
- **框架**：Vue 2.x
- **UI组件**：Element UI
- **HTTP客户端**：Axios

## 文件结构

### 后端文件
```
ruoyi-common/
└── src/main/java/com/ruoyi/common/
    ├── config/MinioConfig.java          # MinIO配置类
    └── utils/MinioUtils.java            # MinIO工具类

ruoyi-system/
├── src/main/java/com/ruoyi/system/
│   ├── domain/
│   │   ├── SysFileInfo.java            # 文件信息实体
│   │   └── SysUserFileConfig.java      # 用户配置实体
│   ├── mapper/
│   │   ├── SysFileInfoMapper.java      # 文件信息Mapper
│   │   └── SysUserFileConfigMapper.java # 用户配置Mapper
│   └── service/
│       ├── ISysFileInfoService.java     # 文件服务接口
│       ├── ISysUserFileConfigService.java # 配置服务接口
│       └── impl/
│           ├── SysFileInfoServiceImpl.java
│           └── SysUserFileConfigServiceImpl.java
└── src/main/resources/mapper/system/
    ├── SysFileInfoMapper.xml
    └── SysUserFileConfigMapper.xml

ruoyi-admin/
└── src/main/java/com/ruoyi/web/controller/system/
    ├── SysFileInfoController.java       # 文件控制器
    └── SysUserFileConfigController.java # 配置控制器
```

### 前端文件
```
ruoyi-ui/src/
├── api/system/
│   ├── file.js
│   └── fileconfig.js
└── views/system/file/
    ├── myfile.vue
    ├── publicfile.vue
    └── config.vue
```

## 注意事项

1. **MinIO服务**：确保MinIO服务正常运行，系统才能正常上传下载文件
2. **端口配置**：MinIO默认使用9000端口，确保端口未被占用
3. **存储路径**：确保MinIO数据目录有足够的磁盘空间
4. **权限配置**：首次使用需要在MinIO控制台创建bucket（系统会自动创建ruoyi bucket）
5. **文件大小限制**：
   - 系统默认单文件限制10MB
   - 可在application.yml中修改spring.servlet.multipart.max-file-size
   - 管理员可为用户单独配置限制
6. **网络配置**：如果MinIO部署在其他服务器，需要修改endpoint地址

## 常见问题

### 1. 上传失败
- 检查MinIO服务是否启动
- 检查application.yml中的MinIO配置是否正确
- 检查用户存储配额是否已满

### 2. 下载失败
- 检查MinIO服务是否正常
- 检查文件是否存在
- 检查用户是否有下载权限

### 3. 菜单不显示
- 确认已执行菜单SQL脚本
- 清除浏览器缓存重新登录
- 检查用户角色权限

## 扩展功能建议

1. **文件分享**：生成分享链接供外部访问
2. **文件预览**：支持图片、视频、PDF等文件在线预览
3. **文件版本**：支持文件版本管理
4. **文件夹管理**：支持文件夹层级管理
5. **断点续传**：支持大文件断点续传
6. **文件压缩**：支持批量文件打包下载

## 联系支持

如有问题，请查阅若依官方文档或MinIO官方文档。
