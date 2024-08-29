package me.flyray.bsin.facade.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface TzTwoBestOneFirstService {

    @POST
    @Path("queryTzTwoBestOneFirstPage")
    @Produces("application/json")
    Map<String, Object> queryTzTwoBestOneFirstPage(Map<String, Object> requestMap);

    @POST
    @Path("getTzTwoBestOneFirstById")
    @Produces("application/json")
    Map<String, Object> getTzTwoBestOneFirstById(Map<String, Object> requestMap);

    @POST
    @Path("updateTzTwoBestOneFirstById")
    @Produces("application/json")
    Map<String, Object> updateTzTwoBestOneFirstById(Map<String, Object> requestMap);


    @POST
    @Path("deleteTzTwoBestOneFirstById")
    @Produces("application/json")
    Map<String, Object> deleteTzTwoBestOneFirstById(Map<String, Object> requestMap);

    @POST
    @Path("auditTzTwoBestOneFirstById")
    @Produces("application/json")
    Map<String, Object> auditTzTwoBestOneFirstById(Map<String, Object> requestMap);

    @POST
    @Path("addTzTwoBestOneFirst")
    @Produces("application/json")
    Map<String, Object> addTzTwoBestOneFirst(Map<String, Object> requestMap);

    @POST
    @Path("getTzTwoBestOneFirstListByPartyMemberId")
    @Produces("application/json")
    Map<String, Object> getTzTwoBestOneFirstListByPartyMemberId(Map<String, Object> requestMap);

}
