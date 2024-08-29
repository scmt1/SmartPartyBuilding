<template>
    <div class="container1" v-loading="showArr.every(val => val === true) ? false : true">
        <!--<div class="con-title">
            <div>
                <img style="height: 49px;width: 50px" :src="conTitle">
            </div>
            <div class="con-title-text">党务看板</div>
        </div>-->
        <div class="con-top" style="padding: 15px;">
            <div class="con-top-row">
                <div class="con-top-row-item">
                    <div class="con-top-row-content">
                        <div class="con-top-row-content-label">
                            <div class="con-top-row-label">党员数量</div>
                        </div>
                        <div class="con-top-row-content-bottom">
                            <div>
                                <img :src="partyCount">
                            </div>
                            <div style="margin-left: 12px">
                                <div class="con-top-row-content-text">{{partyTotal}}</div>
                            </div>
                            <div style="font-size: 18px;margin-left: 5px;font-weight: 700;letter-spacing: 0px;color: rgba(255, 255, 255, 1);">人</div>
                        </div>
                    </div>
                </div>
                <div class="con-top-row-item">
                    <div class="con-top-row-content"
                         style="background: linear-gradient(90deg, rgba(255, 121, 54, 1) 0%, rgba(255, 155, 79, 1) 100%);">
                        <div class="con-top-row-content-label">
                            <div class="con-top-row-label">党组织数量</div>
                        </div>
                        <div class="con-top-row-content-bottom">
                            <div>
                                <img :src="orgCount">
                            </div>
                            <div style="margin-left: 12px">
                                <div class="con-top-row-content-text">{{totalChildren}}</div>
                            </div>
                            <div style="font-size: 18px;margin-left: 5px;font-weight: 700;letter-spacing: 0px;color: rgba(255, 255, 255, 1);">个</div>
                        </div>
                    </div>
                </div>
                <div class="con-top-row-item">
                    <div class="con-top-row-content" style="background: linear-gradient(90deg, rgba(232, 42, 42, 1) 0%, rgba(224, 74, 74, 1) 100%);">
                        <div class="con-top-row-content-label">
                            <div class="con-top-row-label">三会一课开展次数(本月)</div>
                        </div>
                        <div class="con-top-row-content-bottom">
                            <div>
                                <img :src="partyCount">
                            </div>
                            <div style="margin-left: 12px">
                                <div class="con-top-row-content-text">{{meetingClass}}</div>
                            </div>
                            <div style="font-size: 18px;margin-left: 5px;font-weight: 700;letter-spacing: 0px;color: rgba(255, 255, 255, 1);">次</div>
                        </div>
                    </div>
                </div>
                <div class="con-top-row-item">
                    <div class="con-top-row-content"
                         style="background: linear-gradient(90deg, rgba(255, 121, 54, 1) 0%, rgba(255, 155, 79, 1) 100%);">
                        <div class="con-top-row-content-label">
                            <div class="con-top-row-label">组织生活开展次数(本年)</div>
                        </div>
                        <div class="con-top-row-content-bottom">
                            <div>
                                <img :src="partyCount">
                            </div>
                            <div style="margin-left: 12px">
                                <div class="con-top-row-content-text">{{orgLifeConut}}</div>
                            </div>
                            <div style="font-size: 18px;margin-left: 5px;font-weight: 700;letter-spacing: 0px;color: rgba(255, 255, 255, 1);">次</div>
                        </div>
                    </div>
                </div>
                <div class="con-top-row-item">
                    <div class="con-top-row-content" style="background: linear-gradient(90deg, rgba(232, 42, 42, 1) 0%, rgba(224, 74, 74, 1) 100%);">
                        <div class="con-top-row-content-label">
                            <div class="con-top-row-label">党费缴纳(本月)</div>
                        </div>
                        <div class="con-top-row-content-bottom">
                            <div>
                                <img :src="partyCount">
                            </div>
                            <div style="margin-left: 12px">
                                <div class="con-top-row-content-text">{{payCount}}</div>
                            </div>
                            <div style="font-size: 18px;margin-left: 5px;font-weight: 700;letter-spacing: 0px;color: rgba(255, 255, 255, 1);">人</div>
                        </div>
                    </div>
                </div>

            </div>
        </div>

        <div class="con-second" style="width: 100%; padding: 15px;">
            <div class="con-second-left">
                <div style="text-align: left;">
                    <div class="con-second-left-title">党员数据统计</div>
                </div>
                <div class="con-second-left-row">
                    <div class="con-second-left-row-left">
                        <div ref="chart" style="height:366px;"></div>
                    </div>
                    <div class="con-second-left-row-right">
                        <div ref="chart2" style="height:366px;"></div>
                    </div>
                </div>
                <div class="con-second-left-row">
                    <div class="con-second-left-row-left">
                        <div ref="chart3" style="height:366px;"></div>
                    </div>
                    <div class="con-second-left-row-right">
                        <div ref="chart4" style="height:366px;"></div>
                    </div>
                </div>
            </div>
            <div class="con-second-right">
                <div style="text-align: left;">
                    <div class="con-second-left-title">党组织换届数据</div>
                </div>
                <div class="con-second-right-row">
                    <div class="con-second-right-con">
                        <img :src="font" style="margin-top: 53px">
                        <div class="con-second-right-con-font">{{changedNum+0}}个</div>
                        <div class="con-second-right-con-row" style="  margin-top: 41px;">
                            <div class="con-child">
                                <div style="display: flex">
                                    <div class="con-child-font">已换届党委数</div>
                                    <div class="con-child-font">{{changedDangweiNum+0}}</div>
                                </div>
                            </div>
                            <div class="con-child">
                                <div style="display: flex">
                                    <div class="con-child-font">已换届党总支数</div>
                                    <div class="con-child-font">{{changedDangzongNum+0}}个</div>
                                </div>
                            </div>
                        </div>
                        <div class="con-second-right-con-row" style="margin-top: 26px">
                            <div class="con-child">
                                <div style="display: flex">
                                    <div class="con-child-font">已换届党支部数</div>
                                    <div class="con-child-font">{{changedDangzhiNum+0}}个</div>
                                </div>
                            </div>
                            <div class="con-child">
                                <div style="display: flex">
                                    <div class="con-child-font">已换届联合党支部数</div>
                                    <div class="con-child-font">{{changedUnitdangzhi+0}}个</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="con-second-right-con" style="background: rgba(255, 245, 237, 1);margin-top: 20px">
                        <img :src="font2" style="margin-top: 53px">
                        <div class="con-second-right-con-font" style="color: rgba(255, 111, 38, 1);">{{unChangedNum+0}}个</div>
                        <div class="con-second-right-con-row" style="  margin-top: 41px;">
                            <div class="con-child" style="background: linear-gradient(90deg, rgba(254, 133, 74, 1) 0%, rgba(255, 163, 92, 1) 100%);">
                                <div style="display: flex">
                                    <div class="con-child-font">未换届党委数</div>
                                    <div class="con-child-font">{{unChangedDangweiNum+0}}个</div>
                                </div>
                            </div>
                            <div class="con-child" style="background: linear-gradient(90deg, rgba(254, 133, 74, 1) 0%, rgba(255, 163, 92, 1) 100%);">
                                <div style="display: flex">
                                    <div class="con-child-font">未换届党总支数</div>
                                    <div class="con-child-font">{{unChangedDangzongNum+0}}个</div>
                                </div>
                            </div>
                        </div>
                        <div class="con-second-right-con-row" style="margin-top: 26px">
                            <div class="con-child" style="background: linear-gradient(90deg, rgba(254, 133, 74, 1) 0%, rgba(255, 163, 92, 1) 100%);">
                                <div style="display: flex">
                                    <div class="con-child-font">未换届党支部数</div>
                                    <div class="con-child-font">{{unChangedDangzhiNum+0}}个</div>
                                </div>
                            </div>
                            <div class="con-child" style="background: linear-gradient(90deg, rgba(254, 133, 74, 1) 0%, rgba(255, 163, 92, 1) 100%);">
                                <div style="display: flex">
                                    <div class="con-child-font">未换届联合党支部数</div>
                                    <div class="con-child-font">{{unChangedUnitdangzhi}}个</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="con-third" style="padding: 15px;">
            <div class="con-third-left">
                <div style="text-align: left;">
                    <div class="con-third-left-title">基层党组织数据</div>
                </div>
                <div class="con-third-left-con">
                    <div ref="chart5" style="height:366px;"></div>
                </div>
            </div>
            <div class="con-third-right">
                <div style="text-align: left;">
                    <div class="con-third-right-title">党费缴纳率(近一年)</div>
                </div>
                <div class="con-third-right-con">
                    <div ref="chart6" style="height:366px;"></div>
                </div>
            </div>
        </div>
        <div class="con-third" style="padding: 15px;">
            <div class="con-third-left" style="width: 100%;">
                <div style="text-align: left;">
                    <div class="con-third-left-title">三会一课开展数据(近一年)</div>
                </div>
                <div class="con-third-left-con">
                    <div ref="chart7" style="height:366px;"></div>
                </div>
            </div>
        </div>
    </div>

