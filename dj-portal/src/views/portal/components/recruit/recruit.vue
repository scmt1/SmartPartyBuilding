<template>
  <div class="recruit_body main_content">
    <!--    顶部搜索栏-->
    <div class="head_search">
      <el-input placeholder="请输入内容" v-model="input_top" class="top_search">
      </el-input>
      <button class="search_button">搜索</button>
      <div class="search_tips">大家都在搜:<a class="search_a">销售 JAVA 人事 会计 UI 平面设计 行政 WEB前端 房地产</a></div>
    </div>

    <!--    顶部大板块-->
    <div class="top">
      <div class="zp_right">
        <div class="zp_tab">
          <el-tabs v-model="activeclass"
                   @tab-click="handleClick">
            <el-tab-pane label="职位类型" name="first"></el-tab-pane>
            <el-tab-pane label="区县分类" name="second"></el-tab-pane>
            <el-tab-pane label="求职类型" name="third"></el-tab-pane>
          </el-tabs>
        </div>
        <a>
          <Row align="middle" class="rt_fl home_fs"
               v-for="(item,index) in zpflList" :key="index">
            <Col :span="20">{{item}}</Col>
            <Col :span="4" style="text-align: center">></Col>
          </Row>
        </a>
        <div class="zp_tab_down"></div>
      </div>
      <div class="top_mid">
        <el-carousel :interval="5000"
                     :autoplay="false"
                     arrow="never"
                     class="top_l">
          <el-carousel-item v-for="item in 5" :key="item">
            <img src="~@/assets/images/portal/recruit/banner.png" class="top_l_img">
          </el-carousel-item>
        </el-carousel>
      </div>
      <div class="top_right">
        <div class="top_login">
          <img src="~@/assets/images/portal/recruit/二维码切换@2x.png" class="login_img">
          <el-tabs v-model="activelogin" @tab-click="handleClick">
            <el-tab-pane label="手机登录" name="first">
              <el-input placeholder="请输入手机号码" v-model="input_phone" class="top_phone">
              </el-input>
              <div class="top_code">
                <el-input placeholder="请输入验证码" v-model="input_code" class="top_phone_code">
                </el-input>
                <button class="login_code_button">获取验证码</button>
              </div>
              <button class="login_button">立即登录</button>
              <a href="" class="forget">忘记密码</a>
            </el-tab-pane>
            <el-tab-pane label="账号登录" name="second">
              <el-input placeholder="请输入账号" v-model="input_act" class="top_phone">
              </el-input>
              <div class="top_code">
                <el-input placeholder="请输入密码" v-model="input_psw" class="top_phone">
                </el-input>
              </div>
              <button class="login_button">立即登录</button>
              <a href="" class="forget">忘记密码</a>
            </el-tab-pane>
          </el-tabs>
        </div>
        <div class="top_message">
          <img src="~@/assets/images/portal/recruit/头像@2x.png" class="msg_img">
          <p class="msg_font" id="msgf1">尊敬的李湘先生</p>
          <p class="msg_font" id="msgf2">您好！</p>
          <p class="msg_font" id="msgf3">欢迎来到智慧党建云平台</p>
          <button class="msg_btn">编辑简历</button>
        </div>
      </div>
    </div>

    <!--    热门招聘-->
    <div class="main_margin">
      <div class="home_title">
        <h3 class="title-h3">热门招聘</h3>
        <span class="title-span" @click="goto('type')">更多</span>
      </div>
      <!--      <div class="rmzp_logo logo_size"></div>-->
    </div>
    <div class="hot_recruit">
      <!--      <a href="" class="view" id="rmzp_more">查看更多 ></a>-->
      <div class="recruitment" >
        <el-carousel indicator-position="none"
                     trigger="click"
                     arrow="always"
                     :autoplay="true"
                     :interval="4000">
          <el-carousel-item v-for="(item,index) in recruitment" :key="index" class="recruit_l">
            <div class="recruit_card">
              <div class="recruit_c" v-for="(item,index) in item" :key="index" >
                <p class="recruit_state">{{ item.state }}</p>
                <img :src="item.idView" class="recruit_i">
                <p class="recruit_font">{{ item.msg }}</p>
                <button class="recruit_b">正在进行 ></button>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>

    <!--    通知公告-->

    <div class="main_margin">
      <div class="home_title">
        <h3 class="title-h3">通知公告</h3>
        <span class="title-span" @click="goto('type')">更多</span>
      </div>
      <!--      <div class="tzgg_logo logo_size"></div>-->

    </div>
    <div class="announcement">
      <div class="announcement_left">
        <img src="~@/assets/images/portal/recruit/通知公告banner@2x.png" class="announcement_left_i">
        <p class="announcement_left_title">【成都理工大学线下双选】“就业向未来的选择”</p>
        <p class="announcement_left_content">成都晚报掌上成都12月21日讯（全媒体记者 李静 通讯员 黄辉 实习生 陈德钰）让本色永恒，让奋斗继续。</p>
        <p class="announcement_left_time">2022-12-21</p>
      </div>
      <div class="announcement_right">
        <a href="" class="view" id="tzgg_more">查看更多 ></a>
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="最新通知" name="first">
            <div class="notice" v-for="(item,index) in notice" :key="index">
              <div class="dot"></div>
              <div class="notice_font">{{ item.title }}</div>
              <div class="notice_time">{{ item.time }}</div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="政策公告" name="second">
            <div class="notice" v-for="(item,index) in notice" :key="index">
              <div class="dot"></div>
              <div class="notice_font">{{ item.title }}</div>
              <div class="notice_time">{{ item.time }}</div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <!--    公招信息-->

    <div class="main_margin">
      <div class="home_title">
        <h3 class="title-h3">公招信息</h3>
        <span class="title-span" @click="goto('type')">更多</span>
      </div>
      <!--      <div class="gzxx_logo logo_size"></div>-->

    </div>
    <div class="information">
<!--      <a href="" class="view" id="gzxx_more">查看更多 ></a>-->
      <div class="recruit">招聘公示</div>
      <div class="public" >
        <el-carousel indicator-position="none"
                     trigger="click"
                     arrow="always"
                     :autoplay="true"
                     :interval="4000">
          <el-carousel-item v-for="(item,index) in recruit" :key="index" class="public_l">
            <div class="public_card">
              <div class="public_c" v-for="(item,index) in item" :key="index" >
                <img :src="item.idView" class="public_i">
                <p class="public_font">{{ item.msg }}</p>
                <p class="public_font">{{ item.Ftime }}至{{ item.Ltime }}</p>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
