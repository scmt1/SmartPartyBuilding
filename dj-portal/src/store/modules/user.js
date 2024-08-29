import {
  login, logout, getInfo, toLogout
  , getUserType, getOneAccessInfo, getCurLoginCompany
} from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { getEmployeeBindCompany } from '@/api/portal/employee'
import cache from '@/plugins/cache'
import { sm4Encrypt } from '@/utils/encrypt'
import { convertColumnOrder } from 'view-design/src/components/table/util'

const user = {
  state: {
    token: getToken(),
    name: '',
    userType: '',
    avatar: '',
    roles: [],
    permissions: [],
    isEnterprise: 0
  },

  mutations: {
    SET_IS_ENTERPRISE: (state, isEnterprise) => {
      state.isEnterprise = isEnterprise
    },
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_USER_TYPE: (state, userType) => {
      state.userType = userType
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    }
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim()
      const password = userInfo.password
      const code = userInfo.code
      const uuid = userInfo.uuid
      return new Promise((resolve, reject) => {
        login(username, password, code, uuid).then(res => {
          setToken(res.token)
          commit('SET_TOKEN', res.token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取企业信息
    GetCompanyInfo({ commit }) {
      return new Promise((resolve, reject) => {
        getCurLoginCompany().then(res => {
          console.log(res)
          if (res.code == 200 && res.data) {
            cache.local.set('portal_company', sm4Encrypt(JSON.stringify(res.data)))
          }
          resolve(res)
        })

      })
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getUserType({ 'accessToken': getToken() }).then(async res => {

          commit('SET_USER_TYPE', 'user')
          await getUserInfo_oneaccess({ commit, state })

          await getEmployeeBindCompany().then(res => {
            if(res.code==200&&Object.keys(res.data).length>0){
              cache.local.set('portal_employee', sm4Encrypt(JSON.stringify(res.data)))
            }

          })
          if (res.code == 200 && res.userType == 'manager') {
            commit('SET_USER_TYPE', res.userType)
            getManageInfo({ commit, state }).then()
          }
          resolve(res)

        }).catch(error => {
          reject(error)
        })

      })
    },

    // 退出系统
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          commit('SET_IS_ENTERPRISE', 0)
          removeToken()
          localStorage.clear()
          toLogout()
          // delAllCookie()
          // resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    }

  }
}

function getUserInfo_oneaccess({ commit, state }) {
  return new Promise((resolve, reject) => {
    getOneAccessInfo().then(res => {
      const user = res.data
      const avatar = (user.avatar == '' || user.avatar == null) ? require('@/assets/images/profile.jpg') : user.avatar
      if (res.roles && res.roles.length > 0) { // 验证返回的roles是否是一个非空数组
        commit('SET_ROLES', res.roles)
        commit('SET_PERMISSIONS', res.permissions)
      } else {
        commit('SET_ROLES', ['ROLE_DEFAULT'])
      }
      commit('SET_NAME', user.nickName)
      commit('SET_AVATAR', avatar)
      cache.local.set('portal_user', sm4Encrypt(JSON.stringify(res.data)))
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

function getManageInfo({ commit, state }) {
  return new Promise((resolve, reject) => {
    getInfo().then(res => {
      const user = res.user
      const avatar = (user.avatar == '' || user.avatar == null) ? require('@/assets/images/profile.jpg') : process.env.VUE_APP_BASE_API + user.avatar

      if (res.roles && res.roles.length > 0) { // 验证返回的roles是否是一个非空数组
        commit('SET_ROLES', res.roles)
        commit('SET_PERMISSIONS', res.permissions)
      } else {
        commit('SET_ROLES', ['ROLE_DEFAULT'])
      }
      commit('SET_NAME', user.userName)
      commit('SET_AVATAR', avatar)

      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })

}

export default user
