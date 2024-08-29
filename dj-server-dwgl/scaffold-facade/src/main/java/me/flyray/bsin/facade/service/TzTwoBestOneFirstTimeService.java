package me.flyray.bsin.facade.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("tzTwoBestOneFirstTime")
public interface TzTwoBestOneFirstTimeService {

    @POST
    @Path("queryTzTwoBestOneFirstTimePage")
    @Produces("application/json")
    Map<String, Object> queryTzTwoBestOneFirstTimePage(Map<String, Object> requestMap);

    @POST
    @Path("addTzTwoBestOneFirstTime")
    @Produces("application/json")
    Map<String, Object> addTzTwoBestOneFirstTime(Map<String, Object> requestMap);

    @POST
    @Path("updateTzTwoBestOneFirstTime")
    @Produces("application/json")
    Map<String, Object> updateTzTwoBestOneFirstTime(Map<String, Object> requestMap);

    @POST
    @Path("getTzTwoBestOneFirstTimeById")
    @Produces("application/json")
    Map<String, Object> getTzTwoBestOneFirstTimeById(Map<String, Object> requestMap);

    @POST
    @Path("deleteTzTwoBestOneFirstTimeById")
    @Produces("application/json")
    Map<String, Object> deleteTzTwoBestOneFirstTimeById(Map<String, Object> requestMap);

    @POST
    @Path("updateTimeStatusById")
    @Produces("application/json")
    Map<String, Object> updateTimeStatusById(Map<String, Object> requestMap);

    @POST
    @Path("getIsAddTime")
    @Produces("application/json")
    Map<String, Object> getIsAddTime(Map<String, Object> requestMap);

    @POST
    @Path("getIsAuditTime")
    @Produces("application/json")
    Map<String, Object> getIsAuditTime(Map<String, Object> requestMap);

    @POST
    @Path("getAddTimeList")
    @Produces("application/json")
    Map<String, Object> getAddTimeList(Map<String, Object> requestMap);

    @POST
    @Path("queryTimeByContent")
    @Produces("application/json")
    Map<String, Object> queryTimeByContent(Map<String, Object> requestMap);

}
