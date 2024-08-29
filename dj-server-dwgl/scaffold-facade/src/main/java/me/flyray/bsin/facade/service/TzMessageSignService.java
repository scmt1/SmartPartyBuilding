package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Path;
import java.util.Map;

public interface TzMessageSignService {

    @ApiOperation("保存短信签名")
    @Path("saveMessageSign")
    Map<String, Object> saveMessageSign(Map<String, Object> requestMap);

    @ApiOperation("删除短信签名")
    @Path("deleteMessageSign")
    Map<String, Object> deleteMessageSign(Map<String, Object> requestMap);

    @ApiOperation("修改短信签名")
    @Path("updateMessageSign")
    Map<String, Object> updateMessageSign(Map<String, Object> requestMap);

    @ApiOperation("获取短信签名")
    @Path("getMessageSign")
    Map<String, Object> getMessageSign(Map<String, Object> requestMap);

    @ApiOperation("查询短信签名")
    @Path("queryMessageSign")
    Map<String, Object> queryMessageSign(Map<String, Object> requestMap);

    @ApiOperation("查询短信签名列表")
    @Path("queryMessageSignList")
    Map<String, Object> queryMessageSignList(Map<String, Object> requestMap);
}
