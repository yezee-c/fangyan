import request from '@/utils/request'

// 查询数字资源列表
export function listDigitalResource(query) {
  return request({
    url: '/pth/digital/resource/list',
    method: 'get',
    params: query
  })
}

// 查询数字资源详细
export function getDigitalResource(id) {
  return request({
    url: '/pth/digital/resource/' + id,
    method: 'get'
  })
}

// 新增数字资源
export function addDigitalResource(data) {
  return request({
    url: '/pth/digital/resource',
    method: 'post',
    data: data
  })
}

// 修改数字资源
export function updateDigitalResource(data) {
  return request({
    url: '/pth/digital/resource',
    method: 'put',
    data: data
  })
}

// 删除数字资源
export function delDigitalResource(id) {
  return request({
    url: '/pth/digital/resource/' + id,
    method: 'delete'
  })
}

// 批量删除数字资源
export function delDigitalResources(ids) {
  return request({
    url: '/pth/digital/resource/' + ids,
    method: 'delete'
  })
}