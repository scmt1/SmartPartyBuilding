import request from '@/utils/request'

//热门招聘
export function getRecruitList() {
  return request({
    url: '/recruit/position/sort',
    method: 'get',
    requestBase:'VUE_APP_RECRUIT_API'
  })
}

// 简历详情
export function getInfoByID(query) {
  return request({
    url: 'recruit/resume/:id',
    method: 'get',
    params: query,
    requestBase:'VUE_APP_RECRUIT_API'
  })
}
