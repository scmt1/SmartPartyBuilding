<template>
<!--  资讯首页-->
    <div class="page">
      <div class="content-width" style="margin: auto">
        <a @click="toNewDetail('/cms/article/'+titleList[0].id)">
          <h1>{{ titleList[0].title }}</h1>
        </a>
        <a @click="toNewDetail('/cms/article/'+titleList[1].id)" >
          <h3>{{ titleList[1].title }}</h3>
        </a>
      </div>
<!--      图片，主题-->
      <div class="page-content1 content-width">
        <div class="content1-left">
          <a
             @click="toNewDetail('/cms/article/'+textId)"
          >
            <el-image style="width: 100%;height: 100%" :src="ADDRESS+'/cms'+image[0]" alt=""/>
          </a>
        </div>
        <div class="content1-right">
          <div v-for="(item,index) in menusName">
            <el-button :class="getStyle(index)" @click="changeColor(index,item)">
              <span :title="item.title">
                {{item.title}}
              </span>
            </el-button>
          </div>
        </div>
      </div>
<!--      通知公告、人才政策、人才需求-->
      <div style="background-color: #F8F1E8;height: 23rem;padding-top: 1px">
        <div class="page-content2 content-width">
          <el-card class="box-card"
                   v-for="(item,index) in cardTitle" :key="index"

                   :style="index==0?'':'margin-left: 1.3225rem'">
            <div slot="header" class="clearfix">
              <span class="text-title">{{item.title}}</span>
              <el-button style="float: right; padding: 3px 0" type="text" class="button-style1" @click="toView(item.path,{categoryId: item.categoryId})">更多</el-button>
            </div>
            <div style="position: relative;left: -7px">
              <div v-for="(temp,num) in cardData[index]" :key="num"
                   style="height: 30px;margin-bottom: 10px;">
                <div class="line-pre">
                  <div v-if="temp.withTop" class="i1 i-icon">
                    <span>置顶</span>
                  </div>
                  <div v-if="temp.withHot&&!temp.withTop" class="i2 i-icon">
                    <span>热</span>
                  </div>

                  <i v-if="!temp.withHot&&!temp.withTop" class="status-point" />
                </div>
                <div :style="'display: inline-block;vertical-align: top;width: 240px;height: 30px;position: relative;left:' + getMargin(temp.title)">
                  <a @click="toNewDetail('/cms/article/'+temp.id)"
                     :title="temp.title">
                    <span class="text">{{temp.title}}</span>
                  </a>
                </div>
                <div class="line-suffix">
                   <span class="text" style="color: #888888">
                    {{getDate(temp.created)}}
                  </span>
                </div>
              </div>
            </div>
          </el-card>
        </div>
      </div>
<!--      人才要闻、工作动态、人才活动-->
      <div class="page-content3">
        <div class="content3-log content-width">
          <el-image :src="ADDRESS+image[1]" alt=""/>
        </div>
        <div class="content3-tabs content-width">
          <div class="menu-page" style="position: relative">
            <el-tabs
              v-model="activeName"
            >
              <el-tab-pane v-for="(item,index) in dataList" :key="index" :value="item" :name="nameList[index]">
                <div slot="label">
                  <span> {{labelList[index]}}</span>
<!--                  <el-divider v-if="index!==2" direction="vertical"></el-divider>-->
                </div>
                <!-- v-if="showPane&&showPane2&&showPane3"-->
                <div class="tabs-pane" v-if="showPane">

                  <div class="pane-width" v-for="(temp,num) in 3" style="margin-top: 20px">
                    <a @click="toNewDetail('/cms/article/'+item[num].id)">
                      <el-image style="width: 350px; height: 187px" :src="(item[num].showImage+'').indexOf('http')==0?item[num].showImage:ADDRESS+'/cms'+item[num].showImage" alt=""/>
                    </a>
                    <a @click="toNewDetail('/cms/article/'+item[num].id)">
                      <p class="pane-title" style="margin-top: 30px">{{item[num].title}}</p>
                    </a>
                    <a @click="toNewDetail('/cms/article/'+item[num].id)">
                      <p class="pane-content">{{item[num].highlightContent}}</p>
                    </a>
                    <p style="text-align: right;margin-top: 10px;color: #888888">{{getDate(item[num].created)}}</p>
                  </div>
                  <el-divider></el-divider>
                  <div class="pane-width" v-for="(temp,num) in item.length-3">
                    <a @click="toNewDetail('/cms/article/'+item[num+3].id)">
                      <p class="pane-title">{{item[num+3].title}}</p>
                    </a>
                    <a @click="toNewDetail('/cms/article/'+item[num+3].id)">
                      <p class="pane-content">{{item[num+3].highlightContent}}</p>
                    </a>
                    <p style="text-align: right;margin-top: 10px;color: #888888">{{getDate(item[num+3].created)}}</p>
                  </div>
                </div>
              </el-tab-pane>
            </el-tabs>
            <div class="button-box">
              <el-button class="div-button" type="text" @click="toView('/talentHighlights',{activeName: activeName})">查看更多</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
