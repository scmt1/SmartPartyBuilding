package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiModelProperty;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * @author ：hxh
 * @date ：Created in 2021/11/30 16:19
 * @description：
 * @modified By：
 */

@Path("payFeeDetail")
public interface JcxfPayFeeDetailService {

    @ApiModelProperty("获取缴费列表")
    @POST
    @Path("queryTzPayFeeDetailList")
    @Produces("application/json")
    Map<String, Object> queryTzPayFeeDetailList(Map<String, Object> requestMap);

    @ApiModelProperty("所有下级部门缴费列表")
    @POST
    @Path("queryAllTzPayFeeDetailList")
    @Produces("application/json")
    Map<String, Object> queryAllTzPayFeeDetailList(Map<String, Object> requestMap);

    @ApiModelProperty("校验即将上传的党员缴费信息")
    @POST
    @Path("importPayFeeVerify")
    @Produces("application/json")
    Map<String, Object> importPayFeeVerify(Map<String, Object> requestMap);

    @ApiModelProperty("导入党员缴费信息")
    @POST
    @Path("importPayFee")
    @Produces("application/json")
    Map<String, Object> importPayFee(Map<String, Object> requestMap) throws Exception;

    @ApiModelProperty("党员获取缴费记录")
    @POST
    @Path("getPayFeeDetailPageByPartyMember")
    @Produces("application/json")
    Map<String, Object> getPayFeeDetailPageByPartyMember(Map<String, Object> requestMap);

    @ApiModelProperty("党员获取缴费记录列表")
    @POST
    @Path("getPayFeeDetailListByPartyMember")
    @Produces("application/json")
    Map<String, Object> getPayFeeDetailListByPartyMember(Map<String, Object> requestMap);

    @ApiModelProperty("获取部门的待缴费列表")
    @POST
    @Path("getPayFeeDetailListByDept")
    @Produces("application/json")
    Map<String, Object> getPayFeeDetailListByDept(Map<String, Object> requestMap);


    @ApiModelProperty("党费缴纳支付")
    @POST
    @Path("payByOneself")
    @Produces("application/json")
    Map<String, Object> payByOneself(Map<String, Object> requestMap) throws Exception;

    @ApiModelProperty("党费代缴")
    @POST
    @Path("payForOther")
    @Produces("application/json")
    Map<String, Object> payForOther(Map<String, Object> requestMap) throws Exception;

    /*@POST
    @Path("updatePayStatus")
    @Produces("application/json")
    Map<String, Object> updatePayStatus(Map<String, Object> requestMap) throws UnknownHostException;*/

    @ApiModelProperty("支付回调")
    @Path("payCallBack")
    @Produces("application/json")
    Map<String, Object> payCallBack(Map<String, Object> requestMap);

    @ApiModelProperty("获取缴费信息")
    @Path("getPayFeeDetailById")
    @Produces("application/json")
    Map<String, Object> getPayFeeDetailById(Map<String, Object> requestMap);

    @ApiModelProperty("修改缴费信息")
    @Path("updatePayFeeDetail")
    @Produces("application/json")
    Map<String, Object> updatePayFeeDetail(Map<String, Object> requestMap);

    @ApiModelProperty("导出缴费列表")
    @POST
    @Path("downloadTzPayFeeDetailList")
    @Produces("application/json")
    Map<String, Object> downloadTzPayFeeDetailList(Map<String, Object> requestMap);


    @ApiModelProperty("获取部门下党员缴费列表-标准配置")
    @POST
    @Path("getPayFeeDetailPersonList")
    @Produces("application/json")
    Map<String, Object> getPayFeeDetailPersonList(Map<String, Object> requestMap);


    @ApiModelProperty("获取部门下党员缴费列表-标准配置")
    @POST
    @Path("exportPayFeeDetailPersonList")
    @Produces("application/json")
    Map<String, Object> exportPayFeeDetailPersonList(Map<String, Object> requestMap);




    @ApiModelProperty("根据党员类型和基数，计算应缴党费信息")
    @POST
    @Path("getShouldPayData")
    @Produces("application/json")
    Map<String, Object> getShouldPayData(Map<String, Object> requestMap);


    @ApiModelProperty("修改党员的党费基数和党费信息")
    @POST
    @Path("updatePayFeeDetailByBatchMonth")
    @Produces("application/json")
    Map<String, Object> updatePayFeeDetailByBatchMonth(Map<String, Object> requestMap);


    @ApiModelProperty("查询本年月党员的党费标准")
    @POST
    @Path("queryPayFeeDetailByPerson")
    @Produces("application/json")
    Map<String, Object> queryPayFeeDetailByPerson(Map<String, Object> requestMap);


    @ApiModelProperty("把未加入党费标准的党员加进去")
    @Path("addPartMemberToPayFee")
    @Produces("application/json")
    Map<String, Object> addPartMemberToPayFee(Map<String, Object> requestMap);


    @ApiModelProperty("查询党员可补交的数据")
    @Path("queryPartMemberRePayData")
    @Produces("application/json")
    Map<String, Object> queryPartMemberRePayData(Map<String, Object> requestMap);


    @ApiModelProperty("修改党员补交数据")
    @Path("updateRePayFeeDetail")
    @Produces("application/json")
    Map<String, Object> updateRePayFeeDetail(Map<String, Object> requestMap);


    @ApiModelProperty("查询党员可删除的数据")
    @Path("queryPartMemberDeleteData")
    @Produces("application/json")
    Map<String, Object> queryPartMemberDeleteData(Map<String, Object> requestMap);


    @ApiModelProperty("批量导入党费标准")
    @Path("importPayFeeStandard")
    @Produces("application/json")
    Map<String, Object> importPayFeeStandard(Map<String, Object> requestMap);


    @ApiModelProperty("查询支部党员数据生成党费标准模板")
    @Path("queryPartyMemberListByDeptId")
    @Produces("application/json")
    Map<String, Object> queryPartyMemberListByDeptId(Map<String, Object> requestMap);

    @ApiModelProperty("获取支部党员党费缴纳统计")
    @Path("getPayFeeDetailStatistics")
    @Produces("application/json")
    Map<String, Object> getPayFeeDetailStatistics(Map<String, Object> requestMap);

    @ApiModelProperty("获取党费缴纳统计")
    @Path("getPayFeeStatistics")
    @Produces("application/json")
    Map<String, Object> getPayFeeStatistics(Map<String, Object> requestMap);

    @ApiModelProperty("获取未交党费人员")
    @POST
    @Path("getNoDonePartyMember")
    @Produces("application/json")
    Map<String, Object> getNoDonePartyMember(Map<String, Object> requestMap);
}
