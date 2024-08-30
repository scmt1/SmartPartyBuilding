/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.service.impl;


import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.app.dto.ProductItemDto;
import com.yami.shop.bean.enums.*;
import com.yami.shop.bean.event.EsProductUpdateEvent;
import com.yami.shop.bean.model.*;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.dao.*;
import com.yami.shop.service.*;
import com.yami.shop.user.common.dto.ScoreOrderMergerDto;
import com.yami.shop.user.common.model.UserScoreDetail;
import com.yami.shop.user.common.model.UserScoreLog;
import com.yami.shop.user.common.service.ScoreOrderService;
import com.yami.shop.user.common.service.UserScoreDetailService;
import com.yami.shop.user.common.service.UserScoreLogService;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author LHD
 * @date 2019-12-12 13:55:57
 */
@Service
@AllArgsConstructor
public class ScoreOrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements ScoreOrderService {

    private final OrderItemService orderItemService;
    private final UserScoreDetailService userScoreDetailService;
    private final UserScoreLogService userScoreLogService;
    private final UserExtensionService userExtensionService;
    private final OrderMapper orderMapper;
    private final OrderLangMapper orderLangMapper;
    private final SkuStockMapper skuStockMapper;
    private final MapperFacade mapperFacade;
    private final SkuService skuService;
    private final ProductService productService;
    private final ProdExtensionMapper prodExtensionMapper;
    private final UserAddrOrderService userAddrOrderService;
    private final Snowflake snowflake;
    private final OrderSettlementMapper orderSettlementMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @CachePut(cacheNames = "ConfirmScoreOrderCache", key = "#userId")
    public ScoreOrderMergerDto putConfirmScoreOrderCache(String userId, ScoreOrderMergerDto scoreOrderMergerDto) {
        return scoreOrderMergerDto;
    }

    @Override
    @Cacheable(cacheNames = "ConfirmScoreOrderCache", key = "#userId")
    public ScoreOrderMergerDto getConfirmScoreOrderCache(String userId) {
        return null;
    }

    @Override
    @CacheEvict(cacheNames = "ConfirmScoreOrderCache", key = "#userId")
    public void removeConfirmScoreOrderCache(String userId) {
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order submit(String userId, ScoreOrderMergerDto mergerOrder) {
        // 提交订单
        Order scoreOrder = handlerOrder(mergerOrder);
        // 插入订单
        orderMapper.insert(scoreOrder);
        // 保存订单语言表
        orderLangMapper.insertBatchOrderLang(Collections.singletonList(scoreOrder));
        // 减少用户积分
        updateUserScore(mergerOrder,scoreOrder);
        // 插入订单项，返回主键
        orderItemService.insertBatchOrderItem(scoreOrder.getOrderItems());
        // 更新es商品库存
        eventPublisher.publishEvent(new EsProductUpdateEvent(mergerOrder.getProductItemDto().getProdId(), null, EsOperationType.UPDATE));
        return scoreOrder;
    }

    private void updateUserScore(ScoreOrderMergerDto mergerOrder,Order order) {
        UserExtension userExtension = userExtensionService.getOne(new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId, order.getUserId()));
        //查询积分详细表数据
        List<UserScoreDetail> scoreDetailList = userScoreDetailService.list(new LambdaQueryWrapper<UserScoreDetail>()
                .eq(UserScoreDetail::getUserId, userExtension.getUserId()).eq(UserScoreDetail::getStatus,1).orderByAsc(UserScoreDetail::getCreateTime));
        if(CollectionUtils.isEmpty(scoreDetailList)){
            // 积分不足无法提交订单
            throw new YamiShopBindException("yami.user.score.no.enough");
        }
        long score = scoreDetailList.stream().mapToLong(UserScoreDetail::getUsableScore).sum();
        Long scorePrice = mergerOrder.getProductItemDto().getScorePrice();
        List<UserScoreDetail> updateScoreDetails = new ArrayList<>();
        if(userExtension.getScore() < scorePrice || score < scorePrice.longValue()){
            // 积分不足无法提交订单
            throw new YamiShopBindException("yami.user.score.no.enough");
        }
        UserScoreLog userScoreLog = new UserScoreLog();
        userScoreLog.setUserId(order.getUserId());
        userScoreLog.setBizId(order.getOrderNumber());
        userScoreLog.setScore(scorePrice);
        userScoreLog.setSource(ScoreLogType.SCORE_CASH.value());
        userScoreLog.setCreateTime(new Date());
        userScoreLog.setIoType(0);
        // 修改积分明细，如果当前明细不够扣除在进行下一条
        // 如果够添加一条积分明细记录
        for (UserScoreDetail scoreDetail : scoreDetailList) {
            if(scoreDetail.getUsableScore() <= scorePrice){
                scoreDetail.setStatus(0);
                scoreDetail.setBizId(order.getOrderNumber());
                updateScoreDetails.add(scoreDetail);

                scorePrice -= scoreDetail.getUsableScore();
            }else{
                UserScoreDetail addDetail = new UserScoreDetail();
                addDetail.setCreateTime(scoreDetail.getCreateTime());
                addDetail.setStatus(0);
                addDetail.setUserId(scoreDetail.getUserId());
                addDetail.setBizId(order.getOrderNumber());
                addDetail.setUsableScore(scorePrice);
                userScoreDetailService.save(addDetail);

                scoreDetail.setUsableScore(scoreDetail.getUsableScore() - scorePrice);
                updateScoreDetails.add(scoreDetail);
                break;
            }
            if(scorePrice <= 0){
                break;
            }
        }
        userScoreLogService.save(userScoreLog);
        userScoreDetailService.updateBatchById(updateScoreDetails);
        //保存用户积分
        userExtension.setScore(userExtension.getScore() - mergerOrder.getProductItemDto().getScorePrice());
        userExtensionService.updateById(userExtension);
    }

