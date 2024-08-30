/*

 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 *//*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.enums.OrderCloseType;
import com.yami.shop.bean.enums.OrderStatus;
import com.yami.shop.bean.enums.OrderType;
import com.yami.shop.bean.enums.ReturnMoneyStsType;
import com.yami.shop.bean.event.ReceiptOrderEvent;
import com.yami.shop.bean.model.Category;
import com.yami.shop.bean.model.OrderItem;
import com.yami.shop.bean.model.OrderRefund;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.bean.order.ConfirmOrderOrder;
import com.yami.shop.bean.param.GrowthParamConfig;
import com.yami.shop.bean.param.ProdOrderParam;
import com.yami.shop.bean.param.ScoreConfigParam;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.util.Arith;
import com.yami.shop.dao.ProductMapper;
import com.yami.shop.dao.UserExtensionMapper;
import com.yami.shop.service.CategoryService;
import com.yami.shop.service.OrderItemService;
import com.yami.shop.service.OrderRefundService;
import com.yami.shop.service.SysConfigService;
import com.yami.shop.user.common.dao.UserLevelMapper;
import com.yami.shop.user.common.model.UserLevel;
import com.yami.shop.user.common.service.UserLevelService;
import com.yami.shop.user.common.util.CategoryScale;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**

 * 确认收货的事件，增加用户成长值和积分

 * @author lhd

 * @date 2020/03/03
 */
@Slf4j
@Component("UserReceiptOrderListener")
@AllArgsConstructor
public class ReceiptOrderListener {

    private final SysConfigService sysConfigService;
    private final UserLevelMapper userLevelMapper;
    private final ProductMapper productMapper;
    private final UserLevelService userLevelService;
    private final OrderRefundService orderRefundService;
    private final OrderItemService orderItemService;
    private final CategoryService categoryService;
    private final UserExtensionMapper userExtensionMapper;

    @EventListener(ReceiptOrderEvent.class)
    @Order(ConfirmOrderOrder.SCORE)
    public void userReceiptOrderLister(ReceiptOrderEvent event) {
        com.yami.shop.bean.model.Order order = event.getOrder();
        //如果订单已经关闭（整单都已经退款了）不需要进行积分、成长值结算
        if(Objects.equals(order.getStatus() , OrderStatus.CLOSE.value()) && Objects.equals(order.getCloseType(), OrderCloseType.REFUND.value())){
            return;
        }
        //如果是积分订单不能获取成长值和积分
        if(Objects.equals(order.getOrderType() , OrderType.SCORE.value())){
            return;
        }
        //判断订单是否有订单项退款成功，如果有减少用户可以获取的部分积分
        List<OrderRefund> orderRefunds = orderRefundService.list(new LambdaQueryWrapper<OrderRefund>()
                .eq(OrderRefund::getOrderId, order.getOrderId()).eq(OrderRefund::getReturnMoneySts, ReturnMoneyStsType.SUCCESS.value()));
        List<Long> orderItemIds = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(orderRefunds)){
            orderRefunds.forEach(refund -> orderItemIds.add(refund.getOrderItemId()));
        }
        // 根据比例取可以获取的积分上限
        List<ProdOrderParam> products = productMapper.getProdsByOrderNumber(order.getOrderNumber());
        // 筛选用户可以获得的订单项积分
        products  = products.stream().filter(prodOrderParam ->orderItemIds.stream().noneMatch(
                orderItemId -> Objects.equals(orderItemId, prodOrderParam.getOrderItemId()))).collect(Collectors.toList());
        //获取积分和成长值获取比例
        ScoreConfigParam scoreParam = sysConfigService.getSysConfigObject(Constant.SCORE_CONFIG, ScoreConfigParam.class);
        GrowthParamConfig growthParamConfig = sysConfigService.getSysConfigObject(Constant.GROWTH_CONFIG, GrowthParamConfig.class);
        if(Objects.isNull(scoreParam) && Objects.isNull(growthParamConfig)){
            return;
        }
        UserExtension userExtension = userExtensionMapper.selectOne(new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId,order.getUserId()));
        UserLevel nowLevel = userLevelMapper.selectOne(new LambdaQueryWrapper<UserLevel>()
                .eq(UserLevel::getLevel, userExtension.getLevel())
                .eq(UserLevel::getLevelType, userExtension.getLevelType()));
        //初始每笔订单可以获取的成长值
        double growthPrice = growthParamConfig.getBuyOrder();
        long totalScore =0;
        //获取所有分类的比例，并放进map
        List<Category> categories = categoryService.listByShopId(Constant.PLATFORM_SHOP_ID);
        Map<Long, Double> categoryMap = CategoryScale.getScaleByCategory(categories, scoreParam,1);
        //需要批量修改的orderItem
        List<OrderItem> orderItems = new ArrayList<>();
        for (ProdOrderParam product : products) {
            double scoreLimit;
            double score = Arith.div(product.getActualTotal(), scoreParam.getShopGetScore());
            //计算出成长值
            growthPrice += Arith.div(product.getActualTotal(), growthParamConfig.getBuyPrice());
            double getDiscount = Objects.isNull(scoreParam.getGetDiscount()) ? 0.0 : scoreParam.getGetDiscount();
            //判断积分购物获取是全平台还是根据分类上限
            if(scoreParam.getGetDiscountRange() == 0){
                scoreLimit = Arith.div(Arith.mul(product.getActualTotal(), getDiscount), 100);
            }else {
                //查询不到分类比例上限直接下一次循环
                if(!categoryMap.containsKey(product.getCategoryId())) {
                    continue;
                }
                scoreLimit = Arith.div(Arith.mul(product.getActualTotal(), categoryMap.get(product.getCategoryId())), 100);
            }
            scoreLimit = Arith.div(scoreLimit, scoreParam.getShopGetScore());
            //上限取最小值
            score = Math.min(score, scoreLimit);
            //根据用户等级获取积分倍数
            if(nowLevel != null && nowLevel.getRateScore() != null && nowLevel.getRateScore() > 1){
                score = Arith.mul(score ,nowLevel.getRateScore());
            }
            long gainScore = (long) score;
            // 如果积分获取开关关闭了，则无法获取积分
            if(Objects.isNull(scoreParam.getShopScoreSwitch()) || !scoreParam.getShopScoreSwitch()){
                gainScore = 0;
            }
            if(gainScore <= 0){
                continue;
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemId(product.getOrderItemId());
            orderItem.setGainScore(gainScore);
            orderItems.add(orderItem);
            totalScore += gainScore;
        }
        // 如果积分获取开关关闭了，则无法获取积分
        if(Objects.isNull(scoreParam.getShopScoreSwitch()) || !scoreParam.getShopScoreSwitch()){
            totalScore = 0;
        }
        // 如果成长值获取开关关闭了，则无法获取成长值
        if(Objects.isNull(growthParamConfig.getShopGrowthSwitch()) || !growthParamConfig.getShopGrowthSwitch()){
            growthPrice = 0;
        }
        if(CollectionUtils.isNotEmpty(orderItems) && totalScore > 0){
            orderItemService.updateBatchById(orderItems);
        }
        // 成长值和积分大于0 增加用户成长值和积分
        if(growthPrice > 0 || totalScore > 0) {
            userLevelService.addGrowthAndScore(growthPrice, totalScore, order.getUserId(),order.getOrderNumber(), userExtension,1);
        }
    }

}
