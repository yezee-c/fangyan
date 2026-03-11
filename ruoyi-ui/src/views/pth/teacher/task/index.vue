<template>
  <div class="app-container">
    <el-card>
      <div slot="header">
        <span><i class="el-icon-s-order"></i> 任务管理</span>
        <el-button type="primary" size="small" style="float: right;" @click="handleAdd">
          <i class="el-icon-plus"></i> 发布任务
        </el-button>
      </div>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form">
        <el-form-item label="任务类型">
          <el-select v-model="searchType" placeholder="全部" clearable style="width: 120px;">
            <el-option label="测评任务" value="测评任务"></el-option>
            <el-option label="练习任务" value="练习任务"></el-option>
            <el-option label="录音任务" value="录音任务"></el-option>
            <el-option label="阅读任务" value="阅读任务"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchStatus" placeholder="全部" clearable style="width: 120px;">
            <el-option label="进行中" value="0"></el-option>
            <el-option label="已结束" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="filteredTaskList" v-loading="loading">
        <el-table-column label="任务名称" prop="taskName" min-width="150" />
        <el-table-column label="任务类型" prop="taskType" width="120">
          <template slot-scope="scope">
            <el-tag :type="getTaskTypeColor(scope.row.taskType)">{{ scope.row.taskType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="目标班级" prop="className" width="150" />
        <el-table-column label="截止时间" prop="deadline" width="180" />
        <el-table-column label="完成情况" width="200">
          <template slot-scope="scope">
            <div>
              <el-progress :percentage="scope.row.completeRate" :color="getProgressColor(scope.row.completeRate)" />
              <span style="font-size: 12px; color: #909399;">{{ scope.row.completedCount }}/{{ scope.row.totalCount }}人</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="status" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '0' ? 'success' : 'info'">
              {{ scope.row.status === '0' ? '进行中' : '已结束' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-view" @click="viewSubmissions(scope.row)">查看提交</el-button>
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" style="color: #F56C6C;">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="filteredTaskList.length === 0" description="暂无任务"></el-empty>
    </el-card>

    <!-- 发布/编辑任务对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" @close="resetForm">
      <el-form ref="taskForm" :model="taskForm" :rules="taskRules" label-width="100px">
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="taskForm.taskName" placeholder="请输入任务名称"></el-input>
        </el-form-item>
        <el-form-item label="任务类型" prop="taskType">
          <el-select v-model="taskForm.taskType" placeholder="请选择任务类型" style="width: 100%;">
            <el-option label="测评任务" value="测评任务"></el-option>
            <el-option label="练习任务" value="练习任务"></el-option>
            <el-option label="录音任务" value="录音任务"></el-option>
            <el-option label="阅读任务" value="阅读任务"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="目标班级" prop="classId">
          <el-select v-model="taskForm.classId" placeholder="请选择班级" style="width: 100%;">
            <el-option v-for="cls in classList" :key="cls.classId" :label="cls.className" :value="cls.classId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="任务内容" prop="taskContent">
          <el-input v-model="taskForm.taskContent" type="textarea" :rows="4" placeholder="请输入任务内容和要求"></el-input>
        </el-form-item>
        <el-form-item label="截止时间" prop="deadline">
          <el-date-picker
            v-model="taskForm.deadline"
            type="datetime"
            placeholder="选择截止时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            style="width: 100%;"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="分数" prop="totalScore">
          <el-input-number v-model="taskForm.totalScore" :min="0" :max="100" label="总分"></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitTask" :loading="submitting">确定</el-button>
      </div>
    </el-dialog>

    <!-- 查看提交情况对话框 -->
    <el-dialog title="任务提交情况" :visible.sync="submissionDialogVisible" width="900px">
      <div v-if="currentTask">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="任务名称">{{ currentTask.taskName }}</el-descriptions-item>
          <el-descriptions-item label="任务类型">
            <el-tag :type="getTaskTypeColor(currentTask.taskType)">{{ currentTask.taskType }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="班级">{{ currentTask.className }}</el-descriptions-item>
          <el-descriptions-item label="完成情况" :span="3">
            <el-progress :percentage="currentTask.completeRate" :color="getProgressColor(currentTask.completeRate)"></el-progress>
            <span style="margin-left: 10px;">{{ currentTask.completedCount }}/{{ currentTask.totalCount }}人已完成</span>
          </el-descriptions-item>
        </el-descriptions>

        <el-divider></el-divider>

        <el-table :data="submissions" style="margin-top: 20px;">
          <el-table-column label="学生姓名" prop="studentName" width="120" />
          <el-table-column label="学号" prop="studentId" width="120" />
          <el-table-column label="提交时间" prop="submitTime" width="180" />
          <el-table-column label="得分" width="120">
            <template slot-scope="scope">
              <span v-if="scope.row.score !== null" :style="{ color: getScoreColor(scope.row.score) }">
                {{ scope.row.score }}分
              </span>
              <el-tag v-else type="info" size="small">未评分</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="状态" width="100">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.status === 'submitted'" type="success">已提交</el-tag>
              <el-tag v-else-if="scope.row.status === 'graded'" type="primary">已评分</el-tag>
              <el-tag v-else type="info">未提交</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template slot-scope="scope">
              <el-button v-if="scope.row.status === 'submitted' || scope.row.status === 'graded'" 
                         size="mini" type="text" @click="gradeSubmission(scope.row)">
                {{ scope.row.status === 'graded' ? '修改评分' : '评分' }}
              </el-button>
              <span v-else style="color: #909399;">未提交</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>

    <!-- 评分对话框 -->
    <el-dialog title="任务评分" :visible.sync="gradeDialogVisible" width="500px">
      <el-form v-if="currentTask" :model="gradeForm" label-width="100px">
        <el-form-item label="学生姓名">
          <el-input v-model="gradeForm.studentName" disabled></el-input>
        </el-form-item>
        <el-form-item label="得分">
          <el-input-number v-model="gradeForm.score" :min="0" :max="currentTask.totalScore" style="width: 100%;"></el-input-number>
          <span style="margin-left: 10px; color: #909399;">/{{ currentTask.totalScore }}分</span>
        </el-form-item>
        <el-form-item label="评语">
          <el-input v-model="gradeForm.feedback" type="textarea" :rows="4" placeholder="请输入评语和建议"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="gradeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitGrade">提交评分</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
const TASK_STORAGE_KEY = 'pth_task_list'
const SUBMISSION_STORAGE_KEY = 'pth_task_submissions'

export default {
  name: "Task",
  data() {
    return {
      loading: false,
      taskList: [],
      classList: [],
      searchType: '',
      searchStatus: '',
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      submitting: false,
      
      // 任务表单
      taskForm: {
        taskId: null,
        taskName: '',
        taskType: '',
        classId: null,
        className: '',
        taskContent: '',
        deadline: '',
        totalScore: 100,
        status: '0'
      },
      
      taskRules: {
        taskName: [{ required: true, message: '请输入任务名称', trigger: 'blur' }],
        taskType: [{ required: true, message: '请选择任务类型', trigger: 'change' }],
        classId: [{ required: true, message: '请选择目标班级', trigger: 'change' }],
        taskContent: [{ required: true, message: '请输入任务内容', trigger: 'blur' }],
        deadline: [{ required: true, message: '请选择截止时间', trigger: 'change' }]
      },
      
      // 提交情况
      submissionDialogVisible: false,
      currentTask: null,
      submissions: [],
      
      // 评分
      gradeDialogVisible: false,
      gradeForm: {
        submissionId: null,
        studentName: '',
        score: 0,
        feedback: ''
      }
    };
  },
  
  computed: {
    filteredTaskList() {
      let list = this.taskList
      
      if (this.searchType) {
        list = list.filter(task => task.taskType === this.searchType)
      }
      
      if (this.searchStatus) {
        list = list.filter(task => task.status === this.searchStatus)
      }
      
      return list
    }
  },
  
  created() {
    this.loadData()
  },
  
  methods: {
    loadData() {
      // 加载班级列表
      const classList = localStorage.getItem('pth_class_list')
      this.classList = classList ? JSON.parse(classList) : []
      
      // 加载任务列表
      const taskList = localStorage.getItem(TASK_STORAGE_KEY)
      if (taskList) {
        this.taskList = JSON.parse(taskList)
      } else {
        // 初始化模拟数据
        this.taskList = [
          {
            taskId: 1,
            taskName: '单字朗读练习',
            taskType: '练习任务',
            classId: 1,
            className: '一年级一班',
            taskContent: '请按要求朗读以下单字：鹤、颜、振、翁、略，注意发音和声调。',
            deadline: '2024-12-20 23:59:59',
            totalScore: 100,
            totalCount: 45,
            completedCount: 38,
            completeRate: 84,
            status: '0',
            createTime: '2024-12-10'
          },
          {
            taskId: 2,
            taskName: '普通话水平测试',
            taskType: '测评任务',
            classId: 1,
            className: '一年级一班',
            taskContent: '完成普通话水平测试的四个部分：单字朗读、词语朗读、短文朗读、命题说话。',
            deadline: '2024-12-25 23:59:59',
            totalScore: 100,
            totalCount: 45,
            completedCount: 25,
            completeRate: 56,
            status: '0',
            createTime: '2024-12-08'
          }
        ]
        this.saveTaskList()
      }
    },
    
    saveTaskList() {
      localStorage.setItem(TASK_STORAGE_KEY, JSON.stringify(this.taskList))
    },
    
    handleSearch() {
      // 搜索功能已通过computed属性实现
    },
    
    resetSearch() {
      this.searchType = ''
      this.searchStatus = ''
    },
    
    handleAdd() {
      this.resetForm()
      this.dialogTitle = '发布任务'
      this.dialogVisible = true
    },
    
    handleEdit(row) {
      this.taskForm = { ...row }
      this.dialogTitle = '编辑任务'
      this.dialogVisible = true
    },
    
    handleDelete(row) {
      this.$confirm(`确定删除任务「${row.taskName}」吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.taskList = this.taskList.filter(t => t.taskId !== row.taskId)
        this.saveTaskList()
        this.$message.success('删除成功')
      }).catch(() => {})
    },
    
    submitTask() {
      this.$refs.taskForm.validate(valid => {
        if (!valid) return
        
        this.submitting = true
        
        // 找到选中的班级
        const selectedClass = this.classList.find(c => c.classId === this.taskForm.classId)
        if (selectedClass) {
          this.taskForm.className = selectedClass.className
          this.taskForm.totalCount = selectedClass.studentCount || 0
        }
        
        setTimeout(() => {
          if (this.taskForm.taskId) {
            // 编辑
            const index = this.taskList.findIndex(t => t.taskId === this.taskForm.taskId)
            if (index !== -1) {
              this.taskList[index] = { ...this.taskList[index], ...this.taskForm }
            }
            this.$message.success('修改成功')
          } else {
            // 新增
            const maxId = this.taskList.length > 0 ? Math.max(...this.taskList.map(t => t.taskId)) : 0
            const newTask = {
              ...this.taskForm,
              taskId: maxId + 1,
              completedCount: 0,
              completeRate: 0,
              createTime: new Date().toISOString().split('T')[0]
            }
            this.taskList.unshift(newTask)
            this.$message.success('发布成功')
          }
          
          this.saveTaskList()
          this.dialogVisible = false
          this.submitting = false
        }, 500)
      })
    },
    
    resetForm() {
      this.taskForm = {
        taskId: null,
        taskName: '',
        taskType: '',
        classId: null,
        className: '',
        taskContent: '',
        deadline: '',
        totalScore: 100,
        status: '0'
      }
      if (this.$refs.taskForm) {
        this.$refs.taskForm.clearValidate()
      }
    },
    
    viewSubmissions(row) {
      this.currentTask = row
      this.loadSubmissions(row.taskId)
      this.submissionDialogVisible = true
    },
    
    loadSubmissions(taskId) {
      // 加载提交记录
      const allSubmissions = localStorage.getItem(SUBMISSION_STORAGE_KEY)
      let submissions = allSubmissions ? JSON.parse(allSubmissions) : {}
      
      if (!submissions[taskId]) {
        // 生成模拟数据
        submissions[taskId] = this.generateMockSubmissions(taskId)
        localStorage.setItem(SUBMISSION_STORAGE_KEY, JSON.stringify(submissions))
      }
      
      this.submissions = submissions[taskId]
    },
    
    generateMockSubmissions(taskId) {
      const task = this.taskList.find(t => t.taskId === taskId)
      if (!task) return []
      
      const submissions = []
      const studentNames = ['张三', '李四', '王五', '赵六', '刘七', '陈八', '周九', '吴十', '郑十一', '孙十二']
      
      for (let i = 0; i < Math.min(task.totalCount, 10); i++) {
        const isSubmitted = i < task.completedCount
        const isGraded = isSubmitted && Math.random() > 0.3
        
        submissions.push({
          submissionId: i + 1,
          taskId: taskId,
          studentId: `2024${String(i + 1).padStart(4, '0')}`,
          studentName: studentNames[i % studentNames.length] + (i > 9 ? i : ''),
          submitTime: isSubmitted ? `2024-12-${10 + Math.floor(i / 3)} ${8 + (i % 12)}:${(i * 5) % 60}:00` : null,
          score: isGraded ? Math.floor(Math.random() * 30) + 70 : null,
          feedback: isGraded ? '发音准确，声调把握很好，继续加油！' : '',
          status: isGraded ? 'graded' : (isSubmitted ? 'submitted' : 'pending')
        })
      }
      
      return submissions
    },
    
    gradeSubmission(submission) {
      this.gradeForm = {
        submissionId: submission.submissionId,
        studentName: submission.studentName,
        score: submission.score || 0,
        feedback: submission.feedback || ''
      }
      this.gradeDialogVisible = true
    },
    
    submitGrade() {
      // 保存评分
      const submissionIndex = this.submissions.findIndex(s => s.submissionId === this.gradeForm.submissionId)
      if (submissionIndex !== -1) {
        this.submissions[submissionIndex].score = this.gradeForm.score
        this.submissions[submissionIndex].feedback = this.gradeForm.feedback
        this.submissions[submissionIndex].status = 'graded'
        
        // 保存到localStorage
        const allSubmissions = JSON.parse(localStorage.getItem(SUBMISSION_STORAGE_KEY) || '{}')
        allSubmissions[this.currentTask.taskId] = this.submissions
        localStorage.setItem(SUBMISSION_STORAGE_KEY, JSON.stringify(allSubmissions))
        
        this.$message.success('评分成功')
        this.gradeDialogVisible = false
      }
    },
    
    getProgressColor(rate) {
      if (rate >= 80) return '#67C23A'
      if (rate >= 50) return '#E6A23C'
      return '#F56C6C'
    },
    
    getTaskTypeColor(type) {
      const colors = {
        '测评任务': 'danger',
        '练习任务': 'success',
        '录音任务': 'warning',
        '阅读任务': 'primary'
      }
      return colors[type] || ''
    },
    
    getScoreColor(score) {
      if (score >= 90) return '#67C23A'
      if (score >= 80) return '#409EFF'
      if (score >= 60) return '#E6A23C'
      return '#F56C6C'
    }
  }
};
</script>

<style scoped>
.search-form {
  margin-bottom: 20px;
}
</style>
