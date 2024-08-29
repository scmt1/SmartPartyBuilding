<template>
<!--  人才驿站-->
  <div>
    <div class="page_body">
      <div class="page_main" style="position: absolute;height: calc(100% - 6.5625rem);">
        <iframe style="height:100%;width:100%"  :src="iframeUrl" frameborder="0"></iframe>
      </div>
    </div>
  </div>
</template>
<script>

import { sm4Encrypt,sm4Dncrypt } from "@/utils/encrypt"
import { getToken } from '@/utils/auth'
import ThirdParty from '@/orange-party/index.js';
export default {
  data() {
    return {
      showIframe: true,
      iframeUrl:"",
      activeIndex:"/home"
    }
  },
  mixins: [ThirdParty],
  mounted() {
    // let userInfoStr=this.$cache.local.get("portal_user")
    localStorage.setItem("enterpriseType","yz")
    if(getToken()){
      // let userInfo = JSON.parse(sm4Dncrypt(userInfoStr))
      // this.iframeUrl="https://yizhan.lzjczl.com?token="+sm4Encrypt(getToken())
      this.iframeUrl= process.env.VUE_APP_IFRAME_QNYZ+"?token="+sm4Encrypt(getToken())
    }else{
      // this.iframeUrl="https://yizhan.lzjczl.com"
      this.iframeUrl= process.env.VUE_APP_IFRAME_QNYZ
    }
    console.log(this.iframeUrl)
  },
  methods: {
  }
}
</script>
<style lang="scss" scoped>
.page_body{
  width: 100%;
  height: 100%;

  .page_main{
    width: 100%;
    height: 100%;
  }
}
</style>
