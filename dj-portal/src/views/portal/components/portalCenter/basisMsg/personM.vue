<template>
  <div style="height: 100%;width: 100%">
    <div class="person_title">个人信息管理</div>
    <div class="person_content">
      <div class="person_left">
        <div class="person_card">
          <div class="person_name">个人姓名</div>
          <div class="person_msg">{{ this.homeData.realName }}</div>
        </div>
        <div class="person_card">
          <div class="person_name">证件类型</div>
          <div class="person_msg">身份证</div>
        </div>
        <div class="person_card">
          <div class="person_name">性别</div>
          <div class="person_msg">{{ sexName[this.homeData.sex] }}</div>
        </div>
        <div class="person_card">
          <div class="person_name">民族</div>
          <div class="person_msg">汉族</div>
        </div>
      </div>
      <div class="person_right">
        <div class="person_card">
          <div class="person_name">手机号码</div>
          <div class="person_msg">{{ this.homeData.phone }}</div>
        </div>
        <div class="person_card">
          <div class="person_name">证件号码</div>
          <div class="person_msg">{{ this.homeData.idCard }}</div>
        </div>
        <div class="person_card">
          <div class="person_name">出生日期</div>
          <div class="person_msg">{{ this.homeData.birthday }}</div>
        </div>
      </div>
    </div>
    <div class="person_title">账号安全管理</div>
    <div class="account">
      <div class="person_card">
        <div class="person_name">登录密码</div>
        <el-input
          v-model="input"
          :disabled="true"
          :show-password="true"
          class="person_psw">
        </el-input>
        <!--              <div class="person_msg" type="password">12345678</div>-->
        <div class="person_change">修改登录密码</div>
      </div>
    </div>
  </div>

</template>

<script>
  import {sm4Dncrypt} from "@/utils/encrypt";
  import cache from "@/plugins/cache";
  export default {
    name: "personM",
    data() {
      return {
        input: '1234',
        homeData: {},
        sexName: ['女','男'],
      }
    },
    mounted() {
      this.init()

    },
    methods:{
      init(){
        this.getUser()
      },
      getUser(){
        //获取用户信息
        this.homeData=JSON.parse(sm4Dncrypt(cache.local.get("portal_user")))
      },
    }
  }
</script>


<style scoped>
  * {
    margin: 0;
    padding: 0;
  }


  /*个人信息管理*/
  .person_title {
    width: 96%;
    height: 2.1875rem;
    margin-left: 1.25rem;
    font-size: 16px;
    font-family: PingFang SC, PingFang SC-Bold;
    font-weight: 700;
    text-align: left;
    color: #333333;
    border-bottom: 2px solid #333333;;
  }
  .person_content {
    width: 100%;
    height: 100%;
    display: flex;
    margin-bottom: 1.875rem;
    /*border: 1px solid black;*/
  }
  .person_left {
    width: 48%;
    height: 100%;
    /*margin-left: 50px;*/
    /*border: 1px solid black;*/
  }
  .person_right {
    width: 45%;
    height: 100%;
    margin-left: 2.1875rem;
    /*border: 1px solid black;*/
  }
  .person_card {
    width: 100%;
    height: 3.125rem;
    display: flex;
    margin-top: 1.25rem;
    /*border: 1px solid black;*/
  }
  .person_name {
    width: 7.5rem;
    height: 2.875rem;
    /*border: 1px solid black;*/
    font-size: 14px;
    font-family: PingFang SC, PingFang SC-Medium;
    font-weight: 500;
    text-align: right;
    color: #333333;
    line-height: 2.875rem;
    /*margin-right: 20px;*/
  }
  .person_msg {
    width: 16.25rem;
    height: 2.875rem;
    background: #ffffff;
    border: 1px solid #e9eaed;
    font-size: 14px;
    font-family: PingFang SC, PingFang SC-Medium;
    font-weight: 500;
    text-align: left;
    color: #333333;
    line-height: 2.875rem;
    padding-left: 1.25rem;
    margin-left: 1.25rem;
  }

  /*账号安全管理*/
  .account {
    width: 34.375rem;
    height: 5rem;
    /*border: 1px solid black;*/
    margin-left: 50px;
  }
  .person_psw {
    margin-left: 1.25rem;
  }
  .person_psw >>> .el-input__inner{
    width: 20.25rem;
    height: 2.875rem;
    background: #ffffff;
    border: 1px solid #e9eaed;
    border-radius: 0;

  }
  .person_change {
    width: 11.25rem;
    height: 2.875rem;
    background: #0256ca;
    font-size: 14px;
    font-family: PingFang SC, PingFang SC-Bold;
    font-weight: 700;
    text-align: center;
    line-height: 2.875rem;
    color: #ffffff;
    cursor: pointer;
  }

</style>
