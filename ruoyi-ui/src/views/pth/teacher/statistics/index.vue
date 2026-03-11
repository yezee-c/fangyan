<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20">
      <el-col :span="6" v-for="stat in stats" :key="stat.key">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-item">
            <i :class="stat.icon" :style="{ color: stat.color }"></i>
            <div class="stat-content">
              <div class="stat-value">{{ stat.value }}</div>
              <div class="stat-label">{{ stat.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <i class="el-icon-pie-chart"></i> 测评成绩分布
          </div>
          <div ref="scoreChart" style="height: 350px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <i class="el-icon-data-line"></i> 任务完成趋势
          </div>
          <div ref="progressChart" style="height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 详细统计 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card>
          <div slot="header">
            <i class="el-icon-s-data"></i> 班级学习数据明细
            <el-select v-model="selectedClassId" placeholder="选择班级" size="small" style="float: right; width: 200px;" @change="loadClassData">
              <el-option label="全部班级" :value="null"></el-option>
              <el-option v-for="cls in classList" :key="cls.classId" :label="cls.className" :value="cls.classId"></el-option>
            </el-select>
          </div>
          <el-table :data="classDetailData" stripe>
            <el-table-column label="班级名称" prop="className" width="150" />
            <el-table-column label="学生人数" prop="studentCount" width="120" />
            <el-table-column label="发布任务" prop="taskCount" width="120" />
            <el-table-column label="完成任务" prop="completedTasks" width="120" />
            <el-table-column label="完成率" width="200">
              <template slot-scope="scope">
                <el-progress :percentage="scope.row.completeRate" :color="getProgressColor(scope.row.completeRate)"></el-progress>
              </template>
            </el-table-column>
            <el-table-column label="平均分" prop="avgScore" width="120">
              <template slot-scope="scope">
                <span :style="{ color: getScoreColor(scope.row.avgScore) }">{{ scope.row.avgScore }}分</span>
              </template>
            </el-table-column>
            <el-table-column label="最高分" prop="maxScore" width="120" />
            <el-table-column label="最低分" prop="minScore" width="120" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 学生排名 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <i class="el-icon-trophy"></i> 成绩排名 TOP 10
          </div>
          <el-table :data="topStudents" :show-header="false">
            <el-table-column width="60">
              <template slot-scope="scope">
                <el-tag v-if="scope.$index < 3" :type="['danger', 'warning', 'success'][scope.$index]" size="small">
                  {{ scope.$index + 1 }}
                </el-tag>
                <span v-else style="color: #909399;">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="studentName" />
            <el-table-column prop="className" width="150" />
            <el-table-column width="100" align="right">
              <template slot-scope="scope">
                <span style="font-weight: bold; font-size: 16px;" :style="{ color: getScoreColor(scope.row.avgScore) }">
                  {{ scope.row.avgScore }}分
                </span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <i class="el-icon-warning"></i> 学习警示
          </div>
          <el-table :data="alertStudents" :show-header="false">
            <el-table-column width="60">
              <template>
                <i class="el-icon-warning" style="color: #F56C6C; font-size: 20px;"></i>
              </template>
            </el-table-column>
            <el-table-column prop="studentName" />
            <el-table-column prop="className" width="150" />
            <el-table-column width="150" align="right">
              <template slot-scope="scope">
                <el-tag type="danger" size="small">{{ scope.row.reason }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: "Statistics",
  data() {
    return {
      stats: [
        { key: 'class', label: '管理班级', value: 0, icon: 'el-icon-school', color: '#409EFF' },
        { key: 'student', label: '学生总数', value: 0, icon: 'el-icon-user', color: '#67C23A' },
        { key: 'task', label: '发布任务', value: 0, icon: 'el-icon-document', color: '#E6A23C' },
        { key: 'avg', label: '平均分', value: '0', icon: 'el-icon-star-on', color: '#F56C6C' }
      ],
      classList: [],
      taskList: [],
      selectedClassId: null,
      classDetailData: [],
      topStudents: [],
      alertStudents: [],
      scoreChartInstance: null,
      progressChartInstance: null
    };
  },
  
  mounted() {
    this.loadData()
    this.initCharts()
    window.addEventListener('resize', this.handleResize)
  },
  
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.scoreChartInstance) {
      this.scoreChartInstance.dispose()
    }
    if (this.progressChartInstance) {
      this.progressChartInstance.dispose()
    }
  },
  
  methods: {
    loadData() {
      // 加载班级数据
      const classList = localStorage.getItem('pth_class_list')
      this.classList = classList ? JSON.parse(classList) : []
      
      // 加载任务数据
      const taskList = localStorage.getItem('pth_task_list')
      this.taskList = taskList ? JSON.parse(taskList) : []
      
      // 计算总体统计
      this.calculateStats()
      
      // 加载班级详细数据
      this.loadClassData()
      
      // 加载学生排名
      this.loadTopStudents()
      
      // 加载警示学生
      this.loadAlertStudents()
    },
    
    calculateStats() {
      // 班级数
      this.stats[0].value = this.classList.length
      
      // 学生总数
      const totalStudents = this.classList.reduce((sum, cls) => sum + (cls.studentCount || 0), 0)
      this.stats[1].value = totalStudents
      
      // 任务数
      this.stats[2].value = this.taskList.length
      
      // 平均分（模拟）
      const avgScore = totalStudents > 0 ? (Math.random() * 15 + 80).toFixed(1) : '0'
      this.stats[3].value = avgScore
    },
    
    loadClassData() {
      const data = []
      
      const classes = this.selectedClassId 
        ? this.classList.filter(c => c.classId === this.selectedClassId)
        : this.classList
      
      classes.forEach(cls => {
        const classTasks = this.taskList.filter(t => t.classId === cls.classId)
        const taskCount = classTasks.length
        const completedTasks = classTasks.reduce((sum, t) => sum + (t.completedCount || 0), 0)
        const totalTasks = classTasks.reduce((sum, t) => sum + (t.totalCount || 0), 0)
        const completeRate = totalTasks > 0 ? Math.round((completedTasks / totalTasks) * 100) : 0
        
        data.push({
          className: cls.className,
          studentCount: cls.studentCount || 0,
          taskCount: taskCount,
          completedTasks: completedTasks,
          completeRate: completeRate,
          avgScore: (Math.random() * 20 + 75).toFixed(1),
          maxScore: (Math.random() * 5 + 95).toFixed(1),
          minScore: (Math.random() * 20 + 60).toFixed(1)
        })
      })
      
      this.classDetailData = data
    },
    
    loadTopStudents() {
      const students = []
      const names = ['张三', '李四', '王五', '赵六', '刘七', '陈八', '周九', '吴十', '郑十一', '孙十二']
      
      for (let i = 0; i < 10; i++) {
        students.push({
          studentName: names[i],
          className: this.classList[i % this.classList.length]?.className || '一年级一班',
          avgScore: (98 - i * 2 - Math.random() * 2).toFixed(1)
        })
      }
      
      this.topStudents = students
    },
    
    loadAlertStudents() {
      const students = []
      const names = ['王小明', '李小红', '张小华', '陈小浩', '刘小芳']
      const reasons = ['成绩不及格', '未提交任务', '缺勤过多', '成绩下降', '未提交任务']
      
      for (let i = 0; i < 5; i++) {
        students.push({
          studentName: names[i],
          className: this.classList[i % this.classList.length]?.className || '一年级一班',
          reason: reasons[i]
        })
      }
      
      this.alertStudents = students
    },
    
    initCharts() {
      this.initScoreChart()
      this.initProgressChart()
    },
    
    initScoreChart() {
      if (!this.$refs.scoreChart) return
      
      this.scoreChartInstance = echarts.init(this.$refs.scoreChart)
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}人 ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center'
        },
        series: [
          {
            name: '成绩分布',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: true,
              formatter: '{b}\n{d}%'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 16,
                fontWeight: 'bold'
              }
            },
            data: [
              { value: 35, name: '优秀(90-100)', itemStyle: { color: '#67C23A' } },
              { value: 45, name: '良好(80-89)', itemStyle: { color: '#409EFF' } },
              { value: 28, name: '中等(70-79)', itemStyle: { color: '#E6A23C' } },
              { value: 12, name: '及格(60-69)', itemStyle: { color: '#F56C6C' } },
              { value: 5, name: '不及格(<60)', itemStyle: { color: '#909399' } }
            ]
          }
        ]
      }
      
      this.scoreChartInstance.setOption(option)
    },
    
    initProgressChart() {
      if (!this.$refs.progressChart) return
      
      this.progressChartInstance = echarts.init(this.$refs.progressChart)
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['任务数', '完成数']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['12-08', '12-09', '12-10', '12-11', '12-12', '12-13', '12-14']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '任务数',
            type: 'line',
            data: [2, 3, 3, 4, 5, 6, 6],
            smooth: true,
            itemStyle: { color: '#409EFF' },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
                { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
              ])
            }
          },
          {
            name: '完成数',
            type: 'line',
            data: [1, 2, 2, 3, 4, 5, 5],
            smooth: true,
            itemStyle: { color: '#67C23A' },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(103, 194, 58, 0.3)' },
                { offset: 1, color: 'rgba(103, 194, 58, 0.1)' }
              ])
            }
          }
        ]
      }
      
      this.progressChartInstance.setOption(option)
    },
    
    handleResize() {
      if (this.scoreChartInstance) {
        this.scoreChartInstance.resize()
      }
      if (this.progressChartInstance) {
        this.progressChartInstance.resize()
      }
    },
    
    getProgressColor(rate) {
      if (rate >= 80) return '#67C23A'
      if (rate >= 50) return '#E6A23C'
      return '#F56C6C'
    },
    
    getScoreColor(score) {
      if (score >= 90) return '#67C23A'
      if (score >= 80) return '#409EFF'
      if (score >= 60) return '#E6A23C'
      return '#F56C6C'
    }
  }
};
</script>

<style scoped lang="scss">
.stat-card {
  .stat-item {
    display: flex;
    align-items: center;
    
    i {
      font-size: 48px;
      margin-right: 20px;
    }
    
    .stat-content {
      .stat-value {
        font-size: 28px;
        font-weight: bold;
        margin-bottom: 5px;
      }
      
      .stat-label {
        color: #909399;
        font-size: 14px;
      }
    }
  }
}
</style>
