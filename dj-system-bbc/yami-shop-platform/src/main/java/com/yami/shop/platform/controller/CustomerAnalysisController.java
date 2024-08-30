/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.platform.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.yami.shop.bean.enums.RetainedDateType;
import com.yami.shop.bean.param.*;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.Arith;
import com.yami.shop.common.util.DateParam;
import com.yami.shop.common.util.DateUtils;
import com.yami.shop.dao.FlowPageAnalyseUserMapper;
import com.yami.shop.dao.OrderMapper;
import com.yami.shop.dao.UserExtensionMapper;
import com.yami.shop.platform.config.PlatformConstant;
import com.yami.shop.service.*;
import com.yami.shop.user.common.dao.UserLevelLogMapper;
import com.yami.shop.user.common.service.UserLevelLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * 顾客分析接口
 * @author
 */
@Api(tags = "客户分析接口")
@RestController
@RequestMapping("/platform/customerAnalysis")
@AllArgsConstructor
public class CustomerAnalysisController {

    private final UserService userService;
    private final UserExtensionService userExtensionService;
    private final UserExtensionMapper userExtensionMapper;
    private final UserLevelLogService userLevelLogService;
    private final UserLevelLogMapper userLevelLogMapper;
    private final OrderDataAnalysisService orderDataAnalysisService;
    private final OrderMapper orderMapper;
    private final FlowPageAnalyseUserMapper flowPageAnalyseUserMapper;
    private final CustomerAnalysisService customerAnalysisService;
    private final FlowLogService flowLogService;
    private final FlowUserAnalysisService flowUserAnalysisService;
    private final FlowService flowService;

    /**
     * 客户分析，客户概况及趋势(会员概览)
     */
    @ApiOperation(value = "客户分析，客户概况及趋势", notes = "客户分析，客户概况及趋势")
    @GetMapping("/getCustomerAnalysis")
    public ServerResponseEntity<CustomerRespParam> getCustomerAnalysis(CustomerReqParam param) {
        Date startTime = param.getStartTime();
        Date endTime = param.getEndTime();
        param.setAppId(1);
        // 访客数,统计时间类进入商城的所有访客
        Integer visitor;
        Integer preVisitor;
        if (Objects.equals(1,param.getDateType())) {
            flowLogService.getFlowLogListByCache();
            visitor = flowLogService.countAllVisitor(null,startTime,endTime);
            preVisitor = flowLogService.countAllVisitor(endTime,null,null);
        } else {
            visitor = flowPageAnalyseUserMapper.countAllVisitor(param.getEndTime(),null,null);
            preVisitor = visitor;
        }
        // 累积粉丝数 平台为商城所有的用户数；商家端为用户收藏该店铺的人数
        Integer allPersonNum = userExtensionMapper.countAllPersonNum(param.getEndTime(),null,null,null);
        Integer preAllPersonNum = userExtensionMapper.countAllPersonNum(param.getStartTime(),null,null,null);
        // 累积会员数
        CustomerRespParam respParam = userService.countUserByParam(param);
        // 成交客户数
        param.setStartTime(startTime);
        param.setEndTime(endTime);
        CustomerRespParam res = orderDataAnalysisService.countPayNum(param);
        respParam.setPayNum(res.getPayNum());
        respParam.setPrePayNum(res.getPrePayNum());
        respParam.setPayNumRate(res.getPayNumRate());
        respParam.setVisitor(visitor);
        respParam.setPreVisitor(preVisitor);
        respParam.setVisitorRate(divAverage(visitor-preVisitor,preVisitor,4));
        respParam.setMember(allPersonNum);
        respParam.setPreMember(preAllPersonNum);
        respParam.setMemberRate(divAverage(allPersonNum-preAllPersonNum,preAllPersonNum,4));
        param.setStartTime(startTime);
        param.setEndTime(endTime);
        List<CustomerPayParam> levelParams = userLevelLogMapper.countGrowthMemberByTime(param);
        respParam.setFashNum(userLevelLogMapper.countPayUserByTime(param));
        Map<Date, Integer> levelMap = levelParams.stream().collect(toMap(CustomerPayParam::getCreateDate, CustomerPayParam::getNumber));
        respParam.setCustomerTrend(orderDataAnalysisService.countCustomerParam(param, levelMap));
        if (Objects.equals(1, param.getDateType())) {
            for (CustomerPayParam customerPayParam : respParam.getCustomerTrend()) {
                customerPayParam.setVisitor(respParam.getVisitor());
            }
        }
        return ServerResponseEntity.success(respParam);
    }

