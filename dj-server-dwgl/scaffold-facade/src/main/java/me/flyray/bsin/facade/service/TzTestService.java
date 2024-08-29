package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface TzTestService {
    @ApiOperation("新增试题")
    @Path("addTest")
    @Produces("application/json")
    Map<String, Object> addTest(Map<String, Object> requestMap);

    @ApiOperation("分页查询试题")
    @Path("queryTestByPage")
    @Produces("application/json")
    Map<String, Object> queryTestByPage(Map<String, Object> requestMap);

    @ApiOperation("分页查询试题")
    @Path("findTestById")
    @Produces("application/json")
    Map<String, Object> findTestById(Map<String, Object> requestMap);

    @ApiOperation("根据题库id查询试题")
    @Path("findTestByQuestionId")
    @Produces("application/json")
    Map<String, Object> findTestByQuestionId(Map<String, Object> requestMap);


}
