<template>
  <div class="app-container promotion-achievements">
    <div class="header-section">
      <span><i class="el-icon-trophy"></i> 推广成果</span>
      <el-tag type="success" size="small" style="float: right;">展示普通话推广成果</el-tag>
    </div>

    <div class="content-wrapper">
      <!-- 搜索和筛选区域 -->
      <div class="search-filter-section">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-input 
              v-model="searchKeyword" 
              placeholder="请输入成果关键词" 
              clearable
              @keyup.enter.native="searchAchievements"
              prefix-icon="el-icon-search">
            </el-input>
          </el-col>
          <el-col :span="6">
            <el-select v-model="filterType" placeholder="成果类型" clearable @change="filterAchievements">
              <el-option label="全部类型" value=""></el-option>
              <el-option label="图片" value="image"></el-option>
              <el-option label="文档" value="document"></el-option>
              <el-option label="视频" value="video"></el-option>
              <el-option label="报告" value="report"></el-option>
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-button type="primary" @click="searchAchievements" icon="el-icon-search">搜索</el-button>
          </el-col>
        </el-row>
      </div>

      <!-- 成果展示区域 -->
      <div class="achievements-grid">
        <el-row :gutter="20">
          <!-- 成果卡片 -->
          <el-col 
            :xs="24" 
            :sm="12" 
            :md="8" 
            :lg="6" 
            v-for="achievement in filteredAchievements" 
            :key="achievement.id">
            <el-card 
              shadow="hover" 
              class="achievement-card"
              @click.native="viewAchievement(achievement)">
              <div class="card-header">
                <i :class="getIconByType(achievement.type)" class="achievement-icon"></i>
                <span class="achievement-type">{{ achievement.typeName }}</span>
              </div>
              <div class="achievement-content">
                <h4 class="achievement-title">{{ achievement.title }}</h4>
                <p class="achievement-desc">{{ achievement.description }}</p>
                <div class="achievement-meta">
                  <span class="date"><i class="el-icon-time"></i> {{ achievement.date }}</span>
                  <span class="location"><i class="el-icon-location"></i> {{ achievement.location }}</span>
                </div>
              </div>
              <div class="achievement-preview" v-if="achievement.preview">
                <img :src="achievement.preview" alt="成果预览" v-if="achievement.type === 'image' || achievement.type === 'document'">
                <div class="video-preview" v-else-if="achievement.type === 'video'">
                  <i class="el-icon-video-play"></i>
                </div>
                <div class="report-preview" v-else-if="achievement.type === 'report'">
                  <i class="el-icon-document"></i>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 分页 -->
      <div class="pagination-section">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[8, 12, 16, 24]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="filteredAchievements.length">
        </el-pagination>
      </div>
    </div>

    <!-- 成果详情弹窗 -->
    <el-dialog
      :title="selectedAchievement.title"
      :visible.sync="dialogVisible"
      width="60%"
      center>
      <div class="achievement-detail">
        <div class="detail-header">
          <h3>{{ selectedAchievement.title }}</h3>
          <p class="detail-meta">
            <span><i class="el-icon-time"></i> {{ selectedAchievement.date }}</span>
            <span><i class="el-icon-location"></i> {{ selectedAchievement.location }}</span>
            <span><i class="el-icon-user"></i> {{ selectedAchievement.author }}</span>
          </p>
        </div>
        <div class="detail-content">
          <p>{{ selectedAchievement.description }}</p>
          <div class="detail-media" v-if="selectedAchievement.media">
            <img :src="selectedAchievement.media" alt="成果详情" v-if="selectedAchievement.type === 'image'">
            <video :src="selectedAchievement.media" controls v-else-if="selectedAchievement.type === 'video'"></video>
            <div class="document-preview" v-else-if="selectedAchievement.type === 'document'">
              <i class="el-icon-document"></i>
              <p>文档预览功能待实现</p>
            </div>
            <div class="report-preview" v-else-if="selectedAchievement.type === 'report'">
              <i class="el-icon-document"></i>
              <p>报告预览功能待实现</p>
            </div>
          </div>
        </div>
        <div class="detail-stats">
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="stat-item">
                <i class="el-icon-view"></i>
                <span>{{ selectedAchievement.views || 0 }} 浏览</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="stat-item">
                <i class="el-icon-star-off"></i>
                <span>{{ selectedAchievement.likes || 0 }} 点赞</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="stat-item">
                <i class="el-icon-share"></i>
                <span>{{ selectedAchievement.shares || 0 }} 分享</span>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">关 闭</el-button>
        <el-button type="primary" @click="downloadAchievement(selectedAchievement)">下 载</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'PromotionAchievements',
  data() {
    return {
      searchKeyword: '',
      filterType: '',
      currentPage: 1,
      pageSize: 8,
      dialogVisible: false,
      selectedAchievement: {},
      achievements: [
        {
          id: 1,
          title: '2023年度普通话推广活动总结',
          description: '详细记录了2023年度在多个城市开展的普通话推广活动成果',
          type: 'report',
          typeName: '报告',
          date: '2023-12-15',
          location: '全国',
          author: '推广部',
          preview: require('@/assets/images/profile.jpg'),
          media: require('@/assets/images/profile.jpg'),
          views: 1245,
          likes: 89,
          shares: 32
        },
        {
          id: 2,
          title: '大别山地区方言保护与普通话推广成果',
          description: '在保护方言文化的同时，有效推广普通话使用的成功案例',
          type: 'document',
          typeName: '文档',
          date: '2023-11-20',
          location: '大别山地区',
          author: '文化部',
          preview: require('@/assets/images/profile.jpg'),
          media: require('@/assets/images/profile.jpg'),
          views: 987,
          likes: 102,
          shares: 45
        },
        {
          id: 3,
          title: '普通话推广活动精彩瞬间',
          description: '记录了多场普通话推广活动的精彩照片',
          type: 'image',
          typeName: '图片',
          date: '2023-10-05',
          location: '武汉',
          author: '宣传部',
          preview: require('@/assets/images/profile.jpg'),
          media: require('@/assets/images/profile.jpg'),
          views: 2156,
          likes: 156,
          shares: 67
        },
        {
          id: 4,
          title: '普通话学习效果对比分析',
          description: '通过数据分析展示普通话学习前后的能力提升情况',
          type: 'report',
          typeName: '报告',
          date: '2023-09-18',
          location: '华中地区',
          author: '研究部',
          preview: require('@/assets/images/profile.jpg'),
          media: require('@/assets/images/profile.jpg'),
          views: 876,
          likes: 74,
          shares: 28
        },
        {
          id: 5,
          title: '推广活动宣传视频',
          description: '普通话推广活动的宣传视频，展示活动精彩内容',
          type: 'video',
          typeName: '视频',
          date: '2023-08-30',
          location: '全国',
          author: '宣传部',
          preview: require('@/assets/images/profile.jpg'),
          media: require('@/assets/images/profile.jpg'),
          views: 3421,
          likes: 234,
          shares: 120
        },
        {
          id: 6,
          title: '方言与普通话对比研究报告',
          description: '深入研究方言与普通话的关系及其对学习的影响',
          type: 'document',
          typeName: '文档',
          date: '2023-07-22',
          location: '大别山地区',
          author: '研究部',
          preview: require('@/assets/images/profile.jpg'),
          media: require('@/assets/images/profile.jpg'),
          views: 765,
          likes: 56,
          shares: 19
        },
        {
          id: 7,
          title: '普通话学习者反馈汇总',
          description: '收集和分析普通话学习者的学习体验和反馈意见',
          type: 'report',
          typeName: '报告',
          date: '2023-06-15',
          location: '全国',
          author: '客服部',
          preview: require('@/assets/images/profile.jpg'),
          media: require('@/assets/images/profile.jpg'),
          views: 1098,
          likes: 87,
          shares: 34
        },
        {
          id: 8,
          title: '校园普通话推广活动照片',
          description: '在多所学校开展的普通话推广活动照片集锦',
          type: 'image',
          typeName: '图片',
          date: '2023-05-10',
          location: '校园',
          author: '教育部',
          preview: require('@/assets/images/profile.jpg'),
          media: require('@/assets/images/profile.jpg'),
          views: 1876,
          likes: 134,
          shares: 56
        }
      ],
      filteredAchievements: []
    }
  },
  created() {
    this.filteredAchievements = this.achievements;
  },
  methods: {
    getIconByType(type) {
      switch (type) {
        case 'image': return 'el-icon-picture';
        case 'document': return 'el-icon-document';
        case 'video': return 'el-icon-video-camera';
        case 'report': return 'el-icon-data-line';
        default: return 'el-icon-folder';
      }
    },
    searchAchievements() {
      this.filterAchievements();
    },
    filterAchievements() {
      this.filteredAchievements = this.achievements.filter(item => {
        const matchesKeyword = !this.searchKeyword || 
          item.title.toLowerCase().includes(this.searchKeyword.toLowerCase()) ||
          item.description.toLowerCase().includes(this.searchKeyword.toLowerCase());
        
        const matchesType = !this.filterType || item.type === this.filterType;
        
        return matchesKeyword && matchesType;
      });
      this.currentPage = 1;
    },
    viewAchievement(achievement) {
      this.selectedAchievement = achievement;
      this.dialogVisible = true;
    },
    downloadAchievement(achievement) {
      this.$message.success(`正在下载: ${achievement.title}`);
      // 这里可以实现实际的下载逻辑
    },
    handleSizeChange(val) {
      this.pageSize = val;
    },
    handleCurrentChange(val) {
      this.currentPage = val;
    }
  }
}
</script>

