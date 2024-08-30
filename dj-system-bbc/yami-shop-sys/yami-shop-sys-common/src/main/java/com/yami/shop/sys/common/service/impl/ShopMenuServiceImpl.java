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
import com.yami.shop.bean.enums.PositionType;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.sys.common.dao.ShopEmployeeMapper;
import com.yami.shop.sys.common.dao.ShopMenuMapper;
import com.yami.shop.sys.common.dao.ShopRoleMenuMapper;
import com.yami.shop.sys.common.model.ShopEmployee;
import com.yami.shop.sys.common.model.ShopMenu;
import com.yami.shop.sys.common.service.ShopMenuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单管理
 * @author yami
 * @date 2021-03-01 17:42:09
 */
@Service
@AllArgsConstructor
public class ShopMenuServiceImpl extends ServiceImpl<ShopMenuMapper, ShopMenu> implements ShopMenuService {

    private final ShopMenuMapper shopMenuMapper;
    private final ShopRoleMenuMapper shopRoleMenuMapper;
    private final ShopEmployeeMapper shopEmployeeMapper;

    @Override
    public List<ShopMenu> listMenuAndBtn(Long employeeId) {
        ShopEmployee shopEmployee = shopEmployeeMapper.selectById(employeeId);
        if (Objects.equals(PositionType.ADMIN.value(),shopEmployee.getType())){
            return shopMenuMapper.listMenuAndBtn(I18nMessage.getLang());
        }else {
            return shopMenuMapper.listRoleMenuAndBtn(I18nMessage.getLang(),employeeId);
        }

    }

    @Override
    public List<ShopMenu> listSimpleMenuNoButton() {
        return shopMenuMapper.listSimpleMenuNoButton(I18nMessage.getLang());
    }

    @Override
    public List<ShopMenu> listRootMenu() {
       return shopMenuMapper.listRootMenu(I18nMessage.getLang());
    }

    @Override
    public List<ShopMenu> listChildrenMenuByParentId(Long parentId) {
        return shopMenuMapper.listChildrenMenuByParentId(parentId,I18nMessage.getLang());
    }

    @Override
    public void deleteMenuAndRoleMenu(Long menuId) {
        //删除菜单
        this.removeById(menuId);
        //删除菜单与角色关联
        shopRoleMenuMapper.deleteByMenuId(menuId);
    }

    @Override
    public List<ShopMenu> listMenuByEmployeeId(Long employeeId) {
        // 用户的所有菜单信息
        List<ShopMenu> shopMenus;
        //商铺超级管理员，拥有最高权限
        ShopEmployee shopEmployee = shopEmployeeMapper.selectById(employeeId);
        if(Objects.equals(PositionType.ADMIN.value(),shopEmployee.getType())){
            shopMenus = shopMenuMapper.listMenu(I18nMessage.getLang());
        }else {
            shopMenus = shopMenuMapper.listMenuByEmployeeId(employeeId,I18nMessage.getLang());
        }
        //List<ShopMenu> rootMenu = getTree(shopMenus, 0l);
        Map<Long, List<ShopMenu>> sysMenuLevelMap = shopMenus.stream().sorted(Comparator.comparing(ShopMenu::getOrderNum))
                .collect(Collectors.groupingBy(ShopMenu::getParentId));

        // 一级菜单
        List<ShopMenu> rootMenu = sysMenuLevelMap.get(0L);
        if (CollectionUtil.isEmpty(rootMenu)) {
            return Collections.emptyList();
        }
        // 二三级菜单
        for (ShopMenu sysMenu : rootMenu) {
            List<ShopMenu> secondList = sysMenuLevelMap.get(sysMenu.getMenuId());
            if (CollectionUtil.isEmpty(secondList)){
                sysMenu.setList(secondList);
                continue;
            }
            for (ShopMenu secondMenu : secondList) {
                secondMenu.setList(sysMenuLevelMap.get(secondMenu.getMenuId()));
            }
            sysMenu.setList(secondList);
        }
        return rootMenu;
    }

    public List<ShopMenu> getTree(List<ShopMenu> sysMenus, Long id){
        List<ShopMenu> list = new ArrayList<>();
        for (ShopMenu shopMenu : sysMenus) {
            if (shopMenu.getParentId() == id) {
                list.add(shopMenu);
            }
        }
        //递归
        for (ShopMenu shopMenu  : list) {
            shopMenu.setList(getTree(sysMenus, shopMenu.getMenuId()));// .setChildrenMenuList();
        }
        if (list.size() == 0) {
            return null;//new ArrayList<SysMenu>();
        }
        return list;
    }

    @Override
    public List<Long> listMenuIdByRoleId(Long roleId) {
        return shopMenuMapper.listMenuIdByRoleId(roleId);
    }

    @Override
    public Set<String> updateSonMenuState(Long parentId, Integer state) {
        shopMenuMapper.updateSonMenuState(parentId,state);
        return shopMenuMapper.listMenuPerms(parentId);
    }

    @Override
    public Set<String> listConcealButtonPerms() {
        return shopMenuMapper.listConcealButtonPerms();
    }
}
