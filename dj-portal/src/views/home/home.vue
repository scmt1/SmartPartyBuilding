<template>
    <div class="main_content">
        <!--    轮播背景图-->
        <div class="header_lb_position">
            <el-carousel indicator-position="none">
                <el-carousel-item v-for="(item,index) in backgroundImage" :key="index">
                    <img :src="item" width="100%" height="100%">
                </el-carousel-item>
            </el-carousel>

            <div class="search" style="position: absolute; top: 150px;right: 180px; z-index: 9999;width: 300px;">
                <el-input placeholder="请输入内容" v-model="searchKey" size="medium" @change="gotoSearch">
                    <el-button slot="append" icon="el-icon-search" @click="gotoSearch"></el-button>
                </el-input>
            </div>


        </div>
        <div class="dh" osize="16">
            <div class="dh_box" osize="16">
                <ul class="pc" voice-area="nav" voice-arealabel="网站主页" osize="16">
                    <li class="active" osize="16">
                        <a class="activeTxt" href="/home" osize="20" @click="toPage(1)">首页</a>
                    </li>
                    <li osize="16">
                        <a href="/newsList?type=机关动态" osize="20" @click="toPage(2)">机关动态</a>
                    </li>
                    <li osize="16">
                        <a href="/newsList?type=专题专栏" osize="20" @click="toPage(3)">专题专栏</a>
                    </li>
                    <li osize="16">
                        <a href="/newsList?type=党建信息" osize="20" @click="toPage(4)">党建信息</a>
                    </li>
                    <li osize="16">
                        <a href="/newsList?type=先锋典型" osize="20" @click="toPage(5)">先锋典型</a>
                    </li>
                    <li osize="16">
                        <a href="/newsList?type=党建微视" osize="20" @click="toPage(6)">党建微视</a>
                    </li>
                </ul>
            </div>
        </div>

        <div class="center-content">
            <!--    轮播图片-->
            <div class="home_lunbo">
                <div class="lunbo_box">
                    <el-row style="width: 100%;" :gutter="20">
                        <el-col :span="12">
                            <div class="ywbt" osize="16">
                                <span osize="24">机关动态</span>
                                <a href="./newsList?type=机关动态" style="color: #57a3f3;" @click="toPage(2)" osize="16">[
                                    更多 ]</a>
                            </div>
                            <div class="ywbt_lb" v-if="jgdtList.length > 0">
                                <li osize="16" v-for="(item,index) in jgdtList" :key="index">
                                    <svg t="1692177055380" class="icon" viewBox="0 0 1024 1024" version="1.1"
                                         xmlns="http://www.w3.org/2000/svg" p-id="4222" width="30" height="13"
                                         tabindex="-1" osize="16">
                                        <path d="M480 480m-288 0a4.5 4.5 0 1 0 576 0 4.5 4.5 0 1 0-576 0Z" p-id="4223"
                                              fill="#d97971"
                                              tabindex="-1" osize="16"></path>
                                    </svg>
                                    <a :href="item.url" target="_blank" osize="16" role="link">{{ item.title }}</a>
                                </li>
                            </div>
                            <div v-else style="text-align: center;padding: 20px 0;font-size: 20px;font-weight: bold;">暂无数据</div>
                        </el-col>
                        <el-col :span="12">
                            <div class="ywbt" osize="16">
                                <span osize="24">专题专栏</span>
                                <a href="./newsList?type=专题专栏" style="color: #57a3f3;" @click="toPage(3)" osize="16">[
                                    更多 ]</a>
                            </div>
                            <div class="ywbt_lb" v-if="ztzlList.length > 0">
                                <li osize="16" v-for="(item,index) in ztzlList" :key="index">
                                    <svg t="1692177055380" class="icon" viewBox="0 0 1024 1024" version="1.1"
                                         xmlns="http://www.w3.org/2000/svg" p-id="4222" width="30" height="13"
                                         tabindex="-1" osize="16">
                                        <path d="M480 480m-288 0a4.5 4.5 0 1 0 576 0 4.5 4.5 0 1 0-576 0Z" p-id="4223"
                                              fill="#d97971"
                                              tabindex="-1" osize="16"></path>
                                    </svg>
                                    <a :href="item.url" target="_blank" osize="16" role="link">{{ item.title }}</a>
                                </li>
                            </div>
                            <div v-else style="text-align: center;padding: 20px 0;font-size: 20px;font-weight: bold;">暂无数据</div>
                        </el-col>
                    </el-row>
                </div>
            </div>
            <div class="width-content home_lb">
                <img src="../../assets/home/ban1.jpg" width="100%">
            </div>

            <div class="width-content home_lb">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <div class="ywbt" osize="16">
                            <span osize="24">党建信息</span>
                            <a href="./newsList?type=党建信息" style="color: #57a3f3;" @click="toPage(4)" osize="16">[ 更多
                                ]</a>
                        </div>
                        <div class="ywbt_lb" v-if="djxxList.length > 0">
                            <li osize="16" v-for="(item,index) in djxxList" :key="index">
                                <svg t="1692177055380" class="icon" viewBox="0 0 1024 1024" version="1.1"
                                     xmlns="http://www.w3.org/2000/svg" p-id="4222" width="30" height="13" tabindex="-1"
                                     osize="16">
                                    <path d="M480 480m-288 0a4.5 4.5 0 1 0 576 0 4.5 4.5 0 1 0-576 0Z" p-id="4223"
                                          fill="#d97971"
                                          tabindex="-1" osize="16"></path>
                                </svg>
                                <a :href="item.url" target="_blank" osize="16" role="link">{{ item.title }}</a>
                            </li>
                        </div>
                        <div v-else style="text-align: center;padding: 20px 0;font-size: 20px;font-weight: bold;">暂无数据</div>
                    </el-col>
                    <el-col :span="12">
                        <div class="ywbt" osize="16">
                            <span osize="24">先锋典型</span>
                            <a href="./newsList?type=先锋典型" style="color: #57a3f3;" @click="toPage(5)" osize="16">[ 更多
                                ]</a>
                        </div>
                        <div class="ywbt_lb" v-if="xfdxList.length >0">
                            <li osize="16" v-for="(item,index) in xfdxList" :key="index">
                                <svg t="1692177055380" class="icon" viewBox="0 0 1024 1024" version="1.1"
                                     xmlns="http://www.w3.org/2000/svg" p-id="4222" width="30" height="13" tabindex="-1"
                                     osize="16">
                                    <path d="M480 480m-288 0a4.5 4.5 0 1 0 576 0 4.5 4.5 0 1 0-576 0Z" p-id="4223"
                                          fill="#d97971"
                                          tabindex="-1" osize="16"></path>
                                </svg>
                                <a :href="item.url" target="_blank" osize="16" role="link">{{ item.title }}</a>
                            </li>
                        </div>
                        <div v-else style="text-align: center;padding: 20px 0;font-size: 20px;font-weight: bold;">暂无数据</div>
                    </el-col>
                </el-row>
            </div>
            <div class="width-content home_lb">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <div class="ywbt" osize="16">
                            <span osize="24">党建微视</span>
                            <a href="./newsList?type=党建微视" style="color: #57a3f3;" @click="toPage(6)" osize="16">[ 更多
                                ]</a>
                        </div>
                        <div class="ywbt_lb" v-if="djwsList.length > 0">
                            <li osize="16" v-for="(item,index) in djwsList" :key="index">
                                <svg t="1692177055380" class="icon" viewBox="0 0 1024 1024" version="1.1"
                                     xmlns="http://www.w3.org/2000/svg" p-id="4222" width="30" height="13" tabindex="-1"
                                     osize="16">
                                    <path d="M480 480m-288 0a4.5 4.5 0 1 0 576 0 4.5 4.5 0 1 0-576 0Z" p-id="4223"
                                          fill="#d97971"
                                          tabindex="-1" osize="16"></path>
                                </svg>
                                <a :href="item.url" target="_blank" osize="16" role="link">{{ item.title }}</a>
                            </li>
                        </div>
                        <div v-else style="text-align: center;padding: 20px 0;font-size: 20px;font-weight: bold;">暂无数据</div>
                    </el-col>
                </el-row>
            </div>
            <!--    轮播-->
            <!--      <div class="width-content home_lb">-->
            <!--        <div class="ywbt" osize="16">-->
            <!--          <span osize="24">党建微视</span>-->
            <!--          <a href="./newsList?type=党建微视" style="color: #57a3f3;" @click="toPage(5)" osize="16">[ 更多 ]</a>-->
            <!--        </div>-->
            <!--        <el-carousel indicator-position="outside" :autoplay="true" height="200px" style="margin-top: 15px;">-->
            <!--          <el-carousel-item>-->
            <!--            <div class="flex_ld">-->
            <!--              <div v-for="(item,index) in videoList" :key="index" class="lb_zq box_hover">-->
            <!--                <video :src="item.filePath" style="width: 300px;height: 200px;cursor: pointer;"-->
            <!--                       @click="toVideo(item.filePath)"></video>-->
            <!--              </div>-->
            <!--            </div>-->
            <!--          </el-carousel-item>-->
            <!--        </el-carousel>-->
            <!--      </div>-->

            <div class="footer" voice-area="service" voice-arealabel="服务区,当前为网页底部服务区，有网站建设维护联系方式、网站地图、便民电话等服务内容。"
                 osize="16">
                <div class="bqxi" osize="16">
                    技术支持：数字泸州产业投资集团有限公司 蜀ICP备17003881号<br osize="16">
                    中共泸州市机关工委 主办 联系电话：0830-2361732<br osize="16">
                    Copyright 2018 www.lz12371.cn All Rights Reserved<br osize="16">
                    联系地址：中共泸州市机关工委
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { queryNews, queryVideos } from '@/api/news'