    /**
     * 微信粉丝统计
     *  累积粉丝数：截至到筛选时间的最后一天，店铺的微信粉丝累积人数
     *  新增粉丝数： 筛选时间内，新关注的粉丝去重人数
     *  跑路粉丝数：筛选时间内，取消关注的粉丝去重人数
     *  净增粉丝数：筛选时间内，实际增长的粉丝人数，新增粉丝数减去跑路粉丝数
     */
    @ApiOperation(value = "微信粉丝统计,暂时不统计", notes = "客户分析，微信粉丝统计")
    @GetMapping("/getCustomerWxFash")
    public ServerResponseEntity<?> getCustomerWxFash(CustomerReqParam param) {
        // 暂不统计，bbc小程序没有店铺关注的接口，b2c 暂不适用，
        return ServerResponseEntity.success(null);
    }

    /**
     * 会员统计 ,商城分成普通会员和付费会员，普通会员注册就是普通会员
     * 累积会员数：截至到筛选时间的最后一天，店铺的会员累积人数
     * 新增会员数： 筛选时间内，通过领取会员卡，新成为会员的客户数量
     * 升级会员数：筛选时间内，通过会员规则升级的会员数量， 一人多次升级记为一人
     * 储值会员数：筛选时间内，进行储值的会员数量，一人多次储值记为一人。我们商城没有储值设计,
     *              将储值会员数，改为付费会员数
     */
    @ApiOperation(value = "会员统计", notes = "客户分析，会员统计")
    @GetMapping("/getMemberCount")
    public ServerResponseEntity<CustomerMemberRespParam> getMemberCount(MemberReqParam param) {
        Date startTime = param.getStartTime();
        Date endTime = param.getEndTime();
        CustomerMemberRespParam respParam = new CustomerMemberRespParam();
        // 会员统计
        CustomerMemberParam res = userLevelLogService.countMemberNum(param);
        respParam.setMemberCount(res);
        // 会员活跃
        param.setStartTime(startTime);
        param.setEndTime(endTime);
        CustomerMemberLivelyParam memberLivelyParam = getCustomerMemberLively(param);
        memberLivelyParam.setMemberVRate(divAverage(memberLivelyParam.getMemberV(),res.getAccumulate(),4));
        respParam.setMemberLively(memberLivelyParam);
        return ServerResponseEntity.success(respParam);
    }
    private CustomerMemberLivelyParam getCustomerMemberLively(CustomerReqParam param) {
        CustomerMemberLivelyParam res = new CustomerMemberLivelyParam();
        List<DateParam> everyDays = DateUtils.findEveryDays(param.getStartTime(), param.getEndTime());
        param.setDateTime(null);
        Integer num = 0;
        Integer addCartUserNum = 0;
        if(Objects.equals(1,param.getDateType())) {
            num = flowLogService.countUserNum(param.getStartTime(), param.getEndTime());
            addCartUserNum = flowLogService.countAddCartUserNum(param.getStartTime(), param.getEndTime());
        } else {
            num = userExtensionMapper.countVisitMemberNum(param.getDateTime(), param.getStartTime(), param.getEndTime());
            addCartUserNum = flowUserAnalysisService.countAddCartUserNum(param.getStartTime(), param.getEndTime());
        }
        // 访问会员数
        res.setMemberV(num);
        Integer allPersonNum = userExtensionMapper.countAllPersonNum(param.getEndTime(), null, null,null);
        res.setMemberVRate(divAverage(res.getMemberV(),allPersonNum,4));
        // 加购会员数
        res.setMemberAddCat(addCartUserNum);
        res.setMemberAddCatRate(divAverage(res.getMemberAddCat(),res.getMemberV(),4));
        // 领券会员数
//        res.setMemberGetCoupon(couponUserService.countMemberGetCoupon(param));
        res.setMemberGetCouponRate(divAverage(res.getMemberGetCoupon(),res.getMemberV(),4));

        // 成交会员数
        res.setMemberPay(orderDataAnalysisService.countMemberPayNum(param));
        res.setMemberPayRate(divAverage(res.getMemberPay(),res.getMemberV(),4));
        List<CustomerMemberLivelyTrendParam> resList = new ArrayList<>();
        for (DateParam everyDay : everyDays) {
            CustomerMemberLivelyTrendParam liveParam = new CustomerMemberLivelyTrendParam();
            param.setStartTime(everyDay.getStartTime());
            param.setEndTime(everyDay.getEndTime());
            liveParam.setCurrentDay(DateUtils.dateToStrYmd(param.getEndTime()));
            // 访问会员数、加购会员数
            if(Objects.equals(1,param.getDateType())) {
                liveParam.setMemberV(num);
                liveParam.setMemberAddCat(addCartUserNum);
            } else {
                liveParam.setMemberV(userExtensionMapper.countVisitMemberNum(null,param.getStartTime(),param.getEndTime()));
                liveParam.setMemberAddCat(userExtensionMapper.countAddCartMemberNum(null,param.getStartTime(),param.getEndTime()));
            }
            // 领券会员数
//            liveParam.setMemberGetCoupon(couponUserService.countMemberGetCoupon(param));
            // 成交会员数
            liveParam.setMemberPay(orderDataAnalysisService.countMemberPayNum(param));
            resList.add(liveParam);
        }
        res.setMemberLivelyTrend(resList);
        return res;
    }

