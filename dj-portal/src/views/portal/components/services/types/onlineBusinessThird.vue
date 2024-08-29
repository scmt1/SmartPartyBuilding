<template>
  <div class="page">
    <div class="content">
      <div>
        <h1 class="h1">专业技术资格证书(评审、考核认定取得)补（换）发</h1>
        <div>
          <el-steps :active="2" finish-status="wait">
            <el-step title="条件自检"></el-step>
            <el-step title="填写表单"></el-step>
            <el-step title="上传材料"></el-step>
            <el-step title="完成申请"></el-step>
          </el-steps>
        </div>
        <h2 class="h2 margin-top">事项材料上传</h2>
        <el-table :data="tableData">
          <el-table-column type="index" width="50" label="#"></el-table-column>
          <el-table-column prop="name" label="材料名称" width="512"></el-table-column>
          <el-table-column prop="requirement" label="材料要求" width="100">
            <template slot-scope="scope">
              <el-button :style="scope.row.requirement?'color:#C0C4CC':'color: #409EFF'"
                         :disabled="scope.row.requirement"
                         type="text"
                         @click="dialogVisible = true">
                查看要求
              </el-button>
            </template>
          </el-table-column>
          <el-table-column prop="model" label="范本表格" width="100">
            <template slot-scope="scope">
              <el-button style="color:#C0C4CC"
                         disabled
                         type="text"
                         @click="see">
                <i class="el-icon-download" style="margin-right: 5px;"></i>
                范本
              </el-button>
            </template>
          </el-table-column>
          <el-table-column prop="isOrNo" label="是否必传" width="100"></el-table-column>
          <el-table-column label="操作" width="190">
            <template slot-scope="scope">
              <el-button style="color:#409EFF"
                         type="text"
              >
                <i class="el-icon-upload2"></i>
                上传附件
              </el-button>
              <el-button style="color:#C0C4CC"
                         disabled
                         type="text"
              >
                <i class="el-icon-download"></i>
                报表打印
              </el-button>
              <el-button style="color:#409EFF;margin: 0"
                         type="text"
              >
                <i class="el-icon-sort"></i>
                材料排序
              </el-button>
            </template>
          </el-table-column>
          <el-table-column prop="uploadResults" label="上传结果" width="100">
            <template slot-scope="scope">
              <span style="color: red">暂未上传</span>
            </template>
          </el-table-column>
        </el-table>

        <div style="margin-top: 30px;text-align: center">
          <el-button class="button-style1" @click="toOther('/onlineBusinessSecond')">上一步</el-button>
          <el-button class="button-style2" @click="toOther('/onlineBusinessFour')">提交</el-button>
        </div>
        <el-dialog
          title="要求详情"
          :visible.sync="dialogVisible"
          width="28%"
          :before-close="handleClose">
          <p>{{ dialogText }}</p>
          <span slot="footer" class="dialog-footer">
<!--            <el-button @click="dialogVisible = false">取 消</el-button>-->
            <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
          </span>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script>
  import portalHeader from '../../portalHeader.vue'
  export default {
    components: {
      portalHeader,
    },
  name: 'onlineBusiness',
  props: {

  },
  data(){
    return{
      dialogVisible: false,
      dialogText: '1. 请上传本人近期（半年内）正面免冠彩色大一寸电子证照；\n' +
        '2. 照片背景颜色应为红底或蓝底；\n' +
        '3. jpg格式（注意非JPG），文件大小不超过50K，像素大小不小于128×180。',
      value: 4,
      tableData: [
        {
          name: '照片',
          requirement: false,
          model: '',
          isOrNo: '是',
          uploadResults: '',
        },
        {
          name: '四川省补（换）发专业技术资格证书、专业技术人员职（执）业资格证书申报表\n',
          requirement: true,
          model: '',
          isOrNo: '是',
          uploadResults: '',
        },
        {
          name: '评审获得资格证人员提交职称评审表 或者 考核认定获得资格证人员专业技术资格初次认定通知',
          requirement: true,
          model: '',
          isOrNo: '否',
          uploadResults: '',
        },
        {
          name: '丢失证书申请补发人员，提交登报声明原件',
          requirement: true,
          model: '',
          isOrNo: '是',
          uploadResults: '',
        }
      ],
      option:[
        {label: '男',value: '1'},
        {label: '女',value: '0'},
      ],
      form: {
        name: '文沛然',
        gender: '男',
        cardType:'身份证',
        cardNum: '510522199402120256',
        highestEducation: '',
        highestDegree: '',
        major: '',
        workingTime: '',
        currentEmployer: '',
        phoneNum: '13320783767',

        promise: '本人已充分了解四川省泸州市专业技术资格证书(评审、考核认定取得)补（换）发的申报要求，确保所有申报材料、申报信息真实、完整，申报资质有效。本人对全部申报材料、申报系统中所填信息的真实性、准确性负责，并授权及同意市人力资源和社会保障局使用本人的信息和资料，通过相关机构就有关事项进行核查。如有提供虚假材料或者通过其他不正当手段申报该业务的行为，愿意承担相关的行政、经济和法律责任。',
        type: '',
      }
    }
  },
  methods:{
    see(){
      alert('123');
    },
    toOther(path){
      this.$router.push(path)
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => {});
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
    .el-select{
      width: 100%;
    }
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
    .button-style1{
      display: inline-block;
      padding: 10px 50px;
      color: #4293f4;
      font-size: 16px;
      cursor: pointer;
      margin-right: 20px;
      background: #ecf4fd;
    }
    .button-style2{
      display: inline-block;
      padding: 10px 50px;
      color: #fff;
      font-size: 16px;
      cursor: pointer;
      margin-right: 20px;
      border: none;
      background: #4293f4;
    }

  }
}

</style>
