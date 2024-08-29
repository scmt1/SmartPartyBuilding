<template>
  <!--  企业业务申报-->
  <iFrame :iframeSrc="newSrc"></iFrame>

<!--  <div class="matter-page">
    &lt;!&ndash;    标题&ndash;&gt;
&lt;!&ndash;    <div class="title">业务申报管理</div>&ndash;&gt;
    &lt;!&ndash;    主要内容&ndash;&gt;
    <div class="matter-content">
      <el-tabs tab-position="left" style="height: 100%">
        <el-tab-pane v-for="(item,num) in menusName" :key="num">
          <div slot="label" @click="changeColor(num)">
            <span > {{item}}</span>
          </div>

          <div class="right">
            <el-tabs
              v-model="activeName"
            >
              <el-tab-pane v-for="(item,index) in nameList" :key="index" :value="item" :name="item">
                <div slot="label">
                  <span> {{labelList[index]}}</span>
                </div>
                &lt;!&ndash;        表格&ndash;&gt;
                <el-table :data="tableData"
                          class="table-style"
                          style="width: 100%;"
                          :header-cell-style="{'text-align':'center'}"
                          :cell-style="{'text-align':'center'}"
                          stripe>
                  <el-table-column prop="name" label="业务名字" width="160">
                    <template slot-scope="scope">
                      <span> {{scope.row.name}}</span>
                    </template>
                  </el-table-column>
                  <el-table-column prop="status" label="业务状态">
                    <template slot-scope="scope">
                      <span style="color: #1AD21F"> {{scope.row.status}}</span>
                    </template>
                  </el-table-column>
                  <el-table-column prop="organization" label="发布组织" width="130">
                    <template slot-scope="scope">
                      <span> {{scope.row.organization}}</span>
                    </template>
                  </el-table-column>
                  <el-table-column prop="type" label="业务类型" width="90" v-if="num===0"></el-table-column>
                  <el-table-column prop="progress" label="审核进度" v-if="num!==0">
                    <template slot-scope="scope">
                      <el-button style="color: #0256CA" type="text" @click="getStatusAndColor">
                        查看<br>审核进度
                      </el-button>
                    </template>
                  </el-table-column>
                  <el-table-column prop="date" label="时间" width="130">
                    <template slot-scope="scope">
                      <span> {{scope.row.date}}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="150">
                    <template slot-scope="scope">
                      <div v-if="num===0">
                        <el-button style="color: #0256CA" type="text">查看<br>申报指南</el-button>
                        <el-button style="color: #0256CA" type="text">查看<br>人员名单</el-button>
                      </div>
                      <div v-else>
                        <el-button style="color: #0256CA" type="text">查看<br>申报信息</el-button>
                        <el-button v-if="index!==1" style="color: #0256CA" type="text">删除<br>申报信息</el-button>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column label="上报" v-if="num===0" width="100">
                    <template slot-scope="scope">
                      <div style="height: 20px;position: relative; top: 15px">
                        <el-image :src="image"></el-image>
                      </div>
                      <el-popover
                        placement="left"
                        width="293"
                        trigger="click"
                        >
                        <span style="font-size: 14px;
                                    font-family: 'PingFang SC', 'PingFang SC-Medium';
                                    font-weight: 500;
                                    text-align: left;
                                    color: red">
                          您还有未被审核的申报表，无法上报提交
                        </span>
                        <el-button slot="reference" style="color: #0256CA" type="text">确认上报</el-button>
                      </el-popover>
                    </template>
                  </el-table-column>
                </el-table>
              </el-tab-pane>
              <div style="text-align: right">
                <el-pagination
                  background
                  layout="prev, pager, next"
                  :total="30">
                </el-pagination>
              </div>
            </el-tabs>
            <div class="div-box" v-if="num===0">
              <el-image :src="image" style="position: relative;top: 9px"></el-image>
              <span>表示还有未被审核的申报表，上报无法提交</span>
            </div>
            &lt;!&ndash;        搜索条件&ndash;&gt;
            <div class="div-box" v-else >
              <span style="margin-right: 0.726rem">选择年月:</span>
              <el-date-picker
                style="margin-right: 0.538rem"
                v-model="value1"
                type="date"
                placeholder="请选择需要查看的年月">
              </el-date-picker>
              <el-button style="background-color: #0256CA;color: #FFFFFF">查询</el-button>
              <el-button>重置</el-button>
            </div>

          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    &lt;!&ndash;    弹窗&ndash;&gt;
    <el-dialog
      title="审核进度"
      :visible.sync="centerDialogVisible"
      width="47.625rem"
      center>
      <div style="text-align: center">
        <h2 :style="'color:'+color">{{ title }}</h2>
      </div>
      <div class="dialog-content">
        <div class="line-style dialog-left">
          <span class="move" v-if="activeName==='4'">最终节点</span>
        </div>
        <div class="line-style dialog-right">
          <div v-for="(item,index) in processData"
               :style="{
               borderWidth: index===processData.length-1?'0':'0  0 0 2px' ,
               position: 'relative',
               left: index===processData.length-1?'2px':'0' ,
               borderColor: 'rgb(5, 91, 194,.35)',
               borderStyle: 'solid',
               height: '85px'}"
          >
            <div class="line-style circle move"></div>
            <span class="line-style move" :style="index===0?'':'color: #333333;'+'margin-left: 17px'">{{ item.date }}</span>
            <div class="line-style move" style="vertical-align: top;width: 311px;margin-left: 2.4375rem">
              <span :style="index===0?'':'color: #333333;'">{{ item.massage }}</span>
            </div>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">返回</el-button>
      </span>
    </el-dialog>
  </div>-->
