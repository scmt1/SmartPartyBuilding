package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.facade.service.TzMessageAutoRoleService;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.domain.PageVo;
import me.flyray.bsin.server.domain.TzMessageAuto;
import me.flyray.bsin.server.domain.TzMessageAutoRole;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.mapper.TzMessageAutoMapper;
import me.flyray.bsin.server.mapper.TzMessageAutoRoleMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TzMessageAutoRoleServiceImpl implements TzMessageAutoRoleService {
    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private TzMessageAutoRoleMapper tzMessageAutoRoleMapper;

    @Autowired
    private TzMessageAutoMapper tzMessageAutoMapper;

    @Override
    public Map<String, Object> getDeptAutoMessageRoleByPage(Map<String, Object> requestMap) {
        try {
            Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(dataMap.get("pageVo")), PageVo.class);

            String deptName = (String) dataMap.get("deptName");
            String autoMessageRoleId = (String) dataMap.get("autoMessageRoleId");

            int page = 1;
            int limit = 10;
            if (pageVo != null) {
                if (pageVo.getPageNumber() != 0) {
                    page = pageVo.getPageNumber();
                }
                if (pageVo.getPageSize() != 0) {
                    limit = pageVo.getPageSize();
                }
            }

            // 只查询启用的
            QueryWrapper qn = new QueryWrapper();
            qn.select("id");
            qn.eq("is_open", 1);
            List<TzMessageAuto> autos = tzMessageAutoMapper.selectList(qn);
            if (autos == null || autos.size() == 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(new Page()));
            }
            List<Integer> autoIds = new ArrayList<>();
            for (TzMessageAuto auto: autos) {
                autoIds.add(auto.getId());
            }

            IPage<TzMessageAutoRole> pageData = new Page<>(page, limit);
            QueryWrapper<TzMessageAutoRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("dept_id, GROUP_CONCAT(auto_id) as auto_id_list");
            queryWrapper.in("auto_id", autoIds);
            if (StringUtils.isNotBlank(autoMessageRoleId)) {
                queryWrapper.eq("auto_id", autoMessageRoleId);
            }

            if (StringUtils.isNotBlank(deptName)) {
                QueryWrapper q1 = new QueryWrapper();
                q1.select("id");
                q1.eq("del_flag", 0);
                q1.like("name", deptName);
                List<JcxfSysDept> deptList = jcxfSysDeptMapper.selectList(q1);
                if (deptList == null || deptList.size() == 0) {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(new Page()));
                }
                List<Integer> ids = new ArrayList<>();
                for (JcxfSysDept dept: deptList) {
                    ids.add(dept.getId());
                }
                queryWrapper.in("dept_id", ids);
            }


            queryWrapper.groupBy("dept_id");

            IPage<TzMessageAutoRole> result = tzMessageAutoRoleMapper.selectPage(pageData, queryWrapper);
            for (TzMessageAutoRole autoRole: result.getRecords()) {
                JcxfSysDept dept = jcxfSysDeptMapper.selectById(autoRole.getDeptId());
                if (dept == null) {
                    autoRole.setDelFlag(1);
                } else {
                    autoRole.setDeptName(dept.getName());
                    autoRole.setDelFlag(dept.getDelFlag());
                }

                QueryWrapper queryWrapper1 = new QueryWrapper();
                queryWrapper1.select("auto_id");
                queryWrapper1.in("dept_id", autoRole.getDeptId());
                queryWrapper1.in("auto_id", autoIds);
                List<TzMessageAutoRole> autoRoleList = tzMessageAutoRoleMapper.selectList(queryWrapper1);
                if (autoRoleList.size() > 0) {
                    List<Integer> ids = new ArrayList<>();

                    String idList = "";
                    for (TzMessageAutoRole role: autoRoleList) {
                        ids.add(role.getAutoId());
                        idList += role.getAutoId()+",";
                    }
                    idList = idList.substring(0, idList.length() -1);
                    autoRole.setAutoIdList(idList);

                    QueryWrapper q = new QueryWrapper();
                    q.in("id", ids);
                    List<TzMessageAuto> autoList = tzMessageAutoMapper.selectList(q);
                    if (autoList != null) {
                        autoRole.setAutoList(autoList);
                    }
                }

            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> updateDeptAutoMessageRole(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            String deptId = (String) map.get("deptId");
            String autoMessageRole = (String) map.get("autoMessageRole");

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("dept_id", deptId);
            tzMessageAutoRoleMapper.delete(queryWrapper);

            if (StringUtils.isNotBlank(autoMessageRole)) {
                String[] autoIds = autoMessageRole.split(",");
                Date nowDate = new Date();
                for (int i = 0; i < autoIds.length; i++) {
                    TzMessageAutoRole role = new TzMessageAutoRole();
                    role.setAutoId(Integer.parseInt(autoIds[i]));
                    role.setDeptId(Long.parseLong(deptId));
                    role.setStatus("2"); // 状态 1、启用 2、关闭
                    role.setCreateDate(nowDate);

                    tzMessageAutoRoleMapper.insert(role);
                }
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存异常");
        }
    }

    @Override
    public Map<String, Object> getAutoMessageRoleByDeptId(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("dept_id", deptId);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzMessageAutoRoleMapper.selectList(queryWrapper)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存异常");
        }
    }

    @Override
    public Map<String, Object> updateAutoMessageRoleStatus(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            String autoId = (String) map.get("autoId");
            String deptId = (String) map.get("deptId");
            String status = (String) map.get("status");

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("auto_id", autoId);
            updateWrapper.eq("dept_id", deptId);
            updateWrapper.set("status", status);

            int count = tzMessageAutoRoleMapper.update(null, updateWrapper);
            if (count > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存异常");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存异常");
        }
    }
}
