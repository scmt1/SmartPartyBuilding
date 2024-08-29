<template>
<!--  人才政策-->
  <div class="page-content">
    <div>
      <el-input
        v-model="input"
        placeholder="请输入关键字"
        style="width: 200px;">
        <el-button slot="append" class="button1">搜索</el-button>
      </el-input>
    </div>
    <div v-for="(item,index) in tableData" :key="index" :value="item">
      <div class="my_table">
        <el-table
          :show-header="false"
          :data="item"
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
      <el-divider style="color: #FFF7E7"></el-divider>
    </div>
    <div>
      <el-pagination
        style="text-align:center;margin-top: 30px"
        background
        layout="prev, pager, next"
        prev-text="上一页"
        next-text="下一页"
        :total="1000">
      </el-pagination>
    </div>
  </div>
</template>

<script>
  import {getPaginate} from "@/api/portal/cms";

  export default {
    name: 'talentPolicy',
    props: {

    },
    data() {
      return {
        input: '',
        tableData:[
          [
            {
              date: '2022-10-31',
              name: '中国电子与泸州市全面深化合作发展签约仪式暨交流座谈会举行',
            },
            {
              date: '2022-10-31',
              name: '2022网信产业融合发展大会暨西部网信产业先行先试赋能会在泸召开',
            },
            {
              date: '2022-10-31',
              name: '“中国海归创业联合体”泸州行项目对接会举行杨林兴出席并致辞',
            },
            {
              date: '2022-10-31',
              name: '守正创新勇毅前行一一泸州市干部群众认真学习贯彻党的二十大精神',
            },
            {
              date: '2022-10-31',
              name: '泸州市通过四川省首批创新型城市评估验收',
            }
          ],
          [
            {
              date: '2022-10-31',
              name: '中国电子与泸州市全面深化合作发展签约仪式暨交流座谈会举行',
            },
            {
              date: '2022-10-31',
              name: '2022网信产业融合发展大会暨西部网信产业先行先试赋能会在泸召开',
            },
            {
              date: '2022-10-31',
              name: '“中国海归创业联合体”泸州行项目对接会举行杨林兴出席并致辞',
            },
            {
              date: '2022-10-31',
              name: '守正创新勇毅前行一一泸州市干部群众认真学习贯彻党的二十大精神',
            },
            {
              date: '2022-10-31',
              name: '泸州市通过四川省首批创新型城市评估验收',
            }
          ],
          [
            {
              date: '2022-10-31',
              name: '中国电子与泸州市全面深化合作发展签约仪式暨交流座谈会举行',
            },
            {
              date: '2022-10-31',
              name: '2022网信产业融合发展大会暨西部网信产业先行先试赋能会在泸召开',
            },
            {
              date: '2022-10-31',
              name: '“中国海归创业联合体”泸州行项目对接会举行杨林兴出席并致辞',
            },
            {
              date: '2022-10-31',
              name: '守正创新勇毅前行一一泸州市干部群众认真学习贯彻党的二十大精神',
            },
            {
              date: '2022-10-31',
              name: '泸州市通过四川省首批创新型城市评估验收',
            }
          ],
        ]
      }
    },
    mounted() {
      this.getData()
    },
    methods: {
      View(row) {
        this.$router.push({
          path: "/policyNoticeDetail",
        });
      },
      getData(){
        //人才要闻
        let param={
          categoryId:3,
          pageNumber:1,
          pageSize:5
        }
        this.tableData=[]
        let tempList=[]
        getPaginate(param).then(res=>{
          let temp=null;
          if(res.state=="ok"){
            tempList=(res.page.list)
            for (let i = 0; i < 3; i++) {
              this.tableData.push(tempList)
            }
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
