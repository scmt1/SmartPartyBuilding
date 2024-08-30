/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.sys.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.sys.common.model.ShopRole;

import java.util.List;

/**
 * 角色
 * @author yami
 * @date 2021-03-01 17:42:09
 */
public interface ShopRoleService extends IService<ShopRole> {

    /**
     * 保存角色以及保存角色与菜单关系
     * @param shopRole 店铺角色
     * @return 是否成功
     */
    Boolean saveRoleAndRoleMenu(ShopRole shopRole);

    /**
     * 更新角色以及更新角色与菜单关系
     * @param shopRole 店铺角色
     * @return 是否成功
     */
    Boolean updateRoleAndRoleMenu(ShopRole shopRole);

    /**
     * 根据角色ID列表批量删除角色以及角色与菜单关系
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    Boolean deleteBatch(Long[] roleIds);

    /**
     * 根据用户ID获取用户对应的角色列表
     * @param employeeId 用户ID
     * @return 角色ID列表
     */
    List<Long> listRoleIdByEmployeeId(Long employeeId);
}
