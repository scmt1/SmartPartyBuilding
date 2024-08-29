package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface TzQuestionBankService {
    @ApiOperation("新增题库")
    @Path("addQuestionBank")
    @Produces("application/json")
    Map<String, Object> addQuestionBank(Map<String, Object> requestMap);

    @ApiOperation("题库分页查询")
    @Path("queryQuestionBankByPage")
    @Produces("application/json")
    Map<String, Object> queryQuestionBankByPage(Map<String, Object> requestMap);

    @ApiOperation("根据id查询题库")
    @Path("findQuestionBankById")
    @Produces("application/json")
    Map<String, Object> findQuestionBankById(Map<String, Object> requestMap);

    @ApiOperation("根据id查询题库")
    @Path("findAllBankBy")
    @Produces("application/json")
    Map<String, Object> findAllBankBy(Map<String, Object> requestMap);

    @ApiOperation("根据id删除题库")
    @Path("deleteQuestionBank")
    @Produces("application/json")
    Map<String, Object> deleteQuestionBank(Map<String, Object> requestMap);


}
