<template>
  <div class="center_content">
    <div class="banner-container index">
      <div class="banner-main">
        <div class="banner-title">个人中心</div>
        <div class="banner-desc">欢迎来到用户中心！</div>
        <div class="nav-menu col-6">
<!--          <a class="nav-item" :class="[titleIndex == 1||titleIndex == 6 ? 'active':'']" @click="titleClick(1)" @mouseenter="enter()">首页</a>-->

<!--          <a @click="titleClick(2)" :class="[titleIndex == 2 ? 'active':'']" class="nav-item" @mouseenter="enter()">个人业务管理</a>-->
<!--          <a @click="titleClick(3)" :class="[titleIndex == 3 ? 'active':'']" class="nav-item" @mouseenter="enter()">个人服务管理</a>-->
          <a @click="titleClick(4)" :class="[titleIndex == 4 ? 'active':'']" class="nav-item" @mouseenter="enter()">个人安居管理</a>
<!--          <a @click="titleClick(5)" :class="[titleIndex == 5 ? 'active':'']" class="nav-item" @mouseenter="enter()">个人招聘管理</a>-->
        </div>
<!--        首页-->
        <div v-if="titleIndex === 1" class="my-info">
          <div class="basic">
            <div class="avatar-wrapper"><img src="../../../image/userCenter/tx.svg">
              <div title="" class="username">
                {{ this.homeData.nickName }}
              </div>
            </div>
            <div class="detail-wrapper">
              <div class="detail level">
                <div class="detail-title">邮箱号码</div>
                <div class="detail-content">{{ this.homeData.email }}</div>
              </div>
              <div class="detail code">
                <div class="detail-title">证件号码</div>
                <div class="detail-content">{{ this.homeData.idCard }}</div>
              </div>
            </div>
            <div class="link-wrapper">
              <a @click="titleClick(6)" class="link"><img
                src="../../../image/userCenter/user-info.svg">
                <div>基本信息</div>
              </a>
            </div>
          </div>
          <div class="matter-wrapper">
            <div class="wrapper-title">我的申报信息</div>
            <div class="matter-container">
              <a href="" class="matter">
                <div>
                  <div class="matter-title">已提交</div>
                  <div class="matter-desc">已提交的事项<span class="highlight">{{ declarationInfo.SUBMITTED }}</span></div>
                </div>
                <img src="../../../image/userCenter/matter.svg">
              </a>
              <a href="" class="matter red">
                <div>
                  <div class="matter-title">审核中</div>
                  <div class="matter-desc">审核中的事项<span class="highlight">{{ declarationInfo.UNDER_REVIEW }}</span></div>
                </div>
                <img src="../../../image/userCenter/express.svg">
              </a>
            </div>
            <div class="matter-container" style="margin-top: 16px;">
              <a href="" class="matter yellow">
                <div>
                  <div class="matter-title">已完结</div>
                  <div class="matter-desc">已完结的事项<span class="highlight">{{ declarationInfo.FINISHED }}</span></div>
                </div>
                <img src="../../../image/userCenter/consult.svg">
              </a>
              <a href="" class="matter green">
                <div>
                  <div class="matter-title">事项评价</div>
                  <div class="matter-desc">待评价的事项<span class="highlight">3</span></div>
                </div>
                <img src="../../../image/userCenter/rate.svg">
              </a>
            </div>
          </div>
          <div class="cert-wrapper">
            <div class="wrapper-title">我的求职信息</div>
            <a href="" class="cert">
              <img src="../../../image/userCenter/zhengzhao.svg">
              <div class="name">求职意向</div>
              <div class="highlight">3</div>
            </a>
            <a href="" class="cert"><img
              src="../../../image/userCenter/zhengming.svg">
              <div class="name">投递次数</div>
              <div class="highlight">15</div>
            </a>
            <a href="" class="cert">
              <img src="../../../image/userCenter/yinzhang.svg">
              <div class="name">受邀次数</div>
              <div class="highlight">10</div>
            </a>
          </div>
        </div>
