package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("jcxfPartyMemberDeptTransferLog")
public interface JcxfPartyMemberTransferService {

    @ApiOperation("分页查询组织关系转移数据")
    @POST
    @Path("queryPartyMemberDeptTransferLogList")
    @Produces("application/json")
    Map<String, Object> queryPartyMemberDeptTransferLogList(Map<String, Object> requestMap);

    @ApiOperation("新增")
    @POST
    @Path("addPartyMemberDeptTransfer")
    @Produces("application/json")
    Map<String, Object> addPartyMemberDeptTransfer(Map<String, Object> requestMap);

    @ApiOperation("删除")
    @POST
    @Path("delPartyMemberDeptTransfer")
    @Produces("application/json")
    Map<String, Object> delPartyMemberDeptTransfer(Map<String, Object> requestMap);

    @ApiOperation("根据id获取详情")
    @POST
    @Path("getPartyMemberDeptTransferById")
    @Produces("application/json")
    Map<String, Object> getPartyMemberDeptTransferById(Map<String, Object> requestMap);

    @ApiOperation("根据id修改状态")
    @POST
    @Path("updateTransferStatusById")
    @Produces("application/json")
    Map<String, Object> updateTransferStatusById(Map<String, Object> requestMap);

    @ApiOperation("根据党员id查询历史记录")
    @POST
    @Path("getTransferPageByPartyMemberId")
    @Produces("application/json")
    Map<String, Object> getTransferPageByPartyMemberId(Map<String, Object> requestMap);

    @ApiOperation("根据id取消申请")
    @POST
    @Path("cancelTransferById")
    @Produces("application/json")
    Map<String, Object> cancelTransferById(Map<String, Object> requestMap);

}
