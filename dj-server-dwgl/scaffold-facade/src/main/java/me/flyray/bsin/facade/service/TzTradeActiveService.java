package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface TzTradeActiveService {


    @ApiOperation("新增工会活动")
    @Path("addTradeActive")
    @Produces("application/json")
    Map<String, Object> addTradeActive(Map<String, Object> requestMap);

    @ApiOperation("分页查询工会活动")
    @Path("queryTradeActiveList")
    @Produces("application/json")
    Map<String, Object> queryTradeActiveList(Map<String, Object> requestMap);

    @ApiOperation("根据id查询工会活动")
    @Path("getTradeActiveById")
    @Produces("application/json")
    Map<String, Object> getTradeActiveById(Map<String, Object> requestMap);

    @ApiOperation("删除工会活动")
    @Path("deleteTradeActive")
    @Produces("application/json")
    Map<String, Object> deleteTradeActive(Map<String, Object> requestMap);
}
