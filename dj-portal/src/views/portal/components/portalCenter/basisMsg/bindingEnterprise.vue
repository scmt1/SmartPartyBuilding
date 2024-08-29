<template>
  <div>
    <div class="person_title">企业绑定管理(若为智慧党建云平台已注册企业在职员工，选择所属企业进行绑定即可)</div>
    <div v-loading="this.company.status===0"
         element-loading-text="正在审批，请等待结果。。。。"
         element-loading-spinner="el-icon-loading"
         element-loading-background="rgb(120 117 117 / 20%)">
      <div class="person_content">
        <div class="person_left">
          <div class="company_card">
            <div class="company_name">职位</div>
            <el-input v-model="company.job" placeholder="请输入内容" class="company_select" :disabled="!isEdit"></el-input>
          </div>
        </div>
        <div class="person_right">
          <div class="company_card">
            <div class="company_name">所属企业</div>
            <el-select
              class="company_select"
              v-model="company.companyId"
              filterable
              remote
              reserve-keyword
              placeholder="请输入关键词"
              :remote-method="remoteMethod"
              :loading="loading"
              :disabled="!isEdit">
              <el-option
                class="company_select"
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </div>
        </div>
      </div>

      <div style="text-align: center">
        <el-alert v-if="this.company.status===1&&!isEdit" class="alert-style" title="审批已通过！！" type="success" :closable="false"/>
        <el-button v-if="this.company.status===1&&!isEdit" class="company_button" @click="unbind">解除绑定</el-button>

        <el-alert  v-if="this.company.status===-1&&!isEdit" class="alert-style" title="审批未通过，请重新提交！！" type="error" :closable="false"/>
        <el-button v-if="this.company.status===-1&&!isEdit" class="company_button" @click="isEdit=true">修改</el-button>

        <el-button v-if="isEdit" class="company_button" @click="saveEmployee">提交绑定申请</el-button>
      </div>

    </div>

  </div>
</template>

<script>
import {getEmployeeCompany, saveEmployee, queryCoByName, unbindEmployee} from "@/api/portal/employee"
  import { Message } from 'element-ui'
  import { sm4Dncrypt } from "@/utils/encrypt"
  export default {
    name: "bindingEnterprise",
    mounted() {
      this.initData()
    },
    data() {
      return {
        isEdit:true,
        tabPosition: 'left',
        loading: false,
        options: [],
        company: {
          id:"",
          companyId:"",
          job: "",
          status: null,
          userId:0,
        }
      };
    },

    methods: {

      async initData(){
        let user=JSON.parse(sm4Dncrypt(this.$cache.local.get("portal_user")))
        this.company.userId=user.id
        await getEmployeeCompany().then(res=>{
            if(res.code==200&&res.data&&Object.keys(res.data).length>0){
              this.isEdit=false
              this.company.companyId=res.data.companyId
              this.company.userId=res.data.userId
              this.options.push({"label":res.data.coName,"value":res.data.companyId})
              this.company.job=res.data.job
              this.company.id=res.data.id
              this.company.status=res.data.status
            }else {
              this.isEdit=true
            }
        })

      },

      remoteMethod(query) {
        if (query !== '') {
          this.loading = true;
          queryCoByName({name:query}).then(res=>{
            if(res.code==200&&res.data&&Object.keys(res.data).length>0){
              this.options = [];
              let list=res.data
              list.forEach(item=>{
                this.options.push({
                  label:item.coName,
                  value:item.id,
                })
              })
              this.loading = false;
            }
          })
        } else {
          this.options = [];
        }
      },
      //提交绑定
      saveEmployee(){
        saveEmployee(this.company).then(res=>{
            if(res.code==200){
              Message.success(res.msg)
              this.initData()
            }else{
              Message.error(res.msg)
            }
        })
      },
      //解绑
      unbind(){
        this.$confirm('是否确认解除用户绑定?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          unbindEmployee({ "id": this.company.id }).then(res => {
            if (res.code == 200) {
              this.initData()
              Message.success(res.msg)
              this.isEdit=true;
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
    }
  }
</script>

<style scoped>


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
    margin-top: 2px;
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

  /*账号安全管理*/
  /*.account {*/
  /*  width: 34.375rem;*/
  /*  height: 5rem;*/
  /*  !*border: 1px solid black;*!*/
  /*  margin-left: 50px;*/
  /*}*/


  .person_psw >>> .el-input__inner {
    width: 20.25rem;
    height: 2.875rem;
    background: #ffffff;
    border: 1px solid #e9eaed;
    border-radius: 0;

  }


  /*企业绑定管理*/
  .company_card {
    width: 100%;
    height: 3.125rem;
    display: flex;
    margin-top: 1.25rem;
    /*margin-left: 30px;*/
    /*border: 1px solid black;*/
  }

  .company_name {
    width: 8.125rem;
    height: 2.875rem;
    /*border: 1px solid black;*/
    font-size: 14px;
    font-family: PingFang SC, PingFang SC-Medium;
    font-weight: 500;
    text-align: right;
    color: #333333;
    line-height: 2.875rem;
  }


  .company_select >>> .el-input__inner {
    /*width: 280px;*/
    height: 2.875rem;
    background: #ffffff;
    border: 1px solid #e9eaed;
    border-radius: 0;
    margin-left: 25px;
  }

  /*.company_change {*/
  /*  width: 11.25rem;*/
  /*  height: 2.875rem;*/
  /*  background: #0256ca;*/
  /*  font-size: 14px;*/
  /*  font-family: PingFang SC, PingFang SC-Bold;*/
  /*  font-weight: 700;*/
  /*  text-align: center;*/
  /*  line-height: 2.875rem;*/
  /*  color: #ffffff;*/
  /*  cursor: pointer;*/
  /*  !*margin: auto;*!*/
  /*  margin-left: 300px;*/
  /*}*/
  .company_button {
    width: 11.25rem;
    height: 2.875rem;
    background: #0256ca;
    font-size: 14px;
    font-family: PingFang SC, PingFang SC-Bold;
    font-weight: 700;
    text-align: center;
    line-height: 2.875rem;
    color: #ffffff;
    border-radius: 0;
    padding: 0px 18px;
  }
  .text-style{
    font-size: 28px;
  }

  .alert-style{
    width: 815px;
    margin: 30px auto;
    height: 84px;
  }

</style>
