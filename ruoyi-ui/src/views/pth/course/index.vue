<template>
  <div class="course-container">
    <div class="page-header">
      <h1>普通话学习课程</h1>
      <p>系统化学习普通话发音技巧</p>
    </div>

    <!-- 课程分类 -->
    <el-row :gutter="20" class="course-categories">
      <el-col :span="24">
        <el-card shadow="hover">
          <div slot="header">
            <span style="font-size: 18px; font-weight: bold;">📚 课程分类</span>
          </div>
          <el-row :gutter="20">
            <el-col :xs="24" :sm="12" :md="8">
              <div class="category-item">
                <i class="el-icon-video-camera-solid" style="font-size: 48px; color: #409EFF;"></i>
                <h3>视频课程</h3>
                <p>系统化视频教学，从基础到进阶</p>
                <el-button type="primary" size="small" @click="gotoVideoCourse">进入学习</el-button>
              </div>
            </el-col>
            <el-col :xs="24" :sm="12" :md="8">
              <div class="category-item">
                <i class="el-icon-reading" style="font-size: 48px; color: #67C23A;"></i>
                <h3>字词朗读</h3>
                <p>声母、韵母、声调专项训练</p>
                <el-button type="success" size="small" @click="gotoReading">开始练习</el-button>
              </div>
            </el-col>
            <el-col :xs="24" :sm="12" :md="8">
              <div class="category-item">
                <i class="el-icon-headset" style="font-size: 48px; color: #E6A23C;"></i>
                <h3>日常语音示例</h3>
                <p>常用语句、对话场景示范</p>
                <el-button type="warning" size="small" @click="gotoExamples">收听示例</el-button>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <!-- 推荐课程 -->
    <el-row :gutter="20" class="recommended-courses">
      <el-col :span="24">
        <el-card shadow="hover">
          <div slot="header">
            <span style="font-size: 18px; font-weight: bold;">🔥 推荐课程</span>
          </div>
          <el-row :gutter="20">
            <el-col :xs="24" :sm="12" :md="6" v-for="(course, index) in courses" :key="index">
              <el-card shadow="hover" class="course-card">
                <div class="course-image">
                  <i :class="course.icon" style="font-size: 60px;"></i>
                </div>
                <h4>{{ course.title }}</h4>
                <p class="course-desc">{{ course.desc }}</p>
                <div class="course-info">
                  <span><i class="el-icon-video-camera"></i> {{ course.lessons }}课时</span>
                  <span><i class="el-icon-user"></i> {{ course.students }}人学习</span>
                </div>
                <el-button type="primary" size="small" plain @click="startCourse(course)">开始学习</el-button>
              </el-card>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'PthCourse',
  data() {
    return {
      courses: [
        {
          title: '声母韵母基础',
          desc: '零基础入门，系统学习发音方法',
          icon: 'el-icon-reading',
          lessons: 24,
          students: 1543
        },
        {
          title: '声调训练营',
          desc: '掌握四声变化，纠正声调错误',
          icon: 'el-icon-sort',
          lessons: 18,
          students: 982
        },
        {
          title: '朗读技巧提升',
          desc: '提升朗读表现力和感染力',
          icon: 'el-icon-microphone',
          lessons: 15,
          students: 756
        },
        {
          title: '常见难点突破',
          desc: '针对性解决发音难题',
          icon: 'el-icon-warning-outline',
          lessons: 20,
          students: 1234
        }
      ]
    }
  },
  methods: {
    gotoVideoCourse() {
      this.$router.push('/pth/course/video')
    },
    gotoReading() {
      this.$router.push('/pth/course/reading')
    },
    gotoExamples() {
      this.$router.push('/pth/course/examples')
    },
    startCourse(course) {
      this.$router.push({
        path: '/pth/course/detail',
        query: { title: course.title }
      })
    }
  }
}
</script>

<style scoped>
.course-container {
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

.course-categories {
  margin-bottom: 30px;
}

.category-item {
  text-align: center;
  padding: 30px 20px;
  transition: all 0.3s;
}

.category-item:hover {
  transform: translateY(-5px);
}

.category-item h3 {
  font-size: 18px;
  margin: 15px 0 10px;
  color: #303133;
}

.category-item p {
  font-size: 14px;
  color: #909399;
  margin-bottom: 15px;
}

.recommended-courses {
  margin-bottom: 20px;
}

.course-card {
  margin-bottom: 20px;
  text-align: center;
  transition: all 0.3s;
}

.course-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.course-image {
  padding: 20px;
  color: #409EFF;
}

.course-card h4 {
  font-size: 16px;
  margin: 10px 0;
  color: #303133;
}

.course-desc {
  font-size: 14px;
  color: #909399;
  margin-bottom: 15px;
  min-height: 40px;
}

.course-info {
  display: flex;
  justify-content: space-around;
  margin-bottom: 15px;
  font-size: 13px;
  color: #606266;
}

.course-info span {
  display: flex;
  align-items: center;
  gap: 5px;
}
</style>
