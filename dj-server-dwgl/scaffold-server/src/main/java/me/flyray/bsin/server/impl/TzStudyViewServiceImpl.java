package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.facade.service.TzStudyViewService;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.mapper.TzStudyViewMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyMemberMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class TzStudyViewServiceImpl implements TzStudyViewService {

    @Autowired
    private TzStudyViewMapper tzStudyViewMapper;

    @Autowired
    private JcxfPartyMemberMapper partyMemberMapper;

    @Autowired
    private JcxfSysDeptMapper tzSysDeptMapper;

    @Override
    public Map<String, Object> getTzStudyViewByStudyId(Map<String, Object> requestMap) {
        try {
            String studyId = (String) requestMap.get("studyId");
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("study_id", studyId);

            TzStudyView view = tzStudyViewMapper.selectOne(queryWrapper);

            if (view !=null) {
                if (StringUtils.isNotBlank(view.getPartyMemberIds())) {
                    String[] ids = view.getPartyMemberIds().split(",");
                    if (ids.length > 0) {
                        QueryWrapper q = new QueryWrapper();
                        q.in("id", ids);
                        q.eq("del_flag", 0);
                        view.setPartyMemberList(partyMemberMapper.selectList(q));
                    }
                }

                if (StringUtils.isNotBlank(view.getDeptIds())) {
                    String[] ids = view.getDeptIds().split(",");
                    if (ids.length > 0) {
                        QueryWrapper q = new QueryWrapper();
                        q.in("id", ids);
                        q.eq("del_flag", 0);
                        view.setTzSysDeptList(tzSysDeptMapper.selectList(q));
                    }
                }
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(view));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public synchronized Map<String, Object> saveTzStudyView(Map<String, Object> requestMap) {
        try {

            TzStudyView tzStudyView = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), TzStudyView.class);

            int res = 0;
            if (tzStudyView.getId() == null) {
                tzStudyView.setCreateTime(new Date());
                res = tzStudyViewMapper.insert(tzStudyView);
            } else {
                tzStudyView.setUpdateTime(new Date());
                res = tzStudyViewMapper.updateById(tzStudyView);
            }

            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存异常");
        }
    }

    @Override
    public Map<String, Object> getViewScopeInfoByStudyId(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = new HashMap<>();
            String studyId = (String) requestMap.get("studyId");

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("study_id", studyId);
            TzStudyView tzStudyView = tzStudyViewMapper.selectOne(queryWrapper);

            if (StringUtils.isNotBlank(tzStudyView.getDeptIds())) {
                tzStudyView.setDeptIds(tzStudyView.getDeptIds().substring(1, tzStudyView.getDeptIds().length() -1));
            }
            if (StringUtils.isNotBlank(tzStudyView.getPartyMemberIds())) {
                tzStudyView.setPartyMemberIds(tzStudyView.getPartyMemberIds().substring(1, tzStudyView.getPartyMemberIds().length() -1));
            }
            if (StringUtils.isNotBlank(tzStudyView.getPositionIds())) {
                tzStudyView.setPositionIds(tzStudyView.getPositionIds().substring(1, tzStudyView.getPositionIds().length() -1));
            }


            String[] viewDeptIds = tzStudyView.getDeptIds() != null? tzStudyView.getDeptIds().split(","): new String[0];
            //	当前及其下级部门可看/当前部门可看
            if ("1".equals(tzStudyView.getType()) || "2".equals(tzStudyView.getType())) {
                List<JcxfSysDept> deptList = new ArrayList<>();
                int partyNum = 0;

                if (viewDeptIds.length > 0) {
                    QueryWrapper qDept = new QueryWrapper();
                    qDept.in("id", viewDeptIds);
                    qDept.eq("del_flag", 0);
                    deptList = tzSysDeptMapper.selectList(qDept);

                    Set<Long> deptIds = new HashSet<>();
                    for (String deptId: viewDeptIds) {
                        if ("1".equals(tzStudyView.getType())) {
                            deptIds.addAll(tzSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId)));
                            deptIds.add(Long.valueOf(deptId));
                        } else if ("2".equals(tzStudyView.getType())){
                            deptIds.add(Long.valueOf(deptId));
                        }
                    }

                    if (deptIds.size() > 0) {
                        QueryWrapper qParty = new QueryWrapper();
                        qParty.in("dept_id", deptIds);
                        qParty.eq("del_flag", 0);
                        partyNum = partyMemberMapper.selectCount(qParty);
                    }
                }

                map.put("deptList", deptList);
                map.put("partyNum", partyNum);
            } else if ("3".equals(tzStudyView.getType())) {
                //  当前职务可看-当前及其下级部门
                List<JcxfSysDept> deptList = new ArrayList<>();
                if (viewDeptIds.length > 0) {
                    QueryWrapper qDept = new QueryWrapper();
                    qDept.in("id", viewDeptIds);
                    qDept.eq("del_flag", 0);
                    deptList = tzSysDeptMapper.selectList(qDept);

                    Set<Long> deptIds = new HashSet<>();
                    for (String deptId: viewDeptIds) {
                        deptIds.addAll(tzSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId)));
                        deptIds.add(Long.valueOf(deptId));
                    }

                    String[] positionIds = tzStudyView.getPositionIds() !=null? tzStudyView.getPositionIds().split(","): new String[0];
                    if (deptIds.size() > 0 && positionIds.length > 0) {
                        PageVo pageVo = JSON.parseObject(JSON.toJSONString(requestMap.get("pageVo")), PageVo.class);
                        Page<JcxfPartyMember> pageData = new Page<>(pageVo.getPageNumber(), pageVo.getPageSize());

                        QueryWrapper qParty = new QueryWrapper();
                        qParty.in("dept_id", deptIds);
                        qParty.in("position", positionIds);
                        qParty.eq("del_flag", 0);
                        IPage<JcxfPartyMember> partyMemberList = partyMemberMapper.selectPage(pageData, qParty);
                        for (JcxfPartyMember partyMember: partyMemberList.getRecords()) {
                            JcxfSysDept tzSysDept = tzSysDeptMapper.selectById(partyMember.getDeptId());
                            partyMember.setDeptName(tzSysDept.getName());
                        }

                        map.put("partyMemberList", partyMemberList);
                        map.put("partyNum", partyMemberList.getTotal());
                    } else {
                        map.put("partyMemberList", new Page<JcxfPartyMember>());
                        map.put("partyNum", 0);
                    }
                } else {
                    map.put("partyMemberList", new Page<JcxfPartyMember>());
                    map.put("partyNum", 0);
                }

                map.put("deptList", deptList);
            } else if ("4".equals(tzStudyView.getType())) {
                //	当前职务可看-仅当前部门
                List<JcxfSysDept> deptList = new ArrayList<>();
                if (viewDeptIds.length > 0) {
                    QueryWrapper qDept = new QueryWrapper();
                    qDept.in("id", viewDeptIds);
                    qDept.eq("del_flag", 0);
                    deptList = tzSysDeptMapper.selectList(qDept);

                    PageVo pageVo = JSON.parseObject(JSON.toJSONString(requestMap.get("pageVo")), PageVo.class);
                    Page<JcxfPartyMember> pageData = new Page<>(pageVo.getPageNumber(), pageVo.getPageSize());

                    String[] positionIds = tzStudyView.getPositionIds()!=null? tzStudyView.getPositionIds().split(","): new String[0];
                    if (positionIds.length > 0) {
                        QueryWrapper qParty = new QueryWrapper();
                        qParty.in("dept_id", viewDeptIds);
                        qParty.in("position", positionIds);
                        qParty.eq("del_flag", 0);
                        IPage<JcxfPartyMember> partyMemberList = partyMemberMapper.selectPage(pageData, qParty);
                        for (JcxfPartyMember partyMember: partyMemberList.getRecords()) {
                            JcxfSysDept tzSysDept = tzSysDeptMapper.selectById(partyMember.getDeptId());
                            partyMember.setDeptName(tzSysDept.getName());
                        }
                        map.put("partyMemberList", partyMemberList);
                        map.put("partyNum", partyMemberList.getTotal());
                    } else {
                        map.put("partyMemberList", new Page<>());
                        map.put("partyNum", 0);
                    }

                } else {
                    map.put("partyMemberList", new Page<>());
                    map.put("partyNum", 0);
                }

                map.put("deptList", deptList);
            } else if ("5".equals(tzStudyView.getType())) {
                String[] partyMemberIds = tzStudyView.getPartyMemberIds() !=null? tzStudyView.getPartyMemberIds().split(","): new String[0];
                if (partyMemberIds.length > 0) {
                    PageVo pageVo = JSON.parseObject(JSON.toJSONString(requestMap.get("pageVo")), PageVo.class);
                    Page<JcxfPartyMember> pageData = new Page<>(pageVo.getPageNumber(), pageVo.getPageSize());
                    //	选中党员可看
                    QueryWrapper qParty = new QueryWrapper();
                    qParty.in("id", partyMemberIds);
                    qParty.eq("del_flag", 0);
                    IPage<JcxfPartyMember> partyMemberList = partyMemberMapper.selectPage(pageData, qParty);
                    for (JcxfPartyMember partyMember: partyMemberList.getRecords()) {
                        JcxfSysDept tzSysDept = tzSysDeptMapper.selectById(partyMember.getDeptId());
                        partyMember.setDeptName(tzSysDept.getName());
                    }
                    map.put("partyMemberList", partyMemberList);
                    map.put("partyNum", partyMemberList.getTotal());
                } else {
                    map.put("partyMemberList", new Page<>());
                    map.put("partyNum", 0);
                }
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }
}
