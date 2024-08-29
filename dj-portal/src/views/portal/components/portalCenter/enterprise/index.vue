<template>
  <div class="center_content">
    <div class="banner-container index">
      <div class="banner-main">
        <div class="banner-title">企业中心</div>
        <div class="banner-desc">欢迎来到企业中心！</div>
        <div class="nav-menu col-6">
<!--          <a @click="titleClick(1)" :class="[titleIndex == 1||titleIndex == 7 ? 'active':'']" class="nav-item"  @mouseenter="enter()">首页</a>-->
<!--          <a @click="titleClick(2)" :class="[titleIndex == 2 ? 'active':'']" class="nav-item" @mouseenter="enter()">用户绑定管理</a>-->
<!--          <a @click="titleClick(3)" :class="[titleIndex == 3 ? 'active':'']" class="nav-item" @mouseenter="enter()">业务申报管理</a>-->
          <a @click="titleClick(6)" :class="[titleIndex == 6 ? 'active':'']" class="nav-item" @mouseenter="enter()">人才公寓管理</a>
<!--          <a @click="titleClick(4)" :class="[titleIndex == 4 ? 'active':'']" class="nav-item" @mouseenter="enter()">通知公告管理</a>-->
<!--          <a @click="titleClick(5)" :class="[titleIndex == 5 ? 'active':'']" class="nav-item" @mouseenter="enter()">职位招聘管理</a>-->

        </div>
        <div v-if="titleIndex == 1" class="my-info">
          <div class="basic">
            <div class="avatar-wrapper"><img src="../../../../image/userCenter/tx.svg">
              <div title="" class="username">
                {{ coInfo.coName }}
              </div>
            </div>
<!--            <div class="avatar-wrapper">-->
<!--              <div title="" class="username" style="font-size: 15px;">-->
<!--                国有资产监督管理委员会-->
<!--              </div>-->
<!--            </div>-->
            <div class="detail-wrapper">
              <div class="detail level">
                <div class="detail-title">
                  {{ coTypeName[coInfo.coType] }}
                </div>
              </div>
              <div class="detail code">
                <div class="detail-title">
                  {{ coInfo.coScale }}
                </div>
              </div>
            </div>
            <div class="link-wrapper">
              <a @click="titleClick(7)" class="link"><img
                      src="../../../../image/userCenter/user-info.svg">
                <div>企业信息管理</div>
              </a>
            </div>
          </div>
          <div class="matter-wrapper">
            <div class="wrapper-title">企业工作台</div>
            <div class="matter-container">
              <a @click="titleClick(2)" class="matter">
                <div>
                  <div class="matter-title">待审绑定申请</div>
                  <div class="matter-desc">待审核的员工申请
                    <span class="highlight">{{ binding }}</span>
                  </div>
                </div>
                <img src="../../../../image/userCenter/matter.svg">
              </a>
              <a href="" class="matter red">
                <div>
                  <div class="matter-title">待审简历</div>
                  <div class="matter-desc">待审核的求职简历<span class="highlight">1</span></div>
                </div>
                <img src="../../../../image/userCenter/express.svg">
              </a>
            </div>
            <div class="matter-container" style="margin-top: 16px;">
              <a href="" class="matter yellow">
                <div>
                  <div class="matter-title">待审事项申请</div>
                  <div class="matter-desc">待审核的事项申请<span class="highlight">{{ pendingApproved }}</span></div>
                </div>
                <img src="../../../../image/userCenter/consult.svg">
              </a>
              <a href="" class="matter green">
                <div>
                  <div class="matter-title">近期已发邀请函</div>
                  <div class="matter-desc">近期已发送的邀请函<span class="highlight">3</span></div>
                </div>
                <img src="../../../../image/userCenter/rate.svg">
              </a>
            </div>
          </div>
          <div class="cert-wrapper">
            <div class="wrapper-title">企业数据</div>
            <a @click="titleClick(2)" class="cert">
              <img src="../../../../image/userCenter/zhengzhao.svg">
              <div class="name">已绑用户</div>
              <div class="highlight">{{ bound }}</div>
            </a>
            <a href="" class="cert"><img
                    src="../../../../image/userCenter/zhengming.svg">
              <div class="name">在招岗位</div>
              <div class="highlight">15</div>
            </a>
            <a href="" class="cert">
              <img src="../../../../image/userCenter/yinzhang.svg">
              <div class="name">申报事项</div>
              <div class="highlight">10</div>
            </a>
          </div>
        </div>
