/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.platform.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.param.UserUpdateParam;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.user.common.dto.UserTagDto;
import com.yami.shop.user.common.model.UserTag;
import com.yami.shop.user.common.model.UserTagUser;
import com.yami.shop.user.common.service.UserTagService;
import com.yami.shop.user.common.service.UserTagUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


/**
 * 客户标签
 *
 * @author LGH
 * @date 2020-09-09 11:31:16
 */
@Api(tags = "客户标签")
@RestController
@AllArgsConstructor
@RequestMapping("/user/userTag")
public class UserTagController {

    private final UserTagService userTagService;
    private final UserTagUserService userTagUserService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public ServerResponseEntity<IPage<UserTag>> getUserTagPage(PageParam<UserTag> page) {
        return ServerResponseEntity.success(userTagService.page(page, new LambdaQueryWrapper<>()));
    }

    @GetMapping("/info/{userTagId}")
    @ApiOperation(value = "查询客户标签信息", notes = "通过id查询客户标签")
    public ServerResponseEntity<UserTag> getById(@PathVariable("userTagId") Long userTagId) {
        return ServerResponseEntity.success(userTagService.getById(userTagId));
    }

    @PostMapping
    @ApiOperation(value = "新增客户标签", notes = "新增客户标签")
    @PreAuthorize("@pms.hasPermission('user:userTag:save')")
    public ServerResponseEntity<Boolean> save(@RequestBody @Validated(UserTagDto.AddUserTag.class) UserTagDto userTagDto) {
        int count = userTagService.count(new LambdaQueryWrapper<UserTag>().eq(UserTag::getTagType, 1));
        if(Objects.equals(userTagDto.getTagType(),1) && count + 1 > Constant.TAG_LIMIT_NUM){
            // 数量超过上限
            throw new YamiShopBindException("yami.tag.num.check");
        }
        int nameCount = userTagService.count(new LambdaQueryWrapper<UserTag>().eq(UserTag::getTagName,userTagDto.getTagName()));
        if(nameCount > 0){
            // 标签名称已存在！
            throw new YamiShopBindException("yami.tag.name.exist");
        }
        return ServerResponseEntity.success(userTagService.addUserTag(userTagDto));
    }

    @PutMapping
    @ApiOperation(value = "修改客户标签", notes = "修改客户标签")
    @PreAuthorize("@pms.hasPermission('user:userTag:update')")
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Validated(UserTagDto.UpdateUserTag.class) UserTagDto userTagDto) {
        int nameCount = userTagService.count(new LambdaQueryWrapper<UserTag>().eq(UserTag::getTagName,userTagDto.getTagName())
                            .ne(UserTag::getUserTagId,userTagDto.getUserTagId()));
        if(nameCount > 0){
            // 标签名称已存在！
            throw new YamiShopBindException("yami.tag.name.exist");
        }
        return ServerResponseEntity.success(userTagService.updateUserTag(userTagDto));
    }

    @SysLog("删除客户标签")
    @DeleteMapping("/{userTagId}")
    @ApiOperation(value = "删除客户标签", notes = "通过id删除客户标签")
    @PreAuthorize("@pms.hasPermission('user:userTag:delete')")
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long userTagId) {
        return ServerResponseEntity.success(userTagService.removeUserTag(userTagId));
    }

    @SysLog("刷新客户标签统计数据")
    @GetMapping("/refresh/{userTagId}")
    @ApiOperation(value = "刷新指定客户标签统计数据", notes = "通过id刷新客户标签统计数据")
    public ServerResponseEntity<UserTag> refreshUserTag(@PathVariable("userTagId") Long userTagId) {
        return ServerResponseEntity.success(userTagService.refreshConditionTag(userTagId));
    }

    @SysLog("查看客户标签")
    @GetMapping("/getTagList" )
    @ApiOperation(value = "可以添加的标签列表", notes = "可以添加的标签列表")
    @PreAuthorize("@pms.hasPermission('user:userTag:getTagList')")
    public ServerResponseEntity<List<UserTag>> getTagList() {
        return ServerResponseEntity.success(userTagService.list(null));
    }

    @SysLog("给对应标签的用户推送消息" )
    @DeleteMapping("/sendMsg/{templateId}" )
    @ApiOperation(value = "给对应标签的用户推送消息", notes = "通过消息id推送给对应标签的用户")
    @PreAuthorize("@pms.hasPermission('platform:notifyTemplate:update')" )
    public ServerResponseEntity<Void> sendMsg(@PathVariable Long templateId) {
        userTagService.sendTagMsg(templateId);
        return ServerResponseEntity.success();
    }

    @GetMapping("/byTagType")
    @ApiOperation(value = "获取手动标签", notes = "获取手动标签")
    public ServerResponseEntity<IPage<UserTag>> getTagPage(PageParam<UserTag> page, UserTag userTag) {
        PageParam<UserTag> resPage = userTagService.page(page, new LambdaQueryWrapper<UserTag>()
                .eq(userTag.getTagType() != null,UserTag::getTagType, userTag.getTagType())
                .like(StrUtil.isNotBlank(userTag.getTagName()), UserTag::getTagName, userTag.getTagName())
                .orderByDesc(UserTag::getCreateTime)
        );
        return ServerResponseEntity.success(resPage);
    }

    @PutMapping("/updateUserTag")
    @ApiOperation(value = "批量修改会员标签", notes = "平台批量修改会员标签")
    public ServerResponseEntity<Boolean> batchUpdateUserTag(@RequestBody UserUpdateParam param) {
        return ServerResponseEntity.success(userTagService.batchUpdateUserTag(param));
    }

    @DeleteMapping("/deleteUserTag")
    @ApiOperation(value = "删除会员的指定标签", notes = "删除会员的指定标签")
    public ServerResponseEntity<Boolean> deleteUserTag(@RequestBody UserTagUser tagUser) {
        return ServerResponseEntity.success(userTagUserService.removeByUserIdAndTagId(tagUser.getUserId(),tagUser.getUserTagId()));
    }
}
