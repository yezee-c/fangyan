<template>
  <div class="putonghua-home">
    <!-- 顶部导航栏 -->
    <nav class="top-nav">
      <div class="nav-content">
        <div class="nav-logo">
          <i class="el-icon-reading"></i>
          <span>普通话学习推广平台</span>
        </div>
        <div class="nav-links">
          <a href="#hero" class="nav-link">首页</a>
          <a href="#features" class="nav-link">功能</a>
          <a href="#courses" class="nav-link">课程</a>
          <el-button type="text" @click="goToLogin">登录后台</el-button>
        </div>
      </div>
    </nav>

    <!-- Hero Section 大横幅 -->
    <section id="hero" class="hero-section">
      <div class="hero-overlay"></div>
      <div class="hero-content">
        <h1 class="hero-title">让普通话成为你交流的桥梁</h1>
        <p class="hero-subtitle">在线学习 · 智能评测 · 科普推广</p>
        <div class="hero-buttons">
          <el-button type="primary" size="large" round @click="goToLearning">
            <i class="el-icon-reading"></i> 开始学习普通话
          </el-button>
          <el-button type="success" size="large" round plain @click="goToTest">
            <i class="el-icon-microphone"></i> 在线测试发音
          </el-button>
        </div>
      </div>
    </section>

    <!-- 功能模块介绍 -->
    <section id="features" class="features-section">
      <div class="section-header">
        <h2>核心功能</h2>
        <p>多维度学习方式，助您快速提升普通话水平</p>
      </div>
      <div class="features-grid">
        <div 
          v-for="(feature, index) in features" 
          :key="index"
          class="feature-card"
          @click="goToFeature(feature.path)"
        >
          <div class="feature-icon" :style="{ background: feature.color }">
            <i :class="feature.icon"></i>
          </div>
          <h3>{{ feature.title }}</h3>
          <p class="feature-desc">{{ feature.desc }}</p>
          <ul class="feature-list">
            <li v-for="(item, i) in feature.items" :key="i">{{ item }}</li>
          </ul>
        </div>
      </div>
    </section>

    <!-- 学习路径 -->
    <section class="learning-path-section">
      <div class="section-header">
        <h2>学习路径</h2>
        <p>五步进阶，系统提升普通话能力</p>
      </div>
      <div class="path-container">
        <div 
          v-for="(step, index) in learningSteps" 
          :key="index"
          class="path-step"
        >
          <div class="step-number">{{ index + 1 }}</div>
          <div class="step-content">
            <h4>{{ step.title }}</h4>
            <p>{{ step.desc }}</p>
          </div>
          <div v-if="index < learningSteps.length - 1" class="step-arrow">
            <i class="el-icon-right"></i>
          </div>
        </div>
      </div>
    </section>

    <!-- 推荐课程 -->
    <section id="courses" class="courses-section">
      <div class="section-header">
        <h2>推荐课程</h2>
        <p>精选优质课程，助力高效学习</p>
      </div>
      <div class="courses-grid">
        <div 
          v-for="(course, index) in courses" 
          :key="index"
          class="course-card"
        >
          <div class="course-image">
            <img :src="course.image" :alt="course.title">
            <div class="course-tag">{{ course.tag }}</div>
          </div>
          <div class="course-info">
            <h4>{{ course.title }}</h4>
            <p>{{ course.desc }}</p>
            <div class="course-meta">
              <span><i class="el-icon-video-camera"></i> {{ course.lessons }}课时</span>
              <span><i class="el-icon-user"></i> {{ course.students }}人学习</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 平台数据 -->
    <section class="stats-section">
      <div class="stats-grid">
        <div 
          v-for="(stat, index) in stats" 
          :key="index"
          class="stat-item"
        >
          <div class="stat-icon" :style="{ background: stat.color }">
            <i :class="stat.icon"></i>
          </div>
          <div class="stat-number">{{ stat.number }}</div>
          <div class="stat-label">{{ stat.label }}</div>
        </div>
      </div>
    </section>

    <!-- Footer -->
    <footer class="footer-section">
      <div class="footer-content">
        <div class="footer-col">
          <h4>关于我们</h4>
          <p>本网站致力于普通话推广学习与公益宣传</p>
        </div>
        <div class="footer-col">
          <h4>常用链接</h4>
          <ul>
            <li><a href="#">学习中心</a></li>
            <li><a href="#">在线测评</a></li>
            <li><a href="#">推广活动</a></li>
          </ul>
        </div>
        <div class="footer-col">
          <h4>联系我们</h4>
          <ul>
            <li><i class="el-icon-phone"></i> 400-123-4567</li>
            <li><i class="el-icon-message"></i> info@putonghua.cn</li>
          </ul>
        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; 2025 普通话学习推广平台 | 用于普通话推广学习与公益宣传用途</p>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'Index',
  data() {
    return {
      features: [
        {
          title: '普通话学习课程',
          icon: 'el-icon-reading',
          color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
          desc: '系统化学习普通话发音技巧',
          items: ['视频课程', '字词朗读', '日常语音示例'],
          path: '/pth/student/course'
        },
        {
          title: 'AI语音发音评测',
          icon: 'el-icon-microphone',
          color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
          desc: '智能评测发音准确度',
          items: ['声母韵母评分', '声调准确度', '可视化波形反馈'],
          path: '/pth/student/test'
        },
        {
          title: '分级测试模拟',
          icon: 'el-icon-medal',
          color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
          desc: '模拟普通话水平测试',
          items: ['PSC模拟测试', '自动评分', '错误分析报告'],
          path: '/pth/student/exam'
        },
        {
          title: '推广活动资源',
          icon: 'el-icon-folder-opened',
          color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
          desc: '丰富的学习资源下载',
          items: ['宣传资料', '海报音视频', '活动报名'],
          path: '/pth/activity'
        },
        {
          title: '方言词典',
          icon: 'el-icon-notebook-2',
          color: 'linear-gradient(135deg, #a8edea 0%, #fed6e3 100%)',
          desc: '大别山地区方言词汇查询',
          items: ['湖北黄冈', '河南信阳', '安徽六安', '安徽岳西'],
          path: '/pth/dialect/dictionary'
        }
      ],
      learningSteps: [
        { title: '学习基础发音', desc: '掌握声母、韵母、声调' },
        { title: '跟读与练习', desc: '通过跟读提升发音准确度' },
        { title: 'AI智能测评', desc: '获取专业发音评分' },
        { title: '模拟考试', desc: '检验学习成果' },
        { title: '领取学习报告', desc: '获得详细学习分析' }
      ],
      courses: [
        {
          title: '声母韵母基础',
          desc: '零基础入门，系统学习发音方法',
          image: 'https://via.placeholder.com/300x200/667eea/ffffff?text=声母韵母',
          tag: '基础',
          lessons: 24,
          students: 1543
        },
        {
          title: '声调训练进阶',
          desc: '掌握四声变化，提升表达能力',
          image: 'https://via.placeholder.com/300x200/f5576c/ffffff?text=声调训练',
          tag: '进阶',
          lessons: 18,
          students: 982
        },
        {
          title: '常见发音难点',
          desc: 'n/l、z/zh/j不分？手把手教你',
          image: 'https://via.placeholder.com/300x200/4facfe/ffffff?text=发音难点',
          tag: '专项',
          lessons: 12,
          students: 756
        },
        {
          title: 'PSC考试冲刺',
          desc: '模拟真题训练，快速提分',
          image: 'https://via.placeholder.com/300x200/43e97b/ffffff?text=考试冲刺',
          tag: '冲刺',
          lessons: 16,
          students: 1234
        }
      ],
      stats: [
        {
          icon: 'el-icon-data-line',
          number: '12,543',
          label: '累计测评次数',
          color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
        },
        {
          icon: 'el-icon-user',
          number: '8,900+',
          label: '学习用户',
          color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)'
        },
        {
          icon: 'el-icon-trophy',
          number: '98%',
          label: '用户好评率',
          color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)'
        },
        {
          icon: 'el-icon-video-camera',
          number: '200+',
          label: '精品课程',
          color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)'
        }
      ]
    }
  },
  mounted() {
    console.log('普通话推广首页已加载')
  },
  methods: {
    goToLogin() {
      this.$router.push('/login')
    },
    goToLearning() {
      // 如果用户已登录，跳转到后台学习页面，否则跳转到登录页
      this.$message.info('请先登录后台后使用学习功能')
      this.$router.push('/login')
    },
    goToTest() {
      this.$message.info('请先登录后台后使用测试功能')
      this.$router.push('/login')
    },
    goToFeature(path) {
      this.$message.info('请先登录后台后使用相关功能')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
/* 全局样式 */
.putonghua-home {
  width: 100%;
  min-height: 100vh;
  background: #f5f7fa;
}

/* 顶部导航栏 */
.top-nav {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  backdrop-filter: blur(10px);
}

.nav-content {
  max-width: 1200px;
  margin: 0 auto;
  height: 100%;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.nav-logo {
  display: flex;
  align-items: center;
  font-size: 20px;
  font-weight: bold;
  color: #667eea;
}

.nav-logo i {
  font-size: 28px;
  margin-right: 10px;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 30px;
}

.nav-link {
  color: #303133;
  text-decoration: none;
  font-size: 16px;
  transition: color 0.3s;
}

.nav-link:hover {
  color: #667eea;
}

.section-header {
  text-align: center;
  margin-bottom: 50px;
}

.section-header h2 {
  font-size: 36px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 15px;
}

.section-header p {
  font-size: 16px;
  color: #909399;
}

/* Hero Section */
.hero-section {
  position: relative;
  height: 600px;
  margin-top: 60px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  overflow: hidden;
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.1);
}

.hero-content {
  position: relative;
  z-index: 2;
  text-align: center;
  padding: 0 20px;
}

.hero-title {
  font-size: 56px;
  font-weight: bold;
  margin-bottom: 20px;
}

.hero-subtitle {
  font-size: 24px;
  margin-bottom: 40px;
  opacity: 0.95;
}

.hero-buttons .el-button {
  margin: 0 10px;
  padding: 15px 40px;
  font-size: 18px;
}

/* 功能模块 */
.features-section {
  padding: 80px 20px;
  background: #fff;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.feature-card {
  text-align: center;
  padding: 40px 20px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  transition: all 0.3s;
}

.feature-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.feature-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  color: #fff;
}

.feature-card h3 {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 10px;
}

.feature-desc {
  font-size: 14px;
  color: #606266;
  margin-bottom: 15px;
}

.feature-list {
  list-style: none;
  padding: 0;
  text-align: left;
}

.feature-list li {
  padding: 5px 0;
  color: #909399;
  font-size: 13px;
}

.feature-list li:before {
  content: '✓';
  color: #67c23a;
  font-weight: bold;
  margin-right: 8px;
}

/* 学习路径 */
.learning-path-section {
  padding: 80px 20px;
  background: #f5f7fa;
}

.path-container {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  max-width: 1200px;
  margin: 0 auto;
  gap: 20px;
}

.path-step {
  display: flex;
  align-items: center;
  background: #fff;
  padding: 20px 30px;
  border-radius: 10px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.step-number {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: bold;
  margin-right: 20px;
  flex-shrink: 0;
}

.step-content h4 {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.step-content p {
  font-size: 13px;
  color: #909399;
  margin: 0;
}

.step-arrow {
  font-size: 24px;
  color: #667eea;
  margin: 0 15px;
}

/* 课程 */
.courses-section {
  padding: 80px 20px;
  background: #fff;
}

.courses-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.course-card {
  background: #fff;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s;
}

.course-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.course-image {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.course-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.course-tag {
  position: absolute;
  top: 10px;
  right: 10px;
  background: rgba(255, 255, 255, 0.95);
  padding: 5px 15px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: bold;
  color: #667eea;
}

.course-info {
  padding: 20px;
}

.course-info h4 {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 10px;
}

.course-info p {
  font-size: 14px;
  color: #606266;
  margin-bottom: 15px;
  line-height: 1.6;
}

.course-meta {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  color: #909399;
}

/* 平台数据 */
.stats-section {
  padding: 80px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.stat-item {
  text-align: center;
  padding: 40px 20px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  backdrop-filter: blur(10px);
}

.stat-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  color: #fff;
}

.stat-number {
  font-size: 42px;
  font-weight: bold;
  margin-bottom: 10px;
}

.stat-label {
  font-size: 16px;
  opacity: 0.9;
}

/* Footer */
.footer-section {
  background: #2c3e50;
  color: #ecf0f1;
  padding: 60px 20px 20px;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 40px;
  max-width: 1200px;
  margin: 0 auto 40px;
}

.footer-col h4 {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #fff;
}

.footer-col p {
  font-size: 14px;
  line-height: 1.8;
  color: #bdc3c7;
}

.footer-col ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-col ul li {
  padding: 8px 0;
  font-size: 14px;
  color: #bdc3c7;
}

.footer-col a {
  color: #bdc3c7;
  text-decoration: none;
  transition: color 0.3s;
}

.footer-col a:hover {
  color: #fff;
}

.footer-bottom {
  text-align: center;
  padding-top: 30px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  max-width: 1200px;
  margin: 0 auto;
}

.footer-bottom p {
  font-size: 13px;
  color: #95a5a6;
  margin: 0;
}

/* 响应式 */
@media (max-width: 768px) {
  .hero-title {
    font-size: 36px;
  }
  
  .hero-subtitle {
    font-size: 18px;
  }
  
  .hero-buttons .el-button {
    margin: 10px 5px;
    padding: 12px 30px;
    font-size: 16px;
  }
  
  .section-header h2 {
    font-size: 28px;
  }
  
  .path-container {
    flex-direction: column;
  }
  
  .step-arrow {
    transform: rotate(90deg);
  }
}
</style>
