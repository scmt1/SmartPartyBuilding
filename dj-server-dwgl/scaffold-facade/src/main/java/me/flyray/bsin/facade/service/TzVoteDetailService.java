package me.flyray.bsin.facade.service;


import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("tzVoteDetail")
public interface TzVoteDetailService {

    @ApiOperation("查询投票项")
    @POST
    @Path("queryTzVoteDetailListByPage")
    @Produces("application/json")
    Map<String, Object> queryTzVoteDetailListByPage(Map<String, Object> requestMap);

    @ApiOperation("根据id查询具体的投票排名")
    @POST
    @Path("queryTzVoteDetailById")
    @Produces("application/json")
    Map<String, Object> queryTzVoteDetailById(Map<String, Object> requestMap);

    @ApiOperation("根据id获取投票项的信息")
    @POST
    @Path("getVoteDetailInfoById")
    @Produces("application/json")
    Map<String, Object> getVoteDetailInfoById(Map<String, Object> requestMap);

    @ApiOperation("新增投票项")
    @POST
    @Path("addVoteDetail")
    @Produces("application/json")
    Map<String, Object> addVoteDetail(Map<String, Object> requestMap);

    @ApiOperation("修改投票项信息")
    @POST
    @Path("editVoteDetail")
    @Produces("application/json")
    Map<String, Object> editVoteDetail(Map<String, Object> requestMap);

    @ApiOperation("删除投票项")
    @POST
    @Path("deleteVoteDetail")
    @Produces("application/json")
    Map<String, Object> deleteVoteDetail(Map<String, Object> requestMap);

    @ApiOperation("变更投票项状态")
    @POST
    @Path("editVoteDetailStatus")
    @Produces("application/json")
    Map<String, Object> editVoteDetailStatus(Map<String, Object> requestMap);


}
