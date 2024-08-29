package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("TzStudyView")
public interface TzStudyViewService {

    @ApiOperation("获取设置信息")
    @POST
    @Path("getTzStudyViewByStudyId")
    @Produces("application/json")
    Map<String, Object> getTzStudyViewByStudyId(Map<String, Object> requestMap);

    @ApiOperation("获取列表")
    @POST
    @Path("saveTzStudyView")
    @Produces("application/json")
    Map<String, Object> saveTzStudyView(Map<String, Object> requestMap);

    @ApiOperation("获取课程范围内的人员/部门")
    @POST
    @Path("getViewScopeInfoByStudyId")
    @Produces("application/json")
    Map<String, Object> getViewScopeInfoByStudyId(Map<String, Object> requestMap);
}
