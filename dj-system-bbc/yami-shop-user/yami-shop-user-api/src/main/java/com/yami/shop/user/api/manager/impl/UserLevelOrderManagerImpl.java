package com.yami.shop.user.api.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.app.dto.ShopCartItemDiscountDto;
import com.yami.shop.bean.app.dto.ShopCartItemDto;
import com.yami.shop.bean.app.dto.ShopCartOrderDto;
import com.yami.shop.bean.app.dto.ShopCartOrderMergerDto;
import com.yami.shop.bean.enums.OrderActivityType;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.util.Arith;
import com.yami.shop.manager.UserLevelOrderManager;
import com.yami.shop.security.api.util.SecurityUtils;
import com.yami.shop.service.UserExtensionService;
import com.yami.shop.user.common.dao.UserLevelMapper;
import com.yami.shop.user.common.model.UserLevel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FrozenWatermelon
 * @date 2021/12/21
 */
@Component
public class UserLevelOrderManagerImpl implements UserLevelOrderManager {

    @Autowired
    private UserLevelMapper userLevelMapper;

    @Autowired
    private UserExtensionService userExtensionService;

    private Long maxAmountSkuId;

    @Override
    public void calculateLevelDiscount(ShopCartOrderMergerDto shopCartOrderMerger) {
//        // 如果是预售订单则不能使用优惠
//        if (Objects.equals(shopCartOrderMerger.getPreSellStatus(), 1)) {
//            return;
//        }
        UserExtension extension = userExtensionService.getOne(
                new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId, SecurityUtils.getUser().getUserId()));
        if (Objects.isNull(extension)) {
            return;
        }
        if (extension.getLevel() == null) {
            extension.setLevel(Constant.USER_LEVEL_INIT);
        }
        UserLevel level = userLevelMapper.selectOneAndCategory(extension);
        if (Objects.isNull(level) || level.getDiscount() > Constant.MAX_LEVEL_DISCOUNT) {
            return;
        }
        List<ShopCartOrderDto> shopCartOrders = shopCartOrderMerger.getShopCartOrders();
        // 获取分类比例
        // 最后计算成长值折扣
        double levelDiscount = 0.0;
        double freeTransFee = 0.0;

        double discount = Arith.sub(10, level.getDiscount());

        List<Long> categoryIds = level.getCategoryIds();
        Set<Long> categorySet = new HashSet<>(categoryIds);

