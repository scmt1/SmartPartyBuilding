package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Path;
import java.util.Map;

public interface TzMessageTemplateService {


    @ApiOperation("新增消息管理数据")
    @Path("addMessageTemplate")
    Map<String, Object> addMessageTemplate(Map<String, Object> requestMap);


}
