<template>
<!--  人才落户-->
  <div class="content">
<!--    背景图-->
    <div class="back-image"></div>
    <div class="content-text">
      <div>
        <h1>情系人才，致力安居</h1>
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
              :data="tableData"
              style="width: 100%">
              <el-table-column label="符号" width="10px">
                <template slot-scope="scope">
                  <img src="@/assets/images/portal/talentSet/图标.png">
                </template>
              </el-table-column>
              <el-table-column prop="name" label="标题">
                <template slot-scope="scope">
                  <span class="span1" :style="'margin-left:'+getMargin(scope.row.name)">
                    {{scope.row.name}}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="date" label="时间" align="right" width="120px">
                <template slot-scope="scope">
                  <span class="span2">
                    {{scope.row.date}}
                  </span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
<!--      就业创业培训-->
      <div style="text-align: center;">
        <div class="part2">
          <img src="@/assets/images/portal/talentSet/左.png" style="margin-right: 30px">
          <span>事项服务</span>
          <img src="@/assets/images/portal/talentSet/右.png" style="margin-left: 30px">
        </div>
      </div>
      <div class="part3">
        <div class="part3-box" v-for="(item,index) in boxList"
             :style="index===0?'margin-left: 0':'margin-left: 5.125rem'">
          <el-image class="part3-image" :src="item.image"/>
          <p class="part3-span">{{ item.title }}</p>
<!--          <el-button class="part3-button1">-->
<!--            <img src="@/assets/images/portal/talentSet/按钮图标.png">-->
<!--            申报指南-->
<!--          </el-button>-->
          <el-button class="part3-button2">立即查看</el-button>
        </div>
      </div>
    </div>

  </div>
</template>

<script>

export default {
  name: 'talentSet',
  data(){
    return{
      boxList: [
        {
          title: '人才安家补助申报',
          image: require('@/assets/images/portal/talentSet/card1.png'),
        },
        {
          title: '人才生活补贴申报',
          image: require('@/assets/images/portal/talentSet/card2.png'),
        },
        {
          title: '人才公寓认定申报',
          image: require('@/assets/images/portal/talentSet/card3.png'),
        },
        {
          title: '人才购房补助申报',
          image: require('@/assets/images/portal/talentSet/card4.png'),
        }
      ],
      images: [
        require('@/assets/images/portal/talentSet/图1.png'),
      ],
      tableData:[
        {
          date: '2023-01-20',
          name: '《关于实施“英才汇江”工程推动人才人口聚集的工作意见》',
        },
        {
          date: '2023-01-17',
          name: '关于印发《泸州市“酒城创新人才聚集行动”人才住房保障实施细则...',
        },
        {
          date: '2023-01-13',
          name: '关于2022年度泸州市赴高校开展招才引智系列活动的公告',
        },
        {
          date: '2023-01-03',
          name: '关于印发泸州市“酒城学者“特聘专家选聘办法《试行》的通知',
        },
        {
          date: '2022-12-24',
          name: '《泸州市鼓励引导人才向基层流动实施方案》',
        },
      ]
    }
  },
  methods:{
    //截取标题书名号
    getMargin(item){
      let str=item.substr(0,1)
      console.log('1243124124124',str)
      if (str==='《'){
        return '-5px'
      }else {
        return 0;
      }
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
        height: 17.9375rem;
        text-align: center;
        box-shadow: 0.67px 0.74px 43px 1px rgba(3,21,51,0.06);
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
        .part3-button1{
          width: 7rem;
          height: 2.0625rem;
          color: #4184F4;
          background-color: #C3DAFF;
          font-size: 16px;
          padding: 5px;
          margin-top: 1.324rem;
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
