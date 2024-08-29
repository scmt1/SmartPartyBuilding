package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("tzStudyVide")
public interface TzStudyVideoService {

    @ApiOperation("根据主键来删除TzStudyVideo数据")
    @POST
    @Path("deleteTzStudyVideo")
    @Produces("application/json")
    Map<String, Object> deleteTzStudyVideo(Map<String, Object> requestMap);

    @POST
    @Path("postVideo")
    @Produces("application/json")
    Map<String, Object> postVideo(Map<String, Object> requestMap);

    @POST
    @Path("queryTzStudyVideoList")
    @Produces("application/json")
    Map<String, Object> queryTzStudyVideoList(Map<String, Object> requestMap);

    @POST
    @Path("saveVideo")
    @Produces("application/json")
    Map<String, Object> saveVideo(Map<String, Object> requestMap);

    @POST
    @Path("getTzStudyVideo")
    @Produces("application/json")
    Map<String, Object> getTzStudyVideo(Map<String, Object> requestMap);

    @POST
    @Path("getTzStudyVideoByApp")
    @Produces("application/json")
    Map<String, Object> getTzStudyVideoByApp(Map<String, Object> requestMap);


    @ApiOperation("手机端党建学习根据type获取置顶课程")
    @POST
    @Path("getTzStudyVideoListByType")
    @Produces("application/json")
    Map<String, Object> getTzStudyVideoListByType(Map<String, Object> requestMap);


    @ApiOperation("手机端点击学习课程的文章后新增对应的记录")
    @POST
    @Path("addTzStudyUser")
    @Produces("application/json")
    Map<String, Object> addTzStudyUser(Map<String, Object> requestMap);

    @ApiOperation("手机端点击课程内容查询是否已经学习")
    @POST
    @Path("queryStudyStatus")
    @Produces("application/json")
    Map<String, Object> queryStudyStatus(Map<String, Object> requestMap);

    @ApiOperation("手机端点击课程内容查询已学习人数")
    @POST
    @Path("queryStudyCount")
    @Produces("application/json")
    Map<String, Object> queryStudyCount(Map<String, Object> requestMap);

    @ApiOperation("查询用户最后学习的课程")
    @POST
    @Path("getLastStudyByUserId")
    @Produces("application/json")
    Map<String, Object> getLastStudyByUserId(Map<String, Object> requestMap);

    @ApiOperation("手机端获取课程分页列表")
    @POST
    @Path("getVideoColumnListByPage")
    @Produces("application/json")
    Map<String, Object> getVideoColumnListByPage(Map<String, Object> requestMap);

    @ApiOperation("获取该视频课程的往期课程")
    @POST
    @Path("getPastVideoColumnList")
    @Produces("application/json")
    Map<String, Object> getPastVideoColumnList(Map<String, Object> requestMap);

    @ApiOperation("发送学习短信")
    @POST
    @Path("sendStudyMessage")
    @Produces("application/json")
    Map<String, Object> sendStudyMessage(Map<String, Object> requestMap);
}
