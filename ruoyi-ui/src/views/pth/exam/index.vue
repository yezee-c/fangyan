<template>
  <div class="exam-container">
    <div class="page-header">
      <h1>分级测试（模拟PSC）</h1>
      <p>模拟普通话水平测试 · 自动评分 · 错误分析</p>
    </div>

    <!-- 考试等级选择 -->
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card shadow="hover">
          <div slot="header">
            <span style="font-size: 18px; font-weight: bold;">🎯 选择测试等级</span>
          </div>
          <el-row :gutter="20">
            <el-col :xs="24" :sm="8" v-for="(level, index) in levels" :key="index">
              <div class="level-card" @click="selectLevel(level)">
                <div class="level-icon" :style="{ background: level.color }">
                  <i :class="level.icon"></i>
                </div>
                <h3>{{ level.name }}</h3>
                <p>{{ level.desc }}</p>
                <div class="level-info">
                  <span>题目数: {{ level.questions }}</span>
                  <span>时长: {{ level.duration }}分钟</span>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <!-- 模拟考试题型 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card shadow="hover">
          <div slot="header">
            <span style="font-size: 18px; font-weight: bold;">📝 考试题型</span>
          </div>
          <el-collapse v-model="activeNames">
            <el-collapse-item title="一、读单音节字词（100个）" name="1">
              <p>考查声母、韵母、声调的发音</p>
              <p><strong>分值：</strong>10分</p>
              <el-button type="primary" size="small" @click="startPractice('单音节')">开始练习</el-button>
            </el-collapse-item>
            <el-collapse-item title="二、读多音节词语（100个）" name="2">
              <p>考查声母、韵母、声调和变调、轻声、儿化的发音</p>
              <p><strong>分值：</strong>20分</p>
              <el-button type="primary" size="small" @click="startPractice('多音节')">开始练习</el-button>
            </el-collapse-item>
            <el-collapse-item title="三、朗读短文（1篇，400字）" name="3">
              <p>考查使用普通话朗读书面作品的能力</p>
              <p><strong>分值：</strong>30分</p>
              <el-button type="primary" size="small" @click="startPractice('朗读')">开始练习</el-button>
            </el-collapse-item>
            <el-collapse-item title="四、命题说话（3分钟）" name="4">
              <p>考查在无文字凭借的情况下说普通话的水平</p>
              <p><strong>分值：</strong>40分</p>
              <el-button type="primary" size="small" @click="startPractice('说话')">开始练习</el-button>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>
    </el-row>

    <!-- 考试记录 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card shadow="hover">
          <div slot="header">
            <span style="font-size: 18px; font-weight: bold;">📊 考试记录</span>
          </div>
          <el-table :data="examRecords" stripe style="width: 100%">
            <el-table-column type="index" label="#" width="60"></el-table-column>
            <el-table-column prop="date" label="考试时间" width="180"></el-table-column>
            <el-table-column prop="level" label="等级" width="120"></el-table-column>
            <el-table-column prop="score" label="总分" width="100">
              <template slot-scope="scope">
                <span :style="{ color: getScoreColor(scope.row.score), fontWeight: 'bold' }">
                  {{ scope.row.score }}分
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="grade" label="等级评定" width="150">
              <template slot-scope="scope">
                <el-tag :type="getGradeType(scope.row.grade)">{{ scope.row.grade }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="viewReport(scope.row)">查看报告</el-button>
                <el-button size="mini" type="success" @click="retry(scope.row)">重新测试</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'PthExam',
  data() {
    return {
      activeNames: ['1'],
      levels: [
        {
          name: '一级甲等',
          desc: '97-100分，最高等级',
          icon: 'el-icon-trophy',
          color: 'linear-gradient(135deg, #FFD700 0%, #FFA500 100%)',
          questions: 400,
          duration: 45
        },
        {
          name: '一级乙等',
          desc: '92-96.99分',
          icon: 'el-icon-medal',
          color: 'linear-gradient(135deg, #67C23A 0%, #85CE61 100%)',
          questions: 400,
          duration: 45
        },
        {
          name: '二级甲等',
          desc: '87-91.99分',
          icon: 'el-icon-medal-1',
          color: 'linear-gradient(135deg, #409EFF 0%, #66B1FF 100%)',
          questions: 400,
          duration: 45
        }
      ],
      examRecords: [
        { date: '2024-12-10 10:00', level: '一级乙等', score: 94.5, grade: '一级乙等' },
        { date: '2024-12-05 14:30', level: '二级甲等', score: 89.2, grade: '二级甲等' },
        { date: '2024-11-28 09:15', level: '二级甲等', score: 87.8, grade: '二级甲等' },
        { date: '2024-11-20 16:45', level: '二级乙等', score: 82.5, grade: '二级乙等' }
      ]
    }
  },
  methods: {
    selectLevel(level) {
      this.$confirm(`确定开始${level.name}模拟测试吗？`, '提示', {
        confirmButtonText: '开始测试',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$message.success(`即将开始${level.name}模拟测试`)
        // 跳转到考试页面
      }).catch(() => {})
    },
    startPractice(type) {
      this.$message.success(`开始${type}练习`)
      // 跳转到对应练习页面
    },
    getScoreColor(score) {
      if (score >= 97) return '#FFD700'
      if (score >= 92) return '#67C23A'
      if (score >= 87) return '#409EFF'
      if (score >= 80) return '#E6A23C'
      return '#F56C6C'
    },
    getGradeType(grade) {
      if (grade.includes('一级甲')) return 'warning'
      if (grade.includes('一级乙')) return 'success'
      if (grade.includes('二级甲')) return ''
      return 'info'
    },
    viewReport(row) {
      this.$message.info(`查看考试报告: ${row.date}`)
    },
    retry(row) {
      this.$message.success('开始重新测试')
    }
  }
}
</script>

<style scoped>
.exam-container {
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

.level-card {
  text-align: center;
  padding: 30px 20px;
  cursor: pointer;
  transition: all 0.3s;
  border-radius: 8px;
  border: 2px solid transparent;
}

.level-card:hover {
  transform: translateY(-5px);
  border-color: #409EFF;
  box-shadow: 0 8px 20px rgba(64, 158, 255, 0.3);
}

.level-icon {
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

.level-card h3 {
  font-size: 18px;
  margin: 10px 0;
  color: #303133;
}

.level-card p {
  font-size: 14px;
  color: #909399;
  margin-bottom: 15px;
}

.level-info {
  display: flex;
  justify-content: space-around;
  font-size: 13px;
  color: #606266;
}

.el-collapse {
  border: none;
}

.el-collapse-item {
  margin-bottom: 10px;
}
</style>