<!--        个人业务管理,个人安居管理-->
        <div v-if="2<=titleIndex&&titleIndex<=4" style="margin-bottom: 50px">
          <component :is="isComponent" ></component>
        </div>
<!--        基本信息-->
        <div v-if="titleIndex == 6" class="my-info">
          <accountMsg :index="index"></accountMsg>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import portalHeader from '../portalHeader.vue'
import accountMsg from "@/views/portal/components/portalCenter/basisMsg/accountMsg.vue";
import Matter from './matter.vue'
import PersonService from './personService.vue'
import PersonLive from './personLive.vue'
import cache from "@/plugins/cache";
import {decrypt, encrypt, sm4Dncrypt} from "@/utils/encrypt";
import {getEnterpriseStatistics, getPersonStatistics} from "@/api/portal/rcfw";
import {getToken} from "@/utils/auth";

export default {

  name: 'portalCenter',
  components: {
    portalHeader,
    accountMsg
  },
  props: {},
  mounted() {
    if(this.$route.query.titleIndex){
      this.titleIndex = this.$route.query.titleIndex;
      this.index=this.$route.query.index;
    }
    this.init()
  },
  computed: {
    isComponent() {
      return this.componentsList[this.componentsName[this.titleIndex-1]];
    }
  },
  data() {
    return {
      swShow:false,
      zlShow:false,
      name:'首页',
      titleIndex:4,
      index:"0",
      homeData: {


      },
      //申报信息
      declarationInfo:{
        SUBMITTED: 3,//已提交
        FINISHED: 0,//已完结
        UNDER_REVIEW: 2 //审核中
      },
      componentsList: {
        matter: Matter,//需要引入具体的组件
        personService: PersonService,
        personLive: PersonLive
      },
      componentsName: ["matter","matter","personService","personLive","matter"],
    }
  },
  methods:{
    init(){
      this.getDate()
      this.getUser()
    },
    getUser(){
      //获取用户信息
      this.homeData=JSON.parse(sm4Dncrypt(cache.local.get("portal_user")))
    },
    getDate(){
      //个人中心申报信息
      /*getPersonStatistics().then(res=>{
        if (res.code===0){
          this.declarationInfo=res.data[0]
        }
      })*/
    },
    //我的事务鼠标移动事件
    swEnter(){
      this.swShow = true;
      this.zlShow = false;
    },
    //鼠标离开事件
    leave(){
      this.swShow = false;
      this.zlShow = false;
    },
    //我的证件资料鼠标移动事件
    zlEnter(){
      this.swShow = false;
      this.zlShow = true;
    },
    //鼠标移动事件
    enter(){
      this.swShow = false;
      this.zlShow = false;
    },
    titleClick(index){
      this.titleIndex = index;
    }
  },

}
</script>