    /**
     * 客户分析，成交客户分析
     */
    @ApiOperation(value = "客户分析，成交客户分析", notes = "客户分析，成交客户分析")
    @GetMapping("/getCustomerDeal")
    public ServerResponseEntity<CustomerDealRespParam> getCustomerDeal(CustomerReqParam param) {
        CustomerDealRespParam res = orderDataAnalysisService.getCustomerDeal(param);
        return ServerResponseEntity.success(res);
    }

    /**
     * 客户分析，客户留存分析
     */
    @ApiOperation(value = "客户分析，客户留存分析",notes = "客户分析，客户留存分析,不做周留存数据")
    @GetMapping("/getCustomerRetained")
    public ServerResponseEntity<List<CustomerRetainRespParam>> refreshRetainedData(CustomerRetainedReqParam param) {
        Integer dateType = param.getDateType();
        Integer dateRetainType = param.getDateRetainType();
        List<CustomerRetainRespParam> respParam = new ArrayList<>();
        if (Objects.equals(1,dateType) && !Objects.equals(1,dateRetainType)) {
            // 最近一月，月留存。此时不显示数据
            return ServerResponseEntity.success(respParam);
        }
        Integer retainType = param.getRetainType();
        if (Objects.equals(1,retainType)) {
            // 访问留存
            respParam = getVisitRetained(param);
        } else if (Objects.equals(PlatformConstant.TWO,retainType)) {
            // 成交留存
            respParam = getTradeRetained(param);
        }
        return ServerResponseEntity.success(respParam);
    }


    /**
     * 客户分析，客户留存分析
     */
    @ApiOperation(value = "客户分析，客户留存分析,刷新数据缓存",notes = "刷新数据缓存，重新拉取数据")
    @GetMapping("/refreshRetainedData")
    public ServerResponseEntity<List<CustomerRetainRespParam>> refreshRetainedData() {
        flowService.refreshRetainedData();
        return ServerResponseEntity.success();
    }
    private List<CustomerRetainRespParam> getVisitRetained(CustomerRetainedReqParam param) {
        // 访问留存，包含未注册了的用户访问留存数
        List<CustomerRetainRespParam> list = flowUserAnalysisService.getVisitRetained(param);
        list = retainedAnalysis(list, param);
        return list;
    }

    private List<CustomerRetainRespParam> getTradeRetained(CustomerRetainedReqParam param) {
        List<CustomerRetainRespParam> list = orderDataAnalysisService.getTradeRetained(param);
        list = retainedAnalysis(list, param);
        return list;
    }

