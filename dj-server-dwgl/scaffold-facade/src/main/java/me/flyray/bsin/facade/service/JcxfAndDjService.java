package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("JcxfAndDj")
public interface JcxfAndDjService {

    @ApiOperation("查询日志")
    @POST
    @Path("syncLife")
    @Produces("application/json")
    Map<String, Object> syncLife(Map<String, Object> requestMap);

    @ApiOperation("重置所有党员的密码")
    @POST
    @Path("resetAllMemberPassword")
    @Produces("application/json")
    Map<String, Object> resetAllMemberPassword(Map<String, Object> requestMap);

}
