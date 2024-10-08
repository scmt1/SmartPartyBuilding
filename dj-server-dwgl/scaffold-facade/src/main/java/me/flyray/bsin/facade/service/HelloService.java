package me.flyray.bsin.facade.service;

import me.flyray.bsin.facade.response.HelloResp;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.Map;

/**
 * @author ：bolei
 * @date ：Created in 2021/11/30 16:19
 * @description：hello world
 * @modified By：
 */

@Path("hello")
public interface HelloService {

    /**
     * 新增
     */
    @POST
    @Path("add")
    @Produces("application/json")
    public Map<String, Object> add(Map<String, Object> requestMap) throws ClassNotFoundException;

    /**
     * 查询列表
     */
    @POST
    @Path("getList")
    @Produces("application/json")
    public Map<String, Object> getList(Map<String, Object> requestMap);

    /**
     * 查询分页列表
     */
    @POST
    @Path("getPageList")
    @Produces("application/json")
    public Map<String, Object> getPageList(Map<String, Object> requestMap);

}
