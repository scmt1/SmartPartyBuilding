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
                    “<span class="CALLER_NAME">泸州市人力资源和社会保障局</span>”
                  </a>管理部门。
                </p>
                <p>3、个人用户无需注册，直接手机验证码登录后自动注册。</p>

              </div>
            </div>
            <div class="eui-account">
              <div class="account_card" :style="{backgroundImage: 'url(' + backImage[0] + ')'}" @click="handleLogin(0)">
                <el-image :src="images[0]"/>
                <h1>个人注册</h1>
              </div>
              <div class="account_card" :style="{backgroundImage: 'url(' + backImage[1] + ')'}" @click="handleLogin(1)">
                <el-image :src="images[1]"/>
                <h1>企业注册</h1>
              </div>

            </div>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
  import { oneaccessLogin } from  '@/api/login'
  export default {
    components: {},
    name: 'Login',
    data() {
      return {
        images: [
          require('@/assets/images/portal/login/person.png'),
          require('@/assets/images/portal/login/enterprise.png')
        ],
        backImage: [
          require('@/assets/images/portal/login/person_image.png'),
          require('@/assets/images/portal/login/enterprise_image.png')
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
        redirect: undefined,
        activeName: 'first',
        loginType: 0,
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
    },
    methods: {
      handleLogin(index) {
        let path=''
        if(1===index){
          this.$cache.local.set("isEnterprise",1)
          path = '/enterpriseCenter'
        }else{
          this.$cache.local.set("isEnterprise",0)
          path = '/portalCenter'
        }
        oneaccessLogin(path);
      },
    }
  }
</script>

<style lang="scss" scoped>
  .login {
    .login-container {
      width: 75rem;
      margin: auto;

      .eui-loginBox {
        display: flex;
        min-height: 45.875rem;
        box-shadow: 0 0 1.875rem rgb(28 88 157 / 15%);
        margin: 2.5rem 0;
      }

      .eui-loginHd {
        width: 18.75rem;
        background: #f9fcff;
        position: relative;
      }

      .eui-loginHd .on {
        background: rgba(31, 131, 243, 0.1);
      }

      .eui-loginHd::before {
        content: "";
        width: 1.875rem;
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
        height: 10rem;
        border-bottom: solid 1px #e3ebf4;
        padding: 1.5625rem 0;
        box-sizing: border-box;
        transition: all 0.2;
        cursor: pointer;
        position: relative;
      }

      .eui-loginHd ul li .photo {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 5.1875rem;
        height: 5.1875rem;
        margin: auto;
      }

      .eui-loginHd ul li p {
        font-size: 1.125rem;
      }

      .eui-smdl {
        height: 100%;
        padding: 1.125rem 2.125rem;
        box-sizing: border-box;
        position: relative;

        .eui-account {
          display: flex;
          justify-content: space-between;
          align-items: center;

          .account_card {
            background-color: #F3F8FE;
            width: 34.375rem;
            /*width: 100%;*/
            height: 26.4875rem;
            background-size: 100% 100%;
            text-align: center;
            padding-top: 3.5625rem;
          }

          .account_card:hover {
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
        padding: 0 1.5625rem;
        margin-bottom: 4.5625rem;
      }

      .eui-smdlTit .info {
        flex: 1;
        margin-left: 1.25rem;
        overflow: hidden;
      }

      .blue {
        color: #1f83f3 !important;
      }

      .eui-smdlTit .info p {
        font-size: 16px;
        line-height: 1.875rem;
      }

      .eui-loginBd {
        width: 100%;
      }

      .eui-accountHd {
        margin-bottom: 1.875rem;
      }

      .eui-personBox {
        width: 27.6875rem;
        margin: auto;
      }

      ::v-deep .el-tabs__header {
        width: 16.25rem;
        margin: 1.25rem auto 3.125rem;
      }

      ::v-deep .el-tabs__item {
        font-size: 25px;
      }

      ::v-deep .el-tabs__nav-wrap::after {
        background-color: '' !important;
        height: 0;
      }

      ::v-deep .el-input--medium .el-input__inner {
        height: 2.8125rem;
        line-height: 2.8125rem;
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
        width: 22.5rem;
        height: auto;
        background: #FFFFFF;
        border: solid 1px #eaf0f5;
        padding: 1.875rem 0;
        box-sizing: border-box;
        margin: 0 auto 1.25rem;
        text-align: center;
        box-shadow: 0 0 2rem rgb(22 88 160 / 8%);
      }

      .eui-smdlEwm .name {
        font-size: 20px;
      }

      .eui-smdlEwm .photo {
        width: 12.3125rem;
        height: 12.3125rem;
        margin: 1.5625rem auto;
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
        margin: 0 2.75rem;
      }

      .eui-smdlTs .list .photo {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 2.9375rem;
        height: 2.9375rem;
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
      }
    }
  }
</style>

