<template>
  <div>
    <div class="portal_header">
      <div class="hed_bg">
        <div class="hed_logo"></div>
        <div class="tologin"  v-show="showLogin">
          <div v-if="isLogin">
            <Row style="height: 100%" align="middle"  >
              <Col :span="20" class="userName" >
                <div @click="toCenter()">
                  <i class="el-icon-s-custom"></i>
                  {{userName}}
                </div>

              </Col>
              <Col :span="3"><el-button type="text" @click="toLogout" >退出</el-button></Col>
            </Row>
          </div>
          <div v-else>
<!--            <el-button  @click="toLogin(1)"  type="text" v-show="loginStatus">注册</el-button>-->
            <el-button  @click="toLogin(2)"  type="text" v-show="loginStatus">登录</el-button>
          </div>
        </div>
      </div>
    </div>
    <el-divider style="color: #FFF7E7"></el-divider>
  </div>

</template>

<script>
  import { getToken } from '@/utils/auth'
  import { sm4Dncrypt } from "@/utils/encrypt"

  export default {
    name: 'portalHeader',
    props: {
      /*showLogin: {
        type: Boolean,
        default: true
      },*/
      loginStatus: {
        type: Boolean,
        default: true
      }

    },

    mounted() {
      this.refreshData()
      this.getShowLogin()

    },

    data() {
      return {
        showLogin:true,
        userName:"",
        isLogin:false,
        isEnterprise:"",
      }
    },
    methods: {
      toCenter(){
        let path=''
        if (this.isEnterprise==1){
          path='/enterpriseCenter'
        }else {
          path='/portalCenter'
        }
        this.$router.push({ path: path });
      },
      getShowLogin(){
        let path=this.$route.path
        // const loginList=['/login','/register',]
        const loginList=[]
        if(loginList.indexOf(path) ==-1){
          this.showLogin=true
        }else {
          this.showLogin=false
        }
      },
      toLogin(index){
        // this.showLogin=false
        if(index==1){
          this.$router.push({ path: '/register' });
        }else if(index==2){
          this.$router.push({ path: '/login' });
        }
      },
      toLogout(){
        this.$confirm('确定注销并退出系统吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$store.dispatch('LogOut').then(() => {
            location.href = '/portal';
          })
        }).catch(() => {});
      },
      refreshData(){
        if(getToken()){
          this.isEnterprise=this.$cache.local.get("isEnterprise")
          this.isLogin=true
          if(this.isEnterprise==1){
            let company=JSON.parse(sm4Dncrypt(this.$cache.local.get("portal_company")))
            this.userName=company.coName
          }else{
            let user=JSON.parse(sm4Dncrypt(this.$cache.local.get("portal_user")))
            this.userName=user.nickName
          }
        }else{
          this.isLogin=false
        }


      }
    }
  }
</script>

<style lang="scss" scoped>

  .el-divider--horizontal {
    margin: 0px 0;
  }
  .portal_header {
    height: 6.125rem;
    width: 100%;
    /*background-color: #236dbd;*/
    background-image: url("~@/assets/images/portal/header/header_bg.png");

    .hed_bg{
      margin: 0 auto;
      width: 75rem;
      height: 100%;


      .hed_logo{
        position: absolute;
        background-image: url("~@/assets/images/portal/header/logo4home.png");
        background-size:100% 100%;
        top:34px;
        height:49px;
        width: 235px;

      }
      .tologin {
        text-align: end;
        position: relative;
        left: 62.5rem;
        top: 2.625rem;
        z-index: 10;
        width: 250px;

        .userName{
          color: #FFFFFF;
          padding-right: 10px
        }

        ::v-deep .el-button--text {
          color: #FFFFFF;
          font-size: 16px;
          padding: 5px 15px;
          border: 1px solid #FFFFFF;
        }
        .el-button:hover {
          background-color: #1c599d;
          border-color: #1c599d;
        }
      }
    }


  }

  .el-divider--horizontal {
    margin: 0;
  }
</style>
