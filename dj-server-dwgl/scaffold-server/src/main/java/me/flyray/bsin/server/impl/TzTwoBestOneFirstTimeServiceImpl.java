package me.flyray.bsin.server.impl;


import com.alibaba.fastjson.JSON;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.models.auth.In;
import me.flyray.bsin.facade.service.TzTwoBestOneFirstTimeService;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.domain.PageVo;
import me.flyray.bsin.server.domain.SearchVo;
import me.flyray.bsin.server.domain.TzTwoBestOneFirst;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.domain.TzTwoBestOneFirstTime;
import me.flyray.bsin.server.mapper.TzTwoBestOneFirstMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.mapper.TzTwoBestOneFirstTimeMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class TzTwoBestOneFirstTimeServiceImpl implements TzTwoBestOneFirstTimeService {

    @Autowired
    private TzTwoBestOneFirstTimeMapper tzTwoBestOneFirstTimeMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private TzTwoBestOneFirstMapper tzTwoBestOneFirstMapper;

    @Override
    public Map<String, Object> queryTzTwoBestOneFirstTimePage(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            String deptId = (String) map.get("deptId");
            List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            deptIds.add(Long.valueOf(deptId));
            TzTwoBestOneFirstTime tzTwoBestOneFirstTime =  JSON.parseObject(JSON.toJSONString(map.get("tzTwoBestOneFirstTime")), TzTwoBestOneFirstTime.class);
            tzTwoBestOneFirstTime.setDeptIds(deptIds);
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(map.get("searchVo")), SearchVo.class);

            String upStartTime = (String) map.get("upStartTime");
            String upEndTime = (String) map.get("upEndTime");

            IPage<TzTwoBestOneFirstTime> result = queryTzTwoBestOneFirstTimePage(tzTwoBestOneFirstTime, upStartTime, upEndTime, searchVo, pageVo);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Object> addTzTwoBestOneFirstTime(Map<String, Object> requestMap) {
        try {
            TzTwoBestOneFirstTime tzTwoBestOneFirstTime =  JSON.parseObject(JSON.toJSONString(requestMap.get("tzTwoBestOneFirstTime")), TzTwoBestOneFirstTime.class);
            JcxfSysDept tzSysDept = jcxfSysDeptMapper.selectById(tzTwoBestOneFirstTime.getDeptId());
            if (tzSysDept.getParentId() == 0) {
                tzTwoBestOneFirstTime.setMaxParentId(tzSysDept.getId());
            } else {
                tzTwoBestOneFirstTime.setMaxParentId(Integer.parseInt(tzSysDept.getParentIds().split(",")[1]));
            }
            tzTwoBestOneFirstTime.setDelFlag(0);
            tzTwoBestOneFirstTime.setStatus("0");
            tzTwoBestOneFirstTime.setCreateTime(new Date());

            int res = tzTwoBestOneFirstTimeMapper.insert(tzTwoBestOneFirstTime);
            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"新增失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"新增失败");
        }
    }

    @Override
    public Map<String, Object> updateTzTwoBestOneFirstTime(Map<String, Object> requestMap) {
        try {
            TzTwoBestOneFirstTime tzTwoBestOneFirstTime =  JSON.parseObject(JSON.toJSONString(requestMap.get("tzTwoBestOneFirstTime")), TzTwoBestOneFirstTime.class);
            JcxfSysDept tzSysDept = jcxfSysDeptMapper.selectById(tzTwoBestOneFirstTime.getDeptId());
            if (tzSysDept.getParentId() == 0) {
                tzTwoBestOneFirstTime.setMaxParentId(tzSysDept.getId());
            } else {
                tzTwoBestOneFirstTime.setMaxParentId(Integer.parseInt(tzSysDept.getParentIds().split(",")[1]));
            }
            tzTwoBestOneFirstTime.setUpdateTime(new Date());

            int res = tzTwoBestOneFirstTimeMapper.updateById(tzTwoBestOneFirstTime);
            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"修改失败");
        }
    }

    @Override
    public Map<String, Object> getTzTwoBestOneFirstTimeById(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzTwoBestOneFirstTimeMapper.selectById(id)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Override
    public Map<String, Object> deleteTzTwoBestOneFirstTimeById(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", id);
            updateWrapper.set("del_flag", 1);

            int res = tzTwoBestOneFirstTimeMapper.update(null, updateWrapper);
            if (res > 0) {
                QueryWrapper<TzTwoBestOneFirst> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("time_id",id);
                List<TzTwoBestOneFirst> tzTwoBestOneFirsts = tzTwoBestOneFirstMapper.selectList(queryWrapper);
                List<Integer> collect = tzTwoBestOneFirsts.stream().map(item -> item.getId()).collect(Collectors.toList());
                if(collect.size() > 0){
                    tzTwoBestOneFirstMapper.deleteBatchIds(collect);
                }
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
    public Map<String, Object> updateTimeStatusById(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

            String id = (String) map.get("id");
            String status = (String) map.get("status");

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", id);
            updateWrapper.set("status", status);

            int res = tzTwoBestOneFirstTimeMapper.update(null, updateWrapper);
            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"修改失败");
        }
    }

    @Override
    public Map<String, Object> getIsAddTime(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            JcxfSysDept dept = jcxfSysDeptMapper.selectById(deptId);
            if (dept == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未获取到部门信息");
            }

            String searchDeptId = null;

            JcxfSysDept thisTzSysDept = jcxfSysDeptMapper.selectById(deptId);
            if (thisTzSysDept.getParentId() == 0) {
                searchDeptId = deptId;
            } else {
                searchDeptId = thisTzSysDept.getParentIds().split(",")[1];
            }

            Date time = new Date();
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("max_parent_id", searchDeptId);
            queryWrapper.eq("del_flag", 0);
            queryWrapper.eq("status", "1"); // 状态 0、未启用 1、启用
            queryWrapper.lt("up_start_time", time);
            queryWrapper.ge("up_end_time", time);
            int count = tzTwoBestOneFirstTimeMapper.selectCount(queryWrapper);

            if (count > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(true));
            } else {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(false));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询出错");
        }
    }

    @Override
    public Map<String, Object> getIsAuditTime(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            JcxfSysDept dept = jcxfSysDeptMapper.selectById(deptId);
            if (dept == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未获取到部门信息");
            }

            String searchDeptId = null;

            JcxfSysDept thisTzSysDept = jcxfSysDeptMapper.selectById(deptId);
            if (thisTzSysDept.getParentId() == 0) {
                searchDeptId = deptId;
            } else {
                searchDeptId = thisTzSysDept.getParentIds().split(",")[1];
            }

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("max_parent_id", searchDeptId);
            queryWrapper.eq("del_flag", 0);
            queryWrapper.eq("status", "1"); // 状态 0、未启用 1、启用
            queryWrapper.lt("audit_start_time", new Date());
            queryWrapper.ge("audit_end_time", new Date());
            int count = tzTwoBestOneFirstTimeMapper.selectCount(queryWrapper);

            if (count > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(true));
            } else {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(false));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询出错");
        }
    }

    @Override
    public Map<String, Object> getAddTimeList(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            JcxfSysDept dept = jcxfSysDeptMapper.selectById(deptId);
            if (dept == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未获取到部门信息");
            }

            String searchDeptId = null;

            JcxfSysDept thisTzSysDept = jcxfSysDeptMapper.selectById(deptId);
            if (thisTzSysDept.getParentId() == 0) {
                searchDeptId = deptId;
            } else {
                searchDeptId = thisTzSysDept.getParentIds().split(",")[1];
            }

            Date time = new Date();
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("max_parent_id", searchDeptId);
            queryWrapper.eq("del_flag", 0);
            queryWrapper.eq("status", "1"); // 状态 0、未启用 1、启用
            queryWrapper.lt("up_start_time", time);
            queryWrapper.ge("up_end_time", time);
            List<TzTwoBestOneFirstTime> timeList = tzTwoBestOneFirstTimeMapper.selectList(queryWrapper);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(timeList));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询出错");
        }
    }

    @Override
    public Map<String, Object> queryTimeByContent(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

            TzTwoBestOneFirstTime tzTwoBestOneFirstTime =  JSON.parseObject(JSON.toJSONString(map.get("tzTwoBestOneFirstTime")), TzTwoBestOneFirstTime.class);
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(map.get("searchVo")), SearchVo.class);

            String deptId = (String) map.get("deptId");
            String upStartTime = (String) map.get("upStartTime");
            String upEndTime = (String) map.get("upEndTime");

            List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            if(deptIds.size() == 0){
                JcxfSysDept jcxfSysDept = jcxfSysDeptMapper.selectById(deptId);
                if(jcxfSysDept != null){
                    String parentIds = jcxfSysDept.getParentIds();
                    parentIds = parentIds.substring(0,parentIds.length() - 1);
                    String[] split = parentIds.split(",");
                    for (String s:split) {
                        deptIds.add(Long.parseLong(s));
                    }
                }
            }
            deptIds.add(Long.valueOf(deptId));

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

            Page<TzTwoBestOneFirstTime> pageData = new Page<>(page, limit);
            QueryWrapper<TzTwoBestOneFirstTime> queryWrapper = new QueryWrapper<>();
            if (tzTwoBestOneFirstTime != null) {
                queryWrapper = LikeAllField(tzTwoBestOneFirstTime, searchVo);
            }

            if (StringUtils.isNotEmpty(upStartTime)) {
                queryWrapper.ge("tz_two_best_one_first_time.up_start_time", upStartTime);
            }

            if (StringUtils.isNotEmpty(upEndTime)) {
                queryWrapper.le("tz_two_best_one_first_time.up_end_time", upEndTime);
            }
            queryWrapper.in("tz_two_best_one_first_time.dept_id",deptIds);
            queryWrapper.groupBy("tz_two_best_one_first_time.id");
            queryWrapper.orderByDesc("tz_two_best_one_first_time.create_time");
            IPage<TzTwoBestOneFirstTime> result = tzTwoBestOneFirstTimeMapper.getTwoBestOneFirstTimeByPage(pageData, queryWrapper,deptId);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询出错");
        }
    }

    public IPage<TzTwoBestOneFirstTime> queryTzTwoBestOneFirstTimePage(TzTwoBestOneFirstTime tzTwoBestOneFirstTime, String upStartTime, String upEndTime, SearchVo searchVo, PageVo pageVo) {
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

        Page<TzTwoBestOneFirstTime> pageData = new Page<>(page, limit);
        QueryWrapper<TzTwoBestOneFirstTime> queryWrapper = new QueryWrapper<>();
        if (tzTwoBestOneFirstTime != null) {
            queryWrapper = LikeAllField(tzTwoBestOneFirstTime, searchVo);
        }

        if (StringUtils.isNotEmpty(upStartTime)) {
            queryWrapper.ge("up_start_time", upStartTime);
        }

        if (StringUtils.isNotEmpty(upEndTime)) {
            queryWrapper.le("up_end_time", upEndTime);
        }
        queryWrapper.in("dept_id",tzTwoBestOneFirstTime.getDeptIds());

        queryWrapper.orderByDesc("create_time");
        IPage<TzTwoBestOneFirstTime> result = tzTwoBestOneFirstTimeMapper.selectPage(pageData, queryWrapper);
        return result;
    }

    public QueryWrapper<TzTwoBestOneFirstTime> LikeAllField(TzTwoBestOneFirstTime tzTwoBestOneFirstTime, SearchVo searchVo) {
        QueryWrapper<TzTwoBestOneFirstTime> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(i -> i.eq("tz_two_best_one_first_time.del_flag", 0));

        if (tzTwoBestOneFirstTime.getDeptId() != null) {
            queryWrapper.and(i -> i.like("tz_two_best_one_first_time.dept_id", tzTwoBestOneFirstTime.getDeptId()));
        }
        if (StringUtils.isNotEmpty(tzTwoBestOneFirstTime.getTitle())) {
            queryWrapper.and(i -> i.like("tz_two_best_one_first_time.title", tzTwoBestOneFirstTime.getTitle()));
        }

        if (StringUtils.isNotEmpty(tzTwoBestOneFirstTime.getStatus())) {
            queryWrapper.and(i -> i.eq("tz_two_best_one_first_time.status", tzTwoBestOneFirstTime.getStatus()));
        }

        return queryWrapper;
    }
}
