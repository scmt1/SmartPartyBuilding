package me.flyray.bsin.facade.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("sysTerm")
public interface JcxfTermDeptService {

    @POST
    @Path("deleteTzTermDept")
    @Produces("application/json")
    Map<String, Object> deleteTzTermDept(Map<String, Object> requestMap);

    @POST
    @Path("queryTzTermDeptList")
    @Produces("application/json")
    Map<String, Object> queryTzTermDeptList(Map<String, Object> requestMap);

    @POST
    @Path("queryTzTermDeptByDeptPage")
    @Produces("application/json")
    Map<String, Object> queryTzTermDeptByDeptPage(Map<String, Object> requestMap);

    @POST
    @Path("addTzTermDept")
    @Produces("application/json")
    Map<String, Object> addTzTermDept(Map<String, Object> requestMap);

    @POST
    @Path("getTzTermDept")
    @Produces("application/json")
    Map<String, Object> getTzTermDept(Map<String, Object> requestMap);

    @POST
    @Path("getTzTermStatistics")
    @Produces("application/json")
    Map<String, Object> getTzTermStatistics(Map<String, Object> requestMap);

}
