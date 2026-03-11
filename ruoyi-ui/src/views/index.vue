<template>
  <div class="dashboard-container">
    <!-- 背景层 -->
    <div class="background-overlay">
      <!-- 飘动的装饰元素 -->
      <div class="float-decoration">
        <span class="float-item" style="left: 10%; animation-delay: 0s;">📚</span>
        <span class="float-item" style="left: 25%; animation-delay: 2s;">🎤</span>
        <span class="float-item" style="left: 40%; animation-delay: 4s;">✨</span>
        <span class="float-item" style="left: 55%; animation-delay: 1s;">🎵</span>
        <span class="float-item" style="left: 70%; animation-delay: 3s;">📖</span>
        <span class="float-item" style="left: 85%; animation-delay: 5s;">💬</span>
      </div>

      <!-- 右侧装饰图案 -->
      <div class="side-decoration right-deco">
        <div class="deco-item deco-star">⭐</div>
        <div class="deco-item deco-heart">💙</div>
        <div class="deco-item deco-book">📕</div>
      </div>

      <!-- 左侧装饰图案 -->
      <div class="side-decoration left-deco">
        <div class="deco-item deco-music">🎼</div>
        <div class="deco-item deco-smile">😊</div>
        <div class="deco-item deco-pen">✏️</div>
      </div>
    </div>

    <!-- 内容层 -->
    <div class="content-wrapper">
      <!-- 顶部轮播图 -->
      <div class="banner-section">
        <el-carousel height="700px" :interval="5000" arrow="always">
          <el-carousel-item v-for="(banner, index) in banners" :key="index">
            <div class="banner-item" :style="{ backgroundImage: `url(${banner.image})` }">
              <div class="banner-overlay"></div>
              <div class="banner-content">
                <h2>{{ banner.title }}</h2>
                <p>{{ banner.subtitle }}</p>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>

        <!-- 搜索框 -->
        <div class="search-box">
          <el-input
            v-model="searchText"
            placeholder="搜索普通话相关内容..."
            class="search-input"
            @keyup.enter.native="handleSearch"
          >
            <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
          </el-input>
        </div>
      </div>

      <!-- 标题区域 -->
      <div class="hero-section">
        <h1 class="main-title">让普通话成为你交流的桥梁</h1>
        <p class="subtitle">在线学习 · 智能评测 · 科普推广</p>
      </div>

      <!-- 功能模块 -->
      <el-row :gutter="30" class="features-row">
        <!-- 1. 普通话学习课程 -->
          <el-col :xs="24" :sm="12" :lg="6">
          <el-card shadow="hover" class="feature-card card-purple" @click.native="goTo('/pth/course')">
            <div class="card-decoration">
              <span class="corner-deco top-left">📚</span>
              <span class="corner-deco top-right">✨</span>
            </div>
            <div class="card-content">
              <div class="icon-wrapper" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                <i class="el-icon-reading"></i>
              </div>
              <h3>普通话学习课程</h3>
              <ul class="feature-list">
                <li>视频课程</li>
                <li>字词朗读</li>
                <li>日常语音示例</li>
              </ul>
              <el-button type="primary" size="small" round>进入学习</el-button>
            </div>
          </el-card>
        </el-col>

        <!-- 2. AI 语音发音评测 -->
        <el-col :xs="24" :sm="12" :lg="6">
          <el-card shadow="hover" class="feature-card card-pink" @click.native="goTo('/pth/evaluation')">
            <div class="card-decoration">
              <span class="corner-deco top-left">🎤</span>
              <span class="corner-deco top-right">🎵</span>
            </div>
            <div class="card-content">
              <div class="icon-wrapper" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                <i class="el-icon-microphone"></i>
              </div>
              <h3>AI 语音发音评测</h3>
              <ul class="feature-list">
                <li>上传录音即可评测</li>
                <li>声母韵母声调评分</li>
                <li>可视化波形反馈</li>
              </ul>
              <el-button type="danger" size="small" round>开始测评</el-button>
            </div>
          </el-card>
        </el-col>

        <!-- 3. 分级测试（模拟PSC） -->
        <el-col :xs="24" :sm="12" :lg="6">
          <el-card shadow="hover" class="feature-card card-blue" @click.native="goTo('/pth/exam')">
            <div class="card-decoration">
              <span class="corner-deco top-left">🏆</span>
              <span class="corner-deco top-right">⭐</span>
            </div>
            <div class="card-content">
              <div class="icon-wrapper" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                <i class="el-icon-medal"></i>
              </div>
              <h3>分级测试（模拟PSC）</h3>
              <ul class="feature-list">
                <li>模拟普通话水平测试</li>
                <li>自动给分</li>
                <li>提供错误分析</li>
              </ul>
              <el-button type="info" size="small" round>模拟考试</el-button>
            </div>
          </el-card>
        </el-col>

        <!-- 4. 推广活动与资源 -->
        <el-col :xs="24" :sm="12" :lg="6">
          <el-card shadow="hover" class="feature-card card-green" @click.native="goTo('/pth/resources')">
            <div class="card-decoration">
              <span class="corner-deco top-left">📁</span>
              <span class="corner-deco top-right">💡</span>
            </div>
            <div class="card-content">
              <div class="icon-wrapper" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                <i class="el-icon-folder-opened"></i>
              </div>
              <h3>推广活动与资源</h3>
              <ul class="feature-list">
                <li>宣传资料</li>
                <li>海报、音视频资源</li>
                <li>活动报名链接</li>
              </ul>
              <el-button type="success" size="small" round>查看资源</el-button>
            </div>
          </el-card>
        </el-col>

        <!-- 5. 方言词典 -->
        <el-col :xs="24" :sm="12" :lg="6">
          <el-card shadow="hover" class="feature-card card-yellow" @click.native="goTo('/pth/dialect/dictionary')">
            <div class="card-decoration">
              <span class="corner-deco top-left">📚</span>
              <span class="corner-deco top-right">💬</span>
            </div>
            <div class="card-content">
              <div class="icon-wrapper" style="background: linear-gradient(135deg, #f7971e 0%, #ffd200 100%);">
                <i class="el-icon-notebook-2"></i>
              </div>
              <h3>方言词典</h3>
              <ul class="feature-list">
                <li>大别山地区方言词汇</li>
                <li>河南方言</li>
                <li>湖北方言</li>
                <li>安徽方言</li>
              </ul>
              <el-button type="warning" size="small" round>查询方言</el-button>
            </div>
          </el-card>
        </el-col>

        <!-- 6. 推广成果 -->
        <el-col :xs="24" :sm="12" :lg="6">
          <el-card shadow="hover" class="feature-card card-purple" @click.native="goTo('/pth/promotion/achievements')">
            <div class="card-decoration">
              <span class="corner-deco top-left">🏆</span>
              <span class="corner-deco top-right">📈</span>
            </div>
            <div class="card-content">
              <div class="icon-wrapper" style="background: linear-gradient(135deg, #8e2de2 0%, #4a00e0 100%);">
                <i class="el-icon-trophy"></i>
              </div>
              <h3>推广成果</h3>
              <ul class="feature-list">
                <li>推广活动成果展示</li>
                <li>图片、文档、视频</li>
                <li>数据分析报告</li>
                <li>学习者反馈</li>
              </ul>
              <el-button type="primary" size="small" round>查看成果</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 快捷入口 -->
      <div class="quick-links">
        <h2>快捷入口</h2>
        <el-row :gutter="20">
          <el-col :xs="12" :sm="6">
            <div class="quick-item" @click="goToGallery">
              <i class="el-icon-user"></i>
              <span>人物画廊</span>
            </div>
          </el-col>
          <el-col :xs="12" :sm="6">
            <div class="quick-item" @click="goTo('/pth/student/game')">
              <i class="el-icon-trophy"></i>
              <span>学习小游戏</span>
            </div>
          </el-col>
          <el-col :xs="12" :sm="6">
            <div class="quick-item" @click="goTo('/system/user')">
              <i class="el-icon-setting"></i>
              <span>系统管理</span>
            </div>
          </el-col>
          <el-col :xs="12" :sm="6">
            <div class="quick-item" @click="goTo('/system/file')">
              <i class="el-icon-document"></i>
              <span>文件管理</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 名人介绍转播 -->
      <div class="famous-section">
        <h2 class="section-title">普通话推广名人</h2>
        <p class="section-subtitle">了解他们的故事，感受语言的魅力</p>
        <el-carousel :interval="4000" type="card" height="320px" arrow="always">
          <el-carousel-item v-for="(person, index) in famousPeople" :key="index">
            <div class="famous-card">
              <div class="famous-image" :style="{ backgroundImage: `url(${person.image})` }">
                <div class="famous-overlay"></div>
              </div>
              <div class="famous-info">
                <h3>{{ person.name }}</h3>
                <p class="famous-title">{{ person.title }}</p>
                <p class="famous-desc">{{ person.desc }}</p>
                <el-button type="primary" size="small" @click="viewDetail(person)">了解更多</el-button>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Index',
  data() {
    return {
      searchText: '',
      banners: [
        {
          image: require('@/assets/images/banner/banner1.jpg'),
          title: '普通话推广周',
          subtitle: '推广普通话，奋进新征程'
        },
        {
          image: require('@/assets/images/banner/banner2.jpg'),
          title: 'AI智能评测',
          subtitle: '科技赋能语言学习'
        },
        {
          image: require('@/assets/images/banner/banner3.jpg'),
          title: '在线学习平台',
          subtitle: '随时随地，轻松学普通话'
        }
      ],
      famousPeople: [
        {
          name: '陈嘉庚',
          title: '著名主持人、普通话推广大使',
          desc: '他用标准的普通话和优雅的主持风格，影响了几代人。',
          image: require('@/assets/images/famous/person1.jpg')
        },
        {
          name: '董卿',
          title: '著名主持人、作家',
          desc: '以文化内涵和普通话魅力，传播中华文化。',
          image: require('@/assets/images/famous/person2.jpg')
        },
        {
          name: '朱军',
          title: '著名配音演员',
          desc: '用声音赋予角色生命，展现普通话的艺术魅力。',
          image: require('@/assets/images/famous/person3.jpg')
        },
        {
          name: '张泽群',
          title: '普通话教育专家',
          desc: '多年致力于普通话教学研究，培养无数优秀人才。',
          image: require('@/assets/images/famous/person4.jpg')
        }
      ]
    }
  },
  methods: {
    goTo(path) {
      this.$router.push(path)
    },
    goToGallery() {
      // 直接跳转到外部的人物画廊应用
      window.location.href = 'http://localhost:8082/characters'
    },
    handleSearch() {
      if (this.searchText.trim()) {
        // 在新窗口打开百度搜索
        const searchUrl = `https://www.baidu.com/s?wd=普通话 ${this.searchText}`
        window.open(searchUrl, '_blank')
      } else {
        this.$message.warning('请输入搜索关键词')
      }
    },
    viewDetail(person) {
      this.$message.info(`查看${person.name}的详细信息`)
      // 可以跳转到详情页面
    }
  }
}
</script>

