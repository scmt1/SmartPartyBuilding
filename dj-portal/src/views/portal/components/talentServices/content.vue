<template>
  <!--  人才服务详情页-->
  <div class="talent-view">
    <h1>{{form.SXMC}}</h1>
    <div class="box" v-if="form.SXMC=='高层次人才子女入学申报'">
      <el-image :src="description.image" style="height: 221px"/>
      <div style="width: 823px;margin-left: 24px">
        <h2>{{description.title}}</h2>
        <el-image :src="description.image2" style="top: -2px;"></el-image>
        <p style="font-weight: 700">{{description.text}}</p>
        <p>{{description.content}}</p>
      </div>
    </div>
    <el-form ref="form" :model="form" label-width="110px">
      <!--     基本信息-->
      <h2>基本信息</h2>
      <el-divider></el-divider>
      <el-row>
        <el-col :span="12">
          <el-form-item label="姓名" >
            <el-input v-model="form.NAME" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="身份证号码">
            <el-input v-model="form.IDCARD" disabled></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="电话号码">
        <el-input v-model="form.PHONENUMBER" disabled></el-input>
      </el-form-item>
      <!--      需求内容-->
      <h2>需求内容</h2>
      <el-divider></el-divider>
      <el-input placeholder="请填写需求信息" type="textarea" v-model="form.CONTENT" :rows="9" style="margin-bottom: 20px;" :disabled="!isEdit"> </el-input>
      <el-form-item label="预约办理时间">
        <el-date-picker type="date" placeholder="选择日期" v-model="form.HANDLINGTIME" style="width: 100%;" :disabled="!isEdit"></el-date-picker>
      </el-form-item>
      <div style="text-align: center;">
        <el-button @click="onSubmit" v-show="isEdit">提交</el-button>
        <el-button @click="toPath" v-show="!isEdit">返回</el-button>
      </div>
    </el-form>
  </div>
</template>
<script>
import { SaveSubmitProject} from '@/api/portal/rcfw'

  export default {
    name: 'view',
    props: {
      form:Object,
      isEdit:{
        type:Boolean,
        default:true
      },
    },
    mounted() {
    },
    data(){
      return{
        description: {
          title: '适用范围',
          image: require('@/assets/images/portal/talentServices/rczvrx.png'),
          image2: require('@/assets/images/portal/service/scene/underline.png'),
          text: '经市委人才工作领导小组办公室认定的高层次人才。',
          content: '(一)领军人才\n' +
            '中国科学院院士、中国工程院院士;国家和省级人才计划入选者;国家和省级有突出贡献专家;享受国务院政府特殊津贴专家;省级及以上学术、技术带头人;国家和省级科学技术奖励获得者;优秀海外归国专家学者;酒城英才卡获得者等。\n' +
            '(二）其他人才\n' +
            '全日制博士研究生;正高级职称人员。',
        },
      }
    },
    methods:{
      onSubmit(){
        //保存提交数据
        SaveSubmitProject(this.form).then(res=>{
          if (res.code===0){
            this.$Message.success('提交成功')
            this.toPath()
          }
        })
      },
      toPath(){
        this.$router.push({path: '/portalCenter',query: {titleIndex: '3'}})
      }
    },

  }
</script>

<style lang="scss" scoped>
  .talent-view{
    width: 75rem;
    margin: 0 auto 50px;
    .box{
      display: flex;
      height: 20.4375rem;
      margin-bottom: 20px;
      p{
        font-size: 20px;
        font-family: 'PingFang SC', 'PingFang SC-Medium';
        font-weight: 500;
        color: #212121;
        line-height: 40px;
        white-space: pre-wrap;
      }
    }
    .el-button{
      width: 137px;
      border-radius: 0;
      color: white;
      background: rgba(2, 86, 202);
    }

    ::v-deep .el-form-item__label {
      font-size: 16px;
      color: #000000;
    }
    ::v-deep .el-input__inner {
      width: 327px;
      color: #000000;
    }
    ::v-deep .el-divider--horizontal {
      display: block;
      height: 2px;
      width: 100%;
      background-color: #000000;
      margin: 8px 0 22px;
    }
    h1{
      line-height: 3.1;
      font-size: 40px;
      font-family: 'PingFang SC', 'PingFang SC-Heavy';
      font-weight: 800;
      color: #222222;
    }
    h2{
      font-size: 24px;
      font-family: 'PingFang SC', 'PingFang SC-Bold';
      font-weight: 700;
      color: #212121;
    }
  }

</style>