</template>

<script>
    import {
        queryTzSysDeptList
    } from "@/api/tzSysDept";
    import {queryMeetingClass} from "@/api/tzOrganizationLife";
    import {getInfo} from "@/api/partyMember";
    import conTitle from "@/assets/home/conTitle.png"
    import partyCount from "@/assets/home/partyCount.png"
    import orgCount from "@/assets/home/orgCount.png"
    import font from "@/assets/home/font.png"
    import font2 from "@/assets/home/font2.png"
    import {queryFeeTotalByMonth} from "@/api/tzPayFee";
    import {queryTzTermDeptList} from "@/api/tzTermDept";
    import {getBaseInfoById} from "@/api/tzSysDept";
    export default {
        name: "Home",
        props: {
            msg: String,
        },
        computed: {
            isAllTrue: function() {
                return this.showArr.every(val => val === true);
            }
        },

        data() {
            return {
                dataListLoading:true,
                man:0,
                woMan:0,
                flux:0,
                unFlux:0,
                totalChildren:0,
                partyTotal:0,
                deptId:'',
                font: font,
                font2: font2,
                conTitle: conTitle,
                partyCount: partyCount,
                orgCount: orgCount,
                nameColor: "orange",
                ages: [],
                partyAgeData: [],
                yearMonthArr: [],
                meetingClass:0,
                orgLifeConut:0,
                payCount:0,
                changedNum:0,
                changedDangweiNum:0,
                changedDangzongNum:0,
                changedDangzhiNum:0,
                changedUnitdangzhi:0,
                unChangedNum:0,
                unChangedDangweiNum:0,
                unChangedDangzongNum:0,
                unChangedDangzhiNum:0,
                unChangedUnitdangzhi:0,
                dangweiNum:0,
                dangzongNum:0,
                dangzhiNum:0,
                unitdangzhi:0,
                timedangzhi:0,
                timeunitdangzhi:0,
                totalBase:0,
                classList:[],
                showArr:[false,false,false,false,false,false]
            };
        },
        mounted() {
            let userInformation = JSON.parse(window.localStorage.getItem("userInformation"))
            this.deptId=userInformation.deptId
            const now = new Date()
            const currentYear = now.getFullYear()
            const currentMonth = now.getMonth() + 1
            const yearMonthArr = []
            for (let i = 0; i < 12; i++) {
                const test = currentMonth - i + 1;
                const tmpMonth = currentMonth - i
                const month = tmpMonth <= 0 ? tmpMonth + 12 : tmpMonth
                let year = currentYear - Math.floor((month - 1) / 12)
                if (month > test) {
                    year = year - 1
                }
                yearMonthArr.push(year.toString() + '—' + month.toString())
            }
            this.yearMonthArr = yearMonthArr
            this.getTzSysDeptList()
            this.queryFeeTotalByMonth()
            this.getPartyMembertData()
            this.getClassCount('1')
            this.getClassCount('2')
            this.queryTzTermDeptList('2')
            this.getDeptCount()
            this.$nextTick(() => {
                this.initEcharts6()
            })
        },
        methods: {
            getDeptCount () {
                let that = this
                getBaseInfoById(this.deptId).then(res => {
                    if(res.data.code=='00000'){
                        var tmp = res.data.data
                        var dangweiNum = 0
                        var dangzongNum = 0
                        var dangzhiNum = 0
                        var unitdangzhi = 0
                        var timedangzhi = 0
                        var timeunitdangzhi = 0

                        if (tmp) {
                            tmp.forEach(i => {
                                if (i.type === 611) {
                                    dangweiNum = dangweiNum + 1
                                } else if (i.type === 621) {
                                    dangzongNum = dangzongNum + 1
                                } else if (i.type === 631) {
                                    dangzhiNum = dangzhiNum + 1
                                } else if (i.type === 632) {
                                    unitdangzhi = unitdangzhi + 1
                                } else if (i.type === 931) {
                                    timedangzhi = timedangzhi + 1
                                } else if (i.type === 932) {
                                    timeunitdangzhi = timeunitdangzhi + 1
                                }
                            })
                            this.dangweiNum = dangweiNum
                            this.dangzongNum = dangzongNum
                            this.dangzhiNum = dangzhiNum
                            this.unitdangzhi = unitdangzhi
                            this.timedangzhi = timedangzhi
                            this.timeunitdangzhi = timeunitdangzhi
                            this.totalBase = dangweiNum + dangzongNum + dangzhiNum + unitdangzhi
                        }
                    }


                    this.$nextTick(()=>{
                        this.initEcharts5()
                    })
                })
            },
            // 获取分页信息
            queryTzTermDeptList (type) {
                let data = {
                    tzTermDept:{deptId:this.deptId},
                    type:type
                }
                // type根据权限配置，type为1是只查询自己的历史记录，type为2是查询所有组织的当前换届信息
                queryTzTermDeptList({data: data}).then(i => {
                    if(i.data.code=='00000'){
                        let res = i.data
                        this.changedNum = res.data.changedNum
                        this.unChangedNum = res.data.unChangedNum
                        this.deptAllNum = this.changedNum + this.unChangedNum
                        this.changedDangweiNum = res.data.changedDangweiNum
                        this.changedDangzongNum = res.data.changedDangzongNum
                        this.changedDangzhiNum = res.data.changedDangzhiNum
                        this.changedUnitdangzhi = res.data.changedUnitdangzhi
                        this.unChangedDangweiNum = res.data.unChangedDangweiNum
                        this.unChangedDangzongNum = res.data.unChangedDangzongNum
                        this.unChangedDangzhiNum = res.data.unChangedDangzhiNum
                        this.unChangedUnitdangzhi = res.data.unChangedUnitdangzhi
                    }
                   this.showArr[5]=true
                    this.$forceUpdate()
                }).catch(e => {
                    this.showArr[5]=true
                })
            },
            queryFeeTotalByMonth() {
                let date = this.formartDate(new Date(),'yyyy-MM')
                let data = {
                    deptId:this.deptId,
                    month:date
                }
                queryFeeTotalByMonth({data: data}).then(res => {
                    this.tableDataNow = []
                    if (res.data.code == "00000") {
                        this.payCount = res.data.data
                    }
                    this.showArr[1] = true
                    this.$forceUpdate()
                }).finally(i => {
                    this.showArr[1] = true
                })
            },
            getClassCount(type) {
                let data = {
                    meetingType:type,
                    deptId:this.deptId
                }
                queryMeetingClass({data: data}).then((res) => {
                    if(type=='1'){
                        this.showArr[3] = true
                        this.$forceUpdate()

                        let tmp = null
                        if(res.data.code='00000'){
                            tmp=res.data.data[0]
                        }
                        this.classList.push(tmp.january)
                        this.classList.push(tmp.february)
                        this.classList.push(tmp.march)
                        this.classList.push(tmp.april)
                        this.classList.push(tmp.may)
                        this.classList.push(tmp.june)
                        this.classList.push(tmp.july)
                        this.classList.push(tmp.august)
                        this.classList.push(tmp.september)
                        this.classList.push(tmp.october)
                        this.classList.push(tmp.november)
                        this.classList.push(tmp.december)

                        let now = this.formartDate(new Date(),'MM')
                        if(now == '01'){
                            this.meetingClass = tmp.january
                        }
                        if(now == '02'){
                            this.meetingClass = tmp.february
                        }
                        if(now == '03'){
                            this.meetingClass = tmp.march
                        }
                        if(now == '04'){
                            this.meetingClass = tmp.april
                        }
                        if(now == '05'){
                            this.meetingClass = tmp.may
                        }
                        if(now == '06'){
                            this.meetingClass = tmp.june
                        }
                        if(now == '07'){
                            this.meetingClass = tmp.july
                        }
                        if(now == '08'){
                            this.meetingClass = tmp.august
                        }
                        if(now == '09'){
                            this.meetingClass = tmp.september
                        }
                        if(now == '10'){
                            this.meetingClass = tmp.october
                        }
                        if(now == '11'){
                            this.meetingClass = tmp.november
                        }
                        if(now == '12'){
                            this.meetingClass = tmp.december
                        }
                    }else if(type=='2'){
                        this.showArr[4] = true
                        this.$forceUpdate()

                        let tmp = null
                        if(res.data.code='00000'){
                            tmp=res.data.data[0].oneYear
                        }
                        this.orgLifeConut = tmp
                    }
                    this.$nextTick(()=>{
                        this.initEcharts7()
                    })
                }).catch(e => {
                    if(type=='1'){
                        this.showArr[3] = true
                        this.$forceUpdate()
                    }else if(type=='2'){
                        this.showArr[4] = true
                    }
                })
            },
            getTzSysDeptList() {
                let tzSysDept = {
                    id:this.deptId
                }
                let data = {
                    tzSysDept:tzSysDept
                }
                queryTzSysDeptList({data: data}).then(res => {
                    this.showArr[0] = true

                    const dataRes = res.data.data
                    this.totalChildren = res.data.data.total 

                    this.$forceUpdate()
                }).catch(e => {
                    this.showArr[0] = true
                })
            },
            getPartyMembertData() {
                getInfo(this.deptId + '').then(res => {
                    const data = res.data
                    if (data.code == '00000') {
                        this.man = data.data.man
                        this.woMan = data.data.woMan

                        this.partyTotal = data.data.total
                        this.partyAgeData = data.data.partyAges
                        this.ages = data.data.ages


                        this.unKnownSex = data.data.unKnownSex
                        this.flux = data.data.flux
                        this.unFlux = data.data.unFlux

                        this.pre = data.data.pre
                        this.poverty = data.data.poverty
                        this.unPoverty = data.data.unPoverty
                        this.unLost = data.data.unLost
                        this.lost = data.data.lost
                        this.position = data.data.position
                        this.$nextTick(() => {
                            this.initEcharts1()
                            this.initEcharts2()
                            this.initEcharts3()
                            this.initEcharts4()
                        })
                    }
                    this.showArr[2] = true
                    this.$forceUpdate()
                })
            },
            initEcharts1() {
                const chart = this.$refs.chart
                if (chart) {
                    const myChart = this.$echarts.init(chart, null, {renderer: 'svg'})
                    // const myChart = this.$echarts.init(chart)
                    const option = {
                        title: {
                            text: '性别',
                            x: 10,
                            y: 20
                        },
                        tooltip: {
                            trigger: 'item'
                        },
                        legend: {
                            data: ['男', '女'],
                            orient: 'vertical',
                            textStyle: {  // 图列内容样式
                                color: '#000',  // 字体颜色
                            },
                            x: 20,//图例位置，设置right发现图例和文字位置反了，设置一个数值就好了
                            y: 60//延Y轴居中
                        },
                        color: ['#5470c6', '#fc3e36'],
                        series: [
                            /*   {
                                   type: 'pie',
                                   data: [
                                       {
                                           value: this.man,
                                           name: '男'
                                       },
                                       {
                                           value: this.woMan,
                                           name: '女'
                                       },

                                   ],
                                   label: {
                                       //echarts饼图内部显示百分比设置
                                       show: true,
                                       position: 'outside', //outside 外部显示  inside 内部显示
                                       formatter: `{d}%`,
                                       color: '#000000', //颜色
                                       fontSize: 15 //字体大小
                                   },
                                   center: ['50%', '70%'],
                                   radius: this.precent
                               }
   */
                            {
                                name: '性别',
                                type: 'pie',
                                radius: ['40%', '60%'],
                                center: ['55%', '50%'],
                                avoidLabelOverlap: false,
                                label: {
                                    //echarts饼图内部显示百分比设置
                                    show: true,
                                    textBorderColor: '#5470c6',
                                    textBorderWidth: 2,
                                    position: 'inside', //outside 外部显示  inside 内部显示
                                    formatter: `{d}%`,
                                    color: '#ffffff', //颜色
                                    fontSize: 15 //字体大小
                                },
                                emphasis: {
                                    label: {
                                        show: true,
                                        fontSize: 20,
                                        fontWeight: 'bold'
                                    }
                                },
                                labelLine: {
                                    show: false
                                },
                                data: [
                                    {value: this.man, name: '男'},
                                    {value: this.woMan, name: '女'},
                                ]
                            }
                        ]
                    }
                    myChart.setOption(option)
                    window.addEventListener('resize', function () {
                        myChart.resize()
                    })
                }
                this.$on('hook:destroyed', () => {
                    window.removeEventListener('resize', function () {
                        myChart.resize()
                    })
                })
            },
            initEcharts2() {
                const chart = this.$refs.chart2
                if (chart) {
                    const myChart = this.$echarts.init(chart, null, {renderer: 'svg'})
                    // const myChart = this.$echarts.init(chart)
                    const option = {
                        title: {
                            text: '年龄分布',
                            x: 10,
                            y: 20
                        },
                        tooltip: {
                            trigger: 'item'
                        },
                        xAxis: {
                            type: 'category',
                            data: ['35岁及以下', '36-40岁', '41-45岁', '46-50岁', '51-54岁', '55岁及以上'],
                            interval: 0,
                            axisLabel: {
                                formatter:function(value)  //X轴的内容
                                {
                                    let ret = '' //拼接加\n返回的类目项
                                    let max = 3  //每行显示的文字字数
                                    let val = value.length  //X轴内容的文字字数
                                    let rowN = Math.ceil(val / max)  //需要换的行数
                                    if(rowN > 1)  //判断 如果字数大于2就换行
                                    {
                                        for(let i = 0; i<rowN; i++){
                                            let temp = ''  //每次截取的字符串
                                            let start = i * max  //开始截取的位置
                                            let end = start + max  //结束截取的位置
                                            temp = value.substring(start,end)+ "\n"
                                            ret += temp  //最终的字符串
                                        }
                                        return ret
                                    }
                                    else {return value}
                                }
                            }
                        },
                        yAxis: {},
                        grid: {
                            containLabel: true,
                            left: '10%', // 添加 left 属性
                            right: '10%', // 添加 right 属性
                            top: '25%' // 添加 right 属性
                        },
                        series: [
                            {
                                type: 'bar',
                                data: this.ages,
                                grid: {
                                    y: 101,
                                    x2: 10,
                                    height: '100%'
                                },
                                barWidth: '40%',
                                itemStyle: {
                                    color: 'rgba(254, 135, 75, 1)'
                                }
                            }
                        ]
                    }
                    myChart.setOption(option)
                    window.addEventListener('resize', function () {
                        myChart.resize()
                    })
                }
                this.$on('hook:destroyed', () => {
                    window.removeEventListener('resize', function () {
                        myChart.resize()
                    })
                })
            },
            initEcharts3() {
                const chart = this.$refs.chart3
                if (chart) {
                    const myChart = this.$echarts.init(chart, null, {renderer: 'svg'})
                    // const myChart = this.$echarts.init(chart)
                    const option = {
                        title: {
                            text: '是否流动党员',
                            x: 10,
                            y: 20
                        },
                        tooltip: {
                            trigger: 'item'
                        },
                        legend: {
                            data: ['流动党员', '非流动党员'],
                            orient: 'vertical',
                            textStyle: {  // 图列内容样式
                                color: '#000',  // 字体颜色
                            },
                            x: 20,//图例位置，设置right发现图例和文字位置反了，设置一个数值就好了
                            y: 60//延Y轴居中
                        },
                        color: ['#c67840', 'rgb(204,174,22)'],
                        series: [
                            {
                                name: '是否流动党员',
                                type: 'pie',
                                avoidLabelOverlap: false,
                                radius: '60%',
                                center: ['55%', '50%'],
                                label: {
                                    //echarts饼图内部显示百分比设置
                                    show: true,
                                    textBorderColor: '#c67840',
                                    textBorderWidth: 2,
                                    position: 'inside', //outside 外部显示  inside 内部显示
                                    formatter: `{d}%`,
                                    color: '#ffffff', //颜色
                                    fontSize: 15 //字体大小
                                },
                                emphasis: {
                                    label: {
                                        show: true,
                                        fontSize: 20,
                                        fontWeight: 'bold'
                                    }
                                },
                                labelLine: {
                                    show: false
                                },
                                data: [
                                    {value: this.flux, name: '流动党员'},
                                    {value: this.unFlux, name: '非流动党员'},
                                ]
                            }
                        ]
                    }
                    myChart.setOption(option)
                    window.addEventListener('resize', function () {
                        myChart.resize()
                    })
                }
                this.$on('hook:destroyed', () => {
                    window.removeEventListener('resize', function () {
                        myChart.resize()
                    })
                })
            },
            initEcharts4() {
                const chart = this.$refs.chart4
                if (chart) {
                    const myChart = this.$echarts.init(chart, null, {renderer: 'svg'})
                    // const myChart = this.$echarts.init(chart)
                    const option = {
                        title: {
                            text: '党龄分布',
                            x: 10,
                            y: 20
                        },
                        tooltip: {
                            trigger: 'item'
                        },
                        xAxis: {
                            type: 'category',
                            data: ['35岁及以下', '36-40岁', '41-45岁', '46-50岁', '51-54岁', '55岁及以上'],
                            interval: 0,
                            axisLabel: {
                                formatter:function(value)  //X轴的内容
                                {
                                    let ret = '' //拼接加\n返回的类目项
                                    let max = 3  //每行显示的文字字数
                                    let val = value.length  //X轴内容的文字字数
                                    let rowN = Math.ceil(val / max)  //需要换的行数
                                    if(rowN > 1)  //判断 如果字数大于2就换行
                                    {
                                        for(let i = 0; i<rowN; i++){
                                            let temp = ''  //每次截取的字符串
                                            let start = i * max  //开始截取的位置
                                            let end = start + max  //结束截取的位置
                                            temp = value.substring(start,end)+ "\n"
                                            ret += temp  //最终的字符串
                                        }
                                        return ret
                                    }
                                    else {return value}
                                }
                            }
                        },
                        grid: {
                            containLabel: true,
                            left: '10%', // 添加 left 属性
                            right: '10%', // 添加 right 属性
                            top: '25%' // 添加 right 属性
                        },
                        yAxis: {},
                        series: [
                            {
                                type: 'bar',
                                data: this.partyAgeData,
                                grid: {
                                    y: 101,
                                    x2: 10,
                                    height: '100%'
                                },
                                barWidth: '40%',
                                itemStyle: {
                                    color: 'rgba(254, 135, 75, 1)'
                                }
                            }
                        ]
                    }
                    myChart.setOption(option)
                    window.addEventListener('resize', function () {
                        myChart.resize()
                    })
                }
                this.$on('hook:destroyed', () => {
                    window.removeEventListener('resize', function () {
                        myChart.resize()
                    })
                })
            },
            initEcharts5() {
                const chart = this.$refs.chart5
                if (chart) {
                    const myChart = this.$echarts.init(chart, null, {renderer: 'svg'})
                    // const myChart = this.$echarts.init(chart)
                    const option = {
                        title: {
                            text: '基层党组织数：' + this.totalBase+'个',
                            textStyle:{
                                fontSize: 20, // 标题字体大小
                                color: '#e04b45' // 标题颜色
                            },
                            x: 10,
                            y: 20
                        },
                        tooltip: {
                            trigger: 'item'
                        },
                        legend: {
                            data: ['党支部总数', '党委总数','党总支总数','联合党支部总数'],
                            orient: 'vertical',
                            textStyle: {  // 图列内容样式
                                color: '#000',  // 字体颜色
                            },
                            x: 20,//图例位置，设置right发现图例和文字位置反了，设置一个数值就好了
                            y: 60//延Y轴居中
                        },
                        color: ['#fd383a', '#fea142','#3097da','#546ef0'],
                        series: [
                            /*   {
                                   type: 'pie',
                                   data: [
                                       {
                                           value: this.man,
                                           name: '男'
                                       },
                                       {
                                           value: this.woMan,
                                           name: '女'
                                       },

                                   ],
                                   label: {
                                       //echarts饼图内部显示百分比设置
                                       show: true,
                                       position: 'outside', //outside 外部显示  inside 内部显示
                                       formatter: `{d}%`,
                                       color: '#000000', //颜色
                                       fontSize: 15 //字体大小
                                   },
                                   center: ['50%', '70%'],
                                   radius: this.precent
                               }
   */
                            {
                                name: '基层党组织数据',
                                type: 'pie',
                                radius: ['40%', '70%'],
                                center: ['65%', '50%'],
                                avoidLabelOverlap: false,
                                label: {
                                    //echarts饼图内部显示百分比设置
                                    show: true,
                                    position: 'outside', //outside 外部显示  inside 内部显示
                                    formatter: `{d}%`,
                                    color: '#000000', //颜色
                                    fontSize: 15 //字体大小
                                },
                                emphasis: {
                                    label: {
                                        show: true,
                                        fontSize: 20,
                                        fontWeight: 'bold'
                                    }
                                },
                                labelLine: {
                                    show: false
                                },
                                data: [
                                    {value: this.dangzhiNum, name: '党支部总数'},
                                    {value: this.dangweiNum, name: '党委总数'},
                                    {value: this.dangzongNum, name: '党总支总数'},
                                    {value: this.unitdangzhi, name: '联合党支部总数'},
                                ]
                            }
                        ]
                    }
                    myChart.setOption(option)
                    window.addEventListener('resize', function () {
                        myChart.resize()
                    })
                }
                this.$on('hook:destroyed', () => {
                    window.removeEventListener('resize', function () {
                        myChart.resize()
                    })
                })
            },
            initEcharts6() {
                const chart = this.$refs.chart6
                if (chart) {
                    const myChart = this.$echarts.init(chart, null, {renderer: 'svg'})
                    const option = {
                        tooltip: {
                            trigger: 'item'
                        },

                        xAxis: {
                            type: 'category',
                            data: this.yearMonthArr
                        },
                        yAxis: {
                            type: 'value',
                            axisLabel: {
                                show: true,
                                interval: 'auto',
                                formatter: '{value} %'//纵坐标百分比
                            }
                        },
                        grid: {
                            left: 60,
                            top: 80,
                            right: 20,
                            bottom: 40
                        },
                        series: [
                            {
                                data: [1,3,1,5,8,9,8,7,8,9],
                                type: 'line',
                                center: ['50%', '20%'],
                                grid: {
                                    y: 101,
                                    x2: 10,
                                    height: '80%'
                                },
                                symbol: 'circle', //折线点设置为实心点
                                symbolSize: 6, //折线点的大小
                                itemStyle: {
                                    normal: {
                                        color: "#fe884c", //折线点的颜色
                                        lineStyle: {
                                            color: "#fe884c" //折线的颜色
                                        }
                                    }
                                },
                            },

                        ]
                    };
                    myChart.setOption(option)
                    window.addEventListener('resize', function () {
                        myChart.resize()
                    })
                }
                this.$on('hook:destroyed', () => {
                    window.removeEventListener('resize', function () {
                        myChart.resize()
                    })
                })
            },
            initEcharts7() {
                const chart = this.$refs.chart7
                if (chart) {
                    const myChart = this.$echarts.init(chart, null, {renderer: 'svg'})
                    const option = {
                        tooltip: {
                            trigger: 'item'
                        },
                        xAxis: {
                            type: 'category',
                            boundaryGap: false,
                            data: this.yearMonthArr
                        },
                        yAxis: {
                            type: 'value',

                        },
                        grid: {
                            left: 60,
                            top: 80,
                            right: 40,
                            bottom: 40
                        },
                        series: [
                            {
                                data: this.classList,
                                type: 'line',
                                areaStyle: {
                                    color: "#fd373a"
                                },
                                itemStyle: {
                                    normal: {
                                        color: "#fe884c", //折线点的颜色
                                        lineStyle: {
                                            color: "#9dd8fe" //折线的颜色
                                        }
                                    }
                                },
                            },

                        ]
                    };
                    myChart.setOption(option)
                    window.addEventListener('resize', function () {
                        myChart.resize()
                    })
                }
                this.$on('hook:destroyed', () => {
                    window.removeEventListener('resize', function () {
                        myChart.resize()
                    })
                })
            },
            formartDate(date, fmt) {
                if (date == undefined || date == null || date.toString().trim().length <= 0) {
                    return ''
                }
                if (typeof date === 'string') {
                    date = new Date(date)
                }
                date = date == undefined ? new Date() : date
                date = typeof date == 'number' ? new Date(date) : date
                fmt = fmt || 'yyyy-MM-dd HH:mm:ss'
                let obj = {
                    'y': date.getFullYear(), // 年份，注意必须用getFullYear
                    'M': date.getMonth() + 1, // 月份，注意是从0-11
                    'd': date.getDate(), // 日期
                    'q': Math.floor((date.getMonth() + 3) / 3), // 季度
                    'w': date.getDay(), // 星期，注意是0-6
                    'H': date.getHours(), // 24小时制
                    'h': date.getHours() % 12 == 0 ? 12 : date.getHours() % 12, // 12小时制
                    'm': date.getMinutes(), // 分钟
                    's': date.getSeconds(), // 秒
                    'S': date.getMilliseconds() // 毫秒
                }
                let week = ['天', '一', '二', '三', '四', '五', '六']
                for (let i in obj) {
                    fmt = fmt.replace(new RegExp(i + '+', 'g'), function (m) {
                        let val = obj[i] + ''
                        if (i == 'w') return (m.length > 2 ? '星期' : '周') + week[val]
                        for (var j = 0, len = val.length; j < m.length - len; j++) val = '0' + val
                        return m.length == 1 ? val : val.substring(val.length - m.length)
                    })
                }
                return fmt
            },
        },
    };
