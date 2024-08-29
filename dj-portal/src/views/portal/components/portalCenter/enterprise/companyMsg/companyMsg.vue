<template>
  <div class="acMsg">
    <div class="title">企业信息管理</div>
    <div class="container">
      <el-tabs :tab-position="tabPosition" style="height: 100%;">
        <el-tab-pane label="企业注册信息">
          <div class="person_title">企业注册信息</div>
          <div class="person_content">
            <div class="person_left">
              <div class="person_card">
                <div class="person_name">企业名称</div>
                <div class="person_msg">{{ coInfo.coName }}</div>
              </div>
              <div class="person_card">
                <div class="person_name">统一社会信用代码</div>
                <div class="person_msg">{{ coInfo.coCredit }}</div>
              </div>
<!--              <div class="person_card">-->
<!--                <div class="person_name">证件号码</div>-->
<!--                <div class="person_msg">511527199801260013</div>-->
<!--              </div>-->
              <div class="person_card">
                <div class="person_name">企业地址</div>
                <div class="person_msg">{{ coInfo.coRegion+coInfo.coAddress }}</div>
              </div>
              <div class="person_card">
                <div class="person_name">行业领域</div>
                <div class="person_msg" :title="coInfo.coFullIndustryText">{{ coInfo.coFullIndustryText }}</div>
              </div>
              <div class="person_card">
                <div class="person_name">企业规模</div>
                <div class="person_msg">{{ coInfo.coScale }}</div>
              </div>
            </div>
            <div class="person_right">
              <div class="person_card_logo">
                <div class="person_name">企业logo</div>
                <div class="person_logo_img">
                  <img :src="coInfo.coLogo" alt="" class="enterpriseLogo">
                </div>
              </div>
              <div class="person_card_logo">
                <div class="person_name">企业营业执照</div>
                <div class="person_logo_img">
                  <img :src="coInfo.coLicence" alt="" class="enterpriseLogo">
                </div>
              </div>
              <div class="person_card">
                <div class="person_name">企业性质</div>
                <div class="person_msg">{{ coTypeName[coInfo.coType] }}</div>
              </div>
<!--              <div class="person_card">-->
<!--                <div class="person_name">所属二级单位</div>-->
<!--                <div class="person_msg">泸州市国有资产监督管理委员会</div>-->
<!--              </div>-->
            </div>
          </div>
          <div class="person_bottom">
            <div class="person_card_introduce">
              <div class="person_name">企业简介</div>
              <div class="person_msg_introduce">{{coInfo.coIntroduce}}</div>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="账号信息管理">
          <div class="person_title">联系人信息管理</div>
          <div class="person_content">
            <div class="person_left">
              <div class="person_card">
                <div class="person_name">个人姓名</div>
                <div class="person_msg">刘智愚</div>
              </div>
              <div class="person_card">
                <div class="person_name">电子邮箱</div>
                <div class="person_msg">1654754253@qq.com</div>
              </div>
            </div>
            <div class="person_right">
              <div class="person_card">
                <div class="person_name">手机号码</div>
                <div class="person_msg">18880912538</div>
              </div>
            </div>
          </div>
          <div class="account_bottom">
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

        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import {queryCoInfo} from "@/api/portal/employee";
import {getToken} from "@/utils/auth";


export default {
  name: "accountMsg",
  data() {
    return {
      tabPosition: 'left',
      input: '1234',
      flats: [{
        value: '选项1',
        label: '二级单位1'
      }, {
        value: '选项2',
        label: '二级单位1'
      }, {
        value: '选项3',
        label: '二级单位3'
      }],
      job: [{
        value: '选项1',
        label: '黄金糕'
      }, {
        value: '选项2',
        label: '双皮奶'
      }, {
        value: '选项3',
        label: '蚵仔煎'
      }],
      company: [{
        value: '选项1',
        label: '黄金糕'
      }, {
        value: '选项2',
        label: '双皮奶'
      }, {
        value: '选项3',
        label: '蚵仔煎'
      }],
      value1: '',
      value2: '',
      value3: '',
      coInfo: {

      },
      coTypeName: ['','有限责任公司','股份有限公司','集体所有制','股份合作制','国有企业','个体工商户','个人独资企业','有限合伙','普通合伙','外商投资企业','港、澳、台','联营企业','私营企业'],
    };
  },
  mounted() {
    this.init()
  },
  methods:{
    init(){
      this.getData()
    },
    getData(){
      /*queryCoInfo({id : '1627610894397812738'}).then(res=>{
        if (res.success){
          this.coInfo=res.data
        }
      })*/

    }
  }
}

