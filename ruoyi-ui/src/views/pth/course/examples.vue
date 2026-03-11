<template>
  <div class="examples-container">
    <div class="page-header">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/pth/course' }">课程中心</el-breadcrumb-item>
        <el-breadcrumb-item>日常语音示例</el-breadcrumb-item>
      </el-breadcrumb>
      <h1>🎧 日常语音示例</h1>
      <p>常用语句、对话场景示范</p>
    </div>

    <!-- 场景分类 -->
    <el-row :gutter="20">
      <el-col :xs="24" :sm="12" :lg="8" v-for="(category, index) in categories" :key="index">
        <el-card shadow="hover" class="category-card">
          <div class="category-icon">
            <i :class="category.icon" style="font-size: 48px;"></i>
          </div>
          <h3>{{ category.title }}</h3>
          <p>{{ category.desc }}</p>
          <div class="example-list">
            <div v-for="(example, idx) in category.examples" :key="idx" 
                 class="example-item" 
                 :class="{ 'is-playing': currentPlaying === example }"
                 @click="playExample(example)">
              <i class="el-icon-service"></i>
              <span>{{ example }}</span>
              <i v-if="currentPlaying === example" class="el-icon-loading" style="margin-left: auto;"></i>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'VoiceExamples',
  data() {
    return {
      currentPlaying: null, // 当前正在播放的文本
      categories: [
        {
          title: '日常问候',
          desc: '常用的问候语',
          icon: 'el-icon-sunny',
          examples: [
            '您好！',
            '早上好！',
            '晚安！',
            '再见！',
            '谢谢您！',
            '不客气！'
          ]
        },
        {
          title: '购物场景',
          desc: '购物时的常用对话',
          icon: 'el-icon-shopping-cart-2',
          examples: [
            '这个多少钱？',
            '可以便宜一点吗？',
            '我要买这个。',
            '可以刷卡吗？',
            '请给我开发票。',
            '谢谢，再见！'
          ]
        },
        {
          title: '问路指路',
          desc: '问路和指路的表达',
          icon: 'el-icon-map-location',
          examples: [
            '请问去火车站怎么走？',
            '一直往前走。',
            '在第二个路口右转。',
            '就在前面不远处。',
            '谢谢您的帮助！',
            '不用谢！'
          ]
        },
        {
          title: '餐厅用餐',
          desc: '在餐厅的常用语',
          icon: 'el-icon-food',
          examples: [
            '有位子吗？',
            '请给我菜单。',
            '我要点餐。',
            '买单！',
            '味道很好！',
            '谢谢！'
          ]
        },
        {
          title: '电话交流',
          desc: '打电话的常用表达',
          icon: 'el-icon-phone',
          examples: [
            '喂，您好！',
            '请问您找谁？',
            '请稍等。',
            '他不在。',
            '需要留言吗？',
            '再见！'
          ]
        },
        {
          title: '办公场景',
          desc: '办公室常用语',
          icon: 'el-icon-office-building',
          examples: [
            '早上好，同事们！',
            '这个项目怎么样？',
            '我们开会吧。',
            '请查看邮件。',
            '辛苦了！',
            '下班了！'
          ]
        }
      ]
    }
  },
  methods: {
    playExample(text) {
      // 停止当前正在播放的语音
      window.speechSynthesis.cancel()
      
      // 检查浏览器是否支持语音合成
      if (!window.speechSynthesis) {
        this.$message.error('您的浏览器不支持语音播放功能')
        return
      }
      
      this.currentPlaying = text
      
      // 创建语音合成实例
      const utterance = new SpeechSynthesisUtterance(text)
      
      // 设置语音参数
      utterance.lang = 'zh-CN' // 设置为中文
      utterance.rate = 0.9 // 语速（0.1-10，默认1）
      utterance.pitch = 1 // 音调（0-2，默认1）
      utterance.volume = 1 // 音量（0-1，默认1）
      
      // 播放开始事件
      utterance.onstart = () => {
        this.$message.success(`正在播放: ${text}`)
      }
      
      // 播放结束事件
      utterance.onend = () => {
        this.currentPlaying = null
      }
      
      // 播放错误事件
      utterance.onerror = (event) => {
        console.error('语音播放错误:', event)
        this.$message.error('语音播放失败，请重试')
        this.currentPlaying = null
      }
      
      // 开始播放
      window.speechSynthesis.speak(utterance)
    },
    
    stopPlaying() {
      window.speechSynthesis.cancel()
      this.currentPlaying = null
    }
  },
  
  beforeDestroy() {
    // 组件销毁时停止播放
    window.speechSynthesis.cancel()
  }
}
</script>

<style scoped>
.examples-container {
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

.category-card {
  margin-bottom: 20px;
  transition: all 0.3s;
}

.category-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.category-icon {
  text-align: center;
  color: #409EFF;
  margin-bottom: 15px;
}

.category-card h3 {
  font-size: 18px;
  color: #303133;
  text-align: center;
  margin-bottom: 10px;
}

.category-card p {
  font-size: 14px;
  color: #909399;
  text-align: center;
  margin-bottom: 20px;
}

.example-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.example-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.example-item:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  transform: translateX(5px);
}

.example-item.is-playing {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
  transform: translateX(5px);
  box-shadow: 0 4px 12px rgba(240, 147, 251, 0.4);
}

.example-item i {
  font-size: 18px;
}
</style>