</template>

<script>
import iFrame from "@/components/iFrame/full";

export default {
  name: 'matter',
  components: { iFrame },
  data() {
    return {
      newSrc:process.env.VUE_APP_YSZJ_ADDRESS+"/rcfw/web/view/enterprise.html",
      /*
      //侧边栏
      menusName: ['个人申报审核','团队服务管理','企业服务管理'],
      num: 0,

      centerDialogVisible: false,
      value1: '',
      activeName: '1',
      nameList: ['1','2','3','4'],
      labelList: ['人才引进','人才培养','人才激励','人才服务保障'],
      labelTdQyList: ['待审核','审核中','已通过','未通过'],
      labelGrList: ['人才引进','人才培养','人才激励','人才服务保障'],
      //表格 todo:有数据后从后台取
      tableData: [
        {
          name: '2022年度高层次人才\n' +
            '岗位激励个人申报',
          status: '进行中',
          organization: '泸州市人才工作\n' +
            '领导小组办公室',
          type: '人才激励',
          progress: '查看\n' +
            '审核进度',
          date: '2022-01-01\n' +
            '至\n' +
            '2022-12-31',
        },
        {
          name: '2022年度高层次人才\n' +
            '岗位激励个人申报',
          status: '进行中',
          organization: '泸州市人才工作\n' +
            '领导小组办公室',
          type: '人才激励',
          progress: '查看\n' +
            '审核进度',
          date: '2022-01-01\n' +
            '至\n' +
            '2022-12-31',
        },
        {
          name: '2022年度高层次人才\n' +
            '岗位激励个人申报',
          status: '进行中',
          organization: '泸州市人才工作\n' +
            '领导小组办公室',
          type: '人才激励',
          progress: '查看\n' +
            '审核进度',
          date: '2022-01-01\n' +
            '至\n' +
            '2022-12-31',
        },
        {
          name: '2022年度高层次人才\n' +
            '岗位激励个人申报',
          status: '进行中',
          organization: '泸州市人才工作\n' +
            '领导小组办公室',
          type: '人才激励',
          progress: '查看\n' +
            '审核进度',
          date: '2022-01-01\n' +
            '至\n' +
            '2022-12-31',
        },
        {
          name: '2022年度高层次人才\n' +
            '岗位激励个人申报',
          status: '进行中',
          organization: '泸州市人才工作\n' +
            '领导小组办公室',
          type: '人才激励',
          progress: '查看\n' +
            '审核进度',
          date: '2022-01-01\n' +
            '至\n' +
            '2022-12-31',
        },
      ],
      //个人申报审核
      tableData1: [
        {
          name: '2022年度高层次人才\n' +
            '岗位激励个人申报',
          status: '进行中',
          organization: '泸州市人才工作\n' +
            '领导小组办公室',
          type: '人才激励',
          progress: '查看\n' +
            '审核进度',
          date: '2022-01-01\n' +
            '至\n' +
            '2022-12-31',
        },
        {
          name: '2022年度高层次人才\n' +
            '岗位激励个人申报',
          status: '进行中',
          organization: '泸州市人才工作\n' +
            '领导小组办公室',
          type: '人才激励',
          progress: '查看\n' +
            '审核进度',
          date: '2022-01-01\n' +
            '至\n' +
            '2022-12-31',
        },
        {
          name: '2022年度高层次人才\n' +
            '岗位激励个人申报',
          status: '进行中',
          organization: '泸州市人才工作\n' +
            '领导小组办公室',
          type: '人才激励',
          progress: '查看\n' +
            '审核进度',
          date: '2022-01-01\n' +
            '至\n' +
            '2022-12-31',
        },
        {
          name: '2022年度高层次人才\n' +
            '岗位激励个人申报',
          status: '进行中',
          organization: '泸州市人才工作\n' +
            '领导小组办公室',
          type: '人才激励',
          progress: '查看\n' +
            '审核进度',
          date: '2022-01-01\n' +
            '至\n' +
            '2022-12-31',
        },
        {
          name: '2022年度高层次人才\n' +
            '岗位激励个人申报',
          status: '进行中',
          organization: '泸州市人才工作\n' +
            '领导小组办公室',
          type: '人才激励',
          progress: '查看\n' +
            '审核进度',
          date: '2022-01-01\n' +
            '至\n' +
            '2022-12-31',
        },
      ],
      //团队服务管理
      tableData2: [
        {
          name: '2023年度酒城创新团队申报',
          status: '进行中',
          organization: '泸州市科学技术和人才工作局',
          type: '人才激励',
          progress: '查看\n' +
            '审核进度',
          date: '2022-01-01\n' +
            '至\n' +
            '2022-12-31',
        },
        {
          name: '2023年度技术合同认定登记申报',
          status: '进行中',
          organization: '泸州市科学技术和人才工作局',
          type: '人才激励',
          progress: '查看\n' +
            '审核进度',
          date: '2022-01-01\n' +
            '至\n' +
            '2022-12-31',
        },
        {
          name: '2023市级科技计划项目申报、立项、验收等事项申报',
          status: '进行中',
          organization: '泸州市科学技术和人才工作局',
          type: '人才激励',
          progress: '查看\n' +
            '审核进度',
          date: '2022-01-01\n' +
            '至\n' +
            '2022-12-31',
        },
        {
          name: '2023创新券补助申领',
          status: '进行中',
          organization: '泸州市科学技术和人才工作局',
          type: '人才激励',
          progress: '查看\n' +
            '审核进度',
          date: '2022-01-01\n' +
            '至\n' +
            '2022-12-31',
        },
        {
          name: '2023年度酒城创新团队申报',
          status: '进行中',
          organization: '泸州市科学技术和人才工作局',
          type: '人才激励',
          progress: '查看\n' +
            '审核进度',
          date: '2022-01-01\n' +
            '至\n' +
            '2022-12-31',
        },
      ],
      //企业服务管理
      tableData3: [
        {
          name: '2023年度市级企业技术中心申报',
          status: '进行中',
          organization: '泸州市科学技术和人才工作局',
          type: '人才激励',
          progress: '查看\n' +
            '审核进度',
          date: '2022-01-01\n' +
            '至\n' +
            '2022-12-31',
        },
        {
          name: '2023年度市级众创空间备案',
          status: '进行中',
          organization: '泸州市科学技术和人才工作局',
          type: '人才激励',
          progress: '查看\n' +
            '审核进度',
          date: '2022-01-01\n' +
            '至\n' +
            '2022-12-31',
        },
        {
          name: '2023年度市级工程技术研究中心认定申报',
          status: '进行中',
          organization: '泸州市科学技术和人才工作局',
          type: '人才激励',
          progress: '查看\n' +
            '审核进度',
          date: '2022-01-01\n' +
            '至\n' +
            '2022-12-31',
        },
        {
          name: '2023年度市级重点实验室认定申报',
          status: '进行中',
          organization: '泸州市科学技术和人才工作局',
          type: '人才激励',
          progress: '查看\n' +
            '审核进度',
          date: '2022-01-01\n' +
            '至\n' +
            '2022-12-31',
        },
        {
          name: '2023年度泸州市成果转化项目申报申报',
          status: '进行中',
          organization: '泸州市科学技术和人才工作局',
          type: '人才激励',
          progress: '查看\n' +
            '审核进度',
          date: '2022-01-01\n' +
            '至\n' +
            '2022-12-31',
        },
      ],
      //审核进度
      processData: [
        {
          massage: '[泸州市人才工作领导小组办公室] 审核通过审核意见: 资料完整，同意通过。',
          date: '2021-10-14 08:34:22',
        },
        {
          massage: '[泸州市国有资产监督管理委员会] 审核通过审核意见: 同意通过',
          date: '2021-10-13 08:34:22',
        },
        {
          massage: '[数字泸州产业投资集团有限公司] 审核通过审核意见: 同意通过',
          date: '2021-10-13 08:34:22',
        },
        {
          massage: '[吴通]提交申报材料',
          date: '2021-10-11 08:34:22',
        },
      ],
      processData1: [
        {
          massage: '[吴通]提交申报材料',
          date: '2021-10-11 08:34:22',
        },
      ],
      processData2: [
        {
          massage: '[数字泸州产业投资集团有限公司] 审核通过审核意见: 同意通过',
          date: '2021-10-13 08:34:22',
        },
        {
          massage: '[吴通]提交申报材料',
          date: '2021-10-11 08:34:22',
        },
      ],
      processData3: [
        {
          massage: '[泸州市人才工作领导小组办公室] 审核通过审核意见: 资料完整，同意通过。',
          date: '2021-10-14 08:34:22',
        },
        {
          massage: '[泸州市国有资产监督管理委员会] 审核通过审核意见: 同意通过',
          date: '2021-10-13 08:34:22',
        },
        {
          massage: '[数字泸州产业投资集团有限公司] 审核通过审核意见: 同意通过',
          date: '2021-10-13 08:34:22',
        },
        {
          massage: '[吴通]提交申报材料',
          date: '2021-10-11 08:34:22',
        },
      ],
      processData4: [
        {
          massage: '[泸州市人才工作领导小组办公室] 审核通过审核意见: 资料完整，同意通过。',
          date: '2021-10-14 08:34:22',
        },
        {
          massage: '[泸州市国有资产监督管理委员会] 审核通过审核意见: 同意通过',
          date: '2021-10-13 08:34:22',
        },
        {
          massage: '[数字泸州产业投资集团有限公司] 审核通过审核意见: 同意通过',
          date: '2021-10-13 08:34:22',
        },
        {
          massage: '[吴通]提交申报材料',
          date: '2021-10-11 08:34:22',
        },
      ],
      //个人申报审核进度
      processData9: [
        {
          massage: '[吴通]提交申报材料',
          date: '2021-10-11 08:34:22',
        },
      ],
      processData10: [
        {
          massage: '[数字泸州产业投资集团有限公司] 审核通过审核意见: 同意通过',
          date: '2021-10-13 08:34:22',
        },
        {
          massage: '[吴通]提交申报材料',
          date: '2021-10-11 08:34:22',
        },
      ],
      processData11: [
        {
          massage: '[泸州市人才工作领导小组办公室] 审核通过审核意见: 资料完整，同意通过。',
          date: '2021-10-14 08:34:22',
        },
        {
          massage: '[泸州市国有资产监督管理委员会] 审核通过审核意见: 同意通过',
          date: '2021-10-13 08:34:22',
        },
        {
          massage: '[数字泸州产业投资集团有限公司] 审核通过审核意见: 同意通过',
          date: '2021-10-13 08:34:22',
        },
        {
          massage: '[吴通]提交申报材料',
          date: '2021-10-11 08:34:22',
        },
      ],
      processData12: [
        {
          massage: '[泸州市人才工作领导小组办公室] 审核通过审核意见: 资料完整，同意通过。',
          date: '2021-10-14 08:34:22',
        },
        {
          massage: '[泸州市国有资产监督管理委员会] 审核通过审核意见: 同意通过',
          date: '2021-10-13 08:34:22',
        },
        {
          massage: '[数字泸州产业投资集团有限公司] 审核通过审核意见: 同意通过',
          date: '2021-10-13 08:34:22',
        },
        {
          massage: '[吴通]提交申报材料',
          date: '2021-10-11 08:34:22',
        },
      ],
      //企业团队审核进度
      processData5: [
        {
          massage: '[数字泸州产业投资集国有限公司] 提交申报资料',
          date: '2021-10-13 08:34:22',
        },
      ],
      processData6: [
        {
          massage: '[泸州市国有资产监督管理委员会] 审核通过审核意见:同意通过',
          date: '2021-10-13 08:34:22',
        },
        {
          massage: '[数字泸州产业投资集国有限公司] 提交申报资料',
          date: '2021-10-13 08:34:22',
        },
      ],
      processData7: [
        {
          massage: '[泸州市人才工作领导小组办公室] 审核通过审核意见:资料完整，同意通过',
          date: '2021-10-14 08:34:22',
        },
        {
          massage: '[泸州市国有资产监督管理委员会] 审核通过审核意见:同意通过',
          date: '2021-10-13 08:34:22',
        },
        {
          massage: '[数字泸州产业投资集国有限公司] 提交申报资料',
          date: '2021-10-13 08:34:22',
        },
      ],
      processData8: [
        {
          massage: '[泸州市人才工作领导小组办公室] 审核未通过审核意见: 资料不完整，不予通过',
          date: '2021-10-14 08:34:22',
        },
        {
          massage: '[泸州市国有资产监督管理委员会] 审核通过审核意见:同意通过',
          date: '2021-10-13 08:34:22',
        },
        {
          massage: '[数字泸州产业投资集国有限公司] 提交申报资料',
          date: '2021-10-13 08:34:22',
        },
      ],
      image: require('@/assets/images/portal/center/mapper_tanhao.png'),

      title: '',
      color: '',*/
    }
  },
  methods: {
    //状态值
   /* getStatusAndColor(){
      let title=''
      let color=''
      switch (this.activeName){
        case '1': title='待审核';color='#FF9C01';this.processData=this.processData1
          break
        case '2': title='审核中';color='#0256CA';this.processData=this.processData2
          break
        case '3': title='已通过';color='#28DC1F';this.processData=this.processData3
          break
        case '4': title='未通过';color='#E71111';this.processData=this.processData4
          break
      }
      this.title=title
      this.color=color
      this.centerDialogVisible = true
    },
    changeColor(num){
      this.num=num
      if (num===0){
        this.tableData=this.tableData1
        this.processData1=this.processData9
        this.processData2=this.processData10
        this.processData3=this.processData11
        this.processData4=this.processData12
        this.labelList=this.labelGrList
      }else if (num===1){
        this.tableData=this.tableData2
        this.processData1=this.processData5
        this.processData2=this.processData6
        this.processData3=this.processData7
        this.processData4=this.processData8
        this.labelList=this.labelTdQyList
      }else if (num===2){
        this.tableData=this.tableData3
        this.processData1=this.processData5
        this.processData2=this.processData6
        this.processData3=this.processData7
        this.processData4=this.processData8
        this.labelList=this.labelTdQyList
      }
    },*/
  }
}
</script>

