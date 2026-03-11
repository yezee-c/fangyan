<template>
  <div class="course-detail-container">
    <div class="page-header">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/pth/course' }">课程中心</el-breadcrumb-item>
        <el-breadcrumb-item>课程详情</el-breadcrumb-item>
      </el-breadcrumb>
      <h1>{{ courseTitle }}</h1>
      <p>详细的课程内容和学习计划</p>
    </div>

    <el-row :gutter="20">
      <!-- 课程信息 -->
      <el-col :xs="24" :lg="16">
        <el-card shadow="hover">
          <div slot="header">
            <span style="font-size: 18px; font-weight: bold;">📖 课程介绍</span>
          </div>
          <div class="course-intro">
            <p>本课程将帮助您系统学习普通话发音技巧，通过大量练习和实战，快速提升普通话水平。</p>
            <h3>学习目标</h3>
            <ul>
              <li>掌握标准的普通话发音</li>
              <li>纠正常见的发音错误</li>
              <li>提高口语表达能力</li>
              <li>通过普通话水平测试</li>
            </ul>
          </div>
        </el-card>

        <!-- 课程章节 -->
        <el-card shadow="hover" style="margin-top: 20px;">
          <div slot="header">
            <span style="font-size: 18px; font-weight: bold;">📚 课程章节</span>
          </div>
          <el-collapse v-model="activeChapter">
            <el-collapse-item v-for="(chapter, index) in chapters" :key="index" :title="chapter.title" :name="index">
              <div class="lesson-list">
                <div v-for="(lesson, idx) in chapter.lessons" :key="idx" class="lesson-item" @click="startLesson(lesson)">
                  <i class="el-icon-video-play"></i>
                  <span>{{ lesson }}</span>
                  <el-tag size="mini" type="success">未学习</el-tag>
                </div>
              </div>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>

      <!-- 侧边栏 -->
      <el-col :xs="24" :lg="8">
        <el-card shadow="hover">
          <div slot="header">
            <span style="font-size: 18px; font-weight: bold;">📊 课程统计</span>
          </div>
          <div class="course-stats">
            <div class="stat-item">
              <i class="el-icon-video-camera"></i>
              <div class="stat-info">
                <div class="stat-value">24课时</div>
                <div class="stat-label">总课时</div>
              </div>
            </div>
            <div class="stat-item">
              <i class="el-icon-user"></i>
              <div class="stat-info">
                <div class="stat-value">1543人</div>
                <div class="stat-label">已学习</div>
              </div>
            </div>
            <div class="stat-item">
              <i class="el-icon-star-off"></i>
              <div class="stat-info">
                <div class="stat-value">4.8分</div>
                <div class="stat-label">课程评分</div>
              </div>
            </div>
          </div>
          <el-button type="primary" size="medium" style="width: 100%; margin-top: 20px;" @click="startStudy">立即学习</el-button>
        </el-card>

        <!-- 讲师信息 -->
        <el-card shadow="hover" style="margin-top: 20px;">
          <div slot="header">
            <span style="font-size: 18px; font-weight: bold;">👨‍🏫 讲师介绍</span>
          </div>
          <div class="teacher-info">
            <div class="teacher-avatar">
              <i class="el-icon-user-solid" style="font-size: 60px; color: #409EFF;"></i>
            </div>
            <h4>张老师</h4>
            <p>普通话国家级测试员，10年教学经验</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'CourseDetail',
  data() {
    return {
      courseTitle: this.$route.query.title || '课程详情',
      activeChapter: [0],
      chapters: [
        {
          title: '第一章：发音基础',
          lessons: [
            '1.1 普通话概述',
            '1.2 声母系统',
            '1.3 韵母系统',
            '1.4 声调介绍'
          ]
        },
        {
          title: '第二章：发音技巧',
          lessons: [
            '2.1 唇音发音技巧',
            '2.2 舌音发音技巧',
            '2.3 喉音发音技巧',
            '2.4 鼻音发音技巧'
          ]
        },
        {
          title: '第三章：常见问题',
          lessons: [
            '3.1 平翘舌区分',
            '3.2 前后鼻音区分',
            '3.3 轻声变调',
            '3.4 儿化音处理'
          ]
        },
        {
          title: '第四章：实战练习',
          lessons: [
            '4.1 单音节练习',
            '4.2 双音节练习',
            '4.3 短文朗读',
            '4.4 话题说话'
          ]
        }
      ]
    }
  },
  methods: {
    startLesson(lesson) {
      this.$message.success(`开始学习: ${lesson}`)
    },
    startStudy() {
      this.$message.success('开始学习课程！')
    }
  }
}
</script>

<style scoped>
.course-detail-container {
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

.course-intro p {
  line-height: 1.8;
  color: #606266;
  margin-bottom: 20px;
}

.course-intro h3 {
  font-size: 18px;
  color: #303133;
  margin-bottom: 15px;
}

.course-intro ul {
  list-style: none;
  padding: 0;
}

.course-intro ul li {
  padding: 8px 0;
  padding-left: 20px;
  position: relative;
  color: #606266;
}

.course-intro ul li::before {
  content: '✓';
  position: absolute;
  left: 0;
  color: #67C23A;
  font-weight: bold;
}

.lesson-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.lesson-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.lesson-item:hover {
  background: #409EFF;
  color: white;
}

.lesson-item i {
  font-size: 18px;
}

.lesson-item span {
  flex: 1;
}

.course-stats {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 15px;
}

.stat-item i {
  font-size: 36px;
  color: #409EFF;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.teacher-info {
  text-align: center;
}

.teacher-avatar {
  margin-bottom: 15px;
}

.teacher-info h4 {
  font-size: 18px;
  color: #303133;
  margin-bottom: 10px;
}

.teacher-info p {
  font-size: 14px;
  color: #909399;
}
</style>