export default {
    name: 'home',
    components: {},
    data() {
        return {
            searchKey: '',
            pageCount: 1,
            ADDRESS: process.env.VUE_APP_YSZJ_ADDRESS,
            //轮播图片
            backgroundImage: [
                require('@/assets/home/top_back.jpg')
            ],
            tplbList: [],
            activeName: 'jgdt',
            // 轮播索引
            lbindex: 0,
            // 场景服务轮播图片
            lytpList: [
                require('@/assets/home/xxesd.png'),
                require('@/assets/home/yygdz.png'),
                require('@/assets/home/bndd.png'),
                require('@/assets/home/cfh.png')
            ],
            jgdtList: [],
            ztzlList: [],
            djxxList: [],
            xfdxList: [],
            djwsList: [],
            videoList: []
        }
    },
    created() {
        this.queryNewsList(21)
        this.queryNewsList(2)
        this.queryNewsList(11)
        this.queryNewsList(31)
        this.queryNewsList(41)
        // this.queryVideoList()
    },
    methods: {
        toPage(num) {
            this.pageCount = num
            localStorage.setItem('menuType', num)
        },
        changeItem(index) {
            this.lbindex = index
        },
        queryNewsList(categoryId) {
            let openUrl = window.location.protocol + '//' + window.location.host
            let data = '?pageNum=1&pageSize=6&categoryId=' + categoryId
            queryNews(data).then(res => {
                if (res.code == 200) {
                    let list = res.data.records
                    for (let i = 0; i < list.length; i++) {
                        if (openUrl == 'http://10.46.19.198:7112') {
                            list[i].url = list[i].url.replace('http://10.4.117.31', 'http://10.46.19.198')
                        }
                        if (openUrl == 'https://portal.jcjgdj.cn') {
                            list[i].url = list[i].url.replace("http://10.4.117.31:7112", "https://cms.jcjgdj.cn/adminApi");
                        }
                    }
                    if(categoryId == 21){
                        this.jgdtList = list;
                    }else if(categoryId == 2){
                        this.ztzlList = list;
                    }else if(categoryId == 11){
                        this.djxxList = list;
                    }else if(categoryId == 31){
                        this.xfdxList = list;
                    }else if(categoryId == 41){
                        this.djwsList = list;
                    }
                } else {
                    this.$message.error('查询异常')
                }
            })
        },
        queryVideoList() {
            let openUrl = window.location.protocol + '//' + window.location.host
            let data = '?pageNum=1&pageSize=4'
            queryVideos(data).then(res => {
                if (res.code == 200) {
                    this.videoList = res.data.records
                    for (let i = 0; i < this.videoList.length; i++) {
                        if (openUrl == 'http://10.46.19.198:7112') {
                            this.videoList[i].url = this.videoList[i].url.replace('http://10.4.117.31', 'http://10.46.19.198')
                        }
                        if(openUrl == 'https://portal.jcjgdj.cn') {
                            this.videoList[i].url = this.videoList[i].url.replace("http://10.4.117.31:7380", "https://cms.jcjgdj.cn/adminApi");
                        }
                    }
                } else {
                    this.$message.error('查询异常')
                }
            })
        },
        toVideo(url) {
            window.open(url, '_blank')
        },
        toArticle(url) {
            window.open(url, '_blank')
        },
        gotoSearch() {
            var A = document.createElement('A')
            A.setAttribute('href', './searchList?searchKey=' + this.searchKey)
            A.click()
        }
    }
}
</script>
<style lang="less">
@import "./home.less";

.flex_ld {
    display: flex;
    justify-content: space-between;
}

.search {
    .el-input__inner {
        height: 45px !important;
        line-height: 45px !important;
    }

    .el-input-group {
        border: 1px solid #c01313;
        border-radius: 20px
    }

    .el-input-group--append .el-input__inner {
        border-right: 1px solid #ccc;
        border-top-left-radius: 20px;
        border-bottom-left-radius: 20px;
    }

    .el-input-group__append {
        border-top-right-radius: 20px;
        border-bottom-right-radius: 20px;
    }

    .el-icon-search {
        color: #c01313 !important;
    }

    .el-input__inner:focus {
        outline: none;
        border-color: transparent;
    }
}
</style>
