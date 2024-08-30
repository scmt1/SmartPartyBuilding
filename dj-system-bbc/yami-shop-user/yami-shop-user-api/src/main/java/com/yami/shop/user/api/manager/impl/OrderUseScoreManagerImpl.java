package com.yami.shop.user.api.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.app.dto.ShopCartItemDiscountDto;
import com.yami.shop.bean.app.dto.ShopCartItemDto;
import com.yami.shop.bean.app.dto.ShopCartOrderDto;
import com.yami.shop.bean.app.dto.ShopCartOrderMergerDto;
import com.yami.shop.bean.app.param.OrderParam;
import com.yami.shop.bean.model.Category;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.bean.param.ScoreConfigParam;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.util.Arith;
import com.yami.shop.manager.OrderUseScoreManager;
import com.yami.shop.security.api.util.SecurityUtils;
import com.yami.shop.service.CategoryService;
import com.yami.shop.service.SysConfigService;
import com.yami.shop.service.UserExtensionService;
import com.yami.shop.user.common.util.CategoryScale;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author FrozenWatermelon
 * @date 2021/12/23
 */
@Component
@AllArgsConstructor
public class OrderUseScoreManagerImpl implements OrderUseScoreManager {


    private final UserExtensionService userExtensionService;
    private final SysConfigService sysConfigService;

    private final CategoryService categoryService;
    private static final Integer SCORE_RATIO = 100;
    private static final Integer MIN_SCORE_RATIO = 10;

