<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 文件上传区域 -->
      <el-col :span="24" class="upload-section">
        <el-card class="upload-card">
          <div slot="header" class="clearfix">
            <span>上传资源</span>
          </div>
          <el-upload
            class="upload-demo"
            drag
            :action="uploadUrl"
            :headers="headers"
            :before-upload="beforeUpload"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            multiple
            :file-list="fileList"
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">
              支持上传图片和视频文件，单个文件不超过10MB
            </div>
          </el-upload>
        </el-card>
      </el-col>

      <!-- 资源展示区域 -->
      <el-col :span="24" class="resource-section">
        <el-card>
          <div slot="header" class="clearfix">
            <span>数字资源</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="refreshResources">刷新</el-button>
          </div>
          
          <!-- 资源筛选 -->
          <div class="filter-container">
            <el-select v-model="resourceType" placeholder="资源类型" clearable @change="filterResources">
              <el-option label="全部" value=""></el-option>
              <el-option label="图片" value="image"></el-option>
              <el-option label="视频" value="video"></el-option>
            </el-select>
            <el-input
              v-model="searchKeyword"
              placeholder="搜索资源..."
              style="width: 200px; margin-left: 10px"
              @keyup.enter.native="filterResources"
            />
            <el-button type="primary" icon="el-icon-search" @click="filterResources">搜索</el-button>
          </div>
          
          <!-- 资源列表 -->
          <div class="resource-list">
            <el-row :gutter="20">
              <el-col 
                v-for="resource in filteredResources" 
                :key="resource.id" 
                :span="6" 
                class="resource-item"
              >
                <el-card :body-style="{ padding: '10px' }" shadow="hover">
                  <div class="resource-preview" @click="previewResource(resource)">
                    <img 
                      v-if="isImage(resource.url)" 
                      :src="getResourceUrl(resource.url)" 
                      class="resource-image" 
                      alt="资源预览"
                    />
                    <div v-else-if="isVideo(resource.url)" class="resource-video">
                      <i class="el-icon-video-play video-icon"></i>
                      <span>视频文件</span>
                    </div>
                    <div v-else class="resource-other">
                      <i class="el-icon-document"></i>
                      <span>文件</span>
                    </div>
                  </div>
                  <div class="resource-info">
                    <div class="resource-name" :title="resource.name">{{ resource.name }}</div>
                    <div class="resource-meta">
                      <span class="resource-size">{{ formatFileSize(resource.size) }}</span>
                      <span class="resource-date">{{ formatDate(resource.createTime) }}</span>
                    </div>
                    <div class="resource-actions">
                      <el-button type="text" size="mini" @click="downloadResource(resource)">下载</el-button>
                      <el-button type="text" size="mini" @click="deleteResource(resource)">删除</el-button>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
            
            <!-- 空状态 -->
            <div v-if="filteredResources.length === 0" class="empty-state">
              <el-empty description="暂无资源"></el-empty>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 资源预览对话框 -->
    <el-dialog
      :title="previewTitle"
      :visible.sync="previewDialogVisible"
      width="80%"
      :before-close="handlePreviewClose"
    >
      <div class="preview-content">
        <img 
          v-if="isImage(currentPreview.url)" 
          :src="getResourceUrl(currentPreview.url)" 
          class="preview-image" 
          alt="预览图片"
        />
        <video 
          v-else-if="isVideo(currentPreview.url)" 
          :src="getResourceUrl(currentPreview.url)" 
          class="preview-video" 
          controls
        ></video>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getToken } from "@/utils/auth";

