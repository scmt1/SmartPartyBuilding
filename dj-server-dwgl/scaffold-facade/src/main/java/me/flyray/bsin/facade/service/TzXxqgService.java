package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

public interface TzXxqgService {

    @ApiOperation("根据detailId查询投票记录")
    @Path("downTemplate")
    //@Produces({"application/vnd.ms-excel", "application/octet-stream"})
    Response downTemplate(Map<String, Object> requestMap) throws IOException, Exception;
    @ApiOperation("根据detailId查询投票记录")
    @Path("xxx")
    @Produces({"application/vnd.ms-excel", "application/octet-stream"})
    Map<String, Object> xxx(Map<String, Object> requestMap,HttpServletResponse response) throws IOException;

    @ApiOperation("学习强国分页查询")
    @Path("queryByPage")
    @Produces("application/json")
    Map<String, Object> queryByPage(Map<String, Object> requestMap);

    @ApiOperation("根据前端带来的excel模板数据来添加")
    @Path("addXxqgList")
    @Produces("application/json")
    Map<String, Object> addXxqgList(Map<String, Object> requestMap);
}
