<template>
  <div class="pth-home">
    <!-- 沉浸式滚动模式 -->
    <div v-if="displayMode === 'immersive'" class="immersive-mode">
      <div
        v-for="(quote, index) in quoteList"
        :key="quote.quoteId"
        class="quote-slide"
        :style="getSlideStyle(quote)"
      >
        <div class="quote-overlay"></div>
        <div class="quote-content">
          <h1 class="celebrity-name">{{ quote.celebrityName }}</h1>
          <p class="quote-text">"{{ quote.quoteContent }}"</p>
          <div class="quote-bio" v-if="quote.biography">
            <p>{{ quote.biography }}</p>
          </div>
        </div>
      </div>
      <div class="scroll-indicator">
        <i class="el-icon-arrow-down"></i>
        <span>滑动查看更多</span>
      </div>
    </div>

    <!-- 侧边联动模式 -->
    <div v-else class="sidebar-mode">
      <div class="photo-sidebar">
        <div
          v-for="(quote, index) in quoteList"
          :key="'photo-' + quote.quoteId"
          class="photo-item"
          :class="{ active: activeIndex === index }"
          :style="getPhotoStyle(quote, index)"
        >
          <img v-if="quote.photoUrl" :src="quote.photoUrl" :alt="quote.celebrityName" />
          <div v-else class="photo-placeholder">{{ quote.celebrityName }}</div>
        </div>
      </div>
      <div class="content-scroll" @scroll="handleScroll">
        <div
          v-for="(quote, index) in quoteList"
          :key="'content-' + quote.quoteId"
          class="content-item"
          :data-index="index"
        >
          <h2 class="celebrity-name">{{ quote.celebrityName }}</h2>
          <p class="quote-text">"{{ quote.quoteContent }}"</p>
          <div class="quote-details">
            <p class="biography" v-if="quote.biography">
              <strong>简介：</strong>{{ quote.biography }}
            </p>
            <p class="achievements" v-if="quote.achievements">
              <strong>成就：</strong>{{ quote.achievements }}
            </p>
            <p class="connection" v-if="quote.connection">
              <strong>与大别山的关联：</strong>{{ quote.connection }}
            </p>
          </div>
        </div>
      </div>
    </div>

    <!-- 登录入口 -->
    <div class="login-entrance">
      <el-button type="primary" icon="el-icon-user" @click="goToLogin">进入系统</el-button>
    </div>

    <!-- 平台简介 -->
    <div class="platform-intro" v-if="showIntro">
      <div class="intro-content">
        <h2>普通话学习与测评平台</h2>
        <p>传承大别山文化，提升普通话水平</p>
        <ul>
          <li>方言识别与转译</li>
          <li>普通话水平测评</li>
          <li>趣味学习游戏</li>
          <li>智能语音合成</li>
        </ul>
        <el-button @click="showIntro = false">关闭</el-button>
      </div>
    </div>

    <!-- 简介按钮 -->
    <div class="intro-button">
      <el-button circle icon="el-icon-info" @click="showIntro = true"></el-button>
    </div>
  </div>
</template>

<script>
import { listDisplayQuotes } from "@/api/pth/quote";

export default {
  name: "PthHome",
  data() {
    return {
      quoteList: [],
      displayMode: "immersive", // immersive 或 sidebar
      activeIndex: 0,
      showIntro: false
    };
  },
  created() {
    this.getDisplayMode();
    this.getQuoteList();
  },
  methods: {
    // 获取展示模式配置
    getDisplayMode() {
      // 从系统配置获取或使用默认值
      this.displayMode = "immersive";
    },
    // 获取名人名言列表
    getQuoteList() {
      listDisplayQuotes().then(response => {
        this.quoteList = response.data || [];
      });
    },
    // 获取幻灯片样式（沉浸式模式）
    getSlideStyle(quote) {
      if (quote.photoUrl) {
        return {
          backgroundImage: `url(${quote.photoUrl})`,
          backgroundSize: "cover",
          backgroundPosition: "center"
        };
      }
      return {
        background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)"
      };
    },
    // 获取照片样式（侧边联动模式）
    getPhotoStyle(quote, index) {
      const scale = this.activeIndex === index ? 1.1 : 1;
      return {
        transform: `scale(${scale})`,
        transition: "transform 0.3s ease"
      };
    },
    // 处理滚动（侧边联动模式）
    handleScroll(event) {
      const scrollTop = event.target.scrollTop;
      const items = event.target.querySelectorAll(".content-item");
      
      items.forEach((item, index) => {
        const rect = item.getBoundingClientRect();
        if (rect.top >= 0 && rect.top < window.innerHeight / 2) {
          this.activeIndex = index;
        }
      });
    },
    // 跳转到登录页
    goToLogin() {
      this.$router.push("/login");
    }
  }
};
</script>

