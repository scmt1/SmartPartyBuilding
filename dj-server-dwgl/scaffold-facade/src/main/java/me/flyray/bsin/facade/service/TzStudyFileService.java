package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("tzStudyFile")
public interface TzStudyFileService {

    @ApiOperation("根据主键来取消发布学习文件")
    @POST
    @Path("cancelPostIt")
    @Produces("application/json")
    Map<String, Object> cancelPostIt(Map<String, Object> requestMap);

    @ApiOperation("根据主键来发布学习文件")
    @POST
    @Path("postIt")
    @Produces("application/json")
    Map<String, Object> postIt(Map<String, Object> requestMap);

    @ApiOperation("分页查询学习文件")
    @POST
    @Path("queryTzTStudyFile")
    @Produces("application/json")
    Map<String, Object> queryTzTStudyFile(Map<String, Object> requestMap);

    @ApiOperation("根据主键来删除deleteTStudyFile数据")
    @POST
    @Path("deleteTStudyFile")
    @Produces("application/json")
    Map<String, Object> deleteTStudyFile(Map<String, Object> requestMap);

    @ApiOperation("新增学习文件")
    @POST
    @Path("saveStudyFile")
    @Produces("application/json")
    Map<String, Object> saveStudyFile(Map<String, Object> requestMap);

    @ApiOperation("根据主键来获取getStudyFile数据")
    @POST
    @Path("saveStudyFile")
    @Produces("application/json")
    Map<String, Object> getStudyFile(Map<String, Object> requestMap);

}
