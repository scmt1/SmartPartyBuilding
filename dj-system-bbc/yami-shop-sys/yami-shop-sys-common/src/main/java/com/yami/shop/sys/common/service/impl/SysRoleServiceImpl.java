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
import com.yami.shop.sys.common.dao.SysRoleMapper;
import com.yami.shop.sys.common.dao.SysRoleMenuMapper;
import com.yami.shop.sys.common.dao.SysUserRoleMapper;
import com.yami.shop.sys.common.model.SysRole;
import com.yami.shop.sys.common.service.SysRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 角色
 * @author lgh
 */
@Service("sysRoleService")
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMapper sysRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleAndRoleMenu(SysRole role) {
        role.setCreateTime(new Date());
        this.save(role);
        if (CollectionUtil.isEmpty(role.getMenuIdList())) {
            return;
        }


        //保存角色与菜单关系
        sysRoleMenuMapper.insertRoleAndRoleMenu(role.getRoleId(), role.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleAndRoleMenu(SysRole role) {
        // 更新角色
        sysRoleMapper.updateById(role);
        //先删除角色与菜单关系
        sysRoleMenuMapper.deleteBatch(Collections.singletonList(role.getRoleId()));
        if (CollectionUtil.isEmpty(role.getMenuIdList())) {
            return;
        }
        //保存角色与菜单关系
        sysRoleMenuMapper.insertRoleAndRoleMenu(role.getRoleId(), role.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> roleIds) {
        // 获取正在使用中的角色
        List<Long> userRoleIds = sysUserRoleMapper.selectUseRoleIds(roleIds);

        if (CollUtil.isNotEmpty(userRoleIds)) {
            List<SysRole> sysRoles = list(new LambdaQueryWrapper<SysRole>().select(SysRole::getRoleName).in(SysRole::getRoleId, userRoleIds));
            List<String> names = sysRoles.stream().map(SysRole::getRoleName).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(names)) {
                String message = I18nMessage.getMessage("yami.role.use.cannot.deleted");
                throw new YamiShopBindException(message + names);
            }
        }

        //删除角色
        sysRoleMapper.deleteBatch(roleIds);

        //删除角色与菜单关联
        sysRoleMenuMapper.deleteBatch(roleIds);

        //删除角色与用户关联
        sysUserRoleMapper.deleteBatch(roleIds);
    }
    @Override
    public List<Long> listRoleIdByUserId(Long userId) {
        return sysRoleMapper.listRoleIdByUserId(userId);
    }

}
