/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.sys.common.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.sys.common.dao.ShopEmployeeRoleMapper;
import com.yami.shop.sys.common.dao.ShopRoleMapper;
import com.yami.shop.sys.common.dao.ShopRoleMenuMapper;
import com.yami.shop.sys.common.model.ShopRole;
import com.yami.shop.sys.common.service.ShopRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色
 * @author yami
 * @date 2021-03-01 17:42:09
 */
@Service
@AllArgsConstructor
public class ShopRoleServiceImpl extends ServiceImpl<ShopRoleMapper, ShopRole> implements ShopRoleService {

    private final ShopRoleMapper shopRoleMapper;
    private final ShopRoleMenuMapper shopRoleMenuMapper;
    private final ShopEmployeeRoleMapper shopEmployeeRoleMapper;


    @Override
    public Boolean saveRoleAndRoleMenu(ShopRole shopRole) {
        shopRole.setCreateTime(new Date());
        this.save(shopRole);
        if (CollectionUtil.isEmpty(shopRole.getMenuIdList())) {
            return true;
        }
        //保存角色与菜单关系
        shopRoleMenuMapper.insertRoleAndRoleMenu(shopRole.getRoleId(), shopRole.getMenuIdList());
        return true;
    }

    @Override
    public Boolean updateRoleAndRoleMenu(ShopRole shopRole) {
        // 更新角色
        shopRoleMapper.updateById(shopRole);
        //先删除角色与菜单关系
        shopRoleMenuMapper.deleteBatch(Collections.singletonList(shopRole.getRoleId()));
        if (CollectionUtil.isEmpty(shopRole.getMenuIdList())) {
            return true;
        }
        //保存角色与菜单关系
        shopRoleMenuMapper.insertRoleAndRoleMenu(shopRole.getRoleId(), shopRole.getMenuIdList());
        return true;
    }

    @Override
    public Boolean deleteBatch(Long[] ids) {
        List<Long> roleIds = CollUtil.newArrayList(ids);

        // 获取正在使用中的角色
        List<Long> userRoleIds = shopEmployeeRoleMapper.selectUseRoleIds(roleIds);

        if (CollUtil.isNotEmpty(userRoleIds)) {
            List<ShopRole> sysRoles = list(new LambdaQueryWrapper<ShopRole>().select(ShopRole::getRoleName).in(ShopRole::getRoleId, userRoleIds));
            List<String> names = sysRoles.stream().map(ShopRole::getRoleName).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(names)) {
                String message = I18nMessage.getMessage("yami.role.use.cannot.deleted");
                throw new YamiShopBindException(message + names);
            }
        }

        //删除角色
        shopRoleMapper.deleteBatch(roleIds);

        //删除角色与菜单关联
        shopRoleMenuMapper.deleteBatch(roleIds);

        //删除角色与用户关联
        shopEmployeeRoleMapper.deleteBatch(roleIds);
        return true;
    }

    @Override
    public List<Long> listRoleIdByEmployeeId(Long employeeId) {

        return shopRoleMapper.listRoleIdByEmployeeId(employeeId);
    }
}