<!--      <div class="admit">录取公示</div>
      <div class="public" >
        <el-carousel indicator-position="none"
                     trigger="click"
                     arrow="always"
                     :autoplay="true"
                     :interval="4000">
          <el-carousel-item v-for="(item,index) in admit" :key="index" class="public_l">
            <div class="public_card">
              <div class="public_c" v-for="(item,index) in item" :key="index" >
                <img :src="item.idView" class="public_i">
                <p class="public_font">{{ item.msg }}</p>
                <p class="public_font">{{ item.Ftime }}至{{ item.Ltime }}</p>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>-->
    </div>

    <!--    就业指导-->
    <div class="main_margin">
      <div class="home_title">
        <h3 class="title-h3">就业指导</h3>
        <span class="title-span" @click="goto('type')">更多</span>
      </div>
<!--      <div class="jyzd_logo logo_size"></div>-->
    </div>
    <div class="guide">
<!--      <a href="" class="view" id="jyzd_more">查看更多 ></a>-->
      <div class="guide_c" v-for="(item,index) in guide" :key="index">
        <img :src="item.back" class="guide_back">
        <div class="guide_left">
          <p class="guide_title">{{ item.title }}</p>
          <p class="guide_font">{{ item.time }}</p>
          <div class="guide_buttom">
            <img src="~@/assets/images/portal/recruit/看过图标@3x.png" class="guide_icon">
            <p class="guide_font">{{ item.people }}人看过</p>
          </div>
        </div>
        <div class="guide_right">
          <button class="guide_button" :style="{backgroundColor: item.color}">查看详情 ></button>
        </div>
      </div>
    </div>

    <!--    最新职位-->
    <div class="main_margin">
      <div class="home_title">
        <h3 class="title-h3">最新职位</h3>
        <span class="title-span" @click="goto('type')">更多</span>
      </div>
<!--      <div class="zxzw_logo logo_size"></div>-->
    </div>
    <div class="job">
<!--      <div class="change" id="zxzw_change"><i class="el-icon-refresh"></i>换一批</div>-->
<!--      <a href="" class="view" id="zxzw_more">查看更多 ></a>-->
      <el-tabs v-model="jobclass" @tab-click="handleClick" class="job_card">
        <el-tab-pane label="精选职位" name="first">
          <div class="job_c" v-for="(item,index) in job" :key="index">
            <div class="job_top">
              <p class="job_job">{{ item.job }}</p>
              <p class="job_money">{{ item.money }}</p>
            </div>
            <div class="job_mid">
              <div class="job_tag">{{ item.year }}</div>
              <div class="job_tag">{{ item.edu }}</div>
              <button class="job_btn">立即投递 ></button>
            </div>
            <div class="job_bottom">
              <p class="job_company">{{ item.company }}</p>
              <p class="job_local">{{ item.local }}</p>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="最新职位" name="second">
          <div class="job_c" v-for="(item,index) in job" :key="index">
            <div class="job_top">
              <p class="job_job">{{ item.job }}</p>
              <p class="job_money">{{ item.money }}</p>
            </div>
            <div class="job_mid">
              <div class="job_tag">{{ item.year }}</div>
              <div class="job_tag">{{ item.edu }}</div>
              <button class="job_btn">立即投递 ></button>
            </div>
            <div class="job_bottom">
              <p class="job_company">{{ item.company }}</p>
              <p class="job_local">{{ item.local }}</p>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!--    求职名片-->

    <div class="main_margin">
      <div class="home_title">
        <h3 class="title-h3">求职名片</h3>
        <span class="title-span" @click="goto('type')">更多</span>
      </div>
<!--      <div class="qzmp_logo logo_size"></div>-->
    </div>
    <div class="card">
<!--      <div class="change" id="qzmp_change"><i class="el-icon-refresh"></i>换一批</div>-->
<!--      <a href="" class="view" id="qzmp_more">查看更多 ></a>-->
      <div class="card_c" v-for="(item,index) in person" :key="index">
        <div class="card_left">
          <img :src="item.idView" class="card_head">
        </div>
        <div class="card_right">
          <div class="card_top">
            <div class="card_name">{{ item.name }}</div>
            <img src="~@/assets/images/portal/recruit/new.png" style="width: 48.3px;height: 15px">
            <div class="card_complete">完整度： <span style="color: #0758CB">{{ item.complete }}</span></div>
          </div>
          <p class="card_message">{{ item.sex }} 丨 {{ item.age }} 丨 {{ item.educational }} 丨 {{ item.year }}</p>
          <p class="card_job">{{ item.job }}</p>
          <div class="card_bottom">
            <img src="~@/assets/images/portal/recruit/seat.png" class="card_local">
            <p class="card_location">{{ item.location }}</p>
          </div>
        </div>
      </div>
    </div>
    <!--    热门企业-->

    <div class="main_margin">
      <div class="home_title">
        <h3 class="title-h3">热门企业</h3>
        <span class="title-span" @click="goto('type')">更多</span>
      </div>
<!--      <div class="rmqy_logo logo_size"></div>-->
    </div>
    <div class="company">
<!--      <div class="change" id="rmqy_change"><i class="el-icon-refresh"></i>换一批</div>-->
<!--      <a href="" class="view" id="rmqy_more">查看更多 ></a>-->
      <div class="company_c" v-for="(item,index) in company" :key="index">
        <img :src="item.idView" class="company_i" alt="">
        <div class="company_w">1个在招职位</div>
      </div>
    </div>

  </div>



</template>

