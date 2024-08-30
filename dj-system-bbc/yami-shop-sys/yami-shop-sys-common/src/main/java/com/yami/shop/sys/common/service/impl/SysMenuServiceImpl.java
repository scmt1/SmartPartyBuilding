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


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.sys.common.dao.SysMenuMapper;
import com.yami.shop.sys.common.dao.SysRoleMenuMapper;
import com.yami.shop.sys.common.model.SysMenu;
import com.yami.shop.sys.common.service.SysMenuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lgh
 */
@Service("sysMenuService")
@AllArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final SysRoleMenuMapper sysRoleMenuMapper;

    private final SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> listMenuByUserId(Long userId) {
        // 用户的所有菜单信息
        List<SysMenu> sysMenus ;
        //系统管理员，拥有最高权限
        if(userId == Constant.SUPER_ADMIN_ID){
            sysMenus = sysMenuMapper.listMenu(I18nMessage.getLang());
        }else {
            sysMenus = sysMenuMapper.listMenuByUserId(userId,I18nMessage.getLang());
        }

        Map<Long, List<SysMenu>> sysMenuLevelMap = sysMenus.stream()
                .sorted(Comparator.comparing(SysMenu::getOrderNum))
                .collect(Collectors.groupingBy(SysMenu::getParentId));

        // 一级菜单
        List<SysMenu> rootMenu = sysMenuLevelMap.get(0L);
        if (CollectionUtil.isEmpty(rootMenu)) {
            return Collections.emptyList();
        }
        // 二三级菜单
        for (SysMenu sysMenu : rootMenu) {
            List<SysMenu> secondList = sysMenuLevelMap.get(sysMenu.getMenuId());
            if (CollectionUtil.isEmpty(secondList)){
                sysMenu.setList(secondList);
                continue;
            }
            for (SysMenu secondMenu : secondList) {
                secondMenu.setList(sysMenuLevelMap.get(secondMenu.getMenuId()));
            }
            sysMenu.setList(secondList);
        }
        return rootMenu;
    }

    @Override
    public void deleteMenuAndRoleMenu(Long menuId){
        //删除菜单
        this.removeById(menuId);
        //删除菜单与角色关联
        sysRoleMenuMapper.deleteByMenuId(menuId);
    }


    @Override
    public List<Long> listMenuIdByRoleId(Long roleId) {
        return sysMenuMapper.listMenuIdByRoleId(roleId);
    }


    @Override
    public List<SysMenu> listSimpleMenuNoButton() {
        return sysMenuMapper.listSimpleMenuNoButton(I18nMessage.getLang());
    }

    @Override
    public List<SysMenu> listRootMenu() {
        return sysMenuMapper.listRootMenu(I18nMessage.getLang());
    }

    @Override
    public List<SysMenu> listChildrenMenuByParentId(Long parentId) {
        return sysMenuMapper.listChildrenMenuByParentId(parentId,I18nMessage.getLang());
    }

    @Override
    public List<SysMenu> listMenuAndBtn(Long userId) {
        //系统管理员，拥有最高权限
        if (userId == Constant.SUPER_ADMIN_ID){
            return sysMenuMapper.listMenuAndBtn(I18nMessage.getLang());
        }else {
            return sysMenuMapper.listRoleMenuAndBtn(I18nMessage.getLang(),userId);
        }

    }

    @Override
    public Set<String> updateSonMenuState(Long menuId, Integer state) {
//        List<SysMenu> sysMenus = sysMenuMapper.selectList(new QueryWrapper<SysMenu>().eq("menu_id", menuId));
//        sysMenuMapper.update(new SysMenu(),new QueryWrapper<SysMenu>().eq("parent_id",menuId));
        sysMenuMapper.updateSonMenuState(menuId,state);
        Set<String> perms = sysMenuMapper.listMenuPerms(menuId);
        return perms;

    }

    @Override
    public Set<String> listConcealButtonPerms() {
        return sysMenuMapper.listConcealButtonPerms();
    }

}