<style lang="scss" scoped>
.matter-page{
  background-color: white;
  width: 75rem;
  margin: 0 auto 0 auto;
  .title {
    width: 200px;
    height: 70px;
    font-size: 26px;
    font-family: 'PingFang SC', 'PingFang SC-Bold';
    font-weight: 700;
    text-align: center;
    color: #333333;
    line-height: 70px;
    /*border: 1px solid black;*/
  }

  .matter-content{
    padding-top: 20px;
    width: 98%;
    height: 32.6875rem;
    ::v-deep .el-tabs--left .el-tabs__header.is-left {
      width: 238px;

      .el-tabs__active-bar {
        left: 0;
      }

      .el-tabs__nav-wrap::after {
        display: none;
      }

      .el-tabs__item {
        padding: 5px 20px;
        height: 50px;
        font-size: 16px;
        font-family: 'PingFang SC', 'PingFang SC-Bold';
        font-weight: 700;
        text-align: left;
      }

      .el-tabs__item:hover {
        color: #0256CA;
        cursor: pointer;
        background-color: #E4F2FE;
      }
      .el-tabs__item.is-active {
        color: #0256CA;
        border-width: 0 0 0 2px;
        border-style: solid;
        border-radius: 0;
        border-color: #0256CA;
        background-color: #E4F2FE;
      }
      .el-tabs__active-bar {
        background-color: #0256CA;
      }
    }

    .right{
      display: inline-block;
      vertical-align: top;
      margin-left: 1.875rem;
      //width: 57.9375rem;
      width: 56.1625rem;
      height: 100%;

      .div-box{
        position: relative;
        text-align: right;
        bottom: 485px;
        right: 15px;
        span{
          font-size: 14px;
          font-family: 'PingFang SC', 'PingFang SC-Bold';
          font-weight: 700;
          color: #333333;
        }
        /*日期选择器*/
        ::v-deep .el-input--medium .el-input__inner {
          height: 28px;
          line-height: 28px;
        }
        ::v-deep .el-input__prefix {
          top: -4px;
        }
        ::v-deep .el-input__inner {
          border: 1px solid #e9eaed;
        }
        /*按钮*/
        .el-button{
          width: 3.4375rem;
          height: 1.75rem;
        }
        .el-button--medium {
          padding: 5px 10px;
          border-radius: 0;
        }

      }

      /*表格*/
      .table-style{
        span{
          word-wrap:break-word;
          white-space: pre-wrap;
        }
        .el-button{
          line-height: 1.5;
        }
      }
      ::v-deep .el-table--medium .el-table__cell {
        padding: 2px;
      }
      ::v-deep .el-table__cell {
        border-bottom: 0 solid #dfe6ec;
      }

      //tabs卡片
      ::v-deep .el-tabs__item {
        font-size: 18px;
        font-family: 'PingFang SC', 'PingFang SC-Bold';
        font-weight: 700;
        text-align: left;
        color: #333333;
      }
      ::v-deep .el-tabs__item:hover {
        color: #1f7bd5;
      }
      ::v-deep .el-tabs__item.is-active {
        color: #1f7bd5;
      }
      //选中时的下划线
      ::v-deep .el-tabs__active-bar.is-top {
        background: #1f7bd5;
      }
      ::v-deep .el-tabs__nav-wrap::after {
        height: 2px;
        background-color: #333333;
      }
    }
  }

  /*弹窗*/
  h2{

  }
  .dialog-content{
    margin-top: 33px;
    min-height: 19.8125rem;
    .dialog-left{
      width: 7.1875rem;
      text-align: center;
    }
    .dialog-right{
      min-height: 15.865rem;
      .circle{
        height:13px;/*与width设置一致*/
        width:13px;
        background:#055bc2;
        border-radius:50px;
        margin:0 auto;
      }
    }
    .line-style{
      display: inline-block;
      vertical-align: top;
    }
    .move{
      position: relative;
      right: 7.5px;
      bottom: 1px;
    }
    span{
      font-size: 14px;
      font-family: 'PingFang SC', 'PingFang SC-Medium';
      font-weight: 500;
      color: #055bc2;
    }
    .other-span{
      color: #333333;
    }
  }
  .dialog-footer{
    .el-button{
      width: 100px;
      height: 28px;
      background: #0256ca;
      color: #ffffff;
      font-size: 14px;
      font-family: 'PingFang SC', 'PingFang SC-Bold';
      font-weight: 700;
      padding: 5px 20px 5px 20px;
    }
  }
}

</style>
