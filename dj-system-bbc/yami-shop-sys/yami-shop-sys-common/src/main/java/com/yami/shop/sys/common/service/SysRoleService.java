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
import com.yami.shop.sys.common.model.SysRole;

import java.util.List;

/**
 * 角色
 * @author lgh
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 根据id批量删除
     * @param roleIds 角色ID列表
     */
    void deleteBatch(List<Long> roleIds);

    /**
     * 保存角色 与 角色菜单关系
     * @param role 角色信息
     */
    void saveRoleAndRoleMenu(SysRole role);

    /**
     * 更新角色 与 角色菜单关系
     * @param role 角色信息
     */
    void updateRoleAndRoleMenu(SysRole role);

    /**
     * 根据用户ID，获取角色ID列表
     * @param userId 用户id
     * @return 角色id列表
     */
    List<Long> listRoleIdByUserId(Long userId);

}
