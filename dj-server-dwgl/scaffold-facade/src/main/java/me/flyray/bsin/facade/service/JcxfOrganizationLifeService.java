package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface JcxfOrganizationLifeService {

    @ApiOperation("")
    @POST
    @Path("queryTzOrganizationLifeList")
    @Produces("application/json")
    public Map<String, Object> queryTzOrganizationLifeList(Map<String, Object> requestMap);

    @ApiOperation("")
    @POST
    @Path("appQueryTzOrganizationLifeList")
    @Produces("application/json")
    Map<String, Object> appQueryTzOrganizationLifeList(Map<String, Object> requestMap);


    @ApiOperation("删除会议")
    @POST
    @Path("deleteTzOrganizationLife")
    @Produces("application/json")
    public Map<String, Object> deleteTzOrganizationLife(Map<String, Object> requestMap);

    @POST
    @Path("queryMeetingClass")
    @Produces("application/json")
    public Map<String, Object> queryMeetingClass(Map<String, Object> requestMap);


    @POST
    @Path("getMeetingCountNearlyThreeMonths")
    @Produces("application/json")
    public Map<String, Object> getMeetingCountNearlyThreeMonths(Map<String, Object> requestMap);

    @POST
    @Path("queryMeetingClassType")
    @Produces("application/json")
    public Map<String, Object> queryMeetingClassType(Map<String, Object> requestMap);

    @POST
    @Path("queryMeetingClassTypeByThisMonth")
    @Produces("application/json")
    public Map<String, Object> queryMeetingClassTypeByThisMonth(Map<String, Object> requestMap);

    @POST
    @Path("findImgById")
    @Produces("application/json")
    public Map<String, Object> findImgById(Map<String, Object> requestMap);

    @POST
    @Path("cancelMeeting")
    @Produces("application/json")
    public Map<String, Object> cancelMeeting(Map<String, Object> requestMap);

    @POST
    @Path("startMeeting")
    @Produces("application/json")
    public Map<String, Object> startMeeting(Map<String, Object> requestMap);

    @POST
    @Path("deleteFileById")
    @Produces("application/json")
    public Map<String, Object> deleteFileById(Map<String, Object> requestMap);

    @POST
    @Path("saveStudyFile")
    @Produces("application/json")
    public Map<String, Object> saveStudyFile(Map<String, Object> requestMap);

    @POST
    @Path("findStudyFile")
    @Produces("application/json")
    public Map<String, Object> findStudyFile(Map<String, Object> requestMap);


    @POST
    @Path("saveImg")
    @Produces("application/json")
    public Map<String, Object> saveImg(Map<String, Object> requestMap);


    @POST
    @Path("updateTzOrganizationLife")
    @Produces("application/json")
    public Map<String, Object> updateTzOrganizationLife(Map<String, Object> requestMap);

    @POST
    @Path("updateTzOrganizationLifeRecord")
    @Produces("application/json")
    Map<String, Object> updateTzOrganizationLifeRecord(Map<String, Object> requestMap);


    @POST
    @Path("getTzOrganizationLife")
    @Produces("application/json")
    public Map<String, Object> getTzOrganizationLife(Map<String, Object> requestMap);


    @POST
    @Path("endMeeting")
    @Produces("application/json")
    public Map<String, Object> endMeeting(Map<String, Object> requestMap);

    @POST
    @Path("addTzOrganizationLife")
    @Produces("application/json")
    public Map<String, Object> addTzOrganizationLife(Map<String, Object> requestMap);


    @POST
    @Path("countMyTotalClassNum")
    @Produces("application/json")
    public Map<String, Object> countMyTotalClassNum(Map<String, Object> requestMap);


    @ApiOperation("记录该会议的拟参会人员")
    @POST
    @Path("setPlanMeetingParty")
    @Produces("application/json")
    Map<String, Object> setPlanMeetingParty(Map<String, Object> requestMap);

    @ApiOperation("根据id获取会议")
    @POST
    @Path("getOrganizationLifeById")
    @Produces("application/json")
    Map<String, Object> getOrganizationLifeById(Map<String, Object> requestMap);

    @ApiOperation("获取参会人员信息")
    @POST
    @Path("getPlanMeetingPartyMemberInfo")
    @Produces("application/json")
    Map<String, Object> getPlanMeetingPartyMemberInfo(Map<String, Object> requestMap);

    @ApiOperation("查询组织生活和三会一课数量")
    @POST
    @Path("getPlanMeetingAllNumAndTypeNum")
    @Produces("application/json")
    Map<String, Object> getPlanMeetingAllNumAndTypeNum(Map<String, Object> requestMap);

    @ApiOperation("根据会议id查询拟参会人员id，根据拟参会人员id查出电话并发送短信")
    @POST
    @Path("sendMessageByPartyIds")
    @Produces("application/json")
    Map<String, Object> sendMessageByPartyIds(Map<String, Object> requestMap);

    @ApiOperation("本年度组织生活开展情况排名")
    @POST
    @Path("rankByYear")
    @Produces("application/json")
    Map<String, Object> rankByYear(Map<String, Object> requestMap);


    @ApiOperation("三会一课区县开展情况排行榜")
    @POST
    @Path("getOrganizationLifeRanking")
    @Produces("application/json")
    Map<String, Object> getOrganizationLifeRanking(Map<String, Object> requestMap);

    @ApiOperation("三会一课其他开展情况排行榜")
    @POST
    @Path("getOrganizationLifeRankingOther")
    @Produces("application/json")
    Map<String, Object> getOrganizationLifeRankingOther(Map<String, Object> requestMap);

    @ApiOperation("三会一课开展情况排行榜")
    @POST
    @Path("getLifeCountToRankOfMeetingType")
    @Produces("application/json")
    Map<String, Object> getLifeCountToRankOfMeetingType(Map<String, Object> requestMap);

    @ApiOperation("查询部门会议的详细统计情况")
    @POST
    @Path("getLifeDetailByDept")
    @Produces("application/json")
    Map<String, Object> getLifeDetailByDept(Map<String, Object> requestMap);

    @ApiOperation("三会一课近十年数量统计")
    @POST
    @Path("getMeetingDecadeCount")
    @Produces("application/json")
    Map<String, Object> getMeetingDecadeCount(Map<String, Object> requestMap);

    @ApiOperation("组织生活十年数量统计")
    @POST
    @Path("getMeetingDecadeCountGroupByYear")
    @Produces("application/json")
    Map<String, Object> getMeetingDecadeCountGroupByYear(Map<String, Object> requestMap);

    @ApiOperation("部门组织生活-三会一课各类型数量统计")
    @POST
    @Path("getMeetingCountByDeptId")
    @Produces("application/json")
    Map<String, Object> getMeetingCountByDeptId(Map<String, Object> requestMap);
}
