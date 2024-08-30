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
import com.yami.shop.bean.dto.AreaDto;
import com.yami.shop.bean.enums.AreaLevelEnum;
import com.yami.shop.bean.model.Area;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.AreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @author lgh on 2018/10/26.
 */
@RestController
@Api(tags = "省市区")
@RequestMapping("/admin/area")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public ServerResponseEntity<IPage<Area>> page(Area area, PageParam<Area> page) {
        IPage<Area> sysUserPage = areaService.page(page, new LambdaQueryWrapper<Area>());
        return ServerResponseEntity.success(sysUserPage);
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取列表", notes = "获取列表")
    public ServerResponseEntity<List<Area>> list(Area area) {
        List<Area> areas = areaService.list(new LambdaQueryWrapper<Area>()
                .like(area.getAreaName() != null, Area::getAreaName, area.getAreaName())
                .lt(!Objects.isNull(area.getMaxGrade()),Area::getLevel,area.getMaxGrade())
                .ne(Area::getLevel, 0)
        );
        return ServerResponseEntity.success(areas);
    }

    @GetMapping("/listByPid")
    @ApiOperation(value = "通过父级id获取列表", notes = "通过父级id获取列表")
    public ServerResponseEntity<List<Area>> listByPid(Long pid, Integer level) {
        List<Area> list = areaService.listByPid(pid, level);
        return ServerResponseEntity.success(list);
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "获取信息", notes = "获取信息")
    public ServerResponseEntity<Area> info(@PathVariable("id") Long id) {
        Area area = areaService.getById(id);
        return ServerResponseEntity.success(area);
    }

    @PostMapping
    @ApiOperation(value = "保存", notes = "保存")
    public ServerResponseEntity<Void> save(@Valid @RequestBody Area area) {
        if (area.getParentId() != null && !Objects.equals(area.getParentId(), 0L)) {
            Area parentArea = areaService.getById(area.getParentId());
            area.setLevel(parentArea.getLevel() + 1);
            areaService.removeAreaCacheByParentId(area.getParentId(), null);
            // 因为获取地址的时候，过滤掉了没有三级地址的一级地址，情况缓存的时候，一级地址也需要清空一下
            if (Objects.equals(area.getLevel(), AreaLevelEnum.THIRD_LEVEL.value())) {
                areaService.removeAreaCacheByParentId(0L, null);
            }
        }
        hasSameName(area);
        areaService.removeAreaListCache();
        areaService.save(area);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "修改", notes = "修改")
    public ServerResponseEntity<Void> update(@Valid @RequestBody Area area) {
        Area areaDb = areaService.getById(area.getAreaId());
        // 判断当前省市区级别，如果是1级、2级则不能修改级别，不能修改成别人的下级
        if(Objects.equals(areaDb.getLevel(),AreaLevelEnum.FIRST_LEVEL.value()) && !Objects.equals(area.getLevel(),AreaLevelEnum.FIRST_LEVEL.value())){
            throw new YamiShopBindException("不能改变一级行政地区的级别");
        }
        if(Objects.equals(areaDb.getLevel(),AreaLevelEnum.SECOND_LEVEL.value()) && !Objects.equals(area.getLevel(),AreaLevelEnum.SECOND_LEVEL.value())){
            throw new YamiShopBindException("不能改变二级行政地区的级别");
        }
        hasSameName(area);
        areaService.updateById(area);
        areaService.removeAreaCacheByParentId(area.getParentId(), null);
        areaService.removeAreaListCache();
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ServerResponseEntity<Void> delete(@PathVariable Long id) {
        if (areaService.count(new LambdaQueryWrapper<Area>().eq(Area::getParentId,id)) > 0) {
            // 请先删除子地区
            throw new YamiShopBindException("yami.area.delete");
        }

        Area area = areaService.getById(id);
        areaService.removeById(id);
        areaService.removeAreaCacheByParentId(area.getParentId(), null);
        areaService.removeAreaListCache();
        // 因为获取地址的时候，过滤掉了没有三级地址的一级地址，情况缓存的时候，一级地址也需要清空一下
        if (Objects.equals(area.getLevel(), AreaLevelEnum.THIRD_LEVEL.value())) {
            areaService.removeAreaCacheByParentId(0L, null);
        }
        return ServerResponseEntity.success();
    }

    @GetMapping("/areaListInfo")
    @ApiOperation(value = "获取省市信息", notes = "获取省市信息")
    public ServerResponseEntity<List<AreaDto>> getAreaListInfo() {
        return ServerResponseEntity.success(areaService.getAreaListInfo());
    }

    @GetMapping("/listAreaOfEnable")
    @ApiOperation(value = "获取可用的省市区列表(完整路径）", notes = "获取可用的省市区列表（完整路径）")
    public ServerResponseEntity<List<AreaDto>> listAreaOfEnable() {
        return ServerResponseEntity.success(areaService.listAreaOfEnable());
    }

    @GetMapping("/areaList")
    @ApiOperation(value = "获取可用的区域省市区信息（四级地址列表）", notes = "获取可用的区域省市区信息")
    public ServerResponseEntity<List<AreaDto>> getAllAreaList() {
        return ServerResponseEntity.success(areaService.getAllAreaList());
    }

    private void hasSameName(Area area) {
        int count = areaService.count(new LambdaQueryWrapper<Area>()
                .eq(Area::getParentId, area.getParentId())
                .eq(Area::getAreaName, area.getAreaName())
                .ne(Objects.nonNull(area.getAreaId()) && !Objects.equals(area.getAreaId(), Constant.ZERO_LONG), Area::getAreaId, area.getAreaId())
        );
        if (count > 0) {
            throw new YamiShopBindException("该地区已存在");
        }
    }
}
