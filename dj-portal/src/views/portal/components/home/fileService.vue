<template>
<!--  毕业生档案服务-->
  <div class="content">
<!--    背景图-->
    <div class="back-image"></div>
    <div class="content-text">
      <div>
        <h1>情系人才，档案服务</h1>
      </div>
<!--      政策公告-->
      <div class="part1">
        <div class="left">
          <div class="box">
            <span>
              政策公告
            </span>
          </div>
          <el-image :src="images[0]" style="position: absolute;bottom: 0"/>
        </div>
        <div class="right">
          <div style="text-align: right;padding: 14px 10px 0 0;">
            <span class="span2">更多>></span>
          </div>
          <div class="my_table">
            <el-table
              :show-header="false"
              :data="tableList"
              style="width: 100%">
              <el-table-column label="符号" width="10px">
                <template slot-scope="scope">
                  <img src="@/assets/images/portal/talentSet/图标.png">
                </template>
              </el-table-column>
              <el-table-column prop="name" label="标题">
                <template slot-scope="scope">
                  <a @click="toNewDetail('/cms/article/'+scope.row.id)"
                     :title="scope.row.highlightTitle">
                    <span class="span1" :style="'margin-left:'+getMargin(scope.row.highlightTitle)">
                    {{scope.row.highlightTitle}}
                    </span>
                  </a>
                </template>
              </el-table-column>
              <el-table-column prop="date" label="时间" align="right" width="120px">
                <template slot-scope="scope">
                  <span class="span2">
                    {{getDate(scope.row.created)}}
                  </span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
<!--      事项服务-->
      <div style="text-align: center;">
        <div class="part2">
          <img src="@/assets/images/portal/talentSet/左.png" style="margin-right: 30px">
          <span>事项服务</span>
          <img src="@/assets/images/portal/talentSet/右.png" style="margin-left: 30px">
        </div>
      </div>
      <div class="part3">
        <div class="part3-box" v-for="(item,index) in boxList"
             :style="{marginLeft: index%4===0?'0':'5.125rem',marginTop: index>3?'3.0625rem':'0'}">
          <el-image class="part3-image" :src="item.image"/>
          <p class="part3-span">{{ item.title }}</p>
          <a href="https://service.scrc168.com/docsearch" v-if="index===1">
            <el-button class="part3-button2">立即查看</el-button>
          </a>
          <el-button v-else class="part3-button2" @click="toPath">立即查看</el-button>

        </div>
      </div>
    </div>
  </div>
</template>

<script>

import {getListByCategorySlug} from "@/api/portal/cms";

export default {
  name: 'fileService',

  data(){
    return{
      //政策公告
      images: [
        require('@/assets/images/portal/talentSet/图1.png'),
      ],
      tableList:[
        {
          highlightTitle:"泸州市人民政府关于印发泸州市进一步加快创新发展若干政策措施的通知",
          created:"2022-01-05 10:56"
        },
      ],
      //事项服务
      boxList: [
        {
          title: '高校毕业生报到',
          image: require('@/assets/images/portal/fileService/card1.png'),
        },
        {
          title: '档案查询',
          image: require('@/assets/images/portal/fileService/card2.png'),
        },
        {
          title: '档案接收',
          image: require('@/assets/images/portal/fileService/card3.png'),
        },
        {
          title: '档案转出',
          image: require('@/assets/images/portal/fileService/card4.png'),
        },
        {
          title: '档案材料收集归档',
          image: require('@/assets/images/portal/fileService/card5.png'),
        },
        {
          title: '档案查 (借) 阅',
          image: require('@/assets/images/portal/fileService/card6.png'),
        },
        {
          title: '政审考察',
          image: require('@/assets/images/portal/fileService/card7.png'),
        },
        {
          title: '出具证明',
          image: require('@/assets/images/portal/fileService/card8.png'),
        },

      ],
    }
  },
  mounted() {
    this.getData()
  },
  methods:{
    getData(){
      //就业政策
      getListByCategorySlug({ "categorySlug":"dafw","count":"5" }).then(res=>{
        if(res.state==="ok"){
          this.tableList=res.list
        }
      })
    },
    //截取日期
    getDate(item){
      return item.substr(0,10)
    },
    //截取标题书名号
    getMargin(item){
      let str=item.substr(0,1)
      console.log('1243124124124',str)
      if (str==='《'){
        return '-5px'
      }else {
        return 0;
      }
    },
    toPath(){
      this.$router.push({path: '/fileServiceView'})
    }
  }

}

