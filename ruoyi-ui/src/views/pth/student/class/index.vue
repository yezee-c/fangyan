<template>
  <div class="app-container">
    <!-- 当前班级信息 -->
    <el-card v-if="currentClass" class="class-info-card">
      <div slot="header" class="clearfix">
        <span><i class="el-icon-school"></i> 我的班级</span>
      </div>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="班级名称">
          <el-tag type="success">{{ currentClass.className }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="年级">{{ currentClass.grade }}</el-descriptions-item>
        <el-descriptions-item label="学期">{{ currentClass.semester }}</el-descriptions-item>
        <el-descriptions-item label="教师">{{ currentClass.teacherName }}</el-descriptions-item>
        <el-descriptions-item label="学生人数">{{ currentClass.studentCount }}人</el-descriptions-item>
        <el-descriptions-item label="加入时间">{{ currentClass.joinTime }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 未加入班级提示 -->
    <el-card v-else class="join-class-card">
      <div slot="header" class="clearfix">
        <span><i class="el-icon-plus"></i> 加入班级</span>
      </div>
      <el-empty description="您还未加入任何班级">
        <el-button type="primary" @click="dialogVisible = true">立即加入班级</el-button>
      </el-empty>
    </el-card>

    <!-- 加入班级对话框 -->
    <el-dialog title="加入班级" :visible.sync="dialogVisible" width="500px" append-to-body>
      <el-form ref="joinForm" :model="joinForm" :rules="joinRules" label-width="100px">
        <el-form-item label="邀请码" prop="invitationCode">
          <el-input
            v-model="joinForm.invitationCode"
            placeholder="请输入6位班级邀请码"
            maxlength="6"
            show-word-limit
            @input="handleCodeInput"
          >
            <el-button slot="append" @click="handleVerifyCode">验证</el-button>
          </el-input>
          <div class="tip-text">
            <i class="el-icon-info"></i>
            请向您的教师索取班级邀请码
          </div>
        </el-form-item>

        <!-- 班级信息预览 -->
        <el-form-item v-if="previewClass" label="班级信息">
          <el-descriptions :column="1" border size="small">
            <el-descriptions-item label="班级名称">{{ previewClass.className }}</el-descriptions-item>
            <el-descriptions-item label="年级">{{ previewClass.grade }}</el-descriptions-item>
            <el-descriptions-item label="学期">{{ previewClass.semester }}</el-descriptions-item>
            <el-descriptions-item label="教师">{{ previewClass.teacherName }}</el-descriptions-item>
            <el-descriptions-item label="学生人数">{{ previewClass.studentCount }}人</el-descriptions-item>
          </el-descriptions>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleJoinClass" :disabled="!previewClass">确认加入</el-button>
      </div>
    </el-dialog>

    <!-- 学习数据统计 -->
    <el-row :gutter="20" style="margin-top: 20px" v-if="currentClass">
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-item">
            <i class="el-icon-document stat-icon"></i>
            <div class="stat-content">
              <div class="stat-value">{{ stats.assessmentCount }}</div>
              <div class="stat-label">测评次数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-item">
            <i class="el-icon-trophy stat-icon"></i>
            <div class="stat-content">
              <div class="stat-value">{{ stats.taskCount }}</div>
              <div class="stat-label">任务完成</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-item">
            <i class="el-icon-star-on stat-icon"></i>
            <div class="stat-content">
              <div class="stat-value">{{ stats.score }}</div>
              <div class="stat-label">平均分</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getClassByCode, joinClass } from "@/api/pth/class";

export default {
  name: "StudentClass",
  data() {
    return {
      // 当前班级信息
      currentClass: null,
      // 对话框显示
      dialogVisible: false,
      // 加入班级表单
      joinForm: {
        invitationCode: ""
      },
      // 验证规则
      joinRules: {
        invitationCode: [
          { required: true, message: "请输入邀请码", trigger: "blur" },
          { len: 6, message: "邀请码长度为6位", trigger: "blur" }
        ]
      },
      // 预览班级信息
      previewClass: null,
      // 学习数据统计
      stats: {
        assessmentCount: 0,
        taskCount: 0,
        score: 0
      }
    };
  },
  created() {
    this.loadCurrentClass();
  },
  methods: {
    // 加载当前班级信息
    loadCurrentClass() {
      // TODO: 调用API获取学生当前加入的班级
      // 这里需要后端提供学生当前班级的查询接口
      this.currentClass = null;
    },
    // 输入邀请码时转换为大写
    handleCodeInput(value) {
      this.joinForm.invitationCode = value.toUpperCase();
      this.previewClass = null;
    },
    // 验证邀请码
    handleVerifyCode() {
      if (!this.joinForm.invitationCode) {
        this.$message.warning("请输入邀请码");
        return;
      }
      
      if (this.joinForm.invitationCode.length !== 6) {
        this.$message.warning("邀请码长度为6位");
        return;
      }

      this.$message({
        message: "正在验证邀请码...",
        type: "info",
        duration: 1000
      });

      getClassByCode(this.joinForm.invitationCode).then(response => {
        if (response.data) {
          this.previewClass = response.data;
          this.$message.success("验证成功！请确认班级信息");
        } else {
          this.$message.error("邀请码无效或已失效");
          this.previewClass = null;
        }
      }).catch(() => {
        this.$message.error("邀请码无效或已失效");
        this.previewClass = null;
      });
    },
    // 加入班级
    handleJoinClass() {
      this.$refs["joinForm"].validate(valid => {
        if (valid && this.previewClass) {
          this.$confirm('确认加入班级"' + this.previewClass.className + '"？加入后不可随意更换班级。', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            joinClass(this.joinForm.invitationCode).then(response => {
              this.$message.success("加入班级成功！");
              this.dialogVisible = false;
              this.loadCurrentClass();
              this.resetJoinForm();
            });
          }).catch(() => {});
        }
      });
    },
    // 重置加入表单
    resetJoinForm() {
      this.joinForm.invitationCode = "";
      this.previewClass = null;
      if (this.$refs.joinForm) {
        this.$refs.joinForm.clearValidate();
      }
    }
  }
};
</script>

<style scoped lang="scss">
.class-info-card,
.join-class-card {
  margin-bottom: 20px;
}

.tip-text {
  margin-top: 10px;
  color: #909399;
  font-size: 13px;
  
  i {
    margin-right: 5px;
  }
}

.stat-card {
  .stat-item {
    display: flex;
    align-items: center;
    padding: 10px 0;
    
    .stat-icon {
      font-size: 48px;
      color: #409EFF;
      margin-right: 20px;
    }
    
    .stat-content {
      flex: 1;
      
      .stat-value {
        font-size: 28px;
        font-weight: bold;
        color: #303133;
        margin-bottom: 5px;
      }
      
      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}
</style>
