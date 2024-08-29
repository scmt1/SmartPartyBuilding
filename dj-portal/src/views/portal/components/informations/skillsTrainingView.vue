<template>
<!--  技能培训详情页-->
  <div class="content">
<!--    题目-->
    <div class="part1">
      <div class="part1-content margin-style">
        <div class="inline">
          <el-image :src="image"></el-image>
        </div>
        <div class="inline inline2">
          <h3>农业科目培训课程</h3>
          <p class="text2">使用年份：2018/2019/2020/2021/2022</p>
          <p class="text2">学 时：4小时</p>
          <el-button class="button-style">
            <el-image :src="image2" class="image-style"></el-image>
            已收藏
          </el-button>
        </div>
      </div>
    </div>

<!--    列表-->
    <div class="part2 margin-style">
      <h3>课程目录</h3>
      <div class="box-card" v-for="item in dataList">
        <span class="text1">{{item.title}}</span>
        <div class="box-right">
          <div>
            <span class="text2">{{ item.allTime }}</span>
            <div class="line" style="margin: auto 0.875rem"></div>
            <span class="text2">{{ item.overTime }}</span>
          </div>
          <el-button class="button-style2"  @click="dialogVisible = true">开始学习</el-button>
        </div>
      </div>
    </div>

    <el-dialog
      :visible.sync="dialogVisible"
      width="45%"
      >
      <VideoPlay
        v-show="true"
        :videoUrl="videoUrl"
        :videoCover="videoCover"
        width="100%"
        :autoplay="false"
        :controls="true"
        :loop="false"
        :muted="false"
        preload="auto"
        :showPlay="true"
        :playWidth="96"
        zoom="fill"
      />
    </el-dialog>
  </div>
</template>
<script>

import VideoPlay from '@/components/Video/VideoPlay.vue'
export default {
  name: 'skillsTrainingView',
  props: {

  },
  components: {
    VideoPlay
  },
  methods:{
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => {});
    }
  },
  data(){
    return{
      dialogVisible: false,
      // 视频链接支持https或require本地地址
      videoUrl: process.env.VUE_APP_YSZJ_ADDRESS+'/cms/attachment/20230228/1b64d7a347e1410bbdf98c1c90ce74c1.mp4',
      // 视频封面支持https或require本地地址/cms/attachment/20230228/254bf8ac5a90412a93c5eb17878e857e.png
      videoCover: process.env.VUE_APP_YSZJ_ADDRESS+'/cms/attachment/20230228/254bf8ac5a90412a93c5eb17878e857e.png',
      tempData: {

      },
      image: require('@/assets/images/portal/invitation/skillsTraining/image_view.png'),
      image2: require('@/assets/images/portal/invitation/skillsTraining/collection.png'),
      dataList: [
        {
          title: '农业科目培训课程(一）',
          allTime: '0时44分48秒',
          overTime: '0时20分48秒',
        },
        {
          title: '农业科目培训课程(二）',
          allTime: '0时44分48秒',
          overTime: '0时20分48秒',
        },
        {
          title: '农业科目培训课程(三）',
          allTime: '0时44分48秒',
          overTime: '0时20分48秒',
        },
        {
          title: '农业科目培训课程(四）',
          allTime: '0时44分48秒',
          overTime: '已完成',
        },

      ],

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
    height: 20.375rem;
    background-color: #F9F9F9;
    padding: 3.4375rem 0 2.5rem 0;

    .part1-content{
      min-height: 14.4375rem;

      .inline{
        display: inline-block;
        vertical-align: top;
      }
      .inline2{
        margin-left: 1.625rem;
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
          margin: 0.8125rem 0 2rem 0;
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
        width: 27.4375rem;
        div{
          display: flex;
          justify-content: space-between;
          align-items: center;
        }
        .line{
          width: 1px;
          height: 17px;
          background: #d1d1d1;
        }
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
