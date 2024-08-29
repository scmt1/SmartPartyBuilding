<template>
<!--  个人安居管理-->
  <div class="live-page">
<!--    标题-->
<!--    <h1>个人安居管理</h1>-->
<!--    主要内容-->
    <div class="live-content">
      <el-tabs tab-position="left" style="height: 100%">
        <el-tab-pane label="人才驿站申请管理" >
          <div class="right">
            <el-tabs v-model="activeName">
              <el-tab-pane label="个人信息" name="1">
                <el-form v-if="showInfo" ref="form" :model="form" label-width="83px" label-suffix=":">
                  <el-form-item >
                    <div slot="label">
                      <span class="text-p">审核状态:</span>
                    </div>
                    <p class="text-p" :style="{backgroundColor: statusColor[form.auditStatus-1],fontSize: '14px'}">
                      {{statusName[form.auditStatus-1]}}
                    </p>
                  </el-form-item>
                  <el-row>
                    <el-col :span="12">
                      <el-form-item label="真实姓名">
                        <span>{{form.name}}</span>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="毕业院校">
                        <span>{{form.graduatedSchool}}</span>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="12">
                      <el-form-item label="性别">
                        <span>{{sexName[form.sex]}}</span>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="学历">
                        <span>{{form.education}}</span>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="12">
                      <el-form-item label="手机号码">
                        <span>{{form.mobile}}</span>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="毕业时间">
                        <span>{{form.graduatedTime}}</span>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="12">
                      <el-form-item label="身份证号">
                        <span>{{form.idCard}}</span>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="所学学科">
                        <span>{{form.major}}</span>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-form-item label="证件上传">
                    <div class="box-images">
                      <el-image :src="ADDRESS+'/tempFileUrl'+item.fileUrl" v-for="(item,index) in form.attachmentList" :key="index"/>
                    </div>
                  </el-form-item>
                </el-form>
                <el-empty v-else></el-empty>
              </el-tab-pane>
              <el-tab-pane label="我的申请" name="2">
                <div v-if="showInfo"  class="wdsq-card" v-for="item in wdsqList">
                  <div class="card1">
                    <span class="text1">{{item.hotelName}}</span>
                    <div style="display: flex;width: 9.125rem;text-align: right;justify-content: space-between;">
                      <span class="text2">审核状态：</span>
                      <p class="text2" :style="{backgroundColor: statusColor2[item.auditStatus-1],color: '#FFFFFF',width: '64px'}">{{statusName2[item.auditStatus]}}</p>
                    </div>

                  </div>
                  <div class="card2">
                    <span>申请人：{{item.name}}</span>
                    <span class="text3">|</span>
                    <span>择业意向：{{item.jobSelection}}</span>
                    <span class="text3">|</span>
                    <span>申请时间：{{item.createTime}}</span>
                    <span class="text3">|</span>
                    <span>预计入住时间：{{item.reserveDate}}</span>
                  </div>
                </div>
                <el-empty v-if="!showInfo"  ></el-empty>
              </el-tab-pane>
              <el-tab-pane label="我的入住" name="3">
                <div v-if="showInfo"  class="wdsq-card" v-for="item in wdrzList">
                  <div class="card1">
                    <span class="text1">{{item.hotelName}}</span>
                    <div style="display: flex;width: 9.125rem;text-align: right;justify-content: space-between;">
                      <span class="text2">审核状态：</span>
                      <p class="text2" :style="{backgroundColor: statusColor2[item.auditStatus-1],color: '#FFFFFF',width: '64px'}">{{statusName2[item.auditStatus]}}</p>
                    </div>

                  </div>
                  <div class="card2">
                    <span>申请人：{{item.name}}</span>
                    <span class="text3">|</span>
                    <span>择业意向：{{item.jobSelection}}</span>
                    <span class="text3">|</span>
                    <span>申请时间：{{item.createTime}}</span>
                    <span class="text3">|</span>
                    <span>预计入住时间：{{item.reserveDate}}</span>
                  </div>
                </div>
                <el-empty v-if="!showInfo"  ></el-empty>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-tab-pane>
        <!--<el-tab-pane label="人才公寓申请管理" >
          <div class="right">
            <el-tabs v-model="activeName2">
              <el-tab-pane :label="labelList2[num]" :name="temp" v-for="(temp,num) in nameList2" :key="num">
                <div class="wdsq-card" v-for="item in dataList2">
                  <div class="card1">
                    <span class="text1">{{item.name}}</span>
