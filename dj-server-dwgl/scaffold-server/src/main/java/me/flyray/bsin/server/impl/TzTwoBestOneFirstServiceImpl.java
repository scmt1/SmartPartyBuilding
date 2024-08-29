package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.facade.service.TzTwoBestOneFirstService;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.mapper.*;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyMemberMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.*;

public class TzTwoBestOneFirstServiceImpl implements TzTwoBestOneFirstService {

    @Autowired
    private TzTwoBestOneFirstMapper tzTwoBestOneFirstMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private TzTwoBestOneFirstAuditMapper tzTwoBestOneFirstAuditMapper;

    @Autowired
    private TzTwoBestOneFirstTimeMapper tzTwoBestOneFirstTimeMapper;

    @Override
    public Map<String, Object> queryTzTwoBestOneFirstPage(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

            TzTwoBestOneFirst tzTwoBestOneFirst =  JSON.parseObject(JSON.toJSONString(map.get("tzTwoBestOneFirst")), TzTwoBestOneFirst.class);
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(map.get("searchVo")), SearchVo.class);
            String deptId = (String) map.get("deptId");

            // 获取所有下级部门id集合
            List<Long> deptIds = new ArrayList<>();
            Boolean onlyThisDept = (Boolean) map.get("onlyThisDept");
            if (onlyThisDept != null && onlyThisDept) {
                deptIds.add(Long.valueOf(deptId));
            } else {
                deptIds = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
                deptIds.add(Long.valueOf(deptId));
            }


            String upStartTime = null;
            String upEndTime = null;
            if (map.get("upStartTime") != null) {
                upStartTime = (String) map.get("upStartTime");
            }
            if (map.get("upStartTime") != null) {
                upEndTime = (String) map.get("upEndTime");
            }

