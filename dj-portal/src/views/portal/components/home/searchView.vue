<template>
  <!--  通知公告-->
  <div class="page-content">
    <div>
      <el-input
        v-model="input"
        placeholder="请输入关键字"
        style="width: 200px;">
        <el-button slot="append" class="button1" @click="search()">搜索</el-button>
      </el-input>
    </div>
    <div class="my_table">
      <el-table
        :show-header="false"
        :data="tableData"
        style="width: 100%">
        <el-table-column prop="name" label="标题">
          <template slot-scope="scope">
            <el-button style="color: black"
                       type="text"
                       @click="handleSelectIteam(scope.row)"
            >
              {{scope.row.label}}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column prop="created" label="时间" align="right" width="120px">
          <template slot-scope="scope">
            <span>{{scope.row.type}}</span>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div>
      <el-pagination
        style="text-align:center;margin-top: 30px"
        background
        :current-page.sync="page"
        layout="prev, pager, next"
        prev-text="上一页"
        next-text="下一页"
        @current-change="currentChange(searches)"
        :page-size="pageSize"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>

export default {
  name: 'searchView',
  props: {

  },
  data() {
    return {
      input: '',
      page: 1,
      pageSize: 10,
      total: 15,
      options: [],//源数据
      searches: [],//搜索数据
      tableData:[
        [
          {
            label:"泸州市酒城青年人才驿站管理系统",
            type: "个人服务",
            value:"022171",
          },
        ],
      ],
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init(){
      this.options = JSON.parse(this.$route.query.data);
      this.searches = this.options;
      this.total=this.options.length;
      this.currentChange(this.searches)
    },

    //分页
    currentChange(options){
      this.tableData = options.slice((this.page-1)*this.pageSize, this.page*this.pageSize);
    },
    //搜索
    search(){
      if (this.input!==''){
        this.searches = this.options.filter(item => {
          return item.label.toLowerCase().indexOf(this.input.toLowerCase()) > -1
        })
        this.total=this.searches.length
        this.currentChange(this.searches)
      }else {
        this.currentChange(this.options)
      }
    },

    handleSelectIteam(item){
      if(item.type=="新闻资讯"){
        this.toNewDetail('/cms/article/'+item.value)
      }else{
        this.toNewDetail('/rcfw/web/view/view.html?SXBM='+item.value)
      }
    },

  }
}
</script>

<style lang="scss" scoped>
.page-content{
  width: 75rem;
  margin: 1.625rem auto 5rem auto;
}
/*输入框*/
::v-deep .el-input__inner{
  width: 529px;
  height: 48px;
  border-radius: 0;
  /*border: 1px solid #6a6a6a;*/
  &::placeholder{
    font-size: 16px;
    font-family: "PingFang SC", "PingFang SC-Medium";
    font-weight: 500;
    color: #727272;
  }
}
/*按钮*/
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
.my_table{
  margin-top: 15px;
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
</style>