<script>
export default {
  name: "recruit",
  data() {
    return {

      //顶部版块
      zpflList:["互联网/开发/工程师","金融/风控/证券","房地产/建筑","贸易/零售",
        "教育/传媒/广告/设计","服务业/导游/顾问/客服","市场/销售","人事/财务/行政"],
      input_top: '',
      input_phone: '',
      input_code: '',
      input_act: '',
      input_psw: '',
      activeclass: 'first',
      activelogin: 'first',
      //通知公告
      activeName: 'first',
      notice: [
        {
          id:0,
          title:'泸州市人力资源和社会保障局关于举办人才选拔会保障局关于举...',
          time:'2023-01-11'
        },{
          id:1,
          title:'悦来·酒城——泸州市2022悦来·酒城——泸州黏啊年赴高校招聘...',
          time:'2023-01-11'
        },{
          id:2,
          title:'【成都理工大学线下双选】“成都理工大学线下双选就业向未来...',
          time:'2023-01-11'
        },{
          id:3,
          title:'关于举办2022年“智汇天府”公共招聘进关于举办2022年“智汇...',
          time:'2023-01-11'
        },{
          id:4,
          title:'西南医科大学附属口腔医院招募第四批公西南医科大学附属口腔...',
          time:'2023-01-11'
        },{
          id:5,
          title:'【空中双选会】“E职在线，职等你来”空中双选E职在线，职等......',
          time:'2023-01-11'
        },{
          id:6,
          title:'西南医科大学附属口腔医院招募第四批公西南医科大学附属口腔...',
          time:'2023-01-11'
        }
      ],
      //热门招聘
      recruitment: [
        [
          {
            id:0,
            state:'线上',
            idView:require("@/assets/images/portal/recruit/rmzp_1.png"),
            msg:'2023年泸州市高校毕业生就业服务...',
          },
          {
            id:1,
            state:'线下',
            idView:require("@/assets/images/portal/recruit/rmzp_2.png"),
            msg:'2023年泸州市高校毕业生就业服务...',
          },
          {
            id:2,
            state: '线下',
            idView:require("@/assets/images/portal/recruit/rmzp_3.png"),
            msg:'2023年泸州市高校毕业生就业服务...',
          },
          {
            id:3,
            state: '线上',
            idView:require("@/assets/images/portal/recruit/rmzp_4.png"),
            msg:'2023年泸州市高校毕业生就业服务...',
          },
        ],
        [
          {
            id:0,
            state:'线上',
            idView:require("@/assets/images/portal/recruit/rmzp_1.png"),
            msg:'2023年泸州市高校毕业生就业服务...',
          },
          {
            id:1,
            state:'线下',
            idView:require("@/assets/images/portal/recruit/rmzp_2.png"),
            msg:'2023年泸州市高校毕业生就业服务...',
          },
          {
            id:2,
            state: '线下',
            idView:require("@/assets/images/portal/recruit/rmzp_3.png"),
            msg:'2023年泸州市高校毕业生就业服务...',
          },
          {
            id:3,
            state: '线上',
            idView:require("@/assets/images/portal/recruit/rmzp_4.png"),
            msg:'2023年泸州市高校毕业生就业服务...',
          },
        ],
        [
          {
            id:0,
            state:'线上',
            idView:require("@/assets/images/portal/recruit/rmzp_1.png"),
            msg:'2023年泸州市高校毕业生就业服务...',
          },
          {
            id:1,
            state:'线下',
            idView:require("@/assets/images/portal/recruit/rmzp_2.png"),
            msg:'2023年泸州市高校毕业生就业服务...',
          },
          {
            id:2,
            state: '线下',
            idView:require("@/assets/images/portal/recruit/rmzp_3.png"),
            msg:'2023年泸州市高校毕业生就业服务...',
          },
          {
            id:3,
            state: '线上',
            idView:require("@/assets/images/portal/recruit/rmzp_4.png"),
            msg:'2023年泸州市高校毕业生就业服务...',
          },
        ],
      ],
      //招聘公示
      recruit: [
        [
          {
            id:0,
            idView:require("@/assets/images/portal/recruit/gzxx_1.png"),
            msg:'泸州兴阳博睿人力资源服务有限公司关于公开招聘...',
            Ftime:'2023-1-1',
            Ltime:'2023-2-18'
          },{
          id:1,
          idView:require("@/assets/images/portal/recruit/gzxx_2.png"),
          msg:'泸州兴阳博睿人力资源服务有限公司关于公开招聘...',
          Ftime:'2023-1-1',
          Ltime:'2023-2-18'
        },
          {
            id:2,
            idView:require("@/assets/images/portal/recruit/gzxx_3.png"),
            msg:'泸州兴阳博睿人力资源服务有限公司关于公开招聘...',
            Ftime:'2023-1-1',
            Ltime:'2023-2-18'
          },
        ],
        [
          {
            id:0,
            idView:require("@/assets/images/portal/recruit/gzxx_1.png"),
            msg:'泸州兴阳博睿人力资源服务有限公司关于公开招聘...',
            Ftime:'2023-1-1',
            Ltime:'2023-2-18'
          },{
          id:1,
          idView:require("@/assets/images/portal/recruit/gzxx_2.png"),
          msg:'泸州兴阳博睿人力资源服务有限公司关于公开招聘...',
          Ftime:'2023-1-1',
          Ltime:'2023-2-18'
        },
          {
            id:2,
            idView:require("@/assets/images/portal/recruit/gzxx_3.png"),
            msg:'泸州兴阳博睿人力资源服务有限公司关于公开招聘...',
            Ftime:'2023-1-1',
            Ltime:'2023-2-18'
          },
        ],
        [
          {
            id:0,
            idView:require("@/assets/images/portal/recruit/gzxx_1.png"),
            msg:'泸州兴阳博睿人力资源服务有限公司关于公开招聘...',
            Ftime:'2023-1-1',
            Ltime:'2023-2-18'
          },{
          id:1,
          idView:require("@/assets/images/portal/recruit/gzxx_2.png"),
          msg:'泸州兴阳博睿人力资源服务有限公司关于公开招聘...',
          Ftime:'2023-1-1',
          Ltime:'2023-2-18'
        },
          {
            id:2,
            idView:require("@/assets/images/portal/recruit/gzxx_3.png"),
            msg:'泸州兴阳博睿人力资源服务有限公司关于公开招聘...',
            Ftime:'2023-1-1',
            Ltime:'2023-2-18'
          },
        ],
      ],
      //录取公示
      admit: [
        [
          {
            id:0,
            idView:require("@/assets/images/portal/recruit/公招信息图4@2x.png"),
            msg:'泸州兴阳博睿人力资源服务有限公司关于公开招聘...',
            Ftime:'2023-1-1',
            Ltime:'2023-2-18'
          },{
          id:1,
          idView:require("@/assets/images/portal/recruit/公招信息图5@2x.png"),
          msg:'泸州兴阳博睿人力资源服务有限公司关于公开招聘...',
          Ftime:'2023-1-1',
          Ltime:'2023-2-18'
        },
          {
            id:2,
            idView:require("@/assets/images/portal/recruit/公招信息图6@2x.png"),
            msg:'泸州兴阳博睿人力资源服务有限公司关于公开招聘...',
            Ftime:'2023-1-1',
            Ltime:'2023-2-18'
          },
        ],
        [
          {
            id:0,
            idView:require("@/assets/images/portal/recruit/公招信息图4@2x.png"),
            msg:'泸州兴阳博睿人力资源服务有限公司关于公开招聘...',
            Ftime:'2023-1-1',
            Ltime:'2023-2-18'
          },{
          id:1,
          idView:require("@/assets/images/portal/recruit/公招信息图5@2x.png"),
          msg:'泸州兴阳博睿人力资源服务有限公司关于公开招聘...',
          Ftime:'2023-1-1',
          Ltime:'2023-2-18'
        },
          {
            id:2,
            idView:require("@/assets/images/portal/recruit/公招信息图6@2x.png"),
            msg:'泸州兴阳博睿人力资源服务有限公司关于公开招聘...',
            Ftime:'2023-1-1',
            Ltime:'2023-2-18'
          },
        ],
        [
          {
            id:0,
            idView:require("@/assets/images/portal/recruit/公招信息图4@2x.png"),
            msg:'泸州兴阳博睿人力资源服务有限公司关于公开招聘...',
            Ftime:'2023-1-1',
            Ltime:'2023-2-18'
          },{
          id:1,
          idView:require("@/assets/images/portal/recruit/公招信息图5@2x.png"),
          msg:'泸州兴阳博睿人力资源服务有限公司关于公开招聘...',
          Ftime:'2023-1-1',
          Ltime:'2023-2-18'
        },
          {
            id:2,
            idView:require("@/assets/images/portal/recruit/公招信息图6@2x.png"),
            msg:'泸州兴阳博睿人力资源服务有限公司关于公开招聘...',
            Ftime:'2023-1-1',
            Ltime:'2023-2-18'
          },
        ]
      ],
      //就业指导
      guide: [
        {
          id: 0,
          title: '求职防骗六大注意事项',
          time: '2023-01-11',
          people: '1627',
          back:require("@/assets/images/portal/recruit/jyzd_1.png"),
          color: '#3FBE6F',
        },{
          id: 1,
          title: '签订劳动合同有什么意义？',
          time: '2023-01-11',
          people: '1627',
          back:require("@/assets/images/portal/recruit/jyzd_2.png"),
          color: '#289DFF',
        },
        {
          id: 2,
          title: '成功的前提什么比较重要？',
          time: '2023-01-11',
          people: '1627',
          back:require("@/assets/images/portal/recruit/jyzd_3.png"),
          color: '#8E67FF',
        },{
          id: 3,
          title: '面试十大难题的样板回答',
          time: '2023-01-11',
          people: '1627',
          back:require("@/assets/images/portal/recruit/jyzd_4.png"),
          color: '#EAA00D',
        },{
          id: 4,
          title: '职场“空窗期”不可胡编乱造',
          time: '2023-01-11',
          people: '1627',
          back:require("@/assets/images/portal/recruit/jyzd_5.png"),
          color: '#FF501C',
        },{
          id: 5,
          title: '不要与面试官“套近乎”',
          time: '2023-01-11',
          people: '1627',
          back:require("@/assets/images/portal/recruit/jyzd_6.png"),
          color: '#F74E4C',
        }
      ],
      // 最新职位
      jobclass: 'first',
      job: [
        {
          id:0,
          job:"Java中级开发工程师",
          money:"8千-1.5万",
          year:"3-5年",
          edu:"本科",
          company:"东华软件股份有限公司",
          local:"泸州-江阳区"
        },{
          id:1,
          job:"Java中级开发工程师",
          money:"8千-1.5万",
          year:"3-5年",
          edu:"本科",
          company:"东华软件股份有限公司",
          local:"泸州-江阳区"
        },{
          id:2,
          job:"Java中级开发工程师",
          money:"8千-1.5万",
          year:"3-5年",
          edu:"本科",
          company:"东华软件股份有限公司",
          local:"泸州-江阳区"
        },{
          id:3,
          job:"Java中级开发工程师",
          money:"8千-1.5万",
          year:"3-5年",
          edu:"本科",
          company:"东华软件股份有限公司",
          local:"泸州-江阳区"
        },{
          id:4,
          job:"Java中级开发工程师",
          money:"8千-1.5万",
          year:"3-5年",
          edu:"本科",
          company:"东华软件股份有限公司",
          local:"泸州-江阳区"
        },{
          id:5,
          job:"Java中级开发工程师",
          money:"8千-1.5万",
          year:"3-5年",
          edu:"本科",
          company:"东华软件股份有限公司",
          local:"泸州-江阳区"
        },{
          id:6,
          job:"Java中级开发工程师",
          money:"8千-1.5万",
          year:"3-5年",
          edu:"本科",
          company:"东华软件股份有限公司",
          local:"泸州-江阳区"
        },{
          id:7,
          job:"Java中级开发工程师",
          money:"8千-1.5万",
          year:"3-5年",
          edu:"本科",
          company:"东华软件股份有限公司",
          local:"泸州-江阳区"
        },{
          id:8,
          job:"Java中级开发工程师",
          money:"8千-1.5万",
          year:"3-5年",
          edu:"本科",
          company:"东华软件股份有限公司",
          local:"泸州-江阳区"
        }
      ],
      // 热门企业图片
      company: [
        {
          id: 0,
          idView:require("@/assets/images/portal/recruit/弘创集团.png")
        },{
          id: 1,
          idView:require("@/assets/images/portal/recruit/shutou_logo.png")
        },{
          id: 2,
          idView:require("@/assets/images/portal/recruit/chengtou_logo.png")
        },{
          id: 3,
          idView:require("@/assets/images/portal/recruit/shudao_logo.png")
        },{
          id: 4,
          idView:require("@/assets/images/portal/recruit/lutianhua_logo.png")
        },{
          id: 5,
          idView:require("@/assets/images/portal/recruit/kaiyue_logo.png")
        },{
          id: 6,
          idView:require("@/assets/images/portal/recruit/funeng_logo.png")
        },{
          id: 7,
          idView:require("@/assets/images/portal/recruit/良友股份.png")
        },{
          id: 8,
          idView:require("@/assets/images/portal/recruit/三人炫.png")
        },{
          id: 9,
          idView:require("@/assets/images/portal/recruit/buchang_logo.png")
        },{
          id: 10,
          idView:require("@/assets/images/portal/recruit/赛扬传媒.png")
        },{
          id: 11,
          idView:require("@/assets/images/portal/recruit/锐力鑫达.png")
        }
      ],
      // 求职名片
      person: [
        {
          id: 0,
          idView:require("@/assets/images/portal/recruit/head_1.png"),
          name: "罗香",
          sex: "女",
          age: "29",
          complete: "60%",
          educational:"本科",
          year: "4年以上",
          job: "技术支持/维护、测试工程师",
          location: "江阳区、龙马潭区"
        }, {
          id: 1,
          idView:require("@/assets/images/portal/recruit/head_2.png"),
          name: "罗香",
          sex: "女",
          age: "29",
          complete: "60%",
          educational:"本科",
          year: "4年以上",
          job: "技术支持/维护、测试工程师",
          location: "江阳区、龙马潭区"
        }, {
          id: 2,
          idView:require("@/assets/images/portal/recruit/head_3.png"),
          name: "罗香",
          sex: "女",
          age: "29",
          complete: "60%",
          educational:"本科",
          year: "4年以上",
          job: "技术支持/维护、测试工程师",
          location: "江阳区、龙马潭区"
        }, {
          id: 3,
          idView:require("@/assets/images/portal/recruit/head_4.png"),
          name: "罗香",
          sex: "女",
          age: "29",
          complete: "60%",
          educational:"本科",
          year: "4年以上",
          job: "技术支持/维护、测试工程师",
          location: "江阳区、龙马潭区"
        }, {
          id: 4,
          idView:require("@/assets/images/portal/recruit/head_3.png"),
          name: "罗香",
          sex: "女",
          age: "29",
          complete: "60%",
          educational:"本科",
          year: "4年以上",
          job: "技术支持/维护、测试工程师",
          location: "江阳区、龙马潭区"
        }, {
          id: 5,
          idView:require("@/assets/images/portal/recruit/head_5.png"),
          name: "罗香",
          sex: "女",
          age: "29",
          complete: "60%",
          educational:"本科",
          year: "4年以上",
          job: "技术支持/维护、测试工程师",
          location: "江阳区、龙马潭区"
        }, {
          id: 6,
          idView:require("@/assets/images/portal/recruit/head_2.png"),
          name: "罗香",
          sex: "女",
          age: "29",
          complete: "60%",
          educational:"本科",
          year: "4年以上",
          job: "技术支持/维护、测试工程师",
          location: "江阳区、龙马潭区"
        }, {
          id: 7,
          idView:require("@/assets/images/portal/recruit/head_5.png"),
          name: "罗香",
          sex: "女",
          age: "29",
          complete: "60%",
          educational:"本科",
          year: "4年以上",
          job: "技术支持/维护、测试工程师",
          location: "江阳区、龙马潭区"
        }, {
          id: 8,
          idView:require("@/assets/images/portal/recruit/head_4.png"),
          name: "罗香",
          sex: "女",
          age: "29",
          complete: "60%",
          educational:"本科",
          year: "4年以上",
          job: "技术支持/维护、测试工程师",
          location: "江阳区、龙马潭区"
        }
      ],
    }
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
}
</script>

<style scoped>


* {
  margin: 0;
  padding: 0;
}
.view {
  width: 75px;
  height: 14px;
  font-size: 14px;
  font-family: PingFang SC, PingFang SC-Medium;
  font-weight: 500;
  text-align: center;
  color: #666666;
  display: block;
}
.change {
  width: 75px;
  height: 14px;
  font-size: 14px;
  font-family: PingFang SC, PingFang SC-Medium;
  font-weight: 500;
  text-align: center;
  color: #666666;
  cursor: pointer;
  /*display: block;*/
}
.el-icon-refresh {
  margin-right: 5px;
}
#tzgg_more {
  position: absolute;
  right: 20px;
  top: 15px;
  z-index: 999;
}
#gzxx_more {
  position: absolute;
  right: 40px;
  top: -60px;
}
#jyzd_more {
  position: absolute;
  right: 0px;
  top: -60px;
}
#rmzp_more {
  position: absolute;
  right: 40px;
  top: -35px;
}
#zxzw_more {
  position: absolute;
  right: 10px;
}
#zxzw_change {
  position: absolute;
  right: 110px;
}
#qzmp_more {
  position: absolute;
  right: 20px;
  top: -65px;
}
#qzmp_change {
  position: absolute;
  right: 110px;
  top: -65px;
}
#rmqy_more {
  position: absolute;
  right: 30px;
  top: -70px;
}
#rmqy_change {
  position: absolute;
  right: 120px;
  top: -70px;
}
.recruit_body{
  width: 100%;
  height: 100%;
  background: linear-gradient(to bottom, rgba( 19,120,223, 0.08) 0%, rgba(255,255,255,0.00) 100%);
}
>>> .el-carousel__arrow {
  display: none;
  border-radius: 6px;
  width: 30px;
  height: 30px;
}
/*顶部搜索栏*/
.head_search {
  width: 1182px;
  height: 100px;
  margin: auto;
  padding-top: 21px;
  border-radius: 0;
  /*border: 1px solid black;*/
}
.top_search {
  width: 1035px;
  height: 60px;
  font-size: 1.125rem;
  border: none;
}
.top_search  >>> .el-input__inner {
  height: 60px;
  border-radius: 10px 0 0 10px;
}
.search_button {
  width: 144px;
  height: 60px;
  cursor: pointer;
  background-color: #0256ca;
  border: none;
  border-radius: 0 10px 10px 0;
  font-size: 18px;
  font-family: PingFang SC, PingFang SC-Bold;
  font-weight: 500;
  text-align: center;
  color: #ffffff;
  letter-spacing: 4px;
}
.search_tips {
  width: 500px;
  height: 20px;
  font-size: 16px;
  font-family: PingFang SC, PingFang SC-Medium;

  font-weight: 500;
  text-align: center;
  color: #666666;
  margin-top: 10px;
}
.search_a {
  color: #666666;
  margin-left: 10px;
}

