<template>
  <div class="tone-match-game">
    <div class="game-content">
      <!-- 得分显示 -->
      <div class="score-board">
        <div class="score-item">
          <span class="label">已答题目</span>
          <span class="value">{{ currentIndex }} / {{ totalQuestions }}</span>
        </div>
        <div class="score-item">
          <span class="label">正确率</span>
          <span class="value">{{ accuracyRate }}%</span>
        </div>
      </div>

      <!-- 题目显示 -->
      <div class="question-area" v-if="currentQuestion">
        <!-- 汉字显示 -->
        <div class="character-display">
          <div class="main-char">{{ currentQuestion.char }}</div>
          <div class="char-meaning">{{ currentQuestion.meaning }}</div>
        </div>

        <!-- 声调说明 -->
        <div class="tone-guide">
          <div class="guide-item">
            <span class="tone-mark">ˉ</span>
            <span class="tone-name">一声（阴平）</span>
          </div>
          <div class="guide-item">
            <span class="tone-mark">ˊ</span>
            <span class="tone-name">二声（阳平）</span>
          </div>
          <div class="guide-item">
            <span class="tone-mark">ˇ</span>
            <span class="tone-name">三声（上声）</span>
          </div>
          <div class="guide-item">
            <span class="tone-mark">ˋ</span>
            <span class="tone-name">四声（去声）</span>
          </div>
        </div>

        <!-- 选项 -->
        <div class="options">
          <div 
            v-for="(option, index) in currentQuestion.options" 
            :key="index"
            class="option-tone"
            :class="{ 
              'selected': selectedAnswer === index,
              'correct': showResult && index === currentQuestion.correctIndex,
              'wrong': showResult && selectedAnswer === index && index !== currentQuestion.correctIndex
            }"
            @click="selectAnswer(index)"
          >
            <div class="tone-number">{{ option.tone }}声</div>
            <div class="tone-pinyin">{{ option.pinyin }}</div>
            <div class="tone-symbol">{{ getToneSymbol(option.tone) }}</div>
          </div>
        </div>

        <!-- 结果提示 -->
        <div v-if="showResult" class="result-message" :class="isCorrect ? 'correct' : 'wrong'">
          <i :class="isCorrect ? 'el-icon-success' : 'el-icon-error'"></i>
          <span v-if="isCorrect">
            回答正确！{{ currentQuestion.char }} 的读音是 {{ currentQuestion.options[currentQuestion.correctIndex].pinyin }}
          </span>
          <span v-else>
            回答错误！正确答案是 {{ currentQuestion.options[currentQuestion.correctIndex].tone }}声（{{ currentQuestion.options[currentQuestion.correctIndex].pinyin }}）
          </span>
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
        <h3>游戏结束！</h3>
        <p class="final-score">正确率：<span>{{ accuracyRate }}%</span></p>
        <p>正确：{{ correctCount }} 题，错误：{{ wrongCount }} 题</p>
        <el-button type="primary" @click="restartGame">重新开始</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ToneMatch',
  data() {
    return {
      questionBank: [
        { char: '妈', meaning: '母亲', options: [
          { tone: 1, pinyin: 'mā' }, { tone: 2, pinyin: 'má' },
          { tone: 3, pinyin: 'mǎ' }, { tone: 4, pinyin: 'mà' }
        ], correctIndex: 0 },
        { char: '麻', meaning: '麻木', options: [
          { tone: 1, pinyin: 'mā' }, { tone: 2, pinyin: 'má' },
          { tone: 3, pinyin: 'mǎ' }, { tone: 4, pinyin: 'mà' }
        ], correctIndex: 1 },
        { char: '马', meaning: '马匹', options: [
          { tone: 1, pinyin: 'mā' }, { tone: 2, pinyin: 'má' },
          { tone: 3, pinyin: 'mǎ' }, { tone: 4, pinyin: 'mà' }
        ], correctIndex: 2 },
        { char: '骂', meaning: '责骂', options: [
          { tone: 1, pinyin: 'mā' }, { tone: 2, pinyin: 'má' },
          { tone: 3, pinyin: 'mǎ' }, { tone: 4, pinyin: 'mà' }
        ], correctIndex: 3 }
      ],
      currentIndex: 0,
      selectedAnswer: null,
      showResult: false,
      isCorrect: false,
      correctCount: 0,
      wrongCount: 0
    };
  },
  computed: {
    totalQuestions() {
      return this.questionBank.length;
    },
    currentQuestion() {
      return this.currentIndex < this.questionBank.length ? this.questionBank[this.currentIndex] : null;
    },
    accuracyRate() {
      const total = this.correctCount + this.wrongCount;
      return total === 0 ? 0 : Math.round((this.correctCount / total) * 100);
    }
  },
  created() {
    this.initGame();
  },
  methods: {
    initGame() {
      this.questionBank = this.shuffleArray([...this.questionBank]);
      this.currentIndex = 0;
      this.correctCount = 0;
      this.wrongCount = 0;
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
      } else {
        this.wrongCount++;
      }
    },
    nextQuestion() {
      this.currentIndex++;
      this.resetQuestion();
    },
    restartGame() {
      this.initGame();
    },
    getToneSymbol(tone) {
      const symbols = ['ˉ', 'ˊ', 'ˇ', 'ˋ'];
      return symbols[tone - 1] || '';
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
.tone-match-game {
  padding: 20px;
}

.score-board {
  display: flex;
  justify-content: center;
  gap: 40px;
  margin-bottom: 30px;
  padding: 20px;
  background: linear-gradient(135deg, #E6A23C 0%, #F56C6C 100%);
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
    }
  }
}

.character-display {
  text-align: center;
  margin-bottom: 30px;
  padding: 30px;
  background: linear-gradient(135deg, #FFF7E6 0%, #FFE7BA 100%);
  border-radius: 16px;
  
  .main-char {
    font-size: 80px;
    font-weight: bold;
    color: #E6A23C;
  }
  
  .char-meaning {
    font-size: 18px;
    color: #606266;
    margin-top: 10px;
  }
}

.tone-guide {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-bottom: 25px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 8px;
  flex-wrap: wrap;
  
  .guide-item {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;
    color: #606266;
    
    .tone-mark {
      font-size: 20px;
      font-weight: bold;
      color: #E6A23C;
    }
  }
}

.options {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
  margin-bottom: 25px;
  
  .option-tone {
    padding: 25px;
    border: 3px solid #DCDFE6;
    border-radius: 12px;
    cursor: pointer;
    transition: all 0.3s;
    text-align: center;
    
    &:hover {
      border-color: #E6A23C;
      transform: translateY(-3px);
    }
    
    &.selected {
      border-color: #E6A23C;
      background: rgba(230, 162, 60, 0.1);
    }
    
    &.correct {
      border-color: #67C23A;
      background: rgba(103, 194, 58, 0.1);
    }
    
    &.wrong {
      border-color: #F56C6C;
      background: rgba(245, 108, 108, 0.1);
    }
    
    .tone-number {
      font-size: 20px;
      font-weight: bold;
    }
    
    .tone-pinyin {
      font-size: 24px;
      color: #E6A23C;
      margin: 8px 0;
      font-weight: 600;
    }
    
    .tone-symbol {
      font-size: 32px;
      color: #909399;
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
    font-size: 28px;
    margin-bottom: 20px;
  }
  
  .final-score {
    font-size: 20px;
    margin: 15px 0;
    
    span {
      font-size: 36px;
      font-weight: bold;
      color: #E6A23C;
    }
  }
}
</style>
