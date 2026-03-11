<template>
  <div class="putonghua-home">
    <!-- Hero Section 横幅区域 -->
    <section class="hero-section">
      <div class="hero-overlay"></div>
      <div class="hero-content">
        <h1 class="hero-title" data-aos="fade-up">让普通话成为你交流的桥梁</h1>
        <p class="hero-subtitle" data-aos="fade-up" data-aos-delay="200">
          在线学习 · 智能评测 · 科普推广
        </p>
        <div class="hero-buttons" data-aos="fade-up" data-aos-delay="400">
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
    <section class="features-section">
      <div class="section-container">
        <h2 class="section-title" data-aos="fade-up">核心功能</h2>
        <p class="section-subtitle" data-aos="fade-up" data-aos-delay="100">
          四大模块助力普通话学习
        </p>
        
        <div class="features-grid">
          <div 
            v-for="(feature, index) in features" 
            :key="index"
            class="feature-card"
            data-aos="zoom-in"
            :data-aos-delay="index * 100"
            @click="goToFeature(feature.path)"
          >
            <div class="feature-icon" :style="{ background: feature.color }">
              <i :class="feature.icon"></i>
            </div>
            <h3 class="feature-title">{{ feature.title }}</h3>
            <p class="feature-desc">{{ feature.desc }}</p>
            <ul class="feature-list">
              <li v-for="item in feature.items" :key="item">{{ item }}</li>
            </ul>
          </div>
        </div>
      </div>
    </section>

    <!-- 学习路径导航 -->
    <section class="learning-path-section">
      <div class="section-container">
        <h2 class="section-title" data-aos="fade-up">学习路径</h2>
        <p class="section-subtitle" data-aos="fade-up" data-aos-delay="100">
          从零基础到专业水平，循序渐进
        </p>
        
        <div class="learning-path">
          <div 
            v-for="(step, index) in learningSteps" 
            :key="index"
            class="path-step"
            data-aos="fade-right"
            :data-aos-delay="index * 150"
          >
            <div class="step-number">{{ index + 1 }}</div>
            <div class="step-content">
              <h4 class="step-title">{{ step.title }}</h4>
              <p class="step-desc">{{ step.desc }}</p>
            </div>
            <div v-if="index < learningSteps.length - 1" class="step-arrow">
              <i class="el-icon-right"></i>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 推荐课程 -->
    <section class="courses-section">
      <div class="section-container">
        <h2 class="section-title" data-aos="fade-up">推荐课程</h2>
        <p class="section-subtitle" data-aos="fade-up" data-aos-delay="100">
          精选优质学习资源
        </p>
        
        <div class="courses-grid">
          <div 
            v-for="(course, index) in courses" 
            :key="index"
            class="course-card"
            data-aos="flip-left"
            :data-aos-delay="index * 100"
          >
            <div class="course-image">
              <img :src="course.image" :alt="course.title">
              <div class="course-tag">{{ course.tag }}</div>
            </div>
            <div class="course-info">
              <h4 class="course-title">{{ course.title }}</h4>
              <p class="course-desc">{{ course.desc }}</p>
              <div class="course-meta">
                <span><i class="el-icon-video-camera"></i> {{ course.lessons }}课时</span>
                <span><i class="el-icon-user"></i> {{ course.students }}人学习</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 推广活动展示 -->
    <section class="activities-section">
      <div class="section-container">
        <h2 class="section-title" data-aos="fade-up">推广活动</h2>
        <p class="section-subtitle" data-aos="fade-up" data-aos-delay="100">
          参与普通话推广公益活动
        </p>
        
        <div class="activities-grid">
          <div 
            v-for="(activity, index) in activities" 
            :key="index"
            class="activity-card"
            data-aos="fade-up"
            :data-aos-delay="index * 150"
          >
            <div class="activity-image">
              <img :src="activity.image" :alt="activity.title">
              <div class="activity-status" :class="activity.statusClass">{{ activity.status }}</div>
            </div>
            <div class="activity-content">
              <h4 class="activity-title">{{ activity.title }}</h4>
              <p class="activity-desc">{{ activity.desc }}</p>
              <div class="activity-meta">
                <span><i class="el-icon-time"></i> {{ activity.date }}</span>
                <span><i class="el-icon-location"></i> {{ activity.location }}</span>
              </div>
              <el-button type="primary" size="small" round @click="joinActivity(activity)">
                立即报名
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 数据统计 -->
    <section class="stats-section">
      <div class="section-container">
        <div class="stats-grid">
          <div 
            v-for="(stat, index) in stats" 
            :key="index"
            class="stat-item"
            data-aos="zoom-in"
            :data-aos-delay="index * 100"
          >
            <div class="stat-icon" :style="{ background: stat.color }">
              <i :class="stat.icon"></i>
            </div>
            <div class="stat-number">{{ stat.number }}</div>
            <div class="stat-label">{{ stat.label }}</div>
          </div>
        </div>
      </div>
    </section>

    <!-- 用户评价 -->
    <section class="testimonials-section">
      <div class="section-container">
        <h2 class="section-title" data-aos="fade-up">学员评价</h2>
        <p class="section-subtitle" data-aos="fade-up" data-aos-delay="100">
          他们都在这里提升了普通话水平
        </p>
        
        <div class="testimonials-carousel">
          <el-carousel height="200px" :interval="5000">
            <el-carousel-item v-for="(item, index) in testimonials" :key="index">
              <div class="testimonial-item">
                <div class="testimonial-avatar">
                  <img :src="item.avatar" :alt="item.name">
                </div>
                <div class="testimonial-content">
                  <p class="testimonial-text">"{{ item.text }}"</p>
                  <div class="testimonial-author">
                    <span class="author-name">{{ item.name }}</span>
                    <span class="author-title">{{ item.title }}</span>
                  </div>
                </div>
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>
      </div>
    </section>

    <!-- Footer -->
    <section class="footer-section">
      <div class="section-container">
        <div class="footer-content">
          <div class="footer-col">
            <h4>关于我们</h4>
            <p>本网站致力于普通话推广学习与公益宣传，为广大学习者提供专业的学习平台和丰富的学习资源。</p>
          </div>
          <div class="footer-col">
            <h4>常用链接</h4>
            <ul>
              <li><a href="#/pth/learning">在线学习</a></li>
              <li><a href="#/pth/test">发音测试</a></li>
              <li><a href="#/pth/game">小游戏</a></li>
              <li><a href="#/pth/resources">资源下载</a></li>
            </ul>
          </div>
          <div class="footer-col">
            <h4>联系我们</h4>
            <ul>
              <li><i class="el-icon-phone"></i> 400-123-4567</li>
              <li><i class="el-icon-message"></i> contact@putonghua.cn</li>
              <li><i class="el-icon-location"></i> 北京市朝阳区</li>
            </ul>
          </div>
          <div class="footer-col">
            <h4>关注我们</h4>
            <div class="social-links">
              <a href="#"><i class="el-icon-s-promotion"></i></a>
              <a href="#"><i class="el-icon-chat-dot-round"></i></a>
              <a href="#"><i class="el-icon-share"></i></a>
            </div>
          </div>
        </div>
        <div class="footer-bottom">
          <p>&copy; 2025 普通话学习推广平台 | 用于普通话推广学习与公益宣传用途</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: 'PutonghuaHome',
  data() {
    return {
      features: [
        {
          title: '普通话学习课程',
          icon: 'el-icon-reading',
          color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
          desc: '系统化学习普通话发音技巧',
          items: ['视频课程', '字词朗读', '日常语音示例'],
          path: '/pth/learning'
        },
        {
          title: 'AI语音发音评测',
          icon: 'el-icon-microphone',
          color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
          desc: '智能评测发音准确度',
          items: ['声母韵母评分', '声调准确度', '可视化波形反馈'],
          path: '/pth/test'
        },
        {
          title: '分级测试模拟',
          icon: 'el-icon-document-checked',
          color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
          desc: '模拟普通话水平测试',
          items: ['PSC模拟测试', '自动评分', '错误分析报告'],
          path: '/pth/exam'
        },
        {
          title: '推广活动资源',
          icon: 'el-icon-download',
          color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
          desc: '丰富的学习资源下载',
          items: ['宣传资料', '海报音视频', '活动报名'],
          path: '/pth/resources'
        },
        {
          title: '方言词典',
          icon: 'el-icon-notebook-2',
          color: 'linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%)',
          desc: '大别山地区方言词汇查询',
          items: ['河南方言', '湖北方言', '安徽方言', '赣方言'],
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
          title: '普通话声母韵母基础',
          desc: '零基础入门，系统学习声母韵母发音方法',
          image: 'https://via.placeholder.com/300x200/667eea/ffffff?text=声母韵母',
          tag: '基础课程',
          lessons: 24,
          students: 1543
        },
        {
          title: '声调训练进阶课',
          desc: '掌握四声变化规律，提升语音表达能力',
          image: 'https://via.placeholder.com/300x200/f5576c/ffffff?text=声调训练',
          tag: '进阶课程',
          lessons: 18,
          students: 987
        },
        {
          title: '朗读技巧提升',
          desc: '学习朗读技巧，提高语言表达感染力',
          image: 'https://via.placeholder.com/300x200/4facfe/ffffff?text=朗读技巧',
          tag: '高级课程',
          lessons: 15,
          students: 765
        }
      ],
      activities: [
        {
          title: '2025年推普周活动',
          desc: '第28届全国推广普通话宣传周',
          image: 'https://via.placeholder.com/400x250/667eea/ffffff?text=推普周',
          date: '2025-09-10',
          location: '全国各地',
          status: '报名中',
          statusClass: 'status-active'
        },
        {
          title: '普通话公益讲座',
          desc: '邀请专家讲解普通话学习方法',
          image: 'https://via.placeholder.com/400x250/43e97b/ffffff?text=公益讲座',
          date: '2025-06-15',
          location: '线上直播',
          status: '即将开始',
          statusClass: 'status-upcoming'
        }
      ],
      stats: [
        { icon: 'el-icon-s-data', number: '12,543', label: '累计测评次数', color: '#667eea' },
        { icon: 'el-icon-user', number: '8,900+', label: '注册用户', color: '#f5576c' },
        { icon: 'el-icon-star-on', number: '98%', label: '用户满意度', color: '#43e97b' },
        { icon: 'el-icon-trophy', number: '156', label: '优秀学员', color: '#4facfe' }
      ],
      testimonials: [
        {
          name: '张同学',
          title: '师范大学学生',
          avatar: 'https://via.placeholder.com/80/667eea/ffffff?text=张',
          text: '通过这个平台，我的普通话水平从二级甲等提升到了一级乙等，非常感谢！'
        },
        {
          name: '李老师',
          title: '小学语文教师',
          avatar: 'https://via.placeholder.com/80/f5576c/ffffff?text=李',
          text: 'AI评测功能非常实用，能够准确指出发音问题，对教学工作帮助很大。'
        },
        {
          name: '王女士',
          title: '播音主持',
          avatar: 'https://via.placeholder.com/80/43e97b/ffffff?text=王',
          text: '课程内容系统专业，老师讲解细致，是学习普通话的好平台。'
        }
      ]
    };
  },
  mounted() {
    // 初始化AOS动画库（需要在main.js中引入）
    if (typeof AOS !== 'undefined') {
      AOS.init({
        duration: 800,
        once: true
      });
    }
  },
  methods: {
    goToLearning() {
      this.$router.push('/pth/learning');
    },
    goToTest() {
      this.$router.push('/pth/test');
    },
    goToFeature(path) {
      this.$router.push(path);
    },
    joinActivity(activity) {
      this.$message.success(`已成功报名：${activity.title}`);
    }
  }
};
</script>

