<template>
  <div class="main_content">

    <!--    背景图-->
    <div class="main_bg_position main_bg"></div>
    <!--    轮播背景图-->
    <div class="header_lb_position">
      <el-carousel indicator-position="none" style="height: 26.875rem;">
        <el-carousel-item v-for="(item,index) in backgroundImage" :key="index">
          <div :class="'header_ls_position ls_bg'+index"></div>
          <div :class="'header_bg_position '+'bg_width'+index" :style="{backgroundImage: 'url(' + item + ')'}"></div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!--    人才服务 · 一网通办-->
    <div class="main_margin home_hy">
      <el-image :src="titleImage"></el-image>
    </div>
    <!--    搜索-->
    <div class="home_ss main_margin" style="margin-top: 0.75rem;">
      <div class="ss_select flex_ld">
        <el-select
          value=""

          filterable
          remote
          reserve-keyword
          suffix-icon="el-icon-date"
          placeholder="请输入关键字查询"
          :remote-method="remoteMethod"
          size="medium"
          :loading="loading">
          <el-option
            v-for="item in options"
            @click.native="handleSelectIteam(item)"
            :key="item.value"
            :label="item.label"
            :value="item.value">
            <span>[ {{item.type}} ] </span>
            <span> {{item.label}}</span>
          </el-option>
        </el-select>
        <el-button class="ss_bt" @click="gotoQuery('searchView',JSON.stringify(options))">搜索</el-button>

      </div>
      <div class="ss_hot ">
<!--        <div class="hot_text"></div>-->
        <div class="hot_text">历史搜索:</div>
        <div class="hot_text2"
             v-for="item in oldSearch"
             style="cursor:pointer;"
             @click="toSearchView(item)">{{ item.label }}</div>
<!--        <div class="hot_text">人才就医</div>-->
<!--        <div class="hot_text">资格认证</div>-->
      </div>
    </div>

    <!--    推荐-->
    <div class="width-content home_tj">

      <div class="hot_list">
        <div class="box_pre">
          <el-image :src="heartImage"></el-image>
          <p>为您推荐</p>
        </div>
        <div style="width: 800px" class="hot_box">
          <div style="width: 200px;height: 56px;display: inline-block;vertical-align: top"
               v-for="(item,index) in hotList.slice(0,8)">
            <!--<div class="line-pre">
              <i class="status-point" />
            </div>-->
            <div style="display: inline-block;vertical-align: top;width: 180px;height: 56px;position: relative">
              <p @click="toNewDetail('/rcfw/web/view/view.html?SXBM='+item.url)"
                 :title="item.name">
                {{item.name}}
              </p>
              <el-image :src="hotImage" style="position: absolute;top: 18px;right: 51px;" v-if="index===0"/>
            </div>
          </div>
        </div>
      </div>
      <div>
        <el-image :src="tjImage" @click="goto('talentPost')" style="cursor: pointer"></el-image>
      </div>
    </div>



    <!--    轮播视频-->