<style scoped>
/* 容器 */
.dashboard-container {
  position: relative;
  min-height: calc(100vh - 50px);
  overflow: hidden;
}

/* 背景层 */
.background-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  /* 白色背景 */
  background: #ffffff;
  z-index: 0;
}

/* 背景装饰元素 */
.background-overlay::before {
  content: '';
  position: absolute;
  top: -10%;
  right: -5%;
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(102, 126, 234, 0.1) 0%, transparent 70%);
  border-radius: 50%;
  animation: float 20s ease-in-out infinite;
}

.background-overlay::after {
  content: '';
  position: absolute;
  bottom: -5%;
  left: -5%;
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, rgba(118, 75, 162, 0.08) 0%, transparent 70%);
  border-radius: 50%;
  animation: float 25s ease-in-out infinite reverse;
}

/* 飘动的装饰元素 */
.float-decoration {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow: hidden;
  pointer-events: none;
}

.float-item {
  position: absolute;
  top: -50px;
  font-size: 30px;
  opacity: 0.6;
  animation: floatDown 15s linear infinite;
}

@keyframes floatDown {
  0% {
    top: -50px;
    transform: translateX(0) rotate(0deg);
    opacity: 0;
  }
  10% {
    opacity: 0.6;
  }
  90% {
    opacity: 0.6;
  }
  100% {
    top: 100%;
    transform: translateX(100px) rotate(360deg);
    opacity: 0;
  }
}

