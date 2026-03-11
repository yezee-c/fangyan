<template>
  <div class="reading-container">
    <div class="page-header">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/pth/course' }">课程中心</el-breadcrumb-item>
        <el-breadcrumb-item>字词朗读</el-breadcrumb-item>
      </el-breadcrumb>
      <h1>📖 字词朗读练习</h1>
      <p>声母、韵母、声调专项训练</p>
    </div>

    <!-- 练习分类 -->
    <el-tabs v-model="activeTab" type="card">
      <el-tab-pane label="声母练习" name="shengmu">
        <el-row :gutter="20">
          <el-col :xs="12" :sm="8" :md="6" v-for="(item, index) in shengmuList" :key="index">
            <el-card shadow="hover" 
                     class="reading-item" 
                     :class="{ 'is-playing': currentPlaying === item.pinyin }"
                     @click.native="practice(item)">
              <div class="pinyin">{{ item.pinyin }}</div>
              <div class="example">{{ item.example }}</div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="韵母练习" name="yunmu">
        <el-row :gutter="20">
          <el-col :xs="12" :sm="8" :md="6" v-for="(item, index) in yunmuList" :key="index">
            <el-card shadow="hover" 
                     class="reading-item" 
                     :class="{ 'is-playing': currentPlaying === item.pinyin }"
                     @click.native="practice(item)">
              <div class="pinyin">{{ item.pinyin }}</div>
              <div class="example">{{ item.example }}</div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="声调练习" name="shengdiao">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="8" v-for="(item, index) in shengdiaoList" :key="index">
            <el-card shadow="hover" class="tone-card">
              <h3>{{ item.tone }}</h3>
              <p>{{ item.desc }}</p>
              <div class="example-words">
                <span v-for="(word, idx) in item.words" :key="idx" class="word-item">{{ word }}</span>
              </div>
              <el-button type="primary" size="small" @click="practice(item)">开始练习</el-button>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
