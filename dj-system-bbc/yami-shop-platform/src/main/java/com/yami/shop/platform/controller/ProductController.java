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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.enums.*;
import com.yami.shop.bean.event.*;
import com.yami.shop.bean.model.OfflineHandleEvent;
import com.yami.shop.bean.model.ProdParameter;
import com.yami.shop.bean.model.Product;
import com.yami.shop.bean.model.Sku;
import com.yami.shop.bean.param.NotifyTemplateParam;
import com.yami.shop.bean.param.OfflineHandleEventAuditParam;
import com.yami.shop.bean.param.ProductExportParam;
import com.yami.shop.bean.param.ProductParam;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
//import com.yami.shop.delivery.common.model.SameCity;
//import com.yami.shop.seckill.common.service.SeckillService;
import com.yami.shop.security.platform.util.SecurityUtils;
import com.yami.shop.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 商品列表、商品发布controller
 *
 * @author lgh
 */
@RestController
@RequestMapping("/prod/prod")
@AllArgsConstructor
@Api(tags = "商品")
public class ProductController {

    private final SkuService skuService;
    private final BasketService basketService;
    private final ProductService productService;
    private final ProductExcelService productExcelService;
    private final ApplicationEventPublisher eventPublisher;
    private final ApplicationContext applicationContext;
    private final OfflineHandleEventService offlineHandleEventService;
    private final SupplierProdService supplierProdService;
    private final ProdParameterService prodParameterService;
//    private final SeckillService seckillService;

//    @GetMapping("/seckills")
//    @ApiOperation(value = "分页获取秒杀商品信息")
//    public ServerResponseEntity<IPage<Product>> seckills(ProductParam product, PageParam<Product> page){
//        product.setLang(I18nMessage.getLang());
//        IPage<Product> productIPage = seckillService.pageSeckillNormalProd(page, product);
//        return ServerResponseEntity.success(productIPage);
//    }

    /**
     * 处理下活动商品的价格
     *
     * @param product
     * @param products
     */
    private void processActivityProdPrice(ProductParam product, List<Product> products) {
        Map<Integer, List<Product>> prodMap = products.stream().collect(Collectors.groupingBy(Product::getProdType));
        if (prodMap.containsKey(ProdType.PROD_TYPE_SECKILL.value())) {
            applicationContext.publishEvent(new ProcessActivityProdPriceEvent(product, prodMap.get(ProdType.PROD_TYPE_SECKILL.value())));
        }

        if (prodMap.containsKey(ProdType.PROD_TYPE_GROUP.value())) {
            applicationContext.publishEvent(new ProcessActivityProdPriceEvent(product, prodMap.get(ProdType.PROD_TYPE_GROUP.value())));
        }
    }

    @GetMapping("/info/{prodId}")
    @PreAuthorize("@pms.hasPermission('prod:prod:info')")
    @ApiOperation(value = "获取商品信息", notes = "获取商品信息")
    @ApiImplicitParam(name = "prodId", value = "商品id")
    public ServerResponseEntity<Product> info(@PathVariable("prodId") Long prodId) {
        Product prod = productService.getProductById(prodId);
        List<Sku> skuList = skuService.listSkuAndSkuStock(prodId, I18nMessage.getDbLang());
        prod.setSkuList(skuList);
//        // 获取语言列表
//        List<ProdLang> prodLangList = prodLangService.list(new LambdaQueryWrapper<ProdLang>().eq(ProdLang::getProdId, prodId));
//        prod.setProdLangList(prodLangList);
        List<ProdParameter> prodParameters = prodParameterService.listParameter(prodId, I18nMessage.getDbLang());
        prod.setProdParameterList(prodParameters);
        return ServerResponseEntity.success(prod);
    }