<!--    <div class="home_lunbo">-->
<!--      <div class="lunbo_box">-->
<!--        <VideoPlay v-for="item in videos"-->
<!--          v-show="true"-->
<!--          :videoUrl="ADDRESS+item.url"-->
<!--          :videoCover="ADDRESS+item.cover"-->
<!--          width="100%"-->
<!--          height="225"-->
<!--          :autoplay="false"-->
<!--          :controls="true"-->
<!--          :loop="false"-->
<!--          :muted="false"-->
<!--          preload="auto"-->
<!--          :showPlay="true"-->
<!--          :playWidth="96"-->
<!--          zoom="fill"-->
<!--        />-->
<!--      </div>-->
<!--    </div>-->
    <!--    轮播图片-->
    <div class="home_lunbo">
      <div class="lunbo_box">
        <div class="lunbo_left">
          <el-carousel :interval="4000" type="card"  trigger="hover" :autoplay="false" @change="changeItem">
            <el-carousel-item v-for="item in videos">
              <VideoPlay
                         v-show="true"
                         :title="item.title"
                         :videoUrl="ADDRESS+item.url"
                         :videoCover="ADDRESS+item.cover"
                         width="100%"
                         height="225"
                         :autoplay="false"
                         :controls="true"
                         :loop="false"
                         :muted="false"
                         preload="auto"
                         :showPlay="true"
                         :playWidth="96"
                         zoom="fill"
              />
            </el-carousel-item>
          </el-carousel>
        </div>
        <div class="lunbo_right">
          <div v-for="(item,index) in tplbList" :key="index">
            <a @click="toNewDetail('/cms/article/'+item.id)"
               :title="item.highlightTitle">
              <h4>{{ item.title }}</h4>
            </a>
            <a @click="toNewDetail('/cms/article/'+item.id)"
               :title="item.highlightContent">
              <p>{{ item.highlightContent }}</p>
            </a>

          </div>
        </div>
      </div>
    </div>


    <!--    轮播图片-->
    <div class="home_lunbo">

      <div class="lunbo_box">
        <div class="lunbo_left">
          <el-carousel :interval="4000" type="card" height="300px" :autoplay="true" @change="changeItem">
            <el-carousel-item v-for="item in tplbList" :key="item.index">
              <!--              <a-->
              <!--                @click="toNewDetail('/cms/article/'+item.id)"-->
              <!--              >-->
              <!--                <el-image :src="ADDRESS+'/cms'+item.showImage"/>-->
              <!--              </a>-->
              <el-image :src="(item.showImage).indexOf('http')==0?item.showImage:ADDRESS+'/cms'+item.showImage"/>

            </el-carousel-item>
          </el-carousel>
        </div>
        <div class="lunbo_right">
          <div v-for="(item,index) in tplbList" :key="index" :class="{'lunbo_click':lbindex==index}">
            <a @click="toNewDetail('/cms/article/'+item.id)"
               :title="item.highlightTitle">
              <h4>{{ item.title }}</h4>
            </a>
            <a @click="toNewDetail('/cms/article/'+item.id)"
               :title="item.highlightContent">
              <p>{{ item.highlightContent }}</p>
            </a>

          </div>
        </div>
      </div>
    </div>


    <!--    通知公告-->
    <div class="width-content home_tz" style="margin-top: 0">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane v-for="(temp,num) in zctpTypes" :name="temp.name" :key="num">
          <div slot="label">
            <span>{{temp.label }}</span>
          </div>

          <div class="tz_card">
            <el-card class="card_item box_hover" v-for="(item,index) in temp.newList" :key="index">
              <a @click="toNewDetail('/cms/article/'+item.id)"
                 :title="item.title">
                <div class="tz_title" style="color: #303133">{{item.highlightTitle}}</div>
                <div class="tz_counent">{{item.created}}</div>
              </a>
            </el-card>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!--    轮播-->
    <div class="width-content home_lb">
      <el-carousel indicator-position="outside" :autoplay="true">
        <el-carousel-item>
          <div class="flex_ld" style="padding: 0 52px">
            <div v-for="(item,index) in lytpList" :key="index" class="lb_zq box_hover">
              <a :style='"background-image:url("+item.url+");"' @click="item.title==='人才就医'?toView(item.SXMC,item.SXBM):goto(item.path)">
                <div v-if="item.isNew" class="new"></div>
                {{item.title}}
              </a>
            </div>
          </div>

        </el-carousel-item>
      </el-carousel>
    </div>

    <!--    场景服务-->
    <div class="width-content home_yjs">
      <div class="home_title">
        <h3>场景服务</h3>
        <span class="title_more" @click="goto('type')">更多</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="8" v-for="(item,index) in yjstpList" :key="index" >
          <div class="yjs_card box_hover" :style='"background-image: url("+item.url+");"' @click="goto(item.path,item.param)">
            <div>{{item.title}}</div>
            <div>{{item.content}}</div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!--    个人服务,团队服务,企业服务-->
    <div class="home_fw">
      <el-tabs v-model="fwActiveName" @tab-click="handleClick">
        <el-tab-pane v-for="(temp,num) in fwTypes" :name="temp.name" :key="num">
          <div slot="label">
            <span>{{ temp.label }}</span>
          </div>

          <div class="fw-back-image">
            <div class="fw_cards">
              <div class="fw_card" v-for="(item,index) in temp.fwList.slice(0,6)" :key="index"
                   @click="toNewDetail('/rcfw/web/view/view.html?SXBM='+item.url)">
                <div style="margin-bottom: 20px">
                  <el-image style="height: 115px;" fit="scale-down"
                            :src="ADDRESS+'/ufapi/file/getimage?path='+item.image" alt=""/>
                </div>
                <div>
                  <span>{{item.title}}</span>
                </div>
              </div>
            </div>
          </div>

        </el-tab-pane>
      </el-tabs>

    </div>


  </div>
