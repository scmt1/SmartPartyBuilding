package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface JcxfSysDeptButtonRoleService {

    @ApiOperation("获取部门权限列表")
    @POST
    @Path("getDeptButtonRoleByPage")
    @Produces("application/json")
    Map<String, Object> getDeptButtonRoleByPage(Map<String, Object> requestMap);

    @ApiOperation("修改部门权限")
    @POST
    @Path("updateDeptButtonRole")
    @Produces("application/json")
    Map<String, Object> updateDeptButtonRole(Map<String, Object> requestMap);

    @ApiOperation("新增部门权限")
    @POST
    @Path("addDeptButtonRole")
    @Produces("application/json")
    Map<String, Object> addDeptButtonRole(Map<String, Object> requestMap);

}