    private List<CustomerRetainRespParam> retainedAnalysis(List<CustomerRetainRespParam> list,CustomerRetainedReqParam param) {
        // 不在sql中统计了，在代码中来计算留存率
        // 因为最多就统计一年的留存数据,最多12行数据
        // 保留位数
        int scale = 2;
        // 放大倍数
        Integer percentage = 100;
        int size = list.size();
        int avgNewCustomers = 0;
        int avgFirstMonthRemain = 0;
        int avgSecondMonthRemain = 0;
        int avgThirdMonthRemain = 0;
        int avgFourthMonthRemain = 0;
        int avgFifthMonthRemain = 0;
        int avgSixthMonthRemain = 0;
        // 可能有一些不存在的数据
        List<String> rangeDate = getRangeDate(param.getStartTime(), param.getEndTime());
        List<String> collect = list.stream().map(CustomerRetainRespParam::getCurrentMonth).collect(Collectors.toList());
        rangeDate.removeAll(collect);
        if (CollUtil.isNotEmpty(rangeDate)) {
            // 初始化未有新增月份的情况
            List<CustomerRetainRespParam> customerRetainRespParams = initCustomerRetain(rangeDate);
            list.addAll(customerRetainRespParams);
            list = list.stream().sorted(Comparator.comparing(CustomerRetainRespParam::getCurrentMonth)).collect(Collectors.toList());
        }
        for (CustomerRetainRespParam customerRetain : list) {
            Integer newCustomers = customerRetain.getNewCustomers();
            avgNewCustomers += newCustomers;
            Integer firstMonthRemain = customerRetain.getFirstMonthRemain();
            avgFirstMonthRemain += firstMonthRemain;
            customerRetain.setFirstMonthRemainRate(Arith.calculatePercentage(firstMonthRemain,newCustomers,scale, percentage));
            Integer secondMonthRemain = customerRetain.getSecondMonthRemain();
            avgSecondMonthRemain += secondMonthRemain;
            customerRetain.setSecondMonthRemainRate(Arith.calculatePercentage(secondMonthRemain,newCustomers,scale, percentage));
            Integer thirdMonthRemain = customerRetain.getThirdMonthRemain();
            avgThirdMonthRemain += thirdMonthRemain;
            customerRetain.setThirdMonthRemainRate(Arith.calculatePercentage(thirdMonthRemain,newCustomers,scale, percentage));
            Integer fourthMonthRemain = customerRetain.getFourthMonthRemain();
            avgFourthMonthRemain += fourthMonthRemain;
            customerRetain.setFourthMonthRemainRate(Arith.calculatePercentage(fourthMonthRemain,newCustomers,scale, percentage));
            Integer fifthMonthRemain = customerRetain.getFifthMonthRemain();
            avgFifthMonthRemain += fifthMonthRemain;
            customerRetain.setFifthMonthRemainRate(Arith.calculatePercentage(fifthMonthRemain,newCustomers,scale, percentage));
            Integer sixthMonthRemain = customerRetain.getSixthMonthRemain();
            avgSixthMonthRemain += sixthMonthRemain;
            customerRetain.setSixthMonthRemainRate(Arith.calculatePercentage(sixthMonthRemain,newCustomers,scale, percentage));
        }

        // 平均留存率
        CustomerRetainRespParam avgCustomerRetain = new CustomerRetainRespParam();
        avgNewCustomers = ceil(avgNewCustomers,size);
        avgCustomerRetain.setCurrentMonth(I18nMessage.getMessage("yami.average.retention.rate"));
        avgCustomerRetain.setNewCustomers(avgNewCustomers);
        avgCustomerRetain.setFirstMonthRemain(ceil(avgFirstMonthRemain,size));
        avgCustomerRetain.setFirstMonthRemainRate(Arith.calculatePercentage(avgCustomerRetain.getFirstMonthRemain(),avgNewCustomers,scale, percentage));
        avgCustomerRetain.setSecondMonthRemain(ceil(avgSecondMonthRemain,size));
        avgCustomerRetain.setSecondMonthRemainRate(Arith.calculatePercentage(avgCustomerRetain.getSecondMonthRemain(),avgNewCustomers,scale, percentage));
        avgCustomerRetain.setThirdMonthRemain(ceil(avgThirdMonthRemain,size));
        avgCustomerRetain.setThirdMonthRemainRate(Arith.calculatePercentage(avgCustomerRetain.getThirdMonthRemain(),avgNewCustomers,scale, percentage));
        avgCustomerRetain.setFourthMonthRemain(ceil(avgFourthMonthRemain,size));
        avgCustomerRetain.setFourthMonthRemainRate(Arith.calculatePercentage(avgCustomerRetain.getFourthMonthRemain(),avgNewCustomers,scale, percentage));
        avgCustomerRetain.setFifthMonthRemain(ceil(avgFifthMonthRemain,size));
        avgCustomerRetain.setFifthMonthRemainRate(Arith.calculatePercentage(avgCustomerRetain.getFifthMonthRemain(),avgNewCustomers,scale, percentage));
        avgCustomerRetain.setSixthMonthRemain(ceil(avgSixthMonthRemain,size));
        avgCustomerRetain.setSixthMonthRemainRate(Arith.calculatePercentage(avgCustomerRetain.getSixthMonthRemain(),avgNewCustomers,scale, percentage));
        list.add(avgCustomerRetain);
        return list;
    }

    private List<CustomerRetainRespParam> initCustomerRetain(List<String> rangeDate) {
        if (CollUtil.isEmpty(rangeDate)) {
            return Collections.emptyList();
        }
        List<CustomerRetainRespParam> list = new ArrayList<>();
        BigDecimal zero = new BigDecimal("0");
        for (String date : rangeDate) {
            CustomerRetainRespParam customerRetainRespParam = new CustomerRetainRespParam();
            customerRetainRespParam.setCurrentMonth(date);
            customerRetainRespParam.setNewCustomers(0);
            customerRetainRespParam.setFirstMonthRemain(0);
            customerRetainRespParam.setFirstMonthRemainRate(zero);
            customerRetainRespParam.setSecondMonthRemain(0);
            customerRetainRespParam.setSecondMonthRemainRate(zero);
            customerRetainRespParam.setThirdMonthRemain(0);
            customerRetainRespParam.setThirdMonthRemainRate(zero);
            customerRetainRespParam.setFourthMonthRemain(0);
            customerRetainRespParam.setFourthMonthRemainRate(zero);
            customerRetainRespParam.setFifthMonthRemain(0);
            customerRetainRespParam.setFifthMonthRemainRate(zero);
            customerRetainRespParam.setSixthMonthRemain(0);
            customerRetainRespParam.setSixthMonthRemainRate(zero);
            list.add(customerRetainRespParam);
        }
        return list;
    }

