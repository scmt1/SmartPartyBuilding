<template>
    <div class="view-service">
        <div class="bannerbg2" :style="{'display': fadeIn ? 'block':'none'}">
            <div class="banner cf ma">
                <div class="bannerleft cf fl">
                    <span title="领取经济专业技术资格（初级、中级）资格证书" tabindex="60" class="text-tag">领取经济专业技术资格（初级、中级）资格证书</span>
                </div>
                <div class="bannerright cf">
                    <ul class="cf fr">
                        <li>
                            <a @click="toView" tabindex="61" class="text-tag">在线申请</a>
                        </li>
                        <li>
                            <a target="_blank" tabindex="63" class="text-tag">好差评</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="bannerbg1">
            <div class="banner cf ma">
                <div class="bannertop">
                    <span title="领取经济专业技术资格（初级、中级）资格证书" tabindex="42" class="text-tag">领取经济专业技术资格（初级、中级）资格证书</span>
                    <p tabindex="43" class="text-tag">泸州市人力资源和社会保障局</p>
                </div>
                <div class="bannermmid cf">
                    <ul class="cf fl">
                        <li>
                            <span tabindex="44" class="text-tag">0</span>
                            <em tabindex="45" class="text-tag">次</em>
                            <p tabindex="46" class="text-tag">到现场次数</p>
                        </li>
                        <li>
                            <span tabindex="47" class="text-tag">1</span>
                            <em tabindex="48" class="text-tag">工作日</em>
                            <p tabindex="49" class="text-tag">法定办结时限</p>
                        </li>
                        <li>
                            <span tabindex="50" class="text-tag">1</span>
                            <em tabindex="51" class="text-tag">工作日</em>
                            <p tabindex="52" class="text-tag">承诺办结时限</p>
                        </li>

                        <li></li>
                    </ul>
                    <div class="manyi fl">
                        <span tabindex="53" class="text-tag">满意度:100%</span>
                        <p tabindex="54" class="text-tag">1人参与了评价</p>
                    </div>
                </div>
                <div class="bannerbottom fl">
                    <ul class="cf">
                        <li>
                            <a @click="toView" tabindex="55" class="text-tag">在线申请</a>
                        </li>
                        <li>
                            <a target="_blank" tabindex="57" class="text-tag">好差评</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="sectionbg" ref="menuRef">
            <div class="ma">
                <div class="section-left">
                    <Anchor show-ink :offset-top="offsetTop">
                        <AnchorLink href="#basic_info" :scroll-offset="scrollOffset" title="基本信息"/>
                        <AnchorLink href="#appliy_materials" :scroll-offset="scrollOffset" title="申请材料"/>
                        <AnchorLink href="#acceptance_conditions" :scroll-offset="scrollOffset" title="受理条件"></AnchorLink>
                        <AnchorLink href="#handling_process" :scroll-offset="scrollOffset" title="办理流程"/>
                        <AnchorLink href="#charging_standard" :scroll-offset="scrollOffset" title="收费标准"/>
                        <AnchorLink href="#setting_basis" :scroll-offset="scrollOffset" title="设定依据"></AnchorLink>
                        <AnchorLink href="#common_problem" :scroll-offset="scrollOffset" title="常见问题"></AnchorLink>
                    </Anchor>
                </div>
                <div class="section_right">
                    <!--  基本信息  -->
                    <div class="bigbox1" id="basic_info">
                        <!-- 基本信息开始 -->
                        <div class="section1">
                            <div class="section1_top">
                                <span tabindex="72" class="text-tag">基本信息</span>
                            </div>
                            <table style="margin-top: 20px;">
                                <tbody><tr>
                                    <td tabindex="73" class="text-tag">服务对象</td>
                                    <td tabindex="74" class="text-tag">自然人</td>
                                    <td tabindex="75" class="text-tag">通办范围</td>
                                    <td tabindex="76" class="text-tag">全省</td>
                                </tr>
                                <tr>
                                    <td tabindex="77" class="text-tag">办件类型</td>
                                    <td tabindex="78" class="text-tag">即办件</td>
                                    <td tabindex="79" class="text-tag">办理形式</td>
                                    <td tabindex="80" class="text-tag">窗口办理、网上办理</td>
                                </tr>
                                <tr>
                                    <td tabindex="81" class="text-tag">法定办结时限</td>
                                    <td tabindex="82" class="text-tag">1个工作日</td>
                                    <td tabindex="83" class="text-tag">承诺办结时限</td>
                                    <td tabindex="84" class="text-tag">1个工作日</td>
                                </tr>
                                <tr>
                                    <td tabindex="85" class="text-tag">法定时限办结说明</td>
                                    <td tabindex="86" class="text-tag">自受理之日起，在法定期限内办结</td>
                                    <td tabindex="87" class="text-tag">承诺时限办结说明</td>
                                    <td tabindex="88" class="text-tag">自受理之日起，在承诺期限内办结</td>
                                </tr>
                                <tr>
                                    <td tabindex="89" class="text-tag">是否收费</td>
                                    <td tabindex="90" class="text-tag">否</td>
                                    <td tabindex="91" class="text-tag">认证等级需求</td>
                                    <td tabindex="92" class="text-tag">实名认证</td>
                                </tr>
                                <tr>
                                    <td tabindex="93" class="text-tag">计划生效日期</td>
                                    <td tabindex="94" class="text-tag">无</td>
                                    <td tabindex="95" class="text-tag">计划取消日期</td>
                                    <td tabindex="96" class="text-tag">无</td>
                                </tr>
                                <tr>
                                    <td tabindex="97" class="text-tag">行使方式</td>
                                    <td tabindex="98" class="text-tag">依申请行使</td>
                                    <td tabindex="99" class="text-tag">特别程序</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td tabindex="100" class="text-tag">到办事现场次数</td>
                                    <td tabindex="101" class="text-tag">0</td>
                                    <td tabindex="102" class="text-tag">必须到现场原因</td>
                                    <td tabindex="103" class="text-tag">无</td>
                                </tr>

                                </tbody></table>
                        </div>
                        <!-- 基本信息结束 -->
                        <!-- 窗口办理开始 -->
                        <div class="section1" :style="{'display': openOrClose ? 'none' : 'block'}">
                            <div class="section2_top">
                                <span tabindex="104" class="text-tag">窗口办理</span>
                            </div>
                            <table>
                                <tbody><tr>
                                    <td tabindex="105" class="text-tag">是否支持预约办理</td>
                                    <td tabindex="106" class="text-tag">否</td>
                                    <td tabindex="107" class="text-tag">是否支持物流快递</td>
                                    <td tabindex="108" class="text-tag">否</td>
                                </tr>
                                <tr>
                                    <td tabindex="109" class="text-tag">是否支持全省就近取件</td>
                                    <td tabindex="110" class="text-tag">否</td>
                                    <td tabindex="111" class="text-tag">是否支持全省就近办理</td>
                                    <td tabindex="112" class="text-tag">否</td>
                                </tr>
                                </tbody></table>
                        </div>
                        <!-- 窗口办理结束 -->
                        <!-- 网上办理开始 -->
                        <div class="section1" :style="{'display': openOrClose ? 'none' : 'block'}">
                            <div class="section2_top">
                                <span tabindex="113" class="text-tag">网上办理</span>
                            </div>
                            <table>
                                <tbody><tr>
                                    <td tabindex="114" class="text-tag">网办类型</td>
                                    <td tabindex="115" class="text-tag">全程网办</td>
                                    <td tabindex="116" class="text-tag">网办地址</td>
                                    <td>
                                        <a style="cursor: pointer;color: #426EC0" onclick="checkIsOnlinenew('1', '101', 'http://202.61.89.231/ctc-0', '512B0361300039-510500000000-000-115104005656782570-3-00', '1', '0', '15693808', '510500000000');" tabindex="117" class="text-tag">点击访问</a>
                                    </td>
                                </tr>
                                <tr>
                                    <td tabindex="118" class="text-tag">是否支持物流快递</td>
                                    <td tabindex="119" class="text-tag">是</td>
                                    <td tabindex="120" class="text-tag">是否支持上门收取申请材料</td>
                                    <td tabindex="121" class="text-tag">无</td>
                                </tr>
                                <tr>
                                    <td tabindex="122" class="text-tag">网上办理深度</td>
                                    <td colspan="3" tabindex="123" class="text-tag">互联网咨询、互联网收件、互联网预审、互联网受理、互联网办理、互联网办理结果信息反馈</td>
                                </tr>
                                </tbody></table>
                        </div>
                        <!-- 网上办理结束 -->
                        <!-- 常规信息开始 -->
                        <div class="section1" :style="{'display': openOrClose ? 'none' : 'block'}">
                            <div class="section2_top">
                                <span tabindex="124" class="text-tag">常规信息</span>
                            </div>
                            <table class="table1">
                                <tbody><tr>
                                    <td tabindex="125" class="text-tag">办理时间</td>
                                    <td tabindex="126" class="text-tag">星期一至星期五 上午：09:00-12:00 下午：14:30-18:30 备注：法定节假日除外;</td>
                                </tr>
                                <tr>
                                    <td tabindex="127" class="text-tag">办理地点</td>
                                    <td tabindex="128" class="text-tag">四川省-泸州市-龙马潭区-红星街道-龙马大道一段70号,详细地址：泸州市龙马潭区龙马大道一段70号市人力资源和社会保障局3楼325;</td>
                                </tr>
                                <tr>
                                    <td tabindex="129" class="text-tag">咨询方式</td>
                                    <td tabindex="130" class="text-tag"> 咨询电话：0830-2504875, 窗口咨询方式：泸州市龙马潭区龙马大道一段70号泸州市人力资源和社会保障局3楼325, 网上咨询地址：四川政务服务网12345在线（http://www.sczwfw.gov.cn/app/main?areaCode=510000000000&amp;iframeUrlLo=http://zmhd.sczwfw.gov.cn）;</td>
                                </tr>
                                <tr>
                                    <td tabindex="131" class="text-tag">监督投诉方式</td>
                                    <td tabindex="132" class="text-tag"> 投诉电话：0830-12345, 窗口投诉：泸州龙马潭区龙马大道一段70号泸州市人力资源和社会保障局10楼1008机关纪委, 网上投诉：四川政务服务网 12345在线（ http://www.sczwfw.gov.cn/app/main?areaCode=510000000000&amp;iframeUrlLo=http://zmhd.sczwfw.gov.cn）;</td>
                                </tr>
                                </tbody></table>

                            <table class="table2">
                                <tbody><tr>
                                    <td tabindex="133" class="text-tag">实施主体</td>
                                    <td tabindex="134" class="text-tag">泸州市人力资源和社会保障局</td>
                                    <td tabindex="135" class="text-tag">事项版本</td>
                                    <td tabindex="136" class="text-tag">12</td>
                                </tr>
                                <tr>
                                    <td tabindex="137" class="text-tag">事项类型</td>
                                    <td tabindex="138" class="text-tag">公共服务</td>
                                    <td tabindex="139" class="text-tag">行使层级</td>
                                    <td tabindex="140" class="text-tag">市级</td>
                                </tr>
                                <tr>
                                    <td tabindex="141" class="text-tag">权力来源</td>
                                    <td tabindex="142" class="text-tag">上级委托</td>
                                    <td tabindex="143" class="text-tag">实施主体性质</td>
                                    <td tabindex="144" class="text-tag">受委托组织</td>
                                </tr>
                                <tr>
                                    <td tabindex="145" class="text-tag">是否进驻政务大厅</td>
                                    <td tabindex="146" class="text-tag">是</td>
                                    <td tabindex="147" class="text-tag">是否支持自助终端办理</td>
                                    <td tabindex="148" class="text-tag">否</td>
                                </tr>
                                <tr>
                                    <td tabindex="149" class="text-tag">是否网办</td>
                                    <td tabindex="150" class="text-tag">是</td>
                                    <td tabindex="151" class="text-tag">是否支持网上支付</td>
                                    <td tabindex="152" class="text-tag">是</td>
                                </tr>
                                <tr>
                                    <td tabindex="153" class="text-tag">委托部门</td>
                                    <td tabindex="154" class="text-tag">泸州市人事考试中心</td>
                                    <td tabindex="155" class="text-tag">联办机构</td>
                                    <td tabindex="156" class="text-tag">无</td>
                                </tr>
                                <tr>
                                    <td tabindex="157" class="text-tag">事项状态</td>
                                    <td tabindex="158" class="text-tag">在用</td>
                                    <td tabindex="159" class="text-tag">数量限制</td>
                                    <td tabindex="160" class="text-tag">无</td>
                                </tr>
                                <tr>
                                    <td tabindex="161" class="text-tag">基本编码</td>
                                    <td tabindex="162" class="text-tag">002014208018</td>
                                    <td tabindex="163" class="text-tag">实施主体编码</td>
                                    <td tabindex="164" class="text-tag">115104005656782570</td>
                                </tr>
                                <tr>
                                    <td tabindex="165" class="text-tag">实施编码</td>
                                    <td tabindex="166" class="text-tag">1151040056567825703002014208018</td>
                                    <td tabindex="167" class="text-tag">业务办理项编码</td>
                                    <td tabindex="168" class="text-tag">115104005656782570300201420801801</td>
                                </tr>
                                <tr>
                                    <td tabindex="169" class="text-tag">乡镇街道名称</td>
                                    <td tabindex="170" class="text-tag">无</td>
                                    <td tabindex="171" class="text-tag">乡镇街道代码</td>
                                    <td tabindex="172" class="text-tag">无</td>
                                </tr>
                                <tr>
                                    <td tabindex="173" class="text-tag">村居社区名称</td>
                                    <td tabindex="174" class="text-tag">无</td>
                                    <td tabindex="175" class="text-tag">村居社区代码</td>
                                    <td tabindex="176" class="text-tag">无</td>
                                </tr>
                                </tbody></table>
                        </div>
                        <!-- 常规信息结束 -->
                        <!-- 开关1 -->
                        <div class="open1" @click="openOrCloseChange(false)" :style="{'display': openOrClose ? 'block' : 'none'}">
                            <p tabindex="177" class="text-tag" style="margin-left: 30px;">全部展开</p>
                        </div>
                        <div class="close1" @click="openOrCloseChange(true)" :style="{'display': openOrClose ? 'none' : 'block'}">
                            <p tabindex="178" class="text-tag">收起</p>
                        </div>
                    </div>
                    <!--  申请资料  -->
                    <div class="bigbox2" id="appliy_materials">
                        <!-- 申请材料开始 -->
                        <div class="section5">
                            <div class="section5_top">
                                <span tabindex="179" class="text-tag">申请材料</span>
                            </div>
                            <div class="section5_mid cf">
                                <input id="lawRegulations" type="hidden" value="[{&quot;lawType&quot;:&quot;9&quot;,&quot;publishTime&quot;:1559664000000,&quot;isExistLaw&quot;:&quot;1&quot;,&quot;iid&quot;:&quot;353ac88b3bc04eebb3215b3ad6a223b0&quot;,&quot;year&quot;:2022,&quot;utime&quot;:1671185492291,&quot;thLrContentList&quot;:[{&quot;clause&quot;:&quot;三&quot;,&quot;iid&quot;:&quot;e586e1b5e2ee4d1e863ac84599e7b7bf&quot;,&quot;utime&quot;:1671185497452,&quot;year&quot;:2022,&quot;updateTime&quot;:1671185497000,&quot;ordernum&quot;:0,&quot;overrideDefaulTime&quot;:false,&quot;shardKey&quot;:&quot;5105&quot;,&quot;delFlag&quot;:&quot;0&quot;,&quot;implListId&quot;:4368119995872870400,&quot;clauseContent&quot;:&quot;资格审核部门（机构）对报考人员填报的信息进行核验和核查，并依据其作出的承诺为其办理报考相关事项。&quot;,&quot;lrId&quot;:4368119996288106498,&quot;deleted&quot;:false,&quot;taskCode&quot;:&quot;512B0361300039-510500000000-000-115104005656782570-3-00&quot;,&quot;createTime&quot;:1671181763000,&quot;ctime&quot;:1671185497452,&quot;id&quot;:4368119996296495104}],&quot;overrideDefaulTime&quot;:false,&quot;updateTime&quot;:1671185492000,&quot;ordernum&quot;:0,&quot;shardKey&quot;:&quot;5105&quot;,&quot;delFlag&quot;:&quot;0&quot;,&quot;implListId&quot;:4368119995872870400,&quot;referenceNum&quot;:&quot;人考中心函〔2019〕26号&quot;,&quot;ownId&quot;:4368119995872870400,&quot;deleted&quot;:false,&quot;taskCode&quot;:&quot;512B0361300039-510500000000-000-115104005656782570-3-00&quot;,&quot;lawName&quot;:&quot;关于印发《专业技术人员资格考试报名证明事项告知承诺制试点工作实施方案》的通知&quot;,&quot;createTime&quot;:1671181763000,&quot;ctime&quot;:1671185492291,&quot;id&quot;:4368119996288106498,&quot;ownTag&quot;:&quot;6&quot;},{&quot;lawType&quot;:&quot;9&quot;,&quot;publishTime&quot;:1014912000000,&quot;isExistLaw&quot;:&quot;1&quot;,&quot;iid&quot;:&quot;3ce4ac556f70433e9e0d656432e9dd1c&quot;,&quot;year&quot;:2022,&quot;utime&quot;:1671185492274,&quot;thLrContentList&quot;:[{&quot;clause&quot;:&quot;第四条&quot;,&quot;iid&quot;:&quot;70625f2bd5b743809e1620d91864a21f&quot;,&quot;utime&quot;:1671185497413,&quot;year&quot;:2022,&quot;updateTime&quot;:1671185497000,&quot;ordernum&quot;:0,&quot;overrideDefaulTime&quot;:false,&quot;shardKey&quot;:&quot;5105&quot;,&quot;delFlag&quot;:&quot;0&quot;,&quot;implListId&quot;:4368119995872870400,&quot;clauseContent&quot;:&quot;有关事项……（二）经济专业技术资格初、中级考试公共科目的名称均为“经济基础知识”……。 &quot;,&quot;lrId&quot;:4368119996246163457,&quot;deleted&quot;:false,&quot;taskCode&quot;:&quot;512B0361300039-510500000000-000-115104005656782570-3-00&quot;,&quot;createTime&quot;:1671181763000,&quot;ctime&quot;:1671185497413,&quot;id&quot;:4368119996279717888}],&quot;overrideDefaulTime&quot;:false,&quot;updateTime&quot;:1671185492000,&quot;ordernum&quot;:0,&quot;shardKey&quot;:&quot;5105&quot;,&quot;delFlag&quot;:&quot;0&quot;,&quot;implListId&quot;:4368119995872870400,&quot;referenceNum&quot;:&quot;人办发〔2002〕18号&quot;,&quot;ownId&quot;:4368119995872870400,&quot;deleted&quot;:false,&quot;taskCode&quot;:&quot;512B0361300039-510500000000-000-115104005656782570-3-00&quot;,&quot;lawName&quot;:&quot;关于调整经济专业技术资格考试专业设置的通知&quot;,&quot;createTime&quot;:1671181763000,&quot;ctime&quot;:1671185492274,&quot;id&quot;:4368119996246163457,&quot;ownTag&quot;:&quot;1&quot;},{&quot;lawType&quot;:&quot;9&quot;,&quot;publishingDepartment&quot;:&quot;人力资源社会保障部&quot;,&quot;publishTime&quot;:1579190400000,&quot;isExistLaw&quot;:&quot;1&quot;,&quot;iid&quot;:&quot;77431fce117f42cebd4b791a413b8512&quot;,&quot;year&quot;:2022,&quot;utime&quot;:1671185492282,&quot;thLrContentList&quot;:[{&quot;clause&quot;:&quot;《经济专业技术资格规定》第十二条&quot;,&quot;iid&quot;:&quot;426a7d35f9f74b12b329b5efc9574d97&quot;,&quot;utime&quot;:1671185497430,&quot;year&quot;:2022,&quot;updateTime&quot;:1671185497000,&quot;ordernum&quot;:0,&quot;overrideDefaulTime&quot;:false,&quot;shardKey&quot;:&quot;5105&quot;,&quot;delFlag&quot;:&quot;0&quot;,&quot;implListId&quot;:4368119995872870400,&quot;clauseContent&quot;:&quot;第十二条  初级、中级经济专业技术资格考试合格者，颁发人力资源社会保障部统一印制的经济专业技术资格证书，在全国范围内有效。人力资源管理和知识产权专业分别颁发相应级别的人力资源管理师证书和知识产权师证书。&quot;,&quot;lrId&quot;:4368119996279717889,&quot;deleted&quot;:false,&quot;taskCode&quot;:&quot;512B0361300039-510500000000-000-115104005656782570-3-00&quot;,&quot;createTime&quot;:1671181763000,&quot;ctime&quot;:1671185497430,&quot;id&quot;:4368119996288106496},{&quot;clause&quot;:&quot;《经济专业技术资格考试实施办法》第六条&quot;,&quot;iid&quot;:&quot;af8e8da0530840cfa9080067e46bca70&quot;,&quot;utime&quot;:1671185497441,&quot;year&quot;:2022,&quot;updateTime&quot;:1671185497000,&quot;ordernum&quot;:0,&quot;overrideDefaulTime&quot;:false,&quot;shardKey&quot;:&quot;5105&quot;,&quot;delFlag&quot;:&quot;0&quot;,&quot;implListId&quot;:4368119995872870400,&quot;clauseContent&quot;:&quot;第六条  初级、中级经济专业技术资格考试成绩实行2年为一个周期的滚动管理方法，应试人员须在连续的两个考试年度内通过全部应试科目，方可取得相应级别经济专业技术资格证书。&quot;,&quot;lrId&quot;:4368119996279717889,&quot;deleted&quot;:false,&quot;taskCode&quot;:&quot;512B0361300039-510500000000-000-115104005656782570-3-00&quot;,&quot;createTime&quot;:1671181763000,&quot;ctime&quot;:1671185497441,&quot;id&quot;:4368119996288106497}],&quot;overrideDefaulTime&quot;:false,&quot;updateTime&quot;:1671185492000,&quot;ordernum&quot;:0,&quot;shardKey&quot;:&quot;5105&quot;,&quot;delFlag&quot;:&quot;0&quot;,&quot;implListId&quot;:4368119995872870400,&quot;referenceNum&quot;:&quot;人社部规[2020]1号&quot;,&quot;ownId&quot;:4368119995872870400,&quot;deleted&quot;:false,&quot;taskCode&quot;:&quot;512B0361300039-510500000000-000-115104005656782570-3-00&quot;,&quot;lawName&quot;:&quot;关于印发《经济专业技术资格规定》和《经济专业技术资格考试实施办法》的通知&quot;,&quot;createTime&quot;:1671181763000,&quot;ctime&quot;:1671185492282,&quot;id&quot;:4368119996279717889,&quot;ownTag&quot;:&quot;3&quot;},{&quot;lawType&quot;:&quot;9&quot;,&quot;publishingDepartment&quot;:&quot;人力资源社会保障部&quot;,&quot;publishTime&quot;:1579190400000,&quot;isExistLaw&quot;:&quot;1&quot;,&quot;iid&quot;:&quot;9db23d03635146b3bf224782c736bbea&quot;,&quot;year&quot;:2022,&quot;utime&quot;:1671185492307,&quot;thLrContentList&quot;:[{&quot;clause&quot;:&quot;《经济专业技术资格规定》第十二条&quot;,&quot;iid&quot;:&quot;17948b18b5d044fc9beea3721340ca99&quot;,&quot;utime&quot;:1671185497464,&quot;year&quot;:2022,&quot;updateTime&quot;:1671185497000,&quot;ordernum&quot;:0,&quot;overrideDefaulTime&quot;:false,&quot;shardKey&quot;:&quot;5105&quot;,&quot;delFlag&quot;:&quot;0&quot;,&quot;implListId&quot;:4368119995872870400,&quot;clauseContent&quot;:&quot;第十二条  初级、中级经济专业技术资格考试合格者，颁发人力资源社会保障部统一印制的经济专业技术资格证书，在全国范围内有效。人力资源管理和知识产权专业分别颁发相应级别的人力资源管理师证书和知识产权师证书。&quot;,&quot;lrId&quot;:4368119997730947072,&quot;deleted&quot;:false,&quot;taskCode&quot;:&quot;512B0361300039-510500000000-000-115104005656782570-3-00&quot;,&quot;createTime&quot;:1671181763000,&quot;ctime&quot;:1671185497464,&quot;id&quot;:4368119997739335680},{&quot;clause&quot;:&quot;《经济专业技术资格考试实施办法》第六条&quot;,&quot;iid&quot;:&quot;55fc347b146a4a3092c7dc68c0fe1cd3&quot;,&quot;utime&quot;:1671185497482,&quot;year&quot;:2022,&quot;updateTime&quot;:1671185497000,&quot;ordernum&quot;:0,&quot;overrideDefaulTime&quot;:false,&quot;shardKey&quot;:&quot;5105&quot;,&quot;delFlag&quot;:&quot;0&quot;,&quot;implListId&quot;:4368119995872870400,&quot;clauseContent&quot;:&quot;第六条  初级、中级经济专业技术资格考试成绩实行2年为一个周期的滚动管理方法，应试人员须在连续的两个考试年度内通过全部应试科目，方可取得相应级别经济专业技术资格证书。&quot;,&quot;lrId&quot;:4368119997730947072,&quot;deleted&quot;:false,&quot;taskCode&quot;:&quot;512B0361300039-510500000000-000-115104005656782570-3-00&quot;,&quot;createTime&quot;:1671181763000,&quot;ctime&quot;:1671185497482,&quot;id&quot;:4368119997739335681}],&quot;overrideDefaulTime&quot;:false,&quot;updateTime&quot;:1671185492000,&quot;ordernum&quot;:0,&quot;shardKey&quot;:&quot;5105&quot;,&quot;delFlag&quot;:&quot;0&quot;,&quot;implListId&quot;:4368119995872870400,&quot;referenceNum&quot;:&quot;人社部规[2020]1号&quot;,&quot;ownId&quot;:4368119997651255296,&quot;deleted&quot;:false,&quot;taskCode&quot;:&quot;512B0361300039-510500000000-000-115104005656782570-3-00&quot;,&quot;lawName&quot;:&quot;关于印发《经济专业技术资格规定》和《经济专业技术资格考试实施办法》的通知&quot;,&quot;createTime&quot;:1671181763000,&quot;ctime&quot;:1671185492307,&quot;id&quot;:4368119997730947072,&quot;ownTag&quot;:&quot;4&quot;},{&quot;lawType&quot;:&quot;9&quot;,&quot;publishTime&quot;:726249600000,&quot;isExistLaw&quot;:&quot;1&quot;,&quot;iid&quot;:&quot;d643390d09824effaf7ea530f6766a7b&quot;,&quot;year&quot;:2022,&quot;utime&quot;:1671185492253,&quot;thLrContentList&quot;:[{&quot;clause&quot;:&quot;第十四条&quot;,&quot;iid&quot;:&quot;18f9d36875fe48b3b1e8fe8621292212&quot;,&quot;utime&quot;:1671185497391,&quot;year&quot;:2022,&quot;updateTime&quot;:1671185497000,&quot;ordernum&quot;:0,&quot;overrideDefaulTime&quot;:false,&quot;shardKey&quot;:&quot;5105&quot;,&quot;delFlag&quot;:&quot;0&quot;,&quot;implListId&quot;:4368119995872870400,&quot;clauseContent&quot;:&quot;经济专业技术资格考试在国务院职称改革工作领导小组统一领导下进行，由人事部负责，委托全国职称考试指导中心具体组织实施，各省、自治区、直辖市资格考试的组织实施工作由当地职改领导小组决定。&quot;,&quot;lrId&quot;:4368119996019671040,&quot;deleted&quot;:false,&quot;taskCode&quot;:&quot;512B0361300039-510500000000-000-115104005656782570-3-00&quot;,&quot;createTime&quot;:1671181763000,&quot;ctime&quot;:1671185497390,&quot;id&quot;:4368119996233580546},{&quot;clause&quot;:&quot;《经济专业技术资格考试暂行规定》第二条&quot;,&quot;iid&quot;:&quot;5705eab2d7b247f39eb52a587e0f15e3&quot;,&quot;utime&quot;:1671185497356,&quot;year&quot;:2022,&quot;updateTime&quot;:1671185497000,&quot;ordernum&quot;:0,&quot;overrideDefaulTime&quot;:false,&quot;shardKey&quot;:&quot;5105&quot;,&quot;delFlag&quot;:&quot;0&quot;,&quot;implListId&quot;:4368119995872870400,&quot;clauseContent&quot;:&quot;经济专业技术资格实行全国统一考试制度，由全国统一组织、统一大纲、统一试题、统一评分标准。资格考试设置两个级别：经济专业初级资格、经济专业中级资格……。&quot;,&quot;lrId&quot;:4368119996019671040,&quot;deleted&quot;:false,&quot;taskCode&quot;:&quot;512B0361300039-510500000000-000-115104005656782570-3-00&quot;,&quot;createTime&quot;:1671181763000,&quot;ctime&quot;:1671185497356,&quot;id&quot;:4368119996233580544},{&quot;clause&quot;:&quot;第六条&quot;,&quot;iid&quot;:&quot;8611f878a917416baf97ca7b7c86ad12&quot;,&quot;utime&quot;:1671185497380,&quot;year&quot;:2022,&quot;updateTime&quot;:1671185497000,&quot;ordernum&quot;:0,&quot;overrideDefaulTime&quot;:false,&quot;shardKey&quot;:&quot;5105&quot;,&quot;delFlag&quot;:&quot;0&quot;,&quot;implListId&quot;:4368119995872870400,&quot;clauseContent&quot;:&quot;报名参加经济专业中、初级资格考试的人员应拥护中国共产党在社会主义初级阶段的基本路线，遵纪守法，遵守社会公德。&quot;,&quot;lrId&quot;:4368119996019671040,&quot;deleted&quot;:false,&quot;taskCode&quot;:&quot;512B0361300039-510500000000-000-115104005656782570-3-00&quot;,&quot;createTime&quot;:1671181763000,&quot;ctime&quot;:1671185497380,&quot;id&quot;:4368119996233580545}],&quot;overrideDefaulTime&quot;:false,&quot;updateTime&quot;:1671185492000,&quot;ordernum&quot;:0,&quot;shardKey&quot;:&quot;5105&quot;,&quot;delFlag&quot;:&quot;0&quot;,&quot;implListId&quot;:4368119995872870400,&quot;referenceNum&quot;:&quot;人职发〔1993〕1号&quot;,&quot;ownId&quot;:4368119995872870400,&quot;deleted&quot;:false,&quot;taskCode&quot;:&quot;512B0361300039-510500000000-000-115104005656782570-3-00&quot;,&quot;lawName&quot;:&quot;关于印发〈经济专业技术资格考试暂行规定〉及其〈实施办法〉的通知&quot;,&quot;createTime&quot;:1671181763000,&quot;ctime&quot;:1671185492253,&quot;id&quot;:4368119996019671040,&quot;ownTag&quot;:&quot;1&quot;},{&quot;lawType&quot;:&quot;9&quot;,&quot;publishingDepartment&quot;:&quot;人力资源社会保障部&quot;,&quot;publishTime&quot;:1579190400000,&quot;isExistLaw&quot;:&quot;1&quot;,&quot;iid&quot;:&quot;f5c70ce3b1804623b1dcf5a1c111d8bf&quot;,&quot;year&quot;:2022,&quot;utime&quot;:1671185492324,&quot;thLrContentList&quot;:[{&quot;clause&quot;:&quot;《经济专业技术资格规定》第十二条&quot;,&quot;iid&quot;:&quot;2bd92823367b4e4eb8ddc5dfafb47f8c&quot;,&quot;utime&quot;:1671185497491,&quot;year&quot;:2022,&quot;updateTime&quot;:1671185497000,&quot;ordernum&quot;:0,&quot;overrideDefaulTime&quot;:false,&quot;shardKey&quot;:&quot;5105&quot;,&quot;delFlag&quot;:&quot;0&quot;,&quot;implListId&quot;:4368119995872870400,&quot;clauseContent&quot;:&quot;第十二条  初级、中级经济专业技术资格考试合格者，颁发人力资源社会保障部统一印制的经济专业技术资格证书，在全国范围内有效。人力资源管理和知识产权专业分别颁发相应级别的人力资源管理师证书和知识产权师证书。&quot;,&quot;lrId&quot;:4368119997957439488,&quot;deleted&quot;:false,&quot;taskCode&quot;:&quot;512B0361300039-510500000000-000-115104005656782570-3-00&quot;,&quot;createTime&quot;:1671181763000,&quot;ctime&quot;:1671185497491,&quot;id&quot;:4368119997965828096},{&quot;clause&quot;:&quot;《经济专业技术资格考试实施办法》第六条&quot;,&quot;iid&quot;:&quot;6f1efda50944418ab0a51dd10cfe5de1&quot;,&quot;utime&quot;:1671185497522,&quot;year&quot;:2022,&quot;updateTime&quot;:1671185498000,&quot;ordernum&quot;:0,&quot;overrideDefaulTime&quot;:false,&quot;shardKey&quot;:&quot;5105&quot;,&quot;delFlag&quot;:&quot;0&quot;,&quot;implListId&quot;:4368119995872870400,&quot;clauseContent&quot;:&quot;第六条  初级、中级经济专业技术资格考试成绩实行2年为一个周期的滚动管理方法，应试人员须在连续的两个考试年度内通过全部应试科目，方可取得相应级别经济专业技术资格证书。&quot;,&quot;lrId&quot;:4368119997957439488,&quot;deleted&quot;:false,&quot;taskCode&quot;:&quot;512B0361300039-510500000000-000-115104005656782570-3-00&quot;,&quot;createTime&quot;:1671181763000,&quot;ctime&quot;:1671185497522,&quot;id&quot;:4368119997965828097}],&quot;overrideDefaulTime&quot;:false,&quot;updateTime&quot;:1671185492000,&quot;ordernum&quot;:0,&quot;shardKey&quot;:&quot;5105&quot;,&quot;delFlag&quot;:&quot;0&quot;,&quot;implListId&quot;:4368119995872870400,&quot;referenceNum&quot;:&quot;人社部规[2020]1号&quot;,&quot;ownId&quot;:4368119997949050880,&quot;deleted&quot;:false,&quot;taskCode&quot;:&quot;512B0361300039-510500000000-000-115104005656782570-3-00&quot;,&quot;lawName&quot;:&quot;关于印发《经济专业技术资格规定》和《经济专业技术资格考试实施办法》的通知&quot;,&quot;createTime&quot;:1671181763000,&quot;ctime&quot;:1671185492324,&quot;id&quot;:4368119997957439488,&quot;ownTag&quot;:&quot;8&quot;},{&quot;lawType&quot;:&quot;9&quot;,&quot;publishTime&quot;:740592000000,&quot;isExistLaw&quot;:&quot;1&quot;,&quot;iid&quot;:&quot;f76b1a9e1f9d4195992e80c10f033327&quot;,&quot;year&quot;:2022,&quot;utime&quot;:1671185492266,&quot;thLrContentList&quot;:[{&quot;clause&quot;:&quot;第五条&quot;,&quot;iid&quot;:&quot;812e66cea9584015b3ec4905915253d6&quot;,&quot;utime&quot;:1671185497401,&quot;year&quot;:2022,&quot;updateTime&quot;:1671185497000,&quot;ordernum&quot;:0,&quot;overrideDefaulTime&quot;:false,&quot;shardKey&quot;:&quot;5105&quot;,&quot;delFlag&quot;:&quot;0&quot;,&quot;implListId&quot;:4368119995872870400,&quot;clauseContent&quot;:&quot;本规定第四条只适用于1993-1994年度组织的经济专业技术资格考试，其报考人员参加工作年限和担任专业职务年限的计算截止1994年12月31日。从1995年开始，除第一、二、三条规定外，一律按《暂行规定》执行。&quot;,&quot;lrId&quot;:4368119996233580547,&quot;deleted&quot;:false,&quot;taskCode&quot;:&quot;512B0361300039-510500000000-000-115104005656782570-3-00&quot;,&quot;createTime&quot;:1671181763000,&quot;ctime&quot;:1671185497401,&quot;id&quot;:4368119996246163456}],&quot;overrideDefaulTime&quot;:false,&quot;updateTime&quot;:1671185492000,&quot;ordernum&quot;:0,&quot;shardKey&quot;:&quot;5105&quot;,&quot;delFlag&quot;:&quot;0&quot;,&quot;implListId&quot;:4368119995872870400,&quot;referenceNum&quot;:&quot;人职发〔1993〕3号&quot;,&quot;ownId&quot;:4368119995872870400,&quot;deleted&quot;:false,&quot;taskCode&quot;:&quot;512B0361300039-510500000000-000-115104005656782570-3-00&quot;,&quot;lawName&quot;:&quot;关于印发〈经济专业技术资格考试报名条件的补充规定〉的通知&quot;,&quot;createTime&quot;:1671181763000,&quot;ctime&quot;:1671185492266,&quot;id&quot;:4368119996233580547,&quot;ownTag&quot;:&quot;1&quot;}]">
                            </div>
                            <div id="material">
                                <table>
                                    <tbody><tr>
                                        <td colspan="2" tabindex="180" class="text-tag">材料名称</td>
                                        <td tabindex="181" class="text-tag">材料必要性</td>
                                        <td tabindex="182" class="text-tag">材料类型</td>
                                        <td tabindex="183" class="text-tag">材料形式</td>
                                        <td tabindex="184" class="text-tag">纸质材料份数</td>
                                        <td tabindex="185" class="text-tag">表格下载</td>
                                        <td tabindex="186" class="text-tag">详情</td>
                                    </tr>
                                    <tr>
                                        <td tabindex="187" class="text-tag">1</td>
                                        <td tabindex="188" class="text-tag">身份证</td>
                                        <td tabindex="189" class="text-tag">必要</td>
                                        <td tabindex="190" class="text-tag">原件</td>
                                        <td tabindex="191" class="text-tag">纸质</td>
                                        <td tabindex="192" class="text-tag">1</td>
                                        <td>
                                            <a class="biao1" style="display:none"></a>
                                            <a class="biao2 text-tag" tabindex="193">示例样表</a>
                                        </td>
                                        <td>
                                            <a class="biao3 text-tag" tabindex="194">查看更多</a>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <!-- 申请资料结束 -->
                        </div>
                    </div>
                    <!--  受理条件  -->
                    <div class="section6" id="acceptance_conditions">
                        <div class="section6_top">
                            <span tabindex="240" class="text-tag">受理条件</span>
                        </div>
                        <p tabindex="241" class="text-tag">通过考试，拟取得合格证书 </p>
                    </div>
                    <!--  办理流程  -->
                    <div class="section7" id="handling_process">
                        <div class="section7_top">
                            <span tabindex="242" class="text-tag">办理流程</span>
                            <div class="section7_topright fr text-tag" tabindex="243">
                            </div>
                        </div>

                        <table>
                            <tbody><tr>
                                <td tabindex="244" class="text-tag">办理流程</td>
                                <td tabindex="245" class="text-tag">办理时长</td>
                                <td tabindex="246" class="text-tag">转外时限</td>
                                <td tabindex="247" class="text-tag">办理流程说明</td>
                            </tr>
                            <tr>
                                <td tabindex="248" class="text-tag">受理</td>
                                <td tabindex="249" class="text-tag">0个工作日</td>
                                <td tabindex="250" class="text-tag">0个工作日</td>
                                <td rowspan="3">
                                    <div class="content text-tag" tabindex="251">
                                        考生现场提供身份证原件或社保卡，核实身份后及时发放证书。
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td tabindex="252" class="text-tag">办结</td>
                                <td tabindex="253" class="text-tag">0个工作日</td>
                                <td tabindex="254" class="text-tag">0个工作日</td>
                            </tr>
                            </tbody></table>
                    </div>
                    <!--  收费标准  -->
                    <div class="section8" id="charging_standard">
                        <div class="section8_top">
                            <span tabindex="255" class="text-tag">收费标准</span>
                        </div>
                        <p style="line-height: 60px; background: #F5F6F8;margin-top: 20px;text-indent: 20px;" tabindex="256" class="text-tag">该事项无收费标准</p>
                    </div>
                    <!--  设定依据  -->
                    <div class="section9" id="setting_basis">
                        <div class="section9_top">
                            <span tabindex="257" class="text-tag">设定依据</span>
                        </div>
                        <table>
                            <tbody>
                                <tr>
                                    <td colspan="2" tabindex="268" class="text-tag">法律法规名称</td>
                                    <td tabindex="269" class="text-tag">法规类型</td>
                                    <td tabindex="270" class="text-tag">条例</td>
                                    <td tabindex="271" class="text-tag">条款内容</td>
                                </tr>
                                <tr>
                                    <td tabindex="272" class="text-tag">1</td>
                                    <td>
                                        <!-- 法律法规地址 -->
                                        <a class="lawUrl text-tag" href="http://www.sc.gov.cn/10462/zcwjk/zcwjk.shtml?title=关于调整经济专业技术资格考试专业设置的通知" target="_blank" tabindex="273">关于调整经济专业技术资格考试专业设置的通知</a>
                                    </td>
                                    <td tabindex="274" class="text-tag">规范性文件</td>
                                    <td tabindex="275" class="text-tag">第四条</td>
                                    <td>
                                        <a style="cursor: pointer" tabindex="276" class="text-tag">查看</a>
                                    </td>
                                </tr>
                                <tr>
                                    <td tabindex="277" class="text-tag">2</td>
                                    <td>
                                        <!-- 法律法规地址 -->
                                        <a class="lawUrl text-tag" href="http://www.sc.gov.cn/10462/zcwjk/zcwjk.shtml?title=关于印发〈经济专业技术资格考试暂行规定〉及其〈实施办法〉的通知" target="_blank" tabindex="278">关于印发〈经济专业技术资格考试暂行规定〉及其〈实施办法〉的通知</a>
                                    </td>
                                    <td tabindex="279" class="text-tag">规范性文件</td>
                                    <td tabindex="280" class="text-tag">第十四条</td>
                                    <td>
                                        <a style="cursor: pointer" tabindex="281" class="text-tag">查看</a>
                                    </td>
                                </tr>
                                <tr>
                                    <td tabindex="282" class="text-tag">3</td>
                                    <td>
                                        <!-- 法律法规地址 -->
                                        <a class="lawUrl text-tag" href="http://www.sc.gov.cn/10462/zcwjk/zcwjk.shtml?title=关于印发〈经济专业技术资格考试暂行规定〉及其〈实施办法〉的通知" target="_blank" tabindex="283">关于印发〈经济专业技术资格考试暂行规定〉及其〈实施办法〉的通知</a>
                                    </td>
                                    <td tabindex="284" class="text-tag">规范性文件</td>
                                    <td tabindex="285" class="text-tag">《经济专业技术资格考试暂行规定》第二条</td>
                                    <td>
                                        <a style="cursor: pointer" tabindex="286" class="text-tag">查看</a>
                                    </td>
                                </tr>
                                <tr>
                                    <td tabindex="287" class="text-tag">4</td>
                                    <td>
                                        <!-- 法律法规地址 -->
                                        <a class="lawUrl text-tag" href="http://www.sc.gov.cn/10462/zcwjk/zcwjk.shtml?title=关于印发〈经济专业技术资格考试暂行规定〉及其〈实施办法〉的通知" target="_blank" tabindex="288">关于印发〈经济专业技术资格考试暂行规定〉及其〈实施办法〉的通知</a>
                                    </td>
                                    <td tabindex="289" class="text-tag">规范性文件</td>
                                    <td tabindex="290" class="text-tag">第六条</td>
                                    <td>
                                        <a style="cursor: pointer" tabindex="291" class="text-tag">查看</a>
                                    </td>
                                </tr>
                                <tr>
                                    <td tabindex="292" class="text-tag">5</td>
                                    <td>
                                        <!-- 法律法规地址 -->
                                        <a class="lawUrl text-tag" href="http://www.sc.gov.cn/10462/zcwjk/zcwjk.shtml?title=关于印发〈经济专业技术资格考试报名条件的补充规定〉的通知" target="_blank" tabindex="293">关于印发〈经济专业技术资格考试报名条件的补充规定〉的通知</a>
                                    </td>
                                    <td tabindex="294" class="text-tag">规范性文件</td>
                                    <td tabindex="295" class="text-tag">第五条</td>
                                    <td>
                                        <a style="cursor: pointer" tabindex="296" class="text-tag">查看</a>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <!--  常见问题  -->
                    <div class="section11" id="common_problem">
                        <div class="section11_top">
                            <span tabindex="299" class="text-tag">常见问题</span>
                        </div>
                        <table>
                            <tbody><tr>
                                <td tabindex="300" class="text-tag">1</td>
                                <td>
                                    <span tabindex="301" class="text-tag">问:忘记用户名及密码该如何处理？</span>
                                    <p tabindex="302" class="text-tag">答:系统提供两种找回方式：验证预留安全问题和验证手机。</p>
                                </td>
                            </tr>
                            <tr>
                                <td tabindex="303" class="text-tag">2</td>
                                <td>
                                    <span tabindex="304" class="text-tag">问:注册信息中姓名及证件号码如何修改？</span>
                                    <p tabindex="305" class="text-tag">答:用户注册成功后，则不允许修改姓名及证件号码，如果注册姓名或证件号码有误，考生可重新注册。如果姓名有误，重新注册时，系统会提示“该证件号码在注册库中已经存在”，点击“确定”继续操作即可。</p>
                                </td>
                            </tr>
                            <tr>
                                <td tabindex="306" class="text-tag">3</td>
                                <td>
                                    <span tabindex="307" class="text-tag">问:找回密码成功后登入时仍提示用户名密码错误。</span>
                                    <p tabindex="308" class="text-tag">答:这类情况基本是用户名输入错误。请仔细核对输入的用户名与“密码修改”页面显示的用户名是否一致，注意区分用户名大小写。</p>
                                </td>
                            </tr>
                            </tbody></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
  export default {
    name: 'view',
    props: {},
    data() {
      return {
        offsetTop:100,
        openOrClose:false,
        fadeIn:false,
        currentScroll:0,
        scrollOffset:70
      }
    },
    methods: {
      toView() {
        const news = this.$router.resolve({ name: 'onlineBusiness' })
        window.open(news.href, '_blank')
      },
      openOrCloseChange(v){
        this.openOrClose = v;
        this.$forceUpdate();
      },
      handleScrollbox() {
        this.currentScroll = window.pageYOffset //表示当前滚动的位置
        if(!this.$refs.menuRef){
          return;
        }
        if (this.currentScroll > this.$refs.menuRef.offsetTop) {
          this.fadeIn = true;
        }else{
          this.fadeIn = false;
        }
      },
    },
    mounted() {
      window.addEventListener('scroll', this.handleScrollbox, true)
    }
  }
