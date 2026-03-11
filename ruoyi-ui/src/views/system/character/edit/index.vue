<template>
  <div class="app-container">
    <el-page-header @back="goBack" :content="title" style="margin-bottom: 20px;"></el-page-header>

    <el-card>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-divider content-position="left">基本信息</el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="人物姓名" prop="characterName">
              <el-input v-model="form.characterName" placeholder="请输入人物姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="头衔/职位" prop="characterTitle">
              <el-input v-model="form.characterTitle" placeholder="请输入头衔/职位" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="出生日期" prop="birthDate">
              <el-date-picker
                v-model="form.birthDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择出生日期"
                style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序" prop="sortOrder">
              <el-input-number v-model="form.sortOrder" controls-position="right" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="人物简介" prop="characterIntro">
          <el-input v-model="form.characterIntro" type="textarea" :rows="3" placeholder="请输入人物简介" />
        </el-form-item>

        <el-form-item label="详细介绍" prop="characterDescription">
          <el-input v-model="form.characterDescription" type="textarea" :rows="8" placeholder="请输入详细介绍" />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio label="0">正常</el-radio>
                <el-radio label="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>

        <el-divider content-position="left">头像和封面</el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="头像">
              <el-upload
                class="avatar-uploader"
                :action="uploadUrl"
                :headers="uploadHeaders"
                :data="{isPublic: '0'}"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
                accept="image/*"
                name="files"
              >
                <img v-if="avatarUrl" :src="avatarUrl" class="avatar" />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
              <div class="upload-tip">支持jpg/png，大小不超过10MB</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="封面图">
              <el-upload
                class="avatar-uploader"
                :action="uploadUrl"
                :headers="uploadHeaders"
                :data="{isPublic: '0'}"
                :show-file-list="false"
                :on-success="handleCoverSuccess"
                :before-upload="beforeCoverUpload"
                accept="image/*"
                name="files"
              >
                <img v-if="coverUrl" :src="coverUrl" class="cover" />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
              <div class="upload-tip">支持jpg/png，大小不超过10MB</div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left" v-if="characterId">人物文件</el-divider>

        <div v-if="characterId">
          <!-- 图片上传区域 -->
          <div style="margin-bottom: 30px;">
            <h4>图片</h4>
            <el-upload
              :auto-upload="false"
              :on-change="handleImageChange"
              :on-remove="handleImageRemove"
              :file-list="imageFileList"
              list-type="picture-card"
              accept="image/*"
              multiple
            >
              <i class="el-icon-plus"></i>
            </el-upload>
          </div>

          <!-- 视频上传区域 -->
          <div style="margin-bottom: 30px;">
            <h4>视频</h4>
            <el-upload
              :auto-upload="false"
              :on-change="handleVideoChange"
              :on-remove="handleVideoRemove"
              :file-list="videoFileList"
              :show-file-list="false"
              accept="video/*"
              multiple
            >
              <el-button size="small" type="primary"><i class="el-icon-upload"></i> 选择视频</el-button>
              <div slot="tip" class="el-upload__tip">支持mp4/avi等格式</div>
            </el-upload>
            <!-- 自定义视频列表 -->
            <el-table :data="videoFileList" style="margin-top: 10px;" v-if="videoFileList.length > 0">
              <el-table-column prop="name" label="文件名" />
              <el-table-column label="操作" width="150">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" @click="handleVideoPreview(scope.row)" v-if="scope.row.isExisting">预览</el-button>
                  <el-button size="mini" type="text" @click="handleVideoRemoveClick(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 文档上传区域 -->
          <div style="margin-bottom: 30px;">
            <h4>文档</h4>
            <el-upload
              :auto-upload="false"
              :on-change="handleDocumentChange"
              :on-remove="handleDocumentRemove"
              :file-list="documentFileList"
              :show-file-list="false"
              accept=".pdf,.doc,.docx,.txt"
              multiple
            >
              <el-button size="small" type="primary"><i class="el-icon-upload"></i> 选择文档</el-button>
              <div slot="tip" class="el-upload__tip">支持pdf/doc/txt等格式</div>
            </el-upload>
            <!-- 自定义文档列表 -->
            <el-table :data="documentFileList" style="margin-top: 10px;" v-if="documentFileList.length > 0">
              <el-table-column prop="name" label="文件名" />
              <el-table-column label="操作" width="150">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" @click="handleDocumentDownload(scope.row)" v-if="scope.row.isExisting">下载</el-button>
                  <el-button size="mini" type="text" @click="handleDocumentRemoveClick(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>

        <el-form-item style="margin-top: 30px;">
          <el-button type="primary" @click="submitForm" :loading="submitLoading">保存</el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 视频预览对话框 -->
    <el-dialog title="视频预览" :visible.sync="videoPreviewVisible" width="800px" append-to-body>
      <video v-if="currentVideoUrl" :src="currentVideoUrl" controls style="width: 100%; max-height: 500px;"></video>
    </el-dialog>
  </div>
