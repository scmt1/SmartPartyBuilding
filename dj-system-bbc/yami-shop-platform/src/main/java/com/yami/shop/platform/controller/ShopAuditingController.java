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
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.bean.app.param.ShopAuditingParam;
import com.yami.shop.bean.dto.ShopAuditingInfoDto;
import com.yami.shop.bean.enums.ShopStatus;
import com.yami.shop.bean.model.ShopAuditing;
import com.yami.shop.bean.model.ShopDetail;
import com.yami.shop.bean.param.AuditingInfoParam;
import com.yami.shop.bean.param.ShopTypeParam;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.common.manager.PasswordManager;
import com.yami.shop.security.platform.util.SecurityUtils;
import com.yami.shop.service.ShopAuditingService;
import com.yami.shop.service.ShopDetailService;
import com.yami.shop.sys.common.model.ShopEmployee;
import com.yami.shop.sys.common.service.ShopEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;


/**
 * 商家审核信息
 *
 * @author Dwl
 * @date 2019-09-19 14:02:57
 */
@RestController
@AllArgsConstructor
@RequestMapping("/shop/shopAuditing")
@Api(tags = "店铺审核信息")
public class ShopAuditingController {

    private final ShopAuditingService shopAuditingService;
    private final ShopDetailService shopDetailService;
    private final ShopEmployeeService shopEmployeeService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordManager passwordManager;

    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('shop:shopAuditing:page')")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public ServerResponseEntity<IPage<ShopAuditingInfoDto>> getShopAuditingPage(PageParam<ShopAuditingInfoDto> page, AuditingInfoParam auditingInfoParam) {
        return ServerResponseEntity.success(shopAuditingService.auditingInfoList(page, auditingInfoParam));
    }

    @GetMapping("/shopDetail/{shopId}")
    @PreAuthorize("@pms.hasPermission('shop:shopAuditing:info')")
    @ApiOperation(value = "查询详情信息", notes = "查询详情信息")
    @ApiImplicitParam(name = "shopId", value = "店铺id")
    public ServerResponseEntity<ShopDetail> auditingDetail(@PathVariable Long shopId) {
        ShopDetail shopDetail = shopDetailService.getShopDetailByShopId(shopId);
        return ServerResponseEntity.success(shopDetail);
    }

    @GetMapping("/{shopId}")
    @ApiOperation(value = "根据店铺id查询审核信息", notes = "根据店铺id查询审核信息")
    @ApiImplicitParam(name = "shopId", value = "店铺id")
    public ServerResponseEntity<ShopAuditing> getShopAuditing(@PathVariable Long shopId) {
        ShopAuditing shopAuditing = shopAuditingService.getOne(new LambdaQueryWrapper<ShopAuditing>().eq(ShopAuditing::getShopId, shopId));
        ShopDetail shopDetail = shopDetailService.getShopDetailByShopId(shopId);
        if (Objects.isNull(shopAuditing)){
            shopAuditing = new ShopAuditing();
        }
        if (Objects.equals(shopDetail.getShopStatus(), ShopStatus.OFFLINE.value())) {
            shopAuditing.setStatus(2);
        } else if (Objects.equals(shopDetail.getShopStatus(), ShopStatus.OFFLINE_AUDIT.value())) {
            shopAuditing.setStatus(0);
        }
        return ServerResponseEntity.success(shopAuditing);
    }

    @SysLog("审核商家信息")
    @PutMapping("/audit")
    @PreAuthorize("@pms.hasPermission('shop:shopAuditing:audit')")
    @ApiOperation(value = "审核信息(审核商家)", notes = "审核信息(审核商家)")
    public ServerResponseEntity<Void> audit(@Valid @RequestBody ShopAuditingParam shopAuditingParam) {
        shopAuditingParam.setAuditorId(SecurityUtils.getSysUser().getUserId());
        shopDetailService.audit(shopAuditingParam);
        shopDetailService.removeShopDetailCacheByShopId(shopAuditingParam.getShopId());

        return ServerResponseEntity.success();
    }

    @PostMapping
    @ApiOperation(value = "新建店铺", notes = "新建店铺")
    public ServerResponseEntity<ShopDetail> insertDetail(@RequestBody ShopDetail shopDetail) {
        // 判断是账号是否已存在
        int count = shopEmployeeService.checkUserName(shopDetail.getShopId(), shopDetail.getMobile());

        shopDetail.setPassword(passwordEncoder.encode(passwordManager.decryptPassword(shopDetail.getPassword())));
        shopDetailService.insertDetail(shopDetail,count);
        return ServerResponseEntity.success(shopDetail);
    }

    @GetMapping("/checkMobile")
    @ApiOperation(value = "校验账号", notes = "校验账号")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "mobile", value = "账号"),
            @ApiImplicitParam(name = "shopId", value = "店铺id"),
    })
    public ServerResponseEntity<Boolean> checkMobile(@RequestParam("mobile") String mobile,@RequestParam("shopId")Long shopId) {
        boolean isTrue = true;
        if (Objects.isNull(shopId)){
            shopId = 0L;
        }
        int count = shopDetailService.checkMobile(mobile,Long.valueOf(shopId));
        // 加多了一个表，并且为了适配，保证数据的唯一性，需要同时验证两个表的数据
        count = count + shopEmployeeService.checkUserName(shopId, mobile);
        if (count > 0){
            isTrue = false;
        }
        return ServerResponseEntity.success(isTrue);
    }

    @GetMapping("/checkUsername")
    @ApiOperation(value = "检查用户名是否可用", notes = "检查用户名是否可用")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username", value = "用户名"),
            @ApiImplicitParam(name = "shopId", value = "店铺id"),
    })
    public ServerResponseEntity<Boolean> checkUsername(@RequestParam("username") String username, @RequestParam("shopId") Long shopId) {
        if (StringUtils.isBlank(username)) {
            return ServerResponseEntity.success(Boolean.FALSE);
        }
        int count = 0;
        if (Objects.isNull(shopId)) {
            // shopId为空，判断用户名是否重复
            count = shopEmployeeService.count(Wrappers.lambdaQuery(ShopEmployee.class)
                    .eq(ShopEmployee::getUsername, username)
            );
        } else {
            // 更改店铺商家用户名时，判断username是否重复
            count = shopEmployeeService.count(Wrappers.lambdaQuery(ShopEmployee.class)
                    .eq(ShopEmployee::getUsername, username)
                    .ne(ShopEmployee::getShopId, shopId)
            );
        }
        return ServerResponseEntity.success(count == 0);
    }

    @PutMapping("/updatePasswordOrMobile")
    @ApiOperation(value = "重置密码或者修改账号", notes = "重置密码或者修改账号")
    public ServerResponseEntity<Boolean> updatePasswordOrMobile(@RequestBody ShopDetail shopDetail) {
        // 判断是账号是否已存在
        String decryptPassword = passwordManager.decryptPassword(shopDetail.getPassword());
        int count = shopEmployeeService.checkUserName(shopDetail.getShopId(), shopDetail.getMobile());
        if (Objects.nonNull(decryptPassword)) {
            //前端传过来密码如果为空，就不set
            shopDetail.setPassword(passwordEncoder.encode(decryptPassword));
        }
        shopDetailService.updatePasswordOrMobile(shopDetail.getShopId(),decryptPassword,shopDetail.getMobile(),count);
        return ServerResponseEntity.success();
    }

    @PutMapping("/updateShopType")
    @ApiOperation(value = "修改店铺类型", notes = "修改店铺类型")
    public ServerResponseEntity<Boolean> updateShopType(@RequestBody ShopTypeParam shopTypeParam) {
        shopDetailService.batchUpdateShopType(shopTypeParam);
        return ServerResponseEntity.success();
    }

}
