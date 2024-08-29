<template>
    <div class="container">
        <Row class="page-content">
            <img class="icon-img" :src="iconImg">
            <div class="page-txt">智慧党建云平台</div>
        </Row>
        <Row class="items">
            <Col span="9">
                <img class="nmg-img" @click="itemClick(1)" :src="nmgImg">
            </Col>
            <Col span="15">
                <Row :gutter="10">
                    <Col span="12">
                        <img class="item-img" @click="itemClick(2)" :src="qydImg">
                    </Col>
                    <Col span="12">
                        <img class="item-img" @click="itemClick(3)" :src="jgdImg">
                    </Col>
                </Row>
                <Row :gutter="10">
                    <Col span="12">
                        <img class="item-img" @click="itemClick(4)" :src="bhglImg">
                    </Col>
                    <Col span="12">
                        <img class="item-img" @click="itemClick(5)" :src="gzhImg">
                    </Col>
                </Row>
            </Col>
        </Row>
        <div class="items bottom">
            <Row :gutter="40">
                <Col span="16">
                    <img class="app-img" @click="itemClick(6)" :src="appImg">
                </Col>
                <Col span="8">
                    <img class="sbd-img" @click="itemClick(7)" :src="sbdImg">
                </Col>
            </Row>
        </div>
        <!--  底部  -->
        <div class="el-login-footer">
            <span>Copyright © 2022 泸州市人力资源和社会保障局  技术支持：数字泸州产业投资集团有限公司</span>
        </div>
        <Modal
                v-model="codeShow"
                title="提示"
                @on-ok="ok"
                @on-cancel="cancel">
            <div style="text-align: center;">
                <img :src="codeImg">
            </div>
        </Modal>
    </div>
</template>

<script>
import iconImg from '@/assets/images/logo.svg'
import nmgImg from '@/assets/panel/nmg.png'
import qydImg from '@/assets/panel/qyd.png'
import jgdImg from '@/assets/panel/jgd.png'
import bhglImg from '@/assets/panel/bhgl.png'
import gzhImg from '@/assets/panel/gzh.png'
import appImg from '@/assets/panel/app.png'
import sbdImg from '@/assets/panel/sbd.png'
import codeImg from '@/assets/panel/code.jpg'

export default {
    name: 'IndexPanel',
    data() {
        return {
            nmgImg: nmgImg,
            qydImg: qydImg,
            jgdImg: jgdImg,
            bhglImg: bhglImg,
            gzhImg: gzhImg,
            appImg: appImg,
            sbdImg: sbdImg,
            iconImg: iconImg,
            codeImg: codeImg,
            codeShow: false
        }
    },
    methods:{
        itemClick(type){
            if(type == 1){
              window.open("https://luzhou-f-ow.gongbaojin.com/",'_blank');
            }else if(type == 2){
              window.open("https://luzhou-f.gongbaojin.com/business/#/login?redirect=%2Fwelcome%2Findex",'_blank');
            }else if(type == 3){
              window.open("https://luzhou-f.gongbaojin.com/rc/#/login?redirect=%2Fwelcome%2Findex",'_blank');
            }else if(type == 4){
              this.$Message.info('敬请期待！');
            }else if(type == 5){
              this.$Message.info('敬请期待！');
            }else if(type == 6){
                this.codeShow = true;
            }else if(type == 7){
              window.open("http://101.206.141.99:8066",'_blank');
            }
        },
        ok () {
          this.codeShow = false;
        },
        cancel () {
          this.codeShow = false;
        }
    }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.container {
    height: 100%;
    background-image: url("../assets/panel/panel_back.png");
    background-size: cover;

    .page-content {
        margin: auto;
        width: 900px;
    }
    .page-txt {
        font-size: 40px;
        font-weight: bold;
        color: #fff;
        padding: 5% 0 0 20px;
    }
    .icon-img {
        width: 80px;
        margin: 4.5% 0 0 8%;
    }
    .top-img {
        width: 800px;
        height: 150px;
        margin: 80px 0 0 8%;
    }
    .items {
        width: 900px;
        margin: 50px auto 0;

        .nmg-img {
            width: 85%;
        }
        .item-img {
            width: 100%;
        }
        .app-img {
            width: 104%;
            height: 100%;
        }
        .sbd-img {
            width: 100%;
            height: 100%;
        }
    }
    .bottom {
        margin-top: 15px;
    }
    .el-login-footer {
        height: 40px;
        line-height: 40px;
        position: fixed;
        bottom: 0;
        width: 100%;
        text-align: center;
        color: #fff;
        font-family: Arial;
        font-size: 16px;
        letter-spacing: 1px;
    }
}
</style>
