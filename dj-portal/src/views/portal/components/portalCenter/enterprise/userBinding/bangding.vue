<template>
  <div class="bangding">
    <!--    <div class="title">业务申报管理</div>-->
    <div class="container">
      <el-tabs :tab-position="tabPosition" style="height: 100%;">
        <el-tab-pane label="绑定申请管理">
          <div class="person_title">绑定申请管理</div>
          <div class="person_content">
            <el-table
              :data="tableData"
              stripe
              style="width: 100%;">
              <el-table-column
                prop="applyUserRealName"
                label="姓名"
                align="center"
                width="100">
              </el-table-column>
              <el-table-column
                prop="sex"
                label="性别"
                :formatter="(row)=>{return sexName[row.sex]}"
                align="center"
                width="60">
              </el-table-column>
              <el-table-column
                prop="applyUserPhone"
                label="联系电话"
                align="center"
                width="180">
              </el-table-column>
              <el-table-column
                prop="applyUserIdCard"
                label="身份证号"
                align="center"
                width="230">
              </el-table-column>
              <el-table-column
                prop="job"
                label="绑定职位"
                align="center"
                width="100">
              </el-table-column>
              <el-table-column
                prop="createTime"
                label="时间"
                align="center"
                width="100">
              </el-table-column>
              <el-table-column
                prop="address"
                label="操作"
                align="center"
                width="120">
                <template slot-scope="scope">
                  <el-button @click="handleClick(scope.row)" type="text" size="small" class="tg">通过</el-button>
                  <el-button @click="quxiaoClick(scope.row)" type="text" size="small" class="bh">驳回</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div class="page">
            <el-pagination
              background
              :current-page.sync="page"
              layout="prev, pager, next"
              @current-change="currentChange1(this.tableDataSource)"
              :page-size="pageSize"
              :total="total">
            </el-pagination>
          </div>
        </el-tab-pane>
        <el-tab-pane label="绑定用户管理">
          <div class="person_title">绑定用户管理</div>
          <div class="person_content">
            <el-table
              :data="personData"
              stripe
              :row-style="{height:'70px'}"
              style="width: 100%;">
              <el-table-column
                prop="applyUserRealName"
                label="姓名"
                align="center"
                width="120">
              </el-table-column>
              <el-table-column
                prop="sex"
                label="性别"
                :formatter="(row)=>{return sexName[row.sex]}"
                align="center"
                width="80">
              </el-table-column>
              <el-table-column
                prop="applyUserPhone"
                label="联系电话"
                align="center"
                width="190">
              </el-table-column>
              <el-table-column
                prop="applyUserIdCard"
                label="身份证号"
                align="center"
                width="250">
              </el-table-column>
              <el-table-column
                prop="job"
                label="绑定职位"
                align="center"
                width="130">
              </el-table-column>
              <el-table-column
                prop="address"
                label="操作"
                align="center"
                width="120">
                <template slot-scope="scope">
                  <el-button @click="unbind(scope.row)" type="text" size="small" class="relieve">解除绑定</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div class="page">
            <el-pagination
              background
              :current-page.sync="page2"
              layout="prev, pager, next"
              @current-change="currentChange2(this.personDataSource)"
              :page-size="pageSize2"
              :total="total2">
            </el-pagination>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
  import { getCompanyEmployee, auditEmployee, unbindEmployee } from "@/api/portal/employee"
  import { Message } from 'element-ui'

  export default {
    name: "accountMsg",
    mounted() {
      this.initData()
    },
    data() {
      return {
        tabPosition: 'left',
        value: true,
        sexName: ["女", "男"],
        tableData: [
          /*{
            applyUserRealName: '刘智愚',
            sex: '男',
            applyUserPhone: '18880912538',
            applyUserIdCard: '511527199801260013',
            job: '研发工程师',
            createTime: '2021-02-12 19:12:11'
          }*/
        ],
        tableDataSource: [],
        page: 1,
        pageSize: 10,
        total: 15,
        personData: [],
        personDataSource: [],
        page2: 1,
        pageSize2: 10,
        total2: 15,
      };
    },
    methods: {
      initData() {
        getCompanyEmployee().then(res => {
          if (res.code == 200 && res.data) {
            this.tableDataSource = []
            this.personDataSource = []
            res.data.forEach(item => {
              if (item.status == 1) {
                this.personDataSource.push(item)
              } else if (item.status == 0) {
                this.tableDataSource.push(item)
              }
            })
            this.currentChange1(this.tableDataSource)
            this.currentChange2(this.personDataSource)
          }
        })
      },
      quxiaoClick(row) {
        this.$prompt('驳回原因', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          // inputPattern: /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/,
          // inputErrorMessage: '邮箱格式不正确'
        }).then(({ value }) => {
          row.status = -1
          row.reason = value
          auditEmployee(row).then(res => {
            if (res.code == 200) {
              this.initData()
              Message.success(res.msg)
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '取消输入'
          });
        });
      },
      handleClick(row){
        row.status = 1
        auditEmployee(row).then(res => {
          if (res.code == 200) {
            this.initData()
            Message.success(res.msg)
          }
        })
      },
      unbind(row) {

        this.$confirm('是否确认解除用户绑定?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          unbindEmployee({ "id": row.id }).then(res => {
            if (res.code == 200) {
              this.initData()
              Message.success(res.msg)
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });




      },

      //分页
      currentChange1(options){
        this.tableData = options.slice((this.page-1)*this.pageSize, this.page*this.pageSize);
        this.total=options.length
      },
      currentChange2(options){
        this.personData = options.slice((this.page2-1)*this.pageSize2, this.page2*this.pageSize2);
        this.total2=options.length
      },
    },

  }

