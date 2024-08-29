<template>
  <div class="content">
    <!--    资格认定-->
    <div >
      <div class="image-part">
        <el-image
          :src="this.images[0]"></el-image>
      </div>
      <div class="text-part margin-left-div">
        <h3 class="text-title1">资格认定</h3>
        <img src="../../../../../assets/images/portal/service/scene/underline.png" class="img-style"/>
        <p class="text-span1">
          国(境)外学历学位认证申请、查询认证进度、下载认证结果
        </p>
        <el-button class="short-button margin-top-div" @click="toPath('https://www.chsi.com.cn/xlcx/index.jsp')">学历认证</el-button>
        <p class="text-span1 margin-top-div">
          在国(境)外学习并获得学士以上学位的留学人员 (包括已获得居住国永久居留权、留学国再入境资格者)，或者在国(境)外高等院校、科研机构工作或学习一年以上并取得一定成果的访问学者或博士后等进修人员。
        </p>
        <el-button class="short-button margin-top-div" >资格审定</el-button>
      </div>
    </div>
    <el-divider></el-divider>
    <!--    工作机会-->
    <div >
      <div class="text-part ">
        <h3 class="text-title1">工作机会</h3>
        <img src="../../../../../assets/images/portal/service/scene/underline.png" class="img-style"/>
        <div class="flex-div">
          <span>
            <el-button class="long-button">事业单位招聘</el-button>
          </span>
          <span class="text-span1 padding-left-div">泸州市事业单位招聘人员</span>
        </div>
        <div class="flex-div margin-top-div">
          <span>
            <el-button class="long-button">公务员招考</el-button>
          </span>
          <span class="text-span1 padding-left-div">泸州市公务员招考、选调公告</span>
        </div>
        <div class="flex-div margin-top-div">
          <span>
            <el-button class="long-button">高校未就业高校毕业生实名登记</el-button>
          </span>
          <span class="text-span1 padding-left-div">已毕业离校两年为工作的人员可以办理未就业登记</span>
        </div>
      </div>
      <div class="image-part margin-left-div">
        <el-image
          :src="this.images[1]"></el-image>
      </div>
    </div>
    <el-divider></el-divider>
    <!--    人才引进-->
    <div >
      <div class="image-part  ">
        <el-image
          :src="this.images[2]"></el-image>
      </div>
      <div class="text-part margin-left-div ">
        <h3 class="text-title1">人才引进</h3>
        <img src="../../../../../assets/images/portal/service/scene/underline.png" class="img-style"/>
        <p class="text-span1">
          年龄在45周岁以下，取得我市《出国留学人员资格证明》
        </p>
        <el-button class="short-button margin-top-div">留学人员引进</el-button>
        <p class="text-span1 margin-top-div">
          经我市(区)组织、人力资源和社会保障部门批准引进的留学生及随同批准迁入的随迁人员，可到我市公安机关核准迁户;申请人无参加国家禁止的组织或活动的记录。
        </p>
        <el-button class="short-button margin-top-div">户籍迁入</el-button>
        <p class="text-span1 margin-top-div">
          符合流动人员人事档案范围的个人或其工作单位可向户籍所在地或工作单位所在地县级以上(含县级)公共就业和人才服务机构申请保管人事档案。
        </p>
        <el-button class="short-button margin-top-div">人事档案保管</el-button>
      </div>
    </div>
    <el-divider></el-divider>
    <!--    福利补贴-->
    <div >
      <div class="text-part ">
        <h3 class="text-title1">福利补贴</h3>
        <img src="../../../../../assets/images/portal/service/scene/underline.png" class="img-style"/>
        <p class="text-span1">{{this.textData.data1}}</p>
        <el-button class="long-button margin-top-div">泸州市新引进人才租房和生活补贴</el-button>
      </div>
      <div class="image-part  margin-left-div">
        <el-image
          :src="this.images[3]"></el-image>
      </div>
    </div>
    <el-divider></el-divider>
    <!--    配套服务-->
    <div >
      <div class="image-part ">
        <el-image
          :src="this.images[4]"></el-image>
      </div>
      <div class="text-part  margin-left-div">
        <h3 class="text-title1">人才成长</h3>
        <img src="../../../../../assets/images/portal/service/scene/underline.png" class="img-style"/>
        <p class="text-span1">
          海外高层次A类人才年龄应在60周岁以下;海外高层次B类人才年龄应在55周岁以下;海外高层次C类人才年龄应在40周岁以下。
        </p>
        <el-button class="short-button margin-top-div" >海外高层次人才认定</el-button>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name:'third',
  data() {
    return {
      images: [
        require('@/assets/images/portal/service/scene/3_1.png'),
        require('@/assets/images/portal/service/scene/3_2.png'),
        require('@/assets/images/portal/service/scene/3_3.png'),
        require('@/assets/images/portal/service/scene/3_4.png'),
        require('@/assets/images/portal/service/scene/3_5.png'),
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
  },
  methods: {
    toPath(url) {
      window.open(url)
    },
  }

}
</script>

<style scoped>
/*字体*/
p {
  word-wrap:break-word;
  white-space: pre-wrap;
}

/*文字板块对齐*/
.flex-div {
  display: flex;
  margin-top: 10px;
}

/*布局样式*/
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