<style scoped>
.promotion-achievements {
  padding: 20px;
}

.header-section {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.search-filter-section {
  margin-bottom: 20px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 4px;
}

.achievements-grid {
  margin-bottom: 20px;
}

.achievement-card {
  cursor: pointer;
  margin-bottom: 20px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.achievement-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0,0,0,0.15);
}

.card-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.achievement-icon {
  font-size: 18px;
  margin-right: 8px;
}

.achievement-type {
  font-size: 14px;
  color: #409EFF;
  font-weight: bold;
}

.achievement-title {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: bold;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.achievement-desc {
  margin: 0 0 10px 0;
  font-size: 13px;
  color: #606266;
  line-height: 1.4;
  height: 40px;
  overflow: hidden;
}

.achievement-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #909399;
}

.achievement-preview {
  text-align: center;
  margin-top: 10px;
}

.achievement-preview img {
  max-width: 100%;
  max-height: 120px;
  border-radius: 4px;
}

.video-preview, .report-preview, .document-preview {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 120px;
  background: #f5f7fa;
  border-radius: 4px;
  color: #909399;
  font-size: 24px;
}

.pagination-section {
  text-align: center;
  margin-top: 20px;
}

.achievement-detail .detail-header h3 {
  margin: 0 0 15px 0;
  color: #303133;
}

.detail-meta {
  display: flex;
  justify-content: space-between;
  color: #909399;
  font-size: 14px;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.detail-content p {
  line-height: 1.6;
  color: #606266;
  margin-bottom: 15px;
}

.detail-media img {
  width: 100%;
  border-radius: 4px;
}

.detail-media video {
  width: 100%;
  border-radius: 4px;
}

.detail-stats {
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #eee;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 4px;
}

.stat-item i {
  font-size: 20px;
  margin-bottom: 5px;
  color: #409EFF;
}
</style>