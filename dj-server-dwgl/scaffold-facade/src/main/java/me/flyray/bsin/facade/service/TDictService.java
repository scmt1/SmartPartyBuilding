package me.flyray.bsin.facade.service;

import javax.ws.rs.Path;
import java.util.Map;

@Path("tDict")
public interface TDictService {
    Map<String, Object> findByType(Map<String, Object> requestMap);
}