<style scoped lang="scss">
.putonghua-home {
  width: 100%;
  overflow-x: hidden;
}

// Hero Section
.hero-section {
  position: relative;
  height: 600px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  background-image: url('https://via.placeholder.com/1920x600/667eea/ffffff?text=普通话学习');
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  justify-content: center;
  
  .hero-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(102, 126, 234, 0.85);
  }
  
  .hero-content {
    position: relative;
    z-index: 1;
    text-align: center;
    color: white;
    max-width: 800px;
    padding: 0 20px;
  }
  
  .hero-title {
    font-size: 56px;
    font-weight: 800;
    margin: 0 0 20px 0;
    text-shadow: 2px 2px 4px rgba(0,0,0,0.2);
  }
  
  .hero-subtitle {
    font-size: 24px;
    margin: 0 0 40px 0;
    opacity: 0.95;
    letter-spacing: 2px;
  }
  
  .hero-buttons {
    display: flex;
    gap: 20px;
    justify-content: center;
    flex-wrap: wrap;
    
    .el-button {
      padding: 15px 40px;
      font-size: 16px;
      font-weight: 600;
    }
  }
}

// Section 通用样式
.section-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 80px 20px;
}

.section-title {
  text-align: center;
  font-size: 40px;
  font-weight: 700;
  color: #303133;
  margin: 0 0 10px 0;
}

