package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface TzUserTagService {


    @ApiOperation("新增自定义用户组")
    @POST
    @Path("addCustomUserTag")
    @Produces("application/json")
    Map<String, Object> addCustomUserTag(Map<String, Object> requestMap);


    @ApiOperation("删除")
    @POST
    @Path("deleteCustomUserTag")
    @Produces("application/json")
    Map<String, Object> deleteCustomUserTag(Map<String, Object> requestMap);



    @ApiOperation("查询自定义用户组")
    @POST
    @Path("queryCustomUserTag")
    @Produces("application/json")
    Map<String, Object> queryCustomUserTag(Map<String, Object> requestMap);



    @ApiOperation("查询自定义用户组")
    @POST
    @Path("getCustomUserTag")
    @Produces("application/json")
    Map<String, Object> getCustomUserTag(Map<String, Object> requestMap);

}
