package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface TzVoteService {
    @ApiOperation("新增投票")
    @POST
    @Path("addVote")
    @Produces("application/json")
    Map<String, Object> addVote(Map<String, Object> requestMap);

    @ApiOperation("分页查询vote数据")
    @POST
    @Path("queryTzVoteList")
    @Produces("application/json")
    Map<String, Object> queryTzVoteList(Map<String, Object> requestMap);


    @ApiOperation("根据id查询投票信息")
    @POST
    @Path("queryTzVoteById")
    @Produces("application/json")
    Map<String, Object> queryTzVoteById(Map<String, Object> requestMap);


    @ApiOperation("根据id删除投票信息")
    @POST
    @Path("deleteTzVoteById")
    @Produces("application/json")
    Map<String, Object> deleteTzVoteById(Map<String, Object> requestMap);



    @ApiOperation("模拟移动端用户登录后，访问自己能查看的投票")
    @POST
    @Path("queryVote")
    @Produces("application/json")
    Map<String, Object> queryVote(Map<String, Object> requestMap);

    @ApiOperation("根据id发布或取消投票")
    @POST
    @Path("updateStatusById")
    @Produces("application/json")
    Map<String, Object> updateStatusById(Map<String, Object> requestMap);

    @ApiOperation("根据id修改投票")
    @POST
    @Path("updateVoteById")
    @Produces("application/json")
    Map<String, Object> updateVoteById(Map<String, Object> requestMap);

}
