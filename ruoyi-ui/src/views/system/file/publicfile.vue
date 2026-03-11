<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="文件名称" prop="originalName">
        <el-input
          v-model="queryParams.originalName"
          placeholder="请输入文件名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件类型" prop="fileType">
        <el-select v-model="queryParams.fileType" placeholder="请选择文件类型" clearable>
          <el-option label="图片" value="image" />
          <el-option label="视频" value="video" />
          <el-option label="音频" value="audio" />
          <el-option label="PDF" value="pdf" />
          <el-option label="Word" value="word" />
          <el-option label="Excel" value="excel" />
          <el-option label="其他" value="other" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-upload
          ref="upload"
          :action="uploadUrl"
          :headers="headers"
          :data="uploadData"
          :on-success="handleUploadSuccess"
          :on-error="handleUploadError"
          :before-upload="handleBeforeUpload"
          :show-file-list="false"
          name="files"
          multiple
          :limit="50"
        >
          <el-button type="primary" icon="el-icon-upload" size="mini">上传公共文件</el-button>
        </el-upload>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="fileList">
      <el-table-column label="文件图标" align="center" width="100">
        <template slot-scope="scope">
          <i :class="getFileIcon(scope.row.fileType)" style="font-size: 30px;"></i>
        </template>
      </el-table-column>
      <el-table-column label="文件名称" align="center" prop="originalName" :show-overflow-tooltip="true" />
      <el-table-column label="文件大小" align="center" prop="fileSize" width="120">
        <template slot-scope="scope">
          {{ formatFileSize(scope.row.fileSize) }}
        </template>
      </el-table-column>
      <el-table-column label="文件类型" align="center" prop="fileType" width="100" />
      <el-table-column label="上传用户" align="center" prop="userName" width="120" />
      <el-table-column label="上传时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="handleDownload(scope.row)"
          >下载</el-button>
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
  </div>
</template>

<script>
import { publicList, getDownloadUrl } from "@/api/system/file";
import { getToken } from "@/utils/auth";

export default {
  name: "PublicFile",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 文件信息表格数据
      fileList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        originalName: null,
        fileType: null
      },
      // 上传地址
      uploadUrl: process.env.VUE_APP_BASE_API + "/system/file/upload",
      // 上传请求头
      headers: {
        Authorization: "Bearer " + getToken()
      },
      // 上传参数
      uploadData: {
        isPublic: "1"  // 公共文件
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询文件信息列表 */
    getList() {
      this.loading = true;
      publicList(this.queryParams).then(response => {
        this.fileList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
    /** 下载按钮操作 */
    handleDownload(row) {
      getDownloadUrl(row.fileId).then(response => {
        if (response.data && response.data.fileUrl) {
          // MinIO URL已包含强制下载的响应头，直接跳转
          window.location.href = response.data.fileUrl;
        }
      });
    },
    /** 上传前校验 */
    handleBeforeUpload(file) {
      const isLt100M = file.size / 1024 / 1024 < 100;
      if (!isLt100M) {
        this.$modal.msgError('上传文件大小不能超过 100MB!');
        return false;
      }
      return true;
    },
    /** 上传成功回调 */
    handleUploadSuccess(response, file, fileList) {
      console.log('上传响应:', response);
      if (response.code === 200) {
        this.$modal.msgSuccess("上传成功");
        // 延迟一下刷新，确保数据库已提交
        setTimeout(() => {
          this.getList();
        }, 100);
      } else {
        console.error('上传失败:', response);
        this.$modal.msgError(response.msg || "上传失败");
      }
    },
    /** 上传失败回调 */
    handleUploadError(err, file, fileList) {
      console.error('上传错误:', err);
      let errorMsg = "上传失败";
      try {
        const response = JSON.parse(err.message);
        if (response.msg) {
          errorMsg = response.msg;
        }
      } catch (e) {
        // 忽略JSON解析错误
      }
      this.$modal.msgError(errorMsg);
    },
    /** 获取文件图标 */
    getFileIcon(fileType) {
      const iconMap = {
        'image': 'el-icon-picture',
        'video': 'el-icon-video-camera',
        'audio': 'el-icon-headset',
        'pdf': 'el-icon-document',
        'word': 'el-icon-document',
        'excel': 'el-icon-document',
        'other': 'el-icon-document'
      };
      return iconMap[fileType] || 'el-icon-document';
    },
    /** 格式化文件大小 */
    formatFileSize(bytes) {
      if (bytes === 0) return '0 B';
      const k = 1024;
      const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
      const i = Math.floor(Math.log(bytes) / Math.log(k));
      return (bytes / Math.pow(k, i)).toFixed(2) + ' ' + sizes[i];
    }
  }
};
</script>