.section-subtitle {
  text-align: center;
  font-size: 18px;
  color: #909399;
  margin: 0 0 50px 0;
}

// Features Section
.features-section {
  background: #f5f7fa;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 30px;
}

.feature-card {
  background: white;
  padding: 40px 30px;
  border-radius: 16px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  
  &:hover {
    transform: translateY(-10px);
    box-shadow: 0 12px 30px rgba(0,0,0,0.15);
  }
  
  .feature-icon {
    width: 80px;
    height: 80px;
    margin: 0 auto 20px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    
    i {
      font-size: 40px;
      color: white;
    }
  }
  
  .feature-title {
    font-size: 22px;
    font-weight: 600;
    margin: 0 0 15px 0;
    color: #303133;
  }
  
  .feature-desc {
    font-size: 14px;
    color: #606266;
    margin: 0 0 20px 0;
  }
  
  .feature-list {
    list-style: none;
    padding: 0;
    margin: 0;
    
    li {
      font-size: 13px;
      color: #909399;
      padding: 5px 0;
      
      &:before {
        content: "✓ ";
        color: #67C23A;
        font-weight: bold;
      }
    }
  }
}

// Learning Path Section
.learning-path-section {
  background: white;
}

.learning-path {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 20px;
}

.path-step {
  flex: 1;
  min-width: 180px;
  display: flex;
  align-items: center;
  gap: 15px;
  
  .step-number {
    width: 60px;
    height: 60px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24px;
    font-weight: bold;
    flex-shrink: 0;
  }
  
  .step-content {
    flex: 1;
  }
  
  .step-title {
    font-size: 16px;
    font-weight: 600;
    margin: 0 0 5px 0;
    color: #303133;
  }
  
  .step-desc {
    font-size: 13px;
    color: #909399;
    margin: 0;
  }
  
  .step-arrow {
    color: #DCDFE6;
    font-size: 24px;
  }
}

