<template>
  <div>
    <div class="img-back">
      <h1 class="text-img">场景服务</h1>
    </div>
    <div  class="page-content" >
      <div class="tabLeft" style="width: 187px;display: inline-block;">
        <el-tabs v-model="activeName" tab-position="left" style="box-shadow: rgba(0,0,0,.3) 0 0 15px;">
          <el-tab-pane v-for="(item,index) in nameList" :key="index" :value="item" :name="item">
            <div :class="getStyle(item)" @click="changeColor(item)" slot="label">
              <span class="text-span">{{labelList[index]}}</span>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div style="width: 980px;display: inline-block;vertical-align: top; margin-left: 21px">
        <component :is="isComponent" ></component>
      </div>
    </div>
  </div>
</template>

<script>
import First from './scenes/first.vue'
import Second from './scenes/second.vue'
import Third from './scenes/third.vue'
import Fourth from './scenes/fourth.vue'
import Five from './scenes/five.vue'
import Six from './scenes/six.vue'

export default {
  name:'scene',

  mounted() {
    if(this.$route.params.data){
      this.activeName =this.$route.params.data
    }else if(this.$cache.local.get('scene_active')){
      this.activeName=this.$cache.local.get('scene_active')
    }
    this.changeColor(this.activeName)
  },
  beforeDestroy() {
    this.$cache.local.set("scene_active",this.activeName)
  },
  data() {
    return {
      activeName: "first",
      componentsList: {
        first: First,//需要引入具体的组件
        second: Second,
        third: Third,
        fourth: Fourth,
        five: Five,
        six: Six,
      },
      nameList: ["first","second","third","fourth","five","six"],
      labelList: ['高校毕业生','高层次人才','海外留学人员','专业技术人才','技能人才','博士后'],
      num: ''
    }
  },
  computed: {
    isComponent() {
      return this.componentsList[this.activeName];
    }
  },
  methods:{
    changeColor(num){
      console.log(num)
      this.num=num
    },
    getStyle(key){
      let value=''
      if (this.num===key){
        value='button-div2'
      }else{
        value='button-div'
      }
      return value
    }
  }
}
</script>

<style lang="scss" scoped>
.page-content{
  width: 75rem;
  margin: 2.619rem auto 3.93rem auto;
}

.img-back{
  width: 100%;
  height: 12.5rem;
  background-image: url("~@/assets/images/portal/service/scene/rcfl_banner.png");
  background-size: 100% 100%;
  min-width: 75rem;
  .text-img{
    width: 75rem;
    margin: 0 auto;
    position: relative;
    top: 5.0625rem;
    font-size: 38px;
    font-family: "PingFang SC", "PingFang SC-Bold";
    font-weight: 700;
    color: #ffffff;
  }
}

/*文字*/
.text-span{
  font-size: 16px;
  font-family: "PingFang SC", "PingFang SC-Bold";
  /*font-weight: 700;*/
}


/*去除tabs竖线*/
.tabLeft ::v-deep .el-tabs__nav-wrap::after{
  height: 0 !important;
}
.tabLeft ::v-deep .el-tabs__active-bar{
  background-color: transparent !important;
}

.button-div{
  /*padding-right: 100px;*/
  width: 187px;
  height: 63px;
  line-height: 63px;
  text-align: center;
  color: black;
}
.button-div2{
  /*padding-right: 100px;*/
  width: 187px;
  height: 63px;
  line-height: 63px;
  text-align: center;
  color: white;
  background-color: #0758CB;
}

/*鼠标悬停时 hover*/
.button-div:hover,.button-div:visited{
  background-color: #0758CB;
  color: white;
  /*text-align: center*/
}

::v-deep .el-tabs__item {
  padding: 0 0;
  height: 63px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  line-height: 40px;
  display: inline-block;
  list-style: none;
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  position: relative;
}

</style>
