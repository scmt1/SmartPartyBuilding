package com.yami.shop.platform.dj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yami.shop.bean.model.AttachFile;

import com.yami.shop.bean.model.dj.*;
import com.yami.shop.common.response.ServerResponseEntity;

import com.yami.shop.bean.vo.dj.PageVo;
import com.yami.shop.bean.vo.dj.SearchVo;
import com.yami.shop.dj.service.*;
import com.yami.shop.service.AttachFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author
 **/
@RequiredArgsConstructor
@RestController
@Api(tags = " 部门数据接口")
@RequestMapping("/scmt/tzSysDept")
public class TzSysDeptController {
    @Autowired
    private ITzSysDeptService tzSysDeptService;
    @Autowired
    private IPartyMemberService partyMemberService;
    @Autowired
    private IDevelopPartyService developPartyService;

    @Autowired
    private ITzTermDeptService tzTermDeptService;

    @Autowired
    private AttachFileService attachFileService;
    @Autowired
    private ITzActiveDeptService activeDeptService;

  /*  @Autowired
    private SecurityUtil securityUtil;*/

    /**
     * 功能描述：新增党组织数据
     *
     * @param tzSysDept 实体
     * @return 返回新增结果
     */
    @ApiOperation("新增党组织数据")
    @PostMapping("addTzSysDept")
    public ServerResponseEntity<Object> addTzSysDept(@RequestBody TzSysDept tzSysDept) {
        String parentIds;
        try {
            if (tzSysDept.getId() != null) {
                tzSysDept.setUpdateDate(new Date());
                boolean res = tzSysDeptService.updateById(tzSysDept);
                if (res) {
                    QueryWrapper<AttachFile> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("tz_attach_file.foreign_key", tzSysDept.getId());
                    queryWrapper.eq("tz_attach_file.table_type", "tz_sys_dept");
                    List<AttachFile> list = attachFileService.list(queryWrapper);
                    if (list != null && list.size() > 0) {
                        List<String> fileId = new ArrayList<>();
                        for (AttachFile file : list) {
                            fileId.add(file.getFileId().toString());
                        }
                        boolean b = attachFileService.removeByIds(fileId);
                    }
                    List<AttachFile> attachmentList = tzSysDept.getAttachFileList();
                    if (attachmentList != null && attachmentList.size() > 0) {
                        for (AttachFile file : tzSysDept.getAttachFileList()) {
                            file.setUploadTime(new Date());
                        }
                        boolean b = attachFileService.saveBatch(attachmentList);
                        if (!b) {
                            return ServerResponseEntity.showFailMsg("保存失败");
                        }
                    }
                    return ServerResponseEntity.success(res);
                } else {
                    return ServerResponseEntity.showFailMsg("修改失败");
                }
            }
            if (tzSysDept.getParentId() == null) {
                return ServerResponseEntity.showFailMsg("无父级组织，请联系管理员!!");
            } else {
                Long parentId = tzSysDept.getParentId();
                TzSysDept byId = tzSysDeptService.getById(parentId);
                String byIdParentIds = byId.getParentIds();
                parentIds = byIdParentIds + "," + parentId.toString();
                tzSysDept.setParentIds(parentIds);
            }
            tzSysDept.setDelFlag(0);
            //tzSysDept.setCreateId(securityUtil.getCurrUser().getId());
            tzSysDept.setCreateTime(new Date());
            boolean res = tzSysDeptService.save(tzSysDept);
            if (res) {
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("保存异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：更新数据
     *
     * @param tzSysDept 实体
     * @return 返回更新结果
     */
    @ApiOperation("更新党组织数据")
    @PostMapping("updateTzSysDept")
    public ServerResponseEntity<Object> updateTzSysDept(@RequestBody TzSysDept tzSysDept) {
        if (StringUtils.isBlank(tzSysDept.getId().toString())) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            //tzSysDept.setUpdateId(securityUtil.getCurrUser().getId());
            tzSysDept.setUpdateDate(new Date());
            boolean res = tzSysDeptService.updateById(tzSysDept);
            if (res) {
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("修改异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据主键来删除数据
     *
     * @param ids 主键集合
     * @return 返回删除结果
     */
    @ApiOperation("根据主键来删除党组织数据")
    @PostMapping("deleteTzSysDept")
    public ServerResponseEntity<Object> deleteTzSysDept(@RequestBody String[] ids) {
        if (ids == null || ids.length == 0) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        QueryWrapper<TzSysDept> queryWrapper = new QueryWrapper<>();
        for (String id : ids) {
            queryWrapper.eq("tz_sys_dept.parent_id", id);
            List<TzSysDept> list = tzSysDeptService.list(queryWrapper);
            int size = list.size();
            if (size > 0) {
                return ServerResponseEntity.showFailMsg("该组织已有下属组织，暂无法删除!!");
            }
        }
        try {

            UpdateWrapper<TzSysDept> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("tz_sys_dept.del_flag", 1).in("tz_sys_dept.id", ids);
            boolean update = tzSysDeptService.update(updateWrapper);
            //boolean res = tzSysDeptService.removeByIds(Arrays.asList(ids));
            if (update) {
                return ServerResponseEntity.success(update);
            } else {
                return ServerResponseEntity.showFailMsg("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("删除异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据主键来获取数据
     *
     * @param id 主键
     * @return 返回获取结果
     */
    @ApiOperation("根据主键来获取数据")
    @GetMapping("getTzSysDept")
    public ServerResponseEntity<Object> getTzSysDept(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            TzSysDept res = tzSysDeptService.getById(id);
            if (res != null) {
                QueryWrapper<AttachFile> wrapper = new QueryWrapper<>();
                wrapper.eq("tz_attach_file.foreign_key", id);
                wrapper.eq("tz_attach_file.table_type", "tz_sys_dept");
                List<AttachFile> attachFiles = attachFileService.list(wrapper);
                if (attachFiles != null && attachFiles.size() > 0) {
                    res.setAttachFileList(attachFiles);
                }
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }


    /**
     * 功能描述：根据主键来组织名字列表
     *
     * @param
     * @return 返回获取结果
     */
    @ApiOperation("根据组织主键来获取组织名字列表")
    @GetMapping("getTzSysDeptNameList")
    public ServerResponseEntity<Object> getTzSysDeptNameList(String id) {
        try {
            List<TzSysDept> res = tzSysDeptService.getNameList(id, id);
            //PartyResponseEntity partyResponseEntity = JSON.parseObject(res, PartyResponseEntity.class);
            if (res != null) {
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据主键来组织名字列表
     *
     * @param
     * @return 返回获取结果
     */
    @ApiOperation("根据主键来组织名字列表")
    @GetMapping("getBaseInfoById")
    public ServerResponseEntity<Object> getBaseInfoById(String id) {
        try {
            List<TzSysDept> res = tzSysDeptService.getBaseInfoById(id);
            if (res != null) {
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：实现分页查询
     *
     * @param searchVo 需要模糊查询的信息
     * @param pageVo   分页参数
     * @return 返回获取结果
     */
    @ApiOperation("分页查询党组织数据")
    @GetMapping("queryTzSysDeptList")
    public ServerResponseEntity<Object> queryTzSysDeptList(TzSysDept tzSysDept, SearchVo searchVo, PageVo pageVo) {
        try {
            IPage<TzSysDept> result = tzSysDeptService.queryTzSysDeptListByPage(tzSysDept, searchVo, pageVo);
            List<TzSysDept> records = result.getRecords();
            if(records!=null) {
                for (TzSysDept record : records) {
                    //换届时间查询
                    QueryWrapper<TzTermDept> termDeptQueryWrapper = new QueryWrapper<>();
                    termDeptQueryWrapper.eq("tz_term_dept.del_flag", 0);
                    termDeptQueryWrapper.eq("tz_term_dept.dept_id", record.getId());
                    termDeptQueryWrapper.and(qw -> {
                        qw.or(i2 -> i2.inSql("leader_time", "select max(`leader_time`) from  tz_term_dept where tz_term_dept.dept_id = " + record.getId()));
                    });
                    TzTermDept one1 = tzTermDeptService.getOne(termDeptQueryWrapper);
                    if (one1 != null) {
                        record.setThisChangeTime(one1.getThisChangeTime());
                        record.setThisFinishTime(one1.getThisFinishTime());
                    } else {
                        record.setThisChangeTime(null);
                        record.setThisFinishTime(null);
                    }
                }
            }

            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：导出数据
     *
     * @param response  请求参数
     * @param tzSysDept 查询参数
     * @return
     */
    @ApiOperation("导出党组织数据")
    @PostMapping("/download")
    public void download(HttpServletResponse response, TzSysDept tzSysDept) {
        try {
            tzSysDeptService.download(tzSysDept, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("导出党组织数据")
    @GetMapping("/exportProvince")
    public void exportProvince(HttpServletResponse response, TzSysDept sysDept, SearchVo searchVo) throws IOException {
        try {
                tzSysDeptService.exportProvince(response, sysDept, searchVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @ApiOperation("导出党组织数据")
    @PostMapping("/exportProvinceByIds")
    public void exportProvinceByIds(HttpServletResponse response, @RequestBody String [] ids) throws IOException {
        try {
                tzSysDeptService.exportProvinceByIds(response, ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 功能描述：根据主键来获取数据
     *
     * @param id 主键
     * @return 返回获取结果
     */
    @ApiOperation("根据主键获取组织简介信息")
    @GetMapping("getDeptIntroduceById")
    public ServerResponseEntity<Object> getDeptIntroduceById(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        Map<String, Object> map = new HashMap<>();

        try {
            // 查询支部简介
            TzSysDept sysDept = tzSysDeptService.getById(id);
            String deptIntroduction = sysDept.getDeptIntroduction();
            map.put("deptIntroduction", deptIntroduction);
            // 查询阵地图片
            QueryWrapper<AttachFile> wrapper = new QueryWrapper<>();
            wrapper.eq("tz_attach_file.foreign_key", id);
            wrapper.eq("tz_attach_file.table_type", "tz_sys_dept");
            List<AttachFile> attachFiles = attachFileService.list(wrapper);

            map.put("deptAttachFiles", attachFiles);

            // 本部所有党员信息查询
            QueryWrapper<PartyMember> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("party_member.dept_id", id);
            queryWrapper.eq("party_member.del_flag", 0);
            List<PartyMember> list = partyMemberService.list(queryWrapper);
            if (list != null && list.size() > 0) {
                //党员头像查询
                for (PartyMember partyMember : list) {
                    QueryWrapper<AttachFile> attachFileQueryWrapper = new QueryWrapper<>();
                    attachFileQueryWrapper.eq("tz_attach_file.foreign_key", partyMember.getId());
                    List<AttachFile> attachFileList = attachFileService.list(attachFileQueryWrapper);
                    if (attachFileList != null && attachFileList.size() > 0) {
                        partyMember.setAttachFiles(attachFileList);
                    }
                }
            }
            map.put("partyInfoList", list);


            //预备党员人数查询
            QueryWrapper<PartyMember> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("party_member.dept_id", id);
            queryWrapper2.eq("party_member.del_flag", 0);
            queryWrapper2.eq("party_member.person_type", 0);

            int preCount = partyMemberService.count(queryWrapper2);
            map.put("preCount", preCount);
            // 入党积极分子人数查询
            QueryWrapper<DevelopParty> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("develop_party.dept_id", id);
            queryWrapper3.eq("develop_party.del_flag", 0);
            queryWrapper3.eq("develop_party.develop_state", "2");

            int activeCount = developPartyService.count(queryWrapper3);
            map.put("activeCount", activeCount);

            //换届时间查询
            QueryWrapper<TzTermDept> termDeptQueryWrapper = new QueryWrapper<>();
            termDeptQueryWrapper.eq("tz_term_dept.del_flag",0);
            termDeptQueryWrapper.eq("tz_term_dept.dept_id",id);
            termDeptQueryWrapper.and(qw -> {
                      qw.or(i2 -> i2.inSql("leader_time", "select max(`leader_time`) from  tz_term_dept where tz_term_dept.dept_id = " + id));
                  });
            TzTermDept one1 = tzTermDeptService.getOne(termDeptQueryWrapper);
            if(one1!=null){
                map.put("thisChangeTime",one1.getThisChangeTime());
                map.put("thisFinishTime",one1.getThisFinishTime());
            }else {
                map.put("thisChangeTime",null);
                map.put("thisFinishTime",null);
            }
            // 活动阵地信息查询
            QueryWrapper<TzActiveDept> queryWrapper4 = new QueryWrapper<>();
            queryWrapper4.eq("tz_active_dept.dept_id", id);
            queryWrapper4.eq("tz_active_dept.del_flag", 0);

            TzActiveDept one = activeDeptService.getOne(queryWrapper4);
            map.put("activeDept", one);

            //活动阵地照片查询
            if (one != null) {
                QueryWrapper<AttachFile> wrapper5 = null;
                wrapper5 = new QueryWrapper<AttachFile>();
                wrapper5.and(i -> i.eq("tz_attach_file.foreign_key", one.getId()));
                wrapper5.and(i -> i.eq("tz_attach_file.table_type", "tz_active_dept"));
                List<AttachFile> activeAttachFiles = attachFileService.list(wrapper5);
                map.put("activeAttachFiles", activeAttachFiles);
            } else {
                map.put("activeAttachFiles", null);
            }

            //党内表彰查询
            TzSysDept byId = tzSysDeptService.getById(id);
            map.put("deptInfo", byId);
            return ServerResponseEntity.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }


    /**
     * 功能描述：新增阵地介绍数据
     *
     * @param activeDept 实体
     * @return 返回新增结果
     */
    @ApiOperation("新增阵地介绍数据")
    @PostMapping("addActiveInfo")
    public ServerResponseEntity<Object> addActiveInfo(@RequestBody TzActiveDept activeDept) {
        try {
            if (activeDept.getId() != null) {
                activeDept.setUpdateTime(new Date());
                boolean b1 = activeDeptService.updateById(activeDept);
                if (b1) {
                    QueryWrapper<AttachFile> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("tz_attach_file.foreign_key", activeDept.getId());
                    queryWrapper.eq("tz_attach_file.table_type", "tz_active_dept");
                    List<AttachFile> list = attachFileService.list(queryWrapper);
                    if (list != null && list.size() > 0) {
                        List<String> fileId = new ArrayList<>();
                        for (AttachFile file : list) {
                            fileId.add(file.getFileId().toString());
                        }
                        boolean b = attachFileService.removeByIds(fileId);
                    }
                    List<AttachFile> attachmentList = activeDept.getAttachFileList();
                    if (attachmentList != null && attachmentList.size() > 0) {
                        for (AttachFile file : activeDept.getAttachFileList()) {
                            file.setUploadTime(new Date());
                        }
                        boolean b = attachFileService.saveBatch(attachmentList);
                        if (!b) {
                            return ServerResponseEntity.showFailMsg("保存失败");
                        }
                    }
                    return ServerResponseEntity.success(b1);
                } else {
                    return ServerResponseEntity.showFailMsg("修改失败");
                }

            }

            activeDept.setDelFlag(0);
            //tzSysDept.setCreateId(securityUtil.getCurrUser().getId());
            activeDept.setCreateTime(new Date());
            boolean res = activeDeptService.save(activeDept);
            if (res) {
                List<AttachFile> attachmentList = activeDept.getAttachFileList();
                if (attachmentList != null && attachmentList.size() > 0) {
                    for (AttachFile file : activeDept.getAttachFileList()) {
                        file.setUploadTime(new Date());
                        file.setForeignKey(activeDept.getId().toString());
                    }
                    boolean b = attachFileService.saveBatch(attachmentList);
                    if (!b) {
                        return ServerResponseEntity.showFailMsg("保存失败");
                    }
                }
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("保存异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据主键来获取数据
     *
     * @param id 主键
     * @return 返回获取结果
     */
    @ApiOperation("根据主键来获取活动阵地数据")
    @GetMapping("getActiveDept")
    public ServerResponseEntity<Object> getActiveDept(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            TzActiveDept res = activeDeptService.getById(id);
            if (res != null) {
                QueryWrapper<AttachFile> wrapper = new QueryWrapper<>();
                wrapper.eq("tz_attach_file.foreign_key", id);
                wrapper.eq("tz_attach_file.table_type", "tz_active_dept");
                List<AttachFile> attachFiles = attachFileService.list(wrapper);
                if (attachFiles != null && attachFiles.size() > 0) {
                    res.setAttachFileList(attachFiles);
                }
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

}
