package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

public interface TzMessageService {


    @ApiOperation("新增消息管理数据")
    @Path("sendMessage")
    Map<String, Object> sendMessage(Map<String, Object> requestMap);

    @ApiOperation("新增模板消息数据")
    @Path("sendTemplateMessage")
    Map<String, Object> sendTemplateMessage(Map<String, Object> requestMap);

    @ApiOperation("查询发送的信息")
    @Path("queryMessage")
    Map<String, Object> queryMessage(Map<String, Object> requestMap);

    @Path("queryMessageTemplate")
    Map<String, Object> queryMessageTemplate(Map<String, Object> requestMap);

    @Path("queryMessageDetail")
    Map<String, Object> queryMessageDetail(Map<String, Object> requestMap);

    @ApiOperation("验证码发送")
    @Path("sendVCode")
    Map<String, Object> sendVCode(Map<String, Object> requestMap);

    @ApiOperation("撤回定时发送")
    @Path("recallSend")
    Map<String, Object> recallSend(Map<String, Object> requestMap);

    @ApiOperation("校验验证码是否正确")
    @Path("validCode")
    Map<String, Object> validCode(Map<String, Object> requestMap);

    @ApiOperation("统计短信的基本信息")
    @Path("baseInfo")
    Map<String, Object> baseInfo(Map<String, Object> requestMap);

    @ApiOperation("统计短信的基本信息")
    @Path("baseInfoByToday")
    Map<String, Object> baseInfoByToday(Map<String, Object> requestMap);

    @ApiOperation("统计近10天发送量")
    @Path("recentlySendCount")
    Map<String, Object> recentlySendCount(Map<String, Object> requestMap);

    @ApiOperation("10天内短信发送成功率")
    @Path("recentlySuccessRate")
    Map<String, Object> recentlySuccessRate(Map<String, Object> requestMap);

    @ApiOperation("查询本单位生日短信情况")
    @Path("queryBirthdayMessageDetail")
    Map<String, Object> queryBirthdayMessageDetail(Map<String, Object> requestMap);

    @ApiOperation("登录绑定验证码发送")
    @Path("sendBindVCode")
    Map<String, Object> sendBindVCode(Map<String, Object> requestMap);
}
