<template>
  <div class="evaluation-container">
    <div class="page-header">
      <h1>AI 语音发音评测</h1>
      <p>上传录音即可评测 · 智能分析 · 专业反馈</p>
    </div>

    <!-- 评测步骤 -->
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card shadow="hover">
          <div slot="header">
            <span style="font-size: 18px; font-weight: bold;">📝 评测流程</span>
          </div>
          <el-steps :active="currentStep" align-center>
            <el-step title="选择测试内容" icon="el-icon-edit"></el-step>
            <el-step title="录音上传" icon="el-icon-microphone"></el-step>
            <el-step title="AI分析" icon="el-icon-loading"></el-step>
            <el-step title="查看结果" icon="el-icon-data-analysis"></el-step>
          </el-steps>
        </el-card>
      </el-col>
    </el-row>

    <!-- 评测功能 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :xs="24" :md="12">
        <el-card shadow="hover" class="function-card">
          <div slot="header">
            <span style="font-size: 16px; font-weight: bold;">🎤 录音评测</span>
          </div>
          <div class="upload-area">
            <i class="el-icon-microphone" :class="{ 'recording': isRecording }" style="font-size: 80px; color: #409EFF;"></i>
            <p style="margin: 20px 0;" v-if="!isRecording">点击按钮开始录音或上传音频文件</p>
            <p style="margin: 20px 0; color: #F56C6C;" v-else>正在录音中... {{ recordingTime }}s</p>
            <el-button v-if="!isRecording" type="primary" icon="el-icon-microphone" @click="startRecording">开始录音</el-button>
            <el-button v-else type="danger" icon="el-icon-video-pause" @click="stopRecording">停止录音</el-button>
            <el-upload
              class="upload-demo"
              action="#"
              :on-change="handleFileChange"
              :auto-upload="false"
              :show-file-list="false"
              accept="audio/*"
              style="margin-top: 10px;"
            >
              <el-button type="success" icon="el-icon-upload2">上传音频</el-button>
            </el-upload>
            <div v-if="audioUrl" style="margin-top: 20px;">
              <audio :src="audioUrl" controls style="width: 100%;"></audio>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :md="12">
        <el-card shadow="hover" class="function-card">
          <div slot="header">
            <span style="font-size: 16px; font-weight: bold;">📊 评测结果</span>
          </div>
          <div class="result-area" v-if="hasResult">
            <el-progress type="circle" :percentage="score" :color="scoreColor"></el-progress>
            <h3 style="margin-top: 20px;">综合得分: {{ score }}分</h3>
            <p v-if="detectedDialect" style="margin-top: 10px; color: #409EFF;">
              检测到的方言：{{ detectedDialect }} (相似度: {{ dialectScore }}%)
            </p>
            <div class="detail-scores">
              <div class="score-item">
                <span>声母准确度</span>
                <el-progress :percentage="85" color="#67C23A"></el-progress>
              </div>
              <div class="score-item">
                <span>韵母准确度</span>
                <el-progress :percentage="78" color="#E6A23C"></el-progress>
              </div>
              <div class="score-item">
                <span>声调准确度</span>
                <el-progress :percentage="92" color="#409EFF"></el-progress>
              </div>
            </div>
            <div style="margin-top: 20px;">
              <el-button type="primary" icon="el-icon-service" @click="playStandardPutonghua">播放普通话示范</el-button>
            </div>
          </div>
          <div class="result-area" v-else>
            <el-empty description="暂无评测结果，请先录音或上传音频"></el-empty>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 评测历史 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card shadow="hover">
          <div slot="header">
            <span style="font-size: 18px; font-weight: bold;">📈 评测历史</span>
          </div>
          <el-table :data="historyData" stripe style="width: 100%">
            <el-table-column type="index" label="#" width="60"></el-table-column>
            <el-table-column prop="date" label="评测时间" width="180"></el-table-column>
            <el-table-column prop="content" label="测试内容"></el-table-column>
            <el-table-column prop="score" label="综合得分" width="120">
              <template slot-scope="scope">
                <el-tag :type="getScoreType(scope.row.score)">{{ scope.row.score }}分</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="viewDetail(scope.row)">查看详情</el-button>
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
  name: 'PthEvaluation',
  data() {
    return {
      currentStep: 0,
      hasResult: false,
      score: 85,
      isRecording: false, // 是否正在录音
      recordingTime: 0, // 录音时长
      mediaRecorder: null, // 录音器
      audioChunks: [], // 音频数据块
      audioUrl: null, // 音频URL
      timer: null, // 计时器
      detectedDialect: '', // 检测到的方言
      dialectScore: 0, // 方言相似度
      historyData: [
        { date: '2024-12-10 14:30', content: '声母练习：b p m f', score: 85 },
        { date: '2024-12-09 10:20', content: '韵母练习：a o e i u ü', score: 92 },
        { date: '2024-12-08 16:45', content: '声调练习：四声变化', score: 78 },
        { date: '2024-12-07 09:15', content: '综合朗读：短文朗读', score: 88 }
      ]
    }
  },
  computed: {
    scoreColor() {
      if (this.score >= 90) return '#67C23A'
      if (this.score >= 80) return '#409EFF'
      if (this.score >= 60) return '#E6A23C'
      return '#F56C6C'
    }
  },
  methods: {
    async startRecording() {
      try {
        // 检查浏览器支持
        if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) {
          this.$message.error('您的浏览器不支持录音功能')
          return
        }

        // 请求麦克风权限
        const stream = await navigator.mediaDevices.getUserMedia({ audio: true })
        
        this.mediaRecorder = new MediaRecorder(stream)
        this.audioChunks = []
        this.isRecording = true
        this.recordingTime = 0
        this.currentStep = 1
        
        // 开始计时
        this.timer = setInterval(() => {
          this.recordingTime++
        }, 1000)
        
        // 收集音频数据
        this.mediaRecorder.ondataavailable = (event) => {
          if (event.data.size > 0) {
            this.audioChunks.push(event.data)
          }
        }
        
        // 录音结束后处理
        this.mediaRecorder.onstop = () => {
          const audioBlob = new Blob(this.audioChunks, { type: 'audio/wav' })
          this.audioUrl = URL.createObjectURL(audioBlob)
          
          // 开始分析
          this.analyzeAudio(audioBlob)
        }
        
        this.mediaRecorder.start()
        this.$message.success('录音已开始，请开始说话...')
        
      } catch (error) {
        console.error('录音错误:', error)
        this.$message.error('无法访问麦克风，请检查权限设置')
      }
    },
    
    stopRecording() {
      if (this.mediaRecorder && this.isRecording) {
        this.mediaRecorder.stop()
        this.isRecording = false
        
        // 停止计时
        if (this.timer) {
          clearInterval(this.timer)
          this.timer = null
        }
        
        // 停止所有音频轨道
        this.mediaRecorder.stream.getTracks().forEach(track => track.stop())
        
        this.$message.success(`录音已停止，时长: ${this.recordingTime}秒`)
      }
    },
    
    analyzeAudio(audioBlob) {
      this.currentStep = 2
      this.$message.success('正在进行AI分析...')
      
      // 模拟方言识别（实际项目中需要调用后端API）
      setTimeout(() => {
        // 随机生成识别结果
        const dialects = [
          { name: '普通话', score: Math.floor(Math.random() * 20) + 80 },
          { name: '北京话', score: Math.floor(Math.random() * 30) + 20 },
          { name: '东北话', score: Math.floor(Math.random() * 30) + 10 },
          { name: '广东话', score: Math.floor(Math.random() * 20) + 5 },
          { name: '四川话', score: Math.floor(Math.random() * 20) + 5 }
        ]
        
        // 按得分排序
        dialects.sort((a, b) => b.score - a.score)
        
        this.detectedDialect = dialects[0].name
        this.dialectScore = dialects[0].score
        this.score = dialects[0].score
        
        this.currentStep = 3
        this.hasResult = true
        
        // 添加到历史记录
        const now = new Date()
        const dateStr = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')} ${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`
        
        this.historyData.unshift({
          date: dateStr,
          content: `方言识别：检测为${this.detectedDialect}`,
          score: this.score
        })
        
        this.$message.success(`识别完成！检测为: ${this.detectedDialect}，相似度: ${this.dialectScore}%`)
      }, 2000)
    },
    
    handleFileChange(file) {
      if (!file || !file.raw) {
        return
      }
      
      this.$message.success(`文件上传成功: ${file.name}`)
      this.currentStep = 2
      
      // 创建音频URL用于播放
      this.audioUrl = URL.createObjectURL(file.raw)
      
      // 模拟分析
      setTimeout(() => {
        this.analyzeUploadedFile(file.raw)
      }, 500)
    },
    
    analyzeUploadedFile(fileBlob) {
      this.currentStep = 2
      this.$message.success('正在进行AI分析...')
      
      // 模拟方言识别
      setTimeout(() => {
        const dialects = [
          { name: '普通话', score: Math.floor(Math.random() * 20) + 80 },
          { name: '北京话', score: Math.floor(Math.random() * 30) + 20 },
          { name: '东北话', score: Math.floor(Math.random() * 30) + 10 },
          { name: '广东话', score: Math.floor(Math.random() * 20) + 5 },
          { name: '四川话', score: Math.floor(Math.random() * 20) + 5 }
        ]
        
        dialects.sort((a, b) => b.score - a.score)
        
        this.detectedDialect = dialects[0].name
        this.dialectScore = dialects[0].score
        this.score = dialects[0].score
        
        this.currentStep = 3
        this.hasResult = true
        
        const now = new Date()
        const dateStr = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')} ${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`
        
        this.historyData.unshift({
          date: dateStr,
          content: `方言识别：检测为${this.detectedDialect}`,
          score: this.score
        })
        
        this.$message.success(`识别完成！检测为: ${this.detectedDialect}，相似度: ${this.dialectScore}%`)
      }, 2000)
    },
    getScoreType(score) {
      if (score >= 90) return 'success'
      if (score >= 80) return ''
      if (score >= 60) return 'warning'
      return 'danger'
    },
    viewDetail(row) {
      this.$message.info(`查看评测详情: ${row.content}`)
    },
    
    playStandardPutonghua() {
      // 播放标准普通话示范
      window.speechSynthesis.cancel() // 停止当前正在播放的语音
      
      // 检查浏览器支持
      if (!window.speechSynthesis) {
        this.$message.error('您的浏览器不支持语音播放功能')
        return
      }
      
      // 根据识别结果播放不同的示范语音
      const exampleTexts = {
        '普通话': '您好，欢迎使用普通话学习系统。您的发音非常标准，请继续保持。',
        '北京话': '您的发音带有北京口音。标准普通话应该是：您好，欢迎使用普通话学习系统。',
        '东北话': '您的发音带有东北口音。标准普通话应该是：您好，欢迎使用普通话学习系统。',
        '广东话': '您的发音带有广东口音。标准普通话应该是：您好，欢迎使用普通话学习系统。',
        '四川话': '您的发音带有四川口音。标准普通话应该是：您好，欢迎使用普通话学习系统。'
      }
      
      const textToSpeak = exampleTexts[this.detectedDialect] || '您好，这是标准普通话示范。'
      
      // 创建语音合成实例
      const utterance = new SpeechSynthesisUtterance(textToSpeak)
      
      // 设置语音参数
      utterance.lang = 'zh-CN' // 中文普通话
      utterance.rate = 0.85 // 语速略慢，便于学习
      utterance.pitch = 1 // 音调
      utterance.volume = 1 // 音量
      
      // 播放开始事件
      utterance.onstart = () => {
        this.$message.success('正在播放标准普通话示范...')
      }
      
      // 播放结束事件
      utterance.onend = () => {
        this.$message.info('播放完毕。请模仿刚才的发音。')
      }
      
      // 播放错误事件
      utterance.onerror = (event) => {
        console.error('语音播放错误:', event)
        this.$message.error('语音播放失败，请重试')
      }
      
      // 开始播放
      window.speechSynthesis.speak(utterance)
    }
  },
  
  beforeDestroy() {
    // 组件销毁时清理资源
    if (this.isRecording && this.mediaRecorder) {
      this.mediaRecorder.stop()
      this.mediaRecorder.stream.getTracks().forEach(track => track.stop())
    }
    if (this.timer) {
      clearInterval(this.timer)
    }
    if (this.audioUrl) {
      URL.revokeObjectURL(this.audioUrl)
    }
  }
}
</script>

<style scoped>
.evaluation-container {
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

.function-card {
  height: 100%;
}

.upload-area {
  text-align: center;
  padding: 40px 20px;
}

.result-area {
  text-align: center;
  padding: 20px;
}

.detail-scores {
  margin-top: 30px;
  text-align: left;
}

.score-item {
  margin-bottom: 20px;
}

.score-item span {
  display: block;
  margin-bottom: 10px;
  font-size: 14px;
  color: #606266;
}

/* 录音动画 */
.recording {
  animation: pulse 1.5s ease-in-out infinite;
  color: #F56C6C !important;
}

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
