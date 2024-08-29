<template>
<!--  人才服务-->
  <div class="content">
<!--    背景图-->
    <div class="img-back">
      <div class="margin-style">
        <span class="text-img">
        人才服务
        </span>
      </div>
    </div>
    <div class="margin-style" style="margin-top: 1.94rem">

      <div class="img-back2" v-for="(item,index) in dataList"
           :style="{marginLeft: index%4===0?'0':'1.7rem' }"
           @click="toView(item.SXMC,item.SXBM)"
      >
        <el-image :src="item.IMAGEURL|getImage" style="cursor: pointer"/>
        <h2  style="cursor: pointer">{{ item.SXMC }}</h2>
      </div>

    </div>

    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose">
      <span>请先申请成为人才</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
  import { getTalentData } from '@/api/portal/rcfw'
  import { encrypt } from "@/utils/encrypt"
  import { getToken } from '@/utils/auth'
  import { oneaccessLogin } from '@/api/login'
  export default {
    name: 'talentService',
    props: {

    },
    filters:{
      getImage(IMAGEURL){
        let Base64 = require('js-base64').Base64
        return process.env.VUE_APP_YSZJ_ADDRESS+'/ufapi/file/getimage?path='+Base64.encode(IMAGEURL)
      }
    },
    mounted(){
      getTalentData().then(res=>{
        if(res.code==0&&res.data){
          this.dataList=res.data
        }
      })
    },
    data(){
      return{
        dataList: [],
        dialogVisible: false,
        isTalent: false
      }
    },
    methods:{
      toView(SXMC,SXBM){
        /*if(!this.isTalent){
          this.dialogVisible=true
          return
        }*/
        if(this.$cache.local.get("isEnterprise")==1&&this.$cache.local.get("portal_company")){
          this.$confirm('您还未登录系统，请先登录系统？', '提示', {
            confirmButtonText: '登录',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {

            // this.$router.push(`/login?redirect=${routeUrl.href}`)
            oneaccessLogin(routeUrl.href)
          }).catch(() => {});
          return
        }

        let str = encrypt(JSON.stringify({SXMC: SXMC,SXBM: SXBM}))
        console.log(encrypt(JSON.stringify({SXMC: SXMC,SXBM: SXBM,CONTENT:"测试",HANDLINGTIME:"",readOnly:true})))
        let routeUrl = this.$router.resolve({
          path: "/talentServiceView",
          query: {fwInfo:str}
        });
        if(getToken()){
          window.location.href=routeUrl.href;
        }else{

          this.$confirm('您还未登录系统，请先登录系统？', '提示', {
            confirmButtonText: '登录',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {

            // this.$router.push(`/login?redirect=${routeUrl.href}`)
            oneaccessLogin(routeUrl.href)
          }).catch(() => {});

        }

        // this.$router.push({path: '/talentServiceView',query:{title: title}})
      },

      handleClose(){

      },

    }


  }
</script>

<style lang="scss" scoped>
.content{
  margin: 0 auto 5rem auto;

  .margin-style{
    width: 75rem;
    margin: 0 auto;
  }
  .img-back{
    width: 100%;
    height: 12.5rem;
    background-image: url("~@/assets/images/portal/talentServices/人才服务背景图.png");
    background-size: 100% 100%;
    min-width: 75rem;
    span{
      position: relative;
      top: 5.0625rem;
      font-size: 38px;
      font-family: "PingFang SC", "PingFang SC-Bold";
      font-weight: 700;
      color: #ffffff;
    }
  }
  .img-back2{
    display: inline-block;
    vertical-align: top;
    width: 17.4118rem;
    height: 14.75rem;
    margin-top: 1.94rem;
    box-shadow: 0.26px 2.99px 6px 0 rgba(197,193,193,0.44);
    h2{
      font-size: 18px;
      font-family: 'PingFang SC', 'PingFang SC-Bold';
      font-weight: 700;
      text-align: left;
      color: #022222;
      padding: 10px 18px;
      margin-top: 15px;
    }

  }

}
</style>
