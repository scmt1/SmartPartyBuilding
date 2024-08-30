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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.enums.*;
import com.yami.shop.bean.event.EsProductUpdateEvent;
import com.yami.shop.bean.model.OfflineHandleEvent;
import com.yami.shop.bean.model.ProdLang;
import com.yami.shop.bean.model.Product;
import com.yami.shop.bean.model.Sku;
import com.yami.shop.bean.param.ProductParam;
import com.yami.shop.bean.param.ProductScoreParam;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
//import com.yami.shop.delivery.common.model.Transport;
//import com.yami.shop.delivery.common.service.TransportService;
import com.yami.shop.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * 商品列表、商品发布controller
 *
 * @author lgh
 */
@RestController
@RequestMapping("/platform/scoreProduct")
@AllArgsConstructor
@Api(tags = "积分商品")
public class ScoreProductController {

    private final ProductService productService;
    private final SkuService skuService;
    private final ProdLangService prodLangService;
    private final BasketService basketService;
    private final ApplicationEventPublisher eventPublisher;
    private final OfflineHandleEventService offlineHandleEventService;
    private final MapperFacade mapperFacade;
//    private final TransportService transportService;


    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('score:prod:page')")
    @ApiOperation(value = "分页获取积分商品信息", notes = "分页获取积分商品信息")
    public ServerResponseEntity<IPage<Product>> page(ProductParam product, PageParam<Product> page) {
        product.setLang(I18nMessage.getDbLang());
        product.setShopId(Constant.PLATFORM_SHOP_ID);
        product.setProdType(ProdType.PROD_TYPE_SCORE.value());
        IPage<Product> products = productService.pageByLang(page,product);
        return ServerResponseEntity.success(products);
    }

    @GetMapping("/info/{prodId}")
    @PreAuthorize("@pms.hasPermission('score:prod:info')")
    @ApiOperation(value = "获取信息", notes = "获取信息")
    @ApiImplicitParam(name = "prodId", value = "商品id")
    public ServerResponseEntity<Product> info(@PathVariable("prodId") Long prodId) {
        Product prod = productService.getProductByProdId(prodId, I18nMessage.getDbLang());
        List<Sku> skuList = skuService.listSkuAndSkuStock(prodId, I18nMessage.getDbLang());
        prod.setSkuList(skuList);
        // 获取语言列表
        List<ProdLang> prodLangList = prodLangService.list(new LambdaQueryWrapper<ProdLang>().eq(ProdLang::getProdId, prodId));
        prod.setProdLangList(prodLangList);
        return ServerResponseEntity.success(prod);
    }

    @DeleteMapping("/{prodId}")
    @PreAuthorize("@pms.hasPermission('score:prod:delete')")
    @ApiOperation(value = "删除积分商品", notes = "删除积分商品")
    @ApiImplicitParam(name = "prodId", value = "商品id")
    public ServerResponseEntity<Void> delete(@PathVariable("prodId") Long prodId) {
        Product dbProduct = productService.getProductByProdId(prodId, I18nMessage.getDbLang());
        if (Objects.equals(dbProduct.getStatus(), ProdStatusEnums.DELETE.getValue())) {
            // 该商品已经被删除
            throw new YamiShopBindException("yami.product.already.deleted");
        }
        List<Sku> dbSkus = skuService.listByProdId(dbProduct.getProdId(), I18nMessage.getDbLang());
        // 删除商品
        productService.removeProductByProdId(prodId);

        // 清除缓存
        productService.removeProdCacheByProdId(prodId);

        for (Sku sku : dbSkus) {
            skuService.removeSkuCacheBySkuId(sku.getSkuId(), sku.getProdId());
        }

        List<String> userIds = basketService.listUserIdByProdId(prodId);
        //清除购物车缓存
        basketService.removeCacheByUserIds(userIds);
        eventPublisher.publishEvent(new EsProductUpdateEvent(prodId, null, EsOperationType.DELETE));
        return ServerResponseEntity.success();
    }

    @GetMapping("/getOfflineHandleEventByProdId/{prodId}")
    @ApiOperation(value = "获取最新下线商品的事件", notes = "获取最新下线商品的事件")
    @ApiImplicitParam(name = "prodId", value = "商品id")
    public ServerResponseEntity<OfflineHandleEvent> getOfflineHandleEventByProdId(@PathVariable Long prodId) {
        OfflineHandleEvent offlineHandleEvent = offlineHandleEventService.getProcessingEventByHandleTypeAndHandleId(OfflineHandleEventType.PROD.getValue(), prodId);
        return ServerResponseEntity.success(offlineHandleEvent);
    }