export default {
  name: 'ReadingPractice',
  data() {
    return {
      activeTab: 'shengmu',
      currentPlaying: null, // 当前正在播放的项
      shengmuList: [
        { pinyin: 'b', example: '八 bā' },
        { pinyin: 'p', example: '坡 pō' },
        { pinyin: 'm', example: '摸 mō' },
        { pinyin: 'f', example: '佛 fó' },
        { pinyin: 'd', example: '得 dé' },
        { pinyin: 't', example: '特 tè' },
        { pinyin: 'n', example: '呢 ne' },
        { pinyin: 'l', example: '勒 lè' },
        { pinyin: 'g', example: '哥 gē' },
        { pinyin: 'k', example: '科 kē' },
        { pinyin: 'h', example: '喝 hē' },
        { pinyin: 'j', example: '基 jī' },
        { pinyin: 'q', example: '七 qī' },
        { pinyin: 'x', example: '西 xī' },
        { pinyin: 'zh', example: '知 zhī' },
        { pinyin: 'ch', example: '吃 chī' },
        { pinyin: 'sh', example: '诗 shī' },
        { pinyin: 'r', example: '日 rì' },
        { pinyin: 'z', example: '资 zī' },
        { pinyin: 'c', example: '次 cì' },
        { pinyin: 's', example: '思 sī' }
      ],
      yunmuList: [
        { pinyin: 'a', example: '啊 ā' },
        { pinyin: 'o', example: '哦 ō' },
        { pinyin: 'e', example: '鹅 é' },
        { pinyin: 'i', example: '衣 yī' },
        { pinyin: 'u', example: '乌 wū' },
        { pinyin: 'ü', example: '鱼 yú' },
        { pinyin: 'ai', example: '爱 ài' },
        { pinyin: 'ei', example: '诶 éi' },
        { pinyin: 'ui', example: '威 wēi' },
        { pinyin: 'ao', example: '奥 ào' },
        { pinyin: 'ou', example: '欧 ōu' },
        { pinyin: 'iu', example: '优 yōu' },
        { pinyin: 'ie', example: '耶 yē' },
        { pinyin: 'üe', example: '约 yuē' },
        { pinyin: 'er', example: '儿 ér' },
        { pinyin: 'an', example: '安 ān' },
        { pinyin: 'en', example: '恩 ēn' },
        { pinyin: 'in', example: '因 yīn' },
        { pinyin: 'un', example: '温 wēn' },
        { pinyin: 'ün', example: '晕 yūn' },
        { pinyin: 'ang', example: '昂 áng' },
        { pinyin: 'eng', example: '亨 hēng' },
        { pinyin: 'ing', example: '英 yīng' },
        { pinyin: 'ong', example: '翁 wēng' }
      ],
      shengdiaoList: [
        {
          tone: '第一声（阴平）',
          desc: '高平调，调值55',
          words: ['妈mā', '花huā', '天tiān', '书shū', '高gāo']
        },
        {
          tone: '第二声（阳平）',
          desc: '高升调，调值35',
          words: ['麻má', '华huá', '田tián', '熟shú', '豪háo']
        },
        {
          tone: '第三声（上声）',
          desc: '降升调，调值214',
          words: ['马mǎ', '化huà', '点diǎn', '鼠shǔ', '好hǎo']
        },
        {
          tone: '第四声（去声）',
          desc: '全降调，调值51',
          words: ['骂mà', '话huà', '电diàn', '树shù', '号hào']
        }
      ]
    }
  },
  methods: {
    practice(item) {
      // 停止当前正在播放的语音
      window.speechSynthesis.cancel()
      
      // 检查浏览器是否支持语音合成
      if (!window.speechSynthesis) {
        this.$message.error('您的浏览器不支持语音播放功能')
        return
      }
      
      // 准备要播放的文本
      let textToSpeak = ''
      
      if (item.pinyin) {
        // 声母或韵母
        this.currentPlaying = item.pinyin
        // 提取示例中的汉字（去掉拼音）
        textToSpeak = item.example.split(' ')[0]
      } else if (item.tone) {
        // 声调练习
        this.currentPlaying = item.tone
        // 播放所有示例词汇
        textToSpeak = item.words.map(w => w.split(/[a-zāáǎàēéěèīíǐìōóǒòūúǔùǖǘǚǜü]/i)[0]).join('、')
      }
      
      if (!textToSpeak) {
        this.$message.warning('无法获取要播放的内容')
        return
      }
      
      // 创建语音合成实例
      const utterance = new SpeechSynthesisUtterance(textToSpeak)
      
      // 设置语音参数
      utterance.lang = 'zh-CN'
      utterance.rate = 0.8 // 更慢的语速，方便学习
      utterance.pitch = 1
      utterance.volume = 1
      
      // 播放开始事件
      utterance.onstart = () => {
        this.$message.success(`正在播放: ${textToSpeak}`)
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
    }
  },
  
  beforeDestroy() {
    // 组件销毁时停止播放
    window.speechSynthesis.cancel()
  }
}
</script>

<style scoped>
.reading-container {
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

.reading-item {
  margin-bottom: 20px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  min-height: 100px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.reading-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.pinyin {
  font-size: 32px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 10px;
}

.reading-item:hover .pinyin {
  color: white;
}

.example {
  font-size: 16px;
  color: #606266;
}

.reading-item:hover .example {
  color: white;
}

.reading-item.is-playing {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(240, 147, 251, 0.4);
}

.reading-item.is-playing .pinyin,
.reading-item.is-playing .example {
  color: white;
}

.tone-card {
  margin-bottom: 20px;
  text-align: center;
  transition: all 0.3s;
}

.tone-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.tone-card h3 {
  font-size: 18px;
  color: #303133;
  margin-bottom: 10px;
}

.tone-card p {
  font-size: 14px;
  color: #909399;
  margin-bottom: 15px;
}

.example-words {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 10px;
  margin-bottom: 15px;
}

.word-item {
  background: #f5f7fa;
  padding: 8px 15px;
  border-radius: 20px;
  font-size: 14px;
  color: #606266;
}
</style>
