<template>
  <div class="main_content">
    <!--    轮播背景图-->
    <div class="header_lb_position">
      <el-carousel indicator-position="none">
        <el-carousel-item v-for="(item,index) in backgroundImage" :key="index">
          <img :src="item" width="100%">
        </el-carousel-item>
      </el-carousel>


      <div class="search" style="position: absolute; top: 150px;right: 180px; z-index: 9999;width: 300px;">
        <el-input placeholder="请输入内容" v-model="searchKey" size="medium" @change="gotoSearch">
          <el-button slot="append" icon="el-icon-search" @click="gotoSearch"></el-button>
        </el-input>
      </div>
    </div>

    <div class="dh" osize="16">
      <div class="dh_box" osize="16">
        <ul class="pc" voice-area="nav" voice-arealabel="网站主" osize="16">
          <li osize="16">
            <a href="/home" osize="20">首页</a>
          </li>
          <li :class="pageCount == 2 ? 'active':''" osize="16">
            <a href="./newsList?type=机关动态" @click="toPage(2)" :class="pageCount == 2 ? 'activeTxt':''"
               osize="20">机关动态</a>
          </li>
          <li :class="pageCount == 3 ? 'active':''" osize="16">
            <a href="./newsList?type=专题专栏" @click="toPage(3)" :class="pageCount == 3 ? 'activeTxt':''"
               osize="20">专题专栏</a>
          </li>
          <li :class="pageCount == 4 ? 'active':''" osize="16">
            <a href="./newsList?type=党建信息" @click="toPage(4)" :class="pageCount == 4 ? 'activeTxt':''"
               osize="20">党建信息</a>
          </li>
          <li :class="pageCount == 5 ? 'active':''" osize="16">
            <a href="./newsList?type=先锋典型" @click="toPage(5)" :class="pageCount == 5 ? 'activeTxt':''"
               osize="20">先锋典型</a>
          </li>
            <li :class="pageCount == 6 ? 'active':''" osize="16">
                <a href="./newsList?type=党建微视" @click="toPage(6)" :class="pageCount == 6 ? 'activeTxt':''"
                   osize="20">党建微视</a>
            </li>
        </ul>
      </div>
    </div>


    <div class="center-content">
      <div class="lmfj" voice-area="content" voice-arealabel="当前位置" osize="18">
        <a href="/home" title="首页" class="CurrChnlCls" osize="18">首页</a>&nbsp;&gt;&gt;&nbsp;
        <a :href="'./newsList?type=' + newsType" :title="newsType" class="CurrChnlCls" osize="18">{{ newsType }}</a>
      </div>
      <div class="news-content" style="margin-bottom: 20px">
        <div class="ejlb" osize="16">
          <div class="ejlb_1" osize="20">{{ newsType }}</div>
        </div>
        <div class="ejlb_3" voice-area="content" voice-arealabel="党建要闻列表" osize="16">
          <ul osize="16" v-if="newsList.length > 0">
            <li osize="16" v-for="(item,index) in newsList" :key="index">
              <svg t="1692177055380" class="icon" viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg"
                   p-id="4222" width="30" height="13" tabindex="-1" osize="16">
                <path d="M480 480m-288 0a4.5 4.5 0 1 0 576 0 4.5 4.5 0 1 0-576 0Z" p-id="4223" fill="#cacaca"
                      tabindex="-1" osize="16"></path>
              </svg>
              <a :href="item.url" target="_blank">{{ item.title }}</a>
              <time osize="16">{{ formartDate(item.created, 'yyyy-MM-dd') }}</time>
            </li>
          </ul>
          <div v-else style="text-align: center;padding: 20px 0;font-size: 20px;font-weight: bold;">暂无数据</div>
        </div>
<!--        <div>-->
<!--          <div class="piclist cl">-->
<!--            <ul class="mt20" v-if="videoList.length > 0">-->
<!--              <li v-for="(item,index) in videoList" :key="index" @click="toVideo(item.filePath)">-->
<!--                <video :src="item.filePath" style="width: 280px;height: 150px;"></video>-->
<!--                <p class="title">{{ item.title }}</p>-->
<!--                <p class="time">{{ item.created }}</p>-->
<!--              </li>-->
<!--            </ul>-->
<!--            <div v-else style="text-align: center;padding: 20px 0;font-size: 20px;font-weight: bold;">暂无数据</div>-->
<!--          </div>-->
<!--        </div>-->
      </div>
      <el-pagination
        style="width: 75rem; text-align: center; margin: 0 auto 50px"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
    <div class="footer" voice-area="service" voice-arealabel="服务区,当前为网页底部服务区，有网站建设维护联系方式、网站地图、便民电话等服务内容。" osize="16">
      <div class="bqxi" osize="16">
        技术支持：数字泸州产业投资集团有限公司 蜀ICP备17003881号<br osize="16">
        中共泸州市机关工委 主办 联系电话：0830-2361732<br osize="16">
        Copyright 2018 www.lz12371.cn All Rights Reserved<br osize="16">
        联系地址：中共泸州市机关工委
      </div>
    </div>
  </div>
</template>

<script>
import {queryNews, getFormatDate, queryVideos} from "@/api/news";

