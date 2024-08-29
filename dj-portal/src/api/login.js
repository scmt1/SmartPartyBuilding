import request from '@/utils/request'
import cache from '@/plugins/cache'
// 登录方法
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid
  }
  return request({
    url: '/login',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 注册方法
export function register(data) {
  return request({
    url: '/register',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/getInfo',
    method: 'get'
  })
}
// 获取用户类型
export function getUserType(data) {
  return request({
    url: '/getUserType',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}


// 获取用户详细信息
export function getOneAccessInfo() {
  return request({
    url: '/one-access/userAuthInfo/baseInfo',
    method: 'get',
    requestBase:'VUE_APP_ONEACCESS_API'
  })
}
// 获取用户详细信息
export function getPhoneNum() {
  return request({
    url: '/one-access/userAuthInfo/privateInfo/phoneNum',
    method: 'get',
    requestBase:'VUE_APP_ONEACCESS_API'
  })
}
// 获取用户隐私信息
export function getIdNum() {
  return request({
    url: '/one-access/userAuthInfo/privateInfo/idNum',
    method: 'get',
    requestBase:'VUE_APP_ONEACCESS_API'
  })
}

// 获取企业信息
export function getCurLoginCompany() {
  return request({
    url: '/one-access/userAuthInfo/getCurLoginCompany',
    method: 'get',
    requestBase:'VUE_APP_ONEACCESS_API'
  })
}


// 退出方法
export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

/**
 * 跳转到认证中心退出登录页面
 */
export function toLogout(){
  let url = process.env.VUE_APP_ONEACCESS_LOGOUT_URL
  let appZoneId=process.env.VUE_APP_ZONE_ID
  url = url + "?app_zone_id=" + appZoneId+"&logout_back_uri="+encodeURIComponent(window.location.protocol+"//"+window.location.host)
  console.log(url)
  //跳转1
  window.location.href = url

}


// 获取验证码
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}


// 统一认证登录
export function oneaccessLogin(path,isOpenNew,forceCompany) {
  var curUrl = window.location.protocol+"//"+window.location.host
  // 代理认证接口地址
  let ssoLoginUri = (encodeURIComponent(encodeURIComponent(curUrl+'/one-access/sso/ssoLogin')))

  curUrl = curUrl+path
  // 回调地址
  let redirectCallback = encodeURIComponent(encodeURIComponent(encodeURIComponent(encodeURIComponent(curUrl))))

  // VUE_APP_ONEACCESS_URL='https://gate-pass-test.lzjczl.com/#/sso/ssoLogin'
  // VUE_APP_ZONE_ID='rcyszj_portal_test'
  // VUE_APP_ONEACCESS_LOGOUT_URL='https://gate-pass-test.lzjczl.com/#/logout'

  // 统一认证地址
  let url = process.env.VUE_APP_ONEACCESS_URL
  // 应用appid
  let appZoneId=process.env.VUE_APP_ZONE_ID
  url = url + "?app_zone_id=" + appZoneId + "&redirectCallback=" + redirectCallback+"&ssoLoginUri="+ssoLoginUri
  if(forceCompany){
    url=url+"&forceCompany=true&forceOrg=true"
  }else{
    url=url+"&forceCompany=false"
  }
  console.log(url)
  if(isOpenNew){
    window.open(url, '_blank');
  }else{
    window.location.href = url
  }

}




