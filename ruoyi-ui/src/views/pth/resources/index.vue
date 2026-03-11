<template>
  <div class="resources-container">
    <div class="page-header">
      <h1>推广活动与资源下载</h1>
      <p>宣传资料 · 音视频资源 · 活动报名</p>
    </div>

    <!-- 资源分类 -->
    <el-row :gutter="20">
      <el-col :xs="24" :sm="8">
        <el-card shadow="hover" class="resource-category">
          <div class="category-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
            <i class="el-icon-document"></i>
          </div>
          <h3>宣传资料</h3>
          <p>普通话推广宣传文档、手册等</p>
          <el-button type="primary" size="small" @click="viewCategory('宣传资料')">查看资料</el-button>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="8">
        <el-card shadow="hover" class="resource-category">
          <div class="category-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
            <i class="el-icon-picture-outline"></i>
          </div>
          <h3>海报素材</h3>
          <p>精美海报、宣传图片素材</p>
          <el-button type="danger" size="small" @click="viewCategory('海报素材')">查看海报</el-button>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="8">
        <el-card shadow="hover" class="resource-category">
          <div class="category-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
            <i class="el-icon-video-camera"></i>
          </div>
          <h3>音视频资源</h3>
          <p>教学视频、朗读音频等资源</p>
          <el-button type="info" size="small" @click="viewCategory('音视频资源')">查看资源</el-button>
        </el-card>
      </el-col>
    </el-row>

    <!-- 热门资源 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card shadow="hover">
          <div slot="header">
            <span style="font-size: 18px; font-weight: bold;">🔥 热门资源下载</span>
          </div>
          <el-table :data="resources" stripe style="width: 100%">
            <el-table-column type="index" label="#" width="60"></el-table-column>
            <el-table-column prop="name" label="资源名称" min-width="200"></el-table-column>
            <el-table-column prop="type" label="类型" width="120">
              <template slot-scope="scope">
                <el-tag :type="getTypeTag(scope.row.type)">{{ scope.row.type }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="size" label="大小" width="100"></el-table-column>
            <el-table-column prop="downloads" label="下载次数" width="120"></el-table-column>
            <el-table-column label="操作" width="180">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" icon="el-icon-view" @click="preview(scope.row)">预览</el-button>
                <el-button size="mini" type="success" icon="el-icon-download" @click="download(scope.row)">下载</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 推广活动 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card shadow="hover">
          <div slot="header">
            <span style="font-size: 18px; font-weight: bold;">📢 推广活动</span>
          </div>
          <el-row :gutter="20">
            <el-col :xs="24" :sm="12" :md="6" v-for="(activity, index) in activities" :key="index">
              <el-card shadow="hover" class="activity-card">
                <div class="activity-status" :class="activity.statusClass">
                  {{ activity.status }}
                </div>
                <div class="activity-image">
                  <i :class="activity.icon" style="font-size: 60px; color: #409EFF;"></i>
                </div>
                <h4>{{ activity.title }}</h4>
                <p class="activity-desc">{{ activity.desc }}</p>
                <div class="activity-time">
                  <i class="el-icon-time"></i> {{ activity.time }}
                </div>
                <el-button 
                  :type="activity.buttonType" 
                  size="small" 
                  @click="joinActivity(activity)"
                  :disabled="activity.status === '已结束'"
                >
                  {{ activity.buttonText }}
                </el-button>
              </el-card>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <!-- 活动照片展示 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card shadow="hover">
          <div slot="header">
            <span style="font-size: 18px; font-weight: bold;">📷 活动精彩瞬间</span>
          </div>
          <el-carousel height="400px">
            <el-carousel-item v-for="(photo, index) in photos" :key="index">
              <div class="carousel-content">
                <i :class="photo.icon" style="font-size: 100px; color: #409EFF;"></i>
                <h3>{{ photo.title }}</h3>
              </div>
            </el-carousel-item>
          </el-carousel>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'PthResources',
  data() {
    return {
      resources: [
        { name: '普通话推广宣传手册（2024版）', type: 'PDF', size: '5.2MB', downloads: 1245 },
        { name: '普通话水平测试指南', type: 'PDF', size: '3.8MB', downloads: 987 },
        { name: '推普周活动海报模板', type: '图片', size: '12.5MB', downloads: 2134 },
        { name: '声母韵母教学视频合集', type: '视频', size: '256MB', downloads: 756 },
        { name: '朗读示范音频（100篇）', type: '音频', size: '89MB', downloads: 1543 },
        { name: '普通话学习PPT课件', type: 'PPT', size: '18.6MB', downloads: 892 }
      ],
      activities: [
        {
          title: '第27届全国推普周',
          desc: '推广普通话，奋进新征程',
          icon: 'el-icon-trophy',
          time: '2024年9月10-16日',
          status: '进行中',
          statusClass: 'status-active',
          buttonText: '立即报名',
          buttonType: 'primary'
        },
        {
          title: '普通话朗读比赛',
          desc: '展示朗读风采，弘扬传统文化',
          icon: 'el-icon-microphone',
          time: '2024年10月1-15日',
          status: '即将开始',
          statusClass: 'status-upcoming',
          buttonText: '提前报名',
          buttonType: 'success'
        },
        {
          title: '普通话公益讲座',
          desc: '名师指导，免费参加',
          icon: 'el-icon-user',
          time: '每周六 14:00-16:00',
          status: '长期有效',
          statusClass: 'status-long',
          buttonText: '预约报名',
          buttonType: 'info'
        },
        {
          title: '普通话测试月',
          desc: '集中开展普通话水平测试',
          icon: 'el-icon-medal',
          time: '2024年8月1-31日',
          status: '已结束',
          statusClass: 'status-ended',
          buttonText: '查看回顾',
          buttonType: 'info'
        }
      ],
      photos: [
        { title: '推普周启动仪式', icon: 'el-icon-picture' },
        { title: '朗读比赛精彩现场', icon: 'el-icon-picture-outline' },
        { title: '公益讲座现场', icon: 'el-icon-picture-outline-round' },
        { title: '颁奖典礼', icon: 'el-icon-picture' }
      ]
    }
  },
  methods: {
    viewCategory(category) {
      this.$message.success(`查看${category}分类`)
    },
    getTypeTag(type) {
      const typeMap = {
        'PDF': '',
        '图片': 'success',
        '视频': 'warning',
        '音频': 'info',
        'PPT': 'danger'
      }
      return typeMap[type] || ''
    },
    preview(row) {
      this.$message.info(`预览: ${row.name}`)
    },
    download(row) {
      this.$message.success(`开始下载: ${row.name}`)
    },
    joinActivity(activity) {
      if (activity.status === '已结束') {
        this.$message.info('活动已结束')
      } else {
        this.$message.success(`报名成功: ${activity.title}`)
      }
    }
  }
}
</script>

