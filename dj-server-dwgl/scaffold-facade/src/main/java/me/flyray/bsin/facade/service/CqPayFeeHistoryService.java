package me.flyray.bsin.facade.service;


import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("cqPayFeeHistoryService")
public interface CqPayFeeHistoryService {

    /**
     * 查询部门
     */
    @POST
    @Path("getPayFeeHistoryList")
    @Produces("application/json")
    Map<String, Object> getPayFeeHistoryList(Map<String, Object> requestMap);

}
