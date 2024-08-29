<style lang="scss">
  @import "./home/home.scss";
</style>

<template>
  <div>
    <div v-show="currNav == 'xboot'" class="home">
      <Row :gutter="10">
        <Col :sm="24" :md="12" :lg="6" :style="{ marginBottom: '10px' }">
          <card1
                  id-name="card1"
                  prefix="￥"
                  :end-val="count.data1"
                  description="今日销售额"
                  :image="require('@/assets/icon/money.png')"
                  width="34px"
                  height="34px"
          />
        </Col>
        <Col :sm="24" :md="12" :lg="6" :style="{ marginBottom: '10px' }">
          <card2
                  id-name="card2"
                  suffix="%"
                  :end-val="count.data2"
                  color="#f90"
                  title="销售量增长"
                  description="相比昨日"
          />
        </Col>
        <Col :sm="24" :md="12" :lg="6" :style="{ marginBottom: '10px' }">
          <card4
                  title="待办事项"
                  :time="time"
                  description="您有一个新的待审批任务，请前往查看"
          />
        </Col>
        <Col :sm="24" :md="12" :lg="6" :style="{ marginBottom: '10px' }">
          <card3
                  id-name="card3"
                  title="应用中心"
                  description="销量统计，用户统计，日活分析"
                  :image="require('@/assets/icon/app.png')"
                  width="34px"
                  height="34px"
                  titleSize="18px"
          />
        </Col>
      </Row>
      <Row :gutter="10">
        <Col :sm="24" :md="12" :lg="4" :style="{ marginBottom: '10px' }">
          <cardApp icon="logo-buffer" title="SaaS应用"/>
        </Col>
        <Col :sm="24" :md="12" :lg="4" :style="{ marginBottom: '10px' }">
          <cardApp activeColor="#2db7f5" icon="md-bookmarks" title="日志分析"/>
        </Col>
        <Col :sm="24" :md="12" :lg="4" :style="{ marginBottom: '10px' }">
          <cardApp activeColor="#19be6b" icon="md-cloud" title="云运维"/>
        </Col>
        <Col :sm="24" :md="12" :lg="4" :style="{ marginBottom: '10px' }">
          <cardApp activeColor="#f90" icon="md-film" title="视频监控"/>
        </Col>
        <Col :sm="24" :md="12" :lg="4" :style="{ marginBottom: '10px' }">
          <cardApp activeColor="#8950fc" icon="md-stats" title="数据分析"
          />
        </Col>
        <Col :sm="24" :md="12" :lg="4" :style="{ marginBottom: '10px' }">
          <cardApp activeColor="#ed4014" icon="md-people" title="用户分析"
          />
        </Col>
      </Row>
      <Row :gutter="10">
        <Col :sm="24" :md="12" :lg="6" :style="{ marginBottom: '10px' }">
          <card1
                  id-name="card5"
                  :bordered="false"
                  :end-val="126778"
                  description="今日新增互动数"
                  backgroundColor="#fff4df"
                  :image="require('@/assets/icon/comment.png')"
                  width="34px"
                  height="34px"
          />
        </Col>
        <Col :sm="24" :md="12" :lg="6" :style="{ marginBottom: '10px' }">
          <card1
                  id-name="card6"
                  :bordered="false"
                  :end-val="68893"
                  backgroundColor="#6993fe"
                  titleColor="#fff"
                  icon="md-person-add"
                  iconColor="#fff"
                  descriptionColor="#fff"
                  description="今日新增用户"
          />
        </Col>
        <Col :sm="24" :md="12" :lg="6" :style="{ marginBottom: '10px' }">
          <card1
                  id-name="card7"
                  :bordered="false"
                  :end-val="count.data4"
                  backgroundColor="#8950fe"
                  titleColor="#fff"
                  icon="md-cloud-download"
                  iconColor="#fff"
                  descriptionColor="#fff"
                  description="今日下载量"
          />
        </Col>
        <Col :sm="24" :md="12" :lg="6" :style="{ marginBottom: '10px' }">
          <card1
                  id-name="card8"
                  :bordered="false"
                  :end-val="13507632434"
                  backgroundColor="#f64e61"
                  titleColor="#fff"
                  icon="md-calendar"
                  iconColor="#fff"
                  descriptionColor="#fff"
                  description="月活"
          />
        </Col>
      </Row>
      <Row :gutter="10">
        <Col :sm="24" :lg="24" :xl="16" :style="{ marginBottom: '10px' }">
          <visit-volume/>
        </Col>
        <Col :sm="24" :lg="24" :xl="8" :style="{ marginBottom: '10px' }">
          <visit-separation/>
        </Col>
      </Row>
    </div>
    <!--<div v-if="currNav == 'app'">
      <dashboard2/>
    </div>-->
  </div>
</template>

<script>
  // import {ipInfo} from "@/api/index";
  import visitVolume from "./home/components/visitVolume.vue";
  import visitSeparation from "./home/components/visitSeparation.vue";
  import card1 from "./home/components/card1.vue";
  import card2 from "./home/components/card2.vue";
  import card3 from "./home/components/card3.vue";
  import card4 from "./home/components/card4.vue";
  import cardApp from "./home/components/cardApp.vue";
  // import dashboard2 from "./xboot-charts/dashboard2/dashboard2.vue";
  import Cookies from "js-cookie";

  export default {
    name: "index",
    components: {
      visitVolume,
      visitSeparation,
      card1,
      card2,
      card3,
      card4,
      cardApp,
      // dashboard2,
    },
    data() {
      return {
        showVideo: false,
        count: {
          data1: 5396,
          data2: 68,
          data3: 19305,
          data4: 39503498,
        },
        city: "未知",
        departmentTitle: "无",
        userType: "无",
        time: "",
        currNav:'app'
      };
    },
    methods: {

    },
    mounted() {

    },
  };
</script>
