import Vue from 'vue'
import Vuex from 'vuex'
import count from './count';
import dept from './dept';
Vue.use(Vuex)
export default new Vuex.Store({
  state: {},
  mutations: {},
  actions: {},
  getters: {},
  modules: {
    count,
    dept
  },
});
