package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.flyray.bsin.facade.service.TzTestService;
import me.flyray.bsin.facade.service.TzVoteDetailService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.mapper.TzTestDetailMapper;
import me.flyray.bsin.server.mapper.TzTestMapper;
import me.flyray.bsin.server.mapper.TzVoteDetailMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 具体投票项 服务实现类
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */
@Transactional(rollbackFor = Exception.class)
public class TzTestServiceImpl extends ServiceImpl<TzTestMapper, TzTest> implements TzTestService {
    @Autowired
    private TzTestMapper tzTestMapper;
    @Autowired
    private TzTestDetailMapper tzTestDetailMapper;


    @Override
    public Map<String, Object> addTest(Map<String, Object> requestMap) {
        try {
            TzTest tzTest = JSON.parseObject(JSON.toJSONString(requestMap.get("test")), TzTest.class);
            List<TzTestDetail> tzTestDetails = JSON.parseObject(JSON.toJSONString(requestMap.get("testDetail")), new TypeReference<List<TzTestDetail>>(){});

            if(StringUtils.isEmpty(tzTest.getDeptId())){
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"部门id为空，请联系管理员");
            }
            if(tzTest==null||tzTestDetails==null||tzTestDetails.size()==0){
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"操作异常，请联系管理员");
            }
            tzTest.setDelFlag(0);
            //新增
            if(tzTest.getId()==null){
                tzTest.setCreateTime(new Date());
                int insert = tzTestMapper.insert(tzTest);
                if(insert== 0){
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"添加失败");
                }
                Integer testId = tzTest.getId();
                for (TzTestDetail tzTestDetail : tzTestDetails) {
                    tzTestDetail.setTestId(testId);
                    tzTestDetail.setCreateTime(new Date());
                    int tzTestInt = tzTestDetailMapper.insert(tzTestDetail);
                    if(tzTestInt==0){
                        throw new BusinessException(String.valueOf(ResponseCode.FAIL),"添加失败");
                    }
                }
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            }else {  //修改
                tzTest.setUpdateTime(new Date());
                QueryWrapper<TzTest>queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("tz_test.id",tzTest.getId()).eq("tz_test.del_flag",0);
                int update = tzTestMapper.update(tzTest, queryWrapper);
                if(update==0){
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"添加失败");
                }
                QueryWrapper<TzTestDetail> testDetailQueryWrapper = new QueryWrapper<>();
                testDetailQueryWrapper.eq("tz_test_detail.test_id",tzTest.getId());
                int delete = tzTestDetailMapper.delete(testDetailQueryWrapper);
                if(delete==0){
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"修改失败");
                }

                Integer testId = tzTest.getId();
                for (TzTestDetail tzTestDetail : tzTestDetails) {
                    tzTestDetail.setTestId(testId);
                    tzTestDetail.setCreateTime(new Date());
                    int tzTestInt = tzTestDetailMapper.insert(tzTestDetail);
                    if(tzTestInt==0){
                        throw new BusinessException(String.valueOf(ResponseCode.FAIL),"添加失败");
                    }
                }
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Map<String, Object> queryTestByPage(Map<String, Object> requestMap) {
        try {
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), PageVo.class);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), SearchVo.class);
            TzTest tzTest = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), TzTest.class);
            //TzTest tzTest = JSON.parseObject(JSON.toJSONString(requestMap.get("test")), TzTest.class);
            if(tzTest==null||StringUtils.isEmpty(tzTest.getDeptId())){
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
            Page<TzTest> pageData = new Page<>(page, limit);
            QueryWrapper<TzTest> queryWrapper = new QueryWrapper<>();
            queryWrapper = LikeAllField(tzTest, searchVo);
            queryWrapper.eq("tz_test.del_flag",0).eq("tz_test.dept_id",tzTest.getDeptId()).orderByAsc("tz_test.id");
            IPage<TzTest> result = tzTestMapper.selectPage(pageData, queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Override
    public Map<String, Object> findTestById(Map<String, Object> requestMap) {
        try {
            String testId= (String) requestMap.get("testId");
            if(StringUtils.isEmpty(testId)){
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
            }
            QueryWrapper<TzTest>tzTestQueryWrapper =new QueryWrapper<>();
            tzTestQueryWrapper.eq("tz_test.id",testId).eq("tz_test.del_flag",0);
            TzTest tzTest = tzTestMapper.selectOne(tzTestQueryWrapper);
            if(tzTest==null){
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
            }
            QueryWrapper<TzTestDetail> testDetailQueryWrapper = new QueryWrapper<>();
            testDetailQueryWrapper.eq("tz_test_detail.test_id",testId);
            List<TzTestDetail> tzTestDetails = tzTestDetailMapper.selectList(testDetailQueryWrapper);
            if(tzTestDetails==null||tzTestDetails.size()==0){
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
            }
            Map<String,Object> map = new HashMap<>();
            map.put("test",tzTest);
            map.put("testDetails",tzTestDetails);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Override
    public Map<String, Object> findTestByQuestionId(Map<String, Object> requestMap) {
        try {
            String questionId= (String) requestMap.get("questionId");
            if(StringUtils.isEmpty(questionId)){
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
            }
            QueryWrapper<TzTest>queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("tz_test.question_id",questionId).eq("tz_test.del_flag",0);
            List<TzTest> tzTests = tzTestMapper.selectList(queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzTests));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    public QueryWrapper<TzTest> LikeAllField(TzTest tzTest, SearchVo searchVo) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        QueryWrapper<TzTest> queryWrapper = new QueryWrapper<>();
        if (tzTest.getId() != null) {
            queryWrapper.and(i -> i.like("tz_test.id", tzTest.getId()));
        }
        if (StringUtils.isNotEmpty(tzTest.getTitle())) {
            queryWrapper.and(i -> i.like("tz_test.title", tzTest.getTitle()));
        }
        if (StringUtils.isNotEmpty(tzTest.getTestType())) {
            queryWrapper.and(i -> i.like("tz_test.test_type", tzTest.getTestType()));
        }
        if (StringUtils.isNotEmpty(tzTest.getQuestionId())) {
            queryWrapper.and(i -> i.like("tz_test.question_id", tzTest.getQuestionId()));
        }
        if (StringUtils.isNotEmpty(tzTest.getQuestionId())) {
            queryWrapper.and(i -> i.like("tz_test.question_id", tzTest.getQuestionId()));
        }
        if (searchVo != null) {
            if (searchVo.getStartDate() != null && searchVo.getEndDate() != null) {
                queryWrapper.lambda().and(i -> i.between(TzTest::getCreateTime, searchVo.getStartDate(), searchVo.getEndDate()));
            }
        }
        return queryWrapper;
    }
}
