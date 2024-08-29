<template>
  <div class="page_content">
    <div class="page_body">
      <div class="page_main">
        <router-view></router-view>
      </div>
    </div>
  </div>

</template>
<script>
  import portalHeader from './components/portalHeader.vue'
  import portalBottom from './components/portalBottom.vue'
  import portalRight from './components/portalRight.vue'
  export default {
    components: {
      portalHeader,
      portalBottom,
      portalRight
    },
    created(){
      this.pathChange(this.$route.path)
    },
    watch: {
      $route() {
        // console.log(this.$route)
        if(this.$route.redirectedFrom){
          this.pathChange(this.$route.redirectedFrom)
        }else {
          this.pathChange(this.$route.path)
        }

      },
    },
    mounted() {
    },
    data() {
      return {
        showIframe:false,
        iframeUrl:"",
        activeIndex:"/talentApartment"
      }
    },
    methods: {

      toPath(index){
        this.pathChange(index)
        this.$refs.pHeader.refreshData()
        this.$refs.pHeader.showLogin=true
        // if(index.indexOf("/talentPost")>=0){
        //   this.showIframe=true
        //   this.iframeUrl="https://yizhan.lzjczl.com"
        //   // this.iframeUrl="http://10.82.12.206:7777"
        //
        //   // window.open("http://101.206.141.234:7777")
        // }else if(index.indexOf("/rcgy")>=0){{
        //   this.showIframe=true
        //   this.iframeUrl="https://yizhan.lzjczl.com/rcgy"
        // }}else{
        //   this.showIframe=false
        //   this.$router.push({ path: index });
        // }
        this.$router.push({ path: index });
      },
      pathChange(index){
        this.showIframe = index.indexOf("/talentPost") >= 0 || index.indexOf("/talentApartment") >= 0;
        this.activeIndex=index
      },
    }
  }
</script>
<style lang="scss">
  .page_content {
    height: 100%;
    width: 100%;
    position: relative;
    min-width: 75rem;

    .postion_menu{
      position: absolute;
      top:0;
      width: 100%;
      height:4.56rem;
      .page_menu{
        margin: 0 auto;
        width: 75rem;
        background-size:100% 100%;
        padding: 23px 0 0 320px;

      }
    }

    .el-menu{
      /*background-image: url("~@/assets/images/portal/header/menu_bg.png");*/
      /*background-size:100% 100%;*/
      background-color: transparent;
      height: 100%;
      width: 75rem;
      .el-menu-item{
        height: 100%;
        line-height: 4.56rem
      }
    }
    .el-menu--horizontal{
      border-bottom: solid 0px #e6e6e6;
      width: 47.5rem;
      /*padding: 0 18.75%;*/



      .el-menu-item, .el-submenu{
        height: 100%;
        font-size: 16px;
        margin: 0;
        margin-right:4%;
        padding:0;
        background-color:transparent !important;
        border-bottom: 0px solid transparent;
        font-weight: 700;

        .el-submenu__title {
          font-size: 16px;
          height: 100%;
          line-height: 4.56rem;
          border-bottom: 0px solid transparent;
          padding: 0 0px;
          margin-right:4%;
          /*color: transparent !important;*/
        }
        .el-submenu__title:hover {
          background-color: transparent;
        }
        .el-submenu__icon-arrow {
          color: #fff;
        }

      }
      .is-active{
        border-bottom: 3px solid #ffffff;
      }
    }



    .page_body{
      width: 100%;
      height: 100%;

      .page_main{
        width: 100%;
        height: 100%;
      }
    }
  }

  .el-menu {
    background-color: transparent;
    text-align: center;
  }

  .text-span1{
   margin-top: 20px;
   font-size: 16px;
   font-family: "PingFang SC", "PingFang SC-Bold";
   font-weight: 500;
   color: #212121;
  }
  .text-title1{
    font-size: 22px;
    font-family: "PingFang SC", "PingFang SC-Bold";
    font-weight: 700;
    color: #212121;
    margin-bottom: 5px;
  }




</style>
<style lang="scss">
  .main_content{
    height: 100%;
    width: 100%;
    text-align: center;
    .main_bg_position{
      background-size:100% 100%;
      background-attachment: fixed;
      height: 100%;
      width: 100%;
      position: fixed;
      bottom: 0;
      z-index: -1;
    }
    .flex_ld{
      display:flex;
      justify-content:space-between
    }
    .logo_size{
      background-size:100% 100%;
      height: 1.75rem;
      width: 29.875rem;
      margin-left:auto;
      margin-right: auto;
      margin-top: 2.87rem;
    }
    .main_margin{

      margin-top: 1.75rem;
      width: 75rem;
      margin-right: auto;
      margin-left: auto;
    }
  }

</style>
<style lang="scss" scoped>
  .page_content {
    .page_body {
      .page_menu_bg{
        /*background-image: url("~@/assets/images/portal/header/menu_bg.png");*/
      }


    }

  }
  .el-menu--popup{
    .el-menu-item{
      color: #000000 !important;
      /*background-color: transparent ;*/
    }
    .el-menu-item.is-active {
      color: #1890ff !important;
    }
  }

</style>
