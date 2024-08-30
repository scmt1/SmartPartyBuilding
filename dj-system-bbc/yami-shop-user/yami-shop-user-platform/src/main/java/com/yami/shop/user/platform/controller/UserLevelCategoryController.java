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


import com.yami.shop.user.common.service.UserLevelCategoryService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户等级关联分类
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Api(tags = "用户等级关联分类")
@RestController
@AllArgsConstructor
@RequestMapping("/user/userLevelCategory" )
public class UserLevelCategoryController {

    private final UserLevelCategoryService userLevelCategoryService;
//
//    /**
//     * 分页查询
//     * @param page 分页对象
//     * @param userLevelCategory
//     * @return 分页数据
//     */
//    @GetMapping("/page" )
//    public ServerResponseEntity<IPage<UserLevelCategory>> getUserLevelCategoryPage(PageParam<UserLevelCategory> page, UserLevelCategory userLevelCategory) {
//        return ServerResponseEntity.success(userLevelCategoryService.page(page, new LambdaQueryWrapper<UserLevelCategory>()));
//    }
//
//
//    /**
//     * 通过id查询
//     * @param userLevelCategoryId id
//     * @return 单个数据
//     */
//    @GetMapping("/info/{userLevelCategoryId}" )
//    public ServerResponseEntity<UserLevelCategory> getById(@PathVariable("userLevelCategoryId") Long userLevelCategoryId) {
//        return ServerResponseEntity.success(userLevelCategoryService.getById(userLevelCategoryId));
//    }
//
//    /**
//     * 新增
//     * @param userLevelCategory
//     * @return 是否新增成功
//     */
//    @SysLog("新增" )
//    @PostMapping
//    @PreAuthorize("@pms.hasPermission('user:userLevelCategory:save')" )
//    public ServerResponseEntity<Boolean> save(@RequestBody @Valid UserLevelCategory userLevelCategory) {
//        return ServerResponseEntity.success(userLevelCategoryService.save(userLevelCategory));
//    }
//
//    /**
//     * 修改
//     * @param userLevelCategory
//     * @return 是否修改成功
//     */
//    @SysLog("修改" )
//    @PutMapping
//    @PreAuthorize("@pms.hasPermission('user:userLevelCategory:update')" )
//    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid UserLevelCategory userLevelCategory) {
//        return ServerResponseEntity.success(userLevelCategoryService.updateById(userLevelCategory));
//    }
//
//    /**
//     * 通过id删除
//     * @param userLevelCategoryId id
//     * @return 是否删除成功
//     */
//    @SysLog("删除" )
//    @DeleteMapping("/{userLevelCategoryId}" )
//    @PreAuthorize("@pms.hasPermission('user:userLevelCategory:delete')" )
//    public ServerResponseEntity<Boolean> removeById(@PathVariable Long userLevelCategoryId) {
//        return ServerResponseEntity.success(userLevelCategoryService.removeById(userLevelCategoryId));
//    }

}
