package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
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

@Path("payFee")
public interface JcxfPayFeeService {

    @POST
    @Path("queryFeeTotalByYear2")
    @Produces("application/json")
    Map<String, Object> queryFeeTotalByYear2(Map<String, Object> requestMap);

    /**
     * 支部缴纳党费统计
     */
    @POST
    @Path("queryFeeTotalByYear")
    @Produces("application/json")
    Map<String, Object> queryFeeTotalByYear(Map<String, Object> requestMap);

    @POST
    @Path("queryTzPayFeeList")
    @Produces("application/json")
    Map<String, Object> queryTzPayFeeList(Map<String, Object> requestMap);

    @POST
    @Path("downloadTzPayFeeList")
    @Produces("application/json")
    Map<String, Object> downloadTzPayFeeList(Map<String, Object> requestMap);

    @POST
    @Path("copyDataByMonth")
    @Produces("application/json")
    Map<String, Object> copyDataByMonth(Map<String, Object> requestMap);

    @ApiOperation(value = "删除支部缴纳党费以及对应的党员记录", notes = "删除支部缴纳党费以及对应的党员记录")
    @POST
    @Path("deleteById")
    @Produces("application/json")
    Map<String, Object> deleteById(Map<String, Object> requestMap);

    @POST
    @Path("getCopyInfoByTime")
    @Produces("application/json")
    Map<String, Object> getCopyInfoByTime(Map<String, Object> requestMap);

    @Path("syncPayResultById")
    @Produces("application/json")
    Map<String, Object> syncPayResultById(Map<String, Object> requestMap);

    @POST
    @Path("getDeptCount")
    @Produces("application/json")
    Map<String, Object> getDeptCount(Map<String, Object> requestMap);
}
