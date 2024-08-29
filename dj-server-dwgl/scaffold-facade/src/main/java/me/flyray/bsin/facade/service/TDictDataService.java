package me.flyray.bsin.facade.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("tDictData")
public interface TDictDataService {

    @POST
    @Path("getByType")
    @Produces("application/json")
    Map<String, Object> getByType(Map<String, Object> requestMap);
}
