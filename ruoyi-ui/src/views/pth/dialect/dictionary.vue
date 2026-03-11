<template>
  <div class="app-container dialect-dictionary">
    <el-card class="feature-card">
      <div slot="header" class="clearfix">
        <span><i class="el-icon-notebook-2"></i> 方言词典</span>
        <el-tag type="info" size="small" style="float: right;">收录{{ dialectList.length }}个方言词汇</el-tag>
      </div>

      <!-- 搜索和筛选 -->
      <div class="search-section">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-input 
              v-model="searchKeyword" 
              placeholder="请输入方言词汇或普通话释义" 
              prefix-icon="el-icon-search"
              @keyup.enter.native="searchDialects"
            ></el-input>
          </el-col>
          <el-col :span="12">
            <el-select 
              v-model="selectedRegion" 
              placeholder="请选择方言地区" 
              style="width: 100%;"
              @change="filterByRegion"
            >
              <el-option label="全部地区" value=""></el-option>
              <el-option label="湖北黄冈" value="湖北黄冈"></el-option>
              <el-option label="河南信阳" value="河南信阳"></el-option>
              <el-option label="安徽六安" value="安徽六安"></el-option>
              <el-option label="安徽岳西" value="安徽岳西"></el-option>
              <el-option label="大别山地区" value="大别山地区"></el-option>
            </el-select>
          </el-col>
        </el-row>
      </div>

      <!-- 方言词汇列表 -->
      <div class="dialect-list">
        <el-table 
          :data="filteredDialects" 
          stripe 
          style="width: 100%"
          :default-sort="{prop: 'region', order: 'ascending'}"
        >
          <el-table-column prop="dialect" label="方言词汇" width="150"></el-table-column>
          <el-table-column prop="meaning" label="普通话释义" width="200"></el-table-column>
          <el-table-column prop="region" label="方言地区" width="120">
            <template slot-scope="scope">
              <el-tag 
                :type="getRegionTagType(scope.row.region)" 
                size="small"
              >
                {{ scope.row.region }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="category" label="分类" width="120"></el-table-column>
        </el-table>
      </div>

      <!-- 方言分类统计 -->
      <div class="stats-section">
        <el-row :gutter="20">
          <el-col :span="6" v-for="(stat, index) in regionStats" :key="index">
            <el-card class="stat-card">
              <div class="stat-item">
                <h4>{{ stat.region }}</h4>
                <p>{{ stat.count }} 个词汇</p>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 方言文化介绍 -->
      <el-card class="culture-card">
        <div slot="header">
          <span><i class="el-icon-trophy"></i> 方言文化价值</span>
        </div>
        <p>方言是地域文化的重要载体，承载着丰富的历史信息和文化内涵。通过了解不同地区的方言，我们可以更好地理解各地的风土人情、文化传统和历史变迁。</p>
        <p>本词典收录了大别山地区及周边地区的方言词汇，旨在保护和传承方言文化，同时帮助普通话学习者了解方言与普通话的对应关系。</p>
      </el-card>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'DialectDictionary',
  data() {
    return {
      searchKeyword: '',
      selectedRegion: '',
      dialectList: [
        // 湖北黄冈方言
        { dialect: '七饭', meaning: '吃饭', region: '湖北黄冈', category: '日常用语' },
        { dialect: '克马', meaning: '青蛙', region: '湖北黄冈', category: '动物' },
        { dialect: '苕', meaning: '蠢人、傻子，也可用于打趣', region: '湖北黄冈', category: '人物描述' },
        { dialect: '茅寺', meaning: '厕所', region: '湖北黄冈', category: '场所' },
        { dialect: '烧火', meaning: '做饭', region: '湖北黄冈', category: '日常用语' },
        { dialect: '垮子', meaning: '腿', region: '湖北黄冈', category: '身体部位' },
        { dialect: '色头', meaning: '膝盖', region: '湖北黄冈', category: '身体部位' },
        { dialect: '调杆', meaning: '汤勺', region: '湖北黄冈', category: '器具' },
        { dialect: '陶屋', meaning: '客厅', region: '湖北黄冈', category: '场所' },
        { dialect: '撒子', meaning: '拖鞋', region: '湖北黄冈', category: '服饰用品' },
        { dialect: '冒招到', meaning: '不小心，也用于表达歉意', region: '湖北黄冈', category: '日常用语' },
        { dialect: '黑到一弹', meaning: '吓一跳', region: '湖北黄冈', category: '情绪表达' },
        { dialect: '耿里搞斜了', meaning: '太嚣张', region: '湖北黄冈', category: '行为描述' },
        { dialect: '黑湖', meaning: '很多', region: '湖北黄冈', category: '数量描述' },
        { dialect: '日白', meaning: '说谎话', region: '湖北黄冈', category: '语言行为' },
        { dialect: '结根', meaning: '啰嗦、唠叨', region: '湖北黄冈', category: '语言行为' },
        { dialect: '僵颈', meaning: '人很固执', region: '湖北黄冈', category: '性格描述' },
        { dialect: '枣椰', meaning: '可怜', region: '湖北黄冈', category: '情绪表达' },
        { dialect: '刮气', meaning: '长得漂亮', region: '湖北黄冈', category: '外貌描述' },
        { dialect: '次人', meaning: '脏', region: '湖北黄冈', category: '状态描述' },
        { dialect: '细眉细肝', meaning: '心胸不开阔或人吝啬', region: '湖北黄冈', category: '性格描述' },
        { dialect: '毛焦火辣', meaning: '人烦躁不安', region: '湖北黄冈', category: '情绪表达' },
        { dialect: '猴头凹脸', meaning: '长相难看', region: '湖北黄冈', category: '外貌描述' },
        { dialect: '勺七哈涨', meaning: '暴饮暴食', region: '湖北黄冈', category: '行为描述' },
        { dialect: '二里二四滴', meaning: '很突然、没防备', region: '湖北黄冈', category: '状态描述' },
        { dialect: '告壳子', meaning: '讨饭的', region: '湖北黄冈', category: '职业/身份' },
        { dialect: '灵新', meaning: '漂亮或聪明', region: '湖北黄冈', category: '评价' },
        { dialect: '堂客', meaning: '妻子', region: '湖北黄冈', category: '人物关系' },
        { dialect: '细伢', meaning: '小孩', region: '湖北黄冈', category: '人物关系' },
        { dialect: '嘎爹', meaning: '外公', region: '湖北黄冈', category: '人物关系' },
        { dialect: '家婆', meaning: '外婆', region: '湖北黄冈', category: '人物关系' },
        { dialect: '脚色头', meaning: '膝盖', region: '湖北黄冈', category: '身体部位' },
        { dialect: '歇暗脚', meaning: '腋下', region: '湖北黄冈', category: '身体部位' },
        { dialect: '海子', meaning: '螃蟹', region: '湖北黄冈', category: '动物' },
        { dialect: '灶屋', meaning: '厨房', region: '湖北黄冈', category: '场所' },
        { dialect: '包面', meaning: '饺子', region: '湖北黄冈', category: '食物' },
        { dialect: '果儿', meaning: '油条', region: '湖北黄冈', category: '食物' },
        { dialect: '赖色', meaning: '垃圾', region: '湖北黄冈', category: '物品' },
        { dialect: '荡子', meaning: '水坑', region: '湖北黄冈', category: '地理' },
        { dialect: '革内儿', meaning: '角落', region: '湖北黄冈', category: '地点' },
        { dialect: '上昼', meaning: '上午', region: '湖北黄冈', category: '时间' },
        { dialect: '下昼', meaning: '下午', region: '湖北黄冈', category: '时间' },
        { dialect: '夜歇', meaning: '晚上', region: '湖北黄冈', category: '时间' },
        { dialect: '杠活儿', meaning: '打架', region: '湖北黄冈', category: '行为' },
        { dialect: '舞饭', meaning: '做饭', region: '湖北黄冈', category: '日常用语' },
        { dialect: '抽茅针', meaning: '摘白茅嫩茎', region: '湖北黄冈', category: '行为' },
        { dialect: '飞冲', meaning: '跑得快', region: '湖北黄冈', category: '动作' },
        { dialect: '掇', meaning: '搬', region: '湖北黄冈', category: '动作' },
        { dialect: '拈菜', meaning: '夹菜', region: '湖北黄冈', category: '动作' },
        { dialect: '阴斯儿', meaning: '内向的人', region: '湖北黄冈', category: '性格描述' },
        { dialect: '壮咚了', meaning: '胖', region: '湖北黄冈', category: '状态描述' },
        { dialect: '钢壳子', meaning: '瘦', region: '湖北黄冈', category: '状态描述' },
        { dialect: '皱皮蜡干', meaning: '干瘪', region: '湖北黄冈', category: '状态描述' },
        { dialect: '细眉细眼', meaning: '小气', region: '湖北黄冈', category: '性格描述' },
        { dialect: '妥子', meaning: '傻瓜', region: '湖北黄冈', category: '人物描述' },
        { dialect: '侗', meaning: '笨', region: '湖北黄冈', category: '人物描述' },
        { dialect: '灵醒', meaning: '聪明', region: '湖北黄冈', category: '人物描述' },
        { dialect: '润贴', meaning: '舒服', region: '湖北黄冈', category: '感受' },
        { dialect: '耐死', meaning: '脏', region: '湖北黄冈', category: '状态描述' },
        { dialect: '舍几', meaning: '大方', region: '湖北黄冈', category: '性格描述' },
        { dialect: '搞邪了', meaning: '过分', region: '湖北黄冈', category: '行为描述' },
        { dialect: '门这', meaning: '明天', region: '湖北黄冈', category: '时间' },
        { dialect: '莫走', meaning: '别走', region: '湖北黄冈', category: '日常用语' },
        { dialect: '么早', meaning: '何时', region: '湖北黄冈', category: '时间' },
        { dialect: '默得', meaning: '以为', region: '湖北黄冈', category: '认知' },
        { dialect: '立心', meaning: '故意', region: '湖北黄冈', category: '行为意图' },
        { dialect: '耍啦', meaning: '爽快', region: '湖北黄冈', category: '性格描述' },
        { dialect: '撇脱', meaning: '简单', region: '湖北黄冈', category: '状态描述' },
        { dialect: '将八就', meaning: '凑合', region: '湖北黄冈', category: '行为' },
        { dialect: '落了魂', meaning: '失魂落魄', region: '湖北黄冈', category: '状态描述' },
        { dialect: '跑得起撞', meaning: '跑得飞快', region: '湖北黄冈', category: '动作' },
        { dialect: '驼起', meaning: '拿走', region: '湖北黄冈', category: '动作' },
        { dialect: '牵', meaning: '想念', region: '湖北黄冈', category: '情感' },
        { dialect: '灶马隔落', meaning: '灶台边', region: '湖北黄冈', category: '地点' },
        { dialect: '炕坛', meaning: '取暖陶罐', region: '湖北黄冈', category: '器具' },
        { dialect: '烘娄儿', meaning: '提篮式火盆', region: '湖北黄冈', category: '器具' },
        
        // 河南信阳方言
        { dialect: '嗯', meaning: '你', region: '河南信阳', category: '代词' },
        { dialect: '雪', meaning: '说', region: '河南信阳', category: '语言行为' },
        { dialect: '日头', meaning: '太阳', region: '河南信阳', category: '自然' },
        { dialect: '恁啥子', meaning: '干什么', region: '河南信阳', category: '日常用语' },
        { dialect: '照', meaning: '可以、行', region: '河南信阳', category: '语气词' },
        { dialect: '不照', meaning: '不行、能力不够', region: '河南信阳', category: '语气词' },
        { dialect: '巴巴的', meaning: '故意', region: '河南信阳', category: '行为意图' },
        { dialect: '打马胡眼', meaning: '敷衍了事', region: '河南信阳', category: '行为' },
        { dialect: '泥巴狗子', meaning: '泥鳅', region: '河南信阳', category: '动物' },
        { dialect: '接下巴科子', meaning: '插嘴', region: '河南信阳', category: '语言行为' },
        { dialect: '狼巴子', meaning: '用来吓唬小孩的虚构怪物，类似大灰狼', region: '河南信阳', category: '传说/虚构' },
        { dialect: '格应', meaning: '讨厌', region: '河南信阳', category: '情绪表达' },
        { dialect: '马费儿', meaning: '一会儿', region: '河南信阳', category: '时间' },
        { dialect: '大', meaning: '父亲', region: '河南信阳', category: '人物关系' },
        { dialect: '恩妈', meaning: '母亲', region: '河南信阳', category: '人物关系' },
        { dialect: '俏巴', meaning: '可爱、漂亮', region: '河南信阳', category: '评价' },
        { dialect: '利凉', meaning: '衣物干净或穿着得体', region: '河南信阳', category: '状态描述' },
        { dialect: '叶熊了', meaning: '完了', region: '河南信阳', category: '状态描述' },
        { dialect: '喝茶', meaning: '喝水', region: '河南信阳', category: '日常用语' },
        { dialect: '可朗斯', meaning: '很好、很不错', region: '河南信阳', category: '评价' },
        { dialect: '没戴', meaning: '没有，多见于信阳东部', region: '河南信阳', category: '否定' },
        { dialect: '能蛋', meaning: '聪明，也可指爱炫耀聪明', region: '河南信阳', category: '人物描述' },
        { dialect: '现世', meaning: '好表现、炫耀自己', region: '河南信阳', category: '行为' },
        { dialect: '打金兜子', meaning: '男性上半身没穿衣服', region: '河南信阳', category: '状态描述' },
        { dialect: '寒蛋', meaning: '人吝啬，"寒蛋了"可表程度深', region: '河南信阳', category: '性格描述' },
        
        // 安徽六安方言
        { dialect: '大大', meaning: '母亲', region: '安徽六安', category: '人物关系' },
        { dialect: '爹爹', meaning: '祖父', region: '安徽六安', category: '人物关系' },
        { dialect: '穰草', meaning: '稻草', region: '安徽六安', category: '植物' },
        { dialect: '玉芦', meaning: '玉米', region: '安徽六安', category: '植物' },
        { dialect: '蚩虎子', meaning: '壁虎', region: '安徽六安', category: '动物' },
        { dialect: '蛤蟆骨朵', meaning: '蝌蚪', region: '安徽六安', category: '动物' },
        { dialect: '鸡果子', meaning: '鸡蛋', region: '安徽六安', category: '食物' },
        { dialect: '饳饦子', meaning: '类似年糕的粘性食品', region: '安徽六安', category: '食物' },
        { dialect: '油馃子', meaning: '油条', region: '安徽六安', category: '食物' },
        { dialect: '多喒子', meaning: '什么时候', region: '安徽六安', category: '时间' },
        { dialect: '半边人', meaning: '寡妇', region: '安徽六安', category: '人物关系' },
        { dialect: '溜门子', meaning: '小偷', region: '安徽六安', category: '职业/身份' },
        { dialect: '撑杆子', meaning: '伞', region: '安徽六安', category: '器具' },
        { dialect: '太平菜', meaning: '辣椒酱', region: '安徽六安', category: '食物' },
        { dialect: '掰哧', meaning: '拨弄、修理', region: '安徽六安', category: '动作' },
        { dialect: '打当', meaning: '收拾、安排', region: '安徽六安', category: '动作' },
        { dialect: '宕', meaning: '拖延', region: '安徽六安', category: '行为' },
        { dialect: '聒', meaning: '责备', region: '安徽六安', category: '语言行为' },
        { dialect: '抻练', meaning: '坑害', region: '安徽六安', category: '行为' },
        { dialect: '愁怵', meaning: '犯愁、害怕', region: '安徽六安', category: '情绪表达' },
        { dialect: '软腿子', meaning: '未成年的子女', region: '安徽六安', category: '人物关系' },
        { dialect: '檐老鼠', meaning: '蝙蝠', region: '安徽六安', category: '动物' },
        { dialect: '歪歪', meaning: '河蚌', region: '安徽六安', category: '动物' },
        { dialect: '清泚亮干', meaning: '清洁整齐', region: '安徽六安', category: '状态描述' },
        { dialect: '软磨屁蹭', meaning: '行动缓慢', region: '安徽六安', category: '行为' },
        
        // 安徽岳西方言
        { dialect: '葛捏', meaning: '小孩打架、争吵', region: '安徽岳西', category: '行为' },
        { dialect: '噶设子', meaning: '人说话不清楚', region: '安徽岳西', category: '语言行为' },
        { dialect: '撖', meaning: '摇动杂物使其集中以便清除', region: '安徽岳西', category: '动作' },
        { dialect: '槽人', meaning: '身体不舒服，也指环境嘈杂让人烦躁', region: '安徽岳西', category: '状态描述' },
        { dialect: '迟鱼', meaning: '剖鱼腹，同理有"迟鸡""迟鸭"', region: '安徽岳西', category: '动作' },
        { dialect: '笸篮', meaning: '竹篾编制的晾晒谷物的竹器', region: '安徽岳西', category: '器具' },
        { dialect: '打槁', meaning: '捣乱、打扰别人做事', region: '安徽岳西', category: '行为' },
        
        // 大别山地区通用方言
        { dialect: '特为', meaning: '特意、故意', region: '大别山地区', category: '行为意图' },
        { dialect: '夜兮', meaning: '晚上', region: '大别山地区', category: '时间' },
        { dialect: '几多', meaning: '多少，用于提问数量', region: '大别山地区', category: '疑问' },
        { dialect: '么早', meaning: '什么时候', region: '大别山地区', category: '时间' },
        { dialect: '陡然', meaning: '突然', region: '大别山地区', category: '状态描述' },
        { dialect: '唯愿', meaning: '但愿', region: '大别山地区', category: '愿望' },
        { dialect: '造孽', meaning: '可怜', region: '大别山地区', category: '情绪表达' },
        { dialect: '郎官', meaning: '对男子的尊称', region: '大别山地区', category: '人物关系' },
        { dialect: '背时', meaning: '倒霉', region: '大别山地区', category: '状态描述' },
        { dialect: '该然', meaning: '命里注定', region: '大别山地区', category: '命运' },
        { dialect: '生古', meaning: '人性格乖张、说话怪异', region: '大别山地区', category: '性格描述' },
        { dialect: '点卯', meaning: '只露个面却不干正经事', region: '大别山地区', category: '行为' },
        { dialect: '泡把', meaning: '十几个', region: '大别山地区', category: '数量描述' },
        { dialect: '家们', meaning: '同姓的人', region: '大别山地区', category: '人物关系' },
        { dialect: '润贴', meaning: '有滋有味、很舒服', region: '大别山地区', category: '感受' },
        { dialect: '填情', meaning: '还礼、还人情', region: '大别山地区', category: '社交行为' }
      ],
      filteredDialects: []
    }
  },
  created() {
    this.filteredDialects = this.dialectList;
    this.calculateRegionStats();
  },
  methods: {
    searchDialects() {
      this.filterDialects();
    },
    filterByRegion() {
      this.filterDialects();
    },
    filterDialects() {
      this.filteredDialects = this.dialectList.filter(item => {
        const matchesKeyword = !this.searchKeyword || 
          item.dialect.toLowerCase().includes(this.searchKeyword.toLowerCase()) || 
          item.meaning.toLowerCase().includes(this.searchKeyword.toLowerCase());
        const matchesRegion = !this.selectedRegion || item.region === this.selectedRegion;
        return matchesKeyword && matchesRegion;
      });
    },
    getRegionTagType(region) {
      switch(region) {
        case '湖北黄冈':
          return 'primary';
        case '河南信阳':
          return 'success';
        case '安徽六安':
          return 'warning';
        case '安徽岳西':
          return 'danger';
        case '大别山地区':
          return 'info';
        default:
          return 'info';
      }
    },
    calculateRegionStats() {
      const stats = {};
      this.dialectList.forEach(item => {
        if (stats[item.region]) {
          stats[item.region]++;
        } else {
          stats[item.region] = 1;
        }
      });
      
      this.regionStats = Object.keys(stats).map(region => ({
        region,
        count: stats[region]
      }));
    }
  }
}
</script>

<style scoped>
.dialect-dictionary {
  padding: 20px;
}

.search-section {
  margin-bottom: 20px;
}

.dialect-list {
  margin-bottom: 30px;
}

.stats-section {
  margin-bottom: 30px;
}

.stat-card {
  text-align: center;
}

.stat-item h4 {
  margin: 0;
  color: #409EFF;
}

.stat-item p {
  margin: 10px 0 0 0;
  color: #909399;
}

.culture-card {
  margin-top: 20px;
}

.culture-card p {
  line-height: 1.8;
  margin-bottom: 10px;
}
</style>
</template>