</script>

<style lang="scss" scoped>
    @import url("//unpkg.com/element-ui@2.15.7/lib/theme-chalk/index.css");

    .line {
        left: 50px;
        top: 393px;
        width: 144px;
        height: 4px;
        opacity: 1;
        background: rgba(224, 75, 69, 1);
    }

    .container1 {
        min-width: 1350px;
        overflow: auto;
        padding: 15px;
        margin: 0 auto;
        background-color: white;
        border-radius: 5px;

        .con-title {
            display: flex;
            text-align: center;
            align-items: center;
            justify-content: center;

            .con-title-text {
                margin-left: 10px;
                font-size: 40px;
                font-weight: 700;
                letter-spacing: 0px;
                line-height: 52.79px;
                color: rgba(232, 42, 42, 1);
            }
        }

        .con-top {
            .con-top-row {
                display: flex;
                flex-direction: row;
                justify-content: space-between;

                .con-top-row-item {
                    width: calc(20% - 10px);
                    word-break: break-all;

                    .con-top-row-content {
                        display: flex;
                        flex-direction: column;
                        padding: 15px;
                        width: 100%;
                        height: 100%;
                        // width: 332px;
                        //height: 138px;
                        opacity: 1;
                        border-radius: 10px;
                        background: linear-gradient(90deg, rgba(232, 42, 42, 1) 0%, rgba(224, 74, 74, 1) 100%);
                        flex-grow: 1;
                        flex-shrink: 0;
                        flex-basis: 0;
                        margin-right: 10px;

                        .con-top-row-content-label {
                            text-align: left;
                            /*padding-top: 24px;
                            padding-left: 20px;*/

                            .con-top-row-label {
                                font-size: 24px;
                                font-weight: 700;
                                letter-spacing: 0px;
                                line-height: 31.68px;
                                color: rgba(255, 255, 255, 1);
                            }
                        }

                        .con-top-row-content-bottom {
                            display: flex;
                            flex: 1;
                            margin-top: 11px;
                            align-items: flex-end;

                            .con-top-row-content-text {
                                font-size: 26px;
                                font-weight: 700;
                                letter-spacing: 0px;
                                line-height: 34.32px;
                                color: rgba(255, 255, 255, 1);
                            }
                        }
                    }
                }
            }
        }

        .con-second {
            display: flex;
            margin-top: 57px;

            .con-second-left-title {
                display: inline-block;
                border-bottom: 4px solid rgba(224, 75, 69, 1);
                font-size: 24px;
                font-weight: 400;
                letter-spacing: 0px;
                line-height: 31.68px;
                color: rgba(51, 51, 51, 1);
            }

            .con-second-left {
                //width: 60%;
                flex: 1;
                min-width: 600px;
                text-align: left;

                .con-second-left-row {
                    margin-top: 20px;
                    display: flex;

                    .con-second-left-row-left {
                        /*overflow-x: auto;*/
                        /*overflow-y: hidden;*/
                        /*min-width: 300px;*/
                        width: 45%;
                        height: 358px;
                        border-radius: 10px;
                        background: rgba(255, 255, 255, 1);
                        box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.1);
                    }

                    .con-second-left-row-right {
                        margin-left: 20px;
                        /*min-width: 300px;*/
                        width: calc(55% - 35px);
                        height: 358px;
                        border-radius: 10px;
                        background: rgba(255, 255, 255, 1);
                        box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.1);
                    }
                }
            }

            .con-second-right {
                .con-second-right-row {
                    margin-top: 20px;
                    width: 100%;
                    height: 735.66px;
                    opacity: 1;
                    border-radius: 10px;
                    background: rgba(255, 255, 255, 1);
                    box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.1);
                    padding: 20px;

                    .con-second-right-con {
                        min-width: 677px;
                        height: 338px;
                        opacity: 1;
                        border-radius: 10px;
                        background: rgba(255, 240, 240, 1);

                        .con-second-right-con-font {
                            font-size: 24px;
                            font-weight: 700;
                            letter-spacing: 0px;
                            line-height: 31.68px;
                            color: rgba(237, 58, 59, 1);
                            vertical-align: top;
                            text-align: center;
                            margin-top: 10px;
                        }

                        .con-second-right-con-row {
                            display: flex;

                            .con-child {
                                display: flex;
                                align-items: center;
                                margin: auto;
                                width: 290px;
                                height: 48px;
                                opacity: 1;
                                border-radius: 4px;
                                background: linear-gradient(90deg, rgba(240, 54, 57, 1) 0%, rgba(219, 87, 75, 1) 100%);

                                .con-child-font {
                                    padding: 0 10px;
                                    font-size: 16px;
                                    font-weight: 700;
                                    letter-spacing: 0px;
                                    line-height: 21.12px;
                                    color: rgba(255, 255, 255, 1);
                                    text-align: left;
                                    vertical-align: top;
                                    overflow: hidden;
                                    text-overflow: ellipsis;
                                    display: -webkit-box;
                                    -webkit-line-clamp: 2;
                                    -webkit-box-orient: vertical;
                                    word-break: break-all;
                                }
                            }
                        }
                    }
                }
            }
        }

        .con-third {
            display: flex;
            margin-top: 57px;

            .con-third-left {
                width: 40%;

                .con-third-left-title {
                    display: inline-block;
                    border-bottom: 4px solid rgba(224, 75, 69, 1);
                    font-size: 24px;
                    font-weight: 400;
                    letter-spacing: 0px;
                    line-height: 31.68px;
                    color: rgba(51, 51, 51, 1);
                    text-align: left;
                }

                .con-third-left-con {
                    margin-top: 20px;
                    /*width: 45%;*/
                    height: 358px;
                    opacity: 1;
                    border-radius: 10px;
                    background: rgba(255, 255, 255, 1);
                    box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.1);
                }
            }

            .con-third-right {
                width: 60%;
                margin-left: 20px;
                .con-third-right-title {
                    display: inline-block;
                    border-bottom: 4px solid rgba(224, 75, 69, 1);
                    font-size: 24px;
                    font-weight: 400;
                    letter-spacing: 0px;
                    line-height: 31.68px;
                    color: rgba(51, 51, 51, 1);
                    text-align: left;
                }

                .con-third-right-con {
                    margin-top: 20px;
                    height: 358px;
                    opacity: 1;
                    border-radius: 10px;
                    background: rgba(255, 255, 255, 1);
                    box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.1);
                }
            }
        }
    }
</style>


