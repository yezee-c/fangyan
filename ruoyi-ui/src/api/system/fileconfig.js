import request from '@/utils/request'

// 查询用户文件配置列表
export function listFileConfig(query) {
  return request({
    url: '/system/fileconfig/list',
    method: 'get',
    params: query
  })
}

// 查询用户文件配置详细
export function getFileConfig(userId) {
  return request({
    url: '/system/fileconfig/' + userId,
    method: 'get'
  })
}

// 新增用户文件配置
export function addFileConfig(data) {
  return request({
    url: '/system/fileconfig',
    method: 'post',
    data: data
  })
}

// 修改用户文件配置
export function updateFileConfig(data) {
  return request({
    url: '/system/fileconfig',
    method: 'put',
    data: data
  })
}

// 删除用户文件配置
export function delFileConfig(userId) {
  return request({
    url: '/system/fileconfig/' + userId,
    method: 'delete'
  })
}
