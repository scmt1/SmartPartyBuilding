package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("developParty")
public interface JcxfDeptPositionService {

    @ApiOperation("获取阵地信息")
    @Path("getJcxfDeptPositionByDeptId")
    @Produces("application/json")
    Map<String, Object> getJcxfDeptPositionByDeptId(Map<String, Object> requestMap);

    @ApiOperation("新增或修改阵地信息")
    @Path("addJcxfDeptPosition")
    @Produces("application/json")
    Map<String, Object> addOrUpdateJcxfDeptPosition(Map<String, Object> requestMap);

}
