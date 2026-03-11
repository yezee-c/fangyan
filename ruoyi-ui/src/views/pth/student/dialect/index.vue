<template>
  <div class="app-container dialect-recognition">
    <el-card class="feature-card">
      <div slot="header" class="clearfix">
        <span><i class="el-icon-mic"></i> 方言识别</span>
        <el-tag type="info" size="small" style="float: right;">基于FunASR技术</el-tag>
      </div>

      <!-- 功能说明 -->
      <el-alert
        title="功能说明"
        type="info"
        :closable="false"
        style="margin-bottom: 20px;"
      >
        <p>1. 支持大别山地区方言识别与转换</p>
        <p>2. 可实时录音或上传音频文件</p>
        <p>3. 自动转换为标准普通话文字并语音播报</p>
      </el-alert>

      <!-- 录音区域 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card shadow="hover" class="record-card">
            <div slot="header">
              <i class="el-icon-microphone"></i> 实时录音
            </div>
            <div class="record-area">
              <div class="record-status">
                <i :class="isRecording ? 'el-icon-video-play recording-icon' : 'el-icon-video-pause'"></i>
                <p>{{ isRecording ? '录音中...' : '未录音' }}</p>
                <p v-if="isRecording" class="record-time">{{ recordTime }}s</p>
              </div>
              <div class="record-controls">
                <el-button
                  type="primary"
                  :icon="isRecording ? 'el-icon-video-pause' : 'el-icon-microphone'"
                  @click="toggleRecording"
                  :loading="isProcessing"
                >
                  {{ isRecording ? '停止录音' : '开始录音' }}
                </el-button>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="12">
          <el-card shadow="hover" class="upload-card">
            <div slot="header">
              <i class="el-icon-upload"></i> 上传音频
            </div>
            <el-upload
              class="upload-demo"
              drag
              :action="uploadUrl"
              :headers="uploadHeaders"
              :before-upload="beforeUpload"
              :on-success="handleUploadSuccess"
              :on-error="handleUploadError"
              accept=".mp3,.wav,.m4a"
              :show-file-list="false"
            >
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">将音频文件拖到此处，或<em>点击上传</em></div>
              <div class="el-upload__tip" slot="tip">支持 mp3/wav/m4a 格式，文件不超过10MB</div>
            </el-upload>
          </el-card>
        </el-col>
      </el-row>

      <!-- 识别结果 -->
      <el-card v-if="recognitionResult" class="result-card" style="margin-top: 20px;">
        <div slot="header">
          <i class="el-icon-document"></i> 识别结果
        </div>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="result-section">
              <h4><i class="el-icon-chat-dot-square"></i> 方言识别文字</h4>
              <div class="result-text dialect-text">
                {{ recognitionResult.dialectText || '暂无识别结果' }}
              </div>
              <div class="result-meta">
                <el-tag size="small" type="success" v-if="recognitionResult.dialectRegion">
                  方言地区: {{ recognitionResult.dialectRegion }}
                </el-tag>
                <el-tag size="small" type="info" style="margin-left: 10px;">
                  置信度: {{ (recognitionResult.confidence * 100).toFixed(2) }}%
                </el-tag>
                <el-tag size="small" type="warning" style="margin-left: 10px;">
                  时长: {{ recognitionResult.duration }}s
                </el-tag>
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="result-section">
              <h4><i class="el-icon-document-checked"></i> 普通话对照</h4>
              <div class="result-text mandarin-text">
                {{ recognitionResult.mandarinText || '暂无对照' }}
              </div>
              <div class="result-actions">
                <el-button size="small" type="primary" icon="el-icon-video-play" @click="playAudio">
                  播放普通话
                </el-button>
                <el-button size="small" icon="el-icon-document-copy" @click="copyText">
                  复制文字
                </el-button>
                <el-button size="small" icon="el-icon-download" @click="exportResult">
                  导出结果
                </el-button>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 历史记录 -->
      <el-card class="history-card" style="margin-top: 20px;">
        <div slot="header">
          <i class="el-icon-time"></i> 识别历史
          <el-button type="text" style="float: right;" @click="clearHistory">清空历史</el-button>
        </div>
        <el-empty v-if="historyList.length === 0" description="暂无历史记录"></el-empty>
        <el-timeline v-else>
          <el-timeline-item
            v-for="(item, index) in historyList"
            :key="index"
            :timestamp="item.time"
            placement="top"
          >
            <el-card>
              <p><strong>方言：</strong>{{ item.dialectText }}</p>
              <p><strong>普通话：</strong>{{ item.mandarinText }}</p>
              <div style="margin-top: 10px;">
                <el-button size="mini" type="text" @click="viewHistory(item)">查看详情</el-button>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </el-card>

    <!-- 提示对话框 -->
    <el-dialog
      title="功能说明"
      :visible.sync="showTip"
      width="500px"
      append-to-body
    >
      <div class="tip-content">
        <el-alert
          title="此功能需要集成第三方服务"
          type="warning"
          :closable="false"
          show-icon
        >
          <p>当前展示的是功能界面演示</p>
          <p>实际使用需要集成 FunASR 方言识别服务</p>
          <p>接口位置：<code>IDialectRecognitionService.java</code></p>
        </el-alert>
      </div>
      <span slot="footer">
        <el-button type="primary" @click="showTip = false">我知道了</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getToken } from "@/utils/auth";