<style lang="scss" scoped>
.center_content {
  width: 100%;
  height: 100%;

  .banner-container {
    min-width: 1200px;
    background: url(../../../image/userCenter/user-center.jpg) no-repeat 50%/cover;
    background-position-y: top;
    height: 302px;
  }
  .banner-container.index {
    /*min-height: 750px;*/
    height: 100%;
  }
  .banner-container .banner-main {
    width: 1200px;
    height: 100%;
    margin: 0 auto;
  }
  .banner-container .banner-main:after, .banner-container .banner-main:before {
    display: table;
    content: "";
    clear: both;
  }
  .banner-container .banner-title {
    margin-top: 40px;
    margin-bottom: 8px;
    font-size: 42px;
    color: #0d1c28;
    line-height: 68px;
  }
  .banner-container .banner-desc {
    margin-bottom: 40px;
    font-size: 16px;
    color: #3d4953;
    line-height: 26px;
  }
  .nav-menu {
    margin-bottom: 36px;
  }
  .nav-menu .nav-item.active, .nav-menu .nav-item:hover {
    color: #4293f4;
    border-bottom: 3px solid #4293f4;
  }
  .nav-menu .nav-item {
    width: 20%;
    height: 48px;
    font-size: 14px;
    color: #0d1c28;
    border: 1px solid #e7ebf0;
  }
  .nav-menu .nav-icon, .nav-menu .nav-item {
    display: inline-block;
    line-height: 48px;
    text-align: center;
    background: #fff;
  }
  .nav-menu .nav-title .gd-icon {
    display: inline-block;
    margin-left: 4px;
    -webkit-transition: -webkit-transform .2s;
    transition: -webkit-transform .2s;
    transition: transform .2s;
    transition: transform .2s,-webkit-transform .2s;
  }
  .gd-icon:before {
    font-family: iconfont!important;
    font-style: normal;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  }
  .icon-down:before {
    content: "\E610";
  }
  a {
    transition: color .2s;
    color: #4293f4;
    background: transparent;
    text-decoration: none;
    outline: none;
    cursor: pointer;
  }
  .nav-menu .gd-poptips {
    display: block;
  }
  .gd-poptips {
    position: relative;
    display: inline-block;
    font-size: 0;
  }
  .nav-menu .gd-poptips-text {
    padding: 0;
  }
  .gd-poptips-text {
    padding: 10px 15px;
    font-size: 14px;
  }
  .gd-poptips-box {
    min-width: 150px;
    position: absolute;
  }
  .nav-menu .gd-poptips-inner {
    width: 100%!important;
    padding: 0!important;
  }
  .gd-poptips-inner {
    background-color: #fff;
    box-shadow: 0 2px 15px rgb(9 41 77 / 15%);
  }
  .gd-poptips-content {
    line-height: 22px;
    font-size: 14px;
    color: #0d1c28;
  }
  .wdsx:hover {
    background-color: #F6F6F6;
  }
  .zxts:hover {
    background-color: #F6F6F6;
  }
  .wdpj:hover {
    background-color: #F6F6F6;
  }
  .wdwl:hover {
    background-color: #F6F6F6;
  }
  .wdzz:hover {
    background-color: #F6F6F6;
  }
  .wdzm:hover {
    background-color: #F6F6F6;
  }
  .wdyz:hover {
    background-color: #F6F6F6;
  }
  .nav-menu .gd-poptips-content .sub-title:not(:last-child) {
    border-bottom: 1px solid #e7ebf0;
  }
  .nav-menu .gd-poptips-content .sub-title {
    display: block;
    font-size: 14px;
    color: #0d1c28;
    text-align: center;
    line-height: 24px;
    padding: 12px 0;
  }
  .nav-menu .gd-poptips-box-bottom {
    left: 0;
  }
  .nav-menu .gd-poptips-box {
    z-index: 99;
    width: 100%;
    margin-left: 0!important;
  }
  .gd-poptips-box-bottom {
    top: 100%;
    left: 50%;
    text-align: center;
  }
  .gd-poptips-box-hide {
    visibility: hidden;
  }
  .gd-poptips-box-show {
    visibility: visible;
  }
  .gd-poptips-box {
    min-width: 150px;
    position: absolute;
  }
  .my-info {
    width: 1200px;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    margin-bottom: 24px;
    margin-top: 40px;
    border: 1px solid #e7ebf0;
    -webkit-box-shadow: 0 4px 10px 0 #e6eef2;
    box-shadow: 0 4px 10px 0 #e6eef2;
    border-radius: 4px;
  }
  .my-info .basic {
    width: 320px;
    padding: 24px;
    background-image: -webkit-gradient(linear,left top,left bottom,from(#c3e1ff),color-stop(23%,#e1f0ff),color-stop(85%,#fafcff));
    background-image: linear-gradient(180deg,#c3e1ff,#e1f0ff 23%,#fafcff 85%);
  }
  .my-info .avatar-wrapper {
    text-align: center;
  }
  .my-info .avatar-wrapper .username {
    margin-top: 6px;
    font-size: 18px;
    font-weight: 700;
    color: #0d1c28;
    line-height: 28px;
  }
  .my-info .detail-wrapper {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    margin-top: 14px;
    margin-bottom: 24px;
  }
  .my-info .detail-wrapper .detail {
    position: relative;
    -ms-flex: 50%;
    flex: 50%;
  }
  .my-info .detail-wrapper .detail-title {
    font-family: PingFangSC-Regular;
    font-size: 14px;
    color: #9ea4a9;
    line-height: 24px;
  }
  .my-info .detail-wrapper .detail-content {
    display: inline-block;
    font-family: PingFangSC-Regular;
    font-size: 14px;
    color: #0d1c28;
    text-align: right;
    line-height: 24px;
    padding-right: 8px;
  }
  .my-info .detail-wrapper .detail.code {
    padding-left: 18px;
    border-left: 1px solid #beddfc;
  }
  .my-info .detail-wrapper .detail .detail-content {
    overflow: hidden;
    text-overflow: ellipsis;
    width: 130px;
  }
  .my-info .link-wrapper {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
  }
  .my-info .link-wrapper .link {
    -webkit-box-flex: 1;
    -ms-flex-positive: 1;
    flex-grow: 1;
    text-align: center;
    padding: 21px 0;
    background: #e4f2fe;
    border-radius: 2px;
    font-weight: 700;
    font-size: 14px;
    color: #4293f4;
    line-height: 24px;
  }
  .my-info .matter-wrapper {
    -webkit-box-flex: 1;
    -ms-flex-positive: 1;
    flex-grow: 1;
    padding: 24px;
    background: #fff;
  }
  .my-info .wrapper-title {
    margin-bottom: 16px;
    font-weight: 700;
    font-size: 18px;
    color: #0d1c28;
    line-height: 28px;
  }
  .my-info .matter-container {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
  }
  .my-info .matter:nth-child(odd) {
    margin-right: 16px;
  }
  .my-info .matter {
    -webkit-box-flex: 1;
    -ms-flex: 1;
    flex: 1;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    -webkit-box-pack: justify;
    -ms-flex-pack: justify;
    justify-content: space-between;
    padding: 22px 24px;
    background-image: linear-gradient(128deg,#fff,#edf5ff);
    border: 1px solid #daebfb;
    -webkit-box-shadow: 0 4px 10px 0 #f8f9fb;
    box-shadow: 0 4px 10px 0 #f8f9fb;
    border-radius: 2px;
  }
  .my-info .matter-title {
    margin-bottom: 8px;
    font-weight: 700;
    font-size: 16px;
    color: #0d1c28;
    line-height: 26px;
  }
  .my-info .matter-desc {
    font-size: 14px;
    color: #5c6166;
    line-height: 24px;
  }
  .my-info .matter-desc .highlight {
    padding-left: 8px;
    color: #00002a;
  }
  .my-info .matter.green {
    background-image: linear-gradient(128deg,#fff,#e6f4ef);
    border: 1px solid #e5eeeb;
  }
  .my-info .matter.yellow {
    background-image: linear-gradient(128deg,#fff,#fff7e9);
    border: 1px solid #f4e9d6;
  }
  .my-info .matter.red {
    background-image: linear-gradient(128deg,#fff,#fff1ec);
    border: 1px solid #f6ebe5;
  }
  .my-info .cert-wrapper {
    padding: 24px;
    background: #f8fbfd;
    -webkit-box-shadow: inset 1px 0 0 0 #f1f2f3;
    box-shadow: inset 1px 0 0 0 #f1f2f3;
  }
  .my-info .cert-wrapper .cert {
    width: 220px;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    -webkit-box-pack: justify;
    -ms-flex-pack: justify;
    justify-content: space-between;
    padding: 12px 24px;
    background-image: linear-gradient(137deg,#fff,#f9fcff);
    border: 1px solid #e7ebf0;
    -webkit-box-shadow: 0 4px 10px 0 #f8f9fb;
    box-shadow: 0 4px 10px 0 #f8f9fb;
    border-radius: 2px;
    color: #0d1c28;
  }
  .my-info .cert-wrapper .cert .name {
    margin-right: 35px;
  }
  .my-info .cert-wrapper .cert .highlight {
    font-weight: 700;
  }
}
</style>
