import request from '@/utils/request'

// 本地存储的key
const STORAGE_KEY = 'pth_class_list'

// 获取本地数据
function getLocalData() {
  const data = localStorage.getItem(STORAGE_KEY)
  if (data) {
    return JSON.parse(data)
  }
  // 初始模拟数据
  const initialData = [
    {
      classId: 1,
      className: '一年级一班',
      grade: '2024级',
      semester: '2024-2025第一学期',
      invitationCode: 'ABC123',
      studentCount: 45,
      status: '0',
      createTime: '2024-09-01',
      remark: '普通话重点班级'
    },
    {
      classId: 2,
      className: '一年级二班',
      grade: '2024级',
      semester: '2024-2025第一学期',
      invitationCode: 'DEF456',
      studentCount: 42,
      status: '0',
      createTime: '2024-09-01',
      remark: ''
    },
    {
      classId: 3,
      className: '二年级一班',
      grade: '2023级',
      semester: '2024-2025第一学期',
      invitationCode: 'GHI789',
      studentCount: 48,
      status: '0',
      createTime: '2023-09-01',
      remark: ''
    }
  ]
  localStorage.setItem(STORAGE_KEY, JSON.stringify(initialData))
  return initialData
}

// 保存本地数据
function saveLocalData(data) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(data))
}

// 生成随机邀请码
function generateRandomCode() {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'
  let code = ''
  for (let i = 0; i < 6; i++) {
    code += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  return code
}

// 查询普通话班级列表
export function listClass(query) {
  return new Promise((resolve) => {
    setTimeout(() => {
      let data = getLocalData()
      
      // 过滤
      if (query.className) {
        data = data.filter(item => item.className.includes(query.className))
      }
      if (query.grade) {
        data = data.filter(item => item.grade && item.grade.includes(query.grade))
      }
      if (query.semester) {
        data = data.filter(item => item.semester && item.semester.includes(query.semester))
      }
      
      // 分页
      const pageNum = query.pageNum || 1
      const pageSize = query.pageSize || 10
      const start = (pageNum - 1) * pageSize
      const end = start + pageSize
      
      resolve({
        code: 200,
        msg: '查询成功',
        rows: data.slice(start, end),
        total: data.length
      })
    }, 300)
  })
}

// 查询教师的班级列表
export function listMyClass(query) {
  return listClass(query || {})
}

// 根据邀请码查询班级信息
export function getClassByCode(invitationCode) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      const data = getLocalData()
      const classInfo = data.find(item => item.invitationCode === invitationCode)
      
      if (classInfo) {
        resolve({
          code: 200,
          msg: '查询成功',
          data: classInfo
        })
      } else {
        reject({
          code: 500,
          msg: '邀请码不存在'
        })
      }
    }, 300)
  })
}

// 查询普通话班级详细
export function getClass(classId) {
  return new Promise((resolve) => {
    setTimeout(() => {
      const data = getLocalData()
      const classInfo = data.find(item => item.classId === classId)
      
      resolve({
        code: 200,
        msg: '查询成功',
        data: classInfo || {}
      })
    }, 300)
  })
}

// 新增普通话班级
export function addClass(data) {
  return new Promise((resolve) => {
    setTimeout(() => {
      const classList = getLocalData()
      
      // 生成新ID
      const maxId = classList.length > 0 ? Math.max(...classList.map(item => item.classId)) : 0
      const newClass = {
        ...data,
        classId: maxId + 1,
        studentCount: 0,
        status: '0',
        createTime: new Date().toISOString().split('T')[0]
      }
      
      classList.push(newClass)
      saveLocalData(classList)
      
      resolve({
        code: 200,
        msg: '新增成功',
        data: newClass
      })
    }, 300)
  })
}

// 修改普通话班级
export function updateClass(data) {
  return new Promise((resolve) => {
    setTimeout(() => {
      const classList = getLocalData()
      const index = classList.findIndex(item => item.classId === data.classId)
      
      if (index !== -1) {
        classList[index] = { ...classList[index], ...data }
        saveLocalData(classList)
      }
      
      resolve({
        code: 200,
        msg: '修改成功'
      })
    }, 300)
  })
}

// 删除普通话班级
export function delClass(classId) {
  return new Promise((resolve) => {
    setTimeout(() => {
      let classList = getLocalData()
      
      // 支持单个删除和批量删除
      const idsToDelete = Array.isArray(classId) ? classId : [classId]
      classList = classList.filter(item => !idsToDelete.includes(item.classId))
      
      saveLocalData(classList)
      
      resolve({
        code: 200,
        msg: '删除成功'
      })
    }, 300)
  })
}

// 生成邀请码
export function generateCode() {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        code: 200,
        msg: '生成成功',
        data: generateRandomCode()
      })
    }, 200)
  })
}

// 学生加入班级
export function joinClass(invitationCode) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      const classList = getLocalData()
      const classInfo = classList.find(item => item.invitationCode === invitationCode)
      
      if (classInfo) {
        // 增加学生数
        classInfo.studentCount = (classInfo.studentCount || 0) + 1
        saveLocalData(classList)
        
        resolve({
          code: 200,
          msg: '加入成功',
          data: classInfo
        })
      } else {
        reject({
          code: 500,
          msg: '邀请码不存在'
        })
      }
    }, 300)
  })
}
