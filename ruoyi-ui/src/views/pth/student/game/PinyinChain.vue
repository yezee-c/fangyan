<template>
  <div class="pinyin-chain-game">
    <div class="game-content">
      <!-- 得分显示 -->
      <div class="score-board">
        <div class="score-item">
          <span class="label">当前连击</span>
          <span class="value">{{ combo }}</span>
        </div>
        <div class="score-item">
          <span class="label">正确</span>
          <span class="value correct">{{ correctCount }}</span>
        </div>
        <div class="score-item">
          <span class="label">错误</span>
          <span class="value wrong">{{ wrongCount }}</span>
        </div>
      </div>

      <!-- 题目显示 -->
      <div class="question-area" v-if="currentQuestion">
        <div class="chain-display">
          <div v-for="(item, index) in chain" :key="index" class="chain-item">
            <div class="char">{{ item.char }}</div>
            <div class="pinyin">{{ item.pinyin }}</div>
          </div>
          <div class="chain-item next">
            <div class="char">?</div>
            <div class="pinyin">{{ currentQuestion.nextPinyin }}</div>
          </div>
        </div>

        <div class="hint">
          <i class="el-icon-info"></i>
          找出拼音为 <strong>{{ currentQuestion.nextPinyin }}</strong> 的字
        </div>

        <!-- 选项 -->
        <div class="options">
          <div 
            v-for="(option, index) in currentQuestion.options" 
            :key="index"
            class="option-card"
            :class="{ 
              'selected': selectedAnswer === index,
              'correct': showResult && index === currentQuestion.correctIndex,
              'wrong': showResult && selectedAnswer === index && index !== currentQuestion.correctIndex
            }"
            @click="selectAnswer(index)"
          >
            <div class="option-char">{{ option.char }}</div>
            <div class="option-pinyin">{{ option.pinyin }}</div>
          </div>
        </div>

        <!-- 结果提示 -->
        <div v-if="showResult" class="result-message" :class="isCorrect ? 'correct' : 'wrong'">
          <i :class="isCorrect ? 'el-icon-success' : 'el-icon-error'"></i>
          {{ isCorrect ? '回答正确！' : '回答错误！正确答案是：' + currentQuestion.options[currentQuestion.correctIndex].char }}
        </div>

        <!-- 按钮 -->
        <div class="action-buttons">
          <el-button v-if="!showResult" type="primary" @click="submitAnswer" :disabled="selectedAnswer === null">
            提交答案
          </el-button>
          <el-button v-else type="success" @click="nextQuestion">
            下一题 <i class="el-icon-right"></i>
          </el-button>
        </div>
      </div>

      <!-- 游戏结束 -->
      <div v-else class="game-over">
        <h3>题库已完成！</h3>
        <p>正确：{{ correctCount }} 题，错误：{{ wrongCount }} 题</p>
        <el-button type="primary" @click="restartGame">重新开始</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PinyinChain',
  data() {
    return {
      questionBank: [
        { char: '天', pinyin: 'tiān', nextPinyin: 'ān', options: [
          { char: '安', pinyin: 'ān' }, { char: '岸', pinyin: 'àn' }, 
          { char: '暗', pinyin: 'àn' }, { char: '案', pinyin: 'àn' }
        ], correctIndex: 0 },
        { char: '快', pinyin: 'kuài', nextPinyin: 'ài', options: [
          { char: '爱', pinyin: 'ài' }, { char: '碍', pinyin: 'ài' }, 
          { char: '艾', pinyin: 'ài' }, { char: '哀', pinyin: 'āi' }
        ], correctIndex: 0 },
        { char: '明', pinyin: 'míng', nextPinyin: 'ing', options: [
          { char: '英', pinyin: 'yīng' }, { char: '影', pinyin: 'yǐng' }, 
          { char: '应', pinyin: 'yìng' }, { char: '赢', pinyin: 'yíng' }
        ], correctIndex: 1 },
        { char: '学', pinyin: 'xué', nextPinyin: 'é', options: [
          { char: '鹅', pinyin: 'é' }, { char: '额', pinyin: 'é' }, 
          { char: '饿', pinyin: 'è' }, { char: '恶', pinyin: 'è' }
        ], correctIndex: 0 }
      ],
      currentIndex: 0,
      chain: [],
      selectedAnswer: null,
      showResult: false,
      isCorrect: false,
      correctCount: 0,
      wrongCount: 0,
      combo: 0
    };
  },
  computed: {
    currentQuestion() {
      return this.currentIndex < this.questionBank.length ? this.questionBank[this.currentIndex] : null;
    }
  },
  created() {
    this.initGame();
  },
  methods: {
    initGame() {
      this.currentIndex = 0;
      this.chain = [];
      this.correctCount = 0;
      this.wrongCount = 0;
      this.combo = 0;
      this.resetQuestion();
    },
    resetQuestion() {
      this.selectedAnswer = null;
      this.showResult = false;
      this.isCorrect = false;
    },
    selectAnswer(index) {
      if (!this.showResult) this.selectedAnswer = index;
    },
    submitAnswer() {
      if (this.selectedAnswer === null) return;
      this.showResult = true;
      this.isCorrect = this.selectedAnswer === this.currentQuestion.correctIndex;
      if (this.isCorrect) {
        this.correctCount++;
        this.combo++;
        this.chain.push({ char: this.currentQuestion.char, pinyin: this.currentQuestion.pinyin });
        if (this.chain.length > 5) this.chain.shift();
      } else {
        this.wrongCount++;
        this.combo = 0;
      }
    },
    nextQuestion() {
      this.currentIndex++;
      this.resetQuestion();
    },
    restartGame() {
      this.questionBank = this.shuffleArray([...this.questionBank]);
      this.initGame();
    },
    shuffleArray(array) {
      for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
      }
      return array;
    }
  }
};
</script>

