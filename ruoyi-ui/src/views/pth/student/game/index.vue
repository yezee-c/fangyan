<template>
  <div class="app-container game">
    <!-- 游戏选择页 -->
    <div v-if="!currentGame">
      <el-row :gutter="20">
        <el-col :span="8" v-for="game in gameList" :key="game.id">
          <el-card shadow="hover" class="game-card" @click.native="playGame(game)">
            <div class="game-icon">
              <i :class="game.icon" :style="{ color: game.color }"></i>
            </div>
            <h3>{{ game.name }}</h3>
            <p>{{ game.desc }}</p>
            <div class="game-tags">
              <el-tag v-for="tag in game.tags" :key="tag" size="small" type="info">{{ tag }}</el-tag>
            </div>
            <el-button type="primary" size="small" round style="margin-top: 15px;">
              开始游戏
            </el-button>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <!-- 游戏区域 -->
    <div v-else class="game-area">
      <div class="game-header">
        <el-button @click="backToMenu" icon="el-icon-back">返回菜单</el-button>
        <h2>{{ currentGameName }}</h2>
        <div></div>
      </div>
      <component :is="currentGameComponent" />
    </div>
  </div>
</template>

<script>
import PinyinChain from './PinyinChain.vue'
import ToneMatch from './ToneMatch.vue'
import ErrorWords from './ErrorWords.vue'

export default {
  name: "Game",
  components: {
    PinyinChain,
    ToneMatch,
    ErrorWords
  },
  data() {
    return {
      currentGame: null,
      gameList: [
        { 
          id: 'pinyin-chain', 
          name: '拼音接龙', 
          desc: '根据拼音选择正确的汉字，锻炼拼音识别能力', 
          icon: 'el-icon-link', 
          color: '#667eea',
          tags: ['拼音', '识字']
        },
        { 
          id: 'tone-match', 
          name: '声调匹配', 
          desc: '听音辨调，选择正确的声调，掌握四声变化', 
          icon: 'el-icon-microphone', 
          color: '#E6A23C',
          tags: ['声调', '辨音']
        },
        { 
          id: 'error-words', 
          name: '易错字词', 
          desc: '找出句子中的错别字并改正，提高汉字识别', 
          icon: 'el-icon-edit-outline', 
          color: '#F56C6C',
          tags: ['汉字', '纠错']
        }
      ]
    };
  },
  computed: {
    currentGameComponent() {
      const componentMap = {
        'pinyin-chain': 'PinyinChain',
        'tone-match': 'ToneMatch',
        'error-words': 'ErrorWords'
      };
      return componentMap[this.currentGame];
    },
    currentGameName() {
      const game = this.gameList.find(g => g.id === this.currentGame);
      return game ? game.name : '';
    }
  },
  methods: {
    playGame(game) {
      this.currentGame = game.id;
    },
    backToMenu() {
      this.currentGame = null;
    }
  }
};
</script>

<style scoped lang="scss">
.game-card {
  cursor: pointer;
  text-align: center;
  margin-bottom: 20px;
  transition: all 0.3s;
  min-height: 300px;
  display: flex;
  flex-direction: column;
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  }
  
  .game-icon i {
    font-size: 60px;
    margin: 20px 0;
  }
  
  h3 {
    margin: 15px 0 10px;
    font-size: 22px;
    font-weight: 600;
  }
  
  p {
    color: #606266;
    font-size: 14px;
    line-height: 1.6;
    min-height: 50px;
  }
  
  .game-tags {
    display: flex;
    justify-content: center;
    gap: 8px;
    margin-top: 15px;
  }
}

.game-area {
  .game-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding-bottom: 15px;
    border-bottom: 2px solid #f0f2f5;
    
    h2 {
      margin: 0;
      font-size: 24px;
      color: #303133;
    }
  }
}
</style>