/* 侧边装饰 */
.side-decoration {
  position: absolute;
  top: 30%;
  display: flex;
  flex-direction: column;
  gap: 40px;
  pointer-events: none;
}

.right-deco {
  right: 20px;
  animation: slideInRight 1s ease-out;
}

.left-deco {
  left: 20px;
  animation: slideInLeft 1s ease-out;
}

.deco-item {
  font-size: 40px;
  opacity: 0.4;
  transition: all 0.3s;
}

.deco-star {
  animation: rotate 4s linear infinite;
}

.deco-heart {
  animation: pulse 2s ease-in-out infinite;
}

.deco-book {
  animation: swing 3s ease-in-out infinite;
}

.deco-music {
  animation: bounce 2s ease-in-out infinite;
}

.deco-smile {
  animation: wiggle 3s ease-in-out infinite;
}

.deco-pen {
  animation: shake 2.5s ease-in-out infinite;
}

@keyframes slideInRight {
  from {
    right: -100px;
    opacity: 0;
  }
  to {
    right: 20px;
    opacity: 0.4;
  }
}

@keyframes slideInLeft {
  from {
    left: -100px;
    opacity: 0;
  }
  to {
    left: 20px;
    opacity: 0.4;
  }
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
}

@keyframes swing {
  0%, 100% {
    transform: rotate(-10deg);
  }
  50% {
    transform: rotate(10deg);
  }
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
}