</script>

<style scoped>
  * {
    margin: 0;
    padding: 0;
  }

  .bangding {
    width: 100%;
    height: 100%;
    /*flex: 1;*/
    background: #ffffff;
  }

  .title {
    width: 12.5rem;
    height: 4.375rem;
    font-size: 26px;
    font-family: PingFang SC, PingFang SC-Bold;
    font-weight: 700;
    text-align: center;
    color: #333333;
    line-height: 4.375rem;
    /*border: 1px solid black;*/
  }

  .container {
    padding-top: 20px;
    width: 98%;
    /*margin: auto;*/
    /*border: 1px solid black;*/
  }

  /*tabs样式调整*/
  .container >>> .el-tabs__item {
    width: 14.875rem;
    height: 3.125rem;
    text-align: left;
    /*background: #e4f2fe;*/
    padding-top: 2%;
    font-size: 16px;
    font-family: PingFang SC, PingFang SC-Bold;
    font-weight: 700;
  }

  .container >>> .el-tabs__item.is-active {
    background: #e4f2fe;
    color: #0256ca;
    border-left: 2px solid #0256CA;
  }

  .container >>> .el-tabs__active-bar {
    display: none;
  }

  .container >>> .el-tabs__nav-wrap::after {
    display: none;
  }

  /*个人信息管理*/
  .person_title {
    width: 96%;
    height: 2.1875rem;
    margin-left: 1.875rem;
    font-size: 18px;
    font-family: PingFang SC, PingFang SC-Bold;
    font-weight: 700;
    text-align: left;
    color: #333333;
    /*border: 1px solid black;*/
    border-bottom: 2px solid #333333;
  }

  .person_content {
    width: 100%;
    height: 100%;
    display: flex;
    margin-top: 1.875rem;
    margin-left: 1.875rem;
    /*border: 1px solid black;*/
  }

  .page {
    /*width: 400px;*/
    height: 50px;
    margin-top: 2.5rem;
    float: right;
    margin-bottom: 3.125rem;
    /*border: 1px solid black;*/
  }

  .tg {
    color: #1AD21F;
  }

  .bh {
    color: #E71111;
  }

  .relieve {
    color: #E71111;
  }
</style>