// Courses Section
.courses-section {
  background: #f5f7fa;
}

.courses-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 30px;
}

.course-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  transition: all 0.3s;
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 12px 30px rgba(0,0,0,0.15);
  }
  
  .course-image {
    position: relative;
    height: 200px;
    overflow: hidden;
    
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
    
    .course-tag {
      position: absolute;
      top: 15px;
      right: 15px;
      padding: 5px 15px;
      background: rgba(255,255,255,0.95);
      color: #667eea;
      font-size: 12px;
      font-weight: 600;
      border-radius: 20px;
    }
  }
  
  .course-info {
    padding: 25px;
  }
  
  .course-title {
    font-size: 18px;
    font-weight: 600;
    margin: 0 0 10px 0;
    color: #303133;
  }
  
  .course-desc {
    font-size: 14px;
    color: #606266;
    margin: 0 0 15px 0;
    line-height: 1.6;
  }
  
  .course-meta {
    display: flex;
    gap: 20px;
    font-size: 13px;
    color: #909399;
    
    span {
      display: flex;
      align-items: center;
      gap: 5px;
    }
  }
}

// Activities Section
.activities-section {
  background: white;
}

.activities-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(500px, 1fr));
  gap: 30px;
}

.activity-card {
  display: flex;
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  transition: all 0.3s;
  
  &:hover {
    box-shadow: 0 12px 30px rgba(0,0,0,0.15);
  }
  
  .activity-image {
    position: relative;
    width: 200px;
    flex-shrink: 0;
    
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
    
    .activity-status {
      position: absolute;
      top: 15px;
      left: 15px;
      padding: 5px 15px;
      background: white;
      color: #667eea;
      font-size: 12px;
      font-weight: 600;
      border-radius: 20px;
      
      &.status-active {
        background: #67C23A;
        color: white;
      }
      
      &.status-upcoming {
        background: #E6A23C;
        color: white;
      }
    }
  }
  
  .activity-content {
    padding: 25px;
    flex: 1;
  }
  
  .activity-title {
    font-size: 18px;
    font-weight: 600;
    margin: 0 0 10px 0;
    color: #303133;
  }
  
  .activity-desc {
    font-size: 14px;
    color: #606266;
    margin: 0 0 15px 0;
  }
  
  .activity-meta {
    display: flex;
    gap: 20px;
    margin-bottom: 15px;
    font-size: 13px;
    color: #909399;
    
    span {
      display: flex;
      align-items: center;
      gap: 5px;
    }
  }
}

