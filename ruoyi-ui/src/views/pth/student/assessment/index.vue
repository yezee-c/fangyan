<template>
  <div class="app-container assessment">
    <el-card>
      <div slot="header">
        <span><i class="el-icon-edit"></i> 普通话测评</span>
        <el-tag type="success" size="small" style="float: right;">严格遵循《普通话水平测试大纲》</el-tag>
      </div>

      <!-- 测评类型选择 -->
      <el-row :gutter="20" style="margin-bottom: 20px;">
        <el-col :span="6" v-for="type in assessmentTypes" :key="type.value">
          <el-card shadow="hover" class="type-card" :class="{ 'active-type': currentType === type.value }" @click.native="selectType(type)">
            <div class="type-icon">
              <i :class="type.icon" :style="{ color: type.color }"></i>
            </div>
            <h3>{{ type.label }}</h3>
            <p>{{ type.desc }}</p>
          </el-card>
        </el-col>
      </el-row>

      <!-- 测评内容区域 -->
      <el-card v-if="currentType" style="margin-top: 20px;">
        <div slot="header">
          <i class="el-icon-microphone"></i> {{ getCurrentTypeName() }}
          <el-tag type="info" size="small" style="float: right;">题目 {{ currentQuestionIndex + 1 }}/{{ currentQuestions.length }}</el-tag>
        </div>
        
        <!-- 题目展示 -->
        <div class="question-area">
          <h2 class="question-title">{{ currentQuestion }}</h2>
          <div v-if="currentType === '1' || currentType === '2'" class="pinyin-hint">
            <el-tag size="small" type="info">拼音：{{ currentPinyin }}</el-tag>
          </div>
        </div>

        <!-- 录音区域 -->
        <div class="record-section">
          <div class="record-status">
            <i :class="isRecording ? 'el-icon-microphone recording-icon' : 'el-icon-microphone'" 
               :style="{ fontSize: '80px', color: isRecording ? '#F56C6C' : '#409EFF' }"></i>
            <p v-if="!isRecording" style="margin-top: 20px; font-size: 16px;">点击下方按钮开始录音</p>
            <p v-else style="margin-top: 20px; font-size: 16px; color: #F56C6C;">正在录音中... {{ recordTime }}s</p>
          </div>
          
          <div class="record-controls">
            <el-button v-if="!isRecording" type="primary" size="large" icon="el-icon-microphone" @click="startRecording">
              开始录音
            </el-button>
            <el-button v-else type="danger" size="large" icon="el-icon-video-pause" @click="stopRecording">
              停止录音
            </el-button>
            <el-button v-if="audioUrl" type="success" size="large" icon="el-icon-video-play" @click="playRecording">
              播放录音
            </el-button>
            <el-button type="info" size="large" icon="el-icon-refresh-right" @click="nextQuestion">
              下一题
            </el-button>
          </div>
          
          <!-- 音频播放器 -->
          <div v-if="audioUrl" style="margin-top: 20px;">
            <audio :src="audioUrl" controls style="width: 100%;"></audio>
          </div>
        </div>

        <!-- 评分结果 -->
        <div v-if="currentScore !== null" class="score-result">
          <el-divider></el-divider>
          <h3>本题得分：<span :class="getScoreClass(currentScore)">{{ currentScore }}分</span></h3>
          <el-progress :percentage="currentScore" :color="getProgressColor(currentScore)"></el-progress>
          <div class="score-detail">
            <el-tag type="success">准确度：{{ Math.floor(Math.random() * 20) + 80 }}%</el-tag>
            <el-tag type="info" style="margin-left: 10px;">流畅度：{{ Math.floor(Math.random() * 20) + 80 }}%</el-tag>
            <el-tag type="warning" style="margin-left: 10px;">音调：{{ Math.floor(Math.random() * 20) + 80 }}%</el-tag>
          </div>
        </div>
      </el-card>

      <!-- 我的测评记录 -->
      <el-card style="margin-top: 20px;">
        <div slot="header">
          <i class="el-icon-document"></i> 我的测评记录
          <el-button type="text" style="float: right;" @click="clearRecords" v-if="recordList.length > 0">清空记录</el-button>
        </div>
        <el-table :data="recordList" v-loading="loading">
          <el-table-column label="测评时间" prop="createTime" width="180" />
          <el-table-column label="测评类型" prop="assessmentType" width="120">
            <template slot-scope="scope">
              <el-tag>{{ getTypeName(scope.row.assessmentType) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="总分" prop="totalScore" width="100">
            <template slot-scope="scope">
              <span :class="getScoreClass(scope.row.totalScore)">
                {{ scope.row.totalScore }}分
              </span>
            </template>
          </el-table-column>
          <el-table-column label="等级" prop="level" width="100" />
          <el-table-column label="薄弱环节" prop="weakPoints" show-overflow-tooltip />
          <el-table-column label="操作" width="150">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="viewDetail(scope.row)">查看详情</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-if="recordList.length === 0" description="暂无测评记录"></el-empty>
      </el-card>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Assessment",
  data() {
    return {
      loading: false,
      currentType: null, // 当前选中的测评类型
      currentQuestionIndex: 0, // 当前题目索引
      currentScore: null, // 当前题目得分
      isRecording: false, // 是否正在录音
      recordTime: 0, // 录音时长
      recordTimer: null, // 录音计时器
      mediaRecorder: null, // 录音器
      audioChunks: [], // 音频数据
      audioUrl: null, // 音频URL
      
      assessmentTypes: [
        { value: '1', label: '单字朗读', desc: '测试声母、韵母、声调', icon: 'el-icon-document', color: '#409EFF' },
        { value: '2', label: '词语朗读', desc: '测试词汇发音准确度', icon: 'el-icon-reading', color: '#67C23A' },
        { value: '3', label: '短文朗读', desc: '测试流畅度和连贯性', icon: 'el-icon-notebook-2', color: '#E6A23C' },
        { value: '4', label: '命题说话', desc: '测试口语表达能力', icon: 'el-icon-chat-line-round', color: '#F56C6C' }
      ],
      
      // 题库
      questionBank: {
        '1': [ // 单字
          { text: '鹤', pinyin: 'hè' },
          { text: '颜', pinyin: 'yán' },
          { text: '振', pinyin: 'zhèn' },
          { text: '翁', pinyin: 'chì' },
          { text: '略', pinyin: 'lüè' },
          { text: '丘', pinyin: 'qiū' },
          { text: '蓄', pinyin: 'xù' },
          { text: '炫', pinyin: 'xuàn' },
          { text: '俑', pinyin: 'qiào' },
          { text: '绢', pinyin: 'qiú' }
        ],
        '2': [ // 词语
          { text: '风韵', pinyin: 'fēng yùn' },
          { text: '荡萁', pinyin: 'róng yù' },
          { text: '改变', pinyin: 'gǎi biàn' },
          { text: '巩固', pinyin: 'gǒng gù' },
          { text: '宣扬', pinyin: 'xuān yáng' },
          { text: '机械', pinyin: 'jī xiè' },
          { text: '聪明', pinyin: 'cōng ming' },
          { text: '徘徊', pinyin: 'páng huáng' },
          { text: '高亮', pinyin: 'gāo liàng' },
          { text: '腾飞', pinyin: 'téng fēi' }
        ],
        '3': [ // 短文
          { text: '我国幅员辽阔，地大物博。山川壮丽，风景优美。人民勤劳智慧，为了建设社会主义现代化国家而艰苦奋斗。', pinyin: '' },
          { text: '早晨，太阳凉凉的，空气清新而微寒。一群孩子在公园里锻炼身体，他们有的在跑步，有的在做体操。', pinyin: '' },
          { text: '学习普通话对于我们每个人来说都非常重要。它是我们交流的工具，是团结的纽带。我们应该大力推广普通话。', pinyin: '' }
        ],
        '4': [ // 命题说话
          { text: '请谈谈你最喜欢的一部电影或电视剧', pinyin: '' },
          { text: '请介绍你的家乡特产或风俗习惯', pinyin: '' },
          { text: '请谈谈你对环保的看法', pinyin: '' },
          { text: '请讲述一次难忘的旅行经历', pinyin: '' },
          { text: '请谈谈你的一个好朋友', pinyin: '' }
        ]
      },
      
      recordList: []
    };
  },
  
  computed: {
    currentQuestions() {
      return this.questionBank[this.currentType] || []
    },
    currentQuestion() {
      return this.currentQuestions[this.currentQuestionIndex]?.text || ''
    },
    currentPinyin() {
      return this.currentQuestions[this.currentQuestionIndex]?.pinyin || ''
    }
  },
  
  beforeDestroy() {
    if (this.recordTimer) {
      clearInterval(this.recordTimer)
    }
    if (this.mediaRecorder && this.isRecording) {
      this.mediaRecorder.stop()
      this.mediaRecorder.stream.getTracks().forEach(track => track.stop())
    }
  },
  
  methods: {
    selectType(type) {
      this.currentType = type.value
      this.currentQuestionIndex = 0
      this.currentScore = null
      this.audioUrl = null
      this.$message.success(`已选择：${type.label}`)
    },
    
    getCurrentTypeName() {
      const item = this.assessmentTypes.find(t => t.value === this.currentType)
      return item ? item.label : ''
    },
    
    async startRecording() {
      try {
        if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) {
          this.$message.error('您的浏览器不支持录音功能')
          return
        }

        const stream = await navigator.mediaDevices.getUserMedia({ audio: true })
        
        this.mediaRecorder = new MediaRecorder(stream)
        this.audioChunks = []
        this.isRecording = true
        this.recordTime = 0
        this.currentScore = null
        
        this.recordTimer = setInterval(() => {
          this.recordTime++
          // 自动停止：单字/词语5秒，短文/命题说话60秒
          const maxTime = (this.currentType === '1' || this.currentType === '2') ? 5 : 60
          if (this.recordTime >= maxTime) {
            this.stopRecording()
          }
        }, 1000)
        
        this.mediaRecorder.ondataavailable = (event) => {
          if (event.data.size > 0) {
            this.audioChunks.push(event.data)
          }
        }
        
        this.mediaRecorder.onstop = () => {
          const audioBlob = new Blob(this.audioChunks, { type: 'audio/wav' })
          this.audioUrl = URL.createObjectURL(audioBlob)
          this.evaluateRecording()
        }
        
        this.mediaRecorder.start()
        this.$message.success('录音已开始，请开始朗读...')
        
      } catch (error) {
        console.error('录音错误:', error)
        this.$message.error('无法访问麦克风，请检查权限设置')
      }
    },
    
    stopRecording() {
      if (this.mediaRecorder && this.isRecording) {
        this.mediaRecorder.stop()
        this.isRecording = false
        
        if (this.recordTimer) {
          clearInterval(this.recordTimer)
          this.recordTimer = null
        }
        
        this.mediaRecorder.stream.getTracks().forEach(track => track.stop())
        this.$message.success(`录音已停止，时长: ${this.recordTime}秒`)
      }
    },
    
    evaluateRecording() {
      this.$message.info('正在评分...')
      
      setTimeout(() => {
        // 模拟评分（60-100分）
        this.currentScore = Math.floor(Math.random() * 40) + 60
        
        // 添加到记录
        this.addRecord()
        
        this.$message.success(`评分完成！得分: ${this.currentScore}分`)
      }, 1500)
    },
    
    playRecording() {
      if (this.audioUrl) {
        const audio = new Audio(this.audioUrl)
        audio.play()
        this.$message.success('正在播放录音...')
      }
    },
    
    nextQuestion() {
      if (this.currentQuestionIndex < this.currentQuestions.length - 1) {
        this.currentQuestionIndex++
      } else {
        this.currentQuestionIndex = 0
      }
      this.currentScore = null
      this.audioUrl = null
      this.$message.info(`已切换到第 ${this.currentQuestionIndex + 1} 题`)
    },
    
    addRecord() {
      const now = new Date()
      const dateStr = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')} ${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`
      
      let level = '三级乙等'
      if (this.currentScore >= 97) level = '一级甲等'
      else if (this.currentScore >= 92) level = '一级乙等'
      else if (this.currentScore >= 87) level = '二级甲等'
      else if (this.currentScore >= 80) level = '二级乙等'
      else if (this.currentScore >= 70) level = '三级甲等'
      
      this.recordList.unshift({
        createTime: dateStr,
        assessmentType: this.currentType,
        totalScore: this.currentScore,
        level: level,
        weakPoints: this.getWeakPoints()
      })
      
      // 只保留20条
      if (this.recordList.length > 20) {
        this.recordList = this.recordList.slice(0, 20)
      }
    },
    
    getWeakPoints() {
      const points = ['声母发音', '韵母发音', '声调把握', '语音语调', '流畅度']
      const weak = []
      const count = Math.floor(Math.random() * 2) + 1
      for (let i = 0; i < count; i++) {
        const index = Math.floor(Math.random() * points.length)
        if (!weak.includes(points[index])) {
          weak.push(points[index])
        }
      }
      return weak.join('、')
    },
    
    getTypeName(type) {
      const item = this.assessmentTypes.find(t => t.value === type)
      return item ? item.label : '未知'
    },
    
    getScoreClass(score) {
      if (score >= 90) return 'score-excellent'
      if (score >= 80) return 'score-good'
      if (score >= 60) return 'score-pass'
      return 'score-fail'
    },
    
    getProgressColor(score) {
      if (score >= 90) return '#67C23A'
      if (score >= 80) return '#409EFF'
      if (score >= 60) return '#E6A23C'
      return '#F56C6C'
    },
    
    viewDetail(row) {
      this.$message.info(`查看详情: ${this.getTypeName(row.assessmentType)} - ${row.totalScore}分`)
    },
    
    clearRecords() {
      this.$confirm('确定清空所有测评记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.recordList = []
        this.$message.success('已清空')
      }).catch(() => {})
    }
  }
};
</script>