export default {
  name: "DialectRecognition",
  data() {
    return {
      // 录音状态
      isRecording: false,
      isProcessing: false,
      recordTime: 0,
      recordTimer: null,
      
      // 上传配置
      uploadUrl: process.env.VUE_APP_BASE_API + "/common/upload",
      uploadHeaders: {
        Authorization: "Bearer " + getToken()
      },
      
      // 识别结果
      recognitionResult: null,
      
      // 历史记录
      historyList: [],
      
      // 提示
      showTip: false
    };
  },
  created() {
    this.loadHistory();
    // 不再自动显示功能提示
    // this.showTip = true;
  },
  beforeDestroy() {
    if (this.recordTimer) {
      clearInterval(this.recordTimer);
    }
  },
  methods: {
    // 开始/停止录音
    toggleRecording() {
      if (this.isRecording) {
        this.stopRecording();
      } else {
        this.startRecording();
      }
    },
    
    // 开始录音
    startRecording() {
      this.$message.info("正在初始化录音...");
      
      // TODO: 实际录音逻辑需要使用浏览器录音API
      // navigator.mediaDevices.getUserMedia({ audio: true })
      
      this.isRecording = true;
      this.recordTime = 0;
      
      this.recordTimer = setInterval(() => {
        this.recordTime++;
        if (this.recordTime >= 60) {
          this.stopRecording();
          this.$message.warning("录音时长已达上限");
        }
      }, 1000);
      
      this.$message.success("录音已开始");
    },
    
    // 停止录音
    stopRecording() {
      this.isRecording = false;
      if (this.recordTimer) {
        clearInterval(this.recordTimer);
      }
      
      // 模拟识别过程
      this.processRecording();
    },
    
    // 处理录音
    processRecording() {
      this.isProcessing = true;
      this.$message.info("正在识别方言...");
      
      // TODO: 调用后端API进行方言识别
      // 这里使用模拟数据
      setTimeout(() => {
        // 随机生成不同方言示例
        const dialectExamples = [
          {
            dialectText: "（示例）侬好啊，今朝天气蛮好额",
            mandarinText: "你好啊，今天天气很好啊",
            dialectRegion: "上海方言",
            confidence: 0.95
          },
          {
            dialectText: "（示例）你吃饭了冇",
            mandarinText: "你吃饭了吗",
            dialectRegion: "广东粤语",
            confidence: 0.92
          },
          {
            dialectText: "（示例）这个东西蛮好",
            mandarinText: "这个东西很好",
            dialectRegion: "大别山方言",
            confidence: 0.88
          },
          {
            dialectText: "（示例）你在干啥呢",
            mandarinText: "你在做什么呢",
            dialectRegion: "东北方言",
            confidence: 0.93
          },
          {
            dialectText: "（示例）老板，来一碗小面",
            mandarinText: "老板，来一碗小面",
            dialectRegion: "四川方言",
            confidence: 0.90
          }
        ];
        
        // 随机选择一个示例
        const randomExample = dialectExamples[Math.floor(Math.random() * dialectExamples.length)];
        
        this.recognitionResult = {
          ...randomExample,
          duration: this.recordTime
        };
        
        // 添加到历史
        this.addToHistory(this.recognitionResult);
        
        this.isProcessing = false;
        this.$message.success("识别完成");
      }, 2000);
    },
    
    // 上传前检查
    beforeUpload(file) {
      const isAudio = ['audio/mpeg', 'audio/wav', 'audio/x-m4a'].includes(file.type);
      const isLt10M = file.size / 1024 / 1024 < 10;

      if (!isAudio) {
        this.$message.error('只能上传音频文件!');
        return false;
      }
      if (!isLt10M) {
        this.$message.error('音频大小不能超过 10MB!');
        return false;
      }
      
      this.$message.info("正在上传音频文件...");
      return true;
    },
    
    // 上传成功
    handleUploadSuccess(response, file) {
      this.$message.success("上传成功，正在识别...");
      
      // TODO: 调用后端方言识别API
      setTimeout(() => {
        // 随机生成不同方言示例
        const dialectExamples = [
          {
            dialectText: "（示例）侬好啊，今朝天气蛮好额",
            mandarinText: "你好啊，今天天气很好啊",
            dialectRegion: "上海方言",
            confidence: 0.95
          },
          {
            dialectText: "（示例）你吃饭了冇",
            mandarinText: "你吃饭了吗",
            dialectRegion: "广东粤语",
            confidence: 0.92
          },
          {
            dialectText: "（示例）这个东西蛮好",
            mandarinText: "这个东西很好",
            dialectRegion: "大别山方言",
            confidence: 0.88
          },
          {
            dialectText: "（示例）你在干啥呢",
            mandarinText: "你在做什么呢",
            dialectRegion: "东北方言",
            confidence: 0.93
          },
          {
            dialectText: "（示例）老板，来一碗小面",
            mandarinText: "老板，来一碗小面",
            dialectRegion: "四川方言",
            confidence: 0.90
          }
        ];
        
        const randomExample = dialectExamples[Math.floor(Math.random() * dialectExamples.length)];
        
        this.recognitionResult = {
          ...randomExample,
          duration: 3.5
        };
        
        this.addToHistory(this.recognitionResult);
        this.$message.success("识别完成");
      }, 2000);
    },
    
    // 上传失败
    handleUploadError() {
      this.$message.error("上传失败，请重试");
    },
    
    // 播放音频
    playAudio() {
      if (!this.recognitionResult || !this.recognitionResult.mandarinText) {
        this.$message.warning('没有可播放的内容')
        return
      }
      
      // 停止当前正在播放的语音
      window.speechSynthesis.cancel()
      
      // 检查浏览器支持
      if (!window.speechSynthesis) {
        this.$message.error('您的浏览器不支持语音播放功能')
        return
      }
      
      // 创建语音合成实例
      const utterance = new SpeechSynthesisUtterance(this.recognitionResult.mandarinText)
      
      // 设置语音参数
      utterance.lang = 'zh-CN' // 中文普通话
      utterance.rate = 0.85 // 语速略慢，便于学习
      utterance.pitch = 1 // 音调
      utterance.volume = 1 // 音量
      
      // 播放开始事件
      utterance.onstart = () => {
        this.$message.success('正在播放普通话语音...')
      }
      
      // 播放结束事件
      utterance.onend = () => {
        this.$message.info('播放完毕')
      }
      
      // 播放错误事件
      utterance.onerror = (event) => {
        console.error('语音播放错误:', event)
        this.$message.error('语音播放失败，请重试')
      }
      
      // 开始播放
      window.speechSynthesis.speak(utterance)
    },
    
    // 复制文字
    copyText() {
      if (!this.recognitionResult) return;
      
      const text = `方言：${this.recognitionResult.dialectText}\n普通话：${this.recognitionResult.mandarinText}`;
      
      navigator.clipboard.writeText(text).then(() => {
        this.$message.success("已复制到剪贴板");
      }).catch(() => {
        this.$message.error("复制失败");
      });
    },
    
    // 导出结果
    exportResult() {
      if (!this.recognitionResult) return;
      
      const content = `方言识别结果

方言文字：${this.recognitionResult.dialectText}

普通话对照：${this.recognitionResult.mandarinText}

置信度：${(this.recognitionResult.confidence * 100).toFixed(2)}%
时长：${this.recognitionResult.duration}秒`;
      
      const blob = new Blob([content], { type: 'text/plain' });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `方言识别_${new Date().getTime()}.txt`;
      a.click();
      URL.revokeObjectURL(url);
      
      this.$message.success("导出成功");
    },
    
    // 添加到历史
    addToHistory(result) {
      this.historyList.unshift({
        ...result,
        time: new Date().toLocaleString()
      });
      
      // 只保留最近10条
      if (this.historyList.length > 10) {
        this.historyList = this.historyList.slice(0, 10);
      }
      
      this.saveHistory();
    },
    
    // 查看历史
    viewHistory(item) {
      this.recognitionResult = item;
      this.$message.info("已加载历史记录");
    },
    
    // 清空历史
    clearHistory() {
      this.$confirm('确定清空所有历史记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.historyList = [];
        this.saveHistory();
        this.$message.success('已清空');
      }).catch(() => {});
    },
    
    // 保存历史到本地
    saveHistory() {
      localStorage.setItem('dialect_history', JSON.stringify(this.historyList));
    },
    
    // 加载历史
    loadHistory() {
      const history = localStorage.getItem('dialect_history');
      if (history) {
        try {
          this.historyList = JSON.parse(history);
        } catch (e) {
          this.historyList = [];
        }
      }
    }
  }
};
</script>

