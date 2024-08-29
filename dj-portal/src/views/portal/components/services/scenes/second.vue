<template>
  <!--  高层次人才-->
  <div class="content">
    <!--    资格认定-->
    <div >
      <div class="text-part">
        <h3 class="text-title1">资格认定</h3>
        <img src="../../../../../assets/images/portal/service/scene/underline.png" class="img-style"/>
        <p class="text-span1">高层次专业人才中杰出人才无年龄限制，国家级领军人才年龄应在60周岁以下(其中高技能人才年龄应在50周岁以下)，地方级领军人才年龄应在55周岁以下(其中高技能人才年龄应在45周岁以下)，后备级人才年龄应在40周岁以下。</p>
        <el-button class="button-style" >高层次人才认定</el-button>
        <p class="text-span1">海外高层次A类人才年龄应在6O周岁以下;海外高层次B类人才年龄应在55周岁以下;海外高层次C类人才年龄应在40周岁以下。</p>
        <el-button class="button-style" >海外高层次人才确认</el-button>
      </div>
      <div class="image-part">
        <el-image
          style="top: 60px;left: 25px"
          :src="this.images[0]"></el-image>
      </div>
      <el-divider></el-divider>
    </div>
    <!--    福利补贴-->
    <div>
      <div class="text-part">
        <h3 class="text-title1">福利补贴</h3>
        <img src="../../../../../assets/images/portal/service/scene/underline.png" class="img-style"/>
        <p class="text-span1">经市人力资源保障部门认定的领军人才，在本市用人单位全职工作;申请人未在本市享受过购房优惠政策、未租住政策性住房;申请人及共同申请人在本市拥有不超过一套商品住房;地方级和后备级领军人才须具有泸州户籍。</p>
        <el-button class="button-style" >购房补贴</el-button>
        <p class="text-span1">申请人为经市人力资源部门认定的高层次人才;在本市未享受过购房优惠政策、未拥有商品住房、未领取过租房补贴:在本市用人单位全职工作。</p>
        <el-button class="button-style" @click="goto('talentApartment')">青年人才公寓</el-button>

      </div>
      <div class="image-part">
        <el-image
          style="top: 60px;left: 25px"
          :src="this.images[1]"></el-image>
      </div>
      <div>
        <p class="text-span1">在泸州全职工作，且符合相应条件的，可申领高层次人才奖励补贴。具体标准为:两院院士和杰出人才600万元,国家级领军人才和A类人才300万元;地方级领军人才和B类人才200万元;后备级人才和C类人才160万元,按五年任期分次发放。</p>
        <el-button class="button-style">奖励补贴</el-button>
        <p class="text-span1">申请人已被认定为区高层次人才，在人才认定((确认)的有效任期内，在本市全职工作，已参加国内外高水平学术会议，每人每年仅可享受一次此补贴。</p>
        <el-button class="button-style">学术研修津贴</el-button>
      </div>
      <el-divider></el-divider>
    </div>
    <!--    配套服务-->
    <div >
      <div class="image-part">
        <el-image
          :src="this.images[2]"></el-image>
      </div>
      <div class="text-part" style="margin-left: 26px">
        <h3 class="text-title1">配套服务</h3>
        <img src="../../../../../assets/images/portal/service/scene/underline.png" class="img-style"/>
        <p class="text-span1">奖励补贴、特殊支持计划盗金发放，落户，人才安居申请，配偶就业，子女入学(园)，学术研修津贴发放，医疗保健办理，国内、国(境)外学历学位认证，居留和出入境签证办理......</p>
        <el-button class="button-style">高层次人才一站式服务</el-button>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'first',
  data(){
    return{
      images: [
        require('@/assets/images/portal/service/scene/2_1.png'),
        require('@/assets/images/portal/service/scene/2_2.png'),
        require('@/assets/images/portal/service/scene/2_3.png'),
      ],
    }

  },
  methods: {

    goto(name, params) {
      this.$router.push({ name: name, params: { data: params } })
    },
  }
}
</script>

<style scoped>
/*字体*/
.text-title1{
  font-size: 24px;
  font-family: "PingFang SC", "PingFang SC-Bold";
  font-weight: 700;
  color: #212121;
}


/*间隔*/
p {
  word-wrap:break-word;
  white-space: pre-wrap;
}

/*布局*/
.content{
  clear: both;
}
.content div{
  /*border: 1px solid #19be6b;*/
  float: left;
}
.image-part{
  width: 19.875rem;
}
.text-part{
  width: 38.9375rem;
}

.img-style{
  position: relative;
  top: -14px;
}

/*按钮样式*/
.button-style {
  width: 289px;
  height: 43px;
  color: #ffffff;
  background: #0095F4;
  border-color: #0095F4;;
  margin-top: 20px;
  border-radius: 0;
}
.el-button--medium {
  font-size: 17px;
}





</style>

