<template>
  <!--  高校毕业生-->
  <div class="content">
    <!--    资格认定-->
    <div>
      <div class="image-part">
        <el-image
          :src="this.images[0]"></el-image>
      </div>
      <div class="text-part margin-left-div">
        <h3 class="text-title1">资格认定</h3>
        <img src="../../../../../assets/images/portal/service/scene/underline.png" class="img-style"/>
        <p class="text-span1">中国高等教育学历证书查询、学籍查询、在线验证</p>
        <el-button class="short-button" style="margin-top: 10px" @click="toPath('https://www.chsi.com.cn/xlcx/index.jsp')">学历认证</el-button>
      </div>
    </div>
    <el-divider></el-divider>
    <!--    工作机会-->
    <div>
      <div class="text-part">
        <h3 class="text-title1">工作机会</h3>
        <img src="../../../../../assets/images/portal/service/scene/underline.png" class="img-style"/>

        <div class="flex-div">
          <span>
            <el-button class="long-button" @click="toPath('https://www.lzsrsks.com/')">事业单位招聘</el-button>
          </span>
          <span class="text-span1 padding-left-div">泸州市事业单位招聘人员</span>
        </div>
        <div class="flex-div margin-top-div">
          <span>
            <el-button class="long-button"  @click="toPath('https://www.lzsrsks.com/News/NewsList.aspx?typeId=402998863ba3622a013bb75aca1e410f')">公务员招考</el-button>
          </span>
          <span class="text-span1 padding-left-div">泸州市公务员招考、选调公告</span>
        </div>
        <div class="flex-div margin-top-div">
          <span>
            <el-button class="long-button"  @click="toPath('https://www.rc168.com/web/rcda')">高校未就业高校毕业生实名登记</el-button>
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
    <div>
      <div class="image-part">
        <el-image
          :src="this.images[2]"></el-image>
      </div>
      <div class="text-part margin-left-div">
        <h3 class="text-title1">人才引进</h3>
        <img src="../../../../../assets/images/portal/service/scene/underline.png" class="img-style"/>
        <div class="flex-div">
          <span>
          <el-button class="short-button" @click="toPath('https://www.rc168.com/personinfo/rsdaInfoSearch?radaid=402881fb62d231540162d23f9fb00006')">毕业生接收</el-button>
          </span>
          <span class="text-span1 padding-left-div">具有全日制大专以上普通高等教育学历，身体健康，未参加国家禁止的组织及活动，无刑事犯罪记录。</span>
        </div>
        <div class="flex-div">
          <span>
          <el-button class="short-button" @click="toPath('http://zxbl.sczwfw.gov.cn/themes/themeService/getNewThServiceDh/4222782945531949056?serviceName=%E6%88%B7%E5%8F%A3%E8%BF%81%E7%A7%BB%E4%B8%80%E4%BB%B6%E4%BA%8B&areaCode=510502000000&themeClassName=%E4%B8%AA%E4%BA%BA%E6%9C%8D%E5%8A%A1')">户籍迁入</el-button>
          </span>
          <span class="text-span1 padding-left-div">经市(区)人力资源和社会保障局批准接收的应届毕业生，无参加国家禁止的组织或活动的记录，无刑事犯罪记录。</span>
        </div>
        <div class="flex-div">
          <span>
            <el-button class="short-button"  @click="toPath('https://www.rc168.com/personinfo/rsdaInfoSearch?radaid=402881fb62d231540162d23f9fb00006')">人事档案保管</el-button>
          </span>
          <span class="text-span1 padding-left-div">符合流动人员人事档案范围的个人或其工作单位可向户籍所在地或工作单位所在地县级以上(含县级)公共就业和人才服务机构申请保管人事档案。</span>
        </div>
        <div class="flex-div">
          <span>
            <el-button class="short-button" @click="toPath('https://www.rc168.com/personinfo/rsdaInfoSearch?radaid=402881fb62d231540162d23f9fb00006')">组织关系转移</el-button>
          </span>
          <span class="text-span1 padding-left-div">党员组织关系接转</span>
        </div>
      </div>
    </div>
    <el-divider></el-divider>

    <!--    福利补贴-->
    <div>
      <div class="text-part">
        <h3 class="text-title1">福利补贴</h3>
        <img src="../../../../../assets/images/portal/service/scene/underline.png" class="img-style"/>
        <p  class="text-span1">{{this.textData.data1}}</p>