</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
}
.acMsg {
  width: 100%;
  height: 100%;
  /*flex: 1;*/
  background: #ffffff;
}
.title {
  width: 12.5rem;
  height: 4.375rem;
  font-size: 1.625rem;
  font-family: PingFang SC, PingFang SC-Bold;
  font-weight: 700;
  text-align: center;
  color: #333333;
  line-height: 4.375rem;
  /*border: 1px solid black;*/
}
.container {
  width: 98%;
  /*margin: auto;*/
  /*border: 1px solid black;*/
}
/*tabs样式调整*/
.container >>> .el-tabs__item {
  width: 14.875rem;
  height: 3.125rem;
  text-align: left;
  /*background: #e4f2fe;*/
  padding-top: 2%;
  font-size: 14px;
  font-family: PingFang SC, PingFang SC-Bold;
  font-weight: 700;
}
.container >>> .el-tabs__item.is-active {
  background: #e4f2fe;
  color: #0256ca;
  border-left: 2px solid #0256CA;
}
.container >>> .el-tabs__active-bar {
  display: none;
}
.container >>> .el-tabs__nav-wrap::after {
  display: none;
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
  /*border: 1px solid black;*/
}
.person_left {
  width: 25rem;
  height: 100%;
  /*margin-left: 10px;*/
  /*border: 1px solid black;*/
}
.person_right {
  width: 25rem;
  height: 100%;
  margin-left: 2.1875rem;
  /*border: 1px solid black;*/
}
.person_bottom {
  width: 100%;
  height: 100%;
  /*margin-left: 35px;*/
  /*display: block !important;*/
  margin-bottom: 3.125rem;
  /*border: 1px solid black;*/
}
.person_card {
  width: 100%;
  height: 3.125rem;
  display: flex;
  margin-top: 1.25rem;
  /*border: 1px solid black;*/
}
.person_card_logo {
  width: 100%;
  height: 7.5rem;
  display: flex;
  margin-top: 1.25rem;
  /*border: 1px solid black;*/
}
.person_card_introduce {
  width: 100%;
  height: 100%;
  display: flex;
  margin-top: 1.875rem;
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

  text-overflow:ellipsis;
  white-space:nowrap;
  overflow:hidden;
}
.person_msg_introduce {
  width: 43.75rem;
  height: 20.875rem;
  background: #ffffff;
  border: 1px solid #e9eaed;
  font-size: 14px;
  font-family: PingFang SC, PingFang SC-Medium;
  font-weight: 500;
  text-align: left;
  color: #333333;
  line-height: 1.5625rem;
  padding-left: 1.25rem;
  padding-top: 0.75rem;
  padding-right: 1.0625rem;
  margin-left: 1.25rem;
}
.person_logo_img {
  width: 16.25rem;
  height: 6.9375rem;
  background: #ffffff;
  border: 1px solid #e9eaed;
  text-align: center;
  padding-left: 1.25rem;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-left: 1.25rem;
}
.account_bottom {
  margin-top: 6.25rem;
}
.person_zz_img {
  width: 20.125rem;
  height: 111px;
  background: #ffffff;
  border: 1px solid #e9eaed;
  text-align: center;
  padding-left: 20px;
}
.enterpriseLogo {
  max-width: 200px;
  max-height: 100px;
  /*width: 100%;*/
  /*height: 100%;*/
  /*background-repeat: no-repeat;*/
  /*background-size: 100% 100%;*/
}

/*账号安全管理*/
.account {
  width: 34.375rem;
  height: 5rem;
  /*border: 1px solid black;*/
  margin-left: 3.125rem;
  margin-bottom: 3.125rem;
}
.person_psw {
  margin-left: 20px;
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
}

/*企业绑定管理*/
.company_card {
  width: 100%;
  height: 3.125rem;
  display: flex;
  margin-top: 1.25rem;
  /*border: 1px solid black;*/
}
.company_name {
  width: 120px;
  height: 46px;
  /*border: 1px solid black;*/
  font-size: 14px;
  font-family: PingFang SC, PingFang SC-Medium;
  font-weight: 500;
  text-align: left;
  color: #333333;
  line-height: 46px;
}
.company_select >>> .el-input__inner{
  width: 17.5rem;
  height: 2.875rem;
  background: #ffffff;
  border: 1px solid #e9eaed;
  border-radius: 0;
}
</style>
