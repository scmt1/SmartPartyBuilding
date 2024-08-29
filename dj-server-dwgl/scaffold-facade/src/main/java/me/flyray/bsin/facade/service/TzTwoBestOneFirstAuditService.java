package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface TzTwoBestOneFirstAuditService {

    @ApiOperation("根据两优一先id查询审核记录")
    @POST
    @Path("getAuditListByTbofId")
    @Produces("application/json")
    Map<String, Object> getAuditListByTbofId(Map<String, Object> requestMap);

    @ApiOperation("新增记录")
    @POST
    @Path("addAudit")
    @Produces("application/json")
    Map<String, Object> addAudit(Map<String, Object> requestMap) throws Exception;

}
