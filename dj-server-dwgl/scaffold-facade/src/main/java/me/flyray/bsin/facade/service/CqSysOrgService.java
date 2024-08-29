package me.flyray.bsin.facade.service;


import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("cqSysOrgService")
public interface CqSysOrgService {

    /**
     * 查询部门
     */
    @POST
    @Path("getTzSysOrgList")
    @Produces("application/json")
    Map<String, Object> getTzSysOrgList(Map<String, Object> requestMap);


    @POST
    @Path("getTzSysOrg")
    @Produces("application/json")
    Map<String, Object> getTzSysOrg(Map<String, Object> requestMap);


    @ApiOperation("根据名称模糊查询列表")
    @POST
    @Path("getOrgListByName")
    @Produces("application/json")
    Map<String, Object> getOrgListByName(Map<String, Object> requestMap);
}