<style scoped lang="scss">
.dialect-recognition {
  .feature-card {
    min-height: 600px;
  }
  
  .record-card,
  .upload-card {
    height: 300px;
    
    .record-area {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      height: 220px;
      
      .record-status {
        text-align: center;
        margin-bottom: 30px;
        
        i {
          font-size: 60px;
          color: #409EFF;
        }
        
        .recording-icon {
          color: #F56C6C;
          animation: pulse 1s infinite;
        }
        
        p {
          margin: 10px 0;
          font-size: 16px;
        }
        
        .record-time {
          font-size: 24px;
          font-weight: bold;
          color: #F56C6C;
        }
      }
    }
  }
  
  .result-card {
    .result-section {
      h4 {
        margin-bottom: 15px;
        color: #303133;
        
        i {
          margin-right: 5px;
          color: #409EFF;
        }
      }
      
      .result-text {
        padding: 15px;
        background: #f5f7fa;
        border-radius: 4px;
        min-height: 80px;
        line-height: 1.8;
        margin-bottom: 15px;
      }
      
      .dialect-text {
        border-left: 3px solid #E6A23C;
      }
      
      .mandarin-text {
        border-left: 3px solid #67C23A;
      }
      
      .result-meta {
        margin-bottom: 15px;
      }
      
      .result-actions {
        .el-button {
          margin-right: 10px;
        }
      }
    }
  }
  
  .history-card {
    .el-timeline {
      padding-left: 10px;
    }
  }
  
  .tip-content {
    code {
      background: #f5f7fa;
      padding: 2px 6px;
      border-radius: 3px;
      color: #409EFF;
    }
  }
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}
</style>
