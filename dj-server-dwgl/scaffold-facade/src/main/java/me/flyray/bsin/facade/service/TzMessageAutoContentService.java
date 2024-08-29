package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface TzMessageAutoContentService {

    @ApiOperation("自定义模板内容")
    @POST
    @Path("editAutoContent")
    @Produces("application/json")
    Map<String, Object> editAutoContent(Map<String, Object> requestMap);

}