    /**
     * 处理订单
     * @param mergerOrder
     * @return
     */
    private Order handlerOrder(ScoreOrderMergerDto mergerOrder){
        Date now = new Date();
        String userId = mergerOrder.getUserId();
        // 订单商品参数
        ProductItemDto productItemDto = mergerOrder.getProductItemDto();
        // 把订单地址保存到数据库
        UserAddrOrder userAddrOrder = mapperFacade.map(mergerOrder.getUserAddr(), UserAddrOrder.class);
        if (userAddrOrder == null) {
            // 请填写收货地址
            throw new YamiShopBindException("yami.delivery.address");
        }
        userAddrOrder.setUserId(userId);
        userAddrOrder.setCreateTime(now);
        userAddrOrderService.save(userAddrOrder);
        if(Objects.isNull(productItemDto)) {
            // 订单已经失效
            throw new YamiShopBindException("yami.order.expired");
        }
        // 订单地址id
        Long addrOrderId = userAddrOrder.getAddrOrderId();
        //检查sku和prod的库存
        Sku sku = checkAndGetSku(productItemDto.getSkuId(), productItemDto);
        Product product = checkAndGetProd(productItemDto.getProdId(), productItemDto);

        // 使用雪花算法生成的订单号
        String orderNumber = String.valueOf(snowflake.nextId());
        mergerOrder.setOrderNumber(orderNumber);
        OrderItem orderItem = mapperFacade.map(productItemDto, OrderItem.class);

        // 插入订单相关信息
        Order order = addOrderData(userId, now, orderNumber, sku, product, orderItem, mergerOrder, productItemDto);
        order.setAddrOrderId(addrOrderId);
        order.setReceiverName(userAddrOrder.getReceiver());
        order.setReceiverMobile(userAddrOrder.getMobile());

        Sku updateSku = new Sku();
        updateSku.setSkuId(sku.getSkuId());
        updateSku.setStocks(orderItem.getProdCount());
        Product updateProd = new Product();
        updateProd.setProdId(sku.getProdId());
        updateProd.setTotalStocks(orderItem.getProdCount());
        // 更新sku库存
        if (skuStockMapper.updateStocks(updateSku) == 0) {
            skuService.removeSkuCacheBySkuId(sku.getSkuId(), sku.getProdId());
            productService.removeProdCacheByProdId(sku.getProdId());
            // 商品库存不足
            String prodMsg = I18nMessage.getMessage("yami.product");
            String msg = I18nMessage.getMessage("yami.insufficient.inventory");
            throw new YamiShopBindException(prodMsg+"[" + sku.getProdName() + "]"+msg);
        }
        // 更新商品库存
        if (prodExtensionMapper.updateProdStocks(updateProd.getProdId()) == 0) {
            productService.removeProdCacheByProdId(product.getProdId());
            // 商品库存不足
            String prodMsg = I18nMessage.getMessage("yami.product");
            String msg = I18nMessage.getMessage("yami.insufficient.inventory");
            throw new YamiShopBindException(prodMsg+"[" + product.getProdName() + "]"+msg);
        }
        return order;
    }

