import router from './router'
import store from './store'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'
import { isRelogin } from '@/utils/request'
import cache from '@/plugins/cache'
import { Message } from 'element-ui'

NProgress.configure({ showSpinner: false })

// const whiteList = ['/login', '/auth-redirect', '/bind', '/register' ,'/indexPanel','/portal']
// 需要登录的页面
const loginList = ['/portalCenter','/personalData','/invitation','/matter','/resume',]
// const loginList = []

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    to.meta.title && store.dispatch('settings/setTitle', to.meta.title)
    /* has token*/

    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    }else {
      if(cache.local.get("isEnterprise")&&cache.local.get("isEnterprise")==1){
        console.log("isEnterprise")
        store.dispatch('GetCompanyInfo').then(() => {
          next()
        }).catch(err => {
          store.dispatch('LogOut').then(() => {
            console.log(err)
            Message.error(err)
            next({ path: '/' })
          })
        })
      }
      else if (store.getters.roles.length === 0&&store.getters.userType!="user") {
        isRelogin.show = true
        // 判断当前用户是否已拉取完user_info信息
        store.dispatch('GetInfo').then(() => {
          isRelogin.show = false
          if(store.getters.userType=="manager"){
            console.log("store.getters.roles",store.getters.roles)
            store.dispatch('GenerateRoutes').then(accessRoutes => {
              // 根据roles权限生成可访问的路由表
              router.addRoutes(accessRoutes) // 动态添加可访问路由表
              next({ ...to, replace: true }) // hack方法 确保addRoutes已完成

            })
          }else{
            next()
          }

        }).catch(err => {
          store.dispatch('LogOut').then(() => {
            console.log(err)
            Message.error(err)
            next({ path: '/' })
          })
        })
      } else {
        next()
      }
      // next()
    }
  } else {
    // 没有token
    /*if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else {
      next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
      NProgress.done()
    }*/
    if (loginList.indexOf(to.path) == -1) {
      // 不需要登录
      next()
    } else {
      // 需要登录
      next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
