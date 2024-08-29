package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("tzStudyUser")
public interface TzStudyUserService {

    @ApiOperation("获取列表")
    @POST
    @Path("getTzStudyUserListByPage")
    @Produces("application/json")
    Map<String, Object> getTzStudyUserListByPage(Map<String, Object> requestMap);

    @ApiOperation("获取党员所有的学习记录")
    @POST
    @Path("getTzStudyUserList")
    @Produces("application/json")
    Map<String, Object> getTzStudyUserList(Map<String, Object> requestMap);

}
