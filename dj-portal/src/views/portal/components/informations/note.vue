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
<!--    <div v-for="(item,index) in tableData" :key="index">-->
      <div class="my_table">
        <el-table
          :show-header="false"
          :data="tableData"
          style="width: 100%">
          <el-table-column prop="name" label="标题">
            <template slot-scope="scope">
              <el-button style="color: black"
                         type="text"
                         @click="toNewDetail('/cms/article/'+scope.row.id)"
                         >
                {{scope.row.title}}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column prop="created" label="时间" align="right" width="120px">
            <template slot-scope="scope">
              <span>{{getDate(scope.row.created)}}</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
<!--/*      <el-divider style="color: #FFF7E7"></el-divider>*/-->
<!--    </div>-->

    <div>
      <el-pagination
        style="text-align:center;margin-top: 30px"
        background
        :current-page.sync="pageNumber"
        layout="prev, pager, next"
        prev-text="上一页"
        next-text="下一页"
        :page-size="pageSize"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {getPaginateByCategorySlug, getSearchByCategorySlug, search} from "@/api/portal/cms";

  export default {
    name: 'note',
    props: {

    },
    data() {
      return {
        input: '',
        categoryId: '2',
        categorySlug: 'tzgg',
        pageNumber: 1,
        pageSize: 10,
        total: 15,
        tableData:[
          {
            date: '2022-10-31',
            name: '关于高校事务有关事项的公告',
            title: '',
            id: '',
            created: '',
          },
        ],
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      init(){
        this.categoryId= this.$route.query.categoryId;
        switch (this.categoryId){
          case '2': this.categorySlug="tzgg";break
          case '3': this.categorySlug="rczc";break
          case '4': this.categorySlug="rcxq";break
        }
        this.getData()
      },
      getData(){
        let orderBy="created desc"
        //人才要闻
        getPaginateByCategorySlug({"categorySlug": this.categorySlug,"orderBy": orderBy,"pageNumber":this.pageNumber,"pageSize":this.pageSize}).then(res=>{
          if(res.state==="ok"){
            this.tableData=res.page.list
            this.total=res.page.list.length
            // let tempList=[]
            // tempList=res.page.list
            // for (let i = 0; i < tempList.length; i+=5) {
            //   this.tableData.push(tempList.slice(i,i+5))
            // }
          }
        })

      },
      search(){
        if (this.input===""){
          this.getData()
          return
        }
        getSearchByCategorySlug({"keyword": this.input,"categorySlug":this.categorySlug,"pageNumber": this.pageNumber,"pageSize":this.pageSize}).then(res=>{
          if(res.state==="ok"){
            this.tableData=res.page.list
            this.total=res.page.totalRow
            // let tempList=[]
            // tempList=res.page.list
            // for (let i = 0; i < tempList.length; i+=5) {
            //   this.tableData.push(tempList.slice(i,i+5))
            // }
          }
        })
      },

      getDate(item){
        if(item){
          return item.substr(0,10)
        }
      }
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