@keyframes wiggle {
  0%, 100% {
    transform: rotate(0deg);
  }
  25% {
    transform: rotate(5deg);
  }
  75% {
    transform: rotate(-5deg);
  }
}

@keyframes shake {
  0%, 100% {
    transform: translateX(0);
  }
  25% {
    transform: translateX(-5px);
  }
  75% {
    transform: translateX(5px);
  }
}

@keyframes float {
  0%, 100% {
    transform: translate(0, 0) scale(1);
  }
  50% {
    transform: translate(30px, 30px) scale(1.1);
  }
}

/* 内容层 */
.content-wrapper {
  position: relative;
  z-index: 1;
  padding: 20px;
}

/* 添加一些装饰点 */
.content-wrapper::before {
  content: '';
  position: absolute;
  top: 20%;
  left: 10%;
  width: 8px;
  height: 8px;
  background: rgba(102, 126, 234, 0.3);
  border-radius: 50%;
  box-shadow:
    50px 80px 0 rgba(102, 126, 234, 0.2),
    -30px 120px 0 rgba(118, 75, 162, 0.2),
    100px -20px 0 rgba(240, 147, 251, 0.2),
    -80px -40px 0 rgba(67, 198, 58, 0.2);
  animation: twinkle 3s ease-in-out infinite;
}

@keyframes twinkle {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.3;
  }
}

/* 顶部轮播图 */
.banner-section {
  position: relative;
  margin-bottom: 40px;
}

/* 顶部轮播图 */
.banner-section {
  position: relative;
  margin-bottom: 40px;
}

.banner-item {
  width: 100%;
  height: 700px;
  background-size: cover;
  background-position: center top; /* 调整为顶部对齐 */
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.banner-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
}

.banner-content {
  position: relative;
  z-index: 1;
  text-align: center;
  color: #fff;
}

.banner-content h2 {
  font-size: 48px;
  margin-bottom: 20px;
  text-shadow: 2px 2px 8px rgba(0, 0, 0, 0.5);
}

.banner-content p {
  font-size: 24px;
  text-shadow: 1px 1px 4px rgba(0, 0, 0, 0.5);
}

/* 搜索框 */
.search-box {
  position: absolute;
  bottom: 30px;
  left: 50%;
  transform: translateX(-50%);
  width: 600px;
  z-index: 10;
}

.search-input {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 30px;
  overflow: hidden;
}

.search-input >>> .el-input__inner {
  background: transparent;
  border: none;
  font-size: 16px;
  padding-left: 25px;
}

.search-input >>> .el-input-group__append {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: #fff;
}

/* 标题区域 */
.hero-section {
  text-align: center;
  margin-bottom: 50px;
}

.main-title {
  font-size: 48px;
  font-weight: bold;
  /* 渐变文字颜色 */
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 20px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
  letter-spacing: 2px;
}

.subtitle {
  font-size: 22px;
  color: #5a5a5a;
  margin: 0;
  font-weight: 500;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
}

