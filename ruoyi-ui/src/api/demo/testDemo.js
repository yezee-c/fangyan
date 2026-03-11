import request from '@/utils/request'

// 查询测试数据列表
export function listTestDemo(query) {
  return request({
    url: '/demo/test/list',
    method: 'get',
    params: query
  })
}

// 查询测试数据详细
export function getTestDemo(id) {
  return request({
    url: '/demo/test/' + id,
    method: 'get'
  })
}

// 新增测试数据
export function addTestDemo(data) {
  return request({
    url: '/demo/test',
    method: 'post',
    data: data
  })
}

// 修改测试数据
export function updateTestDemo(data) {
  return request({
    url: '/demo/test',
    method: 'put',
    data: data
  })
}

// 删除测试数据
export function delTestDemo(id) {
  return request({
    url: '/demo/test/' + id,
    method: 'delete'
  })
}