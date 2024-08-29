<template>
  <div style="height: 100%;" class="login">
    <div class="login-container">
      <div class="eui-loginBox">
        <div class="eui-loginBd">
          <!-- 账号密码登录 -->
          <div class="eui-smdl zhmmdlBox" :style="{'display': loginType == 1 ? 'none' : 'block'}">
            <div class="eui-smdlTit">
              <img src="../assets/images/portal/login/img1.png">
              <div class="info tishimsg1">
                <p class="blue">温馨提示：</p>
                <p>1、账号注册、登录问题，请咨询0830-6568325；</p>
                <p class="wxts">2、业务办理问题，请咨询
                  <a onclick="goDsftz();" href="http://rsj.luzhou.gov.cn/" target="_blank" class="blue dsftz">
                    “<span class="CALLER_NAME">泸州市人才工作领导小组办公室</span>”
                  </a>管理部门。
                </p>
              </div>
            </div>
            <div class="eui-account">
              <div v-if="isEnterprise()" class="account_card" :style="{backgroundImage: 'url(' + backImage[0] + ')'}" @click="handleLogin(0)">
                <el-image :src="images[0]"/>
                <h1>个人登录</h1>
              </div>
              <div v-else class="account_card" :style="{backgroundImage: 'url(' + backImage[1] + ')'}" @click="handleLogin(1)">
                <el-image :src="images[1]"/>
                <h1>企业登录</h1>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Cookies from 'js-cookie'
  import { decrypt } from '@/utils/jsencrypt'
  import { oneaccessLogin } from  '@/api/login'
  export default {
    components:{
    },
    name: 'Login',
    data() {
      return {
        images:[
          require('@/assets/images/portal/login/person.png'),
          require('@/assets/images/portal/login/enterprise.png'),
        ],
        backImage:[
          require('@/assets/images/portal/login/person_image.png'),
          require('@/assets/images/portal/login/enterprise_image.png'),
        ],
        change: true,
        codeUrl: '',
        loginForm: {
          username: '',
          password: '',
          rememberMe: false,
          code: '',
          uuid: ''
        },
        loginRules: {
          username: [
            { required: true, trigger: 'blur', message: '请输入您的账号' }
          ],
          password: [
            { required: true, trigger: 'blur', message: '请输入您的密码' }
          ],
          code: [{ required: true, trigger: 'change', message: '请输入验证码' }]
        },
        loading: false,
        // 验证码开关
        captchaEnabled: true,
        // 注册开关
        register: false,
        redirect: undefined,
        activeName:'first',
        loginType:0,
        iframeUrl:"",
      }
    },
    watch: {
      $route: {
        handler: function(route) {
          this.redirect = route.query && route.query.redirect
        },
        immediate: true
      }
    },
    created() {
      // this.getCode()
      // this.getCookie()
    },
    methods: {
      isEnterprise(){
        let type = localStorage.getItem("enterpriseType")
        if(type=="yz"){
          return true
        }else{
          return false
        }

      },
      toPath(index){
        if(index.indexOf("/rcgy")>=0||index.indexOf("/rcyz")>=0){
          this.showIframe=true
          this.iframeUrl="http://101.206.141.234:7766"
        }else{
          this.showIframe=false
          this.$router.push({ path: index });
        }

      },
      goto(path){
        this.showIframe=false
        this.$router.push({ path: path });
      },
      //登录方式切换
      loginTypeChange(index){
        this.loginType = index;
      },
      //个人与法人登录切换
      handleClick(tab){
        this.activeName = tab.name;
      },
      // getCode() {
      //   getCodeImg().then(res => {
      //     this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
      //     if (this.captchaEnabled) {
      //       this.codeUrl = 'data:image/gif;base64,' + res.img
      //       this.loginForm.uuid = res.uuid
      //     }
      //   })
      // },
      // getCookie() {
      //   const username = Cookies.get('username')
      //   const password = Cookies.get('password')
      //   const rememberMe = Cookies.get('rememberMe')
      //   this.loginForm = {
      //     username: username === undefined ? this.loginForm.username : username,
      //     password: password === undefined ? this.loginForm.password : decrypt(password),
      //     rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      //   }
      // },
      handleLogin(index) {
        let path=''
        if(1===index){
          this.$cache.local.set("isEnterprise",1)
          path = '/enterpriseCenter'
          oneaccessLogin(path,false,true);
        }else{
          this.$cache.local.set("isEnterprise",0)
          path = '/portalCenter'
          oneaccessLogin(path);
        }

      },
    }
  }
</script>

