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
import com.yami.shop.bean.model.IndexImg;
import com.yami.shop.bean.model.Product;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.AttachFileService;
import com.yami.shop.service.IndexImgService;
import com.yami.shop.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

/**
 * @author lgh on 2018/11/26.
 */
@RestController
@RequestMapping("/platform/indexImg")
@Api(tags = "轮播图")
public class IndexImgController {

    @Autowired
    private IndexImgService indexImgService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AttachFileService attachFileService;

    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('platform:indexImg:page')")
    @ApiOperation(value = "分页获取", notes = "分页获取")
    public ServerResponseEntity<IPage<IndexImg>> page(IndexImg indexImg, PageParam<IndexImg> page) {
        IPage<IndexImg> indexImgPage = indexImgService.page(page,
                new LambdaQueryWrapper<IndexImg>()
                        .eq(indexImg.getStatus() != null,IndexImg::getStatus,indexImg.getStatus())
                        .eq(IndexImg::getShopId, Constant.PLATFORM_SHOP_ID)
                        .eq(!Objects.isNull(indexImg.getImgType()),IndexImg::getImgType,indexImg.getImgType())
                        .orderByAsc(IndexImg::getSeq)
                        .orderByDesc(IndexImg::getStatus)
                        .orderByAsc(IndexImg::getImgType));
        return ServerResponseEntity.success(indexImgPage);
    }

    @GetMapping("/info/{imgId}")
    @PreAuthorize("@pms.hasPermission('platform:indexImg:info')")
    @ApiOperation(value = "获取信息", notes = "获取信息")
    @ApiImplicitParam(name = "imgId", value = "轮播图id")
    public ServerResponseEntity<IndexImg> info(@PathVariable("imgId") Long imgId) {
        Long shopId = Constant.PLATFORM_SHOP_ID;
        IndexImg indexImg = indexImgService.getOne(new LambdaQueryWrapper<IndexImg>().eq(IndexImg::getShopId, shopId).eq(IndexImg::getImgId, imgId));
        if (Objects.nonNull(indexImg.getRelation())) {
            Product product = productService.getProductByProdId(indexImg.getRelation(), I18nMessage.getDbLang());
            if (product !=null) {
                indexImg.setPic(product.getPic());
                indexImg.setProdName(product.getProdName());
            }
        }
        return ServerResponseEntity.success(indexImg);
    }

    @PostMapping
    @PreAuthorize("@pms.hasPermission('platform:indexImg:save')")
    @ApiOperation(value = "保存", notes = "保存")
    public ServerResponseEntity<Void> save(@RequestBody @Valid IndexImg indexImg) {
        Long shopId = Constant.PLATFORM_SHOP_ID;
        indexImg.setShopId(shopId);
        indexImg.setUploadTime(new Date());
        int count = indexImgService.count(new LambdaQueryWrapper<IndexImg>()
                .eq(IndexImg::getImgType, indexImg.getImgType())
                .eq(IndexImg::getShopId, shopId)
        );
        if (count >= Constant.MAX_INDEX_IMG_NUM) {
            // 该平台的轮播图已达到最大数量，不能再进行新增操作
            throw new YamiShopBindException("yami.index.img.reached.limit");
        }
        indexImgService.save(indexImg);
        indexImgService.removeIndexImgCacheByShopId(shopId);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @PreAuthorize("@pms.hasPermission('platform:indexImg:update')")
    @ApiOperation(value = "修改", notes = "修改")
    public ServerResponseEntity<Void> update(@RequestBody @Valid IndexImg indexImg) {
        indexImgService.saveOrUpdate(indexImg);
        // 移除缓存
        indexImgService.removeIndexImgCacheByShopId(Constant.PLATFORM_SHOP_ID);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('platform:indexImg:delete')")
    @ApiOperation(value = "删除", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "轮播图id")
    public ServerResponseEntity<Void> delete(@RequestBody Long[] ids) {
        indexImgService.deleteIndexImgsByIds(ids, Constant.PLATFORM_SHOP_ID);
        // 移除缓存
        indexImgService.removeIndexImgCacheByShopId(Constant.PLATFORM_SHOP_ID);
        return ServerResponseEntity.success();
    }

}
