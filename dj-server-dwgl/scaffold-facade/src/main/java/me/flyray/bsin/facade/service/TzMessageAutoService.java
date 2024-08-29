package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface TzMessageAutoService {

    @ApiOperation("分页查询权限内模板")
    @Path("queryMessageAuto")
    Map<String, Object> queryMessageAuto(Map<String, Object> requestMap);

    @ApiOperation("分页查询所有模板")
    @Path("getMessageAutoPageByAdmin")
    Map<String, Object> getMessageAutoPageByAdmin(Map<String, Object> requestMap);

    @ApiOperation("修改默认模板状态")
    @Path("updateAutoMessageOpenStatus")
    Map<String, Object> updateAutoMessageOpenStatus(Map<String, Object> requestMap);

    @ApiOperation("根据id查询发送的信息")
    @Path("queryMessageAutoById")
    Map<String, Object> queryMessageAutoById(Map<String, Object> requestMap);

    @ApiOperation("根据id查询发送的信息")
    @Path("queryMessageAutoByAdmin")
    Map<String, Object> queryMessageAutoByAdmin(Map<String, Object> requestMap);

    @ApiOperation("根据id修改发送的信息")
    @Path("updateMessageAutoById")
    Map<String, Object> updateMessageAutoById(Map<String, Object> requestMap);

    @ApiOperation("获取自动发送任务")
    @Path("getMessageAutoList")
    Map<String, Object> getMessageAutoList(Map<String, Object> requestMap);

    @ApiOperation("查询今日政治生日的党员并且发送短信祝福")
    @Path("queryPolicyBirthdayToday")
    @Produces("application/json")
    void queryPolicyBirthdayToday();


    @ApiOperation("查询今日生日的党员并且发送短信祝福")
    @Path("queryBirthdayToday")
    @Produces("application/json")
    void queryBirthdayToday();

    @ApiOperation("查询生日短信模板")
    @Path("queryBirthdayTemplate")
    @Produces("application/json")
    Map<String, Object> queryBirthdayTemplate(Map<String, Object> requestMap);

}