</script>


<style lang="scss" scoped>
.content{

  .back-image{
    background-image: url("~@/assets/images/portal/talentSet/talent_banner.png");
    background-size:100% 100%;
    height: 35.9375rem;
    position: absolute;
    width: 100%;
    z-index: -1;
    top:100px;
    min-width: 75rem;
  }
  .content-text{
    width: 75rem;
    margin: 6.875rem auto 6.25rem auto;
    .part1{
      position: relative;
      width: 100%;
      height: 16.6875rem;
      margin-top: 5.25rem;
      background-color: #FFFFFF;
      box-shadow: 0.7px 9.98px 13.8px 0.8px rgba(214,214,214,0.20);
      .left{
        display: inline-block;

        .box{
          padding: 1.3125rem;
          background-image: url("~@/assets/images/portal/talentSet/政策公告背景图.png");
          background-size:100% 100%;
          width: 13.25rem;
          height: 4.75rem;
          text-align: center;
          position: relative;
          bottom: 1.9375rem;
          left: 0.625rem;
          span{
            font-size: 24px;
            font-family: 'PingFang SC', 'PingFang SC-Bold';
            font-weight: 700;
            text-align: center;
            color: #ffffff;
            letter-spacing: -0.36px;
          }
        }

      }
      .right{
        position: absolute;
        right: 0;
        vertical-align: top;
        display: inline-block;
        padding-right: 10px;
        width: 44.09rem;
        .span1{
          font-size: 16px;
          font-family: 'PingFang SC', 'PingFang SC-Medium';
          font-weight: 500;
          color: #333333;
          letter-spacing: -0.24px;
        }
        .span2{
          font-size: 16px;
          font-family: 'PingFang SC', 'PingFang SC-Medium';
          font-weight: 500;
          color: #666666;
          letter-spacing: -0.24px;
        }
      }

    }
    .part2{
      text-align: center;
      margin: 2.5rem auto;
      display: flex;
      align-content: center;
      justify-content: center;
      align-items: center;
      img{
        width: 8.1875rem;
        height: 7px;
        vertical-align:middle
      }
      span{
        font-size: 30px;
        font-family: 'PingFang SC', 'PingFang SC-Bold';
        font-weight: 700;
        color: #333333;
        letter-spacing: -0.45px;
        vertical-align:middle;
      }
    }
    .part3{

      .part3-box{
        width: 14.875rem;
        height: 16.375rem;
        text-align: center;
        box-shadow: 0.67px 0.74px 43px 1px rgba(3,21,51,0.10);
        display: inline-block;

        .part3-image{
          width: 4.125rem;
          height: 3.5625rem;
          margin-top: 2.0625rem;
        }
        .part3-span{
          font-size: 23px;
          font-family: 'PingFang SC', 'PingFang SC-Bold';
          font-weight: 700;
          color: #333333;
          letter-spacing: -0.35px;
          margin-top: 1.3125rem;
        }
        .part3-button2{
          width: 12.5rem;
          height: 2.5rem;
          color: #FFFFFF;
          background-color: #0758CB;
          font-size: 23px;
          font-family: 'PingFang SC', 'PingFang SC-Bold';
          font-weight: 700;
          letter-spacing: -0.35px;
          padding: 5px 20px;
          margin-top: 2.9rem;
        }

        ::v-deep .el-button--medium {
          border-radius: 0;
        }


      }

    }
    h1{
      font-size: 84px;
      font-family: 'PingFang SC', 'PingFang SC-Heavy';
      font-weight: 800;
      text-align: center;
      color: #ffffff;
      letter-spacing: -1.26px;
      text-shadow: 0 5px #0657CA;
    }

    .my_table ::v-deep .el-table__row>td{
      /* 去除表格线 */
      border: none;
    }
    .my_table ::v-deep .el-table th.is-leaf {
      /* 去除上边框 */
      border: none;
    }
    .my_table ::v-deep .el-table::before{
      /* 去除下边框 */
      height: 0;
    }
  }
}

</style>
