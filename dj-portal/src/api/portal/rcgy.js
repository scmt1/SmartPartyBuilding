import request from '@/utils/request'

// 获取申请列表信息
export function getRecordList(query) {
  return request({
    url: '/lzrcgy/tGyApplyRecord/pcQueryTGyApplyRecordList',
    method: 'get',
    params: query,
    headers: {
      isToken: false
    },
    requestBase:'VUE_APP_RCGY_API'
  })
}

// 获取申请列表信息
export function getRecordDetail(query) {
  return request({
    url: '/lzrcgy/tGyApplyRecord/getTGyApplyRecord',
    method: 'get',
    params: query,
    headers: {
      isToken: false
    },
    requestBase:'VUE_APP_RCGY_API'
  })
}