<style scoped>
.resources-container {
  padding: 20px;
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
}

.page-header h1 {
  font-size: 32px;
  color: #303133;
  margin-bottom: 10px;
}

.page-header p {
  font-size: 16px;
  color: #909399;
}

.resource-category {
  text-align: center;
  padding: 20px;
  margin-bottom: 20px;
  transition: all 0.3s;
}

.resource-category:hover {
  transform: translateY(-5px);
}

.category-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  color: #fff;
}

.resource-category h3 {
  font-size: 18px;
  margin: 10px 0;
  color: #303133;
}

.resource-category p {
  font-size: 14px;
  color: #909399;
  margin-bottom: 15px;
}

.activity-card {
  position: relative;
  margin-bottom: 20px;
  text-align: center;
  transition: all 0.3s;
}

.activity-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.activity-status {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 5px 10px;
  border-radius: 4px;
  font-size: 12px;
  color: #fff;
}

.status-active {
  background: #67C23A;
}

.status-upcoming {
  background: #409EFF;
}

.status-long {
  background: #E6A23C;
}

.status-ended {
  background: #909399;
}

.activity-image {
  padding: 20px;
}

.activity-card h4 {
  font-size: 16px;
  margin: 10px 0;
  color: #303133;
}

.activity-desc {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
  min-height: 40px;
}

.activity-time {
  font-size: 13px;
  color: #606266;
  margin-bottom: 15px;
}

.carousel-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.carousel-content h3 {
  margin-top: 20px;
  font-size: 24px;
}
</style>
