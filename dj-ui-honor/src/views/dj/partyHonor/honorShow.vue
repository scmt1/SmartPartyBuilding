<template>
    <div :style="'width: 100%; padding: 15px; background-image: url('+ bgImg+'); background-size: cover;'">
        <div>
            <div class="head-content">
                <div class="line-box">
                    <div class="left-line-top"></div>
                    <div class="left-line-bottom"></div>
                </div>
                <div class="star-img-box">
                    <img :src="starImg" class="img">
                </div>
                <div class="honor-box">
                    <div class="title">组织荣誉</div>
                    <div class="img-box">
                        <img :src="organizationHonorImg" class="img">
                    </div>
                </div>
                <div class="star-img-box">
                    <img :src="starImg" class="img">
                </div>
                <div class="line-box">
                    <div class="right-line-top"></div>
                    <div class="right-line-bottom"></div>
                </div>
            </div>

            <el-carousel v-if="organizationHonorTotal > 0" :height="carouselHeight" indicator-position="outside" arrow="always" :interval="10000" @change="carouselOrgChange">
                <el-carousel-item v-for="index of organizationHonorTotal" :key="index">
                    <div class="carousel-box">

                        <div v-resize="getContentHeight" class="carousel-content-box" v-for="(item, index) in organizationHonorList[index - 1]" :key="index">
                            <div class="img-box">
                                <img :src="item.imagePath.length > 0 ? base + item.imagePath:defaultImg" class="img">
                            </div>
                            <div class="decorate-box">
                                <img :src="plateImg" class="decorate-img">
                                <div class="decorate-text">
                                    {{ getDirectLabelByValue(item.honorClass, honorClassOptions) }}</div>
                            </div>
                            <div>
                                {{ item.tzSysDept.name }}
                            </div>
                            <div>{{ item.honorName }}（{{ new Date(item.getYear).getFullYear()}}年度）</div>
                        </div>

                    </div>
                </el-carousel-item>
            </el-carousel>
            <div v-else class="empty">
                暂无组织荣誉
            </div>
        </div>

        <div>
            <div class="head-content">
                <div class="line-box">
                    <div class="left-line-top"></div>
                    <div class="left-line-bottom"></div>
                </div>
                <div class="star-img-box">
                    <img :src="starImg2" class="img">
                </div>
                <div class="honor-box">
                    <div class="title">个人荣誉</div>
                    <div class="img-box">
                        <img :src="organizationHonorImg" class="img">
                    </div>
                </div>
                <div class="star-img-box">
                    <img :src="starImg2" class="img">
                </div>
                <div class="line-box">
                    <div class="right-line-top"></div>
                    <div class="right-line-bottom"></div>
                </div>
            </div>

            <el-carousel v-if="personHonorTotal > 0" :height="carouselHeight" indicator-position="outside" arrow="always" :interval="10000" @change="carouselPersonChange">
                <el-carousel-item v-for="index of personHonorTotal" :key="index">
                    <div class="carousel-box">

                        <div v-resize="getContentHeight" class="carousel-content-box" v-for="(item, index) in personHonorList[index - 1]" :key="index">
                            <div class="img-box">
                                <img :src="item.imagePath.length > 0 ? base + item.imagePath:defaultImg" class="img">
                            </div>
                            <div class="decorate-box">
                                <img :src="plateImg" class="decorate-img">
                                <div class="decorate-text">
                                    {{ getDirectLabelByValue(item.honorClass, honorClassOptions) }}</div>
                            </div>
                            <div>
                                {{ item.partyMember.realName }}
                            </div>
                            <div>{{ item.honorName }}（{{ new Date(item.getYear).getFullYear()}}年度）</div>
                        </div>

                    </div>
                </el-carousel-item>
            </el-carousel>
            <div v-else class="empty">
                暂无个人荣誉
            </div>
        </div>

    </div>
</template>

<script>
import defaultImg from "@/assets/defaultImg.png"
import plateImg from "@/assets/partyHonor/plate.png"
import organizationHonorImg from "@/assets/partyHonor/organizationHonor.png"
import starImg from "@/assets/partyHonor/star.png"
import starImg2 from "@/assets/partyHonor/star2.png"
import bgImg from "@/assets/partyHonor/bg.png"
import {queryTzPartyHonorList} from "@/api/TzPartyHonor";
import util from "@/libs/util";

