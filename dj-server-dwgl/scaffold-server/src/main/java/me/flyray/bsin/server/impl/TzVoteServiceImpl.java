package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.flyray.bsin.facade.service.TzVoteService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.mapper.TzVoteMapper;
import me.flyray.bsin.server.mapper.TzVoteUserMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

public class TzVoteServiceImpl extends ServiceImpl<TzVoteMapper, TzVote> implements TzVoteService {

    @Autowired
    private TzVoteMapper voteMapper;
    @Autowired
    private TzVoteDetailServiceImpl tzVoteDetailService;

    @Autowired
    private TzVoteUserMapper tzVoteUserMapper;

    /**
     * 新增投票
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> addVote(Map<String, Object> requestMap) {
        try {
            Map<String,Object> map = (Map<String, Object>) requestMap.get("data");

            TzVote tzVote = JSON.parseObject(JSON.toJSONString(map.get("vote")), TzVote.class);

            tzVote.setDelFlag("0");
            tzVote.setCreateTime(new Date());
            tzVote.setVoteStatus(1); //状态 1、待发布 2、已发布

            String voteDeptIds = tzVote.getVoteDeptIds();
            if (StringUtils.isNotEmpty(voteDeptIds)) {
                voteDeptIds = voteDeptIds.substring(1, voteDeptIds.length() - 1);
                tzVote.setVoteDeptIds(voteDeptIds);
            }
            boolean save = save(tzVote);
            if (save) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
            }
        } catch (Exception e) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
        }
    }

    /**
     * 分页查询vote数据
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> queryTzVoteList(Map<String, Object> requestMap) {
        Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

        PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);
        SearchVo searchVo = JSON.parseObject(JSON.toJSONString(map.get("searchVo")), SearchVo.class);
        TzVote tzVote = JSON.parseObject(JSON.toJSONString(map.get("tzVote")), TzVote.class);
        String deptId = String.valueOf(tzVote.getStartDeptId());

        try {
            if (StringUtils.isEmpty(deptId)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"部门id为空，请联系管理员");
            }

            /*QueryWrapper<TzSysDept> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("tz_sys_dept.name").eq("tz_sys_dept.id", deptId);
            queryWrapper.eq("tz_sys_dept.del_flag", 0);
            TzSysDept one = sysDeptService.getOne(queryWrapper);*/

            Map<String, Object> resultMap = new HashMap<>();
            ///resultMap.put("startDept", one.getName());
            //resultMap.put("createBy", one.getName());

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
            Page<TzVote> pageData = new Page<>(page, limit);
            QueryWrapper<TzVote> queryWrapper1 = new QueryWrapper<>();
            if (tzVote != null) {
                queryWrapper1 = LikeAllField(tzVote, searchVo);
            }
            IPage<TzVote> result = voteMapper.selectPage(pageData, queryWrapper1);
            for(TzVote vote: result.getRecords()) {
                // 投票项数量
                QueryWrapper q = new QueryWrapper();
                q.eq("vote_id", vote.getId());
                vote.setDetailNum(tzVoteDetailService.count(q));
                //已获得票数
                QueryWrapper q2 = new QueryWrapper();
                q2.eq("vote_id", vote.getId());
                vote.setUserNum(tzVoteUserMapper.selectCount(q2));
            }

