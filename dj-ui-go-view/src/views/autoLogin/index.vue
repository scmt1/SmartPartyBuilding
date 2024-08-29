<template>
    <!-- 登录 -->
    <div class="go-login-box">

    </div>
</template>

<script lang="ts" setup>
import { reactive, ref, onMounted } from 'vue'
import shuffle from 'lodash/shuffle'
import { carouselInterval } from '@/settings/designSetting'
import { useSystemStore } from '@/store/modules/systemStore/systemStore'
import { SystemStoreUserInfoEnum, SystemStoreEnum } from '@/store/modules/systemStore/systemStore.d'
import { PageEnum } from '@/enums/pageEnum'
import { StorageEnum } from '@/enums/storageEnum'
import { icon } from '@/plugins'
import {routerTurnByName, GetQueryString, setLocalStorage} from '@/utils'
import { loginApi } from '@/api/path'

interface FormState {
    username: string
    password: string
}

const { GO_SYSTEM_STORE } = StorageEnum
const { PersonOutlineIcon, LockClosedOutlineIcon } = icon.ionicons5

const formRef = ref()
const loading = ref(false)
const autoLogin = ref(true)
const show = ref(false)
const showBg = ref(false)
const systemStore = useSystemStore()

const t = window['$t']

const formInline = reactive({
    username: 'admin',
    password: 'admin',
})

const rules = {
    username: {
        required: true,
        message: t('global.form_account'),
        trigger: 'blur',
    },
    password: {
        required: true,
        message: t('global.form_password'),
        trigger: 'blur',
    },
}

// 定时器
const shuffleTimiing = ref()

// 轮播图
const carouselImgList = ['one', 'two', 'three']

// 背景图
const bgList = ref([
    'bar_y',
    'bar_x',
    'line_gradient',
    'line',
    'funnel',
    'heatmap',
    'map',
    'pie',
    'radar',
])

// 处理url获取
const getImageUrl = (name: string, folder: string) => {
    return new URL(`../../assets/images/${folder}/${name}.png`, import.meta.url).href
}

// 打乱图片顺序
const shuffleHandle = () => {
    shuffleTimiing.value = setInterval(() => {
        bgList.value = shuffle(bgList.value)
    }, carouselInterval)
}

// 登录
const handleSubmit = async () => {
    const { username, password } = formInline
    loading.value = true
    // 提交请求
    const res = await loginApi({
        username,
        password
    })
    // 获取cookie并且保存为token
    var cookies = document.cookie.split(';');
    var token = '';
    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i].trim();
        if (cookie.startsWith('token=')) {
            token = cookie.substring('token='.length, cookie.length);
            break;
        }
    }
    setLocalStorage("Authorization", token)
    if(res && res.data) {
        setLocalStorage("tenantId", GetQueryString("tenantId"))
        setLocalStorage("djToken", GetQueryString("djToken"))
        setLocalStorage("orgId", GetQueryString("orgId"))
        setLocalStorage("type", GetQueryString("type"))
        const { tokenValue, tokenName } = res.data.token
        const { nickname, username, id } = res.data.userinfo

        // 存储到 pinia
        systemStore.setItem(SystemStoreEnum.USER_INFO, {
            [SystemStoreUserInfoEnum.USER_TOKEN]: tokenValue,
            [SystemStoreUserInfoEnum.TOKEN_NAME]: tokenName,
            [SystemStoreUserInfoEnum.USER_ID]: id,
            [SystemStoreUserInfoEnum.USER_NAME]: username,
            [SystemStoreUserInfoEnum.NICK_NAME]: nickname,
            t
        })

        window['$message'].success(t('login.login_success'))
        routerTurnByName(PageEnum.BASE_HOME_NAME, true)
    }
    loading.value = false
}
onMounted(() => {
    setTimeout(() => {
        show.value = true
    }, 300)

    setTimeout(() => {
        showBg.value = true
    }, 100)

    shuffleHandle()
    handleSubmit()
})
</script>

<style lang="scss" scoped>

</style>
