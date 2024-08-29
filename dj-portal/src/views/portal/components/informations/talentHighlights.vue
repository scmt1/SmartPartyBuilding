<template>
  <div>
<!--    内容body-->
    <div class="page-content">
      <div class="content-title">
        <h1>{{ title }}</h1>
      </div>
<!--      内容-->
      <div class="content">
        <div  class="content-card content-card2" v-for="(item,index) in dataList" >
          <div class="card-l">
            <p class="span1">{{getMonth(item.created)}}</p>
            <p class="span2">{{getDay(item.created)}}</p>
          </div>
          <div class="card-m">
            <span>{{item.title}}</span>
          </div>
          <div class="card-r">
            <el-image :src="image" alt="" @click="toNewDetail('/cms/article/'+dataList[index].id)"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import {getListByCategorySlug, getPaginate} from "@/api/portal/cms";

export default {
  name: 'talentHighlights',
  props:{

  },
  data() {
    return {
      title: '人才要闻',
      type: 'jyzc',
      image: require('@/assets/images/portal/invitation/下载.png'),
      dataList:[
        {
          dateMonth: '2022-10',
          dateDay: '28',
          title: '泸州市人才工作新闻发布会',
        },
        {
          dateMonth: '2022-03',
          dateDay: '28',
          title: '市人民政府关于印发泸州市进一步加快创新发展若干政策措施的通知',
        },
        {
          dateMonth: '2022-03',
          dateDay: '28',
          title: '市人民政府关于印发泸州市进一步加快创新发展若干政策措施的通知',
        },
        {
          dateMonth: '2022-03',
          dateDay: '28',
          title: '市人民政府关于印发泸州市进一步加快创新发展若干政策措施的通知',
        },
        {
          dateMonth: '2022-03',
          dateDay: '28',
          title: '市人民政府关于印发泸州市进一步加快创新发展若干政策措施的通知',
        },
        {
          dateMonth: '2022-03',
          dateDay: '28',
          title: '市人民政府关于印发泸州市进一步加快创新发展若干政策措施的通知',
        },
        {
          dateMonth: '2022-03',
          dateDay: '28',
          title: '市人民政府关于印发泸州市进一步加快创新发展若干政策措施的通知',
        },
        {
          dateMonth: '2022-03',
          dateDay: '28',
          title: '市人民政府关于印发泸州市进一步加快创新发展若干政策措施的通知',
        },
        {
          dateMonth: '2022-03',
          dateDay: '28',
          title: '市人民政府关于印发泸州市进一步加快创新发展若干政策措施的通知',
        },
      ]
    }
  },
  created() {
    // this.init()
  },
  mounted() {
    this.init();
  },
  methods:{
    init(){
      let activeName=this.$route.query.activeName;
      switch (activeName){
        case "first": this.title="人才要闻";
                      this.type= "jyzc";
        break
        case "second": this.title="工作动态";
                        this.type= "gzdt";
        break
        case "third": this.title="人才活动";
                      this.type= "rchd";
        break
      }
      this.getData();
    },
    getMonth(date){
      if (date){
        return date.substr(0,7)
      }
    },
    getDay(date){
      if (date){
        return date.substr(8,10)
      }
    },
    getData(){
      //人才要闻，工作动态，人才活动
      getListByCategorySlug({ "categorySlug": this.type,"count":"6" }).then(res=>{
        if(res.state==="ok"){
          this.dataList=res.list
        }
      })
    },
  }

}
</script>

<style lang="scss" scoped>
/*布局*/
.page-content{
  width: 75rem;
  margin: 1.875rem auto;
  .content-title{
    text-align: center;
  }
  .content{
    margin: 30px auto 0 auto;
    .content-card{
      width: 75rem;
      height: 9.75rem;
      margin: 0 auto 30px auto;
      padding: 28px 90px 28px 90px;
      border: 1px solid #dcdcdc;

      .card-l{
        width: 100px;
        height: 100px;
        background: #0a63dd;
        padding: 10px;
        text-align: center;
        .span1{
          font-size: 16px;
          font-family: Source Han Sans CN;
          font-weight: 400;
          color: #fff;
          margin-top: 10px;
        }
        .span2{
          font-size: 32px;
          font-family: Source Han Sans CN;
          font-weight: 400;
          color: #fff;
        }
      }
      .card-m{
        font-size: 22px;
        font-family: Source Han Sans CN;
        font-weight: 700;
        color: #273140;
      }
      .card-r{
        cursor: pointer;
      }
      div{
        display: inline-block;
      }
    }
    .content-card2{
      display: flex;
      flex-wrap: wrap;
      align-content: flex-start;
      justify-content: space-between;
      align-items: center
    }
  }
  h1{
    font-size: 34px;
    font-family: Source Han Sans CN;
    font-weight: 700;
    color: #0a63dd;
  }
}

</style>