.home_fs{
  font-size: 14px;
  font-family: PingFang SC, PingFang SC-Medium;
  font-weight: 500;
  text-align: left;
  color: #212121;
}
/*顶部大板块*/
.top {
  width: 74.375rem;
  height: 25.625rem;
  /*border: black 1px solid;*/
  margin: auto;
  margin-top: 33px;
  display: flex;
}
.zp_right {
  width: 300px;
  height: 410px;
  /*border: 1px solid black;*/
}
.zp_tab {
  height: 42px;
  background: #ffffff;
  text-align: center;
  border-radius: 0 4px 4px 0;
  border-bottom: 2px solid #0256ca;
}
/*::v-deep*/
.zp_tab >>> .el-tabs__item.is-active {
  color: #ffffff;
  background-color: #0256ca;
  border-radius: 4px 4px 0 0 ;
}
/*::v-deep */
.zp_tab >>>.el-tabs__nav {
  display: flex;
  justify-content: space-between;
  width: 100%;
  text-align: center;
}
.zp_tab >>> .el-tabs__active-bar {
  height: 0px;
}
.zp_tab >>> .el-tabs__item {
  padding: 0;
  width: 33%;
  text-align: center;
}
.rt_fl{
  height: 2.6rem;
  background: #ffffff;
  padding-left: 0.81rem;
}
.zp_tab_down {
  width: 300px;
  height: 35px;
  background: #ffffff;
  border-radius: 0 4px 4px 0;
}
.zp_tab_down::before {
  content: "";
  display: block;
  margin: auto;
  width: 263px;
  height: 1px;
  opacity: 0.05;
  background: #000000;
}
.top_mid {
  width: 620px;
  height: 410px;
  /*border: 1px solid black;*/
  margin-left: 10px;
}
.top_l {
  width: 100%;
  height: 100%;
}
.top_l >>> .el-carousel__container {
  width: 620px;
  height: 410px;
  border-radius: 0px;
}
.top_l >>> .el-carousel__button {
  border-radius: 100px;
  width: 8px;
  height: 8px;
  background: rgba(0,0,0,0.2);
}
.top_l >>> .is-active  .el-carousel__button {
  width: 16px;
  height: 8px;
  background: #597efc;
  border-radius: 4px;
}
.top_l_img {
  width: 100%;
  height: 100%;
}
.top_right {
  width: 260px;
  height: 410px;
  /*border: 1px solid black;*/
  margin-left: 10px;
}
.top_login {
  width: 250px;
  height: 250px;
  background: #ffffff;
  border-radius: 10px;
  box-shadow: 0px 3px 8px 0px rgba(0,0,0,0.04);
  margin-left: 5px;
  position: relative;
}
.top_login >>> .el-tabs__header {
  width: 180px;
  height: 40px;
  /*border: 1px solid black;*/
  margin-top: 16px;
  margin-left: 18px;
}
.top_login >>> .el-tabs__active-bar {
  background-color: #0256ca;
  width: 60px !important;
  height: 4px;
  border-radius: 2px;
}
.top_login >>> .el-tabs__item {
  width: 80px;
  height: 37px;
  font-size: 15px;
  font-family: PingFang SC, PingFang SC-Medium;
  font-weight: 500;
  text-align: center;
  color: #333333;
}
.top_login >>> .el-tabs__nav-wrap::after {
  display: none;
}

