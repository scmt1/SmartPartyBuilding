<template>
<!--  人才公寓管理-->
  <div class="live-page">
<!--    标题-->
<!--    <h1>人才公寓管理</h1>-->
<!--    主要内容-->
    <div class="live-content">
      <el-tabs tab-position="left" style="height: 100%">
        <el-tab-pane label="进度查询" >
          <div v-if="wdsqList.length>0">
            <div class="wdsq-card" v-for="item in wdsqList">
              <div class="card1">
                <span class="text1">{{item.gyName}}</span>
                <div style="display: flex;width: 9.125rem;text-align: right;justify-content: space-between;">
                  <span class="text2">审核状态：</span>
                  <p class="text2" :style="{backgroundColor: statusColor[item.status],color: '#FFFFFF',width: '64px'}">{{statusName[item.status]}}</p>
                </div>

              </div>
              <div class="card2">
                <span>申请人：{{item.sqrName}}</span>
                <span class="text3">|</span>
                <span>房源类型：{{item.hxType}}</span>
                <span class="text3">|</span>
                <span>入住周期：{{item.leaseTerm}}</span>
                <span class="text3">|</span>
                <span>申请时间：{{item.createTime}}</span>
              </div>
            </div>
          </div>
          <div v-else><el-empty></el-empty> </div>

        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import {getRecordList,getRecordDetail} from '@/api/portal/rcgy'
import { sm4Dncrypt } from '@/utils/encrypt.js'
  export default {
    name: 'personLive',
    data() {
      return {
        //侧边栏
        activeName: '1',
        sexName: ['女','男'],
        statusName: ['备案中','已备案'],
        statusColor: ['#FF9C01','#1AD21F'],
        //我的申请
        wdsqList:[
        ],
        title: '',
        color: '',
      }
    },
    mounted() {

      this.getData()
    },
    methods: {
      async getData(){
        const that=this
        let key = localStorage.getItem("portal_company")
        let company=JSON.parse(sm4Dncrypt(key))
        let param={
          pageNumber: 1, // 当前页数
          code: company.coCredit,
          pageSize: 10, //
        }
        await getRecordList(param).then(res=>{
          if(res.data&&res.data.records&&res.data.records.length>0){
            that.wdsqList=res.data.records
          }
        })
        // console.log("this.wdsqList",this.wdsqList)
        this.wdsqList.forEach(item=>{
          getRecordDetail({id:item.id}).then(res=>{
            if(res.data&&res.data.occupants&&res.data.occupants.length>0){
              const names=res.data.occupants.map(i=>{
                return i.checkInName
              })
              item.sqrName=`${names.join(", ")}`;
              that.$forceUpdate();
            }
          })

        })
        console.log("aaa",this.wdsqList)

      }
    }
  }
</script>

<style lang="scss" scoped>

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