            IPage<TzTwoBestOneFirst> result = queryTzTwoBestOneFirstPage(tzTwoBestOneFirst, searchVo, pageVo, upStartTime, upEndTime, deptIds);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Override
    public Map<String, Object> getTzTwoBestOneFirstById(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            TzTwoBestOneFirst tzTwoBestOneFirst = tzTwoBestOneFirstMapper.selectById(id);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzTwoBestOneFirst));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @DS("dj_party")
    @Override
    public Map<String, Object> updateTzTwoBestOneFirstById(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            TzTwoBestOneFirst tzTwoBestOneFirst =  JSON.parseObject(JSON.toJSONString(map.get("tzTwoBestOneFirst")), TzTwoBestOneFirst.class);

            TzTwoBestOneFirst oldTzTwoBestOneFirst = tzTwoBestOneFirstMapper.selectById(tzTwoBestOneFirst.getId());
            tzTwoBestOneFirst.setUpdateTime(new Date());

            if ("1".equals(oldTzTwoBestOneFirst.getStatus()) || "3".equals(oldTzTwoBestOneFirst.getStatus())
                || "5".equals(oldTzTwoBestOneFirst.getStatus()) || "7".equals(oldTzTwoBestOneFirst.getStatus())) {

                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("del_flag", 0);
                queryWrapper.eq("status", "1"); // 状态 0、未启用 1、启用
                queryWrapper.lt("up_start_time", new Date());
                queryWrapper.ge("up_end_time", new Date());
                int count1 = tzTwoBestOneFirstTimeMapper.selectCount(queryWrapper);

                if (count1 == 0) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"暂不在可提交时间内");
                }

                // 如果是待审核或者被驳回，修改后，重置状态
                tzTwoBestOneFirst.setStatus("1");

                UpdateWrapper up = new UpdateWrapper();
                up.eq("id", tzTwoBestOneFirst.getId());
                up.set("status", tzTwoBestOneFirst.getStatus());
                up.set("party_member_id", tzTwoBestOneFirst.getPartyMemberId());
                up.set("table_type", tzTwoBestOneFirst.getTableType());
                up.set("base_condition", tzTwoBestOneFirst.getBaseCondition());
                up.set("commend_condition", tzTwoBestOneFirst.getCommendCondition());
                up.set("main_deed", tzTwoBestOneFirst.getMainDeed());
                up.set("resume", tzTwoBestOneFirst.getResume());
                up.set("update_time", tzTwoBestOneFirst.getUpdateTime());
                up.set("avatar", tzTwoBestOneFirst.getAvatar());
                up.set("party_member_info", tzTwoBestOneFirst.getPartyMemberInfo());
                up.set("dept_info", tzTwoBestOneFirst.getDeptInfo());

                int res = tzTwoBestOneFirstMapper.update(null, up);
                if (res > 0) {
                    // 如果是重新提交，则增加一条记录
                    if (!"1".equals(oldTzTwoBestOneFirst.getStatus()) && "1".equals(tzTwoBestOneFirst.getStatus())) {
                        TzTwoBestOneFirstAudit audit = new TzTwoBestOneFirstAudit();
                        audit.setAuditDeptId(tzTwoBestOneFirst.getCreateDeptId());
                        audit.setTbofId(tzTwoBestOneFirst.getId());
                        audit.setCreateTime(new Date());
                        audit.setAuditStatus("3"); // 审核状态 1、通过  2、驳回 3、重新提交
                        int count = tzTwoBestOneFirstAuditMapper.insert(audit);
                        if (count > 0) {
                            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
                        } else {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"提交失败");
                        }
                    }
                } else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"提交失败");
                }
            } else {
                // 如果已经是被通过状态，则不允许修改
                if ("2".equals(oldTzTwoBestOneFirst.getStatus()) || "4".equals(oldTzTwoBestOneFirst.getStatus()) || "6".equals(oldTzTwoBestOneFirst.getStatus())) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"审核中或已审核完成，不可修改");
                }
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"提交失败");
        }
    }

    @Override
    public Map<String, Object> deleteTzTwoBestOneFirstById(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", id);
            updateWrapper.set("del_flag", 1);

            int res = tzTwoBestOneFirstMapper.update(null, updateWrapper);
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
    public Map<String, Object> auditTzTwoBestOneFirstById(Map<String, Object> requestMap) {
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("del_flag", 0);
            queryWrapper.eq("status", "1"); // 状态 0、未启用 1、启用
            queryWrapper.lt("audit_start_time", new Date());
            queryWrapper.ge("audit_end_time", new Date());
            int count1 = tzTwoBestOneFirstTimeMapper.selectCount(queryWrapper);

            if (count1 == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"暂不在可审核时间内");
            }

            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            String id = (String) map.get("id");
            String auditMessage = (String) map.get("auditMessage");

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", id);
            updateWrapper.set("audit_message", auditMessage);
            updateWrapper.set("audit_time", new Date());

            int res = tzTwoBestOneFirstMapper.update(null, updateWrapper);
            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"审核失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"审核失败");
        }
    }

    @DS("dj_party")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> addTzTwoBestOneFirst(Map<String, Object> requestMap) {
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("del_flag", 0);
            queryWrapper.eq("status", "1"); // 状态 0、未启用 1、启用
            queryWrapper.lt("up_start_time", new Date());
            queryWrapper.ge("up_end_time", new Date());
            int count1 = tzTwoBestOneFirstTimeMapper.selectCount(queryWrapper);

            if (count1 == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"暂不在可提交时间内");
            }

            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            TzTwoBestOneFirst tzTwoBestOneFirst =  JSON.parseObject(JSON.toJSONString(map.get("tzTwoBestOneFirst")), TzTwoBestOneFirst.class);

            tzTwoBestOneFirst.setDelFlag(0);
            tzTwoBestOneFirst.setUpdateTime(new Date());
            tzTwoBestOneFirst.setCreateTime(new Date());
            tzTwoBestOneFirst.setStatus("1");

            int res = tzTwoBestOneFirstMapper.insert(tzTwoBestOneFirst);
            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"提交失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"提交失败");
        }
    }

    @Override
    public Map<String, Object> getTzTwoBestOneFirstListByPartyMemberId(Map<String, Object> requestMap) {
        try {
            String partyMemberId = (String) requestMap.get("partyMemberId");
            String status = (String) requestMap.get("status");

            List<String> typeList = new ArrayList<>();
            typeList.add("1");
            typeList.add("2");

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("del_flag", 0);
            queryWrapper.eq("party_member_id", partyMemberId);
            queryWrapper.in("table_type", typeList);
            if (StringUtils.isNotEmpty(status)) {
                queryWrapper.eq("status", status);
            }
            queryWrapper.orderByDesc("update_time");

            List<TzTwoBestOneFirst> list = tzTwoBestOneFirstMapper.selectList(queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(list));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"提交失败");
        }
    }

    public IPage<TzTwoBestOneFirst> queryTzTwoBestOneFirstPage(TzTwoBestOneFirst tzTwoBestOneFirst, SearchVo searchVo, PageVo pageVo, String upStartTime, String upEndTime, List<Long> deptIds) {
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

        Page<TzTwoBestOneFirst> pageData = new Page<>(page, limit);
        QueryWrapper<TzTwoBestOneFirst> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tz_two_best_one_first.del_flag", 0);
        queryWrapper.in("tz_two_best_one_first.create_dept_id", deptIds);

        if (StringUtils.isNotBlank(upStartTime)) {
            queryWrapper.ge("tz_two_best_one_first.update_time", upStartTime);
        }

        if (StringUtils.isNotBlank(upEndTime)) {
            queryWrapper.le("tz_two_best_one_first.update_time", upEndTime);
        }

        if (tzTwoBestOneFirst.getTimeId() != null) {
            queryWrapper.eq("tz_two_best_one_first.time_id", tzTwoBestOneFirst.getTimeId());
        }

        if (tzTwoBestOneFirst != null) {
            LikeAllField(queryWrapper, tzTwoBestOneFirst, searchVo);
        }

        IPage<TzTwoBestOneFirst> result = tzTwoBestOneFirstMapper.getTzTwoBestOneFirstByPage(pageData, queryWrapper);
        return result;
    }

    public void LikeAllField(QueryWrapper<TzTwoBestOneFirst> queryWrapper, TzTwoBestOneFirst tzTwoBestOneFirst, SearchVo searchVo) {
        if (StringUtils.isNotEmpty(tzTwoBestOneFirst.getStatus())) {
            queryWrapper.and(i -> i.eq("tz_two_best_one_first.status", tzTwoBestOneFirst.getStatus()));
        }

        if (StringUtils.isNotEmpty(tzTwoBestOneFirst.getTableType())) {
            queryWrapper.and(i -> i.eq("tz_two_best_one_first.table_type", tzTwoBestOneFirst.getTableType()));
        }

        if (StringUtils.isNotEmpty(tzTwoBestOneFirst.getCommendType())) {
            queryWrapper.and(i -> i.eq("tz_two_best_one_first.commend_type", tzTwoBestOneFirst.getCommendType()));
        }
    }
}
