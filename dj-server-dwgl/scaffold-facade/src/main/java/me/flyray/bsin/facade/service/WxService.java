package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("wxService")
public interface WxService {

    @ApiOperation("根据url生成当前页面的jsapi签名")
    @POST
    @Path("createJsapiSignature")
    @Produces("application/json")
    public Map<String, Object> createJsapiSignature(Map<String, Object> requestMap);
}