<style scoped lang="scss">
.pth-home {
  width: 100%;
  min-height: 100vh;
  position: relative;
  overflow: hidden;
}

/* 沉浸式滚动模式 */
.immersive-mode {
  width: 100%;
  height: 100vh;
  overflow-y: scroll;
  scroll-snap-type: y mandatory;
  
  .quote-slide {
    width: 100%;
    height: 100vh;
    position: relative;
    scroll-snap-align: start;
    display: flex;
    align-items: center;
    justify-content: center;
    
    .quote-overlay {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: rgba(0, 0, 0, 0.4);
      backdrop-filter: blur(2px);
    }
    
    .quote-content {
      position: relative;
      z-index: 1;
      text-align: center;
      color: #fff;
      padding: 0 20px;
      max-width: 800px;
      
      .celebrity-name {
        font-size: 48px;
        font-weight: bold;
        margin-bottom: 30px;
        text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
      }
      
      .quote-text {
        font-size: 32px;
        line-height: 1.6;
        margin-bottom: 20px;
        font-style: italic;
        text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
      }
      
      .quote-bio {
        font-size: 18px;
        line-height: 1.8;
        opacity: 0.9;
        margin-top: 40px;
      }
    }
  }
  
  .scroll-indicator {
    position: fixed;
    bottom: 30px;
    left: 50%;
    transform: translateX(-50%);
    color: #fff;
    text-align: center;
    z-index: 10;
    animation: bounce 2s infinite;
    
    i {
      font-size: 24px;
      display: block;
      margin-bottom: 5px;
    }
    
    span {
      font-size: 14px;
      text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
    }
  }
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateX(-50%) translateY(0);
  }
  40% {
    transform: translateX(-50%) translateY(-10px);
  }
  60% {
    transform: translateX(-50%) translateY(-5px);
  }
}

/* 侧边联动模式 */
.sidebar-mode {
  display: flex;
  height: 100vh;
  
  .photo-sidebar {
    width: 30%;
    height: 100vh;
    position: sticky;
    top: 0;
    overflow: hidden;
    
    .photo-item {
      position: absolute;
      width: 100%;
      height: 100%;
      transition: all 0.3s ease;
      
      &.active {
        z-index: 1;
      }
      
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
      
      .photo-placeholder {
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: #fff;
        font-size: 48px;
        font-weight: bold;
      }
    }
  }
  
  .content-scroll {
    width: 70%;
    height: 100vh;
    overflow-y: auto;
    padding: 40px 60px;
    background: #f5f5f5;
    
    .content-item {
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      justify-content: center;
      margin-bottom: 40px;
      
      .celebrity-name {
        font-size: 36px;
        color: #333;
        margin-bottom: 20px;
      }
      
      .quote-text {
        font-size: 24px;
        color: #666;
        line-height: 1.8;
        font-style: italic;
        margin-bottom: 30px;
        padding: 20px;
        background: #fff;
        border-left: 4px solid #667eea;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      }
      
      .quote-details {
        background: #fff;
        padding: 30px;
        border-radius: 8px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        
        p {
          margin-bottom: 15px;
          line-height: 1.8;
          color: #555;
          
          strong {
            color: #333;
            margin-right: 8px;
          }
        }
      }
    }
  }
}

/* 登录入口 */
.login-entrance {
  position: fixed;
  top: 30px;
  right: 30px;
  z-index: 100;
}

/* 平台简介 */
.platform-intro {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
  
  .intro-content {
    background: #fff;
    padding: 40px;
    border-radius: 12px;
    max-width: 600px;
    text-align: center;
    
    h2 {
      font-size: 28px;
      color: #333;
      margin-bottom: 15px;
    }
    
    p {
      font-size: 18px;
      color: #666;
      margin-bottom: 25px;
    }
    
    ul {
      text-align: left;
      margin: 0 auto 30px;
      display: inline-block;
      
      li {
        font-size: 16px;
        color: #555;
        margin-bottom: 10px;
        padding-left: 20px;
        position: relative;
        
        &:before {
          content: "✓";
          position: absolute;
          left: 0;
          color: #667eea;
          font-weight: bold;
        }
      }
    }
  }
}

/* 简介按钮 */
.intro-button {
  position: fixed;
  bottom: 30px;
  right: 30px;
  z-index: 100;
}
</style>
