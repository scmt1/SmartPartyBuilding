package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("developParty")
public interface JcxfDevelopPartyService {

    @ApiOperation("发展党员分页查询")
    @Path("queryDevelopPartyList")
    @Produces("application/json")
    Map<String, Object> queryDevelopPartyList(Map<String, Object> requestMap);

    @ApiOperation("获取党员发展历程")
    @Path("getDevelopPartyListByPartyMemberId")
    @Produces("application/json")
    Map<String, Object> getDevelopPartyListByPartyMemberId(Map<String, Object> requestMap);

    @ApiOperation("发展党员数量统计")
    @Path("getBaseCount")
    @Produces("application/json")
    Map<String, Object> getBaseCount(Map<String, Object> requestMap);

    @ApiOperation("发展对象信息删除")
    @Path("deleteDevelopParty")
    @Produces("application/json")
    Map<String, Object> deleteDevelopParty(Map<String, Object> requestMap);

    @ApiOperation("新增发展党员信息")
    @Path("addDevelopPartyMember")
    @Produces("application/json")
    Map<String, Object> addDevelopPartyMember(Map<String, Object> requestMap);

    @ApiOperation("获取发展党员信息")
    @Path("getDevelopParty")
    @Produces("application/json")
    Map<String, Object> getDevelopParty(Map<String, Object> requestMap);

    @ApiOperation("修改发展党员信息")
    @Path("updateDevelopPartyMember")
    @Produces("application/json")
    Map<String, Object> updateDevelopPartyMember(Map<String, Object> requestMap);

}
