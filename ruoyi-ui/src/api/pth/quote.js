import request from '@/utils/request'

// 查询大别山名人名言列表
export function listQuote(query) {
  return request({
    url: '/pth/quote/list',
    method: 'get',
    params: query
  })
}

// 查询前台展示的名人名言列表
export function listDisplayQuotes() {
  return request({
    url: '/pth/quote/display',
    method: 'get'
  })
}

// 查询大别山名人名言详细
export function getQuote(quoteId) {
  return request({
    url: '/pth/quote/' + quoteId,
    method: 'get'
  })
}

// 新增大别山名人名言
export function addQuote(data) {
  return request({
    url: '/pth/quote',
    method: 'post',
    data: data
  })
}

// 修改大别山名人名言
export function updateQuote(data) {
  return request({
    url: '/pth/quote',
    method: 'put',
    data: data
  })
}

// 删除大别山名人名言
export function delQuote(quoteId) {
  return request({
    url: '/pth/quote/' + quoteId,
    method: 'delete'
  })
}
