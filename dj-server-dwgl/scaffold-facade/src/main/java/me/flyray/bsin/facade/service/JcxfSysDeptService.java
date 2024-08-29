package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

@Path("jcxfSysDept")
public interface JcxfSysDeptService {

    /**
     * 查询部门树
     */
    @POST
    @Path("getTzSysDeptNameList")
    @Produces("application/json")
    Map<String, Object> getTzSysDeptNameList(Map<String, Object> requestMap);

    @POST
    @Path("queryTzSysDeptList")
    @Produces("application/json")
    Map<String, Object> queryTzSysDeptList(Map<String, Object> requestMap);

    @POST
    @Path("queryTzSysDeptLowerLevelList")
    @Produces("application/json")
    Map<String, Object> queryTzSysDeptLowerLevelList(Map<String, Object> requestMap);

    /**
     * 根据部门id查询是否是叶子节点
     * @param requestMap
     * @return
     */
    @POST
    @Path("queryIsLeafById")
    @Produces("application/json")
    Map<String, Object> queryIsLeafById(Map<String, Object> requestMap);

    /**
     * 查询部门
     */
    @POST
    @Path("getTzSysDeptList")
    @Produces("application/json")
    Map<String, Object> getTzSysDeptList(Map<String, Object> requestMap);

    /**
     * 导出党组织
     * @param
     * @param requestMap
     */
    @POST
    @Path("exportProvince")
    @Produces("application/json")
    void exportProvince(Map<String, Object> requestMap);

    @POST
    @Path("deleteTzSysDept")
    @Produces("application/json")
    Map<String, Object> deleteTzSysDept(Map<String, Object> requestMap);

    @POST
    @Path("getTzSysDept")
    @Produces("application/json")
    Map<String, Object> getTzSysDept(Map<String, Object> requestMap);

    @POST
    @Path("getTzSysDeptByParentId")
    @Produces("application/json")
    Map<String, Object> getTzSysDeptByParentId(Map<String, Object> requestMap);

    @POST
    @Path("addTzSysDept")
    @Produces("application/json")
    Map<String, Object> addTzSysDept(Map<String, Object> requestMap);

    @POST
    @Path("getBaseInfoById")
    @Produces("application/json")
    Map<String, Object> getBaseInfoById(Map<String, Object> requestMap);

    /**
     * 根据类型和部门id查询不同党组织的数量
     * @param requestMap
     * @return
     */
    @POST
    @Path("getCountById")
    @Produces("application/json")
    Map<String, Object> getCountById(Map<String, Object> requestMap);

    @POST
    @Path("getDeptIntroduceById")
    @Produces("application/json")
    Map<String, Object> getDeptIntroduceById(Map<String, Object> requestMap);

    @ApiOperation("根据名称模糊查询列表")
    @POST
    @Path("getDeptListByName")
    @Produces("application/json")
    Map<String, Object> getChildrenDeptListByName(Map<String, Object> requestMap);

    @ApiOperation("根据名称模糊查询列表")
    @POST
    @Path("getAllDeptListByName")
    @Produces("application/json")
    Map<String, Object> getAllDeptListByName(Map<String, Object> requestMap);

    @ApiOperation("查询同级组织")
    @POST
    @Path("getSameClassDeptListByDeptId")
    @Produces("application/json")
    Map<String, Object> getSameClassDeptListByDeptId(Map<String, Object> requestMap);

    @ApiOperation("查询党组织数量")
    @POST
    @Path("queryBaseDeptCount")
    @Produces("application/json")
    Map<String, Object> queryBaseDeptCount(Map<String, Object> requestMap);

    @ApiOperation("查询党委相关数量")
    @POST
    @Path("getPartyCommitteeCount")
    @Produces("application/json")
    Map<String, Object> getPartyCommitteeCount(Map<String, Object> requestMap);

    @ApiOperation("获取当前部门所有下级")
    @POST
    @Path("getChildrenIdsById")
    @Produces("application/json")
    Map<String, Object> getChildrenIdsById(Map<String, Object> requestMap);

    @ApiOperation("获取当前部门所有下级信息")
    @POST
    @Path("getChildrenInfoById")
    @Produces("application/json")
    Map<String, Object> getChildrenInfoById(Map<String, Object> requestMap);

    @ApiOperation("修改党支部标签")
    @POST
    @Path("updateDeptTag")
    @Produces("application/json")
    Map<String, Object> updateDeptTag(Map<String, Object> requestMap);

    @ApiOperation("获取部门")
    @POST
    @Path("getSysDeptList")
    @Produces("application/json")
    Map<String, Object> getSysDeptList(Map<String, Object> requestMap);


    @ApiOperation("bsin查询已删除的部门list")
    @POST
    @Path("bsinGetSysDeptDelFlagList")
    @Produces("application/json")
    Map<String, Object> bsinGetSysDeptDelFlagList(Map<String, Object> requestMap);

    @ApiOperation("bsin查询部门")
    @POST
    @Path("bsinGetSysDeptList")
    @Produces("application/json")
    Map<String, Object> bsinGetSysDeptList(Map<String, Object> requestMap);

    @ApiOperation("查询部门树")
    @POST
    @Path("getDeptTree")
    @Produces("application/json")
    Map<String, Object> getDeptTree(Map<String, Object> requestMap);

    @ApiOperation("查询未交齐党费部门树")
    @POST
    @Path("getNoDoneDeptTree")
    @Produces("application/json")
    Map<String, Object> getNoDoneDeptTree(Map<String, Object> requestMap);
}