        List<ShopCartItemDto> shopCartItems = new ArrayList<>();
        for (ShopCartOrderDto shopCartOrder : shopCartOrders) {
            // 店铺中的满减项列表
            List<ShopCartItemDiscountDto> shopCartItemDiscounts = shopCartOrder.getShopCartItemDiscounts();
            for (ShopCartItemDiscountDto shopCartItemDiscount : shopCartItemDiscounts) {
                // 如果是套餐活动跳过循环
                if (Objects.equals(shopCartItemDiscount.getActivityType(), OrderActivityType.COMBO.value())) {
                    continue;
                }
                shopCartItems.addAll(shopCartItemDiscount.getShopCartItems());
            }
        }
        // 计算折扣总额
        double maxLevelFeeTotal = getLevelFeeTotal(shopCartItems, level, categorySet);
        // 通过for i找出最后一项，将计算偏差的金额给最后的最大的一项
        for (int shopIndex = 0; shopIndex < shopCartOrders.size(); shopIndex++) {
            ShopCartOrderDto shopCartOrder = shopCartOrders.get(shopIndex);
            // 如果可用范围为自营店不为当前店铺直接下一次循环
            if (level.getDiscountRange() == 1 && !Objects.equals(shopCartOrder.getShopId(), 1L)) {
                continue;
            }
            double reduceSum = 0.0;
//            boolean isShopEnd = shopIndex == shopCartOrders.size() - 1;
            for (int index = 0; index < shopCartItems.size(); index++) {
                ShopCartItemDto shopCartItem = shopCartItems.get(index);
                if (!Objects.equals(shopCartItem.getShopId(), shopCartOrder.getShopId())) {
                    continue;
                }
                double prodDiscount = 0.0;
//                // 判断是否最后一项
//                boolean isEnd = isShopEnd && index == shopCartItems.size() - 1;
                // 如果是金额最大的一项，直接跳过，最后在处理
                if(Objects.equals(shopCartItem.getSkuId(),maxAmountSkuId)){
                    continue;
                }
                // 折扣
                if (level.getDiscountType() == 0 || categorySet.contains(shopCartItem.getCategoryId())) {
                    prodDiscount = Arith.div(Arith.mul(shopCartItem.getActualTotal(), discount), 10, 2);
                    if (Arith.add(Arith.add(levelDiscount, reduceSum), prodDiscount) > maxLevelFeeTotal) {
                        // 总折扣金额减去当前累计的折扣金额，就为最后一件商品分摊的等级优惠金额
                        prodDiscount = Arith.sub(Arith.sub(maxLevelFeeTotal, levelDiscount), reduceSum);
                    }
                }
                // 计算商品分摊的金额
                shopCartItem.setPlatformShareReduce(Arith.add(shopCartItem.getPlatformShareReduce(), prodDiscount));
                shopCartItem.setLevelReduce(prodDiscount);
                reduceSum = Arith.add(reduceSum, Arith.roundByBanker(prodDiscount, 2));
            }
            // 设置店铺的实际总值、积分优惠金额和订单优惠金额
            shopCartOrder.setLevelReduce(reduceSum);
            levelDiscount = Arith.add(levelDiscount, reduceSum);
            // 判断用户等级是否自营店包邮
            if (Objects.equals(shopCartOrder.getShopId(), Constant.MAIN_SHOP) && level.getIsFreeFee() == 1) {
                if (shopCartOrder.getTransFee() > 0) {
                    freeTransFee = shopCartOrder.getTransFee();
                    shopCartOrder.setTransFee(0.0);
                    shopCartOrder.setFreeTransFee(shopCartOrder.getTransFee() + freeTransFee);
                }
            }
        }
        // 最后一项处理，将计算偏差的金额给最后的最大的一项
        for (ShopCartOrderDto shopCartOrder : shopCartOrders) {
            // 如果可用范围为自营店不为当前店铺直接下一次循环
            if (level.getDiscountRange() == 1 && !Objects.equals(shopCartOrder.getShopId(), 1L)) {
                continue;
            }
            for (ShopCartItemDto shopCartItem : shopCartItems) {
                if (!Objects.equals(shopCartItem.getShopId(), shopCartOrder.getShopId())) {
                    continue;
                }
                // 如果是金额最大的一项，直接跳过，最后在处理
                if (!Objects.equals(shopCartItem.getSkuId(), maxAmountSkuId)) {
                    continue;
                }
                // 总折扣金额减去当前累计的折扣金额，就为最后一件商品分摊的等级优惠金额
                double prodDiscount = Arith.sub(maxLevelFeeTotal, levelDiscount);
                // 计算商品分摊的金额
                shopCartItem.setPlatformShareReduce(Arith.add(shopCartItem.getPlatformShareReduce(), prodDiscount));
                shopCartItem.setLevelReduce(prodDiscount);
                // 设置店铺的实际总值、积分优惠金额和订单优惠金额
                shopCartOrder.setLevelReduce(Arith.add(shopCartOrder.getLevelReduce(), prodDiscount));
                levelDiscount = Arith.add(levelDiscount, prodDiscount);
                break;
            }
        }
        shopCartOrderMerger.setTotalLevelAmount(levelDiscount);
        // 设置运费优惠金额
        shopCartOrderMerger.setFreeTransFee(freeTransFee);
    }

    /**
     * 计算出总共可以折扣的金额
     *
     * @param shopCartItems 全部商品项
     * @param level         等级
     * @param categorySet   分类
     * @return 总折扣金额
     */
    private double getLevelFeeTotal(List<ShopCartItemDto> shopCartItems, UserLevel level, Set<Long> categorySet) {
        double totalFee = 0.0;
        double maxAmount = 0.0;
        maxAmountSkuId = 0L;
        for (ShopCartItemDto shopCartItem : shopCartItems) {
            // 折扣
            if (level.getDiscountType() == 0 || categorySet.contains(shopCartItem.getCategoryId())) {
                totalFee = Arith.add(totalFee, shopCartItem.getActualTotal());
                maxAmountSkuId = shopCartItem.getActualTotal() > maxAmount ? shopCartItem.getSkuId() : maxAmountSkuId;
                maxAmount = Math.max(shopCartItem.getActualTotal(),maxAmount);
            }
        }
        return Arith.div(Arith.mul(totalFee, Arith.sub(10, level.getDiscount())), 10, 2);
    }
}
