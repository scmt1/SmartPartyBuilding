package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface JcxfSysDictionaryService {

    @ApiOperation("")
    @Path("getByCodeAndDetailName")
    @Produces("application/json")
    Map<String, Object> getByCodeAndDetailName(Map<String, Object> requestMap);

    @ApiOperation("")
    @Path("getDictByCode")
    @Produces("application/json")
    Map<String, Object> getDictByCode(Map<String, Object> requestMap);

}