    private List<String> getRangeDate(Date startTime, Date endTime) {
        List<DateTime> dateTimes = DateUtil.rangeToList(DateUtil.beginOfMonth(startTime), endTime, DateField.MONTH);
        List<String> list = new ArrayList<>();
        for (DateTime dateTime : dateTimes) {
            list.add(DateUtil.format(dateTime,"yyyy-MM"));
        }
        // 控制月在最近 3 、6、 12个月
        int size = list.size();
        boolean isReduce = size == RetainedDateType.THREE_MONTH.getMonthNum() + 1 ||
                           size == RetainedDateType.SIX_MONTH.getMonthNum() + 1 ||
                           size == RetainedDateType.ONE_YEAR.getMonthNum() + 1;
        if (isReduce) {
            list.remove(list.get(0));
        }
        return list;
    }

    /**
     * @param a dividend
     * @param b divisor
     * @return  向上取整
     */
    private int ceil(int a, int b) {
        if (a==0 || b==0) {
            return 0;
        }
        BigDecimal dividend = new BigDecimal(Integer.toString(a));
        BigDecimal divisor = new BigDecimal(Integer.toString(b));
        BigDecimal divide = dividend.divide(divisor,0,BigDecimal.ROUND_UP);
        return divide.intValue();
    }


    // ------------------------------------------------------------------------------------------------
    // 客户洞察

    /**
     * 客户洞察，RFM模型分析
     */
    @ApiOperation(value = "客户洞察，RFM模型分析", notes = "客户洞察，RFM模型分析")
    @GetMapping("/getCustomerRFM")
    public ServerResponseEntity<List<CustomerRFMRespParam>> getCustomerRfm(CustomerRFMReqParam param) {
        Date recentTime = param.getRecentTime();
        if (Objects.isNull(recentTime)) {
            // 请选择起止时间
            throw new YamiShopBindException("yami.form.time.check");
        }
        List<CustomerRFMRespParam> list = orderDataAnalysisService.countPayNumRfm(param);
        return ServerResponseEntity.success(list);
    }
    /**
     * 客户洞察，RFM模型分析
     * 目前设计是只统计最近两年时间的数据
     */
    @ApiOperation(value = "客户洞察，RFM模型分析", notes = "客户洞察，RFM模型分析,直接返回表格数据，最最后一行合计行除外")
    @GetMapping("/getCustomerRFMSecond")
    public ServerResponseEntity<List<CustomerRFMRespTableParam>> getCustomerRfmSecond(CustomerRFMReqParam param) {
        Date recentTime = param.getRecentTime();
        if (Objects.isNull(recentTime)) {
            // 请选择起止时间
            throw new YamiShopBindException("yami.form.time.check");
        }
        List<CustomerRFMRespTableParam> dateList = orderDataAnalysisService.countPayNumRfm2(param);

        //最后一行的统计数据
        CustomerRFMRespTableParam statistics = new CustomerRFMRespTableParam();
        statistics.setRecencyName("列合计");
        //Map<recency(消费时间), CustomerRFMRespTableParam>
        List<CustomerRFMRespTableParam> list = new ArrayList<>();
        Map<Integer, List<CustomerRFMRespTableParam>> collect = dateList.stream().filter(a->!Objects.equals(a.getRecency(), 6)).collect(Collectors.groupingBy(CustomerRFMRespTableParam::getRecency));
        String[] recencyNameArray = {"","R<=30","30<R<=90","90<R<=180","180<R<=365","R > 365"};
        Integer userNum = 0;
        Double amount = 0.0;
        for (int i=1;i <= PlatformConstant.FIVE;i++){
            CustomerRFMRespTableParam customer = new CustomerRFMRespTableParam();
            customer.setRecencyName(recencyNameArray[i]);
            if(collect.containsKey(i)){
                List<CustomerRFMRespTableParam> customerRfmRespTableList = collect.get(i);
                for (CustomerRFMRespTableParam rfm:customerRfmRespTableList) {
                    calculation(statistics, customer, rfm);
                    userNum += rfm.getUserNum();
                    amount = Arith.add(amount,rfm.getAmount());
                }
            }
            list.add(customer);
        }
        //最后一列的数据统计
        statistics.setPayBuyersTotal(userNum);
        statistics.setPayAmountTotal(amount);
        statistics.setPriceSingle1(divAverage(statistics.getPayAmount1(),statistics.getPayBuyers1(),2));
        statistics.setPriceSingle2(divAverage(statistics.getPayAmount2(),statistics.getPayBuyers2(),2));
        statistics.setPriceSingle3(divAverage(statistics.getPayAmount3(),statistics.getPayBuyers3(),2));
        statistics.setPriceSingle4(divAverage(statistics.getPayAmount4(),statistics.getPayBuyers4(),2));
        statistics.setPriceSingle5(divAverage(statistics.getPayAmount5(),statistics.getPayBuyers5(),2));
        list.add(statistics);
        for (CustomerRFMRespTableParam tableParam : list) {
            tableParam.setPayBuyers1Rate(divAverage(tableParam.getPayBuyers1(),tableParam.getPayBuyersTotal(),4));
            tableParam.setPayBuyers2Rate(divAverage(tableParam.getPayBuyers2(),tableParam.getPayBuyersTotal(),4));
            tableParam.setPayBuyers3Rate(divAverage(tableParam.getPayBuyers3(),tableParam.getPayBuyersTotal(),4));
            tableParam.setPayBuyers4Rate(divAverage(tableParam.getPayBuyers4(),tableParam.getPayBuyersTotal(),4));
            tableParam.setPayBuyers5Rate(divAverage(tableParam.getPayBuyers5(),tableParam.getPayBuyersTotal(),4));
            tableParam.setPayBuyersTotalRate(divAverage(tableParam.getPayBuyersTotal(),userNum,4));
            tableParam.setPriceSingleTotal(divAverage(tableParam.getPayAmountTotal(),tableParam.getPayBuyersTotal(),2));
        }
        return ServerResponseEntity.success(list);
    }

