<template>
  <div class="video-course-container">
    <div class="page-header">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/pth/course' }">课程中心</el-breadcrumb-item>
        <el-breadcrumb-item>视频课程</el-breadcrumb-item>
      </el-breadcrumb>
      <h1>📹 视频课程学习</h1>
      <p>系统化视频教学，从基础到进阶</p>
    </div>

    <!-- 视频播放器弹窗 -->
    <el-dialog :visible.sync="playerVisible" width="80%" top="5vh" @close="closePlayer">
      <div slot="title" class="dialog-title">
        <i class="el-icon-video-play"></i>
        {{ currentVideo.title }}
      </div>
      <div class="video-player-wrapper">
        <video 
          ref="videoPlayer" 
          controls 
          autoplay
          class="video-player"
          :poster="currentVideo.poster"
          @ended="handleVideoEnd">
          <source :src="currentVideo.videoUrl" type="video/mp4">
          您的浏览器不支持视频播放
        </video>
      </div>
      <div class="video-description">
        <h3>课程简介</h3>
        <p>{{ currentVideo.desc }}</p>
      </div>
    </el-dialog>

    <!-- 视频列表 -->
    <el-row :gutter="20">
      <el-col :xs="24" :sm="12" :lg="8" v-for="(video, index) in videos" :key="index">
        <el-card shadow="hover" class="video-card">
          <div class="video-thumbnail" @click="playVideo(video)">
            <!-- 使用canvas捕获视频第一帧作为封面 -->
            <img v-if="video.thumbnailUrl" :src="video.thumbnailUrl" alt="视频封面" class="video-cover">
            <img v-else-if="video.poster" :src="video.poster" alt="视频封面" class="video-cover">
            <div v-else class="default-thumbnail">
              <i class="el-icon-video-play" style="font-size: 60px; color: #409EFF;"></i>
            </div>
            <!-- 隐藏的video元素用于捕获第一帧 -->
            <video 
              :ref="'thumbVideo' + index" 
              :src="video.videoUrl" 
              style="display: none;"
              crossorigin="anonymous"
              preload="metadata"
              muted
              @loadedmetadata="captureFrame(video, index)">
            </video>
            <div class="video-duration">{{ video.duration }}</div>
            <div class="play-overlay">
              <i class="el-icon-caret-right"></i>
            </div>
          </div>
          <div class="video-info">
            <h3>{{ video.title }}</h3>
            <p>{{ video.desc }}</p>
            <div class="video-meta">
              <span><i class="el-icon-view"></i> {{ video.views }}次观看</span>
              <span><i class="el-icon-star-off"></i> {{ video.rating }}分</span>
            </div>
            <el-button type="primary" size="small" @click="playVideo(video)" icon="el-icon-video-play">开始学习</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 上传视频提示 -->
    <el-alert
      title="视频文件夹位置"
      type="info"
      :closable="false"
      style="margin-top: 30px;">
      <div>
        <p><strong>请将您的视频文件放入以下文件夹：</strong></p>
        <p style="background: #f5f7fa; padding: 10px; border-radius: 4px; margin-top: 10px;">
          <code>ruoyi-ui/public/videos/course/</code>
        </p>
        <p style="margin-top: 10px;">
          支持的视频格式：MP4、WebM、Ogg<br>
          文件命名示例：lesson1.mp4, lesson2.mp4, lesson3.mp4 等
        </p>
      </div>
    </el-alert>
  </div>
</template>

