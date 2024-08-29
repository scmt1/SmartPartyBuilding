package me.flyray.bsin.facade.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface TzUnitedDeptService {

    @ApiOperation("查询顶级父节点")
    @Path("getParentDeptById")
    @Produces("application/json")
    Map<String, Object> getParentDeptById(Map<String, Object> requestMap);

    @ApiOperation("查询顶级父节点")
    @Path("getParentDeptByDeptId")
    @Produces("application/json")
    Map<String, Object> getParentDeptByDeptId(Map<String, Object> requestMap);

    @ApiOperation("根据id和部门id查询子节点")
    @Path("getUnitedDeptList")
    @Produces("application/json")
    Map<String, Object> getUnitedDeptList(Map<String, Object> requestMap);

    @ApiOperation("根据当前登录账号的部门id和当前点击的树节点id，判断树节点是否为部门id对应节点的子节点")
    @Path("determineNode")
    @Produces("application/json")
    Map<String, Object> determineNode(Map<String, Object> requestMap);

    @ApiOperation("根据id查询工会部门")
    @Path("getUnitedDept")
    @Produces("application/json")
    Map<String, Object> getUnitedDept(Map<String, Object> requestMap);

    @ApiOperation("根据id查询工会部门")
    @Path("addUnitedDept")
    @Produces("application/json")
    Map<String, Object> addUnitedDept(Map<String, Object> requestMap);


    @ApiOperation("根据id删除工会部门")
    @Path("deleteUnitedDept")
    @Produces("application/json")
    Map<String, Object> deleteUnitedDept(Map<String, Object> requestMap);


    @ApiOperation("根据名称查询")
    @Path("getUnitedDeptByName")
    @Produces("application/json")
    Map<String, Object> getUnitedDeptByName(Map<String, Object> requestMap);

}
