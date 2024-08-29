package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface JcxfSJKBService {

    @ApiOperation("三会一课（区县）")
    @POST
    @Path("partMeeting")
    @Produces("application/json")
    Map<String, Object> partMeeting(Map<String, Object> requestMap);

    @ApiOperation("三会一课(其他)")
    @POST
    @Path("qtpartMeeting")
    @Produces("application/json")
    Map<String, Object> qtpartMeeting(Map<String, Object> requestMap);


    @ApiOperation("组织生活(区县)")
    @POST
    @Path("theoryMetting")
    @Produces("application/json")
    Map<String, Object> theoryMetting(Map<String, Object> requestMap);

    @ApiOperation("组织生活(其他)")
    @POST
    @Path("qttheoryMetting")
    @Produces("application/json")
    Map<String, Object> qttheoryMetting(Map<String, Object> requestMap);

    @ApiOperation("组织生活完成情况详情")
    @POST
    @Path("getLifeDetailByDept")
    @Produces("application/json")
    Map<String, Object> getLifeDetailByDept(Map<String, Object> requestMap);

}