<script>
export default {
  name: 'VideoCourse',
  data() {
    return {
      playerVisible: false, // 播放器弹窗显示
      currentVideo: {}, // 当前播放的视频
      canvasContext: null, // canvas上下文
      videos: [
        {
          title: '普通话发音基础 - 第1课',
          desc: '了解普通话的声母韵母系统',
          duration: '15:30',
          views: 2345,
          rating: 4.8,
          // 使用在线测试视频（如果本地文件不存在）
          videoUrl: 'https://www.w3schools.com/html/mov_bbb.mp4',
          poster: 'https://www.w3schools.com/html/pic_trulli.jpg', // 备用封面
          thumbnailUrl: '' // 自动捕获的缩略图
        },
        {
          title: '声母发音技巧 - 第2课',
          desc: '21个声母的正确发音方法',
          duration: '20:15',
          views: 1876,
          rating: 4.9,
          videoUrl: 'https://www.w3schools.com/html/movie.mp4',
          poster: 'https://picsum.photos/640/360?random=1', // 备用封面
          thumbnailUrl: ''
        },
        {
          title: '韵母发音练习 - 第3课',
          desc: '39个韵母的发音要领',
          duration: '18:45',
          views: 1654,
          rating: 4.7,
          videoUrl: 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4',
          poster: 'https://picsum.photos/640/360?random=2',
          thumbnailUrl: ''
        },
        {
          title: '声调训练 - 第4课',
          desc: '四声的变化规律和练习',
          duration: '22:10',
          views: 2103,
          rating: 4.9,
          videoUrl: 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4',
          poster: 'https://picsum.photos/640/360?random=3',
          thumbnailUrl: ''
        },
        {
          title: '变调规则 - 第5课',
          desc: '一、不、啊的变调规律',
          duration: '16:30',
          views: 1432,
          rating: 4.6,
          videoUrl: 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4',
          poster: 'https://picsum.photos/640/360?random=4',
          thumbnailUrl: ''
        },
        {
          title: '儿化音练习 - 第6课',
          desc: '儿化音的发音技巧',
          duration: '19:20',
          views: 1287,
          rating: 4.8,
          videoUrl: 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4',
          poster: 'https://picsum.photos/640/360?random=5',
          thumbnailUrl: ''
        }
      ]
    }
  },
  mounted() {
    // 组件加载后开始捕获视频第一帧
    // 由于video元素的loadeddata事件会自动触发captureFrame，不需要在这里手动调用
  },
  methods: {
    // 捕获视频第一帧作为缩略图
    captureFrame(video, index) {
      try {
        const videoElement = this.$refs['thumbVideo' + index]
        if (!videoElement || !videoElement[0]) {
          return
        }
        
        const vid = videoElement[0]
        
        // 等待视频加载完成
        if (vid.readyState < 2) {
          // 如果还没加载好，等待一会儿再试
          setTimeout(() => {
            this.captureFrame(video, index)
          }, 100)
          return
        }
        
        // 设置视频时间为第1秒（避免黑屏）
        vid.currentTime = 1
        
        // 等待视频定位到指定时间
        vid.addEventListener('seeked', () => {
          try {
            // 创建canvas元素
            const canvas = document.createElement('canvas')
            canvas.width = vid.videoWidth || 640
            canvas.height = vid.videoHeight || 360
            
            const ctx = canvas.getContext('2d')
            
            // 将视频当前帧绘制到canvas上
            ctx.drawImage(vid, 0, 0, canvas.width, canvas.height)
            
            // 将canvas转换为base64图片
            const thumbnailUrl = canvas.toDataURL('image/jpeg', 0.8)
            
            // 更新video对象的thumbnailUrl
            this.$set(video, 'thumbnailUrl', thumbnailUrl)
            
            console.log(`视频 ${index + 1} 封面捕获成功`)
          } catch (err) {
            console.log(`视频 ${index + 1} 封面捕获失败:`, err)
          }
        }, { once: true })
        
      } catch (error) {
        console.log('捕获视频封面失败:', error)
      }
    },
    
    playVideo(video) {
      this.currentVideo = video
      this.playerVisible = true
      
      // 检查视频文件是否存在
      this.$nextTick(() => {
        const videoElement = this.$refs.videoPlayer
        if (videoElement) {
          videoElement.addEventListener('error', () => {
            this.$message.warning(`视频文件不存在，请将 ${video.videoUrl.split('/').pop()} 放入 ruoyi-ui/public/videos/course/ 文件夹中`)
          })
        }
      })
    },
    
    closePlayer() {
      // 关闭弹窗时暂停视频
      const videoElement = this.$refs.videoPlayer
      if (videoElement) {
        videoElement.pause()
      }
      this.playerVisible = false
    },
    
    handleVideoEnd() {
      this.$message.success('视频播放完毕！')
    }
  },
  
  beforeDestroy() {
    // 组件销毁时清理视频资源
    const videoElement = this.$refs.videoPlayer
    if (videoElement) {
      videoElement.pause()
      videoElement.src = ''
    }
  }
}
</script>

<style scoped>
.video-course-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 30px;
}

.page-header h1 {
  font-size: 28px;
  color: #303133;
  margin: 15px 0 10px;
}

.page-header p {
  font-size: 14px;
  color: #909399;
}

.video-card {
  margin-bottom: 20px;
  transition: all 0.3s;
}

.video-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.video-thumbnail {
  position: relative;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  height: 180px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  margin-bottom: 15px;
  overflow: hidden;
  cursor: pointer;
}

.video-thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.video-cover {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover; /* 裁剪填充 */
  object-position: center; /* 居中显示 */
}

.default-thumbnail {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.play-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: all 0.3s;
}

.play-overlay i {
  font-size: 60px;
  color: white;
}

.video-thumbnail:hover .play-overlay {
  opacity: 1;
}

.video-duration {
  position: absolute;
  bottom: 10px;
  right: 10px;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.video-info h3 {
  font-size: 16px;
  color: #303133;
  margin-bottom: 10px;
}

.video-info p {
  font-size: 14px;
  color: #909399;
  margin-bottom: 15px;
  min-height: 40px;
}

.video-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
  font-size: 13px;
  color: #606266;
}

.video-meta span {
  display: flex;
  align-items: center;
  gap: 5px;
}

/* 视频播放器样式 */
.dialog-title {
  font-size: 18px;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 10px;
}

.video-player-wrapper {
  width: 100%;
  background: #000;
  border-radius: 8px;
  overflow: hidden;
}

.video-player {
  width: 100%;
  max-height: 70vh;
  display: block;
}

.video-description {
  margin-top: 20px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
}

.video-description h3 {
  font-size: 16px;
  color: #303133;
  margin-bottom: 10px;
}

.video-description p {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
}
</style>
