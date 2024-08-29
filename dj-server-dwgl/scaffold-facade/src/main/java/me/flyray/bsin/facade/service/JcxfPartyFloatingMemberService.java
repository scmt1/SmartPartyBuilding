package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("jcxfPartyFloatingMember")
public interface JcxfPartyFloatingMemberService {

    @ApiOperation("")
    @Path("flowOutPartyMemberList")
    @Produces("application/json")
    Map<String, Object> flowOutPartyMemberList(Map<String, Object> requestMap);

    @ApiOperation("")
    @Path("getPartyFloatingBy")
    @Produces("application/json")
    Map<String, Object> getPartyFloatingById(Map<String, Object> requestMap);

    @ApiOperation("")
    @Path("getPartyFloatingByPartyMemberId")
    @Produces("application/json")
    Map<String, Object> getPartyFloatingByPartyMemberId(Map<String, Object> requestMap);

    @ApiOperation("")
    @Path("addFloatingMember")
    @Produces("application/json")
    Map<String, Object> addFloatingMember(Map<String, Object> requestMap);

    @ApiOperation("")
    @Path("updateFloatingMember")
    @Produces("application/json")
    Map<String, Object> updateFloatingMember(Map<String, Object> requestMap);

    @ApiOperation("")
    @Path("delFloatingMember")
    @Produces("application/json")
    Map<String, Object> delFloatingMember(Map<String, Object> requestMap);

    @ApiOperation("流回")
    @Path("setReturnPartyMember")
    @Produces("application/json")
    Map<String, Object> setReturnPartyMember(Map<String, Object> requestMap);

    @ApiOperation("")
    @Path("getPartyFloatingListByPartyMemberId")
    @Produces("application/json")
    Map<String, Object> getPartyFloatingListByPartyMemberId(Map<String, Object> requestMap);

}
