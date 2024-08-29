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
    name: 'second',
    props: {

    },
    data() {
      return{
        dataList: [
          [
            {
              image: require('@/assets/images/portal/service/types/团队服务_1.png'),
              title: '酒城创新团队\n' +
                '（科技局会同发改委共同评选）'
            },
            {
              image: require('@/assets/images/portal/service/types/团队服务_2.png'),
              title: '技术合同认定登记'
            },
            {
              image: require('@/assets/images/portal/service/types/团队服务_3.png'),
              title: '科技政策咨询服务'
            },
            {
              image: require('@/assets/images/portal/service/types/团队服务_4.png'),
              title: '市级科技计划项目申报、 立项、验收等事项'
            },
          ],
          [
            {
              image: require('@/assets/images/portal/service/types/团队服务_5.png'),
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
    box-shadow: 0px 2px 10px 0px rgba(0,0,0,0.17);
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
