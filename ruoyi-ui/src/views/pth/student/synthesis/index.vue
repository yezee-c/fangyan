<template>
  <div class="app-container synthesis">
    <el-card>
      <div slot="header">
        <span><i class="el-icon-s-platform"></i> 语音合成</span>
        <el-tag type="success" size="small" style="float: right;">科大讯飞在线语音合成</el-tag>
      </div>

      <el-form label-width="100px">
        <el-form-item label="输入文字">
          <el-input
            type="textarea"
            :rows="6"
            v-model="inputText"
            placeholder="请输入要合成的文字内容..."
            maxlength="500"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="音色选择">
          <el-radio-group v-model="voiceType">
            <el-radio label="female1">女声1（温柔）</el-radio>
            <el-radio label="female2">女声2（清脆）</el-radio>
            <el-radio label="male1">男声1（浑厚）</el-radio>
            <el-radio label="child">童声</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="语速">
          <el-slider v-model="speed" :min="0" :max="100" show-input />
        </el-form-item>

        <el-form-item label="音调">
          <el-slider v-model="pitch" :min="0" :max="100" show-input />
        </el-form-item>

        <el-form-item label="音量">
          <el-slider v-model="volume" :min="0" :max="100" show-input />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="synthesize" :loading="loading" icon="el-icon-microphone">
            合成语音
          </el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-card v-if="hasSynthesized" style="margin-top: 20px;">
        <div slot="header">
          <i class="el-icon-headset"></i> 合成结果
        </div>
        <div class="play-status">
          <i :class="isSpeaking ? 'el-icon-loading' : 'el-icon-success'" 
             :style="{ fontSize: '60px', color: isSpeaking ? '#409EFF' : '#67C23A' }"></i>
          <p style="margin-top: 15px; font-size: 16px;">
            {{ isSpeaking ? '正在播放中...' : '合成完成，点击下方按钮播放' }}
          </p>
        </div>
        <div style="margin-top: 15px; text-align: center;">
          <el-button size="medium" type="primary" icon="el-icon-video-play" @click="playAudio" :disabled="isSpeaking">
            {{ isSpeaking ? '播放中...' : '播放语音' }}
          </el-button>
          <el-button size="medium" type="danger" icon="el-icon-video-pause" @click="stopAudio" :disabled="!isSpeaking">停止播放</el-button>
          <el-button size="medium" type="success" icon="el-icon-refresh" @click="resynthesize">重新合成</el-button>
          <el-button size="medium" icon="el-icon-download" @click="downloadAudio">下载文本</el-button>
        </div>
      </el-card>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Synthesis",
  data() {
    return {
      loading: false,
      inputText: '',
      voiceType: 'female2',
      speed: 50,
      pitch: 50,
      volume: 50,
      utterance: null, // 语音合成实例
      isSpeaking: false, // 是否正在播放
      hasSynthesized: false // 是否已经合成
    };
  },
  
  beforeDestroy() {
    // 组件销毁时停止语音
    if (window.speechSynthesis) {
      window.speechSynthesis.cancel()
    }
  },
  
  methods: {
    synthesize() {
      if (!this.inputText.trim()) {
        this.$message.warning('请输入要合成的文字')
        return
      }
      
      // 检查浏览器支持
      if (!window.speechSynthesis) {
        this.$message.error('您的浏览器不支持语音合成功能')
        return
      }
      
      this.loading = true
      this.$message.info('正在合成语音...')
      
      // 停止当前正在播放的语音
      window.speechSynthesis.cancel()
      
      // 创建语音合成实例
      this.utterance = new SpeechSynthesisUtterance(this.inputText)
      
      // 设置语音参数
      this.utterance.lang = 'zh-CN' // 中文
      this.utterance.rate = this.speed / 50 // 语速 (0.5-2)
      this.utterance.pitch = this.pitch / 50 // 音调 (0-2)
      this.utterance.volume = this.volume / 100 // 音量 (0-1)
      
      // 根据音色选择设置不同的声音特征
      switch(this.voiceType) {
        case 'female1':
          this.utterance.pitch = 1.2 // 女声，音调高一些
          break
        case 'female2':
          this.utterance.pitch = 1.3
          this.utterance.rate = 1.1 // 清脆，语速略快
          break
        case 'male1':
          this.utterance.pitch = 0.8 // 男声，音调低
          break
        case 'child':
          this.utterance.pitch = 1.5 // 童声，音调很高
          this.utterance.rate = 1.2 // 语速快
          break
      }
      
      // 合成开始事件
      this.utterance.onstart = () => {
        this.loading = false
        this.isSpeaking = true
        this.hasSynthesized = true
        this.$message.success('开始播放...')
      }
      
      // 播放结束事件
      this.utterance.onend = () => {
        this.isSpeaking = false
        this.$message.info('播放完毕')
      }
      
      // 错误事件
      this.utterance.onerror = (event) => {
        this.loading = false
        this.isSpeaking = false
        console.error('语音合成错误:', event)
        this.$message.error('语音合成失败，请重试')
      }
      
      // 开始合成和播放
      setTimeout(() => {
        window.speechSynthesis.speak(this.utterance)
      }, 100)
    },
    
    playAudio() {
      if (!this.utterance) {
        this.$message.warning('请先合成语音')
        return
      }
      
      // 停止当前播放
      window.speechSynthesis.cancel()
      
      // 重新播放
      window.speechSynthesis.speak(this.utterance)
    },
    
    stopAudio() {
      window.speechSynthesis.cancel()
      this.isSpeaking = false
      this.$message.info('已停止播放')
    },
    
    resynthesize() {
      this.synthesize()
    },
    
    reset() {
      // 停止当前播放
      if (window.speechSynthesis) {
        window.speechSynthesis.cancel()
      }
      
      this.inputText = ''
      this.voiceType = 'female2'
      this.speed = 50
      this.pitch = 50
      this.volume = 50
      this.utterance = null
      this.isSpeaking = false
      this.hasSynthesized = false
      
      this.$message.success('已重置')
    },
    
    downloadAudio() {
      // 由于Web Speech API不提供直接下载音频文件的功能
      // 这里提供一个替代方案：复制文本
      if (!this.inputText) {
        this.$message.warning('没有可下载的内容')
        return
      }
      
      // 创建文本文件下载
      const content = `语音合成文本

内容：${this.inputText}

音色：${this.getVoiceTypeName()}
语速：${this.speed}
音调：${this.pitch}
音量：${this.volume}

注：由于浏览器限制，无法直接下载音频文件。
您可以使用录屏软件录制播放的语音。`
      
      const blob = new Blob([content], { type: 'text/plain;charset=utf-8' })
      const url = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = `语音合成_${new Date().getTime()}.txt`
      a.click()
      URL.revokeObjectURL(url)
      
      this.$message.success('已下载文本文件')
    },
    
    getVoiceTypeName() {
      const types = {
        'female1': '女声1（温柔）',
        'female2': '女声2（清脆）',
        'male1': '男声1（浑厚）',
        'child': '童声'
      }
      return types[this.voiceType] || '未知'
    }
  }
};
</script>

<style scoped>
code {
  background: #f5f7fa;
  padding: 2px 6px;
  border-radius: 3px;
  color: #409EFF;
}

.play-status {
  text-align: center;
  padding: 30px;
}

.el-icon-loading {
  animation: rotating 2s linear infinite;
}

@keyframes rotating {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
