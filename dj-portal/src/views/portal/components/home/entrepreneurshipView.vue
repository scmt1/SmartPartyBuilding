<template>
<!--  大学生就业创业详情页-->
  <div class="content">
<!--    题目-->
    <div class="part1">
      <div class="part1-content margin-style">
        <div class="inline2">
          <h3>{{ tempData.title }}</h3>
          <p class="text2">2023-03-08 17:26:38</p>
          <!--          <p class="text2">直播课程</p>-->
          <!--          <el-button class="button-style">-->
          <!--            <el-image :src="image" class="image-style"></el-image>-->
          <!--            已收藏-->
          <!--          </el-button>-->
        </div>
        <div class="inline">
<!--          <el-image :src="tempData.image"></el-image>-->
          <VideoPlay
            v-show="true"
            :videoUrl="ADDRESS+videoUrl"
            :videoCover="ADDRESS+videoCover"
            width="100%"
            :autoplay="true"
            :controls="true"
            :loop="false"
            :muted="false"
            preload="auto"
            :showPlay="true"
            :playWidth="96"
            zoom="fill"
          />
        </div>

      </div>
    </div>

<!--    列表-->
    <div class="part2 margin-style">
      <h3>课程目录</h3>
      <div class="box-card" v-for="item in tempData.dataList">
        <span class="text1">{{item.title}}</span>
        <div class="box-right">
          <div>
            <span class="text2">{{ item.trainingTime }}</span>
<!--            <div class="line" style="margin: auto 1rem"></div>-->
<!--            <span class="text2" :style="{color: item.status==='1'?'#0256CA':'#888888'}">{{ statusName[item.status] }}</span>-->
          </div>
          <el-button class="button-style2" @click="playVideo(item.videoUrl,item.videoCover)">开始学习</el-button>
        </div>
      </div>
    </div>

  </div>
</template>
<script>

import VideoPlay from '@/components/Video/VideoPlay.vue'
export default {
  name: 'entrepreneurshipView',
  props: {

  },
  components: {
    VideoPlay
  },
  data(){
    return{
      ADDRESS:process.env.VUE_APP_YSZJ_ADDRESS,
      // 视频链接支持https或require本地地址/cms/attachment/20230308/e395f6fb8a3840d09cde86dd55d8ab95.mp4
      videoUrl: '/cms/attachment/20230308/e395f6fb8a3840d09cde86dd55d8ab95.mp4',
      // 视频封面支持https或require本地地址/cms/attachment/20230308/eb4e27e13454445ea3d8d989b5d56b55.jpg
      videoCover: '/cms/attachment/20230308/eb4e27e13454445ea3d8d989b5d56b55.jpg',
      tempData: {
        title: '“雏鹰计划”大学生就业创业培训',
        image: require('@/assets/images/portal/invitation/entrepreneurship/image2.png'),
        dataList: [
          {
            title: '“雏鹰计划”大学生就业创业培训 （第一期）',
            trainingTime: '2023-01-01至2023-02-01',
            status: '0',
          },
          {
            title: '“雏鹰计划”大学生就业创业培训 （第二期）',
            trainingTime: '2023-02-01至2023-03-01',
            status: '1',
          },
          {
            title: '“雏鹰计划”大学生就业创业培训 （第三期）',
            trainingTime: '2023-03-04至2023-04-05',
            status: '2',
          },
        ],
      },
      // “雏鹰计划”大学生就业创业培训
      tempData1: {
        title: '“雏鹰计划”大学生就业创业培训',
        image: require('@/assets/images/portal/invitation/entrepreneurship/image2.png'),
        dataList: [
          {
            title: '“雏鹰计划”大学生就业创业培训 （第一期）',
            trainingTime: '2023-01-01至2023-02-01',
            status: '0',
            videoUrl: '/cms/attachment/20230308/7c527e7317504e7d8b7f87f49aefb7a4.mp4',
            videoCover: '/cms/attachment/20230308/b7dd6e087b5c49ad8c18fe3399854c25.jpg',
          },
          {
            title: '“雏鹰计划”大学生就业创业培训 （第二期）',
            trainingTime: '2023-02-01至2023-03-01',
            status: '1',
            videoUrl: '/cms/attachment/20230308/7c527e7317504e7d8b7f87f49aefb7a4.mp4',
            videoCover: '/cms/attachment/20230308/b7dd6e087b5c49ad8c18fe3399854c25.jpg',
          },
          {
            title: '“雏鹰计划”大学生就业创业培训 （第三期）',
            trainingTime: '2023-03-04至2023-04-05',
            status: '2',
            videoUrl: '/cms/attachment/20230308/7c527e7317504e7d8b7f87f49aefb7a4.mp4',
            videoCover: '/cms/attachment/20230308/b7dd6e087b5c49ad8c18fe3399854c25.jpg',
          },
        ],
      },
      // 大学生就业创业指导
      tempData2: {
        title: '大学生就业创业指导',
        image: require('@/assets/images/portal/invitation/entrepreneurship/image3.png'),
        dataList: [
          {
            title: '大学生就业创业指导 （第一期）',
            trainingTime: '2023-01-01至2023-02-01',
            status: '0',
            videoUrl: '/cms/attachment/20230308/e395f6fb8a3840d09cde86dd55d8ab95.mp4',
            videoCover: '/cms/attachment/20230308/eb4e27e13454445ea3d8d989b5d56b55.jpg',
          },
          {
            title: '大学生就业创业指导 （第二期）',
            trainingTime: '2023-02-01至2023-03-01',
            status: '1',
            videoUrl: '/cms/attachment/20230308/e395f6fb8a3840d09cde86dd55d8ab95.mp4',
            videoCover: '/cms/attachment/20230308/eb4e27e13454445ea3d8d989b5d56b55.jpg',
          },
          {
            title: '大学生就业创业指导 （第三期）',
            trainingTime: '2023-03-04至2023-04-05',
            status: '2',
            videoUrl: '/cms/attachment/20230308/e395f6fb8a3840d09cde86dd55d8ab95.mp4',
            videoCover: '/cms/attachment/20230308/eb4e27e13454445ea3d8d989b5d56b55.jpg',
          },
          {
            title: '大学生就业创业指导 （第四期）',
            trainingTime: '2023-03-04至2023-04-05',
            status: '2',
            videoUrl: '/cms/attachment/20230308/e395f6fb8a3840d09cde86dd55d8ab95.mp4',
            videoCover: '/cms/attachment/20230308/eb4e27e13454445ea3d8d989b5d56b55.jpg',
          },
        ],
      },
      statusName: ['已结束','进行中','未开始'],
      image: require('@/assets/images/portal/invitation/skillsTraining/collection.png'),
    }
  },
  mounted() {
    this.init()
  },
  methods:{
    init(){
      let type = this.$route.query.type;
      debugger
      if (type==='0'){
        this.tempData=this.tempData1
      }else {
        this.tempData=this.tempData2
      }
      this.playVideo(this.tempData.dataList[0].videoUrl,this.tempData.dataList[0].videoCover)
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => {});
    },
    playVideo(videoUrl,videoCover){
      this.videoUrl=videoUrl
      this.videoCover=videoCover
    }
  },


}
</script>

