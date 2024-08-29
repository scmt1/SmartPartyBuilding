// 获取企业信息
import request from '@/utils/request'
import {getToken} from "@/utils/auth";

//提交员工绑定企业申请
export function saveEmployee(data) {
  return request({
    url: '/gov-company/companyemployee/save',
    method: 'POST',
    /*headers: {
      isToken: false,
      'Authorization':'Bearer '+token
    },*/
    data:data,
    requestBase:'VUE_APP_ONEACCESS_API'
  })
}

//获取当前员工绑定企业
export function getEmployeeCompany() {
  return request({
    url: '/gov-company/companyemployee/getEmployeeCompany',
    method: 'GET',
    requestBase:'VUE_APP_ONEACCESS_API'
  })
}

//获取当前企业绑定员工
export function getCompanyEmployee() {
  return request({
    url: '/gov-company/companyemployee/getCompanyEmployee',
    method: 'GET',
    requestBase:'VUE_APP_ONEACCESS_API'
  })
}

//员工申请审核
export function auditEmployee(data) {
  return request({
    url: '/gov-company/companyemployee/auditEmployee',
    method: 'POST',
    data: data,
    requestBase:'VUE_APP_ONEACCESS_API'
  })
}

//企业解绑员工
export function unbindEmployee(data) {
  return request({
    url: '/gov-company/companyemployee/unbindEmployee',
    method: 'POST',
    params: data,
    requestBase:'VUE_APP_ONEACCESS_API'
  })
}

//企业解绑员工
export function getEmployeeBindCompany() {
  return request({
    url: '/gov-company/companyemployee/getEmployeeBindCompany',
    method: 'GET',
    requestBase:'VUE_APP_ONEACCESS_API'
  })
}

//企业模糊查询
export function queryCoByName(query) {
  return request({
    url: '/gov-company/company/queryCoByName',
    method: 'GET',
    params:query,
    requestBase:'VUE_APP_ONEACCESS_API'
  })
}

//企业信息查询
export function queryCoInfo(query) {
  return request({
    url: '/gov-company/company/detail',
    method: 'GET',
    Authorization: getToken(),
    // headers:{
    //   // token: "Bearer " + getToken(),
    // },
    params:query,
    requestBase:'VUE_APP_ONEACCESS_API'
  })
}


