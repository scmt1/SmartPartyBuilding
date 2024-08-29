<template>
  <!--  毕业生档案服务-->
  <div class="content">
    <el-tabs v-model="activeName" tab-position="left">
        <!--        高校毕业生报到-->
        <el-tab-pane name="1">
          <div slot="label">
            <span class="text-span">高校毕业生报到</span>
          </div>
          <div class="tabs-content">
            <div class="title">
              <span>服务指南</span>
            </div>
            <div class="text-content">
              <!--              申请条件-->
              <p style="font-weight: bold;">一、申请条件</p>
              <p>派往泸州市人力资源和社会保障局或泸州市人才服务中心的非师范类普通高校毕业生及中专毕业生。</p>
              <!--              申请材料-->
              <p style="font-weight: bold;">二、申请材料</p>
              <p>档案相对人《身份证》。</p>
              <!--              办理流程-->
              <p style="font-weight: bold;">三、办理流程</p>
              <p>登录四川省流动人员人事档案公共服务平台（http://103.203.218.132:8086/）→用户注册→毕业生报到→填写信息→选择存档机构→上传《身份证》→公共人才服务机构审核办理→本人登录四川省人社厅官网“到档信息查询”查询是否到档。</p>
              <!--              办结时限-->
              <p style="font-weight: bold;">四、办结时限</p>
              <p>即时办结。</p>
              <!--              联系方式-->
              <p style="font-weight: bold;">五、联系方式</p>
              <p>（一）联系电话：0830-3156708、0830-3158737。</p>

            </div>
            <div class="button-div">
              <a href="http://103.203.218.132:8086/funcService/ServiceGuide.html?openid=2&servId=62" style="font-size: 16pt;">
                <el-button>立即办理</el-button>
              </a>
            </div>
          </div>
        </el-tab-pane>
        <!--        档案接收-->
        <el-tab-pane name="2">
          <div slot="label">
            <span class="text-span">档案接收</span>
          </div>
          <div class="tabs-content">
            <div class="title">
              <span>服务指南</span>
            </div>
            <div class="text-content">
              <!--              申请条件-->
              <p style="font-weight: bold;">一、申请条件</p>
              <p>（一）派往泸州市人力资源和社会保障局或泸州市人才服务中心的未就业高校毕业生及中专毕业生。</p>
              <p>（二）档案存放在县级（含县级）以上公共就业和人才服务机构的人员。</p>
              <p>（三）档案存放在机关、国有企事业单位，已办理完辞职辞退手续，解除或终止劳动关系的人员。</p>
              <!--              申请材料-->
              <p style="font-weight: bold;">二、申请材料</p>
              <p>（一）高校毕业生档案由学校直接转入。</p>
              <p>（二）档案相对人《身份证》、《户口簿》或劳动（劳务）合同。</p>
              <p>（三）单位移交需提供《单位介绍信》、离职文件（通知书）原件（仅限机关、国有企事业单位离职人员提供）及经办人有效身份证件或工作证原件。</p>
              <p>（四）《委托书》及委托双方有效身份证件（非档案相对人现场办理时提供）。</p>
              <!--              办理流程-->
              <p style="font-weight: bold;">三、办理流程</p>
              <p>（一）网上办理流程</p>
              <p>登录四川省流动人员人事档案公共服务平台（http://103.203.218.132:8086/）→用户注册→流动人员人事档案→档案转接→填写信息→上传本人《身份证》、《户口簿》或《劳动合同》→选择在线办理→公共人才服务机构审核→邮寄《调档函》至本人网上预留的收件地址→本人将《调档函》交档案所在单位→档案机要邮寄至公共人才服务机构→本人登录四川省人社厅官网“到档信息查询”查询是否到档。</p>
              <p>（二）现场办理流程</p>
              <p>1、个人或单位携申请材料到泸州市人才服务中心提出申请。</p>
              <p>2、工作人员审核申请材料是否齐全且符合要求，对申请事项不符合要求的，一次性告知申请人补正。</p>
              <p>3、工作人员审查材料无误后，出具《调档函》。</p>
              <p>4、申请人持《调档函》到存档机构办理相关手续。</p>
              <!--              办结时限-->
              <p style="font-weight: bold;">四、办结时限</p>
              <p>即时办结。</p>
              <!--              办理地点-->
              <p style="font-weight: bold;">五、办理时间、地点</p>
              <p>（一）办理时间：星期一至星期五（法定节假日除外）：上午09:30-12:00，下午2:30-6:30。</p>
              <p>（二）办理地点：泸州市江阳西路飞跃街2号泸州市人才服务中心四楼人事档案管理科。</p>
              <!--              联系方式-->
              <p style="font-weight: bold;">六、联系方式</p>
              <p>（一）联系电话：0830-3156708、0830-3158737。</p>

            </div>
            <div class="button-div">
              <a href="http://103.203.218.132:8086/funcService/ServiceGuide.html?openid=2&servId=70" style="font-size: 16pt;">
                <el-button>立即办理</el-button>
              </a>
            </div>
          </div>
        </el-tab-pane>
        <!--        档案转出-->
        <el-tab-pane name="3">
          <div slot="label">
            <span class="text-span">档案转出</span>
          </div>
          <div class="tabs-content">
            <div class="title">
              <span>服务指南</span>
            </div>
            <div class="text-content">
              <!--              申请条件-->
              <p style="font-weight: bold;">一、申请条件</p>
              <p>（一）人事档案委托管理在泸州市人才服务中心的人员。</p>
              <p>（二）档案接收单位符合以下条件之一：</p>
              <p>1、县级以上（含县级）公共就业和人才服务机构以及经人力资源社会保障部门授权的管理服务机构。</p>
              <p>2、有人事档案管理权限的机关、事业单位、国有企业。</p>
              <!--              申请材料-->
              <p style="font-weight: bold;">二、申请材料</p>
              <p>（一）有效《调档函》或《商调函》。</p>
              <p>（二）档案相对人《身份证》。</p>
              <p>（三）《单位介绍信》及经办人有效身份证件或工作证原件（单位专人现场办理时提供）。</p>
              <p>（四）《委托书》及委托双方有效身份证件（非档案相对人现场办理时提供）。</p>
              <!--              办理流程-->
              <p style="font-weight: bold;">三、办理流程</p>
              <p>（一）网上办理流程</p>
              <p>登录四川省流动人员人事档案公共服务平台（http://103.203.218.132:8086/）→用户注册→流动人员人事档案→档案转接→填写信息→上传本人《身份证》和接收单位出具的有效《调档函》→选择在线办理→档案所在公共人才服务机构审核→机要邮寄档案→调档单位查收→本人联系接收单位确认是否到档。</p>
              <p>（二）现场办理流程</p>
              <p>1、个人或单位携申请材料到泸州市人才服务中心提出申请。</p>
              <p>2、工作人员审核申请材料是否齐全且符合要求，对申请事项不符合要求的，一次性告知申请人补正。</p>
              <p>3、工作人员审查材料无误后办理档案转出手续。</p>
              <!--              办结时限-->
              <p style="font-weight: bold;">四、办结时限</p>
              <p>即时办结。</p>
              <!--              办理地点-->
              <p style="font-weight: bold;">五、办理时间、地点</p>
              <p>（一）办理时间：星期一至星期五（法定节假日除外）：上午09:30-12:00，下午2:30-6:30。</p>
              <p>（二）办理地点：泸州市江阳西路飞跃街2号泸州市人才服务中心四楼人事档案管理科。</p>
              <!--              联系方式-->
              <p style="font-weight: bold;">六、联系方式</p>
              <p>（一）联系电话：0830-3156708、0830-3158737。</p>

            </div>
            <div class="button-div">
              <a href="http://103.203.218.132:8086/funcService/ServiceGuide.html?openid=2&servId=70" style="font-size: 16pt;">
                <el-button>立即办理</el-button>
              </a>
            </div>
          </div>
        </el-tab-pane>
        <!--        档案材料收集归档-->
        <el-tab-pane name="4">
          <div slot="label">
            <span class="text-span">档案材料收集归档</span>
          </div>
          <div class="tabs-content">
            <div class="title">
              <span>服务指南</span>
            </div>
            <div class="text-content">
              <!--              申请条件-->
              <p style="font-weight: bold;">一、申请条件</p>
              <p>人事档案委托管理在泸州市人才服务中心的人员。</p>
              <!--              申请材料-->
              <p style="font-weight: bold;">二、申请材料</p>
              <p>（一）档案相对人《身份证》。</p>
              <p>（二）归档材料原件（袋装密封）。</p>
              <p>（三）《单位介绍信》及经办人有效身份证件或工作证原件（单位专人现场办理时提供）。</p>
              <p>（四）《委托书》及委托双方有效身份证件（非档案相对人现场办理时提供）。</p>
              <!--              办理流程-->
              <p style="font-weight: bold;">三、办理流程</p>
              <p>（一）网上办理流程</p>
              <p>登录四川省流动人员人事档案公共服务平台（http://103.203.218.132:8086/）→用户注册→流动人员人事档案→档案材料补充→填写信息→上传本人《身份证》、由材料形成单位密封盖章的归档材料袋正反面→密封完好的归档材料邮寄至档案所在公共人才服务机构审核→公共人才服务机构将符合条件的材料归入本人人事档案。</p>
              <p>（二）现场办理流程</p>
              <p>1、个人或单位携申请材料到泸州市人才服务中心提出申请。</p>
              <p>2、工作人员审核申请材料是否齐全且符合要求，对申请事项不符合要求的，一次性告知申请人或材料形成单位补正。</p>
              <p>3、工作人员审查材料无误后将符合要求的档案材料归入本人人事档案。</p>
              <!--              办结时限-->
              <p style="font-weight: bold;">四、办结时限</p>
              <p>即时办结。</p>
              <!--              办理地点-->
              <p style="font-weight: bold;">五、办理时间、地点</p>
              <p>（一）办理时间：星期一至星期五（法定节假日除外）：上午09:30-12:00，下午2:30-6:30。</p>
              <p>（二）办理地点：泸州市江阳西路飞跃街2号泸州市人才服务中心四楼人事档案管理科。</p>
              <!--              联系方式-->
              <p style="font-weight: bold;">六、联系方式</p>
              <p>（一）联系电话：0830-3156708、0830-3158737。</p>

            </div>
            <div class="button-div">
              <a href="http://103.203.218.132:8086/funcService/ServiceGuide.html?openid=2&servId=68" style="font-size: 16pt;">
                <el-button>立即办理</el-button>
              </a>
            </div>
          </div>
        </el-tab-pane>
        <!--        档案查 (借) 阅-->
        <el-tab-pane name="5">
          <div slot="label">
            <span class="text-span">档案查 (借) 阅</span>
          </div>
          <div class="tabs-content">
            <div class="title">
              <span>服务指南</span>
            </div>
            <div class="text-content">
              <!--              申请条件-->
              <p style="font-weight: bold;">一、申请条件</p>
              <p>人事档案委托管理在泸州市人才服务中心的人员。</p>
              <!--              申请材料-->
              <p style="font-weight: bold;">二、申请材料</p>
              <p>（一）《单位介绍信》或《查（借）阅函》。</p>
              <p>（二）档案相对人《身份证》（个人网上申请时提供）。</p>
              <p>（三）查（借）阅单位经办人员有效身份证件或工作证原件（单位专人现场办理时提供）。</p>
              <!--              办理流程-->
              <p style="font-weight: bold;">三、办理流程</p>
              <p>（一）网上办理流程</p>
              <p>登录四川省流动人员人事档案公共服务平台（http://103.203.218.132:8086/）→用户注册→流动人员人事档案→档案查（借）阅服务→填写信息→上传《身份证》、有查（借）阅权限单位出具的有效《介绍信》或《查（借）阅函》→公共人才服务机构审核→办理查（借）阅登记→将需查（借）阅档案机要寄送至网上预留的有效查（借）阅单位收件地址→收件人查收。</p>
              <p>（二）现场办理流程</p>
              <p>1、查（借）阅单位携申请材料到泸州市人才服务中心提出申请。</p>
              <p>2、工作人员审核申请材料是否齐全且符合要求，对申请事项不符合要求的，一次性告知申请人补正。</p>
              <p>3、工作人员审查材料无误后，按照规定和需要提供档案材料查（借）阅服务。</p>
              <!--              办结时限-->
              <p style="font-weight: bold;">四、办结时限</p>
              <p>即时办结。</p>
              <!--              办理地点-->
              <p style="font-weight: bold;">五、办理时间、地点</p>
              <p>（一）办理时间：星期一至星期五（法定节假日除外）：上午09:30-12:00，下午2:30-6:30。</p>
              <p>（二）办理地点：泸州市江阳西路飞跃街2号泸州市人才服务中心四楼人事档案管理科。</p>
              <!--              联系方式-->
              <p style="font-weight: bold;">六、联系方式</p>
              <p>（一）联系电话：0830-3156708、0830-3158737。</p>

            </div>
            <div class="button-div">
              <a href="http://103.203.218.132:8086/funcService/ServiceGuide.html?openid=2&servId=61" style="font-size: 16pt;">
                <el-button>立即办理</el-button>
              </a>
            </div>
          </div>
        </el-tab-pane>
        <!--        政审考察-->
        <el-tab-pane name="6">
          <div slot="label">
            <span class="text-span">政审考察</span>
          </div>
          <div class="tabs-content">
            <div class="title">
              <span>服务指南</span>
            </div>
            <div class="text-content">
              <!--              申请条件-->
              <p style="font-weight: bold;">一、申请条件</p>
              <p>人事档案委托管理在泸州市人才服务中心的人员。</p>
              <!--              申请材料-->
              <p style="font-weight: bold;">二、申请材料</p>
              <p>（一）《单位介绍信》或《政审函》。</p>
              <p>（二）档案相对人《身份证》（个人网上申请时提供）。</p>
              <p>（三）政审（考察）单位经办人员有效身份证件或工作证原件（单位专人现场办理时提供）。</p>
              <!--              办理流程-->
              <p style="font-weight: bold;">三、办理流程</p>
              <p>（一）网上办理流程</p>
              <p>登录四川省流动人员人事档案公共服务平台（http://103.203.218.132:8086/）→用户注册→流动人员人事档案→政审（考察）服务→填写信息→上传本人《身份证》和有政审权限单位出具的有效《介绍信》→公共人才服务机构审核→出具政审意见→机要寄送政审意见至政审单位的收件地址→收件人查收。</p>
              <p>（二）现场办理流程</p>
              <p>1、政审（考察）单位携申请材料到泸州市人才服务中心提出申请。</p>
              <p>2、工作人员审核申请材料是否齐全且符合要求，对申请事项不符合要求的，一次性告知申请人补正。</p>
              <p>3、工作人员审查材料无误后，按照规定和需要提供档案材料政审（考察）服务，依据档案记载出具政审意见。</p>
              <!--              办结时限-->
              <p style="font-weight: bold;">四、办结时限</p>
              <p>即时办结。</p>
              <!--              办理地点-->
              <p style="font-weight: bold;">五、办理时间、地点</p>
              <p>（一）办理时间：星期一至星期五（法定节假日除外）：上午09:30-12:00，下午2:30-6:30。</p>
              <p>（二）办理地点：泸州市江阳西路飞跃街2号泸州市人才服务中心四楼人事档案管理科。</p>
              <!--              联系方式-->
              <p style="font-weight: bold;">六、联系方式</p>
              <p>（一）联系电话：0830-3156708、0830-3158737。</p>

            </div>
            <div class="button-div">
              <a href="http://103.203.218.132:8086/funcService/ServiceGuide.html?openid=2&servId=71" style="font-size: 16pt;">
                <el-button>立即办理</el-button>
              </a>
            </div>
          </div>
        </el-tab-pane>
        <!--        出具证明-->
        <el-tab-pane name="7">
          <div slot="label">
            <span class="text-span">出具证明</span>
          </div>
          <div class="tabs-content">
            <div class="title">
              <span>服务指南</span>
            </div>
            <div class="text-content">
              <!--              申请条件-->
              <p style="font-weight: bold;">一、申请条件</p>
              <p>（一）人事档案委托管理在泸州市人才服务中心的人员。</p>
              <p>（二）档案内有需要证明事项的相关记载。  </p>
              <!--              申请材料-->
              <p style="font-weight: bold;">二、申请材料</p>
              <p>（一）档案相对人《身份证》。</p>
              <p>（二）《单位介绍信》及经办人有效身份证件或工作证原件（单位专人现场办理时提供）。</p>
              <p>（三）《委托书》及委托双方有效身份证件（非档案相对人现场办理时提供）。</p>
              <!--              办理流程-->
              <p style="font-weight: bold;">三、办理流程</p>
              <p>（一）网上办理流程</p>
              <p>登录四川省流动人员人事档案公共服务平台（http://103.203.218.132:8086/）→用户注册→流动人员人事档案→出具相关证明→填写信息→选择出具证明类型→上传本人《身份证》→公共人才服务机构审核→依据档案记载出具相关证明→邮寄证明至本人网上预留的收件地址→收件人查收。</p>
              <p>（二）现场办理流程</p>
              <p>1、个人或单位携申请材料到泸州市人才服务中心提出申请。</p>
              <p>2、工作人员审核申请材料是否齐全且符合要求，对申请事项不符合要求的，一次性告知申请人补正。</p>
              <p>3、工作人员审查材料无误后，依据档案记载出具相关证明材料。</p>
              <!--              办结时限-->
              <p style="font-weight: bold;">四、办结时限</p>
              <p>即时办结。</p>
              <!--              办理地点-->
              <p style="font-weight: bold;">五、办理时间、地点</p>
              <p>（一）办理时间：星期一至星期五（法定节假日除外）：上午09:30-12:00，下午2:30-6:30。</p>
              <p>（二）办理地点：泸州市江阳西路飞跃街2号泸州市人才服务中心四楼人事档案管理科。</p>
              <!--              联系方式-->
              <p style="font-weight: bold;">六、联系方式</p>
              <p>（一）联系电话：0830-3156708、0830-3158737。</p>

            </div>
            <div class="button-div">
              <a href="http://103.203.218.132:8086/funcService/ServiceGuide.html?openid=2&servId=60" style="font-size: 16pt;">
                <el-button>立即办理</el-button>
              </a>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
  </div>