<style lang="scss" scoped>
.content{
  margin: 0 auto 5rem auto;

  .margin-style{
    width: 75rem;
    margin: 0 auto;
  }

  .part1{
    //height: 20.375rem;
    background-color: #F9F9F9;
    padding: 0.4375rem 0 2.5rem 0;

    .part1-content{
      //height: 14.4375rem;

      .inline{
        text-align: center;

      }
      .inline2{

        .button-style{
          padding: 5px 10px 10px 10px;
          margin-top: 1rem;
          width: 135px;
          height: 50px;
          background: #0256ca;
          border-radius: 0;
          font-size: 22px;
          font-family: 'PingFang SC', 'PingFang SC-Medium';
          font-weight: 500;
          text-align: center;
          color: #ffffff;
          .image-style{
            position: relative;
            top: 4px;
          }
        }
        h3{
          margin: 0.8125rem 0;
        }
        p{
          margin-bottom: 0.8rem;
        }
      }
    }
  }
  .part2{
    margin: 2.375rem auto 1.0626rem auto;

    .box-card{
      margin-top: 1.25rem;
      padding: 1.25rem;
      display: flex;
      width: 75rem;
      height: 6.75rem;
      justify-content: space-between;
      align-items: center;
      box-shadow: 0.26px 2.99px 6px 2px rgba(202,202,202,0.44);
      .box-right{
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 38.125rem;
        div{
          display: flex;
          justify-content: space-between;
          align-items: center;
        }
        //.line{
        //  width: 1px;
        //  height: 17px;
        //  background: #d1d1d1;
        //}
        .button-style2{
          padding: 10px;
          width: 114px;
          height: 50px;
          background: #0256ca;
          border-radius: 0;
          font-size: 22px;
          font-family: 'PingFang SC', 'PingFang SC-Medium';
          font-weight: 500;
          text-align: center;
          color: #ffffff;
        }
      }
    }
  }
  h3{
    font-size: 26px;
    font-family: 'PingFang SC', 'PingFang SC-Heavy';
    font-weight: 800;
    color: #222222;
  }
  .text1{
    cursor: pointer;
    font-size: 24px;
    font-family: 'PingFang SC', 'PingFang SC-Medium';
    font-weight: 500;
    color: #222222;
  }
  .text2{
    font-size: 22px;
    font-family: 'PingFang SC', 'PingFang SC-Medium';
    font-weight: 500;
    color: #888888;
  }



}
</style>