<style scoped lang="scss">
.pinyin-chain-game {
  padding: 20px;
}

.score-board {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin-bottom: 30px;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  color: white;
  
  .score-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 5px;
    
    .label {
      font-size: 12px;
      opacity: 0.9;
    }
    
    .value {
      font-size: 28px;
      font-weight: bold;
      
      &.correct { color: #67C23A; }
      &.wrong { color: #F56C6C; }
    }
  }
}

.chain-display {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-bottom: 20px;
  flex-wrap: wrap;
  
  .chain-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 15px;
    background: #f5f7fa;
    border-radius: 12px;
    min-width: 70px;
    
    &.next {
      background: linear-gradient(135deg, #667eea20 0%, #764ba220 100%);
      border: 2px dashed #667eea;
    }
    
    .char {
      font-size: 32px;
      font-weight: bold;
      color: #303133;
    }
    
    .pinyin {
      font-size: 14px;
      color: #667eea;
    }
  }
}

.hint {
  text-align: center;
  padding: 15px;
  background: #ecf5ff;
  border-radius: 8px;
  color: #409EFF;
  margin-bottom: 25px;
  
  strong {
    font-size: 18px;
    color: #667eea;
  }
}

.options {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
  margin-bottom: 25px;
  
  .option-card {
    padding: 20px;
    border: 2px solid #DCDFE6;
    border-radius: 12px;
    cursor: pointer;
    transition: all 0.3s;
    text-align: center;
    
    &:hover {
      border-color: #667eea;
      transform: translateY(-2px);
    }
    
    &.selected {
      border-color: #667eea;
      background: rgba(102, 126, 234, 0.1);
    }
    
    &.correct {
      border-color: #67C23A;
      background: rgba(103, 194, 58, 0.1);
    }
    
    &.wrong {
      border-color: #F56C6C;
      background: rgba(245, 108, 108, 0.1);
    }
    
    .option-char {
      font-size: 36px;
      font-weight: bold;
    }
    
    .option-pinyin {
      font-size: 14px;
      color: #909399;
      margin-top: 8px;
    }
  }
}

.result-message {
  text-align: center;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 20px;
  font-size: 16px;
  
  &.correct {
    background: #f0f9ff;
    color: #67C23A;
  }
  
  &.wrong {
    background: #fef0f0;
    color: #F56C6C;
  }
}

.action-buttons {
  text-align: center;
}

.game-over {
  text-align: center;
  padding: 50px 20px;
  
  h3 {
    font-size: 24px;
    margin-bottom: 20px;
  }
}
</style>