&lt;!&ndash;                    <span class="text2">审核状态：{{item.status}}</span>&ndash;&gt;
                  </div>
                  <div class="card2">
                    <span>申请人：{{item.applicant}}</span>
                    <span class="text3">|</span>
                    <span>申请区县：{{item.applicationArea}}</span>
                    <span class="text3">|</span>
                    <span>申请户型：{{item.applicationType}}</span>
                    <span class="text3">|</span>
                    <span>申请时间：{{item.applicationTime}}</span>
                  </div>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-tab-pane>-->
      </el-tabs>
    </div>
  </div>
</template>

<script>
import {getAccountInfo, getAccountInfoByPhone, queryTYjstbAccountList, queryTYjstbAccountList2} from '@/api/portal/qnyz'
import { sm4Dncrypt } from '@/utils/encrypt.js'
import cache from "@/plugins/cache";
import {getPhoneNum} from "@/api/login";
  export default {
    name: 'personLive',
    data() {
      return {
        ADDRESS:process.env.VUE_APP_YSZJ_ADDRESS,
        //侧边栏
        menusName: ['人才驿站申请管理','人才公寓申请管理'],
        activeName: '1',
        activeName2: '1',
        nameList2: ['1','2','3'],
        labelList2: ['待审核','审核通过','审核不通过'],
        sexName: ['女','男'],
        //个人信息
        form: {
          auditStatus: 0, // 审核状态
          name: '刘智愚', // 真实姓名
          graduatedSchool: '福建工程学院', // 毕业院校
          sex: 1, // 性别1男，0女
          education: '本科', // 学历
          mobile: '19982568893', // 手机号码
          graduatedTime: '2023-06', // 毕业时间
          idCard: '510502199812560255', // 身份证号
          major: '计算机科学与技术', // 所学学科
          attachmentList: [
            {
              fileUrl: '',
            },
            {
              fileUrl: '',
            },
            {
              fileUrl: '',
            },
          ], // 已上传文件列表
        },
        statusName: ['待审核','未通过','已通过'],
        statusColor: ['#FF9C01','#FF5D5D','#1AD21F'],
        images:[
          require('@/assets/images/portal/center/personLive/card_back.png'),
          require('@/assets/images/portal/center/personLive/card_front.png'),
          require('@/assets/images/portal/center/personLive/diploma.png'),
        ],
        dataList2:[
          {
            name: '觉醒酒店',
            applicant: '数字泸州产业投资集团有限公司',
            applicationArea: '江阳区',
            applicationType: '一人间',
            applicationTime: '2023-02-21 12:56:60',
          },
          {
            name: '普瑞斯酒店',
            applicant: '数字泸州产业投资集团有限公司',
            applicationArea: '江阳区',
            applicationType: '一人间',
            applicationTime: '2023-02-21 12:56:60',
          },
        ],
        //我的申请
        wdsqList:[
          {
            hotelName: '圣璟酒店',//酒店名字
            name: '刘智愚',//人名
            jobSelection: '自主择业',//择业意向
            createTime: '2023-02-21 12:56:60',//申请时间
            reserveDate: '2023-02-25~2023-02-28',//预计入住时间
            auditStatus: '1',//状态
          },
        ],
        //我的入住
        wdrzList:[
          {
            hotelName: '圣璟酒店',//酒店名字
            name: '刘智愚',//人名
            jobSelection: '自主择业',//择业意向
            createTime: '2023-02-21 12:56:60',//申请时间
            reserveDate: '2023-02-25~2023-02-28',//预计入住时间
            auditStatus: '1',//状态
          },
        ],
        statusName2:{
          "0": "默认状态",
          "-1": "人社审核不通过",
          "1": "人社通过",
          "-2": "酒店不通过",
          "2": "酒店确认接单",
          "3": "酒店入住成功",
          "4": "酒店退房成功",
          "5": "撤销申请",
          "6": "用户取消预约",
          "7": "已失效",
        },
        statusColor2:{
          "0": "#61B6FF",
          "-1": "#FF5D5D",
          "1": "#1AD21F",
          "-2": "#FF5D5D",
          "2": "#1AD21F",
          "3": "#1AD21F",
          "4": "#1AD21F",
          "5": "#FF5D5D",
          "6": "#FF5D5D",
          "7": "#FF5D5D",
        },
        showInfo: false,
        // statusColor2: ['#FF9C01','#61B6FF','#FF5D5D','#1AD21F'],
        // statusName2:['待审核','审核中','已通过','未通过'],
        // statusColor2: ['#FF9C01','#61B6FF','#FF5D5D','#1AD21F'],
        title: '',
        color: '',
        mobile:'',
      }
    },
    created() {
      this.getData()
    },
    methods: {
      async getData(){
        //获取用户信息
        await getPhoneNum().then(res=>{
          if (res.success){
            this.mobile=res.data.phoneNum
            //个人信息
            getAccountInfoByPhone({mobile: this.mobile}).then(res=>{
              if (res.success){
                this.form=JSON.parse(sm4Dncrypt(res.data))
                this.showInfo=true
                console.log("this.form",this.form)
              }
            })
          }
        })

        //我的申请
        queryTYjstbAccountList({mobile:this.mobile,accountId: this.form.id, auditStatus: 77,}).then(res=>{
          if (res.success){
            this.wdsqList=res.data.records
          }
        })
        //我的入住
        queryTYjstbAccountList2({mobile:this.mobile,accountId: this.form.id, auditStatus: 66,}).then(res=>{
          if (res.success){
            this.wdrzList=res.data.records
          }
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
.live-page{
  background-color: white;
  width: 75rem;
  margin: 0 auto 0 auto;
  h1{
    font-size: 26px;
    font-family: 'PingFang SC', 'PingFang SC-Bold';
    font-weight: 700;
    color: #333333;
    margin-left: 1.25rem;
  }

  .live-content{
    padding-top: 20px;
    width: 98%;
    //height: 32.6875rem;
    ::v-deep .el-tabs--left .el-tabs__header.is-left {
      width: 238px;

      .el-tabs__active-bar {
        left: 0;
      }

      .el-tabs__nav-wrap::after {
        display: none;
      }

      .el-tabs__item {
        padding: 5px 20px;
        height: 50px;
        font-size: 16px;
        font-family: 'PingFang SC', 'PingFang SC-Bold';
        font-weight: 700;
        text-align: left;
      }

      .el-tabs__item:hover {
        color: #0256CA;
        cursor: pointer;
        background-color: #E4F2FE;
      }
      .el-tabs__item.is-active {
        color: #0256CA;
        border-width: 0 0 0 2px;
        border-style: solid;
        border-radius: 0;
        border-color: #0256CA;
        background-color: #E4F2FE;
      }
      .el-tabs__active-bar {
        background-color: #0256CA;
      }
    }

    .right{
      display: inline-block;
      vertical-align: top;
      margin-left: 1.875rem;
      width: 56.1625rem;
      height: 100%;
      .box-images{
        padding: 10px 27px 0 0;
        width: 52.5rem;
        height: 23.4375rem;
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        .el-image{
          width: 251px;
        }
      }
      .wdsq-card{
        padding: 0 26px 0 16px;
        width: 56.1625rem;
        height: 7.9375rem;
        background-color: #F6F7F9;
        margin-bottom: 18px;
        .card1{
          display: flex;
          justify-content: space-between;
          align-items: center;
          height: 4.8125rem;
        }
        .card2{
          display: flex;
          justify-content: space-between;
          align-items: flex-start;
          height: 3.125rem;
          span{
            font-size: 14px;
            font-family: 'PingFang SC', 'PingFang SC-Medium';
            font-weight: 500;
            text-align: right;
            color: #333333;
          }
          .text3{
            color: #D2D6D9;
          }
        }
        .text1{
          font-size: 18px;
          font-family: 'PingFang SC', 'PingFang SC-Bold';
          font-weight: 700;
          text-align: left;
          color: #333333;
        }
        .text2{
          font-size: 14px;
          font-family: 'PingFang SC', 'PingFang SC-Bold';
          font-weight: 700;
          text-align: center;
          color: #333333;
          height: 25px;
          line-height: 25px;
        }

      }

      .text-p{
        font-size: 16px;
        font-family: 'PingFang SC', 'PingFang SC-Bold';
        font-weight: 700;
        text-align: right;
        color: #333333;
        height: 26px;
        line-height: 26px;
        margin-top: 5px;
      }

      //form描述表单
      ::v-deep .el-form{
        .el-form-item {
          margin-bottom: 7px;
        }
        .el-form-item__label {
          font-size: 16px;
          font-family: 'PingFang SC', 'PingFang SC-Medium';
          font-weight: 500;
          color: #333333;
        }
        .el-form-item__content{
          span{
            font-size: 16px;
            font-family: 'PingFang SC', 'PingFang SC-Medium';
            font-weight: 500;
            color: #333333;
          }
          p{

            font-size: 16px;
            font-family: 'PingFang SC', 'PingFang SC-Bold';
            font-weight: 700;
            text-align: center;
            color: white;
            width: 64px;
          }

        }
      }
      //tabs卡片
      ::v-deep .el-tabs__item {
        font-size: 18px;
        font-family: 'PingFang SC', 'PingFang SC-Bold';
        font-weight: 700;
        text-align: left;
        color: #333333;
      }
      ::v-deep .el-tabs__item:hover {
        color: #1f7bd5;
      }
      ::v-deep .el-tabs__item.is-active {
        color: #1f7bd5;
      }
      //选中时的下划线
      ::v-deep .el-tabs__active-bar.is-top {
        background: #1f7bd5;
      }
      ::v-deep .el-tabs__nav-wrap::after {
        height: 2px;
        background-color: #333333;
      }
    }

  }
}

</style>


<!--<template>-->
<!--  <div>-->
<!--    <el-row-->
<!--      type="flex"-->
<!--      align="middle"-->
<!--      class="margin-top-div"-->
<!--      style="height: 117px;-->
<!--            width: 875px;-->
<!--            box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)">-->
<!--      <i class="el-icon-tickets" style="font-size: 56px;margin-left: 58px"></i>-->
<!--      <span class="span1" style="margin-left: 34px">全部事项申请</span>-->
<!--      <span class="span1" style="color: #BA6C06">({{allNum}})</span>-->
<!--      <el-button class="button" style="margin-left: 367px" @click="toApply(0)">-->
<!--        查看申请信息-->
<!--      </el-button>-->
<!--    </el-row>-->
<!--    <el-row-->
<!--      type="flex"-->
<!--      align="middle"-->
<!--      class="margin-top-div"-->
<!--      style="height: 117px;-->
<!--            width: 875px;-->
<!--box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)">-->
<!--      <i class="el-icon-tickets" style="font-size: 56px;margin-left: 58px"></i>-->
<!--      <span class="span1" style="margin-left: 34px">未通过事项申请</span>-->
<!--      <span class="span1" style="color: #BA6C06">({{noPassNum}})</span>-->
<!--      <el-button class="button" style="margin-left: 340px" @click="toApply(1)">-->
<!--        查看申请信息-->
<!--      </el-button>-->
<!--    </el-row>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--  export default {-->
<!--    name: 'matter',-->
<!--    data() {-->
<!--      return {-->
<!--        allNum: 8,-->
<!--        noPassNum: 2,-->
<!--      }-->
<!--    },-->
<!--    methods: {-->
<!--      toLogin(){-->
<!--        this.$router.push({ path: '/portalCenter' });-->
<!--      },-->
<!--      toApply(type){-->
<!--        this.$router.push({ path: '/matterApply' });-->
<!--      }-->

<!--    }-->
<!--  }-->
<!--</script>-->

<!--<style lang="scss" scoped>-->
<!--/*间距*/-->
<!--.margin-top-div{-->
<!--  margin-top: 30px;-->
<!--}-->

<!--  /*按钮*/-->
<!--.button{-->
<!--  /*width: 117px;*/-->
<!--  height: 34px;-->
<!--  border-color: #A12E2E;-->
<!--  color: #A12E2E;-->
<!--  border-radius: 0;-->
<!--  border-width: 2px;-->

<!--}-->

<!--  /*字体*/-->
<!--.span1{-->
<!--  height: 45px;-->
<!--  font-size: 28px;-->
<!--  font-family: "PingFang SC", "PingFang SC-Bold";-->
<!--  font-weight: 700;-->
<!--  color: #212121;-->
<!--}-->

<!--</style>-->
