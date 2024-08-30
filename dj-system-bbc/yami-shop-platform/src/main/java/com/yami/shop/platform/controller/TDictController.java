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
import com.yami.shop.bean.model.TDict;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.platform.util.SecurityUtils;
import com.yami.shop.service.TDictDataService;
import com.yami.shop.service.TDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


/**
 *
 *
 * @author LGH
 * @date 2022-09-22 15:48:48
 */
@RestController
@RequestMapping("/tDict")
@Api(tags = "")
public class TDictController {

    @Autowired
    private TDictService tDictService;
    @Autowired
    private TDictDataService tDictDataService;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public ServerResponseEntity<List<TDict>> getAll() {
        List<TDict> list = tDictService.findAllOrderBySortOrder();
        return ServerResponseEntity.success(list);
    }

    @GetMapping("/page")
    @ApiOperation(value = "获取列表", notes = "分页获取列表")
    public ServerResponseEntity<IPage<TDict>> getTDictPage(PageParam<TDict> page, TDict tDict) {
        return ServerResponseEntity.success(tDictService.page(page, new LambdaQueryWrapper<TDict>()));
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "获取", notes = "根据id获取")
    @ApiImplicitParam(name = "id", value = "", required = true, dataType = "Long")
    public ServerResponseEntity<TDict> getById(@PathVariable("id") Long id) {
        return ServerResponseEntity.success(tDictService.getById(id));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "保存", notes = "保存")
    public ServerResponseEntity<Boolean> save(@RequestBody TDict tDict) {
        Long userId = SecurityUtils.getSysUser().getUserId();
        if (tDictService.findByType(tDict.getType()) != null) {
            return ServerResponseEntity.showFailMsg("字典类型已存在");
        }
        tDict.setCreateBy(userId);
        tDict.setCreateTime(new Date());
        return ServerResponseEntity.success(tDictService.save(tDict));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "更新", notes = "更新")
    public ServerResponseEntity<Boolean> updateById(@RequestBody TDict tDict) {
        TDict old = tDictService.getById(tDict.getId());
        Long userId = SecurityUtils.getSysUser().getUserId();
        // 若type修改判断唯一
        if (!old.getType().equals(tDict.getType()) && tDictService.findByType(tDict.getType()) != null) {
            return ServerResponseEntity.showFailMsg("字典类型已存在");
        }
        tDict.setUpdateBy(userId);
        tDict.setUpdateTime(new Date());
        return ServerResponseEntity.success(tDictService.updateById(tDict));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "根据id删除")
    @Transactional(rollbackFor = Exception.class)
    @ApiImplicitParam(name = "id", value = "", required = true, dataType = "Long")
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long id) {
        tDictDataService.removeByDictId(id);
        return ServerResponseEntity.success(tDictService.removeById(id));
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "搜索字典")
    public ServerResponseEntity<List<TDict>> searchPermissionList(@RequestParam String key) {
        List<TDict> list = tDictService.findByTitleOrTypeLike(key);
        return ServerResponseEntity.success(list);
    }
}