/* 功能卡片 */
.features-row {
  margin-bottom: 50px;
}

.feature-card {
  height: 100%;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.5);
  overflow: hidden;
  position: relative;
}

/* 不同颜色的卡片 */
.card-purple {
  background: linear-gradient(145deg, #e0c3fc 0%, #8ec5fc 100%);
}

.card-pink {
  background: linear-gradient(145deg, #fbc2eb 0%, #a6c1ee 100%);
}

.card-blue {
  background: linear-gradient(145deg, #a1c4fd 0%, #c2e9fb 100%);
}

.card-green {
  background: linear-gradient(145deg, #d4fc79 0%, #96e6a1 100%);
}

/* 悬停时增强颜色 */
.card-purple:hover {
  background: linear-gradient(145deg, #d8b4fe 0%, #7fb3fc 100%);
}

.card-pink:hover {
  background: linear-gradient(145deg, #f9aee0 0%, #98afea 100%);
}

.card-blue:hover {
  background: linear-gradient(145deg, #8fb6fd 0%, #b0dffb 100%);
}

.card-green:hover {
  background: linear-gradient(145deg, #c8fc6a 0%, #88dc95 100%);
}

/* 卡片装饰 */
.card-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.corner-deco {
  position: absolute;
  font-size: 28px;
  opacity: 0.5;
  transition: all 0.4s;
}

.top-left {
  top: 15px;
  left: 15px;
  animation: bounceTopLeft 3s ease-in-out infinite;
}

.top-right {
  top: 15px;
  right: 15px;
  animation: bounceTopRight 3s ease-in-out infinite 1.5s;
}

@keyframes bounceTopLeft {
  0%, 100% {
    transform: translate(0, 0) rotate(0deg);
  }
  50% {
    transform: translate(-5px, -5px) rotate(-10deg);
  }
}

@keyframes bounceTopRight {
  0%, 100% {
    transform: translate(0, 0) rotate(0deg);
  }
  50% {
    transform: translate(5px, -5px) rotate(10deg);
  }
}

.feature-card:hover .corner-deco {
  opacity: 0.8;
  transform: scale(1.2);
}

/* 卡片光泽效果 */
.feature-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent);
  transition: left 0.5s;
}

.feature-card:hover::before {
  left: 100%;
}

.feature-card:hover {
  transform: translateY(-15px) scale(1.02);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.4);
  border-color: rgba(255, 255, 255, 0.3);
}

/* 点击时的动画效果 */
.feature-card:active {
  transform: translateY(-12px) scale(0.98);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.3);
}

.card-content {
  text-align: center;
  padding: 35px 25px;
  position: relative;
  z-index: 1;
}

.icon-wrapper {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.icon-wrapper i {
  font-size: 40px;
  color: #fff;
}

.card-content h3 {
  font-size: 18px;
  font-weight: bold;
  color: #2c3e50;
  margin: 0 0 20px 0;
  text-shadow: 0 1px 2px rgba(255, 255, 255, 0.5);
}

.feature-list {
  list-style: none;
  padding: 0;
  margin: 0 0 25px 0;
  text-align: left;
}

.feature-list li {
  padding: 10px 0;
  color: #34495e;
  font-size: 14px;
  position: relative;
  padding-left: 25px;
  transition: all 0.3s;
}

.feature-list li:hover {
  color: #2c3e50;
  padding-left: 30px;
}

.feature-list li:before {
  content: '✓';
  position: absolute;
  left: 5px;
  color: #67c23a;
  font-weight: bold;
  font-size: 16px;
}

.card-content .el-button {
  width: 120px;
}

/* 快捷入口 */
.quick-links {
  background: linear-gradient(145deg, #ffeaa7 0%, #fdcb6e 100%);
  border-radius: 16px;
  padding: 35px;
  border: 1px solid rgba(255, 255, 255, 0.5);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.quick-links h2 {
  text-align: center;
  font-size: 24px;
  color: #2c3e50;
  margin-bottom: 35px;
  text-shadow: 0 1px 2px rgba(255, 255, 255, 0.5);
}

.quick-item {
  background: linear-gradient(145deg, #ffffff 0%, #ffeaa7 100%);
  border-radius: 12px;
  padding: 30px;
  text-align: center;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  border: 2px solid rgba(255, 255, 255, 0.3);
  position: relative;
  overflow: hidden;
}

/* 快捷入口光泽效果 */
.quick-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent);
  transition: left 0.5s;
}

.quick-item:hover::before {
  left: 100%;
}

.quick-item:hover {
  transform: translateY(-8px) scale(1.05);
  border-color: rgba(102, 126, 234, 0.8);
  box-shadow: 0 12px 25px rgba(102, 126, 234, 0.4);
  background: linear-gradient(145deg, #667eea 0%, #764ba2 100%);
}

.quick-item:active {
  transform: translateY(-5px) scale(1.02);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
}

.quick-item i {
  font-size: 45px;
  color: #34495e;
  display: block;
  margin-bottom: 12px;
  transition: all 0.3s;
}

.quick-item:hover i {
  color: #ffffff;
  transform: scale(1.1) rotate(5deg);
}

.quick-item span {
  font-size: 16px;
  color: #2c3e50;
  font-weight: 500;
  display: block;
  transition: all 0.3s;
}

.quick-item:hover span {
  color: #ffffff;
  font-weight: 600;
}

/* 名人介绍 */
.famous-section {
  margin-top: 60px;
  padding: 40px 0;
}

.section-title {
  text-align: center;
  font-size: 32px;
  color: #2c3e50;
  margin-bottom: 10px;
  font-weight: bold;
}

.section-subtitle {
  text-align: center;
  font-size: 16px;
  color: #7f8c8d;
  margin-bottom: 40px;
}

.famous-card {
  height: 100%;
  background: linear-gradient(145deg, #ecf0f1 0%, #bdc3c7 100%);
  border-radius: 12px; /* 减小圆角 */
  overflow: hidden;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
  transition: all 0.3s;
  display: flex;
  flex-direction: column; /* 改为上下布局 */
}

.famous-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.25);
}

.famous-image {
  width: 100%; /* 占100%宽度 */
  height: 60%; /* 调整图片高度比例 */
  background-size: cover;
  background-position: center; /* 改为中心对齐，更好地显示人物 */
  background-repeat: no-repeat; /* 防止图片重复 */
  position: relative;
}

.famous-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to bottom, rgba(0, 0, 0, 0.3) 0%, transparent 100%);
}

.famous-info {
  width: 100%; /* 占100%宽度 */
  height: 40%; /* 增加信息区域高度 */
  padding: 12px; /* 减小内边距 */
  text-align: center; /* 居中对齐文字 */
  display: flex;
  flex-direction: column;
  justify-content: flex-start; /* 从顶部开始排列，确保文字可见 */
  overflow: hidden; /* 防止内容溢出 */
}

.famous-info h3 {
  font-size: 18px; /* 进一步减小字体大小 */
  color: #2c3e50;
  margin: 3px 0; /* 调整间距 */
  font-weight: bold;
  white-space: nowrap; /* 防止换行 */
  overflow: hidden; /* 隐藏溢出文本 */
  text-overflow: ellipsis; /* 显示省略号 */
}

.famous-title {
  font-size: 12px; /* 进一步减小字体大小 */
  color: #7f8c8d;
  margin: 3px 0; /* 调整间距 */
  white-space: nowrap; /* 防止换行 */
  overflow: hidden; /* 隐藏溢出文本 */
  text-overflow: ellipsis; /* 显示省略号 */
}

.famous-desc {
  font-size: 11px; /* 进一步减小字体大小 */
  color: #34495e;
  line-height: 1.3; /* 调整行高 */
  margin: 3px 0; /* 调整间距 */
  flex: 1; /* 占据剩余空间 */
  display: -webkit-box;
  -webkit-line-clamp: 3; /* 增加到最多显示3行 */
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 轮播箭头样式 */
.el-carousel >>> .el-carousel__arrow {
  background: rgba(0, 0, 0, 0.5);
  color: #fff;
  font-size: 20px;
}

.el-carousel >>> .el-carousel__arrow:hover {
  background: rgba(0, 0, 0, 0.7);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-title {
    font-size: 28px;
  }

  .subtitle {
    font-size: 16px;
  }

  .content-wrapper {
    padding: 20px 10px;
  }

  .feature-card {
    margin-bottom: 20px;
  }
}
</style>
