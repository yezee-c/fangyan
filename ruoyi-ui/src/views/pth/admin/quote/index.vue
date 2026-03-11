<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名人姓名" prop="celebrityName">
        <el-input
          v-model="queryParams.celebrityName"
          placeholder="请输入名人姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
        </el-select>
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
          v-hasPermi="['pth:quote:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['pth:quote:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="quoteList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="名人姓名" align="center" prop="celebrityName" width="120" />
      <el-table-column label="名言内容" align="center" prop="quoteContent" :show-overflow-tooltip="true" />
      <el-table-column label="照片" align="center" prop="photoUrl" width="100">
        <template slot-scope="scope">
          <el-image
            v-if="scope.row.photoUrl"
            style="width: 60px; height: 60px"
            :src="scope.row.photoUrl"
            :preview-src-list="[scope.row.photoUrl]"
            fit="cover"
          />
          <span v-else class="no-photo">暂无照片</span>
        </template>
      </el-table-column>
      <el-table-column label="显示顺序" align="center" prop="displayOrder" width="100" />
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
            {{ scope.row.status === '0' ? '正常' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['pth:quote:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['pth:quote:remove']"
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

    <!-- 添加或修改名人名言对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="名人姓名" prop="celebrityName">
              <el-input v-model="form.celebrityName" placeholder="请输入名人姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="displayOrder">
              <el-input-number v-model="form.displayOrder" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="名言内容" prop="quoteContent">
          <el-input v-model="form.quoteContent" type="textarea" :rows="3" placeholder="请输入名言内容" />
        </el-form-item>
        <el-form-item label="名人简介" prop="biography">
          <el-input v-model="form.biography" type="textarea" :rows="3" placeholder="请输入名人简介，包括生平、成就等" />
        </el-form-item>
        <el-form-item label="主要成就" prop="achievements">
          <el-input v-model="form.achievements" type="textarea" :rows="2" placeholder="请输入主要成就" />
        </el-form-item>
        <el-form-item label="与大别山的关联" prop="connection">
          <el-input v-model="form.connection" type="textarea" :rows="2" placeholder="请输入与大别山的关联" />
        </el-form-item>
        <el-form-item label="名人照片">
          <image-upload v-model="form.photoUrl" :limit="1" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listQuote, getQuote, delQuote, addQuote, updateQuote } from "@/api/pth/quote";

export default {
  name: "Quote",
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
      // 名人名言表格数据
      quoteList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        celebrityName: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        celebrityName: [
          { required: true, message: "名人姓名不能为空", trigger: "blur" }
        ],
        quoteContent: [
          { required: true, message: "名言内容不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询名人名言列表 */
    getList() {
      this.loading = true;
      listQuote(this.queryParams).then(response => {
        this.quoteList = response.rows;
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
        quoteId: null,
        celebrityName: null,
        quoteContent: null,
        photoUrl: null,
        biography: null,
        achievements: null,
        connection: null,
        displayOrder: 0,
        status: "0",
        remark: null
      };
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
      this.ids = selection.map(item => item.quoteId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加名人名言";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const quoteId = row.quoteId || this.ids
      getQuote(quoteId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改名人名言";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.quoteId != null) {
            updateQuote(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addQuote(this.form).then(response => {
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
      const quoteIds = row.quoteId || this.ids;
      this.$modal.confirm('是否确认删除名人名言编号为"' + quoteIds + '"的数据项？').then(function() {
        return delQuote(quoteIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
  }
};
</script>

<style scoped>
.no-photo {
  color: #999;
  font-size: 12px;
}
</style>
