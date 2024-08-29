package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("jcxfPartyMember")
public interface JcxfPartyMemberService {
    @ApiOperation("新增党员数据")
    @POST
    @Path("addPartyMember")
    @Produces("application/json")
    public Map<String, Object> addPartyMember(Map<String, Object> requestMap);

    @ApiOperation("删除党员数据")
    @POST
    @Path("deletePartyMemberInfo")
    @Produces("application/json")
    public Map<String, Object> deletePartyMemberInfo(Map<String, Object> requestMap);

    @ApiOperation("分页查询党员数据")
    @POST
    @Path("queryPartyMemberList")
    @Produces("application/json")
    public Map<String, Object> queryPartyMemberList(Map<String, Object> requestMap);

    @ApiOperation("分页查询党员数据")
    @POST
    @Path("getPartyMemberList")
    @Produces("application/json")
    public Map<String, Object> getPartyMemberList(Map<String, Object> requestMap);

    @ApiOperation("根据组织id查询激活党员数量")
    @POST
    @Path("queryCountActive")
    @Produces("application/json")
    public Map<String, Object> queryCountActive(Map<String, Object> requestMap);


    @ApiOperation("查询单个党员信息")
    @POST
    @Path("queryOneselfPartyMemberList")
    @Produces("application/json")
    public Map<String, Object> queryOneselfPartyMemberList(Map<String, Object> requestMap);

    @ApiOperation("统计性别、是否失联、贫困、流动党员数据")
    @POST
    @Path("getStatisticsInfo")
    @Produces("application/json")
    public Map<String, Object> getStatisticsInfo(Map<String, Object> requestMap);

    @ApiOperation("queryOneselfFlowPartyMemberList")
    @POST
    @Path("queryOneselfFlowPartyMemberList")
    @Produces("application/json")
    public Map<String, Object> queryOneselfFlowPartyMemberList(Map<String, Object> requestMap);


    @ApiOperation("resetPassword")
    @POST
    @Path("resetPassword")
    @Produces("application/json")
    public Map<String, Object> resetPassword(Map<String, Object> requestMap);

    @ApiOperation("重置选中部门的所有下级用户密码")
    @POST
    @Path("resetDeptTreePartyMemberPassword")
    @Produces("application/json")
    public Map<String, Object> resetDeptTreePartyMemberPassword(Map<String, Object> requestMap);

    @ApiOperation("queryDeletePartyMemberList")
    @POST
    @Path("queryDeletePartyMemberList")
    @Produces("application/json")
    public Map<String, Object> queryDeletePartyMemberList(Map<String, Object> requestMap);

    @ApiOperation("recoverDeletePartyMemberInfo")
    @POST
    @Path("recoverDeletePartyMemberInfo")
    @Produces("application/json")
    public Map<String, Object> recoverDeletePartyMemberInfo(Map<String, Object> requestMap);

    @ApiOperation("getPartyInfo")
    @POST
    @Path("getPartyInfo")
    @Produces("application/json")
    public Map<String, Object> getPartyInfo(Map<String, Object> requestMap);

    @ApiOperation("getPartyInfoByApp")
    @POST
    @Path("getPartyInfoByApp")
    @Produces("application/json")
    public Map<String, Object> getPartyInfoByApp(Map<String, Object> requestMap);

    @ApiOperation("uploadImages")
    @POST
    @Path("uploadImages")
    @Produces("application/json")
    public Map<String, Object> uploadImages(Map<String, Object> requestMap);
    @ApiOperation("BOSS")
    @Path("queryBoss")
    @Produces("application/json")
    Map<String, Object> queryBoss(Map<String, Object> requestMap);

    @ApiOperation("根据身份证查询党员列表")
    @Path("getPartMemberListByIdCard")
    @Produces("application/json")
    Map<String, Object> getPartMemberListByIdCard(Map<String, Object> requestMap);

    @ApiOperation("查询该部门下所有的党员")
    @Path("getPartMemberListByDeptId")
    @Produces("application/json")
    Map<String, Object> getPartMemberListByDeptId(Map<String, Object> requestMap);

    @ApiOperation("查询该部门及下级部门所有的党员")
    @Path("getPartMemberListByDeptIds")
    @Produces("application/json")
    Map<String, Object> getPartMemberListByDeptIds(Map<String, Object> requestMap);

    @ApiOperation("党员登录")
    @Path("partyMemberLogin")
    @Produces("application/json")
    Map<String, Object> partyMemberLogin(Map<String, Object> requestMap);