    private void calculation(CustomerRFMRespTableParam statistics, CustomerRFMRespTableParam customer, CustomerRFMRespTableParam rfm) {
        Integer frequency = rfm.getFrequency();
        if (frequency == 1) {
            customer.setPayBuyers1(rfm.getUserNum());
            customer.setPayAmount1(rfm.getAmount());
            customer.setPriceSingle1(divAverage(customer.getPayAmount1(),customer.getPayBuyers1(),2));
            statistics.setPayBuyers1(statistics.getPayBuyers1() + rfm.getUserNum());
            statistics.setPayAmount1(Arith.add(statistics.getPayAmount1(), rfm.getAmount()));
        } else if (frequency == PlatformConstant.TWO) {
            customer.setPayBuyers2(rfm.getUserNum());
            customer.setPayAmount2(rfm.getAmount());
            customer.setPriceSingle2(divAverage(customer.getPayAmount2(),customer.getPayBuyers2(),2));
            statistics.setPayBuyers2(statistics.getPayBuyers2() + rfm.getUserNum());
            statistics.setPayAmount2(Arith.add(statistics.getPayAmount2(), rfm.getAmount()));
        } else if (frequency == PlatformConstant.THREE) {
            customer.setPayBuyers3(rfm.getUserNum());
            customer.setPayAmount3(rfm.getAmount());
            customer.setPriceSingle3(divAverage(customer.getPayAmount3(),customer.getPayBuyers3(),2));
            statistics.setPayBuyers3(statistics.getPayBuyers3() + rfm.getUserNum());
            statistics.setPayAmount3(Arith.add(statistics.getPayAmount3(), rfm.getAmount()));
        } else if (frequency == PlatformConstant.FOUR) {
            customer.setPayBuyers4(rfm.getUserNum());
            customer.setPayAmount4(rfm.getAmount());
            customer.setPriceSingle4(divAverage(customer.getPayAmount4(),customer.getPayBuyers4(),2));
            statistics.setPayBuyers4(statistics.getPayBuyers4() + rfm.getUserNum());
            statistics.setPayAmount4(Arith.add(statistics.getPayAmount4(), rfm.getAmount()));
        } else {
            customer.setPayBuyers5(rfm.getUserNum());
            customer.setPayAmount5(rfm.getAmount());
            customer.setPriceSingle5(divAverage(customer.getPayAmount5(),customer.getPayBuyers5(),2));
            statistics.setPayBuyers5(statistics.getPayBuyers5() + rfm.getUserNum());
            statistics.setPayAmount5(Arith.add(statistics.getPayAmount5(), rfm.getAmount()));
        }
        customer.setPayBuyersTotal(customer.getPayBuyersTotal() + rfm.getUserNum());
        customer.setPayAmountTotal(Arith.add(customer.getPayAmountTotal(), rfm.getAmount()));
    }

    /**
     * 客户洞察，消费能力分析
     */
    @ApiOperation(value = "客户洞察，消费能力分析", notes = "客户洞察，消费能力分析")
    @GetMapping("/getConsumePower")
    public ServerResponseEntity<CustomerConsumeRespParam> getConsumePower(CustomerConsumeReqParam param) {
        CustomerConsumeRespParam  respParam = orderDataAnalysisService.getConsumePower(param);
        return ServerResponseEntity.success(respParam);
    }
    /**
     * 客户洞察，消费频次分析
     */
    @ApiOperation(value = "客户洞察，消费频次分析", notes = "客户洞察，消费频次分析")
    @GetMapping("/getConsumeFrequency")
    public ServerResponseEntity<CustomerConsumeRespParam> getConsumeFrequency(CustomerConsumeReqParam param) {
        CustomerConsumeRespParam  respParam = orderDataAnalysisService.getConsumeFrequency(param);
        return ServerResponseEntity.success(respParam);
    }

