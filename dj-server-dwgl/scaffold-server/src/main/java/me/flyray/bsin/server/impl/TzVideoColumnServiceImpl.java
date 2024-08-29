package me.flyray.bsin.server.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.facade.service.TzVideoColumnService;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.mapper.TzStudyVideoMapper;
import me.flyray.bsin.server.mapper.TzStudyViewMapper;
import me.flyray.bsin.server.mapper.TzTeacherInfoMapper;
import me.flyray.bsin.server.mapper.TzVideoColumnMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyMemberMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class TzVideoColumnServiceImpl implements TzVideoColumnService {

    @Autowired
    private TzVideoColumnMapper tzVideoColumnMapper;

    @Autowired
    private TzTeacherInfoMapper tzTeacherInfoMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private TzStudyViewMapper tzStudyViewMapper;

    @Autowired
    private JcxfPartyMemberMapper jcxfPartyMemberMapper;

    @Autowired
    private TzStudyVideoMapper tzStudyVideoMapper;

    @Override
    public Map<String, Object> queryAll(Map<String, Object> requestMap) {
        Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
        //String id = (String) map.get("id");
        String type = (String) map.get("type");
        String deptId = (String) map.get("deptId");
        String tenantId = (String) requestMap.get("bizTenantId");
        try {
            QueryWrapper<TzVideoColumn> q = new QueryWrapper<>();

            if (StringUtils.isNotBlank(type)) {
                q.eq("type", type);
            }
            q.eq("del_flag", 0);
            q.eq("dept_id", deptId);
            //q.eq("tenant_id", tenantId);
            q.orderByAsc("orders");
            List<TzVideoColumn> list = tzVideoColumnMapper.selectList(q);
            //recursionTests(Integer.parseInt(id), list, type, deptId, tenantId);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(list));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> queryVideoColumnByPage(Map<String, Object> requestMap) {
        Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
        String type = (String) map.get("type");
        String deptId = (String) map.get("deptId");
        String tenantId = (String) requestMap.get("bizTenantId");
        PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);
        try {
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

            Page<TzVideoColumn> pageData = new Page<>(page, limit);
            QueryWrapper<TzVideoColumn> q = new QueryWrapper<>();

            if (StringUtils.isNotBlank(type)) {
                q.eq("type", type);
            }
            q.eq("del_flag", 0);
            q.eq("dept_id", deptId);
            q.orderByAsc("orders");
            IPage<TzVideoColumn> list = tzVideoColumnMapper.selectPage(pageData,q);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(list));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> addVideoColumn(Map<String, Object> requestMap) {
        try {
            TzVideoColumn tzVideoColumn = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), TzVideoColumn.class);
            if (tzVideoColumn.getId() != null) {
                tzVideoColumn.setUpdateTime(new Date());
                // tzVideoColumn.setUpdateBy(username);
                int updateById = tzVideoColumnMapper.updateById(tzVideoColumn);
                if (updateById > 0) {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(updateById));
                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"修改失败");
                }
            } else {
                String tenantId = (String) requestMap.get("bizTenantId");

                //tzVideoColumn.setCreateBy(username);
                tzVideoColumn.setCreateTime(new Date());
                //tzVideoColumn.setTenantId(tenantId);
                tzVideoColumn.setDelFlag(0);

                int res = tzVideoColumnMapper.insert(tzVideoColumn);
                if (res > 0) {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(res));
                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存异常");
        }
    }

    @Override
    public Map<String, Object> getVideoColumn(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
        }
        try {
            TzVideoColumn tzVideoColumn = tzVideoColumnMapper.selectById(id);
            if (tzVideoColumn != null && tzVideoColumn.getDelFlag().intValue() == 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzVideoColumn));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> deleteVideoColumn(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");

        if (StringUtils.isBlank(id)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
        }
        try {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", id);
            updateWrapper.set("del_flag", 1);
            tzVideoColumnMapper.update(null, updateWrapper);

            UpdateWrapper updateWrapper1 = new UpdateWrapper();
            updateWrapper1.eq("column_id", id);
            updateWrapper1.set("del_flag", 1);
            tzStudyVideoMapper.update(null, updateWrapper1);

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("column_id", id);
            tzStudyViewMapper.delete(queryWrapper);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除异常");
        }
    }

    @Override
    public Map<String, Object> getVideoColumnListByType(Map<String, Object> requestMap) {
        try {
            String type = (String) requestMap.get("type");
            String partyId = (String) requestMap.get("partyId");

            // 获取可查看范围内的栏目id
            List<Integer> columnIds = new ArrayList<>();
            if (StringUtils.isNotBlank(partyId)) {
                JcxfPartyMember partyMember = jcxfPartyMemberMapper.selectById(partyId);
                if (partyMember != null && !partyMember.getDelFlag()) {
                    columnIds = getColumnListByPartyMember(partyMember);
                }
            }

            if (columnIds != null && columnIds.size() > 0) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.in("id", columnIds);
                queryWrapper.eq("type", type);
                queryWrapper.eq("del_flag", 0);
                queryWrapper.eq("show_status", "1");
                queryWrapper.orderByAsc("orders");

                List<TzVideoColumn> columnList = tzVideoColumnMapper.selectList(queryWrapper);
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(columnList));
            } else {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(new ArrayList()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    public List<Integer> getColumnListByPartyMember(JcxfPartyMember partyMember) {
        JcxfSysDept dept = jcxfSysDeptMapper.selectById(partyMember.getDeptId());
        String[] parentIds = dept.getParentIds().split(",");

        ArrayList<String> parentIdList = new ArrayList<>(parentIds.length);
        Collections.addAll(parentIdList, parentIds);
        parentIdList.add(partyMember.getDeptId().toString());
        List<Integer> columnIds = tzStudyViewMapper.getColumnIdsByPartMember(
                parentIdList, String.valueOf(partyMember.getDeptId()), String.valueOf(partyMember.getPosition()), String.valueOf(partyMember.getId()));
        return columnIds;
    }

    private List<TzVideoColumn> recursionTests(Integer parentId, List<TzVideoColumn> list, String type, String deptId, String tenantId) {
        List<TzVideoColumn> one = tzVideoColumnMapper.selectList(Wrappers.<TzVideoColumn>query().lambda()
                .eq(TzVideoColumn::getParentId, parentId)
                .eq(TzVideoColumn::getType, type)
                .eq(TzVideoColumn::getDelFlag, 0)
                .eq(TzVideoColumn::getDeptId, deptId)
                .eq(TzVideoColumn::getTenantId, tenantId));
        if (ObjectUtil.isEmpty(one)) {
            return list;
        } else {
            for (TzVideoColumn tzVideoColumn : one) {
                list.add(tzVideoColumn);
                recursionTests(tzVideoColumn.getId(), list, type, deptId, tenantId);
            }
        }
        return list;
    }

}