export default {
  name: "DigitalResource",
  data() {
    return {
      // 上传相关
      uploadUrl: process.env.VUE_APP_BASE_API + "/common/upload",
      headers: {
        Authorization: "Bearer " + getToken()
      },
      fileList: [],
      
      // 资源相关
      resources: [],
      filteredResources: [],
      resourceType: "",
      searchKeyword: "",
      
      // 预览相关
      previewDialogVisible: false,
      previewTitle: "",
      currentPreview: {},
      
      // 模拟数据
      mockResources: [
        {
          id: 1,
          name: "风景图片1.jpg",
          url: "/profile/upload/风景图片1.jpg",
          type: "image",
          size: 1024000,
          createTime: "2025-11-20 10:30:00"
        },
        {
          id: 2,
          name: "教学视频1.mp4",
          url: "/profile/upload/教学视频1.mp4",
          type: "video",
          size: 52428800,
          createTime: "2025-11-20 11:45:00"
        },
        {
          id: 3,
          name: "风景图片2.jpg",
          url: "/profile/upload/风景图片2.jpg",
          type: "image",
          size: 2048000,
          createTime: "2025-11-20 14:20:00"
        },
        {
          id: 4,
          name: "教学视频2.mp4",
          url: "/profile/upload/教学视频2.mp4",
          type: "video",
          size: 104857600,
          createTime: "2025-11-20 16:10:00"
        }
      ]
    };
  },
  created() {
    this.loadResources();
  },
  methods: {
    // 上传前检查
    beforeUpload(file) {
      const isImage = file.type.includes('image');
      const isVideo = file.type.includes('video');
      const isLt10M = file.size / 1024 / 1024 < 10;

      if (!isImage && !isVideo) {
        this.$message.error('只能上传图片或视频文件!');
        return false;
      }
      if (!isLt10M) {
        this.$message.error('文件大小不能超过 10MB!');
        return false;
      }
      
      return true;
    },
    
    // 上传成功处理
    handleUploadSuccess(response, file, fileList) {
      if (response.code === 200) {
        this.$message.success('上传成功');
        // 添加到资源列表（实际应调用后端接口）
        const newResource = {
          id: this.mockResources.length + 1,
          name: response.originalFilename,
          url: response.url,
          type: response.originalFilename.match(/\.(jpg|jpeg|png|gif)$/i) ? 'image' : 'video',
          size: file.size,
          createTime: new Date().toLocaleString()
        };
        this.mockResources.unshift(newResource);
        this.filterResources();
      } else {
        this.$message.error(response.msg || '上传失败');
      }
    },
    
    // 上传失败处理
    handleUploadError(err) {
      this.$message.error('上传失败，请重试');
    },
    
    // 加载资源
    loadResources() {
      // 实际应调用后端接口获取资源列表
      this.resources = [...this.mockResources];
      this.filteredResources = [...this.resources];
    },
    
    // 刷新资源
    refreshResources() {
      this.loadResources();
      this.$message.success('刷新成功');
    },
    
    // 筛选资源
    filterResources() {
      let result = [...this.resources];
      
      // 按类型筛选
      if (this.resourceType) {
        result = result.filter(resource => 
          (this.resourceType === 'image' && this.isImage(resource.url)) ||
          (this.resourceType === 'video' && this.isVideo(resource.url))
        );
      }
      
      // 按关键字搜索
      if (this.searchKeyword) {
        result = result.filter(resource => 
          resource.name.toLowerCase().includes(this.searchKeyword.toLowerCase())
        );
      }
      
      this.filteredResources = result;
    },
    
    // 判断是否为图片
    isImage(url) {
      return /\.(jpg|jpeg|png|gif|bmp|webp)$/i.test(url);
    },
    
    // 判断是否为视频
    isVideo(url) {
      return /\.(mp4|avi|mov|wmv|flv|mkv)$/i.test(url);
    },
    
    // 获取资源完整URL
    getResourceUrl(url) {
      if (url.startsWith('http')) {
        return url;
      }
      return process.env.VUE_APP_BASE_API + url;
    },
    
    // 预览资源
    previewResource(resource) {
      this.currentPreview = resource;
      this.previewTitle = resource.name;
      this.previewDialogVisible = true;
    },
    
    // 关闭预览
    handlePreviewClose() {
      this.previewDialogVisible = false;
      this.currentPreview = {};
    },
    
    // 下载资源
    downloadResource(resource) {
      const link = document.createElement('a');
      link.href = this.getResourceUrl(resource.url);
      link.download = resource.name;
      link.click();
      this.$message.success('开始下载');
    },
    
    // 删除资源
    deleteResource(resource) {
      this.$confirm(`确定要删除资源 "${resource.name}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 实际应调用后端接口删除资源
        const index = this.mockResources.findIndex(item => item.id === resource.id);
        if (index > -1) {
          this.mockResources.splice(index, 1);
          this.filterResources();
          this.$message.success('删除成功');
        }
      }).catch(() => {
        // 用户取消删除
      });
    },
    
    // 格式化文件大小
    formatFileSize(size) {
      if (size < 1024) {
        return size + ' B';
      } else if (size < 1024 * 1024) {
        return (size / 1024).toFixed(2) + ' KB';
      } else if (size < 1024 * 1024 * 1024) {
        return (size / (1024 * 1024)).toFixed(2) + ' MB';
      } else {
        return (size / (1024 * 1024 * 1024)).toFixed(2) + ' GB';
      }
    },
    
    // 格式化日期
    formatDate(dateString) {
      return dateString.split(' ')[0];
    }
  }
};
</script>

<style scoped lang="scss">
.app-container {
  padding: 20px;
}

.upload-section {
  margin-bottom: 20px;
}

.upload-card {
  .el-upload {
    width: 100%;
  }
  
  .el-upload-dragger {
    width: 100%;
    height: 180px;
  }
}

.resource-section {
  .filter-container {
    margin-bottom: 20px;
    display: flex;
    align-items: center;
  }
  
  .resource-list {
    .resource-item {
      margin-bottom: 20px;
      
      .resource-preview {
        cursor: pointer;
        text-align: center;
        height: 150px;
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: #f5f7fa;
        border-radius: 4px;
        overflow: hidden;
        
        .resource-image {
          max-width: 100%;
          max-height: 100%;
          object-fit: cover;
        }
        
        .resource-video,
        .resource-other {
          display: flex;
          flex-direction: column;
          align-items: center;
          
          .video-icon {
            font-size: 36px;
            color: #409EFF;
            margin-bottom: 10px;
          }
        }
      }
      
      .resource-info {
        margin-top: 10px;
        
        .resource-name {
          font-size: 14px;
          font-weight: 500;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
        
        .resource-meta {
          display: flex;
          justify-content: space-between;
          margin: 5px 0;
          font-size: 12px;
          color: #999;
        }
        
        .resource-actions {
          display: flex;
          justify-content: space-between;
        }
      }
    }
    
    .empty-state {
      text-align: center;
      padding: 40px 0;
    }
  }
}

.preview-content {
  text-align: center;
  
  .preview-image {
    max-width: 100%;
    max-height: 70vh;
  }
  
  .preview-video {
    width: 100%;
    max-height: 70vh;
  }
}
</style>