    @ApiOperation("党员验证码登录")
    @Path("partyMemberLoginByCode")
    @Produces("application/json")
    Map<String, Object> partyMemberLoginByCode(Map<String, Object> requestMap);

    @ApiOperation("手机端党员修改自己的头像")
    @Path("updatePartyMemberAvatar")
    @Produces("application/json")
    Map<String, Object> updatePartyMemberAvatar(Map<String, Object> requestMap);

    @ApiOperation("党员密码验证")
    @Path("partyMemberPasswordVerify")
    @Produces("application/json")
    Map<String, Object> partyMemberPasswordVerify(Map<String, Object> requestMap);

    @ApiOperation("党员密码修改")
    @Path("partyMemberPasswordUpdate")
    @Produces("application/json")
    Map<String, Object> partyMemberPasswordUpdate(Map<String, Object> requestMap);

    @ApiOperation("查询今日政治生日的党员")
    @Path("getPoliticalBirthdayByDeptId")
    @Produces("application/json")
    Map<String, Object> getPoliticalBirthdayByDeptId(Map<String, Object> requestMap);

    @ApiOperation("获取当前部门的党员部分信息")
    @Path("getPayFeePartyMemberByDeptId")
    @Produces("application/json")
    Map<String, Object> getPayFeePartyMemberByDeptId(Map<String, Object> requestMap);

	 @ApiOperation("民族分布")
    @POST
    @Path("nationDistribution")
    @Produces("application/json")
    Map<String, Object> nationDistribution(Map<String, Object> requestMap);

    @ApiOperation("学历分布")
    @POST
    @Path("educationDistribution")
    @Produces("application/json")
    Map<String, Object> educationDistribution(Map<String, Object> requestMap);


    @ApiOperation("验证新手机号是否已经被绑定")
    @POST
    @Path("validNewPhone")
    Map<String, Object> validNewPhone(Map<String, Object> requestMap);

    @ApiOperation("无需验证修改手机号")
    @POST
    @Path("changePhone")
    Map<String, Object> changePhone(Map<String, Object> requestMap);

    @ApiOperation("更改手机号")
    @POST
    @Path("changeNewPhone")
    Map<String, Object> changeNewPhone(Map<String, Object> requestMap);

    @ApiOperation("获取党员数量统计相关数据")
    @POST
    @Path("getPartyMemberCount")
    Map<String, Object> getPartyMemberCounts(Map<String, Object> requestMap);

    @ApiOperation("获取党员流出和流入数量")
    @POST
    @Path("getPartyMemberFlowCount")
    Map<String, Object> getPartyMemberFlowCount(Map<String, Object> requestMap);

    @ApiOperation("获取部门及其下级的人员数量情况")
    @POST
    @Path("getPartyMemberCountByDeptIdsAndRetire")
    Map<String, Object> getPartyMemberCountByDeptIdsAndVeteran(Map<String, Object> requestMap);

    @ApiOperation("获取第二层级的所有流入流出党员数量")
    @POST
    @Path("getPartyMemberCountFlowAndInFlow")
    Map<String, Object> getPartyMemberCountFlowAndInFlow(Map<String, Object> requestMap);

    @ApiOperation("党员详情数量统计")
    @POST
    @Path("getPartyMemberCountDetail")
    Map<String, Object> getPartyMemberCountDetail(Map<String, Object> requestMap);

    @ApiOperation("赋予手机看板权限")
    @POST
    @Path("setRole")
    Map<String, Object> setRole(Map<String, Object> requestMap);

    @ApiOperation("加密身份证和手机号")
    @POST
    @Path("getPartyMemberNoEncrypt")
    Map<String, Object> getPartyMemberNoEncrypt(Map<String, Object> requestMap);

    @ApiOperation("更新党员登录密码")
    @POST
    @Path("updatePartyMemberPassWord")
    Map<String, Object> updatePartyMemberPassWord(Map<String, Object> requestMap);

    @ApiOperation("获取当前登录账号是否有管理权限")
    @POST
    @Path("getPartyMemberManagepower")
    @Produces("application/json")
    Map<String, Object> getPartyMemberManagepower(Map<String, Object> requestMap);


    @ApiOperation("党员微信登录")
    @Path("wxLogin")
    @Produces("application/json")
    Map<String, Object> wxLogin(Map<String, Object> requestMap);

    @ApiOperation("党员微信手机号绑定")
    @Path("wxBindPhone")
    @Produces("application/json")
    Map<String, Object> wxBindPhone(Map<String, Object> requestMap);
}
