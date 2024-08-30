/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.sys.multishop.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.bean.enums.PositionType;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.common.enums.SysTypeEnum;
import com.yami.shop.security.common.manager.PasswordManager;
import com.yami.shop.security.common.manager.TokenStore;
import com.yami.shop.security.common.model.UpdatePasswordDto;
import com.yami.shop.security.multishop.util.MD5Utils;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.sys.common.model.ShopEmployee;
import com.yami.shop.sys.common.service.ShopEmployeeService;
import com.yami.shop.sys.common.service.ShopRoleService;
import com.yami.shop.utils.HttpClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * 商家端用户
 * @author cl
 * @date 2021-03-01 17:42:09
 */
@RestController
@RequestMapping("/sys/shopEmployee" )
@Api(tags = "商家端用户接口")
public class ShopEmployeeController {

    @Value("${yami.expose.operation.auth:}")
    private Boolean permission;

    @Autowired
    private ShopEmployeeService shopEmployeeService;

    @Autowired
    private ShopRoleService shopRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private PasswordManager passwordManager;

    /**
     * 分页查询
     * @param page 分页对象
     * @param shopEmployee 系统用户
     * @return 分页数据
     */
    @GetMapping("/page" )
    @ApiOperation(value = "分页获取用户", notes = "分页获取用户")
    public ServerResponseEntity<IPage<ShopEmployee>> getShopEmployeePage(PageParam<ShopEmployee> page, ShopEmployee shopEmployee) {
        // 只显示店铺自己的管理员
        //ShopEmployee employee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
        PageParam<ShopEmployee> resPage = shopEmployeeService.page(page, new LambdaQueryWrapper<ShopEmployee>()
                .eq(ShopEmployee::getShopId,SecurityUtils.getShopUser().getShopId())
                .like(StrUtil.isNotBlank(shopEmployee.getUsername()),ShopEmployee::getUsername,shopEmployee.getUsername())
                .like(StrUtil.isNotBlank(shopEmployee.getNickname()),ShopEmployee::getNickname,shopEmployee.getNickname()));
        return ServerResponseEntity.success(resPage);
    }

    /**
     * 获取登录的用户信息
     */
    @GetMapping("/info")
    @ApiOperation(value = "获取登录的用户信息", notes = "获取登录的用户信息")
    public ServerResponseEntity<ShopEmployee> info(){
        ShopEmployee employee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
        if (Objects.isNull(employee)) {
            throw new YamiShopBindException("yami.shop.data.is.removed");
        }
        if (!Objects.equals(SecurityUtils.getShopUser().getShopId(), employee.getShopId())) {
            throw new YamiShopBindException("yami.no.auth");
        }
        return ServerResponseEntity.success(employee);
    }

    /**
     * 修改登录用户密码
     */
    @SysLog("商家端-修改密码")
    @PostMapping("/password")
    @ApiOperation(value="修改密码", notes="修改当前登陆用户的密码")
    public ServerResponseEntity<String> password(@RequestBody @Valid UpdatePasswordDto param){
        Long employeeId = SecurityUtils.getShopUser().getEmployeeId();
        if (Objects.equals(employeeId.intValue(), Constant.SUPER_ADMIN_ID) && BooleanUtil.isFalse(permission)) {
            throw new YamiShopBindException("yami.no.auth");
        }
        ShopEmployee dbShopEmployee = shopEmployeeService.getShopEmployeeById(employeeId);
        String password = passwordManager.decryptPassword(param.getPassword());
        if (!passwordEncoder.matches(password, dbShopEmployee.getPassword())) {
            // 原密码不正确
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.password.error"));
        }
        //新密码
        String decryptPassword = passwordManager.decryptPassword(param.getNewPassword());
        String newPassword = passwordEncoder.encode(decryptPassword);
//        更新密码
        shopEmployeeService.updatePasswordByEmployeeId(employeeId, newPassword);
        tokenStore.deleteAllToken(SysTypeEnum.MULTISHOP.value().toString(),SecurityUtils.getShopUser().getUserId());
        return ServerResponseEntity.success();
    }


