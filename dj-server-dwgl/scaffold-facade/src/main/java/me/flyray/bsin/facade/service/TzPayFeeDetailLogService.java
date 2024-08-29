package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface TzPayFeeDetailLogService {

    @ApiOperation("查询列表")
    @POST
    @Path("queryTzPayFeeDetailLogPage")
    @Produces("application/json")
    Map<String, Object> queryTzPayFeeDetailLogPage(Map<String, Object> requestMap);

}
