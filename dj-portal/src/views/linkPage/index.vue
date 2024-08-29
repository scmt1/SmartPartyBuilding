<template>
    <div>
        <iframe :src="getLinkUrl"
                style="width: 100%; height: calc(100vh - 87px); border: none;"
        />
    </div>
</template>

<script>
  import { getToken } from '@/utils/auth'

  export default {
    // 此处的名称，会在后面配置菜单时用到。
    name: 'LinkPage',
    computed: {
      getLinkUrl() {

        const tempUrl = this.$route.query ? this.$route.query.url : undefined
        console.log("aa")

        if (tempUrl != null) {
          if (tempUrl.indexOf('?') === -1) {
            // 这里appId的值，比如和后台的appCode保持一致。
            return tempUrl + '?appId=ruoyi&token=' + getToken()
          } else {
            console.log(tempUrl + '&appId=ruoyi&token=' + getToken())
            return tempUrl + '&appId=ruoyi&token=' + getToken()
          }
        }
        return tempUrl
      }
    }
  }
</script>
