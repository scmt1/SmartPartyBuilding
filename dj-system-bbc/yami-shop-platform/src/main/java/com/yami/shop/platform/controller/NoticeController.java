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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.img.gif.NeuQuant;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.app.dto.NoticeDto;
import com.yami.shop.bean.enums.NoticeType;
import com.yami.shop.bean.model.Notice;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.platform.util.SecurityUtils;
import com.yami.shop.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 公告管理
 *
 * @author hzm
 * @date
 */
@RestController
@AllArgsConstructor
@RequestMapping("/platform/notice")
@Api(tags = "公告")
public class NoticeController {

    @Autowired
    private  NoticeService noticeService;

    @Autowired
    private MapperFacade mapperFacade;


    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public ServerResponseEntity<IPage<Notice>> getNoticePage(PageParam<Notice> page, Notice notice) {
        IPage<Notice> noticePage = noticeService.page(page, new LambdaQueryWrapper<Notice>()
                .eq(Notice::getShopId, Constant.PLATFORM_SHOP_ID)
//                .eq(notice.getType() != null, Notice::getType, notice.getType())
                .eq(notice.getStatus() != null, Notice::getStatus, notice.getStatus())
                .eq(notice.getIsTop() != null, Notice::getIsTop, notice.getIsTop())
                .like(notice.getTitle() != null, Notice::getTitle, notice.getTitle()).orderByDesc(Notice::getUpdateTime));
        return ServerResponseEntity.success(noticePage);
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "公告信息", notes = "公告信息")
    @ApiImplicitParam(name = "id", value = "公告id")
    public ServerResponseEntity<Notice> getById(@PathVariable("id") Long id) {
        return ServerResponseEntity.success(noticeService.getInfoById(id));
    }

    @SysLog("新增公告管理")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('platform:notice:save')")
    @ApiOperation(value = "新增公告", notes = "新增公告")
    public ServerResponseEntity<Void> save(@RequestBody @Valid Notice notice) {
        notice.setShopId(Constant.PLATFORM_SHOP_ID);
        if (notice.getStatus() == 1) {
            notice.setPublishTime(new Date());
        }
        notice.setUpdateTime(new Date());
        noticeService.save(notice);
        noticeService.removeTopNoticeListCacheByShopId(Constant.PLATFORM_SHOP_ID);
        noticeService.removeNoticeCacheById(notice.getId());
        return ServerResponseEntity.success();
    }

    @SysLog("修改公告管理")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('platform:notice:update')")
    @ApiOperation(value = "修改公告", notes = "修改公告")
    public ServerResponseEntity<Void> updateById(@RequestBody @Valid NoticeDto noticeDto) {
        Notice oldNotice = noticeService.getById(noticeDto.getId());
        Notice notice = mapperFacade.map(noticeDto, Notice.class);
        noticeDto.setShopId(Constant.PLATFORM_SHOP_ID);
        if (oldNotice.getStatus() == 0 && notice.getStatus() == 1) {
            notice.setPublishTime(new Date());
        }
        notice.setUpdateTime(new Date());
        noticeService.updateById(notice);
        noticeService.removeTopNoticeListCacheByShopId(Constant.PLATFORM_SHOP_ID);
        noticeService.removeNoticeCacheById(notice.getId());
        return ServerResponseEntity.success();
    }

    @SysLog("删除公告管理")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('platform:notice:delete')")
    @ApiOperation(value = "删除公告", notes = "删除公告")
    @ApiImplicitParam(name = "id", value = "公告id")
    public ServerResponseEntity<Void> removeById(@PathVariable Long id) {
        noticeService.removeByIdAndShopId(id,Constant.PLATFORM_SHOP_ID);
        noticeService.removeTopNoticeListCacheByShopId(Constant.PLATFORM_SHOP_ID);
        noticeService.removeNoticeCacheById(id);
        return ServerResponseEntity.success();
    }

}