<!--        企业信息管理-->
        <div v-if="titleIndex == 7" class="my-info">
          <company-msg></company-msg>
        </div>
<!--        用户绑定管理-->
        <div v-if="titleIndex == 2" class="my-info">
          <bangding></bangding>
        </div>
        <!--        业务申报管理-->
        <div v-if="titleIndex == 3" class="my-info">
          <Matter></Matter>
        </div>
        <!--        人才公寓管理-->
        <div v-if="titleIndex == 6" class="my-info">
          <TalentApartment></TalentApartment>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
  import portalHeader from '../../portalHeader.vue'
  import companyMsg from "@/views/portal/components/portalCenter/enterprise/companyMsg/companyMsg.vue";
  import Matter from './matter.vue'
  import TalentApartment from './talentApartment.vue'
  import bangding from "@/views/portal/components/portalCenter/enterprise/userBinding/bangding.vue";
  import {getEnterpriseStatistics, getPersonStatistics} from "@/api/portal/rcfw";
  import {getCompanyEmployee, queryCoInfo} from "@/api/portal/employee";
  export default {

    name: 'portalCenter',
    components: {
      portalHeader,
      companyMsg,
      Matter,
      bangding,
      TalentApartment,
    },
    props: {},
    mounted() {
      if(this.$route.query.titleIndex){
        this.titleIndex = this.$route.query.titleIndex;
      }
      this.init()
    },
    data() {
      return {
        swShow:false,
        zlShow:false,
        name:'首页',
        coInfo: {
          coName: "数字泸州产业投资集团",

          coScale: "150-500", //规模
          coType: "1", //企业性质
        },
        coTypeName: ['','有限责任公司','股份有限公司','集体所有制','股份合作制','国有企业','个体工商户','个人独资企业','有限合伙','普通合伙','外商投资企业','港、澳、台','联营企业','私营企业'],
        titleIndex:6,
        //待审绑定申请
        binding: null,
        //待审绑定申请
        bound: null,
        //待审批事项
        pendingApproved: null,
      }
    },
    methods:{
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
      },
      init(){
        this.getDate()
      },
      getDate(){
        //企业信息
        /*queryCoInfo({id : '1627610894397812738'}).then(res=>{
          if (res.success){
            this.coInfo=res.data
          }
        })*/

        //企业中心申报信息
        /*getEnterpriseStatistics().then(res=>{
          if (res.code===0&&res.data.length>0){
            this.pendingApproved=res.data[0].PENDING_APPROVAL
          }
        })*/
        getCompanyEmployee().then(res=>{
          if (res.success&&res.data.length>0){
            let data = res.data;
            this.binding = data.filter(item=>item.status==0).length;
            this.bound = data.filter(item=>item.status==1).length;
          }
        })
      },
    },

  }
</script>

<style lang="scss" scoped>
  .center_content {
    width: 100%;
    height: 100%;

    .banner-container {
      min-width: 1200px;
      background-image: url('~@/assets/images/portal/center/enterprise_center.jpg');
      background-size: 100% 100%;

      //background: url(../../../../image/userCenter/user-center.jpg) no-repeat 50%/cover;
      //background: url(../../../../image/userCenter/user-center.jpg) no-repeat 50%/cover;
      background-position-y: top;
      height: 302px;
    }
    .banner-container.index {
      min-height: 750px;
      height: 100%;
    }
    .banner-container .banner-main {
      width: 1200px;
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
      width: 16.66%;
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
      text-align: center;
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
