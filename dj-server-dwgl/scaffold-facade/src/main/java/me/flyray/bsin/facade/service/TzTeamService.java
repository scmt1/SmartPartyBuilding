package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface TzTeamService {

    @ApiOperation("添加团组织")
    @Path("addTeam")
    @Produces("application/json")
    Map<String, Object> addTeam(Map<String, Object> requestMap);

    @ApiOperation("根据吧部门id查询团组织")
    @Path("queryTeamByDeptId")
    @Produces("application/json")
    Map<String, Object> queryTeamByDeptId(Map<String, Object> requestMap);

}
