package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

public interface TzTradeUnionService {

    @ApiOperation("查询顶级父节点")
    @Path("getParentDeptById")
    @Produces("application/json")
    Map<String, Object> getParentDeptById(Map<String, Object> requestMap);

    @ApiOperation("查询顶级父节点")
    @Path("getParentDeptByDeptId")
    @Produces("application/json")
    Map<String, Object> getParentDeptByDeptId(Map<String, Object> requestMap);

    @ApiOperation("通过名称搜索：限10个")
    @Path("getTradeUnionDeptByName")
    @Produces("application/json")
    Map<String, Object> getTradeUnionDeptByName(Map<String, Object> requestMap);

    @ApiOperation("根据id和部门id查询子节点")
    @Path("getTradeUnionDeptList")
    @Produces("application/json")
    Map<String, Object> getTradeUnionDeptList(Map<String, Object> requestMap);

    @ApiOperation("根据当前登录账号的部门id和当前点击的树节点id，判断树节点是否为部门id对应节点的子节点")
    @Path("determineNode")
    @Produces("application/json")
    Map<String, Object> determineNode(Map<String, Object> requestMap);

    @ApiOperation("根据id查询工会部门")
    @Path("getTradeUnion")
    @Produces("application/json")
    Map<String, Object> getTradeUnion(Map<String, Object> requestMap);

    @ApiOperation("根据id查询工会部门")
    @Path("addTradeUnion")
    @Produces("application/json")
    Map<String, Object> addTradeUnion(Map<String, Object> requestMap);


    @ApiOperation("根据id删除工会部门")
    @Path("deleteTrade")
    @Produces("application/json")
    Map<String, Object> deleteTrade(Map<String, Object> requestMap);

}
