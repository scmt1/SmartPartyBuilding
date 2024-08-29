package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.facade.service.TzPartyHonorService;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.domain.TzPartyHonor;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.mapper.*;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyMemberMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TzPartyHonorImpl implements TzPartyHonorService {

    @Autowired
    private TzPartyHonorMapper tzPartyHonorMapper;

    @Autowired
    private TzHonorRecordMapper tzHonorRecordMapper;
    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private JcxfPartyMemberMapper jcxfPartyMemberMapper;

    @Override
    public Map<String, Object> queryTzPartyHonorList(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

            PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(map.get("searchVo")), SearchVo.class);
            TzPartyHonor tzPartyHonor = JSON.parseObject(JSON.toJSONString(map.get("tzPartyHonor")), TzPartyHonor.class);
            //String tenantId = (String) requestMap.get("bizTenantId");
            String realName = (String) map.get("realName");
            String sortType = (String) map.get("sortType");

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

            //根据党组织id查询该组织下辖的所有组织的id
            List<Long> listAll = new ArrayList<>();
            listAll = jcxfSysDeptMapper.selectChildrenIdsById(tzPartyHonor.getDeptId());
            listAll.add(tzPartyHonor.getDeptId());

            //根据获奖党员姓名查询
            List<Long> partyMemberIds = new ArrayList<>();
            if(StringUtils.isNotBlank(realName)) {
                QueryWrapper q2 = new QueryWrapper();
                q2.select("id");
                q2.like("real_name", realName);
                List<JcxfPartyMember> memberList = jcxfPartyMemberMapper.selectList(q2);
                if (memberList == null || memberList.size() == 0) {
                    IPage<TzPartyHonor> result = new Page<>();
                    result.setTotal(0);
                    result.setRecords(new ArrayList<>());
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
                } else {
                    for (JcxfPartyMember partyMember: memberList) {
                        partyMemberIds.add(partyMember.getId());
                    }
                }
            }

            Page<TzPartyHonor> pageData = new Page<>(page, limit);
            QueryWrapper<TzPartyHonor> queryWrapper = new QueryWrapper<>();
            if (tzPartyHonor != null) {
                queryWrapper = LikeAllField(tzPartyHonor, searchVo, listAll, partyMemberIds, sortType);
            }

            IPage<TzPartyHonor> result = tzPartyHonorMapper.selectPage(pageData, queryWrapper);
            for (TzPartyHonor honor: result.getRecords()) {
                honor.setTzSysDept(jcxfSysDeptMapper.selectById(honor.getDeptId()));
                //荣誉类型 1、组织荣誉 2、个人荣誉
                if ("2".equals(honor.getHonorType()) && honor.getPartyMemberId() !=null) {
                    honor.setPartyMember(jcxfPartyMemberMapper.selectById(honor.getPartyMemberId()));
                }
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> addTzPartyHonor(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            TzPartyHonor tzPartyHonor = JSON.parseObject(JSON.toJSONString(map.get("tzPartyHonor")), TzPartyHonor.class);
            tzPartyHonor.setDelFlag(0);
            tzPartyHonor.setCreateTime(new Date());
            tzPartyHonor.setStatus("1"); //审核状态（1、待审核，2、审核通过，3、已驳回）

            //String tenantId = (String) requestMap.get("bizTenantId");
            //tzPartyHonor.setTenantId(tenantId);

            int res = tzPartyHonorMapper.insert(tzPartyHonor);
            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
        }
    }

    @Override
    public Map<String, Object> updateTzPartyHonor(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            TzPartyHonor tzPartyHonor = JSON.parseObject(JSON.toJSONString(map.get("tzPartyHonor")), TzPartyHonor.class);

            int res = tzPartyHonorMapper.updateById(tzPartyHonor);
            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
        }
    }

    @Override
    public Map<String, Object> delTzPartyHonor(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");

            TzPartyHonor honor = tzPartyHonorMapper.selectById(id);
            if (honor.getDelFlag().intValue() == 0 && "2".equals(honor.getStatus())) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"审核已通过，不可删除");
            }

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", id);
            updateWrapper.set("del_flag", 1);

            int res = tzPartyHonorMapper.update(null, updateWrapper);
            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除失败");
        }
    }

    @Override
    public Map<String, Object> getTzPartyHonorById(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            TzPartyHonor honor = tzPartyHonorMapper.selectById(id);
            if (honor != null) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(honor));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未获取到信息");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"获取异常");
        }
    }

    @Override
    public Map<String, Object> queryTzPartyHonorListOneselfDept(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

            PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(map.get("searchVo")), SearchVo.class);
            TzPartyHonor tzPartyHonor = JSON.parseObject(JSON.toJSONString(map.get("tzPartyHonor")), TzPartyHonor.class);
            String sortType = (String) map.get("sortType");

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

            List<Long> listAll = new ArrayList<>();
            // 获取当前员工所在组织树
            JcxfSysDept dept = jcxfSysDeptMapper.selectById(tzPartyHonor.getDeptId());
            if (dept != null) {
                Long searchDeptId = null;
                if (dept.getParentId() == 0) {
                    searchDeptId = Long.valueOf(dept.getId());
                } else {
                    searchDeptId = Long.valueOf(dept.getParentIds().split(",")[1]);
                }
                listAll = jcxfSysDeptMapper.selectChildrenIdsById(searchDeptId);
                listAll.add(searchDeptId);
            }

            Page<TzPartyHonor> pageData = new Page<>(page, limit);
            QueryWrapper<TzPartyHonor> queryWrapper = new QueryWrapper<>();
            if (tzPartyHonor != null) {
                queryWrapper = LikeAllField(tzPartyHonor, searchVo, listAll, new ArrayList<>(), sortType);
            }

            IPage<TzPartyHonor> result = tzPartyHonorMapper.selectPage(pageData, queryWrapper);
            for (TzPartyHonor honor: result.getRecords()) {
                honor.setTzSysDept(jcxfSysDeptMapper.selectById(honor.getDeptId()));
                //荣誉类型 1、组织荣誉 2、个人荣誉
                if ("2".equals(honor.getHonorType()) && honor.getPartyMemberId() !=null) {
                    honor.setPartyMember(jcxfPartyMemberMapper.selectById(honor.getPartyMemberId()));
                }
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }


    @Override
    public Map<String, Object> passHonor(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            String createId = (String) requestMap.get("createId");
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", id);
            updateWrapper.set("status", 2);

            int res = tzPartyHonorMapper.update(null, updateWrapper);
            if (res <= 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"审核失败");
            }
            TzHonorRecord tzHonorRecord = new TzHonorRecord();
            tzHonorRecord.setHonorId(id);
            tzHonorRecord.setIsPass(1);
            tzHonorRecord.setCreateId(createId);
            tzHonorRecord.setCreateTime(new Date());
            int insert = tzHonorRecordMapper.insert(tzHonorRecord);
            if(insert<=0){
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("审核通过"));
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("审核失败"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"审核异常");
        }
    }

    @Override
    public Map<String, Object> unPass(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            TzPartyHonor tzPartyHonor = JSON.parseObject(JSON.toJSONString(map.get("tzPartyHonor")), TzPartyHonor.class);
            String reason = tzPartyHonor.getReason();

            String id = tzPartyHonor.getId().toString();
            String createId = tzPartyHonor.getDeptId().toString();
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", id);
            updateWrapper.set("status", 3);
            updateWrapper.set("reason", reason);

            int res = tzPartyHonorMapper.update(null, updateWrapper);
            if (res <= 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"驳回失败");
            }
            TzHonorRecord tzHonorRecord = new TzHonorRecord();
            tzHonorRecord.setHonorId(id);
            tzHonorRecord.setIsPass(0);
            tzHonorRecord.setCreateId(createId);
            tzHonorRecord.setReason(reason);
            tzHonorRecord.setCreateTime(new Date());
            int insert = tzHonorRecordMapper.insert(tzHonorRecord);
            if(insert<=0){
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("驳回成功"));
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("驳回失败"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"驳回异常");
        }
    }

    @Override
    public Map<String, Object> queryHonorRecord(Map<String, Object> requestMap) {
        try {
            String honorId = (String) requestMap.get("honorId");
            QueryWrapper<TzHonorRecord> q=new QueryWrapper<>();
            q.eq("honor_id",honorId);
            q.orderByDesc("create_time");
            List<TzHonorRecord> tzHonorRecords = tzHonorRecordMapper.selectList(q);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzHonorRecords));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> queryTzPartyHonorListOneselfPartyMember(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

            PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);
            TzPartyHonor tzPartyHonor = JSON.parseObject(JSON.toJSONString(map.get("tzPartyHonor")), TzPartyHonor.class);

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

            Page<TzPartyHonor> pageData = new Page<>(page, limit);
            QueryWrapper<TzPartyHonor> queryWrapper = new QueryWrapper<>();
            if (tzPartyHonor != null) {
                queryWrapper.eq("del_flag", 0);
                queryWrapper.eq("party_member_id", tzPartyHonor.getPartyMemberId());

                if (tzPartyHonor.getUploadType() != null) {
                    queryWrapper.eq("upload_type", tzPartyHonor.getUploadType());
                }
            }

            queryWrapper.orderByDesc("create_time");

            IPage<TzPartyHonor> result = tzPartyHonorMapper.selectPage(pageData, queryWrapper);
            for (TzPartyHonor honor: result.getRecords()) {
                //荣誉类型 1、组织荣誉 2、个人荣誉
                if ("2".equals(honor.getHonorType()) && honor.getPartyMemberId() !=null) {
                    honor.setPartyMember(jcxfPartyMemberMapper.selectById(honor.getPartyMemberId()));
                }
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> deptHonorRank(Map<String, Object> requestMap) {
        try{
            Integer deptId = (Integer) requestMap.get("deptId");
            Integer honorClass = (Integer) requestMap.get("honorClass");
            if(deptId==null||honorClass==null){
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"部门id为空或荣誉类型为空");
            }
            List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            deptIds.add(Long.valueOf(deptId));
            List<TzPartyHonor> tzPartyHonors =  tzPartyHonorMapper.deptHonorRank(deptIds,honorClass);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzPartyHonors));
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }


    public QueryWrapper<TzPartyHonor> LikeAllField(TzPartyHonor tzPartyHonor, SearchVo searchVo, List<Long> listAll, List<Long> partyMemberIds, String sortType) {
        QueryWrapper<TzPartyHonor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", 0);
        //if (listAll!=null&&listAll.size() > 0 ) {
            queryWrapper.in("dept_id", listAll);
        //}


        if (StringUtils.isNotBlank(tzPartyHonor.getHonorName())) {
            queryWrapper.like("honor_name", tzPartyHonor.getHonorName());
        }
        if (StringUtils.isNotBlank(tzPartyHonor.getAwardName())) {
            queryWrapper.like("award_name", tzPartyHonor.getAwardName());
        }

        if (StringUtils.isNotEmpty(tzPartyHonor.getHonorType())) {
            queryWrapper.eq("honor_type", tzPartyHonor.getHonorType());
        }

        if (StringUtils.isNotEmpty(tzPartyHonor.getStatus())) {
            queryWrapper.eq("status", tzPartyHonor.getStatus());
        }

        if (StringUtils.isNotEmpty(tzPartyHonor.getHonorClass())) {
            queryWrapper.eq("honor_class", tzPartyHonor.getHonorClass());
        }

        if (StringUtils.isNotBlank(tzPartyHonor.getGetYear())) {
            queryWrapper.eq("get_year", tzPartyHonor.getGetYear());
        }

        if (partyMemberIds.size() > 0 ) {
            queryWrapper.in("party_member_id", partyMemberIds);
        }

        if (StringUtils.isNotBlank(sortType)) {
            if ("time".equals(sortType)) {
                queryWrapper.orderByDesc("get_year");
            } else if ("class".equals(sortType)) {
                queryWrapper.orderByAsc("honor_class");
            }
        }


        return queryWrapper;
    }
}
