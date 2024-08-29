import request from '@/utils/request'

// 根据文章分类id查询文章列表
export function getListByCategoryId(query) {
  return request({
    url: '/api/article/listByCategoryId',
    method: 'get',
    params: query,
    requestBase:'VUE_APP_CMS_API'
  })
}

// 根据文章分类id分页查询文章列表
export function getPaginate(query) {
  return request({
    url: '/api/article/paginate',
    method: 'get',
    params: query,
    requestBase:'VUE_APP_CMS_API'
  })
}

// 根据文章分类的固定连接查找文章列表
export function getListByCategorySlug(query) {
  return request({
    url: '/api/article/listByCategorySlug',
    method: 'get',
    params: query,
    requestBase:'VUE_APP_CMS_API'
  })
}

// 文章搜索
export function search(query) {
  return request({
    url: '/api/article/search',
    method: 'get',
    params: query,
    requestBase:'VUE_APP_CMS_API'
  })
}
// 通过分类的固定连接文章分页查询
export function getPaginateByCategorySlug(query) {
  return request({
    url: '/api/article/paginateByCategorySlug',
    method: 'get',
    params: query,
    requestBase:'VUE_APP_CMS_API'
  })
}

// 通过文章分类固定连接过滤 文章搜索
export function getSearchByCategorySlug(query) {
  return request({
    url: '/api/article/searchByCategorySlug',
    method: 'get',
    params: query,
    requestBase:'VUE_APP_CMS_API'
  })
}

