<template>
  <div class="acMsg">
    <div class="title">基本信息</div>
    <div class="container">
      <el-tabs :tab-position="tabPosition" :value="index" style="height: 600px;">
        <el-tab-pane label="账号信息管理">
          <personM></personM>
        </el-tab-pane>
        <el-tab-pane label="企业绑定管理">
          <bindingEnterprise></bindingEnterprise>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
  import personM from "@/views/portal/components/portalCenter/basisMsg/personM"
  import bindingEnterprise from "@/views/portal/components/portalCenter/basisMsg/bindingEnterprise"

  export default {
    props: {
      index: {
        type: String,
        default: "0"
      }
    },
    name: "accountMsg",
    components: {
      personM,
      bindingEnterprise
    },
    data() {
      return {
        tabPosition: 'left',

      };
    },
    mounted() {

    },
    methods: {

    }
  }

</script>

<style scoped>
  /** {*/
  /*  margin: 0;*/
  /*  padding: 0;*/
  /*}*/

  .acMsg {
    width: 100%;
    height: 100%;
    /*flex: 1;*/
    background: #ffffff;
  }

  .title {
    width: 12.5rem;
    height: 4.375rem;
    font-size: 26px;
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

</style>
