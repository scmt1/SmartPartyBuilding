package com.yami.shop.platform.dj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yami.shop.bean.model.AttachFile;

import com.yami.shop.bean.model.dj.PartyMember;
import com.yami.shop.bean.model.dj.TzOrganizationLife;
import com.yami.shop.bean.model.dj.TzPersonMeeting;
import com.yami.shop.bean.model.dj.TzSysDept;
import com.yami.shop.bean.vo.dj.PageVo;
import com.yami.shop.bean.vo.dj.SearchVo;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.dj.service.IPartyMemberService;
import com.yami.shop.dj.service.ITzOrganizationLifeService;
import com.yami.shop.dj.service.ITzPersonMeetingService;
import com.yami.shop.dj.service.ITzSysDeptService;
import com.yami.shop.service.AttachFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author
 **/
@RestController
@Api(tags = " 支部之窗组织生活接口数据")
@RequestMapping("/platform/tzOrganizationLife")
public class TzOrganizationLifeController {
    @Autowired
    private ITzOrganizationLifeService tzOrganizationLifeService;
    /*	@Autowired
        private SecurityUtil securityUtil;*/
    @Autowired
    private ITzSysDeptService tzSysDeptService;

    @Autowired
    private ITzPersonMeetingService personMeetingService;

    @Autowired
    private IPartyMemberService partyMemberService;

    @Autowired
    private AttachFileService attachFileService;

