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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.enums.EsOperationType;
import com.yami.shop.bean.event.EsProductUpdateEvent;
import com.yami.shop.bean.model.ProdComm;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.ProdCommService;
import com.yami.shop.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

/**
 * 商品评论
 *
 * @author xwc
 * @date 2019-04-19 10:43:57
 */
@RestController
@AllArgsConstructor
@RequestMapping("/prod/prodComm" )
@Api(tags="商品评论接口")
public class ProdCommController {

    private final ProdCommService prodCommService;
    private final ProductService productService;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 分页查询
     * @param page 分页对象
     * @param prodComm 商品评论
     * @return 分页数据
     */
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('prod:prodComm:page')")
    @ApiOperation(value = "分页获取商品评价", notes = "分页获取商品评价")
    public ServerResponseEntity<IPage<ProdComm>> getProdCommPage(PageParam page, ProdComm prodComm) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        return ServerResponseEntity.success(prodCommService.getProdCommPage(page,prodComm,shopId));
    }

    @GetMapping("/info/{prodCommId}" )
    @ApiOperation(value = "通过id查询商品评论", notes = "通过id查询商品评论")
    public ServerResponseEntity<ProdComm> getById(@PathVariable("prodCommId" ) Long prodCommId) {
        ProdComm prodComm = prodCommService.getById(prodCommId);
        if(!StrUtil.isBlank(prodComm.getPics())){
            prodComm.setPicsArray(prodComm.getPics().split(","));
        }
        return ServerResponseEntity.success(prodComm);
    }

    /**
     * 新增商品评论
     * @param prodComm 商品评论
     * @return 是否新增成功
     */
    @SysLog("新增商品评论" )
    @PostMapping
    @ApiOperation(value = "新增商品评论", notes = "新增商品评论")
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid ProdComm prodComm) {
        prodComm.setStatus(1);
        boolean isSave = prodCommService.save(prodComm);
        return ServerResponseEntity.success(isSave);
    }

    /**
     * 修改商品评论
     * @param prodComm 商品评论
     * @return 是否修改成功
     */
    @SysLog("修改商品评论" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('prod:prodComm:update')")
    @ApiOperation(value = "修改商品评论", notes = "修改商品评论")
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid ProdComm prodComm) {
        Long shopId = productService.getById(prodComm.getProdId()).getShopId();
        if(!Objects.equals(shopId ,SecurityUtils.getShopUser().getShopId())){
            // 没有权限修改此商品
            throw new YamiShopBindException("yami.product.no.auth.update");
        }
        ProdComm newProdComm = new ProdComm();
        newProdComm.setProdCommId(prodComm.getProdCommId());
        newProdComm.setReplyTime(new Date());
        newProdComm.setReplyContent(prodComm.getReplyContent());
        newProdComm.setStatus(1);
        newProdComm.setReplySts(1);
        eventPublisher.publishEvent(new EsProductUpdateEvent(prodComm.getProdId(), null, EsOperationType.UPDATE_PROD_COMM));

        return ServerResponseEntity.success(prodCommService.updateById(newProdComm));
    }

    /**
     * 通过id删除商品评论
     * @param prodCommId id
     * @return 是否删除成功
     */
    @SysLog("删除商品评论" )
    @DeleteMapping("/{prodCommId}" )
    @PreAuthorize("@pms.hasPermission('prod:prodComm:delete')")
    @ApiOperation(value = "通过id删除商品评论", notes = "通过id删除商品评论")
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long prodCommId) {
        ProdComm prodComm = prodCommService.getById(prodCommId);
        if (Objects.isNull(prodComm)){
            throw new YamiShopBindException("yami.prod.common.invalid");
        }
        boolean isDelete = prodCommService.removeById(prodCommId);
        if (isDelete) {
            eventPublisher.publishEvent(new EsProductUpdateEvent(prodComm.getProdId(), null, EsOperationType.UPDATE_PROD_COMM));

        }
        return ServerResponseEntity.success(isDelete);
    }

}
