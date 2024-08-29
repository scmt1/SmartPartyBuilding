<template>
  <div >
    <div class="img-back">
      <div class="menu-page">
        <h1 class="text-img">
        业务办理
        </h1>
      </div>
    </div>
    <div class="menu-page" style="margin-top: 1.81rem">
      <el-tabs
        v-model="activeName"
      >
        <el-tab-pane v-for="(item,index) in nameList" :key="index" :value="item" :name="item">
          <div slot="label">
            <span class="text-style"> {{labelList[index]}}</span>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div>
      <component :is="isComponent" ></component>
    </div>
  </div>

</template>
<script>
  import First from './types/first.vue'
  import Second from './types/second.vue'
  import Third from './types/third.vue'

  export default {
    name: 'secondary_home',
    props: {

    },
    data(){
      return{
        componentsList: {
          first: First,
          second: Second,
          third: Third,
        },
        activeName: 'first',
        nameList: ['first','second','third'],
        labelList: ['个人服务','团队服务','单位服务'],

      }
    },
    methods: {
      init(){
        if(this.$route.params.data){
          let active_name = this.$route.params.data.activeName;
          if (active_name!==null&&active_name!==''){
            this.activeName=active_name
          }
        }
      },
      handleSelect(key, keyPath) {
        console.log(key, keyPath);
      },
    },
    mounted() {
      this.init()
    },
    computed: {
      isComponent() {
        return this.componentsList[this.activeName];
      },
    },

  }
</script>

<style scoped>
  .menu-page{
    width: 75rem;
    margin: 0 auto;
  }
  .img-back{
    width: 100%;
    height: 12.5rem;
    background-image: url("~@/assets/images/portal/service/scene/banner.png");
    background-size: 100% 100%;
    min-width: 75rem;
  }
  .text-img{
    position: relative;
    top: 5.0625rem;
    font-size: 38px;
    font-family: "PingFang SC", "PingFang SC-Bold";
    font-weight: 700;
    color: #ffffff;
  }

  /*去除tabs竖线*/
  .menu-page ::v-deep .el-tabs__nav-wrap::after{
    height: 0 !important;
  }
  /*.menu-page ::v-deep .el-tabs__active-bar{*/
  /*  background-color: transparent !important;*/
  /*}*/
  ::v-deep .el-tabs__item.is-active {
    color: #0256ca;
  }
  ::v-deep .el-tabs__item:hover {
    color: #0256ca;
  }
  ::v-deep .el-tabs__active-bar.is-top {
    background: #0256ca;
  }

  .el-menu{
    width: 100%;
  }
  .el-submenu .el-menu-item {
    height: 50px;
    line-height: 50px;
    padding: 0 1px;
    min-width: 100%;
  }

  .text-style{
    font-size: 24px;
    font-family: "PingFang SC", "PingFang SC-Bold";
    font-weight: 800;
  }

</style>
