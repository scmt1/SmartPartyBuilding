import request from '@/utils/request'

// 用户个人信息
export function getAccountInfo(query) {
  return request({
    url: '/scmt/appMain/accountInfo',
    method: 'post',
    params: query,
    requestBase:'VUE_APP_QNYZ_API'
  })
}

// 用户个人信息（通过手机号）
export function getAccountInfoByPhone(query) {
  return request({
    url: '/scmt/appMain/accountInfoByMobile',
    method: 'post',
    params: query,
    requestBase:'VUE_APP_QNYZ_API'
  })
}

// 我的申请
export function queryTYjstbAccountList(query) {
  return request({
    url: '/scmt/appMain/queryTYjstbAccountList',
    method: 'get',
    params: query,
    requestBase:'VUE_APP_QNYZ_API'
  })
}

// 我的入住
export function queryTYjstbAccountList2(query) {
  return request({
    url: '/scmt/appMain/queryTYjstbAccountList',
    method: 'get',
    params: query,
    requestBase:'VUE_APP_QNYZ_API'
  })
}
