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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.TDict;
import com.yami.shop.bean.model.TDictData;
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

import javax.validation.Valid;
import java.util.Date;
import java.util.List;


/**
 *
 *
 * @author LGH
 * @date 2022-09-23 09:25:14
 */
@RestController
@RequestMapping("/tDictData")
@Api(tags = "")
public class TDictDataController {

    @Autowired
    private TDictDataService tDictDataService;
    @Autowired
    private TDictService tDictService;

    @GetMapping("/page")
    @ApiOperation(value = "获取列表", notes = "分页获取列表")
    public ServerResponseEntity<IPage<TDictData>> getTDictDataPage(PageParam<TDictData> page, TDictData tDictData) {
        QueryWrapper<TDictData> queryWrapper = new QueryWrapper<>();
        // 模糊搜素
        if (StrUtil.isNotBlank(tDictData.getTitle())) {
            queryWrapper.like("t_dict_data.title",'%' + tDictData.getTitle() + '%');
        }
        // 状态
        if (tDictData.getStatus() != null) {
            queryWrapper.eq("t_dict_data.status", tDictData.getStatus());
        }
        // 所属字典
        if (tDictData.getDictId() != null) {
            queryWrapper.eq("t_dict_data.dict_id", tDictData.getDictId());
        }
        queryWrapper.orderByAsc("t_dict_data.sort_order");
        return ServerResponseEntity.success(tDictDataService.page(page, queryWrapper));
    }

    @GetMapping("/pageByDictId")
    @ApiOperation(value = "获取列表", notes = "分页获取列表")
    public ServerResponseEntity<IPage<TDictData>> getTDictDataPageByDictId(PageParam<TDictData> page, TDictData tDictData) {
        return ServerResponseEntity.success(tDictDataService.page(page, new LambdaQueryWrapper<TDictData>()));
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "获取", notes = "根据id获取")
    @ApiImplicitParam(name = "id", value = "", required = true, dataType = "Long")
    public ServerResponseEntity<TDictData> getById(@PathVariable("id") Long id) {
        return ServerResponseEntity.success(tDictDataService.getById(id));
    }

    @GetMapping("/getByType")
    @ApiOperation(value = "获取", notes = "通过类型获取")
    //@ApiImplicitParam(name = "type", value = "", required = true, dataType = "String")
    public ServerResponseEntity<List<TDictData>> getByType(@RequestParam(value = "type") String type) {
        TDict dict = tDictService.findByType(type);
        if (dict == null) {
            return ServerResponseEntity.showFailMsg("字典类型 " + type + " 不存在");
        }
        List<TDictData> list = tDictDataService.findByDictId(dict.getId());
        return ServerResponseEntity.success(list);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "保存", notes = "保存")
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid TDictData tDictData) {
        Long userId = SecurityUtils.getSysUser().getUserId();
        TDict dict = tDictService.getById(tDictData.getDictId());
        if (dict == null) {
            return ServerResponseEntity.showFailMsg("字典类型id不存在");
        }
        tDictData.setCreateBy(userId);
        tDictData.setCreateTime(new Date());
        return ServerResponseEntity.success(tDictDataService.save(tDictData));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "更新", notes = "更新")
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid TDictData tDictData) {
        Long userId = SecurityUtils.getSysUser().getUserId();
        tDictData.setUpdateBy(userId);
        tDictData.setUpdateTime(new Date());
        return ServerResponseEntity.success(tDictDataService.updateById(tDictData));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "根据id删除")
    @ApiImplicitParam(name = "id", value = "", required = true, dataType = "Long")
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long id) {
        return ServerResponseEntity.success(tDictDataService.removeById(id));
    }

    @DeleteMapping("/ids/{ids}")
    @ApiOperation(value = "删除", notes = "根据id删除")
    @Transactional(rollbackFor = Exception.class)
    @ApiImplicitParam(name = "ids", value = "", required = true, dataType = "Long")
    public ServerResponseEntity<Boolean> removeByIds(@PathVariable Long[] ids) {
        for (Long id : ids) {
            TDictData dictData = tDictDataService.getById(id);
            if (dictData == null) {
                return ServerResponseEntity.showFailMsg("数据不存在");
            }
            tDictDataService.removeById(id);
        }
        return ServerResponseEntity.success(true);
    }
}