</script>

<style lang="scss" scoped>
    .view-service{
        .banner {
            position: relative;
        }
        .ma {
            margin: 0 auto;
            width: 1200px;
        }
        ol, ul, li {
            list-style: none;
        }
        .bannerbg1 {
            width: 100%;
            height: 355px;
            background: url(../../../../image/service/view/bannerbg.png) center top no-repeat;

            .fl {
                float: left;
            }
            .bannertop {
                padding-top: 40px;
                span {
                    font-size: 32px;
                    color: #FFFFFF;
                }
                p {
                    font-size: 17px;
                    color: #FFFFFF;
                    margin-top: 15px;
                    opacity: 0.9;
                }
            }
            .bannermmid {
                margin-top: 30px;
                height: 90px;
                ol, ul, li {
                    list-style: none;
                }
                li {
                    float: left;
                    span {
                        font-size: 42px;
                        color: #FFFFFF;
                    }
                    em {
                        font-size: 21px;
                        color: #FFFFFF;
                        padding-left: 8px;
                    }
                    i, em, b, strong {
                        font-style: normal;
                    }
                    p {
                        font-size: 15px;
                        color: #FFFFFF;
                        opacity: 0.7;
                        padding-top: 3px;
                    }
                }
                li:nth-child(2) {
                    margin-left: 50px;
                }
                li:nth-child(3) {
                    margin-left: 24px;
                }
                li:nth-child(4) {
                    margin-left: 60px;
                    width: 1px;
                    height: 80px;
                    background: rgba(255,255,255,0.30);
                }
                .manyi {
                    padding-left: 27px;
                    span {
                        display: inline-block;
                        width: 165px;
                        height: 40px;
                        line-height: 40px;
                        font-size: 17px;
                        color: #FFED04;
                        background: url(../../../../image/service/view/manyi_icon1.png) center center no-repeat;
                        padding-left: 48px;
                    }
                    p {
                        opacity: 0.7;
                        font-size: 15px;
                        color: #FFFFFF;
                        padding-top: 17px;
                    }
                }
            }
            .bannerbottom {
                padding-top: 40px;
                width: 550px;
                ol, ul, li {
                    list-style: none;
                }
                li {
                    float: left;
                    width: 120px;
                    height: 40px;
                    line-height: 40px;
                    text-align: center;
                    background: rgba(255,255,255,0.15);
                    border-radius: 25px;
                    cursor: pointer;
                }
                a {
                    font-size: 17px;
                    color: #FFFFFF;
                    width: 120px;
                    height: 40px;
                    line-height: 40px;
                    display: block;
                }
                li:not(:first-child) {
                    margin-left: 15px;
                }
            }
        }
        .bannerbg2 {
            width: 100%;
            height: 70px;
            background: #ffffff;
            top: 0;
            position: fixed;
            z-index: 100;
            box-shadow: 0 0 10px 0 rgb(153 153 153 / 10%);
            .bannerleft {
                height: 70px;
                line-height: 70px;
                span {
                    display: inline-block;
                    width: 750px;
                    font-size: 18px;
                    color: #333333;
                    font-weight: 900;
                    line-height: 20px;
                    vertical-align: middle;
                }
            }
            .bannerright {
                padding-top: 15px;
                ul li {
                    float: left;
                    width: 120px;
                    height: 40px;
                    line-height: 40px;
                    text-align: center;
                    background: #F4F6F8;
                    border-radius: 25px;
                    cursor: pointer;
                }
                li:not(:first-child) {
                    margin-left: 15px;
                }
            }
        }
        .sectionbg {
            background: #F5F6F8;
            padding: 22px 0 40px 0;
            .ma {
                margin: 0 auto;
                width: 1200px;
                display: flex;
            }
            .section-left {
                width: 240px;
                font-size: 19px;
            }
            .section_right {
                background: #ffffff;
                box-shadow: 0 0 10px 0 rgb(153 153 153 / 10%);
                padding: 20px 20px;
            }
            .section1_top {
                border-bottom: 1px solid #E8E8E8;
                padding-bottom: 10px;
            }
            .section1 span {
                font-size: 25px;
                color: #333333;
                font-weight: bold;
            }
            .section1 table tr td:nth-child(1), .section1 table tr td:nth-child(3) {
                width: 122px;
                height: 46px;
                background: #F5F6F8;
            }
            .section1 table tr td:nth-child(2), .section1 table tr td:nth-child(4) {
                width: 294px;
                height: 46px;
            }
            .section1 table td {
                border: 1px solid #E8E9EC;
                padding: 0 15px;
                font-size: 15px;
                color: #666666;
            }
            .section2_top {
                padding: 20px 0 15px 0;

                span {
                    font-size: 19px;
                    color: #333333;
                    font-weight: bold;
                }
            }
            .table2 {
                margin-top: 20px;
            }
            .open1 {
                width: 100%;
                height: 40px;
                text-align: center;
                padding-right: 60px;
                line-height: 40px;
                margin-top: 20px;
                background: url(../../../../image/service/view/open.png) 430px center no-repeat #F5F6F8;
                font-size: 15px;
                color: #666666;
                cursor: pointer;
            }
            .close1 {
                width: 100%;
                height: 40px;
                text-align: center;
                padding-right: 60px;
                line-height: 40px;
                margin-top: 20px;
                background: url(../../../../image/service/view/close.png) 430px center no-repeat #F5F6F8;
                font-size: 15px;
                color: #666666;
                cursor: pointer;
            }
            .section5 {
                padding-top: 25px;
            }
            .section5_top {
                border-bottom: 1px solid #E8E8E8;
                padding-bottom: 10px;
                span {
                    font-size: 25px;
                    color: #333333;
                    font-weight: bold;
                }
            }
            .section5>div>table {
                margin-top: 30px;
                width: 100%;
            }
            .section5>div>table tr:first-child {
                background: #F5F6F8;
                height: 46px;
                font-size: 15px;
                color: #666666;
                font-weight: bold;
                text-align: center;
            }
            .section5>div>table td {
                font-size: 15px;
                color: #666666;
                border: 1px solid #E8E9EC;
                padding: 12px 12px;
            }
            .section6 {
                padding-top: 25px;
                .section6_top {
                    border-bottom: 1px solid #E8E8E8;
                    padding-bottom: 10px;
                    span {
                        font-size: 25px;
                        color: #333333;
                        font-weight: bold;
                    }
                }
                p {
                    font-size: 15px;
                    color: #333333;
                    padding-top: 20px;
                }
            }
            .section7 {
                padding-top: 25px;
                .section7_top {
                    border-bottom: 1px solid #E8E8E8;
                    padding-bottom: 10px;
                    margin-bottom: 20px;
                    span {
                        font-size: 25px;
                        color: #333333;
                        font-weight: bold;
                        cursor: pointer;
                    }
                }
                tr {
                    width: 100%;
                }
                tr td:first-child {
                    width: 100px;
                    text-align: center;
                }
                td {
                    border: 1px solid #E8E9EC;
                    font-size: 15px;
                    color: #666666;
                    padding: 12px 15px;
                }
                .section7 tr:first-child {
                    background: #F5F6F8;
                    font-size: 15px;
                    color: #666666;
                    font-weight: bold;
                }
            }
            .section8 {
                padding-top: 25px;
                .section8_top {
                    border-bottom: 1px solid #E8E8E8;
                    padding-bottom: 10px;
                    span {
                        font-size: 25px;
                        color: #333333;
                        font-weight: bold;
                    }
                }
            }
            .section9 {
                padding-top: 25px;
                .section9_top {
                    border-bottom: 1px solid #E8E8E8;
                    padding-bottom: 10px;
                    span {
                        font-size: 25px;
                        color: #333333;
                        font-weight: bold;
                    }
                }
                tr:first-child {
                    background: #F5F6F8;
                    font-size: 15px;
                    color: #666666;
                    font-weight: bold;
                }
                tr td:nth-child(1) {
                    width: 30px;
                }
                tr td:nth-child(2) {
                    width: 323px;
                }
                td {
                    border: 1px solid #E8E9EC;
                    padding: 12px 15px;
                    font-size: 15px;
                    color: #666666;
                }
            }
            .section11 {
                padding-top: 25px;
                .section11_top {
                    border-bottom: 1px solid #E8E8E8;
                    padding-bottom: 10px;
                    span {
                        font-size: 25px;
                        color: #333333;
                        font-weight: bold;
                    }
                }
                tr td:first-child {
                    width: 50px;
                    background: #F5F6F8;
                    text-align: center;
                }
                td {
                    border: 1px solid #E8E9EC;
                    padding: 12px 15px;
                    font-size: 15px;
                    color: #666666;
                }
            }
        }
    }
    table {
        width: 100%;
        border-collapse: collapse;
        border-spacing: 0;
    }
</style>
