package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.flyray.bsin.facade.service.TzExamService;
import me.flyray.bsin.facade.service.TzTestService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.mapper.TzExamMapper;
import me.flyray.bsin.server.mapper.TzTestDetailMapper;
import me.flyray.bsin.server.mapper.TzTestMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
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
@Transactional(rollbackFor = Exception.class)
public class TzExamServiceImpl extends ServiceImpl<TzExamMapper, TzExam> implements TzExamService {
    @Autowired
    private TzExamMapper tzExamMapper;


    @Override
    public Map<String, Object> queryExamByPage(Map<String, Object> requestMap) {
        try {
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), PageVo.class);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), SearchVo.class);
            TzExam tzExam = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), TzExam.class);
            //TzTest tzTest = JSON.parseObject(JSON.toJSONString(requestMap.get("test")), TzTest.class);
            if(tzExam==null||tzExam.getDeptId()==null){
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"部门id为空,请联系管理员");
            }
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
            Page<TzExam> pageData = new Page<>(page, limit);
            QueryWrapper<TzExam> queryWrapper = new QueryWrapper<>();
            queryWrapper = LikeAllField(tzExam, searchVo);
            queryWrapper.eq("tz_exam.del_flag",0).eq("tz_exam.dept_id",tzExam.getDeptId()).orderByAsc("tz_exam.id");
            IPage<TzExam> result = tzExamMapper.selectPage(pageData, queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }
    public QueryWrapper<TzExam> LikeAllField(TzExam tzExam, SearchVo searchVo) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        QueryWrapper<TzExam> queryWrapper = new QueryWrapper<>();
        if (tzExam.getId() != null) {
            queryWrapper.and(i -> i.like("tz_exam.id", tzExam.getId()));
        }
    /*    if (StringUtils.isNotEmpty(tzExam.getTitle())) {
            queryWrapper.and(i -> i.like("tz_exam.title", tzTest.getTitle()));
        }
        if (StringUtils.isNotEmpty(tzExam.getTestType())) {
            queryWrapper.and(i -> i.like("tz_exam.test_type", tzTest.getTestType()));
        }
        if (StringUtils.isNotEmpty(tzExam.getQuestionId())) {
            queryWrapper.and(i -> i.like("tz_exam.question_id", tzTest.getQuestionId()));
        }*/
        if (searchVo != null) {
            if (searchVo.getStartDate() != null && searchVo.getEndDate() != null) {
                queryWrapper.lambda().and(i -> i.between(TzExam::getCreateTime, searchVo.getStartDate(), searchVo.getEndDate()));
            }
        }
        return queryWrapper;
    }


}
