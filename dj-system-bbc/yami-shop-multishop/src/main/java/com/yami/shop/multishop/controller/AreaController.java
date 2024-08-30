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
import com.yami.shop.bean.dto.AreaDto;
import com.yami.shop.bean.model.Area;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.dao.AreaMapper;
import com.yami.shop.service.AreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lgh on 2018/10/26.
 */
@RestController
@RequestMapping("/admin/area")
@Api(tags = "地区信息接口")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @GetMapping("/list")
    @ApiOperation(value = "获取省市区地区信息列表（可传入地区名称搜索）", notes = "分页获取省市区地区信息列表（可传入地区名称搜索）")
    public ServerResponseEntity<List<Area>> list(Area area) {
        List<Area> areas = areaService.list(new LambdaQueryWrapper<Area>()
                .like(area.getAreaName() != null, Area::getAreaName, area.getAreaName())
                .ne(Area::getLevel, 0));
        return ServerResponseEntity.success(areas);
    }

    @GetMapping("/listByPid")
    @ApiOperation(value = "通过父级id获取区域列表", notes = "通过父级id获取区域列表")
    public ServerResponseEntity<List<Area>> listByPid(Long pid, Integer level) {
        List<Area> list = areaService.listByPid(pid, level);
        return ServerResponseEntity.success(list);
    }

    @GetMapping("/areaListInfo")
    @ApiOperation(value = "获取省市区信息", notes = "获取省市区信息")
    public ServerResponseEntity<List<AreaDto>> getAreaListInfo() {
        return ServerResponseEntity.success(areaService.getAreaListInfo());
    }

    @GetMapping("/listAreaOfEnable")
    @ApiOperation(value = "获取可用的省市区列表(完整路径）", notes = "获取可用的省市区列表（完整路径）")
    public ServerResponseEntity<List<AreaDto>> listAreaOfEnable() {
        List<AreaDto> list = areaService.listAreaOfEnable();
        return ServerResponseEntity.success(list);
    }

    @GetMapping("/areaList")
    @ApiOperation(value = "获取可用的区域省市区信息（四级地址列表）", notes = "获取可用的区域省市区信息")
    public ServerResponseEntity<List<AreaDto>> getAllAreaList() {
        return ServerResponseEntity.success(areaService.getAllAreaList());
    }
}
