import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter);

export const routes = [
    {
        path: "/honor",
        name: "党内荣誉展示",
        component: () => import("@/views/dj/partyHonor/honorShow.vue"),
},
    {
        path: "/honor/partyHonor/honorShow",
        name: "党内荣誉展示",
        component: () => import("@/views/dj/partyHonor/honorShow.vue"),
    },
    {
        path: "/honor/partyHonor/index",
        name: "党内荣誉管理",
        component: () => import("@/views/dj/partyHonor/index.vue"),
    },
    {
        path: "/honor/partyHonor/passRecord",
        name: "荣誉审核记录",
        component: () => import("@/views/dj/partyHonor/passRecord.vue"),
    },
{
    path: "/honor/partyHonor/honorPass",
        name: "荣誉审核",
    component: () => import("@/views/dj/partyHonor/honorPass.vue"),
}
]

const router = new VueRouter({
    // base: window.__POWERED_BY_QIANKUN__ ? "/dwgl" : "/", //  base路由和注册子应用入口保持一致
    mode: "hash",
    scrollBehavior: () => ({y: 0}),
    routes
})

export default router