    @PutMapping("/prodStatus")
    @ApiOperation(value = "更新商品状态", notes = "更新商品状态")
    public ServerResponseEntity<Void> shopStatus(@RequestBody ProductParam productParam) {
        Long prodId = productParam.getProdId();
        Integer prodStatus = productParam.getStatus();
        Product dbProduct = productService.getProductByProdId(prodId, I18nMessage.getDbLang());
        if (!(Objects.equals(dbProduct.getStatus(), ProdStatusEnums.NORMAL.getValue())
                || Objects.equals(dbProduct.getStatus(), ProdStatusEnums.SHOP_OFFLINE.getValue()))) {
            // 商品不在正常状态，修改失败
            throw new YamiShopBindException("yami.product.on.normal");
        }
        Product product = new Product();
        product.setProdId(prodId);
        product.setStatus(prodStatus);
        if (prodStatus == 1) {
            product.setPutawayTime(new Date());
        }
        dbProduct.setStatus(prodStatus);
        // 商品状态改变时的发送事件，让活动下线
//        applicationContext.publishEvent(new ProdChangeStatusEvent(dbProduct, dbProduct.getStatus()));

        productService.updateById(product);
        List<String> userIds = basketService.listUserIdByProdId(prodId);
        productService.removeProdCacheByProdId(prodId);
        //清除购物车缓存
        basketService.removeCacheByUserIds(userIds);
        eventPublisher.publishEvent(new EsProductUpdateEvent(prodId, null, EsOperationType.UPDATE));
        return ServerResponseEntity.success();
    }

    @PostMapping
    @PreAuthorize("@pms.hasPermission('score:prod:save')")
    @ApiOperation(value = "保存积分商品", notes = "保存积分商品")
    public ServerResponseEntity<String> save(@Valid @RequestBody ProductScoreParam productScoreParam) {
//        checkParam(productScoreParam);
        ProductParam productParam = mapperFacade.map(productScoreParam,ProductParam.class);
        productParam.setShopId(Constant.PLATFORM_SHOP_ID);
        //积分商品类型
        productParam.setProdType(ProdType.PROD_TYPE_SCORE.value());
        productService.saveProduct(productParam);
        eventPublisher.publishEvent(new EsProductUpdateEvent(productParam.getProdId(), null, EsOperationType.SAVE));
        return ServerResponseEntity.success();
    }

    @PutMapping
    @PreAuthorize("@pms.hasPermission('score:prod:update')")
    @ApiOperation(value = "修改积分商品", notes = "修改积分商品")
    public ServerResponseEntity<String> update(@Valid @RequestBody ProductScoreParam productScoreParam) {
//        checkParam(productScoreParam);
        Product dbProduct = productService.getProductInfo(productScoreParam.getProdId(), I18nMessage.getDbLang());
        if (Objects.equals(dbProduct.getStatus(), ProdStatusEnums.DELETE.getValue())) {
            // 该商品已经被删除
            throw new YamiShopBindException("yami.product.already.deleted");
        }
        List<String> userIds = basketService.listUserIdByProdId(productScoreParam.getProdId());
        List<Sku> dbSkus = skuService.listByProdId(dbProduct.getProdId(), I18nMessage.getDbLang());
        dbProduct.setSkuList(dbSkus);
        productScoreParam.setProdType(ProdType.PROD_TYPE_SCORE.value());
        ProductParam productParam = mapperFacade.map(productScoreParam,ProductParam.class);
        productParam.setShopId(Constant.PLATFORM_SHOP_ID);
        productService.updateProduct(productParam, dbProduct);
        productService.removeProdCacheByProdId(productParam.getProdId());
        //清除缓存
        for (Sku sku : dbSkus) {
            skuService.removeSkuCacheBySkuId(sku.getSkuId(), sku.getProdId());
        }
        //清除购物车缓存
        basketService.removeCacheByUserIds(userIds);
        eventPublisher.publishEvent(new EsProductUpdateEvent(dbProduct.getProdId(), null, EsOperationType.UPDATE));
        return ServerResponseEntity.success();
    }


//    private void checkParam(ProductScoreParam productScoreParam) {
//        //运费模板
//        Transport transport = transportService.getTransportAndAllItems(productScoreParam.getDeliveryTemplateId());
//        if (Objects.isNull(transport) && !DeliveryTemplateType.isUnifiedTemplate(productScoreParam.getDeliveryTemplateId())) {
//            // 产品运费模板不存在
//            throw new YamiShopBindException("yami.prod.transport.not.exist");
//        }
//        boolean isAllUnUse = true;
//        List<Sku> skuList = productScoreParam.getSkuList();
//        for (Sku sku : skuList) {
//            if (sku.getStatus() == 1) {
//                isAllUnUse = false;
//            }
//        }
//        if (isAllUnUse) {
//            // 至少要启用一种商品规格
//            throw new YamiShopBindException("yami.product.enable.sku");
//        }
//    }

}