// Stats Section
.stats-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  
  .section-container {
    padding: 60px 20px;
  }
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 40px;
}

.stat-item {
  text-align: center;
  
  .stat-icon {
    width: 80px;
    height: 80px;
    margin: 0 auto 20px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(255,255,255,0.2);
    
    i {
      font-size: 40px;
      color: white;
    }
  }
  
  .stat-number {
    font-size: 36px;
    font-weight: bold;
    margin-bottom: 10px;
  }
  
  .stat-label {
    font-size: 16px;
    opacity: 0.9;
  }
}

// Testimonials Section
.testimonials-section {
  background: #f5f7fa;
}

.testimonials-carousel {
  max-width: 800px;
  margin: 0 auto;
  
  .testimonial-item {
    display: flex;
    align-items: center;
    gap: 30px;
    padding: 20px;
  }
  
  .testimonial-avatar {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    overflow: hidden;
    flex-shrink: 0;
    
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }
  
  .testimonial-content {
    flex: 1;
  }
  
  .testimonial-text {
    font-size: 16px;
    color: #606266;
    line-height: 1.8;
    margin: 0 0 15px 0;
    font-style: italic;
  }
  
  .testimonial-author {
    display: flex;
    gap: 10px;
    
    .author-name {
      font-weight: 600;
      color: #303133;
    }
    
    .author-title {
      color: #909399;
      font-size: 14px;
    }
  }
}

// Footer Section
.footer-section {
  background: #2c3e50;
  color: white;
  
  .section-container {
    padding: 50px 20px 20px;
  }
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 40px;
  margin-bottom: 40px;
  
  .footer-col {
    h4 {
      font-size: 18px;
      margin: 0 0 20px 0;
    }
    
    p {
      font-size: 14px;
      line-height: 1.8;
      opacity: 0.8;
    }
    
    ul {
      list-style: none;
      padding: 0;
      margin: 0;
      
      li {
        padding: 8px 0;
        font-size: 14px;
        opacity: 0.8;
        
        a {
          color: white;
          text-decoration: none;
          
          &:hover {
            opacity: 1;
          }
        }
      }
    }
  }
}

.social-links {
  display: flex;
  gap: 15px;
  
  a {
    width: 40px;
    height: 40px;
    background: rgba(255,255,255,0.1);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-size: 20px;
    transition: all 0.3s;
    
    &:hover {
      background: rgba(255,255,255,0.2);
      transform: translateY(-3px);
    }
  }
}

.footer-bottom {
  text-align: center;
  padding-top: 30px;
  border-top: 1px solid rgba(255,255,255,0.1);
  
  p {
    margin: 0;
    font-size: 14px;
    opacity: 0.6;
  }
}

// 响应式
@media (max-width: 768px) {
  .hero-title {
    font-size: 36px !important;
  }
  
  .hero-subtitle {
    font-size: 18px !important;
  }
  
  .section-title {
    font-size: 32px !important;
  }
  
  .learning-path {
    flex-direction: column;
    
    .step-arrow {
      transform: rotate(90deg);
    }
  }
  
  .activities-grid {
    grid-template-columns: 1fr;
  }
  
  .activity-card {
    flex-direction: column;
    
    .activity-image {
      width: 100%;
      height: 200px;
    }
  }
}
</style>