            resultMap.put("result", result);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(resultMap));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }


    /**
     * 功能描述：构建模糊查询
     *
     * @param tzVote 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<TzVote> LikeAllField(TzVote tzVote, SearchVo searchVo) {
        QueryWrapper<TzVote> queryWrapper = new QueryWrapper<>();
        if (tzVote.getId() != null) {
            queryWrapper.and(i -> i.like("tz_vote.id", tzVote.getId()));
        }
        if (StringUtils.isNotEmpty(tzVote.getDelFlag())) {
            queryWrapper.and(i -> i.like("tz_vote.del_flag", tzVote.getDelFlag()));
        }
        if (StringUtils.isNotEmpty(tzVote.getName())) {
            queryWrapper.and(i -> i.like("tz_vote.name", tzVote.getName()));
        }
        if (StringUtils.isNotEmpty(tzVote.getStartDept())) {
            queryWrapper.and(i -> i.like("tz_vote.start_dept", tzVote.getStartDept()));
        }
        if (tzVote.getStartDeptId() != null) {
            queryWrapper.and(i -> i.eq("tz_vote.start_dept_id", tzVote.getStartDeptId()));
        }
        if (StringUtils.isNotEmpty(tzVote.getCreateBy())) {
            queryWrapper.and(i -> i.like("tz_vote.create_by", tzVote.getCreateBy()));
        }
        if (tzVote.getStartDate() != null) {
            queryWrapper.and(i -> i.like("tz_vote.start_date", tzVote.getStartDate()));
        }
        if (tzVote.getEndDate() != null) {
            queryWrapper.and(i -> i.like("tz_vote.end_date", tzVote.getEndDate()));
        }
        if (StringUtils.isNotEmpty(tzVote.getIsScore())) {
            queryWrapper.and(i -> i.like("tz_vote.is_score", tzVote.getIsScore()));
        }
        if (tzVote.getNumberTimes() != null) {
            queryWrapper.and(i -> i.like("tz_vote.number_times", tzVote.getNumberTimes()));
        }
        if (StringUtils.isNotEmpty(tzVote.getVoteResult())) {
            queryWrapper.and(i -> i.like("tz_vote.vote_result", tzVote.getVoteResult()));
        }
        if (StringUtils.isNotEmpty(tzVote.getVoteDescription())) {
            queryWrapper.and(i -> i.like("tz_vote.vote_description", tzVote.getVoteDescription()));
        }
        if (StringUtils.isNotEmpty(tzVote.getVoteDeptIds())) {
            queryWrapper.and(i -> i.like("tz_vote.vote_dept_ids", tzVote.getVoteDeptIds()));
        }
        if (tzVote.getCreateTime() != null) {
            queryWrapper.and(i -> i.like("tz_vote.create_time", tzVote.getCreateTime()));
        }
        if (searchVo != null) {
            if (searchVo.getStartDate() != null && searchVo.getEndDate() != null) {
                queryWrapper.lambda().and(i -> i.between(TzVote::getCreateTime, searchVo.getStartDate(), searchVo.getEndDate()));
            }
        }
        queryWrapper.lambda().and(i -> i.eq(TzVote::getDelFlag, 0));
        return queryWrapper;

    }

    @Override
    public Map<String, Object> queryTzVoteById(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");
        if (StringUtils.isEmpty(id)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
        }
        try {
            TzVote one = getById(id);
            if (one == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(one));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }

    }

    /**
     * 根据id删除投票信息
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> deleteTzVoteById(Map<String, Object> requestMap) {
        List<String> ids = (List<String>) requestMap.get("ids");
        if (ids == null || ids.size() == 0) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
        }
        boolean b = removeByIds((ids));
        QueryWrapper<TzVoteDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("tz_vote_detail.vote_id", ids);
        boolean remove = tzVoteDetailService.remove(queryWrapper);
        if (b && remove) {
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(true));
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除失败");
        }
    }

    @Override
    public Map<String, Object> queryVote(Map<String, Object> requestMap) {
        return null;
    }

    @Override
    public Map<String, Object> updateStatusById(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

            String id = (String) map.get("id");
            String status = (String) map.get("status");

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.set("vote_status", status);
            updateWrapper.eq("id", id);

            boolean res = update(updateWrapper);
            if (res) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"修改异常");
        }
    }

    @Override
    public Map<String, Object> updateVoteById(Map<String, Object> requestMap) {
        try {
            Map<String,Object> map = (Map<String, Object>) requestMap.get("data");

            TzVote tzVote = JSON.parseObject(JSON.toJSONString(map.get("vote")), TzVote.class);

            tzVote.setDelFlag("0");
            String voteDeptIds = tzVote.getVoteDeptIds();
            if (StringUtils.isNotEmpty(voteDeptIds)) {
                voteDeptIds = voteDeptIds.substring(1, voteDeptIds.length() - 1);
                tzVote.setVoteDeptIds(voteDeptIds);
            }
            boolean save = updateById(tzVote);
            if (save) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
            }
        } catch (Exception e) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
        }
    }
}