</template>

<script>
import {search, getPaginateByCategorySlug, getListByCategorySlug} from '@/api/portal/cms'
import { getHomePageData, getHomePageHotData } from '@/api/portal/rcfw'
import {oneaccessLogin} from "@/api/login";
import {encrypt} from "@/utils/encrypt";
import {getToken} from "@/utils/auth";
import VideoPlay from '@/components/Video/VideoPlay.vue'


export default {
  name: 'home',
  components: {
    VideoPlay
  },
  mounted() {

    this.initData()
  },
  data() {
    return {
      ADDRESS:process.env.VUE_APP_YSZJ_ADDRESS,
      //标题图片
      titleImage: require('@/assets/images/portal/home/rcfwbt_banner.png'),
      //轮播图片
      backgroundImage: [
        require('@/assets/images/portal/home/header_bg.png')
        // require('@/assets/images/portal/home/header_bg2.png'),
      ],
      //轮播视频
      videos: [
        {
          title:"泸州形象宣传片（中英文版）",
          url: "/cms/attachment/20230606/a876ad9a0c7a430b843a68676c8f832f.mp4",
          cover: "/cms/attachment/20230606/4c5dbe32c2454321add9051fa0a1a4ee.png",
        },
        {
          title:"泸州人才工作宣传片",
          url: "/cms/attachment/20230606/56db17851c5a43f58a70dfeb635502ce.mp4",
          cover: "/cms/attachment/20230606/741e6db48a424b298a79fdc997bb476a.png",
        },
        {
          title:"泸州校园招聘宣传片",
          url: "/cms/attachment/20230606/11ee9166ba394d70b3f587593d96d8ad.mp4",
          cover: "/cms/attachment/20230606/bc98345a3b0a4bfe93a0db1004b75c59.png",
        },
      ],
      //推荐图片
      heartImage: require('@/assets/images/portal/home/heart.png'),
      tjImage: require('@/assets/images/portal/home/qnyz.png'),
      hotImage: require('@/assets/images/portal/home/hot.png'),
      // 热门推荐列表
      hotList: [
        {
          name: '人才驿站',
          path: 'talentPost'
        },
        {
          name: '安家落户',
          path: 'talentSet'
        },
        {
          name: '酒城人才申报',
          path: ''
        },
        {
          name: '高层次人才子女入学申报',
          path: ''
        },
        {
          name: '技能认证',
          path: ''
        },
        {
          name: '人才公寓',
          path: 'talentApartment'
        },
        {
          name: '人才就医',
          path: ''
        },
        {
          name: '职业指导',
          path: ''
        }
      ],
      // 全局搜索内容
      options: [],
      value: "",
      // 全局搜索结果列表
      list: [],
      loading: false,
      states: [],
      divWidth: 0,
      //历史搜索
      oldSearch: [],

      //图片轮播
      tplbImages: [
        require('@/assets/images/portal/home/lunbo_1.png'),
        require('@/assets/images/portal/home/lunbo_2.png'),
        require('@/assets/images/portal/home/lunbo_3.png')
      ],
      // 轮播索引
      lbindex:0,
      tplbList: [

      ],

      // 场景服务轮播图片
      lytpList: [
        {
          id: 0,
          title: '人才落户',
          isNew: true,
          url: require('@/assets/images/portal/home/fl_rclh.png'),
          time: '2022-01-05',
          path: 'talentSet'
        }, {
          id: 1,
          title: '毕业生档案服务',
          isNew: true,
          url: require('@/assets/images/portal/home/fl_bysdagl.png'),
          time: '2022-01-05',
          path: 'fileService'
        }, {
          id: 2,
          title: '大学生就业创业',
          isNew: true,
          url: require('@/assets/images/portal/home/fl_jycy.png'),
          time: '2022-01-05',
          path: 'collegeWork'
        }, {
          id: 3,
          title: '人才就医',
          isNew: true,
          url: require('@/assets/images/portal/home/fl_rcjy.png'),
          time: '2022-01-05',
          SXMC: '医疗绿色通道预约',
          SXBM: 'YLBJ20230310001',
        }
      ],

      // 一件事图片
      yjstpList: [
        {
          id: 0,
          title: '高校毕业生',
          path: 'scene',
          param: 'first',
          content: '学历认证、事业单位、公务员招聘',
          url: require('@/assets/images/portal/home/yjs_bys.png')
        }, {
          id: 1,
          title: '高层次人才',
          path: 'scene',
          param: 'second',
          content: '购房补贴、奖励补贴、奖励补贴',
          url: require('@/assets/images/portal/home/yjs_gcc.png')
        }, {
          id: 2,
          title: '海外留学生',
          path: 'scene',
          param: 'third',
          content: '学历认证、资格审定',
          url: require('@/assets/images/portal/home/yjs_hwlx.png')
        }, {
          id: 3,
          title: '专业技术人才',
          path: 'scene',
          param: 'fourth',
          content: '职称评审、人才引进、户籍迁入',
          url: require('@/assets/images/portal/home/yjs_jsrc.png')
        }, {
          id: 4,
          title: '技能人才',
          path: 'scene',
          param: 'five',
          content: '技能培训、申报职业技能鉴定',
          url: require('@/assets/images/portal/home/yjs_jnrc.png')
        }, {
          id: 5,
          title: '博士后',
          path: 'scene',
          param: 'six',
          content: '博士后本人及配偶、子女随迁',
          url: require('@/assets/images/portal/home/yjs_bsh.png')
        }
      ],
      //个人服务,团队服务,企业服务
      //文字默认值
      fwActiveName: '001',
      //服务分类
      fwTypes: [],
      //文字默认值
      activeName: 'tzgg',
      //资讯分类
      zctpTypes: [
        {
          label: '通知公告',
          name: 'tzgg',
          newList:[]
        },
        {
          label: '人才政策',
          name: 'rczc',
          newList:[]
        },
        {
          label: '新闻资讯',
          name: 'xwzx',
          newList:[]
        },
      ],
    }
  },
  methods: {
    // 初始化数据
    initData() {
      this.getData()
    },

    async getData() {
      //历史数据
      this.oldSearch = JSON.parse(this.$cache.session.get('oldSearch'));

      //热点数据
      getHomePageHotData().then(res => {
        if (res.code === 0) {
          this.hotList = []
          res.data.forEach(item => {
            const obj = {
              name: item.SXMC,
              url: item.SXBM
            }
            this.hotList.push(obj)
          })
        }
      })
      //轮播图片文章


      //就业政策
      getListByCategorySlug({ "categorySlug":"jyzc","count":"3","hasThumbnail":"1" }).then(res=>{
        if(res.state==="ok"){
          this.tplbList=res.list
        }
      })

      //个人服务，团队服务，单位服务
      getHomePageData().then(res => {
        if (res.code === 0) {
          let Base64 = require('js-base64').Base64
          res.data.forEach(item => {
            const parent = this.fwTypes.find(c => c.name === item.SXLX)
            let label=""
            if (parent) {
              label=parent.label
              const obj = {
                image: Base64.encode(item.IMAGEURL),
                title: item.SXMC,
                url: item.SXBM
              }
              parent.fwList.push(obj)
            } else {
              label=item.SXLXMC
              const obj = {
                label: item.SXLXMC,
                name: item.SXLX,
                fwList: [{
                  image: Base64.encode(item.IMAGEURL),
                  title: item.SXMC,
                  url: item.SXBM
                }]
              }
              this.fwTypes.push(obj)
            }
            this.list.push({ type:label,value:item.SXBM, label:item.SXMC})

          })
        }
      })

      await getPaginateByCategorySlug({ 'categorySlug': 'tzgg', 'hasChildren': '0','pageSize':'4' }).then(res => {
        if (res.state === 'ok'&&res.page) {
          this.zctpTypes[0].newList = res.page.list
        }
      })
      //人才政策
      await getPaginateByCategorySlug({ 'categorySlug': 'rczc','hasChildren': '1','pageSize': '4' }).then(res => {
        if (res.state === 'ok'&&res.page) {
          this.zctpTypes[1].newList = res.page.list
        }
      })
      //新闻资讯
      await getPaginateByCategorySlug({ 'categorySlug': 'xwzx', 'hasChildren': '1', 'pageSize': '4' }).then(res => {
        if (res.state === 'ok'&&res.page) {
          this.zctpTypes[2].newList = res.page.list
        }
      })
    },
    //输入时索引
    remoteMethod(query) {
      if (query !== '') {
        this.loading = true
        search({"keyword":query}).then(res=>{
          // console.log("search",res)
          this.loading = false
          this.options = this.list.filter(item => {
            return item.label.toLowerCase()
              .indexOf(query.toLowerCase()) > -1
          })
          if(res.page&&res.page.list){
            res.page.list.forEach(item=>{
              this.options.push({ type:'新闻资讯',value:item.id, label:item.title})
            })
          }


          console.log("options",this.options)

        })
      } else {
        this.options = []
      }
    },
    //点击选项跳转
    handleSelectIteam(item){
      console.log('item111111111',item)
      debugger
      let oldSearch = JSON.parse(this.$cache.session.get('oldSearch'));
      if (oldSearch){
        oldSearch.reverse()
        oldSearch.push(item)
        oldSearch.reverse()
        var length = oldSearch.length;
        if (length>3){
          oldSearch.splice(3,length)
        }
      }else {
        oldSearch=[]
        oldSearch.push(item)
      }
      console.log('oldSearch',oldSearch)
      this.$cache.session.set('oldSearch',JSON.stringify(oldSearch));
      this.oldSearch=oldSearch

      this.toSearchView(item)
    },
    //历史搜索点击跳转
    toSearchView(item){
      if(item.type=="新闻资讯"){
        this.toNewDetail('/cms/article/'+item.value)
      }else{
        this.toNewDetail('/rcfw/web/view/view.html?SXBM='+item.value)
      }
    },

    //中部文字切换
    handleClick(tab, event) {
    },
    goto(name, params) {
      this.$router.push({ name: name, params: { data: params } })
    },
    //query防止刷新页面参数消失
    gotoQuery(name, params) {
      if (this.options!==undefined&&this.options.length>0){
        this.$router.push({ name: name, query: { data: params } })
      }
    },
    changeItem(index){
      this.lbindex=index
      console.log(index)
    },
    //人才就医跳转
    toView(SXMC,SXBM){
      /*if(!this.isTalent){
        this.dialogVisible=true
        return
      }*/
      if(this.$cache.local.get("isEnterprise")==1&&this.$cache.local.get("portal_company")){
        this.$confirm('您还未登录系统，请先登录系统？', '提示', {
          confirmButtonText: '登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {

          // this.$router.push(`/login?redirect=${routeUrl.href}`)
          oneaccessLogin(routeUrl.href)
        }).catch(() => {});
        return
      }

      let str = encrypt(JSON.stringify({SXMC: SXMC,SXBM: SXBM}))

      let routeUrl = this.$router.resolve({
        path: "/talentServiceView",
        query: {fwInfo:str}
      });
      if(getToken()){
        window.location.href=routeUrl.href;
      }else{

        this.$confirm('您还未登录系统，请先登录系统？', '提示', {
          confirmButtonText: '登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {

          // this.$router.push(`/login?redirect=${routeUrl.href}`)
          oneaccessLogin(routeUrl.href)
        }).catch(() => {});

      }

      // this.$router.push({path: '/talentServiceView',query:{title: title}})
    },

  }
}
</script>