</template>

<script>
  import {getListByCategorySlug, getPaginateByCategorySlug} from '@/api/portal/cms'
  export default {
    name: 'information',
    mounted() {
      this.getData();
    },
    components:{
    },
    props: {},
    data() {
      return {
        ADDRESS:process.env.VUE_APP_YSZJ_ADDRESS,
        titleList:
          [
            {title: '', id: '',},
            {title: '', id: '',}
          ],
        menusName: [
          {
            title: '',
            thumbnail: '',
          }
        ],
        textId: '',
        imageLogo: [
          require('@/assets/images/portal/invitation/置顶.png'),
          require('@/assets/images/portal/invitation/火.png'),
        ],
        image: [
          require('@/assets/images/portal/invitation/资讯图1.png'),
          '/cms/attachment/20230206/9994c31e37e1445198a2ccdf81dbed27.png',
          require('@/assets/images/portal/invitation/雏鹰计划图片1.png'),
          require('@/assets/images/portal/invitation/左上.png'),
          require('@/assets/images/portal/invitation/右下.png'),
        ],
        imageList: [
          require('@/assets/images/portal/invitation/img1.png'),
          require('@/assets/images/portal/invitation/img2.png'),
          require('@/assets/images/portal/invitation/img3.png'),
          require('@/assets/images/portal/invitation/img4.png'),
          require('@/assets/images/portal/invitation/img5.png'),
          require('@/assets/images/portal/invitation/img6.png'),
        ],
        imageTitles:['(专业科目)农业科目培训课程[2023]','(专业科目)人工神经系统及其应用','(专业科目)深化政府机构改革 (第一期)','(专业科目)控制权溢价相关案例探讨','(专业科目)人力资本投资的战略、结构、价值','(专业科目) 高等教育学'],
        cardTitle: [
          {
            title: '通知公告',
            path: '/note',
            categoryId: '2',
          },
          {
            title: '人才政策',
            path: '/note',
            categoryId: '3',
          },
          {
            title: '人才需求',
            path: '/note',
            categoryId: '4',
          },
        ],
        cardData: [
          // [
          //   {id:0, title:"",},
          // ],
          // [
          //   {id:0, title:"",},
          // ],
          // [
          //   {id:0, title:"",},
          // ],
        ],
        num: '0',
        //人才要闻，工作动态，人才活动
        activeName: 'first',
        nameList: ['first','second','third'],
        labelList: ['人才要闻','工作动态','人才活动'],
        activeIndex: '1',
        showPane: false,
        dataList: [
          [
            {showImage: '', id: '', title: '', highlightContent: '', created: '',},
            {showImage: '', id: '', title: '', highlightContent: '', created: '',},
            {showImage: '', id: '', title: '', highlightContent: '', created: '',},
            {showImage: '', id: '', title: '', highlightContent: '', created: '',},
            {showImage: '', id: '', title: '', highlightContent: '', created: '',},
            {showImage: '', id: '', title: '', highlightContent: '', created: '',},
          ],
          [
            {showImage: '', id: '', title: '', highlightContent: '', created: '',},
            {showImage: '', id: '', title: '', highlightContent: '', created: '',},
            {showImage: '', id: '', title: '', highlightContent: '', created: '',},
            {showImage: '', id: '', title: '', highlightContent: '', created: '',},
            {showImage: '', id: '', title: '', highlightContent: '', created: '',},
            {showImage: '', id: '', title: '', highlightContent: '', created: '',},
          ],
          [
            {showImage: '', id: '', title: '', highlightContent: '', created: '',},
            {showImage: '', id: '', title: '', highlightContent: '', created: '',},
            {showImage: '', id: '', title: '', highlightContent: '', created: '',},
            {showImage: '', id: '', title: '', highlightContent: '', created: '',},
            {showImage: '', id: '', title: '', highlightContent: '', created: '',},
            {showImage: '', id: '', title: '', highlightContent: '', created: '',},
          ],

        ],
      }
    },
    methods: {
      //跳转
      toView(path,param){
        this.$router.push({path:path,query:param})
      },
      //点击改变状态
      changeColor(num,item){
        this.image[0]=item.thumbnail
        this.textId=item.id
        this.num=num
      },
      //动态style
      getStyle(key){
        let value=''
        if (this.num===key){
          value='button-style-visited'
        }else{
          value='button-style'
        }
        return value
      },
      async getData(){
        let orderBy="with_top desc,with_hot desc,created desc"

        //标题，次标题
        //轮播图片,新闻资讯
        getPaginateByCategorySlug({"categorySlug": "xwzx","orderBy": orderBy,"pageNumber":1,"pageSize":2}).then(res=>{
          if(res.state=="ok"){
            this.titleList=(res.page.list)
          }
        })
        //轮播图片,新闻资讯
        getPaginateByCategorySlug({"categorySlug": "xwzx","orderBy": "with_top,with_hot,created","pageNumber":1,"pageSize":4}).then(res=>{
          if(res.state=="ok"){
            this.menusName=(res.page.list)
            this.changeColor(0,this.menusName[0])
          }
        })

        let pageNumber=1
        let pageSize=6
        //通知公告
        await getPaginateByCategorySlug({"categorySlug": "tzgg","orderBy": orderBy,"pageNumber":pageNumber,"pageSize":pageSize}).then(res=>{
          // console.log(res.page)
          if(res.state=="ok"){
            this.cardData.push(res.page.list)
          }
        })
        //人才政策
        await getPaginateByCategorySlug({"categorySlug": "rczc","orderBy": orderBy,"pageNumber":pageNumber,"pageSize":pageSize}).then(res=>{
          if(res.state=="ok"){
            this.cardData.push(res.page.list)
          }
        })
        //人才需求
        await getPaginateByCategorySlug({"categorySlug": "rcxq","orderBy": orderBy,"pageNumber":pageNumber,"pageSize":pageSize}).then(res=>{
          if(res.state=="ok"){
            this.cardData.push(res.page.list)
          }
        })
        //人才要闻，工作动态，人才活动
        getPaginateByCategorySlug({"categorySlug": "gzdt","orderBy": orderBy,"pageNumber":pageNumber,"pageSize":pageSize}).then(res=>{
          if(res.state==="ok"){
            this.dataList[1]=res.page.list
            // this.showPane2=true
          }
        })
        getPaginateByCategorySlug({"categorySlug": "rchd","orderBy": orderBy,"pageNumber":pageNumber,"pageSize":pageSize}).then(res=>{
          if(res.state==="ok"){
            this.dataList[2]=res.page.list
            // this.showPane3=true
          }
        })
        await getPaginateByCategorySlug({"categorySlug": "rcyw","orderBy": orderBy,"pageNumber":pageNumber,"pageSize":pageSize}).then(res=>{
          if(res.state==="ok"){
            this.dataList[0]=res.page.list
            this.showPane=true
          }
        })
        // console.log(this.dataList)
      },
      //截取标题书名号
      getMargin(item){
        let str=item.substr(0,1)
        if (str==='《'){
          return '-5px'
        }else {
          return 0;
        }
      },
      //截取日期
      getDate(item){
        return item.substr(0,10)
      }
    },
    computed:{

    }
  }
