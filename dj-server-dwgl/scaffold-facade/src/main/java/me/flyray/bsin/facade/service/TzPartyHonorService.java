package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface TzPartyHonorService {

    @ApiOperation("查询荣誉")
    @POST
    @Path("queryTzPartyHonorList")
    @Produces("application/json")
    Map<String, Object> queryTzPartyHonorList(Map<String, Object> requestMap);

    @ApiOperation("新增荣誉")
    @POST
    @Path("addTzPartyHonor")
    @Produces("application/json")
    Map<String, Object> addTzPartyHonor(Map<String, Object> requestMap);

    @ApiOperation("修改荣誉")
    @POST
    @Path("updateTzPartyHonor")
    @Produces("application/json")
    Map<String, Object> updateTzPartyHonor(Map<String, Object> requestMap);

    @ApiOperation("删除荣誉")
    @POST
    @Path("delTzPartyHonor")
    @Produces("application/json")
    Map<String, Object> delTzPartyHonor(Map<String, Object> requestMap);

    @ApiOperation("获取荣誉信息")
    @POST
    @Path("getTzPartyHonorById")
    @Produces("application/json")
    Map<String, Object> getTzPartyHonorById(Map<String, Object> requestMap);

    @ApiOperation("查询本部门的荣誉")
    @POST
    @Path("queryTzPartyHonorListOneselfDept")
    @Produces("application/json")
    Map<String, Object> queryTzPartyHonorListOneselfDept(Map<String, Object> requestMap);

    @ApiOperation("通过审核")
    @POST
    @Path("passHonor")
    @Produces("application/json")
    Map<String, Object> passHonor(Map<String, Object> requestMap);

    @ApiOperation("驳回审核")
    @POST
    @Path("unPass")
    @Produces("application/json")
    Map<String, Object> unPass(Map<String, Object> requestMap);

    @ApiOperation("审核记录")
    @POST
    @Path("queryHonorRecord")
    @Produces("application/json")
    Map<String, Object> queryHonorRecord(Map<String, Object> requestMap);

    @ApiOperation("查询党员自己上传的荣誉")
    @POST
    @Path("queryTzPartyHonorListOneselfPartyMember")
    @Produces("application/json")
    Map<String, Object> queryTzPartyHonorListOneselfPartyMember(Map<String, Object> requestMap);

    @ApiOperation("部门荣誉排行")
    @POST
    @Path("deptHonorRank")
    @Produces("application/json")
    Map<String, Object> deptHonorRank(Map<String, Object> requestMap);
}