.login_img {
  width: 40px;
  height: 39px;
  position: absolute;
  right: 16px;
  top: 16px;
  border: 1px dashed #000000;
}
.top_phone {
  width: 224px;
  height: 37px;
  /*border: 1px solid #cccccc;*/
  border-radius: 6px;

}
.top_code {
  width: 224px;
  height: 37px;
  /*border: 1px solid #cccccc;*/
  border-radius: 6px 0 0 6px;
  display: flex;
  margin-top: 16px;
  margin-left: 14px;
}
.top_phone_code >>>  el-input {
  border-radius: 0;
}
.top_phone_code {
  width: 134px;
  height: 37px;
  border-radius: 6px 0 0 6px;
}
.login_code_button {
  width: 90px;
  height: 37px;
  background: #0256ca;
  border-radius: 0 6px 6px 0;
  cursor: pointer;
  border: none;
  font-size: 14px;
  font-family: PingFang SC, PingFang SC-Medium;
  font-weight: 500;
  text-align: center;
  color: #ffffff;
}
.login_button {
  width: 223px;
  height: 36px;
  background: #0256ca;
  border-radius: 6px;
  cursor: pointer;
  border: none;font-size: 14px;
  font-family: PingFang SC, PingFang SC-Medium;
  font-weight: 500;
  text-align: center;
  color: #ffffff;
  margin: auto;
  margin-top: 15px;
}
.forget {
  display: block;
  width: 56px;
  height: 14px;
  font-size: 14px;
  font-family: PingFang SC, PingFang SC-Medium;
  font-weight: 500;
  text-align: center;
  color: #666666;
  margin-top: 5px;
  margin-left: 32px;
}
.top_message {
  box-sizing: border-box;
  overflow: hidden;
  width: 260px;
  height: 160px;
  background: url("~@/assets/images/portal/recruit/背景@2x.png");
  background-size:100% 100%;
  background-repeat:no-repeat;
  margin-top: 8px;
  position: relative;
  /*border: 1px black solid;*/
}
.msg_img {
  width: 54px;
  height: 54px;
  position: absolute;
  right: 44px;
  top: 17px;
}
.msg_font {
  width: 200px;
  height: 20px;
  font-size: 14px;
  font-family: PingFang SC, PingFang SC-Medium;
  font-weight: 500;
  text-align: left;
  color: #ffffff;
}
#msgf1 {
  margin-top: 20px;
  margin-left: 19px;
}
#msgf2 {
  margin-top: 10px;
  margin-left: 25px;
}
#msgf3 {
  margin-top: 10px;
  margin-left: 19px;
}
.msg_btn {
  width: 112px;
  height: 28px;
  background: #ffad19;
  border-radius: 6px;
  font-size: 14px;
  font-family: PingFang SC, PingFang SC-Medium;
  font-weight: 500;
  text-align: center;
  color: #ffffff;
  border: none;
  cursor: pointer;
  margin-top: 8px;
}

