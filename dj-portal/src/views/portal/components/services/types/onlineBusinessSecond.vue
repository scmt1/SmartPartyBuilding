<template>
  <div class="page">
    <div class="content">
      <div>
        <h1 class="h1">专业技术资格证书(评审、考核认定取得)补（换）发</h1>
        <div>
          <el-steps :active="1" finish-status="wait">
            <el-step title="条件自检"></el-step>
            <el-step title="填写表单"></el-step>
            <el-step title="上传材料"></el-step>
            <el-step title="完成申请"></el-step>
          </el-steps>
        </div>
<!--        申请人证书集-->
        <div class="margin-top">
          <h2 class="h2">申请人证书集</h2>
          <el-table :data="tableData" border>
            <el-table-column type="index" width="50" label="#"></el-table-column>
            <el-table-column prop="title" label="证书编号"></el-table-column>
            <el-table-column prop="title" label="专业技术资格名称"></el-table-column>
            <el-table-column prop="title" label="资格取得方式"></el-table-column>
            <el-table-column prop="title" label="核准机关"></el-table-column>
            <el-table-column prop="title" label="发证日期"></el-table-column>
          </el-table>
        </div>
        <!--:rules="rules"-->
        <el-form ref="form" :model="form" label-width="125px" >
          <!--        基本信息-->
          <div class="margin-top">
            <h2  class="h2">基本信息</h2>
            <el-row>
              <el-col :span="12">
                <el-form-item label="姓名" prop="name">
                  <el-input v-model="form.name" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="性别" prop="name">
                  <el-select v-model="form.gender" placeholder="请输入内容" disabled>
                    <el-option v-for="item in option" :key="item.label" :value="item.label">{{item.label}}</el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="证件类型" prop="name">
                  <el-select v-model="form.cardType" placeholder="请输入内容" disabled>
                    <el-option v-for="item in option" :key="item.label" :value="item.label">{{item.label}}</el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="证件号码" prop="name">
                  <el-input v-model="form.cardNum" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="最高学历" prop="highestEducation">
                  <el-select v-model="form.highestEducation" placeholder="请输入内容">
                    <el-option v-for="item in highestEducationOption" :key="item.label" :value="item.label">{{item.label}}</el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="最高学位" prop="highestDegree">
                  <el-select v-model="form.highestDegree" placeholder="请输入内容">
                    <el-option v-for="item in highestDegreeOption" :key="item.label" :value="item.label">{{item.label}}</el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="所学专业" prop="major">
                  <el-input v-model="form.major" placeholder="请输入内容"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="参加工作时间" prop="workingTime">
                  <el-date-picker
                    style="width: 100%"
                    v-model="form.workingTime"
                    type="date"
                    placeholder="选择日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="现工作单位" prop="currentEmployer">
                  <el-input v-model="form.currentEmployer" placeholder="请输入内容"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系号码" prop="phoneNum">
                  <el-input v-model="form.phoneNum" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <!--        补换证信息-->
          <div class="margin-top">
            <h2 class="h2">补换证信息</h2>
            <el-form-item label="业务类型" prop="input1">
              <el-select v-model="form.input1" placeholder="请选择">
                <el-option v-for="item in input1Option" :key="item.label" :value="item.label">{{item.label}}</el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="证书取得方式" prop="input2">
              <el-select v-model="form.input2" placeholder="请选择">
                <el-option v-for="item in input2Option" :key="item.label" :value="item.label">{{item.label}}</el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="原职称系列" >
              <el-select v-model="form.input3" placeholder="请选择">
                <el-option v-for="item in input3Option" :key="item.label" :value="item.label">{{item.label}}</el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="原职称级别" >
              <el-select v-model="form.input4" placeholder="请选择">
                <el-option v-for="item in input4Option" :key="item.label" :value="item.label">{{item.label}}</el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="原职称" >
              <el-select v-model="form.input5" placeholder="请选择">
                <el-option v-for="item in input5Option" :key="item.label" :value="item.label">{{item.label}}</el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="原职称专业" >
              <el-select v-model="form.input6" placeholder="请选择">
                <el-option v-for="item in input6Option" :key="item.label" :value="item.label">{{item.label}}</el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="原申报职称评委会名称（考核认定单位名称）" prop="input7" class="item-label">
               <el-input class="margin-top2" v-model="form.input7"></el-input>
            </el-form-item>
            <el-form-item label="原发证机关" prop="input8">
              <el-input v-model="form.input8"></el-input>
            </el-form-item>
            <el-form-item label="证书核准机关" prop="input9">
              <el-input v-model="form.input9"></el-input>
            </el-form-item>
            <el-form-item label="原职称审批通过时间" prop="input10" class="item-label">
              <el-date-picker
                style="width: 100%"
                v-model="form.input10"
                type="date"
                placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="原职称证书发放时间" prop="input11" class="item-label">
              <el-date-picker
                style="width: 100%"
                v-model="form.input11"
                type="date"
                placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="原职称证书号码" prop="input12">
              <el-input v-model="form.input12"></el-input>
            </el-form-item>
            <el-form-item label="遗失损坏职称证书时间" prop="input13" class="item-label">
              <el-date-picker
                style="width: 100%"
                v-model="form.input13"
                type="date"
                placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="申请补换发证书理由" prop="input14" class="item-label">
              <el-select v-model="form.input14" placeholder="请选择">
                <el-option v-for="item in input14Option" :key="item.label" :value="item.label">{{item.label}}</el-option>
              </el-select>
            </el-form-item>
          </div>
          <!--        纸质材料递交方式-->
          <div class="margin-top">
            <h2 class="h2">纸质材料递交方式</h2>
            <el-form-item  label=" " prop="resource">
              <el-radio-group v-model="form.resource">
                <el-radio label="窗口递交"></el-radio>
                <el-radio label="邮寄递交纸质资料"></el-radio>
              </el-radio-group>
            </el-form-item>
          </div>
          <!--        审批文件领取方式-->
          <div class="margin-top">
            <h2 class="h2">审批文件领取方式</h2>
            <el-form-item label="证书领取方式" prop="input15">
              <el-select v-model="form.input15" placeholder="请选择">
                <el-option v-for="item in input15Option" :key="item.label" :value="item.label">{{item.label}}</el-option>
              </el-select>
            </el-form-item>
          </div>
          <!--        申报下一环节信息-->
          <div class="margin-top">
            <h2 class="h2">申报下一环节信息</h2>
            <el-row>
              <el-col :span="12">
                <el-form-item label="报送去向" prop="input16">
                  <el-select v-model="form.input16" placeholder="请选择" disabled>
                    <el-option v-for="item in option" :key="item.label" :value="item.label">{{item.label}}</el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="报送单位（部门）" prop="input17">
                  <el-select v-model="form.input17" placeholder="请选择" disabled>
                    <el-option v-for="item in option" :key="item.label" :value="item.label">{{item.label}}</el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

          </div>
        </el-form>
        <div style="text-align: center">
          <el-button class="button-style1" @click="toOther('/onlineBusiness')">上一步</el-button>
          <el-button class="button-style2" @click="submitForm('form','/onlineBusinessThird')">下一步</el-button>
          <el-button class="button-style3" @click="toOther()">暂存</el-button>
        </div>
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
      value: 4,
      tableData: [

      ],
      option:[
        {label: '男',value: '1'},
        {label: '女',value: '0'},
      ],
      highestEducationOption:[
        {label: '大学本科',value: '1'},
        {label: '研究生',value: '0'},
      ],
      highestDegreeOption:[
        {label: '硕士',value: '1'},
        {label: '学士',value: '0'},
      ],
      input1Option:[
        {label: '补证',value: '1'},
        {label: '换证',value: '0'},
      ],
      input2Option:[
        {label: '职称评审',value: '1'},
        {label: '考核认定',value: '0'},
      ],
      input3Option:[
        {label: '高等学校教师',value: '1'},
        {label: '自然科学研究人员',value: '0'},
      ],
      input4Option:[
        {label: '正高级',value: '1'},
        {label: '副高级',value: '0'},
      ],
      input5Option:[
        {label: '教授',value: '1'},
        {label: '讲师',value: '0'},
      ],
      input6Option:[
        {label: '世界经济史',value: '1'},
        {label: '中医基础理论',value: '0'},
      ],
      input14Option:[
        {label: '证书遗失',value: '1'},
        {label: '证书过期',value: '0'},
      ],
      input15Option:[
        {label: '窗口领取',value: '1'},
        {label: '邮政快递',value: '0'},
      ],
      input16Option:[
        {label: '男',value: '1'},
        {label: '女',value: '0'},
      ],
      input17Option:[
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
        resource: null,

        input1: '',
        input2: '',
        input3: '',
        input4: '',
        input5: '',
        input6: '',
        input7: '',
        input8: '',
        input9: '',
        input10: '',
        input11: '',
        input12: '',
        input13: '',
        input14: '',

        input15: '',
        input16: '送市人社审核',
        input17: '泸州市人力资源和社会保障局',

      },
      rules: {
        name: [{ required: true, message: '请输入内容', trigger: 'blur' },],
        gender: [{ required: true, message: '请输入内容', trigger: 'blur' }],
        cardType: [{ required: true, message: '请输入内容', trigger: 'blur' }],
        cardNum: [{ required: true, message: '请输入内容', trigger: 'blur' }],
        highestEducation: [{ required: true, message: '请输入内容', trigger: 'change' }],
        highestDegree: [{ required: true, message: '请输入内容', trigger: 'change' }],
        major: [{ required: true, message: '请输入内容', trigger: 'blur' }],
        workingTime: [{ required: true, message: '请输入内容', trigger: 'blur' }],
        currentEmployer: [{ required: true, message: '请输入内容', trigger: 'blur' }],
        phoneNum: [{ required: true, message: '请输入内容', trigger: 'blur' }],
        resource: [{ required: true, message: '请输入内容', trigger: 'blur' }],

        input1: [{ required: true, message: '请输入内容', trigger: 'blur' },],
        input2: [{ required: true, message: '请输入内容', trigger: 'blur' },],

        input7: [{ required: true, message: '请输入内容', trigger: 'blur' },],
        input8: [{ required: true, message: '请输入内容', trigger: 'blur' },],
        input9: [{ required: true, message: '请输入内容', trigger: 'blur' },],
        input10: [{ required: true, message: '请输入内容', trigger: 'blur' },],
        input11: [{ required: true, message: '请输入内容', trigger: 'blur' },],

        input13: [{ required: true, message: '请输入内容', trigger: 'blur' },],
        input14: [{ required: true, message: '请输入内容', trigger: 'blur' },],
        input15: [{ required: true, message: '请输入内容', trigger: 'blur' },],
        input16: [{ required: true, message: '请输入内容', trigger: 'blur' },],
        input17: [{ required: true, message: '请输入内容', trigger: 'blur' },],
      }
    }
  },
  methods:{
    toOther(path){
      this.$router.push(path)
    },
    submitForm(formName,path) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$router.push(path)
        } else {
          alert('请填写完整!');
          return false;
        }
      });
    },
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
    .item-label ::v-deep .el-form-item__label {
        line-height: 20px;
    }
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
    .button-style3{
      display: inline-block;
      padding: 10px 50px;
      color: #4293f4;
      font-size: 16px;
      cursor: pointer;
      margin-right: 20px;
      background: #ecf4fd;
    }
    .margin-top2{
      margin-top: 10px;
    }

  }
}

</style>
