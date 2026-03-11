<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户名称" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户昵称" prop="nickName">
        <el-input
          v-model="queryParams.nickName"
          placeholder="请输入用户昵称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:fileconfig:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:fileconfig:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:fileconfig:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="fileConfigList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="用户名称" align="center" prop="userName" />
      <el-table-column label="用户昵称" align="center" prop="nickName" />
      <el-table-column label="单文件限制" align="center" prop="maxFileSize">
        <template slot-scope="scope">
          {{ formatFileSize(scope.row.maxFileSize) }}
        </template>
      </el-table-column>
      <el-table-column label="总容量限制" align="center" prop="maxStorage">
        <template slot-scope="scope">
          {{ formatFileSize(scope.row.maxStorage) }}
        </template>
      </el-table-column>
      <el-table-column label="已使用容量" align="center" prop="usedStorage">
        <template slot-scope="scope">
          {{ formatFileSize(scope.row.usedStorage) }}
        </template>
      </el-table-column>
      <el-table-column label="使用率" align="center" width="120">
        <template slot-scope="scope">
          <el-progress 
            :percentage="calculateUsagePercent(scope.row.usedStorage, scope.row.maxStorage)" 
            :color="getProgressColor(scope.row.usedStorage, scope.row.maxStorage)">
          </el-progress>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:fileconfig:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:fileconfig:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改用户文件配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="用户ID" prop="userId" v-if="!form.userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="单文件大小限制" prop="maxFileSize">
          <el-input-number v-model="fileSizeMB" :min="1" :max="1024" label="单文件大小限制(MB)"></el-input-number>
          <span style="margin-left: 10px;">MB</span>
        </el-form-item>
        <el-form-item label="总存储容量限制" prop="maxStorage">
          <el-input-number v-model="storageGB" :min="1" :max="1024" label="总存储容量限制(GB)"></el-input-number>
          <span style="margin-left: 10px;">GB</span>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFileConfig, getFileConfig, delFileConfig, addFileConfig, updateFileConfig } from "@/api/system/fileconfig";

export default {
  name: "FileConfig",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户文件配置表格数据
      fileConfigList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: null,
        nickName: null
      },
      // 表单参数
      form: {},
      // MB单位的文件大小
      fileSizeMB: 10,
      // GB单位的存储容量
      storageGB: 1,
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        maxFileSize: [
          { required: true, message: "单个文件最大大小不能为空", trigger: "blur" }
        ],
        maxStorage: [
          { required: true, message: "最大存储容量不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户文件配置列表 */
    getList() {
      this.loading = true;
      listFileConfig(this.queryParams).then(response => {
        this.fileConfigList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        userId: null,
        maxFileSize: null,
        maxStorage: null,
        usedStorage: null
      };
      this.fileSizeMB = 10;
      this.storageGB = 1;
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加用户文件配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const userId = row.userId || this.ids
      getFileConfig(userId).then(response => {
        this.form = response.data;
        this.fileSizeMB = Math.round(this.form.maxFileSize / 1024 / 1024);
        this.storageGB = Math.round(this.form.maxStorage / 1024 / 1024 / 1024);
        this.open = true;
        this.title = "修改用户文件配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      // 转换单位
      this.form.maxFileSize = this.fileSizeMB * 1024 * 1024;
      this.form.maxStorage = this.storageGB * 1024 * 1024 * 1024;
      
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userId != null && this.title === "修改用户文件配置") {
            updateFileConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFileConfig(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const userIds = row.userId || this.ids;
      this.$modal.confirm('是否确认删除选中的用户文件配置？').then(function() {
        return delFileConfig(userIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 格式化文件大小 */
    formatFileSize(bytes) {
      if (bytes === 0) return '0 B';
      const k = 1024;
      const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
      const i = Math.floor(Math.log(bytes) / Math.log(k));
      return (bytes / Math.pow(k, i)).toFixed(2) + ' ' + sizes[i];
    },
    /** 计算使用率 */
    calculateUsagePercent(used, total) {
      if (total === 0) return 0;
      return Math.round((used / total) * 100);
    },
    /** 获取进度条颜色 */
    getProgressColor(used, total) {
      const percent = (used / total) * 100;
      if (percent < 60) {
        return '#67c23a';
      } else if (percent < 80) {
        return '#e6a23c';
      } else {
        return '#f56c6c';
      }
    }
  }
};
</script>
