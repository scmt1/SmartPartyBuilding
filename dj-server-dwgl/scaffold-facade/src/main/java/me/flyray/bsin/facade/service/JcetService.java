package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("JcxfAndDj")
public interface JcetService {

    @ApiOperation("酒城e通登录")
    @POST
    @Path("jcetLogin")
    @Produces("application/json")
    Map<String, Object> jcetLogin(Map<String, Object> requestMap);

}