<style lang="scss" scoped>

.main_content {
  .width-content {
    width: 75rem;
    margin-top: 1.75rem;
    margin-right: auto;
    margin-left: auto;
  }

  /*鼠标覆盖变色*/
  .box_hover:hover {

    transform: scale(1.08);
    /*transition-duration: 0.5s;*/
    /*transition:all .5s;*/
    /*background-color: #1c599d;*/
    /*border-color: #1c599d;*/
    color: #fff !important;
  }

  /*新 标签*/
  .new {
    position: absolute;
    width: 50px;
    height: 50px;
    z-index: 9;
    top: 0;
    left: 0;
    background: url("~@/assets/images/portal/home/new.png") no-repeat;
    background-size: 100%;
  }

  .el-col {
    margin-top: 20px;
  }

  /*标签统一样式*/
  .home_title {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-pack: justify;
    -ms-flex-pack: justify;
    justify-content: space-between;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;

    h3 {
      color: #1658a0;
      font-size: 24px;
      font-weight: 600;
      line-height: 1.4;
      cursor: pointer;
    }

    .title_more {
      height: 40px;
      background-color: #fff;
      border-radius: 3px;
      border: 1px solid #e6e6e6;
      color: #333;
      font-size: 16px;
      font-weight: 500;
      display: -webkit-box;
      display: -ms-flexbox;
      display: flex;
      padding: 0 20px 0 25px;
      -webkit-box-align: center;
      -ms-flex-align: center;
      align-items: center;
      cursor: pointer;
    }
  }

  .bg_width1 {
    width: 100% !important;
  }

  /*背景图片绝对定位*/
  .header_bg_position {
    background-size: 100% 100%;
    height: 26.875rem;
    position: absolute;
    width: 81.25rem;
    margin: 0 auto;
    z-index: -1;
    top: 0;
    left: 0;
    right: 0;
    min-width: 75rem;
  }

  .header_lb_position {
    background-size: 100% 100%;
    height: 26.875rem;
    position: absolute;
    width: 100%;
    z-index: -1;
    top: 0;
    left: 0;
    right: 0;
    min-width: 75rem;

    ::v-deep .el-carousel__container {
      position: relative;
      height: 430px;
    }
  }

  .ls_bg0 {
    background-image: url("~@/assets/images/portal/home/header_ls.png");
  }

  .ls_bg1 {
    background-image: url("~@/assets/images/portal/home/header_ls2.png");
  }

  /*拉伸背景*/
  .header_ls_position {
    background-size: 100% 100%;
    height: 26.875rem;
    position: absolute;
    width: 100%;
    margin: 0 auto;
    z-index: -2;
    top: 0;
  }


  /*背景色*/
  .main_bg {
    /*background-image: url("~@/assets/images/portal/home/home_bg.png");*/
    background-color: #ffffff;
  }

  /*一网通办*/
  .home_hy {
    margin-top: 55px;
    text-align: center;
    /*width: 353px;*/
    /*height: 31px;*/
    font-size: 30px;
    /*font-family: Source Han Sans CN, Source Han Sans CN-Medium;*/
    font-family: microsoft yahei;
    /*font-weight: bold;*/
    color: #ffffff;
    line-height: 30px;
    letter-spacing: -0.64px;
    text-shadow: 0px 3px 7px 0px #0f48a9;
  }

  /*搜索*/
  .home_ss {
    margin-bottom: 111px;
    text-align: left;

    .ss_select {
      margin: 0 auto;
      width: 40.9375rem;
      height: 3.25rem;

      .el-select {
        /*border: 1px solid #ed530f;*/
        width: 40.9375rem;
        height: 3.25rem;
        /*opacity: 0.74;*/
        font-size: 20px;
        background: #f0f8fd;
        /*border-radius: 10px;*/

        ::v-deep .el-input {
          height: 100%;

          .el-input__inner {
            height: 100% !important;
            border-radius: 0;
          }
        }
      }

      .ss_bt {
        width: 5rem;
        height: 3.25rem;
        background-color: #FF8F1F;
        color: #ffffff;
        font-size: 18px;
      }

      .el-button {
        border: 0 solid #DCDFE6;
        border-radius: 0;
      }
    }

    .ss_hot {
      display: flex;
      justify-content: center;
      flex-wrap: wrap;
      color: #fff;
      margin-top: 15px;

      .hot_text {
        font-size: 18px;
        margin-right: 16px;
      }
      .hot_text2 {
        font-size: 18px;
        margin-right: 16px;
        width: 120px;
        display: -webkit-box;/*作为弹性伸缩盒子模型显示*/
        -webkit-line-clamp: 1; /*显示的行数；如果要设置2行加...则设置为2*/
        overflow: hidden; /*超出的文本隐藏*/
        text-overflow: ellipsis; /* 溢出用省略号*/
        -webkit-box-orient: vertical;/*伸缩盒子的子元素排列：从上到下*/
      }
    }


  }

  /*推荐*/
  .home_tj {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 20px;
    -webkit-box-align: center;
    -ms-flex-align: center;
    width: 1200px;
    height: 113px;

    .hot_list {
      box-shadow: 0.42px 5.99px 29.44px 4.48px rgba(219, 219, 220, 0.41);
      width: 917px;
      height: 113px;
      text-align: left;
      display: flex;
      justify-content: space-between;
      align-items: center;

      .box_pre {
        background-color: #1161CF;
        padding: 19px 0 0;
        width: 117px;
        height: 113px;
        text-align: center;

        p {
          font-size: 20px;
          font-family: 'PingFang SC', 'PingFang SC-Bold';
          font-weight: 700;
          text-align: center;
          color: #f5f6f8;
          line-height: 24px;
        }
      }

      .hot_box {
        text-align: left;

        .line-pre {
          display: inline-block;
          vertical-align: middle;
          width: 20px;
          height: 20px;
          text-align: center;

          .status-point {
            display: inline-block;
            background-color: #3c3c3c;
            width: 8px;
            height: 8px;
            border-radius: 50%;
            position: relative;
            top: 17px;
            right: -15px;
          }
        }

        p {
          cursor: pointer;
          margin-left: 20px;
          font-size: 18px;
          font-family: 'PingFang SC', 'PingFang SC-Bold';
          font-weight: 700;
          color: #3a3a3a;
          line-height: 56px;
          overflow: hidden;
          display: -webkit-box;
          -webkit-line-clamp: 1;
          -webkit-box-orient: vertical;
        }

        p:hover {
          cursor: pointer;
          color: #0256ca;
        }
      }
    }

  }

  /*轮播图*/
  .home_lunbo {
    background-image: url("~@/assets/images/portal/home/lunbo_banner.png");
    background-size: 100% 100%;
    height: 314px;

  }

  .lunbo_box {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 314px;
    width: 75rem;
    margin: 0 auto;

    .lunbo_left {
      width: 636px;

      .el-image{
        height: 250px;
      }

      ::v-deep .el-carousel__indicators--outside {
        display: none;
      }
    }

    .lunbo_right {
      width: 537px;
      min-height: 280px;
      padding: 20px 30px 0;
      background-color: #F5F6F8;

      .lunbo_click{
        /*background-color: red;*/
        transform: scale(1.08);
        transition-duration: 0.5s;
      }

      div {
        /*width: 462px;*/
        h4 {
          /*width: 462px;*/
          height: 22px;
          font-size: 18px;
          font-family: 'PingFang SC', 'PingFang SC-Bold';
          font-weight: 700;
          text-align: left;
          /*color: #000000;*/
          color: #303133;
          line-height: 22px;
          overflow: hidden;
          display: -webkit-box;
          -webkit-line-clamp: 1;
          -webkit-box-orient: vertical;
          word-wrap: break-word;
        }

        p {
          margin: 10px 0 30px 0;
          /*width: 462px;*/
          font-size: 16px;
          font-family: 'PingFang SC', 'PingFang SC-Medium';
          font-weight: 500;
          text-align: left;
          color: #000000;
          line-height: 21px;
          overflow: hidden;
          display: -webkit-box;
          -webkit-line-clamp: 1;
          -webkit-box-orient: vertical;
          word-wrap: break-word;
        }
      }
    }
  }

  /*轮播*/
  .home_lb {
    /*height: 163px;*/
    height: 135px;
    width: 1315px;
    margin-top: 20px;

    ::v-deep .el-carousel__container {
      height: 135px;

      .el-carousel__arrow {
        display: none;
      }
    }

    .lb_zq {
      width: 300px;
      display: block;
      padding: 0 5px;
      transition:all .5s;

      a {
        text-decoration: none !important;
        position: relative;
        height: 135px;
        background-color: #666;
        display: -webkit-box !important;
        display: -ms-flexbox !important;
        display: flex !important;
        -webkit-box-align: center;
        -ms-flex-align: center;
        align-items: center;
        -webkit-box-pack: center;
        -ms-flex-pack: center;
        justify-content: center;
        padding: 0 15px;
        color: #fff;
        font-size: 20px;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        -webkit-box-orient: vertical;
        -webkit-box-direction: normal;
        -ms-flex-direction: column;
        flex-direction: column;
        line-height: 1.4;
        cursor: pointer;
        background-repeat: no-repeat;
        background-size: 100% 100%;
      }
    }
  }

  /*一件事*/
  .home_yjs {

    .yjs_card {
      cursor: pointer;
      /*margin-top: 20px;*/
      height: 160px;
      /*background-color: #e7f3ff;*/
      padding: 28px 30px;
      /*background-image: url('https://zwfw.fujian.gov.cn/bsfw/img/ztbg1.11f514e5.png');*/
      background-size: 100% 100%;
      background-position: 100%;
      background-repeat: no-repeat;
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
      text-align: left;
      transition:all .5s;

      div {
        color: #717171;
        line-height: 1.4;
        -webkit-transition: all .3s ease-in-out;
        transition: all .3s ease-in-out;
      }

      div:first-child {
        font-size: 22px;
        font-weight: 600;
        color: #000;
        margin-bottom: 15px;
      }
    }
  }

  /*服务*/
  .home_fw {
    margin-top: 1.75rem;
    height: 433px;

    .fw-back-image {
      background-image: url("~@/assets/images/portal/home/fuwu_banner.png");
      margin-top: 10px;
    }

    .fw_cards {
      display: flex;
      //justify-content: space-between;
      align-items: center;
      height: 22.375rem;
      width: 75rem;
      margin: 0 auto;

      .fw_card {
        cursor: pointer;
        width: 12.5rem;
        padding: 0 10px;
        min-height: 11.8125rem;
        transition-duration: 0.5s;
        //background-color: #FCFDFF;
        //padding: 26px;
        span {
          font-size: 16px;
          font-family: 'PingFang SC', 'PingFang SC-Bold';
          font-weight: 700;
          text-align: center;
          color: #333333;
          line-height: 24px;
          white-space: pre-wrap;
        }
      }

      .fw_card:hover {
        transform: translate(0, -15px);
      }
    }

    /*标签栏*/
    ::v-deep .el-tabs__header {
      width: 75rem;
      margin: 0 auto;
    }

    ::v-deep .el-tabs__active-bar {
      height: 0;
    }

    ::v-deep .el-tabs__nav-wrap::after {
      height: 0;
    }

    ::v-deep .el-tabs__item.is-active, .el-tabs__item:hover {
      color: #1658a0;
    }

    ::v-deep .el-tabs__item:hover {
      color: #1658a0;
    }

    span {
      font-size: 24px;
      font-weight: 600;
      line-height: 1.4;
    }

  }

  /*通知*/
  .home_tz {
    ::v-deep .el-tabs__active-bar {
      height: 0;
    }

    ::v-deep .el-tabs__nav-wrap::after {
      height: 0;
    }

    ::v-deep .el-tabs__item.is-active, .el-tabs__item:hover {
      color: #1658a0;
    }

    ::v-deep .el-tabs__item:hover {
      color: #1658a0;
    }

    span {
      font-size: 24px;
      font-weight: 600;
      line-height: 1.4;
    }

    .tz_menu {
      padding: 5px 20px 5px 0;
      cursor: pointer;
      color: #000;
    }

    .menu_cur {
      color: #1658a0
    }

    .tz_card {
      display: -webkit-box;
      display: -ms-flexbox;
      display: flex;
      -ms-flex-wrap: wrap;
      flex-wrap: wrap;
      justify-content: space-between;
      align-content: space-between;
      margin-bottom: 20px;
      margin-top: 20px;

      .card_item {
        width: 24.5%;
        text-align: left;

        .tz_title {
          font-size: 16px;
          line-height: 25px;
          margin-bottom: 12px;
          height: 50px;
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 2;
          overflow: hidden;
        }

        .tz_counent {
          padding-top: 20px;
          font-size: 14px;
          color: #bebebe;
        }
      }
    }

    .div-button {
      width: 78px;
      height: 19px;
      font-size: 20px;
      font-family: PingFang SC, PingFang SC-Medium;
      font-weight: 500;
    }
  }

}

</style>
