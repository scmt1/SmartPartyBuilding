/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.multishop.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.IndexImg;
import com.yami.shop.bean.model.Product;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.IndexImgService;
import com.yami.shop.service.ProductService;
import io.swagger.annotations.Api;
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
@RequestMapping("/admin/indexImg")
@Api(tags = "轮播图接口")
public class IndexImgController {

    @Autowired
    private IndexImgService indexImgService;

    @Autowired
    private ProductService productService;

    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('admin:indexImg:page')")
    @ApiOperation(value = "分页获取轮播图列表", notes = "分页获取轮播图列表")
    public ServerResponseEntity<IPage<IndexImg>> page(IndexImg indexImg, PageParam<IndexImg> page) {
        IPage<IndexImg> indexImgPage = indexImgService.page(page,
                new LambdaQueryWrapper<IndexImg>()
                        .eq(indexImg.getStatus() != null, IndexImg::getStatus, indexImg.getStatus())
                        .eq(IndexImg::getShopId, SecurityUtils.getShopUser().getShopId())
                        .eq(!Objects.isNull(indexImg.getImgType()),IndexImg::getImgType, indexImg.getImgType())
                        .orderByAsc(IndexImg::getSeq)
                        .orderByDesc(IndexImg::getStatus)
                        .orderByAsc(IndexImg::getImgType)
                        );
        return ServerResponseEntity.success(indexImgPage);
    }

    @GetMapping("/info/{imgId}")
    @ApiOperation(value = "根据轮播图Id获取轮播图", notes = "根据轮播图Id获取轮播图")
    public ServerResponseEntity<IndexImg> info(@PathVariable("imgId") Long imgId) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
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
    @PreAuthorize("@pms.hasPermission('admin:indexImg:save')")
    @ApiOperation(value = "新增轮播图", notes = "新增轮播图")
    public ServerResponseEntity<Void> save(@RequestBody @Valid IndexImg indexImg) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
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
        checkProdStatus(indexImg);
        indexImgService.save(indexImg);
        indexImgService.removeIndexImgCacheByShopId(shopId);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin:indexImg:update')")
    @ApiOperation(value = "更新轮播图", notes = "更新轮播图")
    public ServerResponseEntity<Void> update(@RequestBody @Valid IndexImg indexImg) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        indexImg.setShopId(shopId);
        checkProdStatus(indexImg);
        indexImgService.saveOrUpdate(indexImg);
        // 移除缓存
        indexImgService.removeIndexImgCacheByShopId(shopId);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('admin:indexImg:delete')")
    @ApiOperation(value = "根据轮播图id删除轮播图", notes = "根据轮播图id删除轮播图")
    public ServerResponseEntity<Void> delete(@RequestBody Long[] ids) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        indexImgService.deleteIndexImgsByIds(ids,shopId);
        indexImgService.removeIndexImgCacheByShopId(shopId);
        return ServerResponseEntity.success();
    }

    private void checkProdStatus(IndexImg indexImg) {
        if (!Objects.equals(indexImg.getType(), 0)) {
            return;
        }
        if (Objects.isNull(indexImg.getRelation())) {
            throw new YamiShopBindException("yami.score.select.num");
        }
        Product product = productService.getById(indexImg.getRelation());
        if (Objects.isNull(product)) {
            throw new YamiShopBindException("yami.product.not.exist");
        }
        if (!Objects.equals(product.getStatus(), 1)) {
            //该商品未上架，请选择别的商品
            throw new YamiShopBindException("yami.product.not.shelf");
        }
    }

}