export default {
  name: "newsList",
  data() {
    return {
      //轮播图片
      backgroundImage: [
        require('@/assets/home/top_back.jpg'),
      ],
      currentPage: 1,
      total: 0,
      pageSize: 10,
      newsType: '',
      newsList: [],
      pageCount: 0,
      searchKey: '',
      videoList: []
    }
  },
  created() {
    let menuType = localStorage.getItem("menuType")
    if (menuType) {
      this.pageCount = menuType
    }
    this.newsType = this.$route.query.type
      this.queryNewsList();
    // if (this.newsType == '专题专栏') {
    //   this.queryVideoList()
    // } else {
    //   this.queryNewsList();
    // }
  },
  methods: {
    toPage(num) {
      localStorage.setItem("menuType", num)
    },
    formartDate(date) {
      return getFormatDate(date, '{y}-{m}-{d}');
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageSize = val;
      this.queryNewsList()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.currentPage = val;
      this.queryNewsList()
    },
    queryNewsList() {
      let openUrl = window.location.protocol + "//" + window.location.host
      let categoryId;
      if (this.newsType == '专题专栏') {
        categoryId = 2
      } else if (this.newsType == '机关动态') {
        categoryId = 21
      } else if (this.newsType == '党建信息') {
        categoryId = 11
      } else if (this.newsType == '先锋典型') {
          categoryId = 31
      } else if (this.newsType == '党建微视') {
          categoryId = 41
      }
      let data = "?pageNum=" + this.currentPage + "&pageSize=" + this.pageSize + "&categoryId=" + categoryId
      queryNews(data).then(res => {
        if (res.code == 200) {
          this.newsList = res.data.records
          this.total = res.data.total
          for (let i = 0; i < this.newsList.length; i++) {
            if (openUrl == 'http://10.46.19.198:7112') {
              this.newsList[i].url = this.newsList[i].url.replace("http://10.4.117.31", "http://10.46.19.198")
            }
              if(openUrl == 'https://portal.jcjgdj.cn') {
                  this.newsList[i].url = this.newsList[i].url.replace("http://10.4.117.31:7380", "https://cms.jcjgdj.cn/adminApi");
              }
          }
        } else {
          this.$message.error("查询异常")
        }
      })
    },
    queryVideoList() {
      let openUrl = window.location.protocol + "//" + window.location.host
      let data = "?pageNum=" + this.currentPage + "&pageSize=" + this.pageSize
      queryVideos(data).then(res => {
        if (res.code == 200) {
          this.videoList = res.data.records
          this.total = res.data.total
          for (let i = 0; i < this.videoList.length; i++) {
            if (openUrl == 'http://10.46.19.198:7112') {
              this.videoList[i].url = this.videoList[i].url.replace("http://10.4.117.31", "http://10.46.19.198")
            }
              if(openUrl == 'https://portal.jcjgdj.cn') {
                  this.videoList[i].url = this.videoList[i].url.replace("http://10.4.117.31:7380", "https://cms.jcjgdj.cn/adminApi");
              }
          }
        } else {
          this.$message.error("查询异常")
        }
      })
    },
    toVideo(url) {
      window.open(url, "_blank")
    },
    gotoSearch() {
      var A = document.createElement("A");
      A.setAttribute("href", "./searchList?searchKey=" + this.searchKey);
      A.click()
    }
  }
}
</script>
<style lang="less">
@import "./home.less";

.news-content {
  margin: 0 auto;
  width: 75rem;
  display: table;
  min-height: calc(51vh - 3px);
}


.ejlb {
  text-align: left;
  height: 50px;
  border-bottom: 1px solid #c01313;
}

.ejlb_1 {
  padding: 0 50px;
  display: inline-block;
  height: 50px;
  font-size: 20px;
  font-weight: bold;
  color: #ffeed2;
  line-height: 50px;
  text-align: center;
  background-color: #c01313;
}

.ejlb_3 {
  float: left;
  margin: 30px 0 0 40px;
  width: 75rem;

  a {
    color: #333;
  }

  a:hover {
    color: #c01313;
  }

  li {
    width: 72rem;
    height: 50px;
    font-size: 16px;
    line-height: 50px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  li time {
    float: right;
    color: #999;
  }
}

.lmfj {
  padding-top: 20px;
  text-align: left;
  margin: 0px auto 35px;
  width: 1200px;
  height: 40px;
  font-size: 18px;
  line-height: 40px;
  color: #777;

  a {
    color: #777;
  }

  a:hover {
    color: #c01313;
  }

  .active {
    color: #c01313;
    background: #ffeed2;

    .activeTxt {
      color: #c01313;
    }
  }
}

.search {
  .el-input__inner {
    height: 45px !important;
    line-height: 45px !important;
  }

  .el-input-group {
    border: 1px solid #c01313;
    border-radius: 20px
  }

  .el-input-group--append .el-input__inner {
    border-right: 1px solid #ccc;
    border-top-left-radius: 20px;
    border-bottom-left-radius: 20px;
  }

  .el-input-group__append {
    border-top-right-radius: 20px;
    border-bottom-right-radius: 20px;
  }

  .el-icon-search {
    color: #c01313 !important;
  }

  .el-input__inner:focus {
    outline: none;
    border-color: transparent;
  }
}

.cl {
  clear: both;
  margin: 0;
  padding: 0;
  border: none;
  overflow: hidden;

  .mt20 {
    margin-top: 20px;
  }

  li {
    float: left;
    _display: inline;
    width: 280px;
    height: 250px;
    margin: 0 18px 20px 0;
    overflow: hidden;
    font-size: 18px;
    color: #333;
    line-height: 280%;
    cursor: pointer;

    .title {
      line-height: 20px;
      font-size: 16px;
      color: #333;
      font-weight: normal;
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      line-clamp: 2;
      -webkit-box-orient: vertical;
      text-align: center;
    }

    .time {
      color: #999;
      font-size: 14px;
      text-align: left;
    }
  }
}
</style>