    @GetMapping("/info/{employeeId}" )
    @PreAuthorize("@pms.hasPermission('sys:shopEmployee:info')")
    @ApiOperation(value = "通过id查询用户信息", notes = "通过id查询用户信息")
    @ApiImplicitParam(name = "employeeId", value = "系统用户id")
    public ServerResponseEntity<ShopEmployee> getById(@PathVariable("employeeId") Long employeeId) {
        ShopEmployee dbShopEmployee = shopEmployeeService.getShopEmployeeById(employeeId);
        dbShopEmployee.setUserId(null);
        //获取用户所属的角色列表
        List<Long> roleIdList = shopRoleService.listRoleIdByEmployeeId(employeeId);
        dbShopEmployee.setRoleIdList(roleIdList);
        return ServerResponseEntity.success(dbShopEmployee);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/shopEmployeeInfo")
    @PreAuthorize("@pms.hasPermission('sys:shopEmployee:info')")
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    public ServerResponseEntity<ShopEmployee> shopEmployeeInfo(){
        ShopEmployee shopEmployee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
        shopEmployee.setEmployeeId(null);
        shopEmployee.setPassword(null);
        return ServerResponseEntity.success(shopEmployee);
    }

    /**
     * 新增系统用户
     * @param shopEmployee 系统用户
     * @return 是否新增成功
     */
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys:shopEmployee:save')" )
    @ApiOperation(value = "新增用户信息", notes = "新增用户信息")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<String> save(@RequestBody @Valid ShopEmployee shopEmployee) {
        shopEmployee.setTenantId(SecurityUtils.getShopUser().getTenantId());
        shopEmployee.setOrgId(SecurityUtils.getShopUser().getOrgId());
        shopEmployee.setShopId(SecurityUtils.getShopUser().getShopId());
        shopEmployee.setCreateEmployeeId(SecurityUtils.getShopUser().getEmployeeId());
        shopEmployee.setType(PositionType.STAFF.value());
        String decryptPassword = passwordManager.decryptPassword(shopEmployee.getPassword());
        shopEmployee.setPassword(passwordEncoder.encode(decryptPassword));
        Boolean flag = shopEmployeeService.saveUserAndUserRole(shopEmployee);
        if(flag){
            HashMap<String, Object> requestMap = new HashMap<>();
            JSONObject obj = new JSONObject();
            obj.put("username",shopEmployee.getUsername());
            obj.put("password", MD5Utils.encrypByMd5(decryptPassword));
            obj.put("orgId",SecurityUtils.getShopUser().getOrgId());
            obj.put("tenantId",SecurityUtils.getShopUser().getTenantId());
            obj.put("status",0);
            obj.put("type",0);
            obj.put("nickname",shopEmployee.getNickname());
            obj.put("phone",shopEmployee.getMobile());
            obj.put("email",shopEmployee.getEmail());
            obj.put("deptId",shopEmployee.getDeptId());
            obj.put("tenantId",shopEmployee.getTenantId());
            requestMap.put("serviceName","UserService");
            requestMap.put("methodName","ywAdd");
            requestMap.put("bizParams",obj);
            org.springframework.http.HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Type", "application/json");
            httpHeaders.add("Accept", "application/json;charset=utf-8");
            String postResult = HttpClient.sendPostRequest("http://127.0.0.1:8097/gateway", requestMap, httpHeaders);
            JSONObject jsonObject = JSONObject.parseObject(postResult);
            if(!"000000".equals(jsonObject.getString("code"))){
                throw new YamiShopBindException("用户新增失败");
            }
        }
        return ServerResponseEntity.success();
    }

    /**
     * 修改系统用户
     * @param shopEmployee 系统用户
     * @return 是否修改成功
     */
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys:shopEmployee:update')" )
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    public ServerResponseEntity<String> updateById(@RequestBody @Valid ShopEmployee shopEmployee) {
        if (Objects.isNull(shopEmployee.getEmployeeId())) {
            throw new YamiShopBindException("员工id不能为空");
        }
        Long employeeId = SecurityUtils.getShopUser().getEmployeeId();
        if(Objects.equals(shopEmployee.getEmployeeId().intValue(), Constant.SUPER_ADMIN_ID) && BooleanUtil.isFalse(permission)) {
            throw new YamiShopBindException("yami.no.auth");
        }
        String password = passwordManager.decryptPassword(shopEmployee.getPassword());
        ShopEmployee dbEmployee = shopEmployeeService.getShopEmployeeById(shopEmployee.getEmployeeId());
        //修改管理员账号但修改人不是管理员，则抛出异常（只有管理员可以改管理员信息）
        if (Objects.equals( PositionType.ADMIN.value(),dbEmployee.getType()) &&
            !Objects.equals(shopEmployee.getEmployeeId(), employeeId)) {
            // 您没有权限修改管理员信息
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.not.permission.modify.administrator.info"));
        }
        if (StrUtil.isBlank(password)) {
            shopEmployee.setPassword(null);
        } else {
            shopEmployee.setPassword(passwordEncoder.encode(password));
        }
        shopEmployeeService.updateUserAndUserRole(shopEmployee);
        // 禁用用户之后将该用户登录信息清除
        if (Objects.equals(shopEmployee.getStatus(), 0)) {
            tokenStore.deleteAllToken(SysTypeEnum.MULTISHOP.value().toString(), shopEmployee.getEmployeeId().toString());
        }
        return ServerResponseEntity.success();
    }

    /**
     * 通过id删除系统用户
     * @return 是否删除成功
     */
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('sys:shopEmployee:delete')" )
    @ApiOperation(value = "通过id删除用户信息", notes = "通过id删除用户信息")
    @ApiImplicitParam(name = "employeeIds", value = "系统用户id列表")
    public ServerResponseEntity<String> removeById(@RequestBody List<Long> employeeIds) {
        if (CollUtil.isEmpty(employeeIds)) {
            // 请选择需要删除的用户
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.sys.select.user"));
        }
        // 获取该店铺的商家信息
        ShopEmployee admin = shopEmployeeService.getOne(Wrappers.lambdaQuery(ShopEmployee.class)
                .eq(ShopEmployee::getShopId, SecurityUtils.getShopUser().getShopId())
                .eq(ShopEmployee::getType, PositionType.ADMIN.value()));
        // 商家id
        Long adminId = admin.getEmployeeId();
        // 当前员工id
        Long currentEmployeeId = SecurityUtils.getShopUser().getEmployeeId();
        // 检查删除的员工中是否存在商家与当前员工
        for (Long id : employeeIds) {
            if (Objects.equals(id, adminId)) {
                throw new YamiShopBindException("yami.sys.admin.error");
            }
            if (Objects.equals(id, currentEmployeeId)) {
                throw new YamiShopBindException("yami.sys.delete.error");
            }
        }
        List<ShopEmployee> shopEmployees = shopEmployeeService.list(new LambdaQueryWrapper<ShopEmployee>().in(ShopEmployee::getEmployeeId, employeeIds));
        shopEmployeeService.removeEmployeesByIds(employeeIds);
        for (ShopEmployee shopEmployee : shopEmployees) {
            tokenStore.deleteAllToken(SysTypeEnum.MULTISHOP.value().toString(), shopEmployee.getEmployeeId().toString());
        }
        return ServerResponseEntity.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取店铺下的所有员工列表", notes = "获取店铺下的所有员工列表")
    public ServerResponseEntity<List<ShopEmployee>> list(ShopEmployee shopEmployee) {
        shopEmployee.setShopId(SecurityUtils.getShopUser().getShopId());
        List<ShopEmployee> shopEmployeeList = shopEmployeeService.listByParam(shopEmployee);
        return ServerResponseEntity.success(shopEmployeeList);
    }
}
