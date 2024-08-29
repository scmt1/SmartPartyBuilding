package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

public interface TzUnitedPersonService {

    @ApiOperation("添加统战对象")
    @Path("addUnitedPerson")
    @Produces("application/json")
    Map<String, Object> addUnitedPerson(Map<String, Object> requestMap);


    @ApiOperation("分页查询统战对象")
    @Path("queryUnitedPersonList")
    @Produces("application/json")
    Map<String, Object> queryUnitedPersonList(Map<String, Object> requestMap);

    @ApiOperation("根据id查询统战对象")
    @Path("getUnitePerson")
    @Produces("application/json")
    Map<String, Object> getUnitePerson(Map<String, Object> requestMap);


    @ApiOperation("删除统战人员对象")
    @Path("deleteUnitePerson")
    @Produces("application/json")
    Map<String, Object> deleteUnitePerson(Map<String, Object> requestMap);

    @ApiOperation("批量添加统战人员")
    @Path("addUnitedPersonList")
    @Produces("application/json")
    Map<String, Object> addUnitedPersonList(Map<String, Object> requestMap);

    @ApiOperation("根据人员类型查询数量")
    @Path("getPersonNumberByUnitedObject")
    @Produces("application/json")
    Map<String, Object> getPersonNumberByUnitedObject(Map<String, Object> requestMap);

}
