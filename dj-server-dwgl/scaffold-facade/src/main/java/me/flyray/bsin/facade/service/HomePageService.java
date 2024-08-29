package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("homePage")
public interface HomePageService {

    @ApiOperation(value = "党员数量统计")
    @Path("getPartyMemberCount")
    @Produces("application/json")
    Map<String, Object> getPartyMemberCount(Map<String, Object> requestMap);

    @ApiOperation(value = "部门数量")
    @Path("getDeptCount")
    @Produces("application/json")
    Map<String, Object> getDeptCount(Map<String, Object> requestMap);

    @ApiOperation(value = "本月三会一课统计")
    @Path("getMeetingCountThisMonth")
    @Produces("application/json")
    Map<String, Object> getMeetingCountThisMonth(Map<String, Object> requestMap);

    @ApiOperation(value = "今年组织生活统计")
    @Path("getMeetingCountThisYear")
    @Produces("application/json")
    Map<String, Object> getMeetingCountThisYear(Map<String, Object> requestMap);

    @ApiOperation(value = "本月党费统计")
    @Path("getPayFeeMoneyByMonth")
    @Produces("application/json")
    Map<String, Object> getPayFeeMoneyByMonth(Map<String, Object> requestMap);

    @ApiOperation(value = "性别统计")
    @Path("getGenderCount")
    @Produces("application/json")
    Map<String, Object> getGenderCount(Map<String, Object> requestMap);

    @ApiOperation(value = "流动党员统计")
    @Path("getFloatingPartyMemberCount")
    @Produces("application/json")
    Map<String, Object> getFloatingPartyMemberCount(Map<String, Object> requestMap);

    @ApiOperation(value = "年龄统计")
    @Path("getAgeDistribution")
    @Produces("application/json")
    Map<String, Object> getAgeDistribution(Map<String, Object> requestMap);

    @ApiOperation(value = "党龄统计")
    @Path("getPartyStandingDistribution")
    @Produces("application/json")
    Map<String, Object> getPartyStandingDistribution(Map<String, Object> requestMap);

    @ApiOperation(value = "换届数量统计")
    @Path("getTermInfo")
    @Produces("application/json")
    Map<String, Object> getTermInfo(Map<String, Object> requestMap);

    @ApiOperation(value = "近一年党费缴纳情况")
    @Path("getPayFeeNearlyYearCount")
    @Produces("application/json")
    Map<String, Object> getPayFeeNearlyYearCount(Map<String, Object> requestMap);

    @ApiOperation(value = "近一年三会一课数量")
    @Path("getMeetingNearlyYearCount")
    @Produces("application/json")
    Map<String, Object> getMeetingNearlyYearCount(Map<String, Object> requestMap);

    @ApiOperation(value = "获取预备党员数量")
    @Path("getPartyMemberYuBeiCount")
    @Produces("application/json")
    Map<String, Object> getPartyMemberYuBeiCount(Map<String, Object> requestMap);

    @ApiOperation(value = "获取党务工作者数量")
    @Path("getPartyWorkerCount")
    @Produces("application/json")
    Map<String, Object> getPartyWorkerCount(Map<String, Object> requestMap);

    @ApiOperation(value = "获取失联党员数量")
    @Path("getPartyLostCount")
    @Produces("application/json")
    Map<String, Object> getPartyLostCount(Map<String, Object> requestMap);

    @ApiOperation(value = "获取职务分布")
    @Path("getPositionDistribution")
    @Produces("application/json")
    Map<String, Object> getPositionDistribution(Map<String, Object> requestMap);

    @ApiOperation(value = "当前及其下级党费统计")
    @Path("getPayFeeTotal")
    @Produces("application/json")
    Map<String, Object> getPayFeeTotal(Map<String, Object> requestMap);

}
