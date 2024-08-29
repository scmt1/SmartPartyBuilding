<template>
  <!--  技能培训-->
  <div class="content">
    <!--    背景图-->
    <div class="img-back">
      <div class="margin-style">
      </div>
    </div>
    <div class="margin-style" style="margin-top: 0.25rem">
      <div class="img-back2" v-for="(item,index) in dataList"
           :style="{marginLeft: index%4===0?'0':'1.125rem' }">
        <el-image :src="item.image" @click="toView('/skillsTrainingView')" class="div-cursor"></el-image>
        <div style="margin-left: 0">
          <p class="text1 div-cursor" @click="toView('/skillsTrainingView')">{{item.title}}</p>
          <p class="text2">学时：{{item.creditHours}}</p>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'skillsTraining',
  props: {

  },
  data(){
    return{
      dataList: [
        {
          title: '农业科目培训课程',
          image: require('@/assets/images/portal/invitation/skillsTraining/image0.png'),
          creditHours: '4小时',
        },
        {
          title: '人工神经系统及其应用',
          image: require('@/assets/images/portal/invitation/skillsTraining/image1.png'),
          creditHours: '13小时',
        },
        {
          title: '人力资本投资',
          image: require('@/assets/images/portal/invitation/skillsTraining/image2.png'),
          creditHours: '12小时',
        },
        {
          title: '控制权溢价相关案例探讨',
          image: require('@/assets/images/portal/invitation/skillsTraining/image3.png'),
          creditHours: '4小时',
        },
        {
          title: '“雏鹰计划”大学生就业创业培训',
          image: require('@/assets/images/portal/invitation/skillsTraining/image4.png'),
          creditHours: '5小时',
        },
        {
          title: '“雏鹰计划”大学生就业创业培训',
          image: require('@/assets/images/portal/invitation/skillsTraining/image5.png'),
          creditHours: '6小时',
        },
        {
          title: '“雏鹰计划”大学生就业创业培训',
          image: require('@/assets/images/portal/invitation/skillsTraining/image6.png'),
          creditHours: '8小时',
        },
        {
          title: 'Excel入门到精通必备技能视频课程',
          image: require('@/assets/images/portal/invitation/skillsTraining/image7.png'),
          creditHours: '6小时',
        },
        {
          title: '针灸推拿培训',
          image: require('@/assets/images/portal/invitation/skillsTraining/image8.png'),
          creditHours: '7小时',
        },{
          title: '大数据培训班',
          image: require('@/assets/images/portal/invitation/skillsTraining/image9.png'),
          creditHours: '9小时',
        },
      ],

    }
  },
  methods:{
    //跳转
    toView(path){
      this.$router.push(path)
    },
  },


}
</script>

<style lang="scss" scoped>
.content{
  margin: 0 auto 5rem auto;

  .div-cursor{
    cursor: pointer;
  }

  .margin-style{
    width: 75rem;
    margin: 0 auto;
  }
  .img-back{
    width: 100%;
    height: 12.25rem;
    background-image: url("~@/assets/images/portal/invitation/skillsTraining/banner.png");
    background-size: 100% 100%;
    min-width: 75rem;
  }
  .img-back2{
    margin-top: 1rem;
    display: inline-block;
    vertical-align: top;
    width: 17.56rem;
    height: 16.5rem;
    background: #ffffff;
    box-shadow: 0.26px 2.99px 6px 0 rgba(202,202,202,0.44);
    .text1{
      margin: 0.5rem 0 0.5rem 0;
      font-size: 18px;
      font-family: 'PingFang SC', 'PingFang SC-Bold';
      font-weight: 700;
      color: #222222;
    }
    .text2{
      font-size: 18px;
      font-family: 'PingFang SC', 'PingFang SC-Medium';
      font-weight: 500;
      text-align: left;
      color: #888888;
    }
  }
}
</style>
