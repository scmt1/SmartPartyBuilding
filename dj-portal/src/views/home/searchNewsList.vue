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

    <div class="center-content">
      <div id="news-content" class="news-content" style="margin-bottom: 20px">
        <div class="ejlb_3" voice-area="content" voice-arealabel="党建要闻列表" osize="16">
          <ul osize="16">
            <li osize="16" v-for="(item,index) in newsList" :key="index">
              <svg t="1692177055380" class="icon" viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg"
                   p-id="4222" width="30" height="13" tabindex="-1" osize="16">
                <path d="M480 480m-288 0a4.5 4.5 0 1 0 576 0 4.5 4.5 0 1 0-576 0Z" p-id="4223" fill="#cacaca"
                      tabindex="-1" osize="16"></path>
              </svg>
              <a :href="item.url" target="_blank"v-html="textHighlighting(item.title)"></a>
              <time osize="16">{{ formartDate(item.created, 'yyyy-MM-dd') }}</time>
            </li>
          </ul>
        </div>
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
import {queryNews, getFormatDate} from "@/api/news";

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
      searchKey: ''
    }
  },
  created() {
    this.searchKey = this.$route.query.searchKey
    this.queryNewsList();
  },
  methods: {
    textHighlighting(textItem) {
      const searchTextArr = this.searchKey.split('')
      const textItemArr = textItem.split('')
      const result = textItemArr.map(item => {
        return searchTextArr.includes(item) ? `<span style="color:red;">${item}</span>` : item
      })
      return result.join('')
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
      let data = "?pageNum=" + this.currentPage + "&pageSize=" + this.pageSize + "&title=" + this.searchKey
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
    gotoSearch() {
      var A = document.createElement("A");
      A.setAttribute("href","./searchList?searchKey=" + this.searchKey);
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

.highlight {
  background-color: red;
}
</style>
