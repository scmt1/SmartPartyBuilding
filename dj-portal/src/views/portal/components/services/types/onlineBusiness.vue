<template>
    <div class="page">

      <div class="content">
        <div>
          <h1 class="h1">专业技术资格证书(评审、考核认定取得)补（换）发</h1>
          <div>
            <el-steps :active="0" finish-status="wait">
              <el-step title="条件自检"></el-step>
              <el-step title="填写表单"></el-step>
              <el-step title="上传材料"></el-step>
              <el-step title="完成申请"></el-step>
            </el-steps>
          </div>
          <!--        申请条件-->
          <div class="margin-top">
            <h2 class="h2">申请条件</h2>
            <span class="span">
            满足下列条件的，予以办理：由泸州市人力资源和社会保障局或原泸州市职称管理办公室审批发放的专业技术资格证书已遗失或破损，符合补发、换发条件。
          </span>
        </div>
<!--        温馨提示-->
        <div class="margin-top">
          <h2  class="h2">温馨提示</h2>
          <p style="line-height: 28px;">
            {{text}}
          </p>
        </div>
<!--        通知公告-->
        <div class="margin-top">
          <h2 class="h2">通知公告</h2>
          <el-table :data="tableData"
                    :show-header="false">
            <el-table-column type="index" width="50"></el-table-column>
            <el-table-column prop="title" label="姓名">
              <template slot-scope="scope">
                <a href="https://hrsspub.sz.gov.cn/rcyth/handbook/ZJJSZG_GR_czsc20200117.pdf" target="_blank">专业技术资格证书(评审、考核认定取得)补（换）发系统操作手册（个人申报操作）</a>
              </template>
            </el-table-column>
            <el-table-column prop="date" label="日期" width="180"></el-table-column>
          </el-table>
        </div>
<!--        申报人基本信息-->
        <div class="margin-top">
          <h2 class="h2">申报人基本信息</h2>
          <el-form ref="form" :model="form" label-width="80px">
            <el-form-item label="申报人">
              <el-input v-model="form.name" disabled></el-input>
            </el-form-item>
            <el-form-item label="证件号码">
              <el-select v-model="form.cardType" placeholder="请选择活动区域" style="width: 270px;" disabled>
                <el-option label="区域一" value="shanghai"></el-option>
                <el-option label="区域二" value="beijing"></el-option>
              </el-select>
              <el-input v-model="form.cardNum" style="width: 800px;float: right" disabled></el-input>
            </el-form-item>
            <el-form-item label="手机号码">
              <el-input v-model="form.phoneNum" disabled></el-input>
            </el-form-item>
            <el-form-item label="本人承诺">
              <div class="commitmentDivCss">
                <div style="margin: 14px auto">
                  <span style="color: rgb(0, 0, 0); font-family: 宋体, SimSun;">本人已充分了解四川省泸州市专业技术资格证书(评审、考核认定取得)补（换）发的申报要求，确保所有申报材料、申报信息真实、完整，申报资质有效。本人对全部申报材料、申报系统中所填信息的真实性、准确性负责，并授权及同意市人力资源和社会保障局使用本人的信息和资料，通过相关机构就有关事项进行核查。如有提供虚假材料或者通过其他不正当手段申报该业务的行为，愿意承担相关的行政、经济和法律责任。
                  </span>
                </div>

              </div>
<!--              <el-input type="textarea" :rows="6" v-model="form.promise" disabled></el-input>-->
            </el-form-item>
            <el-form-item>
              <el-checkbox-group v-model="form.type">
                <el-checkbox label="本人已阅读并同意" name="type"></el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-form-item style="text-align: center">
              <el-button type="primary" @click="toOther('/onlineBusinessSecond')" :disabled="!form.type" class="button-style">下一步</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>


    </div>
</template>