</script>

<style lang="scss" scoped>
  /*布局*/
  .page{
    margin-bottom: 4.6875rem;
    .content-width{
      width: 75rem;
    }
    .page-content1{
      margin: 1.8125rem auto;

      .content1-left{
        width: 50.25rem;
        height: 20.9375rem;
        display: inline-block;
      }
      .content1-right{
        vertical-align: top;
        display: inline-block;
        height: 20.9375rem;
        width: 24.0625rem;
        float: right;
      }
      span{
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        /* autoprefixer: ignore next */
        -webkit-box-orient: vertical;
      }
    }
    .page-content2{
      margin: 1.8125rem auto;

      ::v-deep.el-card__body {
        padding: 15px 0 0 16px;
      }
      .box-card{
        background-color: #FFFAF6;
        width: 24.119rem;
        height: 19.375rem;
        display: inline-block;
        overflow:visible;
        ::v-deep .el-card__body {
          padding: 15px 10px 0 0;
        }
        /*图标*/
        .line-pre{
          display: inline-block;
          vertical-align: middle;
          width: 40px;
          height: 20px;
          text-align: center;
          span{
            font-size: 5px;
            color: white;
            position: relative;
            right: 3px;
            top: 1px;
          }
          .i-icon{
            width: 40px;
            height: 22px;
          }
          .i1{
            background-image: url("~@/assets/images/portal/invitation/置顶2.png");
            background-size: 100% 100%;
          }
          .i2{
            background-image: url("~@/assets/images/portal/invitation/火2.png");
            background-size: 100% 100%;
          }
          .status-point{
            display: inline-block;
            background-color:#0256CA;
            width: 6px;
            height: 6px;
            border-radius: 50%;
            position: relative;
            top: 3px;
          }
          .img-style{
            position: relative;
            top: 5px;
          }
        }

        .text {
          /*height: 36px;*/
          line-height: 30px;
          font-size: 14px;
          font-family: 'PingFang SC-Medium';
          font-weight: 500;
          color: #333333;
          //line-height: 30px;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 1;
          -webkit-box-orient: vertical;
          word-wrap:break-word;
        }
        .line-suffix{
          display: inline-block;
          vertical-align: top;
          text-align: right;
          width: 93px;
        }
        .clearfix:before,
        .clearfix:after {
          display: table;
          content: "";
        }
        .clearfix:after {
          clear: both
        }
      }
    }
    .page-content3{
      .content3-log{
        height: 10rem;
        margin: 1.375rem auto 0 auto;
      }
      .content3-tabs{
        margin: 2.1875rem auto 0 auto;
        .tabs-pane{
          .pane-width{
            width: 21.875rem;
            margin-left: 33px;
            display: inline-block;
            vertical-align: top;
            .pane-title{
              color: black;
              font-size: 16px;
              font-family:'PingFang SC-Bold';
              font-weight: 700;

              overflow: hidden;
              text-overflow: ellipsis;
              display: -webkit-box;
              -webkit-line-clamp: 1;
              -webkit-box-orient: vertical;
              word-wrap:break-word
            }
            .pane-content{
              color: #888888;
              margin-top: 20px;
              font-size: 14px;
              font-family: 'PingFang SC-Medium';
              font-weight: 500;
              line-height: 20px;

              overflow: hidden;
              text-overflow: ellipsis;
              display: -webkit-box;
              -webkit-line-clamp: 2;
              -webkit-box-orient: vertical;
              word-wrap:break-word
            }
          }

          .el-divider--horizontal {
            display: block;
            height: 1px;
            width: 100%;
            margin: 34px 0 45px 0;
            border: 1px dashed #ccc;
          }
        }
        .button-box{
          position: absolute;
          right: 10px;
          top: 0;
          .div-button{
            width: 78px;
            height: 19px;
            font-size: 20px;
            font-family: PingFang SC, PingFang SC-Medium;
            font-weight: 500;
          }
        }
      }
      //tabs卡片
      ::v-deep .el-tabs__item {
        font-size: 20px;
        font-family: "PingFang SC-Heavy";
        font-weight: 800;
        color: #222222;
      }
      ::v-deep .el-tabs__item:hover {
        //color: #0256ca;
      }
      ::v-deep .el-tabs__item.is-active {
        //color: #0256ca;
        font-size: 24px;
      }
      //选中时的下划线
      ::v-deep .el-tabs__active-bar.is-top {
        background: #0256ca;
      }

    }
    .page-content4{
      margin: 2.0625rem auto 0 auto;
      /*布局*/
      .flex-style{
        display: flex;
        justify-content: space-between;
        .div-button{
          position: relative;
          right: 20px;
          width: 78px;
          height: 19px;
          font-size: 20px;
          font-family: PingFang SC, PingFang SC-Medium;
          font-weight: 500;
        }
      }

      .el-divider--horizontal {
        display: block;
        height: 1px;
        width: 100%;
        margin: 18px 0 32px 0;
      }
      .content4-left{
        width: 47.75rem;
        height: 13.8125rem;
        display: inline-block;
        background-color: #F6F6F6;
      }
      .content4-right{
        vertical-align: top;
        display: inline-block;
        width: 25.535rem;
        height: 13.8125rem;
        margin-left: 1rem;
        //float: right;
        .el-divider--horizontal {
          display: block;
          height: 1px;
          width: 100%;
          margin: 15px 0 16px 0;
          border: 1px dashed #ccc;
        }
      }
      .img-left{
        width: 25.4375rem;
        height: 12.25rem;
      }
      .img-size{
        margin-top: 15px;
        width: 24.02rem;
        height: 12.875rem;
        display: inline-block;

      }
    }

    /*字体*/
    h1{
      font-size: 2rem;
      font-family:  "PingFang SC-Heavy";
      font-weight: 800;
      text-align: center;
      color: #0256ca;
      margin-top: 3.8125rem;
    }
    h3{
      font-size: 1.25rem;
      font-family: "PingFang SC-Heavy";
      font-weight: 500;
      text-align: center;
      color: #232323;
      margin-top: 30px;
    }
    .text-title{
      font-size: 16px;
      font-family: 'PingFang SC-Bold';
      font-weight: 700;
      text-align: left;
      color: #222222;
      padding-bottom: 2px;
      //border-bottom: 4px solid #0256CA;
    }
    .text-content{
      line-height: 22px;
    }
    .text-title2{
      font-size: 24px;
      font-family: "PingFang SC-Heavy";
      font-weight: 800;
    }
    .text-title3{
      color: #0256ca;
      font-size: 22px;
      font-weight: 700;
      font-family:  'PingFang SC-Bold';
    }
   /* .text-title4{
      font-size: 26px;
      font-weight: 800;
      text-align: left;
      color: #222222;
      font-family: 'PingFang SC-Heavy';
    }*/
    .text-title4{
      font-size: 20px;
      font-family:  'PingFang SC-Bold';
      font-weight: 700;
      text-align: left;
      color: #333333;
      line-height: 36px;
    }
    .text-title5{
      font-size: 18px;
      font-family:  'PingFang SC-Bold';
      font-weight: 500;
      text-align: left;
      color: #333333;
      line-height: 36px;
    }
    .text-content1{
      font-size: 16px;
      font-weight: 500;
      text-align: left;
      color: #222222;
      line-height: 30px;
      font-family: 'PingFang SC-Medium';
    }
    .text-content2{
      font-size: 18px;
      font-family: 'PingFang SC-Medium';
      font-weight: 500;
      text-align: left;
      color: #232323;
    }



    /*按钮*/
    .button-style{
      word-wrap: break-word;
      overflow-wrap: break-word;
      white-space: normal;
      overflow: hidden;
      width: 385px;
      height: 80px;
      margin-bottom: 5px;
      background-color: #f3f3f3;
      border-color: #f3f3f3;
      border-radius: 0;

      font-size: 16px;
      line-height: 24px;
      text-align: left;
      font-family: "PingFang SC-Medium";
      /*font-weight: 700;*/

    }

    .button-style-visited{
      word-wrap: break-word;
      overflow-wrap: break-word;
      white-space: normal;
      overflow: hidden;
      width: 385px;
      height: 80px;
      margin-bottom: 5px;
      background-color: #0256ca;
      border-color: #0256ca;
      color: white;
      border-radius: 0;

      text-align: left;
      font-size: 16px;
      line-height: 24px;
      font-family: "PingFang SC-Medium";
      font-weight: 600;

    }

    .button-style:hover,.button-style:visited{
      background-color: #0256ca;
      color: white;
    }

    .button-style1{
      font-size: 14px;
      font-family: 'PingFang SC-Bold';
      /*font-weight: 700;*/
      color: #000000;
      text-align: left;
    }
  }

</style>