export default {
    name: "honorShow",
    data() {
        return {
            base: util.nginxUrl,
            defaultImg: defaultImg,
            plateImg: plateImg,
            organizationHonorImg: organizationHonorImg,
            starImg: starImg,
            starImg2: starImg2,
            bgImg: bgImg,
            organizationHonorList: [],
            organizationHonorTotal: 0,
            organizationHonorFlag: true,
            personHonorList: [],
            personHonorTotal: 0,
            personFlag: true,
            carouselHeight: 'auto',
            pageNum: 1,
            pageSize: 8,
            honorClassOptions: [
                {
                    label: '国家级',
                    value: 1
                },
                {
                    label: '省级',
                    value: 2
                },
                {
                    label: '市级',
                    value: 3
                },
            ],
        }
    },
    directives:{
        resize: { // 指令的名称
            bind(el, binding) { // el为绑定的元素，binding为绑定给指令的对象
                let width = '', height = ''
                function isReize() {
                    const style = document.defaultView.getComputedStyle(el)
                    if (width !== style.width || height !== style.height) {
                        binding.value({width:style.width,height:style.height})  // 关键(这传入的是函数,所以执行此函数)
                    }
                    width = style.width;
                    height = style.height;
                }
                el.__vueSetInterval__ = setInterval(isReize, 300)
            },
            unbind(el) {
                clearInterval(el.__vueSetInterval__)
            }
        }
    },
    mounted() {
        this.getHonorList(1)
        this.getHonorList(2)
    },
    methods: {
        getContentHeight(event) {
            let height = event.height.replace('px', '')
            this.carouselHeight = Number(height) + 20 + 'px'
        },
        getDirectLabelByValue(value, direct) {
            for (let i = 0; i < direct.length; i++) {
                if (value == direct[i].value) {
                    return direct[i].label
                }
            }
        },
        carouselOrgChange(event) {
            // 如果后面还有数据，则继续获取
            if (this.organizationHonorFlag && event > event > this.organizationHonorTotal / 4 - 1) {
                this.pageNum++
                this.getHonorList(1)
            }
        },
        carouselPersonChange(event) {
            if (this.organizationHonorFlag && event > event > this.organizationHonorTotal / 4 - 1) {
                this.pageNum++
                this.getHonorList(2)
            }
        },
        getHonorList(honorType) {
            let userInformation = JSON.parse(window.localStorage.getItem("userInformation"))

            const data = {
                tzPartyHonor: {
                    deptId: userInformation.deptId,
                    honorType: honorType
                },
                pageVo: {
                    pageNumber: this.pageNum,
                    pageSize: this.pageSize
                }
            }
            queryTzPartyHonorList({ data: data }).then(res => {
                const result = res.data
                if (result.code == '00000') {
                    if (honorType == 1) {
                        this.organizationHonorTotal = Math.ceil(result.data.total/ 4)

                        if (result.data.records.length < this.pageSize || this.pageSize * this.pageNum == result.data.total) {
                            this.organizationHonorFlag = false
                        }

                        let size = Math.ceil(result.data.records.length / 4)
                        for (let i = 0; i < size; i++) {
                            let va = []
                            for (let j = 0; j < 4; j++) {
                                if (result.data.records[i*4+j]) {
                                    va.push(result.data.records[i*4+j])
                                }
                            }
                            this.organizationHonorList[i+ (this.pageNum*2 -2)] = va
                        }

                        this.$forceUpdate()
                    } else if (honorType == 2) {
                        this.personHonorTotal = Math.ceil(result.data.total/ 4)

                        if (result.data.records.length < this.pageSize || this.pageSize * this.pageNum == result.data.total) {
                            this.personFlag = false
                        }

                        let size = Math.ceil(result.data.records.length / 4)
                        for (let i = 0; i < size; i++) {
                            let va = []
                            for (let j = 0; j < 4; j++) {
                                if (result.data.records[i*4+j]) {
                                    va.push(result.data.records[i*4+j])
                                }
                            }
                            this.personHonorList[i+ (this.pageNum*2 -2)] = va
                        }

                        this.$forceUpdate()
                    }
                }
            })
        }
    }
}
</script>

<style scoped lang="scss">
::v-deep .el-carousel__item {
    width: calc(100% - 120px);
    margin-left: 60px;
}
.head-content {
    display: flex;
    justify-content: center;

    .line-box {
        flex: 1;
        position: relative;

        .left-line-top {
            height: 1px;
            width: calc(100% - 60px);
            background: rgb(255, 195, 0);
            top: 30px;
            right: -3px;
            position: absolute;
        }

        .left-line-bottom {
            height: 1px;
            width: calc(100% - 100px);
            background: rgb(255, 195, 0);
            top: 35px;
            right: -5px;
            position: absolute;
        }

        .right-line-top {
            height: 1px;
            width: calc(100% - 60px);
            background: rgb(255, 195, 0);
            top: 30px;
            left: -3px;
            position: absolute;
        }

        .right-line-bottom {
            height: 1px;
            width: calc(100% - 100px);
            background: rgb(255, 195, 0);
            top: 35px;
            left: -5px;
            position: absolute;
        }
    }

    .star-img-box {
        width: 23px;

        .img {
            width: 100%;
            margin-top: 20px;
        }
    }

    .honor-box {
        display: flex;
        flex-direction: column;
        width: 100px;
        margin: 0 5px;

        .title {
            color: rgb(225, 53, 27);
            font-weight: bold;
            font-size: 16px;
        }
        .img-box {
            margin-top: -28px;
            .img {
                width: 100%;
            }
        }
    }
}


::v-deep .el-carousel__arrow {
    background: red;
    margin-top: -30px;
}


.img-box {
    position: relative;
    width: 100%;
    height: 0px;
    padding-top: 67%;
    margin-bottom: 10px;

    .img {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        object-fit: cover;
    }
}

.carousel-box {
    display: flex;
    align-items: center;

    .carousel-content-box {
        height: 100%;
        width: 25%;
        padding: 0 25px;
        font-weight: bold;

        .decorate-box {
            position: relative;

            .decorate-img {
                width: 100%;
                height: 28px;
                margin-bottom: 10px;
            }

            .decorate-text {
                position: absolute;
                color: white;
                top: 0;
                width: 100%;
                text-align: center;
                line-height: 28px;
            }
        }
    }
}

.empty {
    text-align: center;
    color: #B4B4B9FF;
    padding: 15px 0;
}

</style>
