package me.flyray.bsin.facade.service;


import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.Map;

public interface JcxfPersonMeetingService {


    @ApiOperation("")
    @POST
    @Path("queryTzPersonMeetingListByPage")
    @Produces("application/json")
    public Map<String, Object> queryTzPersonMeetingListByPage(Map<String, Object> requestMap);


    @ApiOperation("导出")
    @POST
    @Path("download")
    @Produces("application/json")
    public Map<String, Object> download(Map<String, Object> requestMap);



    List<Map<String, String>> checkMeeting(String id, String year, String meetingType);

    @ApiOperation("查询会议参会人员详情")
    @POST
    @Path("getJoinsPartyMemberListByMeetingId")
    @Produces("application/json")
    Map<String, Object> getJoinsPartyMemberListByMeetingId(Map<String, Object> requestMap);

}
