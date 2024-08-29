package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface TzMessageAutoRoleService {

    @ApiOperation("获取权限列表")
    @POST
    @Path("getDeptAutoMessageRoleByPage")
    @Produces("application/json")
    Map<String, Object> getDeptAutoMessageRoleByPage(Map<String, Object> requestMap);

    @ApiOperation("修改权限")
    @POST
    @Path("updateDeptAutoMessageRole")
    @Produces("application/json")
    Map<String, Object> updateDeptAutoMessageRole(Map<String, Object> requestMap);


    @ApiOperation("获取部门权限")
    @POST
    @Path("getAutoMessageRoleByDeptId")
    @Produces("application/json")
    Map<String, Object> getAutoMessageRoleByDeptId(Map<String, Object> requestMap);

    @ApiOperation("获取部门权限")
    @POST
    @Path("updateAutoMessageRoleStatus")
    @Produces("application/json")
    Map<String, Object> updateAutoMessageRoleStatus(Map<String, Object> requestMap);
}
