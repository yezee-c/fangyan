import request from '@/utils/request'

// 查询人物信息列表
export function listCharacter(query) {
  return request({
    url: '/system/character/list',
    method: 'get',
    params: query
  })
}

// 查询人物信息详细
export function getCharacter(characterId) {
  return request({
    url: '/system/character/' + characterId,
    method: 'get'
  })
}

// 新增人物信息
export function addCharacter(data) {
  return request({
    url: '/system/character',
    method: 'post',
    data: data
  })
}

// 修改人物信息
export function updateCharacter(data) {
  return request({
    url: '/system/character',
    method: 'put',
    data: data
  })
}

// 删除人物信息
export function delCharacter(characterId) {
  return request({
    url: '/system/character/' + characterId,
    method: 'delete'
  })
}

// 获取人物关联文件列表
export function getCharacterFiles(characterId) {
  return request({
    url: '/system/character/files/' + characterId,
    method: 'get'
  })
}

// 直接上传文件并关联到人物
export function uploadCharacterFiles(characterId, formData) {
  return request({
    url: '/system/character/upload/' + characterId,
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// 为人物添加文件
export function addCharacterFiles(data) {
  return request({
    url: '/system/character/files',
    method: 'post',
    data: data
  })
}

// 删除人物文件关联
export function delCharacterFile(id) {
  return request({
    url: '/system/character/files/' + id,
    method: 'delete'
  })
}

// 清空人物的所有文件关联
export function clearCharacterFiles(characterId) {
  return request({
    url: '/system/character/files/clear/' + characterId,
    method: 'delete'
  })
}