</template>

<script>
import { getCharacter, addCharacter, updateCharacter, getCharacterFiles, delCharacterFile, clearCharacterFiles, uploadCharacterFiles, addCharacterFiles } from "@/api/system/character";
import { getDownloadUrl } from "@/api/system/file";
import { getToken } from "@/utils/auth";

export default {
  name: "CharacterEdit",
  data() {
    return {
      title: "添加人物",
      characterId: null,
      submitLoading: false,
      uploadUrl: process.env.VUE_APP_BASE_API + "/system/file/upload",
      uploadHeaders: {
        Authorization: "Bearer " + getToken()
      },
      avatarUrl: "",
      coverUrl: "",
      form: {
        characterId: null,
        characterName: null,
        characterTitle: null,
        characterIntro: null,
        characterDescription: null,
        birthDate: null,
        avatarFileId: null,
        coverFileId: null,
        sortOrder: 0,
        status: "0",
        remark: null
      },
      rules: {
        characterName: [
          { required: true, message: "人物姓名不能为空", trigger: "blur" }
        ]
      },
      imageFileList: [],
      videoFileList: [],
      documentFileList: [],
      // 新增：用于保存原始File对象
      pendingImageFiles: [],
      pendingVideoFiles: [],
      pendingDocumentFiles: [],
      // 视频预览
      videoPreviewVisible: false,
      currentVideoUrl: ''
    };
  },
  created() {
    const id = this.$route.query.id;
    if (id) {
      this.characterId = id;
      this.title = "编辑人物";
      this.getDetail(id);
      this.loadCharacterFiles(id);
    }
  },
  watch: {
    // 监听路由变化，当从人物1切换到人物2时重新加载
    '$route.query.id': function(newId, oldId) {
      if (newId && newId !== oldId) {
        // 重置数据
        this.resetData();
        // 加载新数据
        this.characterId = newId;
        this.title = "编辑人物";
        this.getDetail(newId);
        this.loadCharacterFiles(newId);
      } else if (!newId) {
        // 切换到新增模式
        this.resetData();
        this.characterId = null;
        this.title = "添加人物";
      }
    }
  },
  methods: {
    // 重置数据
    resetData() {
      this.avatarUrl = "";
      this.coverUrl = "";
      this.form = {
        characterId: null,
        characterName: null,
        characterTitle: null,
        characterIntro: null,
        characterDescription: null,
        birthDate: null,
        avatarFileId: null,
        coverFileId: null,
        sortOrder: 0,
        status: "0",
        remark: null
      };
      this.imageFileList = [];
      this.videoFileList = [];
      this.documentFileList = [];
      this.pendingImageFiles = [];
      this.pendingVideoFiles = [];
      this.pendingDocumentFiles = [];
      this.videoPreviewVisible = false;
      this.currentVideoUrl = '';
    },
    getDetail(id) {
      getCharacter(id).then(response => {
        this.form = response.data;
        // 重置头像和封面URL
        this.avatarUrl = "";
        this.coverUrl = "";
        // 如果有头像，加载URL
        if (this.form.avatarFileId) {
          getDownloadUrl(this.form.avatarFileId).then(res => {
            if (res.code === 200 && res.data.fileUrl) {
              this.avatarUrl = res.data.fileUrl;
            }
          }).catch(() => {});
        }
        // 如果有封面，加载URL
        if (this.form.coverFileId) {
          getDownloadUrl(this.form.coverFileId).then(res => {
            if (res.code === 200 && res.data.fileUrl) {
              this.coverUrl = res.data.fileUrl;
            }
          }).catch(() => {});
        }
      });
    },
    loadCharacterFiles(id) {
      getCharacterFiles(id).then(response => {
        const files = response.data;
        // 分类文件到不同列表，同时保存到pending数组中
        this.imageFileList = files.filter(f => f.fileType === 'image').map(f => ({
          name: f.fileInfo ? f.fileInfo.originalName : '未知文件',
          url: f.fileInfo && f.fileInfo.fileUrl ? f.fileInfo.fileUrl : '',
          uid: f.id,
          // 标记为已存在的文件，不是新上传的
          isExisting: true,
          fileId: f.fileId // 保存文件ID用于重新关联
        }));
        this.videoFileList = files.filter(f => f.fileType === 'video').map(f => ({
          name: f.fileInfo ? f.fileInfo.originalName : '未知文件',
          url: f.fileInfo && f.fileInfo.fileUrl ? f.fileInfo.fileUrl : '',
          uid: f.id,
          isExisting: true,
          fileId: f.fileId
        }));
        this.documentFileList = files.filter(f => f.fileType === 'document').map(f => ({
          name: f.fileInfo ? f.fileInfo.originalName : '未知文件',
          url: f.fileInfo && f.fileInfo.fileUrl ? f.fileInfo.fileUrl : '',
          uid: f.id,
          isExisting: true,
          fileId: f.fileId
        }));
        // 将已存在的文件也加载到pending数组
        this.pendingImageFiles = [...this.imageFileList];
        this.pendingVideoFiles = [...this.videoFileList];
        this.pendingDocumentFiles = [...this.documentFileList];
      }).catch(error => {
        console.error('加载文件列表失败', error);
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.submitLoading = true;
          if (this.form.characterId != null) {
            // 编辑模式：先保存基本信息，再处理文件
            updateCharacter(this.form).then(response => {
              // 保存成功后，上传待处理的文件
              return this.uploadAllFiles();
            }).then(() => {
              this.$modal.msgSuccess("修改成功");
              this.submitLoading = false;
              this.goBack();
            }).catch(() => {
              this.submitLoading = false;
            });
          } else {
            // 新增模式：先保存基本信息，再上传文件
            addCharacter(this.form).then(response => {
              this.characterId = response.data.characterId;
              this.form.characterId = response.data.characterId;
              this.title = "编辑人物";
              // 上传文件
              return this.uploadAllFiles();
            }).then(() => {
              this.$modal.msgSuccess("新增成功");
              this.submitLoading = false;
              // 返回列表页
              this.goBack();
            }).catch(() => {
              this.submitLoading = false;
            });
          }
        }
      });
    },
    // 上传所有待处理的文件
    async uploadAllFiles() {
      if (!this.characterId) {
        return Promise.resolve();
      }

      try {
        // 1. 先清空所有关联（调用后端接口）
        await this.clearAllCharacterFiles();

        // 2. 处理图片
        await this.processFiles(this.pendingImageFiles, 'image');

        // 3. 处理视频
        await this.processFiles(this.pendingVideoFiles, 'video');

        // 4. 处理文档
        await this.processFiles(this.pendingDocumentFiles, 'document');

        return Promise.resolve();
      } catch (error) {
        return Promise.reject(error);
      }
    },
    // 处理文件：区分新上传和已存在的文件
    async processFiles(fileList, fileType) {
      if (fileList.length === 0) {
        return;
      }

      // 分离新文件和已存在的文件
      const newFiles = fileList.filter(f => !f.isExisting && f.raw);
      const existingFiles = fileList.filter(f => f.isExisting && f.fileId);

      // 上传新文件
      if (newFiles.length > 0) {
        const formData = new FormData();
        newFiles.forEach(file => {
          formData.append('files', file.raw);
        });
        formData.append('fileType', fileType);
        formData.append('fileCategory', '');
        await uploadCharacterFiles(this.characterId, formData);
      }

      // 重新关联已存在的文件
      if (existingFiles.length > 0) {
        const fileIds = existingFiles.map(f => f.fileId);
        await addCharacterFiles({
          characterId: this.characterId,
          fileIds: fileIds,
          fileType: fileType,
          fileCategory: ''
        });
      }
    },
    // 清空人物的所有文件关联
    async clearAllCharacterFiles() {
      try {
        // 调用后端接口清空关联
        await clearCharacterFiles(this.characterId);
      } catch (error) {
        console.error('清空文件关联失败', error);
        throw error;
      }
    },
    // 头像上传
    beforeAvatarUpload(file) {
      const isImage = file.type.startsWith('image/');
      const isLt10M = file.size / 1024 / 1024 < 10;
      if (!isImage) {
        this.$message.error('只能上传图片文件!');
      }
      if (!isLt10M) {
        this.$message.error('图片大小不能超过 10MB!');
      }
      return isImage && isLt10M;
    },
    handleAvatarSuccess(res, file) {
      if (res.code === 200) {
        // 文件上传接口返回的是数组，取第一个
        const fileInfo = Array.isArray(res.data) ? res.data[0] : res.data;
        this.form.avatarFileId = fileInfo.fileId;
        this.avatarUrl = URL.createObjectURL(file.raw);
        this.$message.success('头像上传成功');
      } else {
        this.$message.error(res.msg || '上传失败');
      }
    },
    // 封面上传
    beforeCoverUpload(file) {
      const isImage = file.type.startsWith('image/');
      const isLt10M = file.size / 1024 / 1024 < 10;
      if (!isImage) {
        this.$message.error('只能上传图片文件!');
      }
      if (!isLt10M) {
        this.$message.error('图片大小不能超过 10MB!');
      }
      return isImage && isLt10M;
    },
    handleCoverSuccess(res, file) {
      if (res.code === 200) {
        // 文件上传接口返回的是数组，取第一个
        const fileInfo = Array.isArray(res.data) ? res.data[0] : res.data;
        this.form.coverFileId = fileInfo.fileId;
        this.coverUrl = URL.createObjectURL(file.raw);
        this.$message.success('封面上传成功');
      } else {
        this.$message.error(res.msg || '上传失败');
      }
    },
    // 图片文件变化
    handleImageChange(file, fileList) {
      this.imageFileList = fileList;
      this.pendingImageFiles = fileList;
    },
    // 图片文件删除
    handleImageRemove(file, fileList) {
      this.imageFileList = fileList;
      this.pendingImageFiles = fileList;
    },
    // 视频文件变化
    handleVideoChange(file, fileList) {
      this.videoFileList = fileList;
      this.pendingVideoFiles = fileList;
    },
    // 视频文件删除
    handleVideoRemove(file, fileList) {
      this.videoFileList = fileList;
      this.pendingVideoFiles = fileList;
    },
    // 文档文件变化
    handleDocumentChange(file, fileList) {
      this.documentFileList = fileList;
      this.pendingDocumentFiles = fileList;
    },
    // 文档文件删除
    handleDocumentRemove(file, fileList) {
      this.documentFileList = fileList;
      this.pendingDocumentFiles = fileList;
    },
    // 视频预览
    handleVideoPreview(file) {
      if (file.url) {
        this.currentVideoUrl = file.url;
        this.videoPreviewVisible = true;
      } else {
        this.$message.warning('无法预览该视频');
      }
    },
    // 视频手动删除
    handleVideoRemoveClick(file) {
      const index = this.videoFileList.findIndex(f => f.uid === file.uid);
      if (index > -1) {
        this.videoFileList.splice(index, 1);
        this.pendingVideoFiles = [...this.videoFileList];
      }
    },
    // 文档下载
    handleDocumentDownload(file) {
      if (file.url) {
        window.open(file.url, '_blank');
      } else {
        this.$message.warning('无法下载该文档');
      }
    },
    // 文档手动删除
    handleDocumentRemoveClick(file) {
      const index = this.documentFileList.findIndex(f => f.uid === file.uid);
      if (index > -1) {
        this.documentFileList.splice(index, 1);
        this.pendingDocumentFiles = [...this.documentFileList];
      }
    },
    goBack() {
      this.$router.back();
    }
  }
};
</script>

<style scoped>
.app-container {
  padding: 20px;
}

h4 {
  margin: 10px 0;
  color: #606266;
  font-size: 16px;
  font-weight: 600;
}

.avatar-uploader, .cover-uploader {
  display: inline-block;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
}

.avatar, .cover {
  width: 120px;
  height: 120px;
  display: block;
  object-fit: cover;
  border-radius: 6px;
}

.upload-tip {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}
</style>
