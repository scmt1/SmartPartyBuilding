<template>
  <div class="content">
    <div v-for="(item,index) in dataList" :value="item">
      <div v-for="(temp,ind) in item" :value="temp"
           :class="ind==3?'box-style':'box-style margin-right' " @click="toView">
        <div style="margin-top: 16.9px">
          <el-image :src="temp.image" alt=""/>
        </div>
        <div class="text-style1" style="margin-top: 15px" >
          <span>{{temp.title}}</span>
        </div>
      </div>
    </div>
  </div>


</template>
<script>
  export default {
    name: 'third',
    props: {

    },
    data() {
      return{
        dataList: [
          [
            {
              image: require('@/assets/images/portal/service/types/单位服务_1.png'),
              title: '市级企业技术中心申报'
            },
            {
              image: require('@/assets/images/portal/service/types/单位服务_2.png'),
              title: '泸州市院士工作站、专家工作站申报'
            },
            {
              image: require('@/assets/images/portal/service/types/单位服务_3.png'),
              title: '博士后工作站和创新实践基地\n' +
                '的建设和资金相关的申报'
            },
            {
              image: require('@/assets/images/portal/service/types/单位服务_4.png'),
              title: '市级众创空间备案'
            },
          ],
          [
            {
              image: require('@/assets/images/portal/service/types/单位服务_5.png'),
              title: '境外外国人来华工作许可\n' +
                '(高端、专业人才)'
            },
            {
              image: require('@/assets/images/portal/service/types/单位服务_6.png'),
              title: '外国人来华工作许可延期审批\n' +
                '(高端、专业人才)'
            },
            {
              image: require('@/assets/images/portal/service/types/单位服务_7.png'),
              title: '外国人来华工作许可变更审批\n' +
                '(高端、专业人才)'
            },
            {
              image: require('@/assets/images/portal/service/types/单位服务_8.png'),
              title: '外国人来华工作许可注销审批\n' +
                '(高端、专业人才)'
            },
          ],
          [
            {
              image: require('@/assets/images/portal/service/types/单位服务_9.png'),
              title: '市级工程技术研究中心认定'
            },
            {
              image: require('@/assets/images/portal/service/types/单位服务_10.png'),
              title: '市级重点实验室认定'
            },
            {
              image: require('@/assets/images/portal/service/types/单位服务_11.png'),
              title: '泸州市技术交易奖励'
            },
            {
              image: require('@/assets/images/portal/service/types/单位服务_12.png'),
              title: '泸州市成果转化项目申报'
            },
          ],
          [
            {
              image: require('@/assets/images/portal/service/types/单位服务_13.png'),
              title: '泸州市技术合同认定登记'
            },
            {
              image: require('@/assets/images/portal/service/types/单位服务_14.png'),
              title: '个人基本信息变更'
            },
            {
              image: require('@/assets/images/portal/service/types/单位服务_15.png'),
              title: '招聘会服务（企业）'
            },
            {
              image: require('@/assets/images/portal/service/types/单位服务_16.png'),
              title: '企业投资项目备案'
            },
          ],
          [
            {
              image: require('@/assets/images/portal/service/types/单位服务_17.png'),
              title: '市级科技计划项目申报、 立项、验收等事项'
            },
            {
              image: require('@/assets/images/portal/service/types/单位服务_18.png'),
              title: '创新劵补助申领'
            },
          ],

        ],
        num:''
      }
    },
    methods: {
      toView(){
        const news=this.$router.resolve({path: '/view'})
        window.open(news.href,'_blank')
      }
    }

  }
</script>

<style lang="scss" scoped>
  .content{
    width: 75rem;
    margin: 0 auto 116px auto;
    span{
      white-space:pre-wrap;
    }
  }
  .margin-right{
    margin-right: 19px;
  }
  .box-style{
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    display: inline-block;
    vertical-align: top;
    width: 284px;
    height: 200px;
    margin-top: 17px;
    text-align: center;
    cursor: pointer;
    transition-duration: 0.5s;
  }
  .box-style:hover{
    transform:translate(0,-10px);
    background-color: #1F7BD5;
    color: white;
  }

  .text-style1{
    font-size: 20px;
    font-family: "PingFang SC", "PingFang SC-Medium";
    font-weight: 700;
  }


</style>
