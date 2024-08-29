package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Path;
import java.util.Map;

public interface TzLoginLogService {

    @ApiOperation("分页查询")
    @Path("queryLoginLogByPage")
    Map<String, Object> queryLoginLogByPage(Map<String, Object> requestMap);

}
