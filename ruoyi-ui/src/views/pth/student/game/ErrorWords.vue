<template>
  <div class="error-words-game">
    <div class="game-content">
      <!-- 得分显示 -->
      <div class="score-board">
        <div class="score-item">
          <span class="label">进度</span>
          <span class="value">{{ currentIndex + 1 }} / {{ totalQuestions }}</span>
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
        <!-- 句子显示 -->
        <div class="sentence-display">
          <div class="sentence-label">请找出下列句子中的错别字：</div>
          <div class="sentence-content">
            <span 
              v-for="(char, index) in currentQuestion.sentence" 
              :key="index"
              class="sentence-char"
              :class="{ 
                'error': showResult && index === currentQuestion.errorIndex,
                'selected': selectedCharIndex === index
              }"
              @click="selectChar(index)"
            >
              {{ char }}
            </span>
          </div>
        </div>

        <!-- 提示信息 -->
        <div class="hint-box" v-if="!showResult">
          <i class="el-icon-info"></i>
          点击你认为是错别字的位置
        </div>

        <!-- 改正输入 -->
        <div v-if="selectedCharIndex !== null && !showResult" class="correction-area">
          <div class="correction-label">
            你选择的字是：<strong>{{ currentQuestion.sentence[selectedCharIndex] }}</strong>
          </div>
          <div class="correction-input">
            <span class="input-label">改正为：</span>
            <el-input 
              v-model="correctionInput" 
              placeholder="请输入正确的字"
              maxlength="1"
              style="max-width: 200px;"
              @keyup.enter.native="submitAnswer"
            />
          </div>
        </div>

        <!-- 结果显示 -->
        <div v-if="showResult" class="result-area">
          <div class="result-message" :class="isCorrect ? 'correct' : 'wrong'">
            <i :class="isCorrect ? 'el-icon-success' : 'el-icon-error'"></i>
            <span v-if="isCorrect">回答正确！</span>
            <span v-else>回答错误！</span>
          </div>
          
          <div class="answer-detail">
            <div class="detail-row">
              <span class="detail-label">错误的字：</span>
              <span class="detail-value error-char">{{ currentQuestion.sentence[currentQuestion.errorIndex] }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">正确的字：</span>
              <span class="detail-value correct-char">{{ currentQuestion.correctChar }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">正确句子：</span>
              <span class="detail-value">{{ correctSentence }}</span>
            </div>
            <div class="explanation" v-if="currentQuestion.explanation">
              <div class="explanation-label">
                <i class="el-icon-document"></i> 解释：
              </div>
              <div class="explanation-text">{{ currentQuestion.explanation }}</div>
            </div>
          </div>
        </div>

        <!-- 按钮 -->
        <div class="action-buttons">
          <el-button 
            v-if="!showResult" 
            type="primary" 
            @click="submitAnswer" 
            :disabled="selectedCharIndex === null || !correctionInput"
          >
            提交答案
          </el-button>
          <el-button v-else type="success" @click="nextQuestion">
            下一题 <i class="el-icon-right"></i>
          </el-button>
        </div>
      </div>

      <!-- 游戏结束 -->
      <div v-else class="game-over">
        <h3>练习完成！</h3>
        <p class="final-score">正确率：<span>{{ accuracyRate }}%</span></p>
        <p>正确：{{ correctCount }} 题，错误：{{ wrongCount }} 题</p>
        <el-button type="primary" @click="restartGame">重新开始</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ErrorWords',
  data() {
    return {
      questionBank: [
        { sentence: '他的学习成绩一直名列前矛。', errorIndex: 10, correctChar: '茅',
          explanation: '"名列前茅"是成语，茅指茅草，比喻名次排在前面。' },
        { sentence: '这件事情已经尘埃落定了。', errorIndex: 7, correctChar: '定',
          explanation: '"尘埃落定"表示事情有了结局，用"定"而不是"顶"。' },
        { sentence: '他因为工作出色而倍受赞扬。', errorIndex: 10, correctChar: '备',
          explanation: '"备受"表示完全受到，"倍"是加倍的意思。' },
        { sentence: '这道题的答案不容质疑。', errorIndex: 8, correctChar: '置',
          explanation: '"不容置疑"，置是放置的意思，表示不容许有怀疑。' }
      ],
      currentIndex: 0,
      selectedCharIndex: null,
      correctionInput: '',
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
    correctSentence() {
      if (!this.currentQuestion) return '';
      const chars = this.currentQuestion.sentence.split('');
      chars[this.currentQuestion.errorIndex] = this.currentQuestion.correctChar;
      return chars.join('');
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
      this.selectedCharIndex = null;
      this.correctionInput = '';
      this.showResult = false;
      this.isCorrect = false;
    },
    selectChar(index) {
      if (!this.showResult) {
        this.selectedCharIndex = index;
        this.correctionInput = '';
      }
    },
    submitAnswer() {
      if (this.selectedCharIndex === null || !this.correctionInput) return;
      this.showResult = true;
      this.isCorrect = 
        this.selectedCharIndex === this.currentQuestion.errorIndex && 
        this.correctionInput === this.currentQuestion.correctChar;
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
.error-words-game {
  padding: 20px;
}

.score-board {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin-bottom: 30px;
  padding: 20px;
  background: linear-gradient(135deg, #F56C6C 0%, #FF9999 100%);
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
      &.wrong { color: #FFF; }
    }
  }
}

.sentence-display {
  margin-bottom: 25px;
  padding: 25px;
  background: #FFF5F5;
  border-radius: 12px;
  border: 2px solid #FFE0E0;
  
  .sentence-label {
    font-size: 16px;
    color: #606266;
    margin-bottom: 15px;
    font-weight: 500;
  }
  
  .sentence-content {
    font-size: 24px;
    line-height: 2;
    color: #303133;
    
    .sentence-char {
      display: inline-block;
      padding: 5px 8px;
      margin: 2px;
      border-radius: 6px;
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover {
        background: rgba(245, 108, 108, 0.1);
        transform: scale(1.1);
      }
      
      &.selected {
        background: rgba(245, 108, 108, 0.3);
        color: #F56C6C;
        font-weight: bold;
      }
      
      &.error {
        background: #F56C6C;
        color: white;
        font-weight: bold;
      }
    }
  }
}

.hint-box {
  text-align: center;
  padding: 12px;
  background: #FFF0F0;
  border-radius: 8px;
  color: #F56C6C;
  margin-bottom: 20px;
  font-size: 14px;
}

.correction-area {
  padding: 20px;
  background: #f5f7fa;
  border-radius: 12px;
  margin-bottom: 20px;
  
  .correction-label {
    font-size: 16px;
    color: #606266;
    margin-bottom: 15px;
    
    strong {
      color: #F56C6C;
      font-size: 20px;
      margin: 0 5px;
    }
  }
  
  .correction-input {
    display: flex;
    align-items: center;
    gap: 15px;
    
    .input-label {
      font-size: 16px;
      color: #606266;
    }
  }
}

.result-message {
  text-align: center;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 20px;
  font-size: 18px;
  
  &.correct {
    background: #f0f9ff;
    color: #67C23A;
  }
  
  &.wrong {
    background: #fef0f0;
    color: #F56C6C;
  }
}

.answer-detail {
  padding: 20px;
  background: #f5f7fa;
  border-radius: 12px;
  
  .detail-row {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
    font-size: 16px;
    
    .detail-label {
      min-width: 100px;
      color: #606266;
      font-weight: 500;
    }
    
    .detail-value {
      color: #303133;
      flex: 1;
      
      &.error-char {
        color: #F56C6C;
        font-size: 20px;
        font-weight: bold;
        text-decoration: line-through;
      }
      
      &.correct-char {
        color: #67C23A;
        font-size: 20px;
        font-weight: bold;
      }
    }
  }
  
  .explanation {
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #DCDFE6;
    
    .explanation-label {
      font-size: 14px;
      color: #909399;
      margin-bottom: 10px;
    }
    
    .explanation-text {
      font-size: 14px;
      color: #606266;
      line-height: 1.8;
      padding: 12px;
      background: white;
      border-radius: 8px;
      border-left: 3px solid #F56C6C;
    }
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
      color: #F56C6C;
    }
  }
}
</style>
