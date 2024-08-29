<template>
  <div class="content">
    <div class="page-left">
      <div v-for="(item,index) in menusName" >
        <el-button :class="getStyle(index)" @click="changeColor(index)">{{item}}</el-button>
      </div>
    </div>
    <div  class="page-right">
      <div v-for="(item,index) in dataList" :value="item">
        <div v-for="(temp,ind) in item" :value="temp" class="box-style" @click="toView">
          <div style="margin-top: 16.9px">
            <el-image :src="temp.image" alt=""/>
          </div>
          <div class="text-style1" style="margin-top: 15px" >
            <span>{{temp.title}}</span>
          </div>
        </div>
      </div>
    </div>
  </div>


</template>
<script>
  export default {
    name: 'talentIntroduction',
    props: {

    },
    data() {
      return{
        dataList: [],
        //人才引进
        dataList1: [
          [
            {
              image: require('@/assets/images/portal/service/types/personal/人才引进_1.png'),
              title: '境内外国人来华工作许可\n' +
                '（高端、专业人才）'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才引进_2.png'),
              title: '求职创业补贴申领'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才引进_3.png'),
              title: '创业担保贷款申请'
            },
          ],
          [
            {
              image: require('@/assets/images/portal/service/types/personal/人才引进_4.png'),
              title: '大学生创新创业园区\n' +
                '（孵化基地）申请'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才引进_5.png'),
              title: '高校毕业生社保补贴申领'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才引进_6.png'),
              title: '事业单位公开招聘信息'
            },
          ],
          [
            {
              image: require('@/assets/images/portal/service/types/personal/人才引进_7.png'),
              title: '引进人才户口迁移'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才引进_8.png'),
              title: '流动人员档案管理'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才引进_9.png'),
              title: '依据档案记载出具相关证明'
            }
          ],
          [
            {
              image: require('@/assets/images/portal/service/types/personal/人才引进_10.png'),
              title: '求职招聘'
            },

          ],

        ],
        //人才培养
        dataList2: [
          [
            {
              image: require('@/assets/images/portal/service/types/personal/人才培养_1.png'),
              title: '工程系列中初级\n' +
                '专业技术职称申报'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才培养_2.png'),
              title: '大数据与人工智能中初级\n' +
                '专业技术职称申报'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才培养_3.png'),
              title: '泸州市农村乡土人才创新\n' +
                '创业大赛'
            },
          ],
          [
            {
              image: require('@/assets/images/portal/service/types/personal/人才培养_4.png'),
              title: '泸州市自然科学学术论文评选'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才培养_5.png'),
              title: '泸州市“最美科技工作者”遴选'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才培养_6.png'),
              title: '泸州市青少年科技创新大赛'
            },
          ],
          [
            {
              image: require('@/assets/images/portal/service/types/personal/人才培养_7.png'),
              title: '酒城英才评选'
            },

          ],
        ],
        //人才激励
        dataList3: [
          [
            {
              image: require('@/assets/images/portal/service/types/personal/人才激励_1.png'),
              title: '“酒城之星· 名师之星”评选申报'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才激励_2.png'),
              title: '“酒城之星·工业之星”评选'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才激励_3.png'),
              title: '市级市级新型研发机构备案'
            },
          ],
          [
            {
              image: require('@/assets/images/portal/service/types/personal/人才激励_4.png'),
              title: '创新劵补助申领'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才激励_5.png'),
              title: '“酒城之星·工匠之星”评选'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才激励_6.png'),
              title: '大学生创业吸纳就业奖励'
            },
          ],
          [
            {
              image: require('@/assets/images/portal/service/types/personal/人才激励_7.png'),
              title: '在校大学生创业担保贷款贴息申请'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才激励_8.png'),
              title: '就业见习补贴申领'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才激励_9.png'),
              title: '创业补贴申领'
            },
          ],
          [
            {
              image: require('@/assets/images/portal/service/types/personal/人才激励_10.png'),
              title: '“酒城之星·乡村振兴之星”评选'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才激励_11.png'),
              title: '“酒城之星·医卫之星”评选申报'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才激励_12.png'),
              title: '“酒城之星·文化文创之星”评选'
            },
          ],
          [
            {
              image: require('@/assets/images/portal/service/types/personal/人才激励_13.png'),
              title: '贷款贴息补助'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才激励_14.png'),
              title: '“人才贷”风险补贴'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才激励_15.png'),
              title: '“酒城之星·服务业之星”评选'
            },
          ],
          [
            {
              image: require('@/assets/images/portal/service/types/personal/人才激励_16.png'),
              title: '泸州市“酒城之星·科技之星” 项目申报'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/rcjl_17.png'),
              title: '岗位激励'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/rcjl_18.png'),
              title: '安家补助'
            },
          ],
          [
            {
              image: require('@/assets/images/portal/service/types/personal/rcjl_19.png'),
              title: '生活补贴'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/rcjl_20.png'),
              title: '购房补助\n（会同市住建局共同完成）'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/rcjl_21.png'),
              title: '人才公寓'
            },
          ],
        ],
        // 人才服务保障
        dataList4: [
          [
            {
              image: require('@/assets/images/portal/service/types/personal/人才服务保障_1.png'),
              title: '泸州青年人才驿站管理系统'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才服务保障_2.png'),
              title: '职业指导'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才服务保障_3.png'),
              title: '创业专家咨询'
            },
          ],
          [
            {
              image: require('@/assets/images/portal/service/types/personal/人才服务保障_4.png'),
              title: '提供政审（考察）服务'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才服务保障_5.png'),
              title: '档案查（借）阅服务'
            },
            {
              image: require('@/assets/images/portal/service/types/personal/人才服务保障_6.png'),
              title: '职业介绍'
            },
          ],
        ],
        menusName: ['人才引进','人才培养','人才激励','人才服务保障'],
        num: 0
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      //初始化
      init(){
        let num;
        if (typeof(this.$route.params) !== "undefined"&&typeof(this.$route.params.data) !== "undefined"){
          num = this.$route.params.data.num;
          if (typeof(num) !== "undefined"||num===null||num===''){
            num=0
          }
        }else {
          num=0
        }

        this.changeColor(num)
      },
      changeColor(num){
        this.num=num
        if (num===0){
          this.dataList=this.dataList1
        }else if (num===1){
          this.dataList=this.dataList2
        }else if (num===2){
          this.dataList=this.dataList3
        }else if (num===3){
          this.dataList=this.dataList4
        }
      },
      getStyle(key){
        let value=''
        if (this.num===key){
          value='button-style-visited'
        }else{
          value='button-style'
        }
        return value
      },
      toView(){
        this.$router.push({ path: '/view' });
        // const news=this.$router.resolve({name: '/view'})
        // window.open(news.href,'_blank')
      }
    }

  }
</script>

<style lang="scss" scoped>
  .content{
    width: 75rem;
    margin: 0 auto 116px auto;

  }
  .page-left{
    display: inline-block;
  }
  .page-right{
    vertical-align: top;
    display: inline-block;
    margin-left: 20px;
    span{
      white-space:pre-wrap;
    }
  }
  .box-style{
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    display: inline-block;
    vertical-align: top;
    width: 284px;
    height: 200px;
    margin: 17px 0 0 19px;
    text-align: center;
    cursor: pointer;
    transition-duration: 0.5s;
  }
  .box-style:hover{
    transform:translate(0,-10px);
    background-color: #1F7BD5;
    color: white;
  }

  .button-style{
    width: 260px;
    height: 80px;
    margin-top: 10px;
    background-color: #f2f2f2;
    border-radius: 0;
    border-color: #f2f2f2;
    font-size: 18px;
    font-family: "PingFang SC", "PingFang SC-Medium";
    font-weight: 700;
  }

  .button-style-visited{
    width: 260px;
    height: 80px;
    margin-top: 10px;
    background-color: #0256ca;
    border-color: #0256ca;
    color: white;
    border-radius: 0;
    font-size: 22px;
    font-family: "PingFang SC", "PingFang SC-Medium";
    font-weight: 700;
  }

  .button-style:hover,.button-style:visited{
    background-color: #0256ca;
    color: white;
  }

  .text-style1{
    font-size: 20px;
    font-family: "PingFang SC", "PingFang SC-Medium";
    font-weight: 700;
  }


</style>
