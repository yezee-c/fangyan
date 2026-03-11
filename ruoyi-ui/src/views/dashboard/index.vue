<template>
  <div class="dashboard">
    <h1>后台控制台</h1>
    <p>这里是管理后台的控制台页面</p>
    
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card>
          <div style="text-align: center;">
            <i class="el-icon-user" style="font-size: 48px; color: #409EFF;"></i>
            <h3>用户管理</h3>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div style="text-align: center;">
            <i class="el-icon-setting" style="font-size: 48px; color: #67C23A;"></i>
            <h3>系统管理</h3>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div style="text-align: center;">
            <i class="el-icon-data-line" style="font-size: 48px; color: #E6A23C;"></i>
            <h3>数据监控</h3>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div style="text-align: center;">
            <i class="el-icon-document" style="font-size: 48px; color: #F56C6C;"></i>
            <h3>日志管理</h3>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Dashboard',
  data() {
    return {}
  }
}
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.dashboard h1 {
  margin-bottom: 20px;
}
</style>
