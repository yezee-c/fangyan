<template>
  <div class="app-container gallery-container">
    <el-row :gutter="20" class="mb20">
      <el-col :span="24">
        <h2 class="gallery-title">公共文件展示</h2>
      </el-col>
    </el-row>

    <!-- 筛选标签 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="24">
        <el-radio-group v-model="filterType" @change="handleFilterChange" size="medium">
          <el-radio-button label="">全部</el-radio-button>
          <el-radio-button label="image">图片</el-radio-button>
          <el-radio-button label="video">视频</el-radio-button>
        </el-radio-group>
      </el-col>
    </el-row>

    <!-- 文件展示区域 -->
    <div v-loading="loading" class="gallery-grid">
      <div v-for="file in fileList" :key="file.fileId" class="gallery-item">
        <!-- 图片预览 -->
        <div v-if="file.fileType === 'image'" class="image-wrapper" @click="handlePreview(file)">
          <el-image
            :src="file.fileUrl"
            fit="cover"
            lazy
            class="gallery-image"
          >
            <div slot="placeholder" class="image-placeholder">
              <i class="el-icon-loading"></i>
            </div>
            <div slot="error" class="image-error">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
          <div class="image-overlay">
            <span class="file-name">{{ file.originalName }}</span>
          </div>
        </div>

        <!-- 视频预览 -->
        <div v-else-if="file.fileType === 'video'" class="video-wrapper" @click="handleVideoPreview(file)">
          <video 
            :src="file.fileUrl" 
            class="gallery-video"
            preload="metadata"
          ></video>
          <div class="video-overlay">
            <i class="el-icon-video-play play-icon"></i>
            <span class="file-name">{{ file.originalName }}</span>
          </div>
        </div>

        <!-- 其他文件类型 -->
        <div v-else class="other-file" @click="handleDownload(file)">
          <i :class="getFileIcon(file.fileType)" class="file-icon"></i>
          <div class="file-info">
            <p class="file-name">{{ file.originalName }}</p>
            <p class="file-size">{{ formatFileSize(file.fileSize) }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <el-empty v-if="!loading && fileList.length === 0" description="暂无文件"></el-empty>

    <!-- 图片预览对话框 -->
    <el-dialog 
      :visible.sync="previewVisible" 
      width="80%" 
      append-to-body
      @close="handlePreviewClose"
    >
      <img :src="previewUrl" style="width: 100%; display: block;" />
    </el-dialog>

    <!-- 视频预览对话框 -->
    <el-dialog 
      :visible.sync="videoPreviewVisible" 
      width="80%" 
      append-to-body
      @close="handleVideoPreviewClose"
    >
      <video 
        v-if="videoPreviewUrl"
        :src="videoPreviewUrl" 
        controls 
        autoplay
        style="width: 100%; display: block;"
      ></video>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: "FileGallery",
  data() {
    return {
      // 加载状态
      loading: false,
      // 文件列表
      fileList: [],
      // 筛选类型
      filterType: "",
      // 图片预览
      previewVisible: false,
      previewUrl: "",
      // 视频预览
      videoPreviewVisible: false,
      videoPreviewUrl: ""
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询文件列表 */
    getList() {
      this.loading = true;
      request({
        url: '/system/file/gallery/list',
        method: 'get',
        params: {
          fileType: this.filterType
        }
      }).then(response => {
        this.fileList = response.data;
        this.loading = false;
      }).catch(err => {
        this.loading = false;
        console.error('获取文件列表失败', err);
      });
    },
    /** 筛选类型改变 */
    handleFilterChange() {
      this.getList();
    },
    /** 图片预览 */
    handlePreview(file) {
      this.previewUrl = file.fileUrl;
      this.previewVisible = true;
    },
    /** 关闭图片预览 */
    handlePreviewClose() {
      this.previewUrl = "";
    },
    /** 视频预览 */
    handleVideoPreview(file) {
      this.videoPreviewUrl = file.fileUrl;
      this.videoPreviewVisible = true;
    },
    /** 关闭视频预览 */
    handleVideoPreviewClose() {
      this.videoPreviewUrl = "";
    },
    /** 下载文件 */
    handleDownload(file) {
      window.location.href = file.fileUrl;
    },
    /** 获取文件图标 */
    getFileIcon(fileType) {
      const iconMap = {
        'pdf': 'el-icon-document',
        'word': 'el-icon-document',
        'excel': 'el-icon-document',
        'audio': 'el-icon-headset',
        'other': 'el-icon-document'
      };
      return iconMap[fileType] || 'el-icon-document';
    },
    /** 格式化文件大小 */
    formatFileSize(bytes) {
      if (bytes === 0) return '0 B';
      const k = 1024;
      const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
      const i = Math.floor(Math.log(bytes) / Math.log(k));
      return (bytes / Math.pow(k, i)).toFixed(2) + ' ' + sizes[i];
    }
  }
};
</script>

<style>
/* 全局重置，确保页面占满屏幕 */
html, body {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  overflow-x: hidden;
}

#app {
  height: 100%;
  margin: 0;
  padding: 0;
}
</style>

<style scoped>
.gallery-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
  margin: 0;
  width: 100%;
}

.gallery-title {
  text-align: center;
  color: #303133;
  margin-bottom: 20px;
  font-size: 28px;
  font-weight: 500;
}

.mb20 {
  margin-bottom: 20px;
}

.gallery-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  padding: 10px;
}

.gallery-item {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  cursor: pointer;
}

.gallery-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

/* 图片样式 */
.image-wrapper {
  position: relative;
  width: 100%;
  padding-bottom: 75%; /* 4:3 比例 */
  overflow: hidden;
}

.gallery-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.image-placeholder,
.image-error {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 32px;
}

.image-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(to top, rgba(0,0,0,0.7), transparent);
  padding: 15px 10px 10px;
  color: white;
  opacity: 0;
  transition: opacity 0.3s;
}

.image-wrapper:hover .image-overlay {
  opacity: 1;
}

/* 视频样式 */
.video-wrapper {
  position: relative;
  width: 100%;
  padding-bottom: 56.25%; /* 16:9 比例 */
  overflow: hidden;
  background: #000;
}

.gallery-video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.video-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.3);
  color: white;
  transition: background 0.3s;
}

.video-wrapper:hover .video-overlay {
  background: rgba(0, 0, 0, 0.5);
}

.play-icon {
  font-size: 48px;
  margin-bottom: 10px;
}

/* 其他文件样式 */
.other-file {
  display: flex;
  align-items: center;
  padding: 20px;
  min-height: 100px;
}

.file-icon {
  font-size: 48px;
  color: #409EFF;
  margin-right: 15px;
}

.file-info {
  flex: 1;
  overflow: hidden;
}

.file-name {
  font-size: 14px;
  color: #303133;
  margin: 0 0 5px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.file-size {
  font-size: 12px;
  color: #909399;
  margin: 0;
}

/* 响应式布局 */
@media (max-width: 768px) {
  .gallery-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 10px;
  }
  
  .gallery-title {
    font-size: 20px;
  }
}
</style>