    @DeleteMapping("/{prodId}")
    @PreAuthorize("@pms.hasPermission('prod:prod:update')")
    @ApiOperation(value = "平台删除商品", notes = "平台删除商品")
    @ApiImplicitParam(name = "prodId", value = "商品id")
    public ServerResponseEntity<Void> delete(@PathVariable("prodId") Long prodId) {
        this.checkBeforeDeleteProduct(prodId);
        Product dbProduct = productService.getProductByProdId(prodId, I18nMessage.getDbLang());
        List<Sku> dbSkus = skuService.listByProdId(dbProduct.getProdId(), I18nMessage.getDbLang());
        List<Long> supplierIds = supplierProdService.listSupplierIdByProdId(prodId);
        // 没用到，又不知道干嘛的，先注释掉
//        List<Long> takeStockIds = takeStockProdService.listInventoryByProdId(prodId);
        // 删除商品
        productService.removeProductByProdId(prodId);
        //清除供应商商品列表缓存
        if (CollectionUtils.isNotEmpty(supplierIds)) {
            for (Long supplierId : supplierIds) {
                supplierProdService.removeCacheBySupplierId(supplierId);
            }
        }
        // 商品状态改变时的发送事件，让活动下线
        applicationContext.publishEvent(new ProdChangeStatusEvent(dbProduct, ProdStatusEnums.DELETE.getValue()));
        //清除商品缓存
        productService.removeProdCacheByProdId(prodId);

        //清除sku缓存
        for (Sku sku : dbSkus) {
            skuService.removeSkuCacheBySkuId(sku.getSkuId(), sku.getProdId());
        }
        List<String> userIds = basketService.listUserIdByProdId(prodId);

        //清除购物车缓存
        basketService.removeCacheByUserIds(userIds);
        // 删除商品时，改变分销设置,团购订单处理。。。
        applicationContext.publishEvent(new ProdChangeEvent(dbProduct));
        eventPublisher.publishEvent(new EsProductUpdateEvent(prodId, null, EsOperationType.DELETE));
        return ServerResponseEntity.success();
    }

    @GetMapping("/listProdByIdsAndType")
    @ApiOperation(value = "获取商品信息列表", notes = "获取商品信息列表")
    public ServerResponseEntity<List<Product>> listProdByIdsAndType(ProductParam product) {
        product.setLang(I18nMessage.getDbLang());
        List<Product> products = productService.listProdByIdsAndType(product);
        processActivityProdPrice(product, products);
        return ServerResponseEntity.success(products);
    }

    @PutMapping("/waterSoldNum")
    @ApiOperation(value = "更新注水销量", notes = "更新注水销量")
    @ApiImplicitParam(name = "prodId", value = "商品id")
    public ServerResponseEntity<Void> updateWaterSoldNum(Integer waterSoldNum, Long prodId) {
        if (waterSoldNum == null) {
            waterSoldNum = 0;
        }
        productService.updateWaterSaleNum(waterSoldNum, prodId);
        eventPublisher.publishEvent(new EsProductUpdateEvent(prodId, null, EsOperationType.UPDATE_SOLD_NUM));
        return ServerResponseEntity.success();
    }

    @PostMapping("/offline")
    @PreAuthorize("@pms.hasPermission('prod:prod:update')")
    @ApiOperation(value = "下线商品", notes = "下线商品")
    public ServerResponseEntity<Void> offline(@RequestBody OfflineHandleEvent offlineHandleEvent) {
        Product dbProduct = productService.getProductByProdId(offlineHandleEvent.getHandleId(), I18nMessage.getDbLang());
        if (dbProduct == null) {
            // 未找到刚商品的信息
            throw new YamiShopBindException("yami.product.not.exist");
        }
        Long sysUserId = SecurityUtils.getSysUser().getUserId();
        productService.offline(offlineHandleEvent.getHandleId(), offlineHandleEvent.getOfflineReason(), sysUserId);

        // 商品状态改变时的发送事件，让活动下线
        applicationContext.publishEvent(new ProdChangeStatusEvent(dbProduct, ProdStatusEnums.PLATFORM_OFFLINE.getValue()));
        List<String> userIds = basketService.listUserIdByProdId(dbProduct.getProdId());
        //清除购物车缓存
        basketService.removeCacheByUserIds(userIds);
        // 移除缓存
        productService.removeProdCacheByProdId(dbProduct.getProdId());
        eventPublisher.publishEvent(new EsProductUpdateEvent(dbProduct.getProdId(), null, EsOperationType.UPDATE));
        //发送商品下架提醒给商家
        NotifyTemplateParam shopParam = new NotifyTemplateParam();
        shopParam.setShopId(dbProduct.getShopId());
        shopParam.setProdId(offlineHandleEvent.getHandleId());
        shopParam.setProdName(dbProduct.getProdName());
        shopParam.setSendType(SendType.PRODUCT_OFFLINE.getValue());
        applicationContext.publishEvent(new SendMessageEvent(shopParam));
        return ServerResponseEntity.success();
    }