/*热门招聘*/
.rmzp_logo {
  background-image: url("~@/assets/images/portal/recruit/热门招聘标题@3x.png");
}
.hot_recruit {
  width: 1240px;
  height: 243px;
  /*border: 1px black solid;*/
  margin: auto;
  margin-top: 20px;
  position: relative;
}
.recruitment {
  /*border: black solid 1px;*/
  width: 100%;
  height: 243px;
}
.recruit_l {
  width: 77.5rem;
  height: 243px;
}
.recruitment >>> .el-carousel__container {
  height: 243px;
}
.recruit_card {
  width: 75rem;
  height: 243px;
  /*border: black solid 1px;*/
  margin: auto;
  display: flex;
  align-content: space-between;
  justify-content: space-between;
}
.recruit_c {
  width: 280px;
  height: 243px;
  background: #ffffff;
  /*border-radius: 10px;*/
  /*border: 1px solid red;*/
  box-shadow: 0px 3px 8px 0px rgba(0,0,0,0.04);
  position: relative;
}
.recruit_state {
  width: 40px;
  height: 20px;
  font-size: 14px;
  font-family: PingFang SC, PingFang SC-Regular;
  font-weight: 400;
  text-align: left;
  color: #ffffff;
  line-height: 27px;
  letter-spacing: 2.8px;
  position: absolute;
  left: 8px;
  top: 8px;
  z-index: 5;
}
.recruit_i {
  width: 289px;
  height: 166px;
  position: absolute;
  left: -10px;
  /*z-index: -1;*/
}
.recruit_font {
  width: 230px;
  height: 14px;
  font-size: 14px;
  font-family: PingFang SC, PingFang SC-Medium;
  font-weight: 500;
  text-align: left;
  color: #333333;
  position: absolute;
  left: 15px;
  bottom: 50px;
}
.recruit_b {
  width: 104px;
  height: 27px;
  border: 1px solid #0256ca;
  /*border-radius: 6px;*/
  background-color: #ffffff;
  font-size: 14px;
  font-family: PingFang SC, PingFang SC-Bold;
  font-weight: 500;
  color: #0256ca;
  /*text-align: center;*/
  /*display: flex;*/
  margin-top: 20px;
  margin-left: 20px;
  cursor: pointer;
  position: absolute;
  bottom: 10px;
  left: -5px;
}
.recruit_b:hover {
  color: #ffffff;
  background-color: #0256ca;
}

/*通知公告*/
.tzgg_logo{
  background-image: url("~@/assets/images/portal/recruit/通知公告标题@3x.png");
}

.announcement {
  width: 1199px;
  height: 384px;
  background: #ffffff;
  /*border-radius: 10px;*/
  box-shadow: 0px 3px 8px 0px rgba(0,0,0,0.04);
  /*border: 1px black solid;*/
  margin: 10px auto auto;
  display: flex;
}
.announcement_left {
  /*border: 1px black solid;*/
  width: 47%;
  height: 100%;
  margin-top: 30px;
}
.announcement_left_i {
  width: 485px;
  height: 228px;
}
.announcement_left_title {
  width: 330px;
  height: 18px;
  font-size: 14px;
  font-family: PingFang SC, PingFang SC-Bold;
  font-weight: 700;
  text-align: left;
  color: #333333;
  margin-top: 10px;
  margin-left: 45px;
}
.announcement_left_content {
  width: 462px;
  height: 36px;
  font-size: 14px;
  font-family: PingFang SC, PingFang SC-Medium;
  font-weight: 500;
  text-align: left;
  color: #333333;
  line-height: 21px;
  margin-top: 12px;
  margin-left: 45px;
}
.announcement_left_time {
  /*width: 80px;*/
  /*height: 18px;*/
  font-size: 14px;
  font-family: PingFang SC, PingFang SC-Medium;
  font-weight: 500;
  text-align: left;
  color: #666666;
  margin-top: 14px;
  margin-left: 45px;
}
.announcement_right {
  width: 50%;
  height: 100%;
  position: relative;
  margin-top: 15px;
}

