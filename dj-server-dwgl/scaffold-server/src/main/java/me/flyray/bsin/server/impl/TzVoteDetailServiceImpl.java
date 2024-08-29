package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.flyray.bsin.facade.service.TzVoteDetailService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.PageVo;
import me.flyray.bsin.server.domain.SearchVo;
import me.flyray.bsin.server.domain.TzVoteDetail;
import me.flyray.bsin.server.mapper.TzVoteDetailMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 具体投票项 服务实现类
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */
public class TzVoteDetailServiceImpl extends ServiceImpl<TzVoteDetailMapper, TzVoteDetail> implements TzVoteDetailService {
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private TzVoteDetailMapper tzVoteDetailMapper;

    @Override
    public Map<String, Object> queryTzVoteDetailListByPage(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

            TzVoteDetail tzVoteDetail = JSON.parseObject(JSON.toJSONString(map.get("tzVoteDetail")), TzVoteDetail.class);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(map.get("searchVo")), SearchVo.class);
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);

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
            Page<TzVoteDetail> pageData = new Page<>(page, limit);
            QueryWrapper<TzVoteDetail> queryWrapper = new QueryWrapper<>();
            if (tzVoteDetail != null) {
                queryWrapper = LikeAllField(tzVoteDetail, searchVo);
            }
            // queryWrapper.eq("tz_vote_detail.vote_id", tzVoteDetail.getVoteId()).orderByDesc("tz_vote_detail.number_vote");
            IPage<TzVoteDetail> result = tzVoteDetailMapper.selectPage(pageData, queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询错误");
        }

    }

    /**
     * 根据id查询投票排名
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> queryTzVoteDetailById(Map<String, Object> requestMap) {
        Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

        TzVoteDetail tzVoteDetail = JSON.parseObject(JSON.toJSONString(map.get("tzVoteDetail")), TzVoteDetail.class);

        if (tzVoteDetail.getVoteId() == null) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"id为空，请联系管理员");
        }

        try {
            QueryWrapper q = new QueryWrapper();
            q.select("number_vote");
            q.eq("vote_id", tzVoteDetail.getVoteId());
            q.groupBy("number_vote");
            q.orderByDesc("number_vote");
            // q.last(" limit "+(pageVo.getPageNumber()-1) +","+pageVo.getPageSize());
            List<TzVoteDetail> pageList = tzVoteDetailMapper.selectList(q);


            List<TzVoteDetail> resultList = new ArrayList<>();
            for (int i = 0; i < pageList.size(); i++) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("number_vote", pageList.get(i).getNumberVote());
                queryWrapper.eq("vote_id", tzVoteDetail.getVoteId());
                List<TzVoteDetail> list = tzVoteDetailMapper.selectList(queryWrapper);

                int lastNum = resultList.size();
                if (list !=null && list.size() > 0) {
                    for (TzVoteDetail detail: list) {
                        detail.setOrders(lastNum+1);
                        resultList.add(detail);
                    }
                }
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(resultList));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> getVoteDetailInfoById(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            TzVoteDetail tzVoteDetail = tzVoteDetailMapper.selectById(id);
            if (tzVoteDetail!=null) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzVoteDetail));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未找到数据，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> addVoteDetail(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            TzVoteDetail tzVoteDetail = JSON.parseObject(JSON.toJSONString(map.get("tzVoteDetail")), TzVoteDetail.class);
            tzVoteDetail.setNumberVote(0);
            tzVoteDetail.setStatus(1);
            int res = tzVoteDetailMapper.insert(tzVoteDetail);
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
    public Map<String, Object> editVoteDetail(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            TzVoteDetail tzVoteDetail = JSON.parseObject(JSON.toJSONString(map.get("tzVoteDetail")), TzVoteDetail.class);
            tzVoteDetail.setNumberVote(0);

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", tzVoteDetail.getId());
            updateWrapper.set("img_path", tzVoteDetail.getImgPath());
            updateWrapper.set("name_item", tzVoteDetail.getNameItem());
            updateWrapper.set("vote_Intro", tzVoteDetail.getVoteIntro());

            int res = tzVoteDetailMapper.update(null, updateWrapper);
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
    public Map<String, Object> deleteVoteDetail(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            int res = tzVoteDetailMapper.deleteById(id);
            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除异常");
        }
    }

    @Override
    public Map<String, Object> editVoteDetailStatus(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            String id = (String) map.get("id");
            String status = (String) map.get("status");

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", id);
            updateWrapper.set("status", status);

            int res = tzVoteDetailMapper.update(null, updateWrapper);
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

    /**
         * 功能描述：构建模糊查询
         * @param tzVoteDetail 需要模糊查询的信息
         * @return 返回查询
         */
        public QueryWrapper<TzVoteDetail>  LikeAllField(TzVoteDetail  tzVoteDetail, SearchVo searchVo) {
            QueryWrapper<TzVoteDetail> queryWrapper = new QueryWrapper<>();
            if(tzVoteDetail.getId() != null){
                queryWrapper.and(i -> i.like("tz_vote_detail.id", tzVoteDetail.getId()));
            }
            if(tzVoteDetail.getVoteId() != null){
                queryWrapper.and(i -> i.eq("tz_vote_detail.vote_id", tzVoteDetail.getVoteId()));
            }
            if(StringUtils.isNotEmpty(tzVoteDetail.getImgPath())){
                queryWrapper.and(i -> i.like("tz_vote_detail.img_path", tzVoteDetail.getImgPath()));
            }
            if(StringUtils.isNotEmpty(tzVoteDetail.getNameItem())){
                queryWrapper.and(i -> i.like("tz_vote_detail.name_item", tzVoteDetail.getNameItem()));
            }
            if(tzVoteDetail.getNumberVote() != null){
                queryWrapper.and(i -> i.like("tz_vote_detail.number_vote", tzVoteDetail.getNumberVote()));
            }
            if(StringUtils.isNotEmpty(tzVoteDetail.getVoteBy())){
                queryWrapper.and(i -> i.like("tz_vote_detail.vote_by", tzVoteDetail.getVoteBy()));
            }
         /*   if(searchVo!=null){
                if(searchVo.getStartDate()!=null && searchVo.getEndDate()!=null){
                    queryWrapper.lambda().and(i -> i.between(TzVoteDetail::getCreateTime, searchVo.getStartDate(),searchVo.getEndDate()));
                }
            }
            queryWrapper.lambda().and(i -> i.eq(TzVoteDetail::getDelFlag, 0));*/
            return queryWrapper;

        }
}