    @Override
    public void orderUseScore(ShopCartOrderMergerDto shopCartOrderMergerDto, OrderParam orderParam, List<ShopCartItemDto> allCartItem) {
//        // 如果是预售订单则不能使用优惠
//        if (Objects.equals(orderParam.getPreSellStatus(), 1)) {
//            return;
//        }
        // 获取用户等级积分详细表
        UserExtension extension = userExtensionService.getOne(
                new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId, SecurityUtils.getUser().getUserId()));
        // 获取配置信息
        ScoreConfigParam scoreParam = sysConfigService.getSysConfigObject(Constant.SCORE_CONFIG, ScoreConfigParam.class);
        if (Objects.nonNull(scoreParam) && scoreParam.getShopScoreSwitch() != null && !scoreParam.getShopScoreSwitch()) {
            return;
        }
        if (orderParam.getUserUseScore() != null && orderParam.getUserUseScore() < 0 && extension.getScore() < 0) {
            return;
        }
        // 计算积分最多抵现金额
        double totalScoreAmount = 0.0;
        List<Category> categories = categoryService.listByShopId(Constant.PLATFORM_SHOP_ID);
        Map<Long, Double> categoryMap = CategoryScale.getScaleByCategory(categories, scoreParam, 0);
        double useDiscount = Objects.isNull(scoreParam.getUseDiscount()) ? 0.0 : scoreParam.getUseDiscount();
        // 积分抵现金额可用上限 = 订单可使用积分比例 * ((实际总值)/100）
        if (scoreParam.getUseDiscountRange() == 0) {
            // 如果有等级优惠金额，还需减去等级的优惠金额、运费才是实际金额，包邮时运费为0
            double actualTotal = Arith.add(Arith.sub(shopCartOrderMergerDto.getActualTotal(), shopCartOrderMergerDto.getTotalTransFee()), shopCartOrderMergerDto.getFreeTransFee());
            double notScoreAmount =0.0;
            // 获取大类的比例并转成map
            for (ShopCartItemDto shopCartItem : allCartItem) {
                if (shopCartItem.getActualTotal() < scoreParam.getShopUseScore()) {
                    notScoreAmount = Arith.add(notScoreAmount,shopCartItem.getActualTotal());
                }
            }
            actualTotal = Arith.sub(actualTotal , notScoreAmount);
            totalScoreAmount = Arith.div(Arith.mul(actualTotal, useDiscount), 100);
        } else {
            // 获取大类的比例并转成map
            for (ShopCartItemDto shopCartItem : allCartItem) {
                if (!categoryMap.containsKey(shopCartItem.getCategoryId())) {
                    continue;
                }
                if(shopCartItem.getActualTotal() < scoreParam.getShopUseScore()){
                    continue;
                }
                // 商品总额减去总优惠，就是商品项的实际金额
                double actualTotal = Arith.sub(shopCartItem.getProductTotalAmount(), shopCartItem.getShareReduce());
                double scoreReduce = Arith.div(Arith.mul(actualTotal, categoryMap.get(shopCartItem.getCategoryId())), 100);
                totalScoreAmount = Arith.add(totalScoreAmount, scoreReduce);
            }
        }
        // 计算用户可用积分 如果大于总共的积分使用比例直接使用，如果小于根据比例使用
        int canUseScore = (int) Arith.mul(totalScoreAmount, scoreParam.getShopUseScore());
        Long userUseScore = extension.getScore();
        Long totalUsableScore = extension.getScore();
        //如果是用户选择的积分数额，则用此积分数额
        if (orderParam.getUserUseScore() != null && orderParam.getIsScorePay() == 1 && orderParam.getUserUseScore() >= 0) {
            userUseScore = Math.min(orderParam.getUserUseScore(), userUseScore);
        } else {
            userUseScore = 0L;
        }
        long maxScore = Math.min(totalUsableScore, canUseScore);
        userUseScore = Math.min(userUseScore, canUseScore);
        // 如果为大于100的比例，则积分需要整10使用
        if (scoreParam.getShopUseScore() > SCORE_RATIO) {
            userUseScore = userUseScore - userUseScore % MIN_SCORE_RATIO;
            maxScore = maxScore - maxScore % MIN_SCORE_RATIO;
        }
        // 计算可抵扣金额，然后算使用积分，然后再用使用积分算抵扣金额
        totalScoreAmount = Arith.div(userUseScore, scoreParam.getShopUseScore(), 2);
        userUseScore = (long) Arith.mul(totalScoreAmount, scoreParam.getShopUseScore());
        totalScoreAmount = Arith.div(userUseScore, scoreParam.getShopUseScore(), 2);
        // 在极端积分比例的情况下，最大可用积分和使用的积分有冲突操作
        double maxScoreAmount = Arith.div(maxScore, scoreParam.getShopUseScore(), 2);
        maxScore = (long) Arith.mul(maxScoreAmount, scoreParam.getShopUseScore());
        // 这里如果不使用最大可用积分，则后续的计算还需要乘以这个比例
        double scale = 1.0;
        if (userUseScore < canUseScore) {
            scale = Arith.div(userUseScore, maxScore);
        }
        calculatedAmount(scale, maxScore, userUseScore, shopCartOrderMergerDto, scoreParam, categoryMap, totalScoreAmount, orderParam);
    }

    private void calculatedAmount(double scale, Long maxScore, Long userUseScore, ShopCartOrderMergerDto shopCartOrderMergerDto, ScoreConfigParam scoreParam,
                                  Map<Long, Double> categoryMap, double totalScoreAmount, OrderParam orderParam) {

        // 用户选择积分抵现，组装积分信息
        List<ShopCartOrderDto> shopCartOrders = shopCartOrderMergerDto.getShopCartOrders();
        double totalScoreReduce = 0.0;
        double useDiscount = Objects.isNull(scoreParam.getUseDiscount()) ? 0.0 : scoreParam.getUseDiscount();
        long totalScore = 0;
        // 通过for i找出最后一项，将计算偏差的1积分给最后的最大的一项
        double maxAmount = 0.0;
        Long maxAmountSkuId = 0L;
        for (ShopCartOrderDto shopCartOrder : shopCartOrders) {
            for (ShopCartItemDiscountDto shopCartItemDiscount : shopCartOrder.getShopCartItemDiscounts()) {
                for (ShopCartItemDto shopCartItem : shopCartItemDiscount.getShopCartItems()) {
                    maxAmountSkuId = shopCartItem.getActualTotal() > maxAmount ? shopCartItem.getSkuId() : maxAmountSkuId;
                    maxAmount = Math.max(shopCartItem.getActualTotal(),maxAmount);
                }
            }
        }
        for (int shopIndex = 0; shopIndex < shopCartOrders.size(); shopIndex++) {
            ShopCartOrderDto shopCartOrder = shopCartOrders.get(shopIndex);
            double reduceSum = 0.0;
            long shopScore = 0;
            List<ShopCartItemDiscountDto> shopCartItemDiscounts = shopCartOrder.getShopCartItemDiscounts();
            for (int discountIndex = 0; discountIndex < shopCartItemDiscounts.size(); discountIndex++) {
                List<ShopCartItemDto> shopCartItems = shopCartItemDiscounts.get(discountIndex).getShopCartItems();
                for (int itemIndex = 0; itemIndex < shopCartItems.size(); itemIndex++) {
                    ShopCartItemDto shopCartItem = shopCartItems.get(itemIndex);
                    // 如果是金额最大的一项，直接跳过，最后在处理
                    if(Objects.equals(shopCartItem.getSkuId(),maxAmountSkuId)){
                        continue;
                    }
                    if (shopCartItem.getActualTotal() < scoreParam.getShopUseScore()) {
                        continue;
                    }
                    double scoreReduceProd = 0.0;
//                    // 判断是否最后一项
//                    boolean isEnd = shopIndex == shopCartOrders.size() - 1 && discountIndex == shopCartItemDiscounts.size() - 1 && itemIndex == shopCartItems.size() - 1;
                    // 计算商品分摊的金额，需要乘以比例
                    if (scoreParam.getUseDiscountRange() == 0) {
                        scoreReduceProd = Arith.div(Arith.mul(shopCartItem.getActualTotal(), useDiscount), 100, 2);
                    } else if (categoryMap.containsKey(shopCartItem.getCategoryId())) {
                        scoreReduceProd = Arith.div(Arith.mul(shopCartItem.getActualTotal(), categoryMap.get(shopCartItem.getCategoryId())), 100, 2);
                    }
                    long useScore = (long) Arith.mul(Arith.roundByBanker(Arith.mul(scoreReduceProd, scale), 2), scoreParam.getShopUseScore());
                    scoreReduceProd = Arith.div(useScore, scoreParam.getShopUseScore(), 2);
                    // 如果大于可用上限则直接等于，接将剩余的抵扣金额全部赋予最后一个，积分和金额直接等于 使用的 - 已经抵扣的
                    if (Arith.add(totalScoreReduce, scoreReduceProd) > totalScoreAmount) {
                        // 减去当前总共的积分，减去店铺已分摊的积分
                        useScore = userUseScore - totalScore - shopScore;
                        scoreReduceProd = Arith.sub(totalScoreAmount, totalScoreReduce);
                    }

                    totalScoreReduce = Arith.add(totalScoreReduce, scoreReduceProd);
                    reduceSum = Arith.add(reduceSum, scoreReduceProd);
                    shopScore += useScore;
                    if (orderParam.getIsScorePay() != null && orderParam.getIsScorePay() == 1) {
                        double platformReduce = shopCartItem.getPlatformShareReduce() == null ? 0 : shopCartItem.getPlatformShareReduce();
                        shopCartItem.setPlatformShareReduce(Arith.add(platformReduce, scoreReduceProd));
                        shopCartItem.setScorePayReduce(scoreReduceProd);
                        shopCartItem.setScorePrice(useScore);
                        shopCartItem.setShareReduce(Arith.add(Arith.roundByBanker(shopCartItem.getShareReduce(), 2), scoreReduceProd));
                        shopCartItem.setActualTotal(Arith.sub(shopCartItem.getActualTotal(), scoreReduceProd));
                    }
                }
            }
            // 设置店铺的实际总值、积分优惠金额和订单优惠金额
            shopCartOrder.setScoreReduce(reduceSum);
            shopCartOrder.setActualTotal(Arith.sub(shopCartOrder.getActualTotal(), reduceSum));
            // 放入优惠金额
            shopCartOrder.setShopReduce(Arith.add(shopCartOrder.getShopReduce(), reduceSum));
            // 放入平台优惠金额,如果用户等级免自营店运费也要放进去
            shopCartOrder.setPlatformAmount(Arith.add(shopCartOrder.getPlatformAmount(), reduceSum));
            totalScore += shopScore;
            if (orderParam.getIsScorePay() != null && orderParam.getIsScorePay() == 1) {
                shopCartOrder.setUseScore(shopScore);
            }
        }
        // 处理最后一项
        for (ShopCartOrderDto shopCartOrder : shopCartOrders) {
            for (ShopCartItemDiscountDto shopCartItemDiscount : shopCartOrder.getShopCartItemDiscounts()) {
                for (ShopCartItemDto shopCartItem : shopCartItemDiscount.getShopCartItems()) {
                    // 如果不是金额最大的一项，直接跳过
                    if (!Objects.equals(shopCartItem.getSkuId(), maxAmountSkuId)) {
                        continue;
                    }
                    if (shopCartItem.getActualTotal() < scoreParam.getShopUseScore()) {
                        continue;
                    }
                    // 减去当前总共的积分，减去店铺已分摊的积分
                    long useScore = userUseScore - totalScore;
                    double scoreReduceProd = Arith.sub(totalScoreAmount, totalScoreReduce);
                    if(scoreReduceProd > shopCartItem.getActualTotal()) {
                        scoreReduceProd = Math.min(scoreReduceProd, shopCartItem.getActualTotal());
                        useScore = (long) Arith.mul(Arith.roundByBanker(Arith.mul(scoreReduceProd, scale), 2), scoreParam.getShopUseScore());
                        scoreReduceProd = Arith.div(useScore, scoreParam.getShopUseScore(), 2);
                    }
                    if (orderParam.getIsScorePay() != null && orderParam.getIsScorePay() == 1) {
                        double platformReduce = shopCartItem.getPlatformShareReduce() == null ? 0 : shopCartItem.getPlatformShareReduce();
                        shopCartItem.setPlatformShareReduce(Arith.add(platformReduce, scoreReduceProd));
                        shopCartItem.setScorePayReduce(scoreReduceProd);
                        shopCartItem.setScorePrice(useScore);
                        shopCartItem.setShareReduce(Arith.add(Arith.roundByBanker(shopCartItem.getShareReduce(), 2), scoreReduceProd));
                        shopCartItem.setActualTotal(Arith.sub(shopCartItem.getActualTotal(), scoreReduceProd));
                    }
                    // 设置店铺的实际总值、积分优惠金额和订单优惠金额
                    shopCartOrder.setScoreReduce(Arith.add(shopCartOrder.getScoreReduce(),scoreReduceProd));
                    shopCartOrder.setActualTotal(Arith.sub(shopCartOrder.getActualTotal(), scoreReduceProd));
                    // 放入优惠金额
                    shopCartOrder.setShopReduce(Arith.add(shopCartOrder.getShopReduce(), scoreReduceProd));
                    // 放入平台优惠金额,如果用户等级免自营店运费也要放进去
                    shopCartOrder.setPlatformAmount(Arith.add(shopCartOrder.getPlatformAmount(), scoreReduceProd));
                    if (orderParam.getIsScorePay() != null && orderParam.getIsScorePay() == 1) {
                        shopCartOrder.setUseScore(shopCartOrder.getUseScore() + useScore);
                    }
                    totalScoreReduce = Arith.add(totalScoreReduce, scoreReduceProd);
                    totalScore += useScore;
                    break;
                }
            }
        }
        // 设置订单的实际总值和订单优惠金额
        shopCartOrderMergerDto.setTotalScoreAmount(totalScoreReduce);
        shopCartOrderMergerDto.setShopUseScore(scoreParam.getShopUseScore());
        shopCartOrderMergerDto.setTotalUsableScore(totalScore);
        shopCartOrderMergerDto.setActualTotal(Arith.sub(shopCartOrderMergerDto.getActualTotal(), totalScoreReduce));
        shopCartOrderMergerDto.setOrderReduce(Arith.add(shopCartOrderMergerDto.getOrderReduce(), totalScoreReduce));
        shopCartOrderMergerDto.setMaxUsableScore(maxScore);
        if (orderParam.getIsScorePay() != null && orderParam.getIsScorePay() == 1) {
            shopCartOrderMergerDto.setScorePrice(totalScore);
        }
    }

}
