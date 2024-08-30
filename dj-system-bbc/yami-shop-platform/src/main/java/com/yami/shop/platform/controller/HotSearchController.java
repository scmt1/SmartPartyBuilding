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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.HotSearch;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.HotSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 *
 * @author lgh on 2019/03/27.
 */
@RestController
@RequestMapping("/platform/hotSearch")
@Api(tags = "热搜")
public class HotSearchController {

    @Autowired
    private HotSearchService hotSearchService;

    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('platform:hotSearch:page')")
    @ApiOperation(value = "分页获取热搜", notes = "分页获取热搜")
    public ServerResponseEntity<IPage<HotSearch>> page(HotSearch hotSearch, PageParam<HotSearch> page){
        IPage<HotSearch> hotSearchs = hotSearchService.page(page,new LambdaQueryWrapper<HotSearch>()
                .eq(HotSearch::getShopId, Constant.PLATFORM_SHOP_ID)
                .like(StrUtil.isNotBlank(hotSearch.getContent()), HotSearch::getContent,hotSearch.getContent())
                .like(StrUtil.isNotBlank(hotSearch.getTitle()), HotSearch::getTitle,hotSearch.getTitle())
                .eq(hotSearch.getStatus()!=null, HotSearch::getStatus,hotSearch.getStatus())
                .orderByAsc(HotSearch::getSeq)
        );
        return ServerResponseEntity.success(hotSearchs);
    }

    @GetMapping("/info/{id}")
    @PreAuthorize("@pms.hasPermission('platform:hotSearch:info')")
    @ApiOperation(value = "获取信息", notes = "获取信息")
    @ApiImplicitParam(name = "id", value = "热搜id")
    public ServerResponseEntity<HotSearch> info(@PathVariable("id") Long id){
        HotSearch hotSearch = hotSearchService.getById(id);
        return ServerResponseEntity.success(hotSearch);
    }

    @PostMapping
    @PreAuthorize("@pms.hasPermission('platform:hotSearch:save')")
    @ApiOperation(value = "保存", notes = "保存")
    public ServerResponseEntity<Void> save(@RequestBody @Valid HotSearch hotSearch){
        hotSearch.setRecDate(new Date());
        hotSearch.setShopId(Constant.PLATFORM_SHOP_ID);
        hotSearchService.save(hotSearch);
        //清除缓存
        hotSearchService.removeHotSearchDtoCacheByshopId(Constant.PLATFORM_SHOP_ID);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @PreAuthorize("@pms.hasPermission('platform:hotSearch:update')")
    @ApiOperation(value = "修改", notes = "修改")
    public ServerResponseEntity<Void> update(@RequestBody @Valid HotSearch hotSearch){
        hotSearchService.updateById(hotSearch);
        //清除缓存
        hotSearchService.removeHotSearchDtoCacheByshopId(Constant.PLATFORM_SHOP_ID);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('platform:hotSearch:delete')")
    @ApiOperation(value = "删除", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "热搜id列表")
    public ServerResponseEntity<Void> delete(@RequestBody List<Long> ids){
        hotSearchService.removeByIds(ids);
        //清除缓存
        hotSearchService.removeHotSearchDtoCacheByshopId(Constant.PLATFORM_SHOP_ID);
        return ServerResponseEntity.success();
    }
}