    /**
     * 客户洞察，回购周期分析
     */
    @ApiOperation(value = "客户洞察，回购周期分析", notes = "客户洞察，回购周期分析")
    @GetMapping("/getConsumeRepurchaseCount")
    public ServerResponseEntity<CustomerRepurchaseRespParam> getConsumeRepurchaseCount(CustomerConsumeReqParam param) {
        CustomerRepurchaseRespParam respParam = orderDataAnalysisService.getConsumeRepurchaseCount(param);
        return ServerResponseEntity.success(respParam);
    }
    // -----------------------------------------------------------------------------------------------
    // 会员分析 CustomerReqParam
    /**
     * 会员分析，会员概况
     */
    @ApiOperation(value = "会员分析，会员概况", notes = "会员分析，会员概况")
    @GetMapping("/getMemberSurvey")
    public ServerResponseEntity<MemberSurveyRespParam> getMemberSurvey(MemberReqParam param) {
        MemberSurveyRespParam customerRespParam = customerAnalysisService.generalize(param);
//        MemberSurveyRespParam res = getMemberAnalysisSurvey(param);
        customerRespParam.setMemberOverviewModelList(getMemberAnalysisSurvey(param));
        return ServerResponseEntity.success(customerRespParam);
    }

    private List<MemberOverviewListParam> getMemberAnalysisSurvey(MemberReqParam param) {
        Date startTime = param.getStartTime();
        Date endTime = param.getEndTime();
        List<DateParam> everyDays = DateUtils.findEveryDays(startTime,endTime);
        MemberSurveyRespParam respParam = new MemberSurveyRespParam();
        MemberOverviewParam resParam = new MemberOverviewParam();
        List<MemberOverviewListParam> resList = new ArrayList<>();
        if (Objects.isNull(param.getMemberType())){
            param.setMemberType(0);
        }
        if (everyDays.size() == 1 ) {
            Date beforeDate = DateUtils.getBeforeDate(everyDays.get(0).getStartTime());
            everyDays = DateUtils.findEveryDays(beforeDate, param.getEndTime());
        }
        for (DateParam everyDay : everyDays) {
            MemberOverviewListParam res = new MemberOverviewListParam();
            // 累积会员数
            param.setDateTime(everyDay.getEndTime());
            res.setCurrentDay(DateUtils.dateToNumber(everyDay.getEndTime()));
            res.setTotalMember(userExtensionMapper.countMemberByParam(param));
            // 新增会员数
            param.setDateTime(everyDay.getStartTime());
            Integer preAccumulate = userExtensionMapper.countMemberByParam(param);
            res.setNewMember(res.getTotalMember() - preAccumulate);
            // 支付会员数，再筛选时间内，购买商品的会员人数
            param.setDateTime(null);
            param.setStartTime(everyDay.getStartTime());
            param.setEndTime(everyDay.getEndTime());
            res.setPayMember(orderMapper.countPaidMemberByParam(param,null));
            // 领券会员数
//            res.setCouponMember(couponUserService.countMemberCouponByParam(param));
            // 会员支付金额
            res.setMemberPayAmount(handleDouble(orderMapper.countMemberPaidAmount(param)));
            // 会员支付订单数
            res.setMemberPayOrder(orderMapper.countMemberPayOrder(param));
            // 会员客单价
            Integer payMemberNum = orderMapper.countPayMemberNum(param);
            res.setMemberSingleAmount(divAverage(res.getMemberPayAmount(),payMemberNum,2));
            resList.add(res);
        }
        return resList;
//        respParam.setMemberOverviewModelList(resList);
//        MemberOverviewListParam first = resList.get(0);
//        MemberOverviewListParam last = resList.get(resList.size() - 1);
//        resParam.setCurrentDay(DateUtils.dateToNumber(endTime));
//        resParam.setTotalMember(last.getTotalMember());
//        resParam.setTotalMemberRate(divAverage(last.getTotalMember()-first.getTotalMember(),first.getTotalMember(),4));
//        resParam.setNewMember(last.getNewMember());
//        resParam.setNewMemberRate(divAverage(last.getNewMember(),first.getNewMember(),4));
//        //
//        param.setDateTime(endTime);
//        param.setStartTime(null);
//        resParam.setPayMember(orderMapper.countPaidMemberByParam(param));
//        // 会员支付金额
//        resParam.setMemberPayAmount(handleDouble(orderMapper.countMemberPaidAmount(param)));
//        // 领券会员数
//        resParam.setCouponMember(couponUserService.countMemberCouponByParam(param));
//        // 会员支付订单数
//        resParam.setMemberPayOrder(orderMapper.countMemberPayOrder(param));
//        // 会员客单价
//        Integer payMemberNum = orderMapper.countPayMemberNum(param);
//        resParam.setMemberSingleAmount(divAverage(resParam.getMemberPayAmount(),payMemberNum,2));
//
//        param.setDateTime(startTime);
//        Integer prePayMember = orderMapper.countPaidMemberByParam(param);
//        Double prePaidAmount = handleDouble(orderMapper.countMemberPaidAmount(param));
//        Integer preMemberCoupon = couponUserService.countMemberCouponByParam(param);
//        Integer prePayOrder = orderMapper.countMemberPayOrder(param);
//        Integer payMemberNum1 = orderMapper.countPayMemberNum(param);
//        resParam.setPayMemberRate(divAverage(resParam.getPayMember()-prePayMember,prePayMember,4));
//        resParam.setMemberPayAmountRate(divAverage(Arith.sub(resParam.getMemberPayAmount(),prePaidAmount),prePaidAmount,4));
//        resParam.setCouponMemberRate(divAverage(resParam.getCouponMember()-preMemberCoupon,preMemberCoupon,4));
//        resParam.setMemberPayOrderRate(divAverage(resParam.getMemberPayOrder()-prePayOrder,prePayOrder,4));
//        resParam.setMemberSingleAmountRate(divAverage(payMemberNum1-payMemberNum,payMemberNum,4));
//        respParam.setMemberOverviewModel(resParam);
//        return respParam;
    }


