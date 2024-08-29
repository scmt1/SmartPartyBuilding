<template>
  <div class="right-wrap">
    <div v-for="(item,index) in tpList" :key="index"
         v-popover="'popover-hover' + index"
         @click="rt_click(item.link)"
         class="rt_item">
      <span class="icon" :style='"background-image:url("+item.url+");"'></span>
      <div>{{item.title}}</div>
    </div>

    <el-popover
      placement="left"
      ref="popover-hover1"
      width="400"
      trigger="hover">
      <div class="pop_wrap">
        <div class="pop_box">
          <p>酒城e通APP</p>
          <img src="@/assets/images/portal/bottom/jcet.png">
        </div>
        <div class="pop_box">
          <p>酒城e通微信小程序</p>
          <img src="@/assets/images/portal/bottom/jcet-wx.jpg">
        </div>
      </div>
    </el-popover>

  </div>
</template>

<script>
  export default {
    name: 'portalRight',
    data() {
      return {
        // 图片列表
        tpList:[
          {
            id:0,
            title:"智能会话",
            link:process.env.VUE_APP_YSZJ_ADDRESS+"/chat/chat.html",
            url:require("@/assets/images/portal/right/znkf.png"),
          },{
            id:1,
            title:"掌上服务",
            url:require("@/assets/images/portal/right/zsfw.png"),
          },
         /* {
            id:2,
            title:"人才聚焦",
            link:"",
            url:require("@/assets/images/portal/right/jcrcjjxd.png"),
          },*/
        ],
      }
    },
    methods:{
      rt_click(link){
        if(link){
          window.open(link)
        }
      }
    },


  }
</script>

<style lang="scss" scoped>
  .right-wrap{
    position: fixed;
    bottom: 50%;
    margin-bottom: -4.6875rem;
    width: 4.5rem;
    -webkit-box-shadow: 0 2px 5px 0 hsl(0deg 0% 70% / 30%);
    box-shadow: 0 2px 5px 0 hsl(0deg 0% 70% / 30%);
    border: 1px solid #ebebeb;
    font-size: 0;

    right: 0;
    background: red;
    -webkit-transition: all .5s;
    transition: all .5s;
    z-index: 10;

    .rt_item{
      text-align: center;
      color: #333;
      font-size: 14px;
      /*height: 4.6875rem;*/
      padding: 0 5px;
      cursor: pointer;
      background: #fff;

      .icon{
        display: inline-block;
        width: 2.5rem;
        height: 2.5rem;
        margin-top: 5px;
        background-size: 100% 100%;
      }
    }
  }
  .pop_wrap{
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -ms-flex-pack: distribute;
    justify-content: space-around;

    .pop_box{
      text-align: center;

      img{
        width: 8.375rem;
        height: 8.375rem;
        margin: 5px;
      }
    }
  }

</style>
