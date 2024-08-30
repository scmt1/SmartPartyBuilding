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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.HotSearch;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.HotSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author lgh on 2019/03/27.
 */
@RestController
@RequestMapping("/admin/hotSearch")
@Api(tags = "热搜接口")
public class HotSearchController {

    @Autowired
    private HotSearchService hotSearchService;

    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('admin:hotSearch:page')")
    @ApiOperation(value = "分页获取热搜列表", notes = "分页获取热搜列表")
    public ServerResponseEntity<IPage<HotSearch>> page(HotSearch hotSearch,PageParam<HotSearch> page){

        IPage<HotSearch> hotSearchs = hotSearchService.page(page,new LambdaQueryWrapper<HotSearch>()
            .eq(HotSearch::getShopId, SecurityUtils.getShopUser().getShopId())
            .like(StrUtil.isNotBlank(hotSearch.getContent()), HotSearch::getContent,hotSearch.getContent())
                .like(StrUtil.isNotBlank(hotSearch.getTitle()), HotSearch::getTitle,hotSearch.getTitle())
            .eq(hotSearch.getStatus()!=null, HotSearch::getStatus,hotSearch.getStatus())
            .orderByAsc(HotSearch::getSeq)
        );
        return ServerResponseEntity.success(hotSearchs);
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据热搜Id获取热搜信息", notes = "根据热搜Id获取热搜信息")
    public ServerResponseEntity<HotSearch> info(@PathVariable("id") Long id){
        HotSearch hotSearch = hotSearchService.getById(id);
        if (!Objects.equals(SecurityUtils.getShopUser().getShopId(), hotSearch.getShopId())) {
            throw new YamiShopBindException("yami.no.auth");
        }
        return ServerResponseEntity.success(hotSearch);
    }

    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin:hotSearch:save')")
    @ApiOperation(value = "新增热搜", notes = "新增热搜")
    public ServerResponseEntity<Void> save(@RequestBody @Valid HotSearch hotSearch){
        hotSearch.setRecDate(new Date());
        hotSearch.setShopId(SecurityUtils.getShopUser().getShopId());
        hotSearchService.save(hotSearch);
        //清除缓存
        hotSearchService.removeHotSearchDtoCacheByshopId(SecurityUtils.getShopUser().getShopId());
        return ServerResponseEntity.success();
    }

    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin:hotSearch:update')")
    @ApiOperation(value = "更新热搜", notes = "更新热搜")
    public ServerResponseEntity<Void> update(@RequestBody @Valid HotSearch hotSearch){
        hotSearchService.updateById(hotSearch);
        //清除缓存
        hotSearchService.removeHotSearchDtoCacheByshopId(SecurityUtils.getShopUser().getShopId());
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('admin:hotSearch:delete')")
    @ApiOperation(value = "根据热搜id列表批量删除热搜", notes = "根据热搜id列表批量删除热搜")
    public ServerResponseEntity<Void> delete(@RequestBody List<Long> ids){
        hotSearchService.removeByIds(ids);
        //清除缓存
        hotSearchService.removeHotSearchDtoCacheByshopId(SecurityUtils.getShopUser().getShopId());
        return ServerResponseEntity.success();
    }
}
