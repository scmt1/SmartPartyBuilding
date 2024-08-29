package me.flyray.bsin.facade.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

public interface JcxfSysAreaService {

    @POST
    @Path("getAreaList")
    @Produces("application/json")
    Map<String, Object> getAreaList(Map<String, Object> requestMap);

    @POST
    @Path("getAreaTree")
    @Produces("application/json")
    Map<String, Object> getAreaTree(Map<String, Object> requestMap);

}