<!--        <el-button class="long-button margin-top-div">泸州市新引进人才租房和生活补贴</el-button>-->
        <el-button class="short-button margin-top-div">安家补助</el-button>
      </div>
      <div class="image-part margin-left-div">
        <el-image
          :src="this.images[3]"></el-image>
      </div>
      <div class="margin-top-div">
        <p  class="text-span1">{{this.textData.data2}}</p>
        <el-button class="long-button margin-top-div">高校未就业高校毕业生实名登记</el-button>
      </div>
    </div>
    <el-divider></el-divider>

    <!--    人才成长-->
    <div>
      <div class="image-part">
        <el-image
          :src="this.images[4]"></el-image>
      </div>
      <div class="text-part margin-left-div">
        <h3 class="text-title1">人才成长</h3>
        <img src="../../../../../assets/images/portal/service/scene/underline.png" class="img-style"/>
        <p class="text-span1">各专业职称评审条件执行1998年以来四川省颁布的高、中级专业技术资格条件和有关评审政策文件，具体参考相应系列相应专业技术资格条件(可查阅《关于开展2018年度专业技术资格评审工作的通知》、各专业对应的《四川省XXXX专业技术资格条件》)。</p>
        <el-button class="short-button" style="margin-top: 10px">职称评审</el-button>
      </div>
    </div>
    <el-divider></el-divider>

    <!--    配套服务-->
    <div>
      <div class="text-part">
        <h3 class="text-title1">配套服务</h3>
        <img src="../../../../../assets/images/portal/service/scene/underline.png" class="img-style"/>
        <div class="flex-div">
          <span>
          <el-button class="short-button"  @click="toPath('https://www.lzsrcpx.com/')">继续教育</el-button>
          </span>
          <span class="text-span1 padding-left-div">
            四川省专业技术人员继续教育管理系统，供全省专业技术人员登陆免费学习，学习任务完成后，系统自动记录所学科目学时。
          </span>
        </div>
        <div class="flex-div">
          <span>
          <el-button class="short-button">申报职业技能鉴定</el-button>
          </span>
          <span class="text-span1 padding-left-div">
            符合各职业((工种)的国家职业标准中申报条件的考生，可参加相应职业(工种)和等级的职业技能鉴定
          </span>
        </div>
      </div>
      <div class="image-part margin-left-div">
        <el-image
          :src="this.images[5]"></el-image>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'second',
  data(){
    return{
      images: [
        require('@/assets/images/portal/service/scene/1_1.png'),
        require('@/assets/images/portal/service/scene/1_2.png'),
        require('@/assets/images/portal/service/scene/1_3.png'),
        require('@/assets/images/portal/service/scene/1_4.png'),
        require('@/assets/images/portal/service/scene/1_5.png'),
        require('@/assets/images/portal/service/scene/1_6.png'),
      ],
      textData:{
        data1: '符合以下全部条件的，可以申请新引进人才租房和生活补贴:\n' +
          '(1)具有全日制普通高等教育本科以上学历，学历以办理引进手续时申报的为准;\n' +
          '(2) 应届毕业生和归国留学人员无年龄限制。在职人才应当符合下列年龄条件:本科的未满30周岁、硕士的未满35周岁、博士的未满40周岁。上述年龄条件以调入年度的1月1日为判断时点;\n' +
          '(3) 具有泸州户籍;\n' +
          '(4)在泸州缴纳了社会保险费，且社会保险关系仍在本市;\n' +
          '(5)本人未享受过购房优惠政策、未正在租住公租房。',
        data2: '"毕业2年内的中国大陆地区全日制大专及以上普通高校统招毕业生或技工院校高级工班及以上毕业生。申请人符合以下条件之一的，可自愿申请补贴:\n' +
          '(一)在中小微企业就业的申请条件(同时满足以下条件):\n' +
          '1.申请人于毕业2年内，在本市登记注册、具有独立法人资格且正常经营的中小微企业实现就业，并在该企业正常参加我市社会保险,\n' +
          '2.与该企业签订了一年或以上期限劳动合同;\n' +
          '3.在该企业连续正常缴交社会保险费满6个月。\n' +
          '二)在社会组织就业的申请条件(同时满足以下条件):\n' +
          '1.申请人于毕业2年内，在本市民政部门登记成立且正常经营的社会团体、社会服务机构民办非企业单位或基金会等社会组织实现就\n' +
          '业，并在该社会组织正常参加我市社会保险:\n' +
          '2.与该社会组织签订1年或以上期限劳动合同;\n' +
          '3.在该社会组织连续正常缴交社会保险费满6个月\n' +
          '(三)在社区社会管理和公共服务岗位就业的申请条件。(同时满足以下条件):\n' +
          '1.申请人于毕业2年内，在本市社区(包括但不限于社区党委、工作站、居委会等部门)的社会管理和公共服务岗位(包括但不限于社区服务、管理、党建等社区工作的全日制专职岗位)实现就业:\n' +
          '2.与相关单位就此岗位签订1年或以上期限劳动合同或就业协议;\n' +
          '3.该岗位就业期间，在签订劳动合同或就业协议的单位连续正常缴交社会保险费满6个月。'
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

/*文字板块对齐*/
.flex-div {
  display: flex;
  margin-top: 10px;
}

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
  width: 190px;
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
