package me.flyray.bsin.facade.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("tzVoteColumn")
public interface TzVideoColumnService {

    @ApiOperation("查询所有栏目")
    @POST
    @Path("queryAll")
    @Produces("application/json")
    Map<String, Object> queryAll(Map<String, Object> requestMap);

    @ApiOperation("分页查询栏目")
    @POST
    @Path("queryVideoColumnByPage")
    @Produces("application/json")
    Map<String, Object> queryVideoColumnByPage(Map<String, Object> requestMap);

    @ApiOperation("新增栏目")
    @POST
    @Path("addVideoColumn")
    @Produces("application/json")
    Map<String, Object> addVideoColumn(Map<String, Object> requestMap);

    @ApiOperation("根据主键来获取栏目视频数据")
    @POST
    @Path("getVideoColumn")
    @Produces("application/json")
    Map<String, Object> getVideoColumn(Map<String, Object> requestMap);

    @ApiOperation("根据主键来删除deleteTStudyFile数据")
    @POST
    @Path("deleteVideoColumn")
    @Produces("application/json")
    Map<String, Object> deleteVideoColumn(Map<String, Object> requestMap);

    @ApiOperation("根据type获取栏目列表")
    @POST
    @Path("getVideoColumnListByType")
    @Produces("application/json")
    Map<String, Object> getVideoColumnListByType(Map<String, Object> requestMap);

}
