package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface JcxfMeetingService {

    @ApiOperation("通过党员id查询三会一课列表")
    @POST
    @Path("getMeetingByPartyMemberId")
    @Produces("application/json")
    Map<String, Object> getMeetingByPartyMemberId(Map<String, Object> requestMap);
}
