<template>
  <div class="content">
    <!--    资格认定-->
    <div >
      <div class="image-part">
        <el-image
          :src="this.images[0]"></el-image>
      </div>
      <div class="text-part margin-left-div ">
        <h3 class="text-title1">资格认定</h3>
        <img src="../../../../../assets/images/portal/service/scene/underline.png" class="img-style"/>
        <p class="text-span1">各专业职称评审条件执行1998年以来四川省颁布的高、中级专业技术资格条件和有关评审政策文件，具体参考相应系列相应专业技术资格条件(可查阅《关于开展2018年度专业技术资格评审工作的通知》、各专业对应的《四川省XXXX专业技术资格条件》)。
        </p>
        <el-button class="short-button margin-top-div">职称评审</el-button>
      </div>
    </div>
    <el-divider></el-divider>
    <!--    人才引进-->
    <div >
      <div class="text-part  ">
        <h3 class="text-title1">人才引进</h3>
        <img src="../../../../../assets/images/portal/service/scene/underline.png" class="img-style"/>
        <p class="text-span1">具有高级专业技术资格，且年龄在50周岁以下的人员:具有中级专业技术资格，且年龄在45周岁以下的人员，具有中专以上学历，非四川省评定颁发的专业技术资格(不含经全国统考取得)须经过本市(区)人力资源部门审核。
        </p>
        <el-button class="long-button margin-top-div">人才引进</el-button>
        <p class="text-span1 margin-top-div">具有高级专业技术资格，且年龄在50周岁以下的人员;具有中级专业技术资格，且年龄在45周岁以下的人员，具有中专以上学历，非四川省评定颁发的专业技术资格(不含经全国统考取得)须经过本市(区)人力资源部门审核。
        </p>
        <el-button class="long-button margin-top-div">户籍迁入</el-button>
      </div>
      <div class="image-part  margin-left-div ">
        <el-image
          :src="this.images[1]"></el-image>
      </div>
    </div>
    <el-divider></el-divider>
    <!--    福利补贴-->
    <div >
      <div class="image-part  ">
        <el-image
          :src="this.images[2]"></el-image>
      </div>
      <div class="text-part  margin-left-div ">
        <h3 class="text-title1">福利补贴</h3>
        <img src="../../../../../assets/images/portal/service/scene/underline.png" class="img-style"/>
        <p class="text-span1">{{this.textData.data1}}</p>
        <el-button class="long-button margin-top-div">泸州市新引进人才租房和生活补贴</el-button>
      </div>
    </div>
    <el-divider></el-divider>
    <!--    配套服务-->
    <div >
      <div class="text-part ">
        <h3 class="text-title1">配套服务</h3>
        <img src="../../../../../assets/images/portal/service/scene/underline.png" class="img-style"/>
        <p class="text-span1">
          由泸州市人力资源和社会保障局或原泸州市职称管理办公室审批发放的专业技术资格证书已遗失或破损，符合补发、换发条件。
        </p>
        <el-button class="long-button margin-top-div" style="width: 437px">专业技术资格证书(评审、考核认定取得)补(换)发</el-button>
      </div>
      <div class="image-part  margin-left-div ">
        <el-image
          :src="this.images[3]"></el-image>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name:'fourth',
  data() {
    return {
      images: [
        require('@/assets/images/portal/service/scene/4_1.png'),
        require('@/assets/images/portal/service/scene/4_2.png'),
        require('@/assets/images/portal/service/scene/4_3.png'),
        require('@/assets/images/portal/service/scene/4_4.png'),
      ],
      textData: {
        data1: '符合以下全部条件的，可以申请新引进人才租房和生活补贴:\n' +
          '(1)具有全日制普通高等教育本科以上学历，学历以办理引进手续时申报的为准;\n' +
          '(2) 应届毕业生和归国留学人员无年龄限制。在职人才应当符合下列年龄条件:本科的未满30周岁、硕士的未满35周岁、博士的未满40周岁。上述年龄条件以调入年度的1月1日为判断时点;\n' +
          '(3) 具有泸州户籍;\n' +
          '(4)在泸州缴纳了社会保险费，且社会保险关系仍在本市;\n' +
          '(5)本人未享受过购房优惠政策、未正在租住公租房。',
      }
    }
  }
}
</script>

<style scoped>
/*字体*/

p {
  word-wrap:break-word;
  white-space: pre-wrap;
}

/*布局*/
.margin-top-div{
  margin-top: 25px;
}
.margin-left-div{
  margin-left: 16px;
}
.padding-left-div{
  padding-left: 16px;
}
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
.short-button {
  width: 220px;
  height: 43px;
  color: #ffffff;
  background: #0095F4;
  border-color: #0095F4;;
  border-radius: 0;
}

.long-button {
  width: 289px;
  height: 43px;
  color: #ffffff;
  background: #0095F4;
  border-color: #0095F4;;
  border-radius: 0;
}
.el-button--medium {
  font-size: 17px;
}

</style>