<script>
export default {
  name: 'onlineBusiness',
  props: {

  },
  data(){
    return{
      value: 4,
      text:
        '一、系统运行环境\n' +
        '关于您的电脑和浏览器兼容性问题：\n' +
        '1.建议使用win7以上操作系统，谷歌或火狐浏览器访问本系统，如使用其他操作系统和浏览器，可能会有兼容性问题。 \n' +
        '2.申报系统需要以PDF格式输出报表，请安装PDF官方软件。\n' +
        '二、系统操作指引\n' +
        '申请人可自行查阅系统操作手册，如不能解决，可以拨打以下电话进行咨询：\n' +
        '业务咨询电话：12333、88123460；\n' +
        '系统技术支持电话：88892919。\n' +
        '三.根据四川省人力资源和社会保障厅《关于启用职称电子证书的通知》（泸人社函〔2019〕99号）的要求， 自2019年1月起启动职称电子证书，不再印制发放纸质《四川省专业技术资格证书》。按照省工作安排，2020年2月起，在我市申报确认换证、补换证业务并通过的，统一发放职称电子证书。\n' +
        '四．业务办理通过后，请申报人登陆申报系统查询相应资格的证书号码，在四川省专业技术人才职称管理系统证书查询栏目输入姓名、证件类型、证件号码、证书号码信息查询并打印电子证书。\n' +
        '五.通过四川省职称管理系统查询并打印的《四川省职称证书》，表明持证人员取得了相应职称，与原纸质版《四川省专业技术资格证书》具有同等效力。职称系统“证书查询”栏目向社会开放，各单位可对照电子证书信息进行网上核验。\n' +
        '六.四川省专业技术人才职称管理系统证书查询栏目网址： http://www.gdhrss.gov.cn/gdweb/ggfw/web/pub/ggfwzyjs.do',
      tableData: [
        {
        date: '2020-01-19',
        title: '专业技术资格证书(评审、考核认定取得)补（换）发系统操作手册（个人申报操作）',
        },
      ],
      form: {
        name: '文沛然',
        cardType:'身份证',
        cardNum: '510522199402120256',
        phoneNum: '13320783767',
        promise: '本人已充分了解四川省泸州市专业技术资格证书(评审、考核认定取得)补（换）发的申报要求，确保所有申报材料、申报信息真实、完整，申报资质有效。本人对全部申报材料、申报系统中所填信息的真实性、准确性负责，并授权及同意市人力资源和社会保障局使用本人的信息和资料，通过相关机构就有关事项进行核查。如有提供虚假材料或者通过其他不正当手段申报该业务的行为，愿意承担相关的行政、经济和法律责任。',
        type: false,
      }
    }
  },
  methods:{
    toOther(path){
      this.$router.push(path)
    }
  }

}
</script>

<style lang="scss" scoped>
.page{
  padding: 50px 0 100px 0;
  .margin-top{
    margin-top: 20px;
  }
  .content{
    width: 75rem;
    margin: 0 auto;
    padding: 0 20px!important;
    .h1{
      font-size: 40px;
      margin: 10px 0 20px;
      font-weight: 700;
      color: black;
      font-family: Microsoft Yahei,serif;
    }
    .h2{
      font-size: 26px;
      color: #333;
      padding: 10px 0;
      border-bottom: 3px solid #333;
      margin-bottom: 20px;
      font-weight: 700;
      font-family: 'Microsoft Yahei';
    }
    .span{
      font-family: Helvetica Neue,Helvetica,PingFang SC,Hiragino Sans GB,Microsoft YaHei,\\5FAE\8F6F\96C5\9ED1,Arial,sans-serif;
      font-size: 14px;
      font-variant: tabular-nums;
      color: #333;
      word-wrap:break-word;
      white-space: pre-wrap;

    }
    p{
      font-family: '微软雅黑', "Microsoft YaHei";
      font-size: 14px;
      font-variant: tabular-nums;
      color: #333;
      word-wrap:break-word;
      white-space: pre-wrap;
    }
    .commitmentDivCss {
      width: 100%;
      height: 150px;
      display: inline-block!important;
      display: inline;
      border: 1px solid #e4e7ed;
      min-height: 40px;
      border-radius: 4px;
    }
    .button-style{
      width: 148px;
      height: 41px;
    }


  }
}

</style>
