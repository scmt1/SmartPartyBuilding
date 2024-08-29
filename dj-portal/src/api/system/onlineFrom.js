import request from '@/utils/request'

// 查询菜单列表
export function getOnlineFromData(query) {
  return request({
    url: '/admin/online/onlineForm/render',
    method: 'get',
    params: query
  })
}
