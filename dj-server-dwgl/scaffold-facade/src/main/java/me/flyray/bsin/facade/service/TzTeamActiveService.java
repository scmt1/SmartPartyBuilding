package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface TzTeamActiveService {


    @ApiOperation("根据部门id查询团组织")
    @Path("addTeamActive")
    @Produces("application/json")
    Map<String, Object> addTeamActive(Map<String, Object> requestMap);


    @ApiOperation("分页查询团组织活动")
    @Path("queryTeamActiveList")
    @Produces("application/json")
    Map<String, Object> queryTeamActiveList(Map<String, Object> requestMap);

    @ApiOperation("根据id查询团组织活动")
    @Path("getTeamActiveById")
    @Produces("application/json")
    Map<String, Object> getTeamActiveById(Map<String, Object> requestMap);

    @ApiOperation("根据id删除团组织活动")
    @Path("deleteTeamActive")
    @Produces("application/json")
    Map<String, Object> deleteTeamActive(Map<String, Object> requestMap);



}