    /**
     * 功能描述：新增test数据
     *
     * @param tzOrganizationLife 实体
     * @return 返回新增结果
     */
    @ApiOperation("新增会议数据")
    @PostMapping("addTzOrganizationLife")
    public ServerResponseEntity<Object> addTzOrganizationLife(@RequestBody TzOrganizationLife tzOrganizationLife) {
        try {
            if (tzOrganizationLife.getId() != null) {
                QueryWrapper<TzOrganizationLife> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("tz_organization_life.id", tzOrganizationLife.getId());
                tzOrganizationLife.setLastEditTime(new Date());
                boolean b = tzOrganizationLifeService.updateById(tzOrganizationLife);
                if (b) {
                    return ServerResponseEntity.success(b);
                } else {
                    return ServerResponseEntity.showFailMsg("修改失败");
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            tzOrganizationLife.setDelFlag(0);
            tzOrganizationLife.setMeetingStatus("0");
            tzOrganizationLife.setPostTime(new Date());
            //tzOrganizationLife.setCreateId(securityUtil.getCurrUser().getId());
            tzOrganizationLife.setCreateTime(new Date());
            boolean res = tzOrganizationLifeService.save(tzOrganizationLife);
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
     * @param tzOrganizationLife 实体
     * @return 返回更新结果
     */
    @ApiOperation("更新test数据")
    @PostMapping("updateTzOrganizationLife")
    public ServerResponseEntity<Object> updateTzOrganizationLife(@RequestBody TzOrganizationLife tzOrganizationLife) {
        if (StringUtils.isBlank(tzOrganizationLife.getId().toString())) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
		/*	tzOrganizationLife.setUpdateId(securityUtil.getCurrUser().getId());
			tzOrganizationLife.setUpdateTime(new Date());*/
            boolean res = tzOrganizationLifeService.updateById(tzOrganizationLife);
            if (res) {
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("保存异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据主键来删除数据
     *
     * @param ids 主键集合
     * @return 返回删除结果
     */
    @ApiOperation("根据主键来删除test数据")
    @GetMapping("deleteTzOrganizationLife")
    public ServerResponseEntity<Object> deleteTzOrganizationLife(String[] ids) {
        if (ids == null || ids.length == 0) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            UpdateWrapper<TzOrganizationLife> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("tz_organization_life.del_flag", 1).in("tz_organization_life.id", ids);
            boolean res = tzOrganizationLifeService.update(updateWrapper);
            //boolean res = tzOrganizationLifeService.removeByIds(Arrays.asList(ids));
            if (res) {
                return ServerResponseEntity.success(res);
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
    @ApiOperation("根据主键来获取test数据")
    @GetMapping("getTzOrganizationLife")
    public ServerResponseEntity<Object> getTzOrganizationLife(@RequestParam(name = "id") String id, String deptId) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            TzOrganizationLife res = tzOrganizationLifeService.getById(id);
            if (res != null) {
                //会议图片查询
                Integer meetingId = res.getId();
                QueryWrapper<AttachFile> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("tz_attach_file.foreign_key", meetingId);
                queryWrapper.eq("tz_attach_file.table_type", "tz_party_meeting");
                queryWrapper.eq("tz_attach_file.type", 1);
                List<AttachFile> list = attachFileService.list(queryWrapper);
                res.setAttachFile(list);

                //会议应到人数查询
                //根据部门id查出所有党员
                List<String> nameList = new ArrayList<>();
                QueryWrapper<PartyMember> wrapper = new QueryWrapper<>();
                //queryWrapper.and(i -> i.eq("party_member.is_develop", "2").or(i2 ->i2.isNull("party_member.is_develop")));
                wrapper.and(i -> i.eq("party_member.dept_id", deptId));
                wrapper.and(i -> i.eq("party_member.del_flag", 0));
                List<PartyMember> partyMemberList = partyMemberService.list(wrapper);
                for (PartyMember partyMember : partyMemberList) {
                    String realName = partyMember.getRealName();
                    nameList.add(realName);
                }
                res.setNameList(nameList);
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
    @ApiOperation("分页查询test数据")
    @GetMapping("queryTzOrganizationLifeList")
    public ServerResponseEntity<Object> queryTzOrganizationLifeList(TzOrganizationLife tzOrganizationLife, SearchVo searchVo, PageVo pageVo) {
        try {
            IPage<TzOrganizationLife> result = tzOrganizationLifeService.queryTzOrganizationLifeListByPage(tzOrganizationLife, searchVo, pageVo);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：统计三会一课的数量
     *
     * @param searchVo 需要模糊查询的信息
     * @param pageVo   分页参数
     * @return 返回获取结果
     */
    @ApiOperation("分页查询test数据")
    @GetMapping("queryMeetingClass")
    public ServerResponseEntity<Object> queryMeetingClass(TzOrganizationLife tzOrganizationLife, SearchVo searchVo, PageVo pageVo) {
        try {
            QueryWrapper<TzSysDept> queryWrapper = new QueryWrapper<TzSysDept>();
            List<Integer> listAll = new ArrayList<>();

            //根据党组织id查询该组织下辖的所有组织的id，在业务层根据这些id集合查询相对应的党员
            if (tzOrganizationLife.getDeptId() != null) {
                queryWrapper.and(i -> i.eq("tz_sys_dept.del_flag", 0));
                if (tzOrganizationLife.getOrganizationType() != null && tzOrganizationLife.getOrganizationType() != "") {
                    queryWrapper.and(i -> i.eq("tz_sys_dept.organization_type", tzOrganizationLife.getOrganizationType()));
                }
                if (tzOrganizationLife.getVeteran() != null && tzOrganizationLife.getVeteran() != "") {
                    queryWrapper.and(i -> i.eq("tz_sys_dept.veteran", tzOrganizationLife.getVeteran()));
                }
                queryWrapper.and(i -> i.eq(("tz_sys_dept.id"), tzOrganizationLife.getDeptId()).or(i2 -> i2.like("tz_sys_dept.parent_ids", "," + tzOrganizationLife.getDeptId() + ",")));
                //queryWrapper.or(i -> i.like("tz_sys_dept.parent_ids", "," + tzOrganizationLife.getDeptId() + ","));
                List<TzSysDept> deptList = tzSysDeptService.list(queryWrapper);
                if (deptList == null || deptList.size() == 0) {
                    List<Map<String, Integer>> error = null;
                    return ServerResponseEntity.success(error);
                }
                for (TzSysDept tzSysDept : deptList) {
                    Integer id = tzSysDept.getId();
                    listAll.add(id);
                }
            }
            List<Map<String, Integer>> result = tzOrganizationLifeService.queryMeetingClass(tzOrganizationLife, listAll);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：导出数据
     *
     * @param response           请求参数
     * @param tzOrganizationLife 查询参数
     * @return
     */
    @ApiOperation("导出test数据")
    @PostMapping("/download")
    public void download(HttpServletResponse response, TzOrganizationLife tzOrganizationLife) {
        try {
            tzOrganizationLifeService.download(tzOrganizationLife, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("开始会议")
    @GetMapping("startMeeting")
    public ServerResponseEntity<Object> startMeeting(TzOrganizationLife tzOrganizationLife) {
        try {
            Integer id = tzOrganizationLife.getId();
            if (id == null) {
                return ServerResponseEntity.showFailMsg("参数为空，请联系管理员");
            }
            QueryWrapper<TzOrganizationLife> queryWrapper = new QueryWrapper<TzOrganizationLife>();
            queryWrapper.eq("tz_organization_life.id", tzOrganizationLife.getId());
            TzOrganizationLife byId = tzOrganizationLifeService.getById(id);
            if (byId == null) {
                return ServerResponseEntity.showFailMsg("数据不存在");
            }
            byId.setMeetingStatus("1");
            boolean update = tzOrganizationLifeService.update(byId, queryWrapper);
            if (update) {
                return ServerResponseEntity.success(update);
            } else {
                return ServerResponseEntity.showFailMsg("操作异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("操作异常:" + e.getMessage());
        }
    }

    @ApiOperation("取消会议")
    @GetMapping("cancelMeeting")
    public ServerResponseEntity<Object> cancelMeeting(TzOrganizationLife tzOrganizationLife) {
        try {
            Integer id = tzOrganizationLife.getId();
            if (id == null) {
                return ServerResponseEntity.showFailMsg("参数为空，请联系管理员");
            }
            QueryWrapper<TzOrganizationLife> queryWrapper = new QueryWrapper<TzOrganizationLife>();
            queryWrapper.eq("tz_organization_life.id", tzOrganizationLife.getId());
            TzOrganizationLife byId = tzOrganizationLifeService.getById(id);
            if (byId == null) {
                return ServerResponseEntity.showFailMsg("数据不存在");
            }
            byId.setMeetingStatus("3");
            boolean update = tzOrganizationLifeService.update(byId, queryWrapper);
            if (update) {
                return ServerResponseEntity.success(update);
            } else {
                return ServerResponseEntity.showFailMsg("操作异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("操作异常:" + e.getMessage());
        }
    }

    @ApiOperation("结束会议")
    @PostMapping("endMeeting")
    public ServerResponseEntity<Object> endMeeting(@RequestBody TzPersonMeeting tzPersonMeeting) {
        try {
            if (tzPersonMeeting.getMeetingId() == null) {
                return ServerResponseEntity.showFailMsg("参数为空，请联系管理员");
            }
            tzPersonMeeting.setDelFlag(0);
            tzPersonMeeting.setCreateTime(new Date());
            // 先把会议的参会人员存入人员表中
            boolean save = personMeetingService.save(tzPersonMeeting);
            if (save) {
                QueryWrapper<TzOrganizationLife> queryWrapper = new QueryWrapper<TzOrganizationLife>();
                queryWrapper.eq("tz_organization_life.id", tzPersonMeeting.getMeetingId());
                TzOrganizationLife byId = tzOrganizationLifeService.getById(tzPersonMeeting.getMeetingId());
                if (byId == null) {
                    return ServerResponseEntity.showFailMsg("数据不存在");
                }
                byId.setMeetingStatus("2");
                if (tzPersonMeeting.getJoins() != null && tzPersonMeeting.getJoins() != "") {
                    byId.setHost(tzPersonMeeting.getHost());
                    byId.setJoins(tzPersonMeeting.getJoins());
                    byId.setAbsence(tzPersonMeeting.getAbsent());
                    byId.setFlow(tzPersonMeeting.getFlow());
                    byId.setRecord(tzPersonMeeting.getRecord());
                }
                boolean update = tzOrganizationLifeService.update(byId, queryWrapper);
                if (update) {
                    return ServerResponseEntity.success(update);
                } else {
                    return ServerResponseEntity.showFailMsg("操作异常");
                }
            } else {
                return ServerResponseEntity.showFailMsg("操作异常");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("操作异常:" + e.getMessage());
        }
    }


    @ApiOperation("根据年份和部门id查出该部门某年的考勤表")
    @GetMapping("checkMeeting")
    public ServerResponseEntity<Object> checkMeeting(String deptId, String year, String meetingType) {
        try {
            //根据部门id查出所有党员
            List<String> nameList = new ArrayList<>();
            QueryWrapper<PartyMember> queryWrapper = new QueryWrapper<>();
            //queryWrapper.and(i -> i.eq("party_member.is_develop", "2").or(i2 ->i2.isNull("party_member.is_develop")));
            queryWrapper.and(i -> i.eq("party_member.dept_id", deptId));
            queryWrapper.and(i -> i.eq("party_member.del_flag", 0));
            List<PartyMember> list = partyMemberService.list(queryWrapper);
            for (PartyMember partyMember : list) {
                String realName = partyMember.getRealName();
                nameList.add(realName);
            }
            //
            List<Map<String, String>> maps = personMeetingService.checkMeeting(deptId, year, meetingType);
            Map<String, List> map = new HashMap<>();
            map.put("nameList", nameList);
            map.put("list", maps);
            return ServerResponseEntity.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("操作异常:" + e.getMessage());
        }
    }

    @ApiOperation("保存学习附件")
    @PostMapping("saveStudyFile")
    public ServerResponseEntity<Object> saveStudyFile(@RequestBody AttachFile attachFile) {
        try {
            if (attachFile.getForeignKey() == null && attachFile.getForeignKey() == "") {
                return ServerResponseEntity.showFailMsg("会议id为空，请联系管理员");
            }
            attachFile.setTableType("tz_party_meeting");
            attachFile.setType(3);
            attachFile.setUploadTime(new Date());
            boolean save = attachFileService.save(attachFile);
            if (save) {
                return ServerResponseEntity.success(save);
            } else {
                return ServerResponseEntity.showFailMsg("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("保存异常:" + e.getMessage());
        }
    }

    @ApiOperation("查询学习附件")
    @GetMapping("findStudyFile")
    public ServerResponseEntity<Object> findStudyFile(String meetingId) {
        try {
            QueryWrapper<AttachFile> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_attach_file.table_type", "tz_party_meeting");
            queryWrapper.eq("tz_attach_file.foreign_key", meetingId);
            queryWrapper.eq("tz_attach_file.type", "3");
            List<AttachFile> list = attachFileService.list(queryWrapper);
            return ServerResponseEntity.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("保存异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据主键来删除数据
     *
     * @param id 主键集合
     * @return 返回删除结果
     */
    @ApiOperation("根据主键来删除test数据")
    @GetMapping("deleteFileById")
    public ServerResponseEntity<Object> deleteFileById(String id) {
        if (id == null || id == "") {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            boolean res = attachFileService.removeById(id);
            if (res) {
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("删除异常:" + e.getMessage());
        }
    }


    /**
     * 功能描述：保存图片
     *
     * @param attachFile 主键集合
     */
    @ApiOperation("保存图片")
    @PostMapping("saveImg")
    public ServerResponseEntity<Object> saveImg(@RequestBody List<AttachFile> attachFile) {
        if (attachFile ==null||attachFile.size()==0) {
            return ServerResponseEntity.showFailMsg("图片不能为空，保存失败");
        }

        String meetingId = attachFile.get(0).getForeignKey();
        QueryWrapper<AttachFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tz_attach_file.foreign_key", meetingId);
        queryWrapper.eq("tz_attach_file.table_type", "tz_party_meeting");
        queryWrapper.eq("tz_attach_file.type", "1");
        List<AttachFile> list = attachFileService.list(queryWrapper);
        if (list != null && list.size() > 0) {
            List<String> fileId = new ArrayList<>();
            for (AttachFile file : list) {
                fileId.add(file.getFileId().toString());
            }
            boolean b = attachFileService.removeByIds(fileId);
        }
        for (AttachFile file : attachFile) {
            file.setUploadTime(new Date());
        }
        try {
            boolean res = attachFileService.saveBatch(attachFile);
            if (res) {
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("保存成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("保存异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据会议id查询会议图片
     *
     * @param meetingId
     * @return 返回list集合
     */
    @ApiOperation("根据会议id查询会议图片")
    @GetMapping("findImgById")
    public ServerResponseEntity<Object> findImgById(String meetingId) {
        if (meetingId == null || meetingId == "") {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            QueryWrapper<AttachFile> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_attach_file.foreign_key", meetingId);
            queryWrapper.eq("tz_attach_file.table_type", "tz_party_meeting");
            queryWrapper.eq("tz_attach_file.type", "1");
            List<AttachFile> list = attachFileService.list(queryWrapper);
            return ServerResponseEntity.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("删除异常:" + e.getMessage());
        }
    }


    @ApiOperation("统计开展率、党员参会率数据")
    @GetMapping("/getInfo")
    public ServerResponseEntity<Object> getInfo(TzOrganizationLife tzOrganizationLife,String deptId,String year) throws IOException {
        try {
            QueryWrapper<TzSysDept> queryWrapper = new QueryWrapper<>();
            List<Integer> deptIds = new ArrayList<>();
            List<Integer> baseDeptId = new ArrayList<>();
            if (StringUtils.isBlank(deptId)) {
                deptId = "1";
            }
            String finalDeptId = deptId;
            queryWrapper.or(i -> i.like("tz_sys_dept.parent_ids", "," + finalDeptId + ",").or(i2 -> i2.eq(("tz_sys_dept.id"), finalDeptId)));
            queryWrapper.and(i -> i.eq(("tz_sys_dept.del_flag"), 0));
            List<TzSysDept> deptList = tzSysDeptService.list(queryWrapper);

            QueryWrapper<TzSysDept> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.and(i -> i.eq("tz_sys_dept.parent_id",1));
            queryWrapper2.and(i -> i.eq(("tz_sys_dept.del_flag"), 0));
            List<TzSysDept> tzSysDeptList = tzSysDeptService.list(queryWrapper2);

            for (TzSysDept tzSysDept : tzSysDeptList) {
                Integer id = tzSysDept.getId();
                baseDeptId.add(id);
            }

            if (deptList != null && deptList.size() > 0) {
                for (TzSysDept tzSysDept : deptList) {
                    Integer id = tzSysDept.getId();
                    deptIds.add(id);
                }
                Map<String, Object> map = tzOrganizationLifeService.getInfo(deptIds,baseDeptId,tzOrganizationLife,year);
                return ServerResponseEntity.success(map);
            } else {
                return ServerResponseEntity.showFailMsg("该部门不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("统计异常:" + e.getMessage());
        }
    }
}