.el-tabs {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.el-tabs >>>  .el-tabs__content {
  flex: 1;
}
.announcement_right >>> .el-tab-pane {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  /*border: red 1px solid;*/
  width: 100%;
  height: 100%;
  /*height: 320px;*/
}
.announcement_right >>> .el-tabs__nav-wrap::after {
  display: none;
}
.announcement_right >>> .el-tabs__item {
  width: 80px;
  height: 37px;
  font-size: 15px;
  font-family: PingFang SC, PingFang SC-Medium;
  font-weight: 500;
  text-align: center;
  color: #333333;
}
.announcement_right >>> .el-tabs__active-bar {
  background-color: #0256ca;
  width: 60px !important;
  height: 4px;
  border-radius: 2px;
}
.notice {
  width: 100%;
  height: 100%;
  /*border: 1px solid black;*/
  display: flex;
  align-content: space-between;
  justify-content: stretch;
  /*margin-top: 15px;*/
}
.dot {
  width: 6px;
  height: 6px;
  border-radius: 100%;
  background: #0256ca;
  margin-top: 8px;
}
.notice_font {
  width: 420px;
  height: 20px;
  font-size: 14px;
  font-family: PingFang SC, PingFang SC-Medium;
  font-weight: 500;
  text-align: left;
  color: #333333;
  margin-left: 16px;
  text-overflow: ellipsis;
}
.notice_time {
  /*width: 80px;*/
  /*height: 20px;*/
  font-size: 14px;
  font-family: PingFang SC, PingFang SC-Medium;
  font-weight: 500;
  text-align: center;
  color: #333333;
  margin-left: 50px;
}

/*公招信息*/
.gzxx_logo{
  background-image: url("~@/assets/images/portal/recruit/公招信息标题@3x.png");
}
.recruit {
  width: 100%;
  height: 1.875rem;
  font-size: 18px;
  font-family: PingFang SC, PingFang SC-Bold;
  font-weight: 700;
  text-align: center;
  color: #333333;
  margin-bottom: 1.25rem;
  position: relative;
}
.admit {
  width: 100%;
  height: 1.875rem;
  font-size: 18px;
  font-family: PingFang SC, PingFang SC-Bold;
  font-weight: 700;
  text-align: center;
  color: #333333;
  margin-top: 2rem;
  margin-bottom: 1.25rem;
}
.information {
  width: 77.5rem;
  height: 16.25rem;
  /*border: 1px black solid;*/
  margin: 1rem auto auto;
  position: relative;
}
.public {
  /*border: black solid 1px;*/
  width: 100%;
  height: 13.125rem;
}
.public >>> .el-carousel__container{
  height: 13.125rem;
}
.public_l {
  width: 77.5rem;
  height: 13.125rem;
}
.public_card {
  width: 75rem;
  height: 12.875rem;
  /*border: black solid 1px;*/
  margin: auto;
  display: flex;
  align-content: space-between;
  justify-content: space-between;
}
.public_c {
  /*border: black solid 1px;*/
  width: 23.9rem;
  height: 12.875rem;
  /*border-radius: 10px;*/
  box-shadow: 0 0 10px #808080;

}
.public_i {
  width: 24rem;
  height: 8.125rem;
}
.public_font {
  width: 20.9375rem;
  height: 0.875rem;
  font-size: 0.875rem;
  font-family: PingFang SC, PingFang SC-Medium;
  font-weight: 500;
  text-align: left;
  color: #333333;
  margin-left: 0.9375rem;
  margin-top: 0.625rem;
  text-overflow: ellipsis;
}

/*就业指导*/
.jyzd_logo{
  background-image: url("~@/assets/images/portal/recruit/就业指导标题@3x.png");
}
.guide {
  width: 75rem;
  height: 19.3712rem;
  /*border: 1px black solid;*/
  margin: 1rem auto auto;
  display: flex;
  justify-content: space-between;
  align-content: space-between;
  flex-wrap: wrap;
  position: relative;
}
.guide_c {
  width: 23.9rem;
  height: 8.625rem;
  /*border: #000000 1px solid;*/
  /*border-radius: 10px;*/
  display: flex;
  position: relative;

}
.guide_back {
  display: flex;
  position: absolute;
  z-index: -1;
  width: 100%;
  height: 100%;
}
.guide_left {
  height: 100%;
  width: 12.5rem;
  /*border: 1px solid black;*/
  margin-left: 1.4375rem;
}
.guide_right {
  height: 100%;
  width: 8.125rem;
  /*border: 1px solid black;*/
}
.guide_title {
  width: 13.75rem;
  height: 1.0625rem;
  font-size: 16px;
  font-family: PingFang SC, PingFang SC-Bold;
  font-weight: 700;
  text-align: left;
  color: #ffffff;
  margin-top: 2rem;
}
.guide_font {
  width: 6rem;
  height: 0.75rem;
  font-size: 14px;
  font-family: PingFang SC, PingFang SC-Medium;
  font-weight: 500;
  text-align: left;
  color: #ffffff;
  margin-top: 0.9375rem;
}
.guide_button {
  width: 6.875rem;
  height: 1.875rem;
  color: #ffffff;
  font-size: 14px;
  border: none;
  /*border-radius: 6px;*/
  /*background: rgba(0 ,0 ,0 ,0.2);*/
  margin-top: 4.6875rem;
  margin-right: 6.25rem;
  z-index: 2;
  cursor: pointer;
}
.guide_buttom {
  display: flex;
}
.guide_icon {
  width: 1rem;
  height: 0.75rem;
  margin-top: 1.25rem;
  margin-right: 0.3125rem;
}

/*最新职位*/
.zxzw_logo{
  background-image: url("~@/assets/images/portal/recruit/最新职位标题@3x.png");
}
.job {
  width: 75rem;
  height: 35.25rem;
  /*border: 1px black solid;*/
  margin: auto;
  margin-top: 1.5625rem;
  position: relative;
  /*display: flex;*/
  /*flex: 1;*/
  /*justify-content: space-between;*/
  /*align-content: space-between;*/
  /*flex-wrap: wrap;*/
}
.job >>> .el-tab-pane {
  width: 75rem;
  height: 29.5rem;
  margin-top: 0.9375rem;
  display: flex;
  flex: 1;
  justify-content: space-between;
  align-content: space-between;
  flex-wrap: wrap;
}
.job >>> .el-tabs__item {
  width: 7.5rem;
  height: 2.1875rem;
  font-size: 1.0625rem;
  font-family: PingFang SC, PingFang SC-Medium;
  font-weight: 500;
  padding-top: 0.3125rem;
  padding-right: 0.3125rem;
  padding-left: 0.3125rem;
  text-align: center;
  color: #333333;
  line-height: 26px;
  background: #f9f9f9;
}
.job >>> .is-active{
  width: 7.5rem;
  height: 2.5rem;
  color: #ffffff;
  background: #0256ca;
}
.job >>> .el-tabs__active-bar {
  display: none;
}
.job >>> .el-tabs__nav-wrap::after {
  background-color: #0256ca;
}
.job_c {
  width: 23.9rem;
  height: 8.625rem;
  /*border: #ffffff 1px solid;*/
  /*border-radius: 10px;*/
  box-shadow: 0px 3px 11.31px 0.39px rgba(0,0,0,0.08);
  /*display: flex;*/
  background-color: white;
}
.job_top {
  width: 100%;
  height: 27%;
  /*border: black 1px solid;*/
  display: flex;
}
.job_mid {
  width: 100%;
  height: 39%;
  /*border: black 1px solid;*/
  display: flex;
}
.job_bottom {
  width: 23.9rem;
  height: 35%;
  /*border: black 1px solid;*/
  background-color: #0256ca;
  /*border-radius: 0 0 10px 10px;*/
  /*position: absolute;*/
  display: flex;
}
.job_job {
  width: 11.25rem;
  height: 1.875rem;
  /*border: black 1px solid;*/
  margin-left: 0.8125rem;
  margin-top: 0.625rem;
  text-align: left;
  font-size: 16px;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  color: #333333;
}
.job_money {
  width: 6.25rem;
  height: 1.875rem;
  /*border: black 1px solid;*/
  margin-top: 0.625rem;
  margin-left: 3.125rem;
  text-align: right;
  font-size: 20px;
  font-weight: 700;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  color: #F33131;
}
.job_tag {
  width: 3.75rem;
  height: 1.4375rem;
  background-color: #F8F8F8;
  margin-left: 1.0625rem;
  margin-top: 0.3125rem;
  border-radius: 5px;
}
.job_btn {
  width: 6.875rem;
  height: 1.875rem;
  color: #0256ca;
  background-color: #ffffff;
  border-radius: 5px;
  border: 1px solid #0256ca;
  cursor: pointer;
  margin-left: 5rem;
  margin-top: 0.625rem;
}
.job_btn:hover{
  color: #ffffff;
  background-color: #0256ca;
}
.job_company {
  width: 12.5rem;
  height: 80%;
  color: #ffffff;
  margin-top: 0.9375rem;
  margin-left: 1.25rem;
  text-align: left;
}
.job_local {
  width: 6.25rem;
  height: 80%;
  color: #ffffff;
  margin-top: 0.9375rem;
  margin-left: 2.1875rem;
}
/*求职名片*/
.qzmp_logo{
  background-image: url("~@/assets/images/portal/recruit/求职名片标题@3x.png");
}
.card {
  width: 75rem;
  height: 32.7462rem;
  /*border: 1px black solid;*/
  margin: 1rem auto auto;
  display: flex;
  flex: 1;
  justify-content: space-between;
  align-content: space-between;
  flex-wrap: wrap;
  position: relative;
}
.card_c {
  width: 23.9rem;
  height: 9.875rem;
  border: #ffffff 1px solid;
  /*border-radius: 10px;*/
  box-shadow: 0 0 10px #808080;
  display: flex;
}
.card_left{
  width: 8.125rem;
  height: 100%;
  /*border: 1px black solid;*/
}
.card_right{
  width: 15.875rem;
  height: 100%;
  margin-left: 0.3125rem;
  /*border: 1px black solid;*/
}
.card_top {
  margin-top: 1rem;
  display: flex;
  align-items: center;
}
.card_bottom {
  width: 100%;
  height: 1.25rem;
  display: flex;
}

.card_head {
  width: 6.75rem;
  height: 6.75rem;
  margin-top: 1.3125rem;
  /*border: black 1px solid;*/
}
.card_name {
  width: 2.125rem;
  height: 1.25rem;
  /*border: black 1px solid;*/
  text-align: left;
  color: #333333;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  font-size: 14px;
  font-weight: bold;
}
.card_complete {
  width: 5.625rem;
  height: 1.25rem;
  /*border: black 1px solid;*/
  margin-left: 4rem;
}
.card_message {
  width: 12.5rem;
  height: 1.25rem;
  margin-top: 1rem;
  /*border: black 1px solid;*/
  text-align: left;
  color: #333333;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  font-size: 12px;
}
.card_job {
  width: 12.5rem;
  height: 1.25rem;
  margin-top: 0.9375rem;
  /*border: black 1px solid;*/
  text-align: left;
  color: #333333;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  font-size: 12px;
}
.card_local{
  width: 0.75rem;
  height: 0.9375rem;
  margin-top: 0.8125rem;
  /*border: black 1px solid;*/
}
.card_location{
  width: 12.5rem;
  height: 1.25rem;
  margin-top: 0.625rem;
  margin-left: 0.3125rem;
  /*border: black 1px solid;*/
  text-align: left;
  font-size: 12px;
  color: #666666;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
}

/*热门企业*/
.rmqy_logo{
  background-image: url("~@/assets/images/portal/recruit/热门企业标题@3x.png");
}
.company {
  width: 75.125rem;
  height: 25.3125rem;
  /*border: 1px black solid;*/
  margin: 1rem auto 3.125rem;
  display: flex;
  flex: 1;
  /*justify-content: space-between;*/
  /*align-content: space-between;*/
  position: relative;
  flex-wrap: wrap;
}
.company_c {
  width: 18.75rem;
  height: 8.4375rem;
  border: 1px solid #E5E5E5;
  position: relative;
  cursor: pointer;
}
.company_i {
  width: 16.875rem;
  height: 4.375rem;
  /*border: 1px solid blue;*/
  position: absolute;
  top: 0.9375rem;
  left: 0.9375rem;
}
.company_w {
  width: 100%;
  height: 1.125rem;
  /*border: 1px solid green;*/
  position: absolute;
  bottom: 0.9375rem;
  text-align: center;
}

/*标签统一样式*/
.home_title{
  /*width: 75rem;*/
  /*margin: 1.75rem auto 0 auto;*/
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-pack: justify;
  -ms-flex-pack: justify;
  justify-content: space-between;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;

}
.title-h3{
  color: #1658a0;
  font-size: 24px;
  font-weight: 600;
  line-height: 1.4;
  cursor: pointer;
}
.title-span{
  height: 40px;
  background-color: #fff;
  border-radius: 3px;
  border: 1px solid #e6e6e6;
  color: #333;
  font-size: 16px;
  font-weight: 500;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  padding: 0 20px 0 25px;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  cursor: pointer;
}

</style>
