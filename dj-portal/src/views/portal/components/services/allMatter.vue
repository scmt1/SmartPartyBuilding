<template>
  <iFrame :iframeSrc="newSrc"></iFrame>

<!--  <div>-->
<!--    <div class="page_body">-->
<!--      <div class="page_main" style="position: absolute;height: calc(100% - 6.5625rem);">-->
<!--        <iframe style="height:100%;width:100%"  :src="newSrc" frameborder="0"></iframe>-->
<!--      </div>-->
<!--    </div>-->
<!--  </div>-->
<!--  <div>
    <div class="img-back">
      <h1 class="text-img">全部事项</h1>
    </div>
    <div class="menu-page">
      <el-row>
        <el-input
          v-model="input"
          placeholder="请输入关键字"
          style="width: 200px;">
          <el-button slot="append" class="button1">搜索</el-button>
        </el-input>
      </el-row>
      <el-row >
        <div v-for="(item,index) in dateList" :key="index" :value="item" style="margin-top: 20px;position: relative;">
          <span class="tag1">
            {{item.type}}
          </span>
          <el-row  style="box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);width: 1200px;height: 170px"
                   type="flex"
                   align="middle"
          >
            <el-col :span="12" style="margin-left: 121px">
              <p class="text-part1">{{item.title}}</p>
              <p class="text-part2" style="margin-top: 18px">{{item.name}}</p>
              <p class="text-part2" style="margin-top: 18px">{{item.date}}</p>
            </el-col>
            <el-col :span="12" >
              <div style="float: right;margin-right: 80px;text-align: right">
                <el-row>
                  <el-button
                    class="button2"
                    @click="toInvitationDetails(0)">
                    在线申请 >
                  </el-button>
                </el-row>
                <el-row style="margin-top: 17px">
                  <el-button
                    class="button2"
                    style="color: #BCBCBC;border-color: #BCBCBC"
                    @click="toInvitationDetails(0)">
                    办事指南 >
                  </el-button>
                </el-row>


              </div>

            </el-col>
          </el-row>
        </div>

      </el-row>
    </div>

  </div>-->
</template>

<script>
import iFrame from "@/components/iFrame/full";

export default {
  name: 'allMatter',
  components: { iFrame },
  mounted(){
  },
  data() {
    return {
      newSrc:process.env.VUE_APP_YSZJ_ADDRESS+"/rcfw/web/view/allMatter.html",
      /*input: '',
      dateList: [
        {
          title: '2023年度高层次人才岗位激励申报',
          name: '泸州市人力资源和社会保障局',
          type: '人才激励',
          date: '2023-03-04~2023-05-01'
        },
        {
          title: '2023年劳动者技能提升培训补贴',
          name: '泸州市人力资源和社会保障局',
          type: '人才培养',
          date: '2023-03-04~2023-05-01'
        },
        {
          title: '2022年应届毕业生接收',
          name: '泸州市人力资源和社会保障局',
          type: '人才引进',
          date: '2022-03-04~2022-05-01'
        },
        {
          title: '2022年度高层次人才岗位激励申报',
          name: '泸州市人力资源和社会保障局',
          type: '人才激励',
          date: '2022-03-04~2022-05-01'
        },
        {
          title: '2022年金融服务办理',
          name: '泸州市人力资源和社会保障局',
          type: '人才服务保障',
          date: '2022-03-04~2022-05-01'
        },
        {
          title: '2023年度高层次人才岗位激励申报',
          name: '泸州市人力资源和社会保障局',
          type: '人才激励',
          date: '2022-03-04~2022-05-01'
        },
        {
          title: '2022年度科技之星',
          name: '泸州市人力资源和社会保障局',
          type: '人才认定',
          date: '2022-03-04~2022-05-01'
        },
        {
          title: '2022年度文化文创之星',
          name: '泸州市人力资源和社会保障局',
          type: '人才激励',
          date: '2022-03-04~2022-05-01'
        },

      ]*/
    }
  },
  methods: {
  },
}
</script>

<style scoped>
/*.menu-page{
  width: 75rem;
  margin: 2.625rem auto 116px auto;
}
!*输入框*!
::v-deep .el-input__inner{
  width: 529px;
  height: 48px;
  border-radius: 0;
  !*border: 1px solid #6a6a6a;*!
  &::placeholder{
    font-size: 16px;
    font-family: "PingFang SC", "PingFang SC-Medium";
    font-weight: 500;
    color: #727272;
  }
}
!*按钮*!
.button1{
  height: 48px;
  width: 130px;
  background: #2E67CF !important;
  border-color: #2E67CF !important;
  color: #ffffff !important;
  border-radius: 0;
  font-size: 16px;
  padding-left: 22px;
}
.button2{
  width: 152px;
  height: 50px;
  padding-left: 16px;
  border: 1px solid #0758cb;
  border-radius: 0;
  text-align: center;
  font-size: 20px;
  font-family: "PingFang SC", "PingFang SC-Bold";
  font-weight: 700;
  color: #0758cb;
}
.tag1{
  position: absolute;
  top: 44px;
  width: 66px;
  min-height: 68px;
  height:auto;
  background-color: #1F7BD5;
  color: white;
  border-color: #1F7BD5;
  border-radius: 0;
  white-space: normal;
  font-size: 16px;
  font-family: "PingFang SC", "PingFang SC-Bold";
  font-weight: 700;
  text-align: center;
  padding: 10px 10px 10px 10px;
}

.img-back{
  width: 100%;
  height: 12.5rem;
  background-image: url("../../../image/service/banner.png");
  background-size: 100% 100%;
  min-width: 75rem;

  .text-img{
    width: 75rem;
    margin: 0 auto;
    position: relative;
    top: 5.0625rem;
    font-size: 38px;
    font-family: "PingFang SC", "PingFang SC-Bold";
    font-weight: 700;
    color: #ffffff;
  }
}
!*字体*!

.text-part1{
  font-size: 16px;
  font-family: 'PingFang SC', 'PingFang SC-Bold';
  font-weight: 700;
  color: #212121;
}
.text-part2{
  font-size: 14px;
  font-family: 'PingFang SC', 'PingFang SC-Medium';
  font-weight: 500;
  color: #212121;
}*/

</style>