    @GetMapping("/getOfflineHandleEventByProdId/{prodId}")
    @ApiOperation(value = "获取最新下线商品的事件", notes = "获取最新下线商品的事件")
    @ApiImplicitParam(name = "prodId", value = "商品id")
    public ServerResponseEntity<OfflineHandleEvent> getOfflineHandleEventByProdId(@PathVariable Long prodId) {
        OfflineHandleEvent offlineHandleEvent = offlineHandleEventService.getProcessingEventByHandleTypeAndHandleId(OfflineHandleEventType.PROD.getValue(), prodId);
        return ServerResponseEntity.success(offlineHandleEvent);
    }

    @PostMapping("/prodOfflineAudit")
    @ApiOperation(value = "审核违规下架的商品", notes = "审核违规下架的商品")
    public ServerResponseEntity<Void> prodOfflineAudit(@RequestBody OfflineHandleEventAuditParam offlineHandleEventAuditParam) {
        Long userId = SecurityUtils.getSysUser().getUserId();
        productService.prodAudit(offlineHandleEventAuditParam, userId);

        // 移除缓存
        productService.removeProdCacheByProdId(offlineHandleEventAuditParam.getHandleId());
        eventPublisher.publishEvent(new EsProductUpdateEvent(offlineHandleEventAuditParam.getHandleId(), null, EsOperationType.UPDATE));
        return ServerResponseEntity.success();
    }

    @PostMapping("/auditProd")
    @ApiOperation(value = "审核待审核的商品", notes = "商品审核开关打开后，新发布的或要上架的商品处于的待审核状态")
    public ServerResponseEntity<Void> auditProd(@RequestBody OfflineHandleEvent offlineHandleEvent) {
        Long prodId = offlineHandleEvent.getHandleId();
        Product dbProduct = productService.getProductByProdId(prodId, I18nMessage.getDbLang());
        if (Objects.isNull(dbProduct)) {
            // 未找到刚商品的信息
            throw new YamiShopBindException("yami.product.not.exist");
        }
        if (!Objects.equals(dbProduct.getStatus(), ProdStatusEnums.AUDIT.getValue())) {
            // 商品状态已改变，请刷新页面
            throw new YamiShopBindException("yami.prod.status.change");
        }
        offlineHandleEvent.setHandlerId(SecurityUtils.getSysUser().getUserId());
        productService.handleAuditProd(dbProduct, offlineHandleEvent);
        // 移除缓存
        productService.removeProdCacheByProdId(dbProduct.getProdId());
        eventPublisher.publishEvent(new EsProductUpdateEvent(dbProduct.getProdId(), null, EsOperationType.UPDATE));
        return ServerResponseEntity.success();
    }

    @PutMapping("/toTop/{id}")
    @ApiOperation(value = "置顶商品", notes = "置顶商品")
    @ApiImplicitParam(name = "id", value = "商品id")
    public ServerResponseEntity<Void> removeById(@PathVariable Long id) {
        Product product = productService.getProductByProdId(id, I18nMessage.getDbLang());
        if (!Objects.equals(product.getStatus(), ProdStatusEnums.NORMAL.getValue())) {
            // 只能置顶已上架的商品
            throw new YamiShopBindException("yami.prod.set.top.check");
        }
        product.setIsTop(Objects.equals(product.getIsTop(), 0) ? 1 : 0);
        productService.updateById(product);
        // 移除缓存
        productService.removeProdCacheByProdId(id);
        eventPublisher.publishEvent(new EsProductUpdateEvent(product.getProdId(), null, EsOperationType.UPDATE));
        return ServerResponseEntity.success();
    }

    private void checkBeforeDeleteProduct(Long prodId) {
        GetComboProdCountEvent getComboProdCountEvent = new GetComboProdCountEvent();
        getComboProdCountEvent.setProdId(prodId);
        applicationContext.publishEvent(getComboProdCountEvent);
        if (getComboProdCountEvent.getCount() > 0) {
            //参加以下活动的商品不能被删除：优惠套餐
            throw new YamiShopBindException("yami.combo.prod.not.delete");
        }
        GetGiveawayProdCountEvent getGiveawayProdCountEvent = new GetGiveawayProdCountEvent();
        getGiveawayProdCountEvent.setProdId(prodId);
        applicationContext.publishEvent(getGiveawayProdCountEvent);
        if (getGiveawayProdCountEvent.getCount() > 0) {
            //参加以下活动的商品不能被删除：赠品
            throw new YamiShopBindException("yami.giveaway.prod.not.delete");
        }
    }
}