    /**
     * 插入订单信息
     * @param userId 用户id
     * @param now 当前时间
     * @param orderNumber 订单编号
     * @param sku 商品规格
     * @param product 商品信息
     * @param orderItem 订单项
     * @param mergerOrder 订单提交信息
     * @param productItemDto 商品信息
     * @return 订单信息
     */
    private Order addOrderData(String userId, Date now, String orderNumber, Sku sku, Product product, OrderItem orderItem, ScoreOrderMergerDto mergerOrder, ProductItemDto productItemDto) {
        Long shopId = mergerOrder.getShopId();
        List<OrderItem> orderItems = new ArrayList<>();
        // 保存中英文名称
        orderItem.setProdNameCn(productItemDto.getProdNameCn());
        orderItem.setProdName(productItemDto.getProdNameCn());
        orderItem.setProdNameEn(productItemDto.getProdNameEn());
        orderItem.setPic(StrUtil.isBlank(sku.getPic()) ? product.getPic() : sku.getPic());
        orderItem.setShopId(shopId);
        orderItem.setOrderNumber(orderNumber);
        orderItem.setUserId(userId);
        orderItem.setRecTime(now);
        orderItem.setShareReduce(mergerOrder.getFreeTransfee());
        orderItem.setUseScore(mergerOrder.getProductItemDto().getScorePrice());
        orderItem.setCommSts(0);
        orderItems.add(orderItem);
        // 订单信息
        Order order = new Order();
        order.setShopId(shopId);
        order.setOrderNumber(orderNumber);
        // 订单商品名称
        order.setProdName(orderItem.getProdName());
        // 保存中英文名称
        order.setProdNameCn(productItemDto.getProdNameCn());
        order.setProdNameEn(productItemDto.getProdNameEn());
        // 用户id
        order.setUserId(userId);
        // 商品总额
        order.setTotal(mergerOrder.getTotal());
        // 实际总额
        order.setActualTotal(mergerOrder.getActualTotal());
        order.setPlatformAmount(mergerOrder.getFreeTransfee());
        order.setStatus(OrderStatus.UNPAY.value());
        order.setUpdateTime(now);
        order.setCreateTime(now);
        order.setIsPayed(0);
        order.setDeleteStatus(0);
        order.setProductNums(mergerOrder.getTotalCount());
        order.setOrderType(OrderType.SCORE.value());
        order.setDvyType(DvyType.DELIVERY.value());
        order.setReduceAmount(mergerOrder.getFreeTransfee());
        order.setFreightAmount(mergerOrder.getTotalTransfee() - mergerOrder.getFreeTransfee());
        order.setRemarks(mergerOrder.getRemarks());
        order.setPlatformFreeFreightAmount(mergerOrder.getFreeTransfee());
        order.setOrderItems(orderItems);

        //插入订单结算表
        OrderSettlement orderSettlement = new OrderSettlement();
        //orderSettlement.setUserId(userId);
        orderSettlement.setCreateTime(now);
        //orderSettlement.setIsClearing(0);
        orderSettlement.setOrderNumber(orderNumber);
        orderSettlement.setPayAmount(order.getActualTotal());
        orderSettlement.setPayStatus(0);
        orderSettlement.setVersion(0);
        //如果用使用积分，结算表将积分价格插入
        if(mergerOrder.getIsScorePay() != null && mergerOrder.getIsScorePay() == 1 && mergerOrder.getProductItemDto() != null){
            orderSettlement.setPayScore(mergerOrder.getProductItemDto().getScorePrice());
        }
        orderSettlementMapper.insert(orderSettlement);
        return order;

    }


    @SuppressWarnings({"Duplicates"})
    private Sku checkAndGetSku(Long skuId, ProductItemDto shopCartItem) {
        // 获取sku信息
        Sku sku = skuService.getSkuBySkuId(skuId, I18nMessage.getDbLang());
        // 当sku被删除时，不可以提交订单
        if (sku == null || sku.getIsDelete() == 1) {
            // 商品已售空或已下架
            throw new YamiShopBindException("yami.product.sold.out");
        }

        if (sku.getStatus() != 1) {
            String message = I18nMessage.getMessage("yami.product");
            String takeOff = I18nMessage.getMessage("yami.shopCart.take.off");
            // 商品已下架
            throw new YamiShopBindException(message+"[" + sku.getProdName() + "]"+takeOff);
        }
        // -1为无限库存
        if (sku.getStocks() != -1 && shopCartItem.getProdCount() > sku.getStocks()) {
            // 商品库存不足
            String prodMsg = I18nMessage.getMessage("yami.product");
            String msg = I18nMessage.getMessage("yami.insufficient.inventory");
            throw new YamiShopBindException(prodMsg+"[" + sku.getProdName() + "]"+msg);
        }

        if (sku.getStocks() != -1) {
            // 这里的库存是改变的库存
            sku.setStocks(shopCartItem.getProdCount());
        }
        return sku;
    }

    @SuppressWarnings({"Duplicates"})
    private Product checkAndGetProd(Long prodId, ProductItemDto productItemDto) {
        Product product = productService.getProductByProdId(prodId, I18nMessage.getDbLang());
        if (product == null) {
            // 商品已售空或已下架
            throw new YamiShopBindException("yami.product.sold.out");
        }
        if (product.getStatus() != 1) {
            String message = I18nMessage.getMessage("yami.product");
            String takeOff = I18nMessage.getMessage("yami.shopCart.take.off");
            // 商品已下架
            throw new YamiShopBindException(message+"[" + product.getProdName() + "]"+takeOff);
        }
        Integer totalStocks = product.getTotalStocks();
        // 商品需要改变的库存
        if (totalStocks != -1) {
            product.setTotalStocks(productItemDto.getProdCount());
        }
        // -1为无限库存
        if (totalStocks != -1 && product.getTotalStocks() > totalStocks) {
            // 商品库存不足
            String prodMsg = I18nMessage.getMessage("yami.product");
            String msg = I18nMessage.getMessage("yami.insufficient.inventory");
            throw new YamiShopBindException(prodMsg+"[" + product.getProdName() + "]"+msg);
        }
        return product;
    }

}
