<template>
    <div class="footer">
        <div class="content-box">
            <Row>
                <Col span="10">
                    <span class="footer-title">友情链接</span>
                    <div class="footer-nav">
                        <a href="http://www.luzhou.gov.cn/" target="_blank">泸州市人民政府</a>
                        <a href="http://www.lz12371.cn/" target="_blank">智慧党建·酒城先锋-中共泸州市委组织部</a>
                        <a href="http://zjj.luzhou.gov.cn/" target="_blank">泸州市住建局</a>
                        <a href="http://rsj.luzhou.gov.cn/" target="_blank">泸州市人力资源和社会保障局</a>
                        <a href="http://swhzj.luzhou.gov.cn/" target="_blank">泸州市商务和会展局</a>
                    </div>
                </Col>
                <Col span="6">
                    <span class="footer-title">更多服务</span>
                    <div style="display: flex">
                        <div>
                            <img style="width: 90px;" src="~@/assets/images/portal/bottom/jcet.png">
                            <span class="footer-title" style="width: 90px;text-align: center;">酒城e通APP</span>
                        </div>
                        <div>
                            <img style="width: 90px" src="~@/assets/images/portal/bottom/jcet-wx.jpg">
                            <span class="footer-title" style="width: 90px;text-align: center;">微信小程序</span>
                        </div>
                    </div>
                </Col>
                <Col span="8">
                    <span class="footer-title">联系我们</span>
                    <div class="dis-ib a-t a-l mg-r40">
                        <div><span>泸州市人才办</span></div>
                        <div><span>地址：泸州市江阳区大山坪南路1段</span></div>
                        <div><span>业务咨询电话：0830-3190599</span></div>
                        <div><span>技术支持电话：0830-6568325</span></div>
                        <div><span>工作时间：工作日上午9:00-12:00 下午2:00-6:00</span></div>
                    </div>
                </Col>
            </Row>
        </div>
        <div style="border: 1px solid #fff;"></div>
        <div class="footer-info">
            <div class="unit">
                <span style="margin-right: 20px;">Copyright 2023-2024 All Rights Reserved.</span>
                <span style="margin-right: 20px;">主办单位：泸州市人才工作领导小组办公室</span>
                <span>技术单位：数字泸州产业投资集团有限公司</span>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "pageFooter",
        data(){
            return {
                flag:false
            }
        },
        mounted() {
            let bodyHeight = document.body.clientHeight;
            let window_height = window.innerHeight;
            let b_top = window_height - bodyHeight - 170 - 40;
            if (b_top > 0){
                // 添加固定定位css
                this.flag = false;
            }else{
                this.flag = true;
            }
        }
    }
</script>

<style lang="scss" scoped>
    .footer {
        position: relative;
        /*height: 10.625rem;*/
        background: #35383e;
        font-size: 0;
        bottom: 0;
    }
    .footer .content-box {
        width: 75rem;
        padding: 10px 0;
        height: 100%;
        margin: 0 auto;
    }
    .footer .footer-l {
        display: inline-block;
        vertical-align: top;
        width: 46.25rem;
    }
    .footer .footer-r {
        display: inline-block;
        vertical-align: top;
        width: 28.75rem;
        text-align: right;
    }
    .footer .footer-title {
        display: inline-block;
        vertical-align: top;
        color: #fff;
        font-size: 12px;
        margin-right: 25px;
        line-height: 24px;
    }
    .footer .footer-nav {
        display: inline-block;
        vertical-align: top;
        width: 16.875rem;
    }
    .footer a {
        display: inline-block;
        vertical-align: middle;
        color: #999da6;
        line-height: 24px;
        margin-right: 30px;
        font-size: 12px;
    }
    .mg-r40 {
        margin-right: 40px;
    }
    .a-t {
        vertical-align: top;
    }
    .a-l {
        text-align: left;
    }
    .dis-ib {
        display: inline-block;
    }
    .footer span {
        display: inline-block;
        vertical-align: middle;
        color: #999da6;
        line-height: 24px;
        font-size: 12px;
    }
    .footer-nav a:hover {
        color: #fff;
    }
    .footer .footer-info {
        background: #35383e;
    }
    .unit {
        width: 75rem;
        margin: auto;
        height: 2.5rem;
        line-height: 2.5rem;
        text-align: center;
    }
</style>