</template>

<script>

export default {
  name: 'fileServiceView',
  data(){
    return{
      //办事流程弹窗
      activeName: "1",
    }
  },
  methods:{
  }

}

</script>


<style lang="scss" scoped>
.content{
  width: 75rem;
  margin: 20px auto;

  .tabs-content{
    .title{
      width: auto;
      height: 36px;
      border-bottom: 1px solid #ececec;
      span {
        padding: 0 15px;
        float: left;
        font-size: 16px;
        color: #054b92;
        line-height: 36px;
        border-bottom: 2px solid #054b92;
      }
    }
    .text-content{
      width: auto;
      padding-top: 10px;
      p {
        text-indent: 32pt;
        text-align: justify;
        line-height: 1.5;
        font-size: 15pt;
        font-family: 'FangSong', 'FangSong_GB2312',serif;
        color: black;
        a{
          text-indent: 32pt;
          text-align: justify;
          line-height: 1.5;
          font-size: 15pt;
          font-family: 'FangSong', 'FangSong_GB2312',serif;
          color: black;
        }
      }
    }
    .button-div{
      text-align: center;
      margin: 50px 0;
    }
  }
  /*居中*/
  ::v-deep .el-tabs--left {
    .el-tabs__header.is-left {
      width: 258px;
    }
    .el-tabs__item.is-left {
      text-align: center;
      background: #f3f3f3;
      border-bottom: 1px solid #fff;
      height: 48px;
      font-size: 15px;
      color: #000;
      line-height: 48px;
    }
    .el-tabs__item:hover {
      color: white;
      background-color: #0758CB;
    }
    /*选中后颜色*/
    .el-tabs__item.is-active {
      color: white;
      background-color: #0758CB;
    }
  }

}

</style>