    /**
     * 会员分析，会员升级分析
     */
    @ApiOperation(value = "会员分析，会员升级分析", notes = "会员分析，会员升级分析")
    @GetMapping("/getGrowthMember")
    public ServerResponseEntity<List<MemberGrowthDetailParam>> getGrowthMember(MemberGrowthReqParam param) {
        List<MemberGrowthDetailParam> res = userLevelLogMapper.countGrowthMemberByParam(param);
        if (CollectionUtils.isEmpty(res)) {
            return ServerResponseEntity.success(null);
        }
        Integer total = res.stream().collect(Collectors.summingInt(MemberGrowthDetailParam::getMemberNum));

        for (MemberGrowthDetailParam re : res) {
            re.setRate(divAverage(re.getMemberNum(),total,4));
        }
        return ServerResponseEntity.success(res);
    }

    /**
     * // bbc平台/b2c商家 接口
     * 会员分析，会员人数趋势/ 会员占比趋势
     */
    @ApiOperation(value = "会员分析，会员人数趋势/ 会员占比趋势", notes = "会员分析，会员人数趋势/ 会员占比趋势")
    @GetMapping("/getMemberTrend")
    public ServerResponseEntity<List<MemberTrendRespParam>> getMemberTrend(MemberReqParam param) {
        // 筛选时间类的每一天注册的会员数数据，不是每一天平台的总会员数
        List<MemberTrendRespParam> respParam = userExtensionService.getMemberTrend(param);
        return ServerResponseEntity.success(respParam);
    }

    /**
     * // bbc平台/b2c商家 接口
     * 会员分析，会员贡献价值分析
     */
    @ApiOperation(value = "会员分析，会员贡献价值分析", notes = "会员分析，会员贡献价值分析")
    @GetMapping("/getMemberContributeValue")
    public ServerResponseEntity<MemberContributeRespParam> getMemberContributeValue(MemberReqParam param) {
        MemberContributeRespParam respParam = orderDataAnalysisService.getMemberContributeValue(param);
        return ServerResponseEntity.success(respParam);
    }

    /**
     * // bbc平台/b2c商家 接口
     * 会员分析，新老会员成交分析
     */
    @GetMapping("/getMemberDeal")
    @ApiOperation(value = "会员分析，新老会员成交分析", notes = "会员分析，新老会员成交分析")
    public ServerResponseEntity<MemberDealRespParam> getMemberDeal(MemberReqParam param) {
        MemberDealRespParam respParam = orderDataAnalysisService.getMemberDeal(param);
        return ServerResponseEntity.success(respParam);
    }

    private Double handleDouble(Double value){
        if (Objects.isNull(value)){
            return 0.0;
        }
        return value;
    }

    private Double divAverage(Double a, Integer b, Integer scale) {
        if (Objects.isNull(b) || b == 0 || Objects.isNull(a)) {
            return 0.0;
        }
        return Arith.div(a, b, scale);
    }
    private Double divAverage(Double a, Double b, Integer scale) {
        if (Objects.isNull(b) || b == 0 || Objects.isNull(a)) {
            return 0.0;
        }
        return Arith.div(a, b, scale);
    }
    private Double divAverage(Integer a, Integer b,Integer scale) {
        if (Objects.isNull(b) || b == 0 || Objects.isNull(a)) {
            return 0.0;
        }
        return Arith.div(a,b,scale);
    }
}