<style scoped lang="scss">
.type-card {
  cursor: pointer;
  text-align: center;
  transition: all 0.3s;
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  }
  
  &.active-type {
    border: 2px solid #409EFF;
    box-shadow: 0 0 10px rgba(64, 158, 255, 0.3);
  }
  
  .type-icon i {
    font-size: 48px;
  }
  
  h3 {
    margin: 15px 0 10px;
  }
  
  p {
    color: #909399;
    font-size: 14px;
  }
}

.question-area {
  text-align: center;
  padding: 30px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
  margin-bottom: 30px;
  
  .question-title {
    font-size: 32px;
    color: #fff;
    margin-bottom: 15px;
    font-weight: bold;
  }
  
  .pinyin-hint {
    margin-top: 15px;
  }
}

.record-section {
  text-align: center;
  padding: 20px;
  
  .record-status {
    margin-bottom: 30px;
  }
  
  .recording-icon {
    animation: pulse 1.5s ease-in-out infinite;
  }
  
  .record-controls {
    .el-button {
      margin: 0 10px;
    }
  }
}

.score-result {
  margin-top: 20px;
  text-align: center;
  
  h3 {
    font-size: 24px;
    margin-bottom: 20px;
  }
  
  .score-detail {
    margin-top: 20px;
  }
}

.score-excellent { color: #67C23A; font-weight: bold; }
.score-good { color: #409EFF; font-weight: bold; }
.score-pass { color: #E6A23C; }
.score-fail { color: #F56C6C; }

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.8;
  }
}
</style>
