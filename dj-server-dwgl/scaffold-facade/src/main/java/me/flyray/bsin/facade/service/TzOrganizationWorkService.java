package me.flyray.bsin.facade.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

/**
 * @author ：hxh
 * @date ：Created in 2021/11/30 16:19
 * @description：
 * @modified By：
 */

@Path("organizationWork")
public interface TzOrganizationWorkService {

    /**
     * 查询部门树
     */
    @POST
    @Path("getByPage")
    @Produces("application/json")
    Map<String, Object> getByPage(Map<String, Object> requestMap);



    /**
     * 查询部门树
     */
    @POST
    @Path("addNews")
    @Produces("application/json")
    Map<String, Object> addNews(Map<String, Object> requestMap);

    /**
     * 根据id查询组工详情数据
     */
    @POST
    @Path("getDataById")
    @Produces("application/json")
    Map<String, Object> getDataById(Map<String, Object> requestMap);
    /**
     * 根据id删除组工详情数据
     */
    @POST
    @Path("getDelById")
    @Produces("application/json")
    Map<String, Object> getDelById(Map<String, Object> requestMap);


}
