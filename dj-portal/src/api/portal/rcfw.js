import request from '@/utils/request'
import {getToken} from "@/utils/auth";
import {encrypt} from "@/utils/encrypt";

// 首页 搜索和业务类型数据接口
export function getHomePageData() {
  return request({
    url: '/api/callGZDA/getHomePageData',
    method: 'get',
    requestBase:'VUE_APP_RCFW_API'
  })
}

// 首页 热点数据接口
export function getHomePageHotData() {
  return request({
    url: '/api/callGZDA/getHomePageHotData',
    method: 'get',
    requestBase:'VUE_APP_RCFW_API'
  })
}

// 人才服务数据
export function getTalentData() {
  return request({
    url: '/api/callGZDA/getTalentData',
    method: 'get',
    requestBase:'VUE_APP_RCFW_API'
  })
}

// 人才服务事项保存
export async function SaveSubmitProject(query) {
  // console.log(query)
  // console.log(encrypt(JSON.stringify(query)))
  return request({
    url: '/api/callGZDABySK/SaveSubmitProject',
    method: 'post',
    data: encrypt(JSON.stringify(query)),
    // data: encrypt(JSON.stringify(query)),
    headers: {
      isToken: false,
    },
    requestBase:'VUE_APP_RCFW_API'
  })

}

// 人才服务事项提交
export async function noFlowProjectSubmit(query) {
  return request({
    url: 'api/callGZDA/NoFlowProjectSubmit',
    method: 'post',
    data: query,
    headers: {
      isToken: false,
      'Authorization':'Bearer '+jckToken
    },
    requestBase:'VUE_APP_RCFW_API'
  })
}
export function singleSignOn(query) {
  return request({
    url: '/sys/singleSignOn',
    method: 'get',
    params: query,
    headers: {
      isToken: false
    },
    requestBase:'VUE_APP_RCFW_API'
  })
}

//个人中心获取事项数据
export function getPersonStatistics(query) {
  return request({
    url: '/api/callGZDABySK/getPersonStatistics',
    method: 'post',
    data: encrypt(JSON.stringify({"token": getToken()})),
    requestBase:'VUE_APP_RCFW_API'
  })
}

//个人中心获取事项数据
export function getEnterpriseStatistics(query) {
  return request({
    url: '/api/callGZDABySK/getEnterpriseStatistics',
    method: 'post',
    data: encrypt(JSON.stringify({"token": getToken()})),
    requestBase:'VUE_APP_RCFW_API'
  })
}