<style lang="scss" scoped>
  .login {
    /*.postion_menu{
        position: absolute;
        top:0;
        width: 100%;
        height:4.56rem;
        .page_menu{
            margin: 0 auto;
            width: 75rem;
            background-size:100% 100%;
            padding: 23px 0 0 320px;

        }
    }
    .el-menu{
        background-color: transparent;
        text-align: center;
        height: 100%;
        width: 75rem;
        .el-menu-item{
            height: 100%;
            line-height: 4.56rem
        }
    }
    .el-menu--horizontal{
        border-bottom: solid 0px #e6e6e6;
        width: 47.5rem;
        .el-menu-item, .el-submenu{
            height: 100%;
            font-size: 16px;
            margin: 0;
            margin-right:4%;
            padding:0;
            background-color:transparent !important;
            border-bottom: 0px solid transparent;
            font-weight: 700;

            ::v-deep .el-submenu__title {
                font-size: 16px;
                height: 100%;
                line-height: 4.56rem;
                border-bottom: 0px solid transparent;
                padding: 0 0px;
                margin-right:4%;
            }
            ::v-deep .el-submenu__title:hover {
                background-color: transparent;
            }
            ::v-deep .el-submenu__icon-arrow {
                color: #fff;
            }

        }
        .is-active{
            border-bottom: 3px solid #ffffff;
        }
    }
    .top-box {
        width: 100%;
        height: 120px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        padding-left: 360px;
        border-bottom: 8px #1658a0 solid;
    }*/

    .login-container {
        width: 1200px;
        margin: auto;

        .eui-loginBox {
            display: flex;
            min-height: 638.8px;
            box-shadow: 0 0 30px rgb(28 88 157 / 15%);
            margin: 40px 0;
        }
       /* .eui-loginHd {
            width: 300px;
            background: #f9fcff;
            position: relative;
        }
        .eui-loginHd .on {
            background: rgba(31,131,243,0.1);
        }
        .eui-loginHd::before {
            content: "";
            width: 30px;
            height: 100%;
            background: url(./image/login/tabBg.png);
            background-repeat: repeat-y;
            position: absolute;
            right: 0;
            top: 0;
        }
        ul, li {
            list-style: none;
            margin: 0px;
            padding: 0px;
        }
        .eui-loginHd ul li {
            text-align: center;
            height: 160px;
            border-bottom: solid 1px #e3ebf4;
            padding: 25px 0;
            box-sizing: border-box;
            transition: all 0.2;
            cursor: pointer;
            position: relative;
        }
        .eui-loginHd ul li .photo {
            display: flex;
            align-items: center;
            justify-content: center;
            width: 83px;
            height: 83px;
            margin: auto;
        }
        .eui-loginHd ul li p {
            font-size: 18px;
        }*/
        .eui-smdl {
            height: 100%;
            padding: 32px;
            box-sizing: border-box;
            position: relative;
          .eui-account{
            display: flex;
            /*justify-content: space-between;*/
            justify-content: center;
            align-items: center;
            .account_card{
              background-color: #F3F8FE;
              width: 550px;
              height: 423.8px;
              background-size: 100% 100%;
              text-align: center;
              padding-top: 57px;
            }
            .account_card:hover{
              cursor: pointer;
              background-color: #E3EFFE;
            }

          }
        }
        .eui-smdlTit {
            display: flex;
            align-items: center;
            height: auto;
            border-radius: 6px;
            background: #f3f8fe;
            padding: 0 25px;
            margin-bottom: 32px;
        }
        .eui-smdlTit .info {
            flex: 1;
            margin-left: 20px;
            overflow: hidden;
        }
        .blue {
            color: #1f83f3!important;
        }
        .eui-smdlTit .info p {
            font-size: 16px;
            line-height: 30px;
        }
        .eui-loginBd {
            width: 100%;
        }

/*        .eui-accountHd {
            margin-bottom: 30px;
        }
        .eui-personBox {
            width: 443px;
            margin: auto;
        }
        ::v-deep .el-tabs__header {
            width: 260px;
            margin: 20px auto 50px;
        }
        ::v-deep .el-tabs__item {
            font-size: 25px;
        }
        ::v-deep .el-tabs__nav-wrap::after {
             background-color: '' !important;
            height: 0;
        }
        ::v-deep .el-input--medium .el-input__inner {
            height: 45px;
            line-height: 45px;
            font-size: 16px;
        }
        .eui-loginHd ul li.on::before {
            content: "";
            width: 0;
            height: 0;
            border-right: solid 9px #FFFFFF;
            border-top: solid 10px transparent;
            border-bottom: solid 10px transparent;
            position: absolute;
            right: 0;
            top: 50%;
            margin-top: -9px;
        }
        .eui-smdlEwm {
            width: 360px;
            height: auto;
            background: #FFFFFF;
            border: solid 1px #eaf0f5;
            padding: 30px 0;
            box-sizing: border-box;
            margin: 0 auto 20px;
            text-align: center;
            box-shadow: 0 0 32px rgb(22 88 160 / 8%);
        }
        .eui-smdlEwm .name {
            font-size: 20px;
        }
        .eui-smdlEwm .photo {
            width: 197px;
            height: 197px;
            margin: 25px auto;
            position: relative;
        }
        .eui-smdlEwm p {
            font-size: 16px;
            width: 70%;
            margin: auto;
        }
        .eui-smdlTs {
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .eui-smdlTs .list {
            text-align: center;
            margin: 0 44px;
        }
        .eui-smdlTs .list .photo {
            display: flex;
            align-items: center;
            justify-content: center;
            width: 47px;
            height: 47px;
            margin: auto;
            border-radius: 100%;
            border: solid 1px #6a6a6a;
        }
        .eui-smdlTs .list p {
            font-size: 16px;
            color: #666666;
            margin-top: 10px;
        }
        ::v-deep .el-input__icon {
            font-size: 22px;
            position: relative;
            top: 2px
        }*/
    }
  }
  .page_main{
      width: 100%;
      height: 100%;
  }
  .el-menu--popup{
      .el-menu-item{
          color: #000000 !important;
          text-align: center;
      }
      .el-menu-item.is-active {
          color: #1890ff !important;
      }
  }
</style>

