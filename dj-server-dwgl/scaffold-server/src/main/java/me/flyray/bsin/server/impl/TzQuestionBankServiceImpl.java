package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.flyray.bsin.facade.service.TzQuestionBankService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.mapper.TzQuestionBankMapper;
import me.flyray.bsin.server.mapper.TzTestMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class TzQuestionBankServiceImpl extends ServiceImpl<TzQuestionBankMapper, TzQuestionBank> implements TzQuestionBankService {

    @Autowired
    private TzQuestionBankMapper questionBankMapper;

    @Autowired
    private TzTestMapper tzTestMapper;
    @Override
    public Map<String, Object> addQuestionBank(Map<String, Object> requestMap) {
        try {
            TzQuestionBank tzQuestionBank = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), TzQuestionBank.class);
            if(tzQuestionBank==null||StringUtils.isEmpty(tzQuestionBank.getDeptId())){
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"部门id为空");
            }
            tzQuestionBank.setDelFlag(0);
            if(tzQuestionBank.getId()!=null){
                QueryWrapper<TzQuestionBank> queryWrapper = new QueryWrapper();
                queryWrapper.eq("tz_question_bank.id",tzQuestionBank.getId()).eq("tz_question_bank.del_flag",0);
                tzQuestionBank.setUpdateTime(new Date());
                int update = questionBankMapper.update(tzQuestionBank, queryWrapper);
                if(update==0){
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"修改失败");
                }
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(true)) ;
            }
            tzQuestionBank.setCreateTime(new Date());
            int insert = questionBankMapper.insert(tzQuestionBank);
            if(insert==0){
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"添加失败");
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(true));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"操作失败");
        }
    }

    @Override
    public Map<String, Object> queryQuestionBankByPage(Map<String, Object> requestMap) {
        try {
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), PageVo.class);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), SearchVo.class);
            TzQuestionBank tzQuestionBank = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), TzQuestionBank.class);
            if(tzQuestionBank==null||StringUtils.isEmpty(tzQuestionBank.getDeptId())){
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
            Page<TzQuestionBank> pageData = new Page<>(page, limit);
            QueryWrapper<TzQuestionBank> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_question_bank.del_flag",0).eq("tz_question_bank.dept_id",tzQuestionBank.getDeptId());
            IPage<TzQuestionBank> result = questionBankMapper.selectPage(pageData, queryWrapper);
            List<TzQuestionBank> records = result.getRecords();
            if(records!=null&records.size()>0){
                for (TzQuestionBank record : records) {
                    Integer id = record.getId();
                    QueryWrapper<TzTest>tzTestQueryWrapper=new QueryWrapper<>();
                    tzTestQueryWrapper.eq("tz_test.question_id",id).eq("tz_test.del_flag",0);
                    Integer countAll = tzTestMapper.selectCount(tzTestQueryWrapper);
                    //单选题数量
                    tzTestQueryWrapper =new QueryWrapper<>();
                    tzTestQueryWrapper.eq("tz_test.question_id",id).eq("tz_test.del_flag",0).eq("tz_test.test_type",1);
                    Integer oneSelect = tzTestMapper.selectCount(tzTestQueryWrapper);
                    //多选题数量
                    tzTestQueryWrapper =new QueryWrapper<>();
                    tzTestQueryWrapper.eq("tz_test.question_id",id).eq("tz_test.del_flag",0).eq("tz_test.test_type",2);
                    Integer selects = tzTestMapper.selectCount(tzTestQueryWrapper);
                    //判断题数量
                    tzTestQueryWrapper =new QueryWrapper<>();
                    tzTestQueryWrapper.eq("tz_test.question_id",id).eq("tz_test.del_flag",0).eq("tz_test.test_type",3);
                    Integer isTrueCount = tzTestMapper.selectCount(tzTestQueryWrapper);
                    record.setCountAll(countAll);
                    record.setOneSelect(oneSelect);
                    record.setSelects(selects);
                    record.setIsTrueCount(isTrueCount);
                }
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Override
    public Map<String, Object> findQuestionBankById(Map<String, Object> requestMap) {
        try {
            TzQuestionBank tzQuestionBank = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), TzQuestionBank.class);
            if(tzQuestionBank==null||tzQuestionBank.getId()==null){
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"id为空,请联系管理员");
            }
            QueryWrapper<TzQuestionBank> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_question_bank.id",tzQuestionBank.getId()).eq("tz_question_bank.del_flag",0);
            TzQuestionBank tzQuestionBank1 = questionBankMapper.selectOne(queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzQuestionBank1));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Override
    public Map<String, Object> findAllBankBy(Map<String, Object> requestMap) {
        try {
            String deptId= (String) requestMap.get("deptId");
            if(StringUtils.isEmpty(deptId)){
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"部门id为空");
            }
            QueryWrapper<TzQuestionBank>questionBankQueryWrapper=new QueryWrapper<>();
            questionBankQueryWrapper.eq("tz_question_bank.dept_id",deptId).eq("tz_question_bank.del_flag",0);
            List<TzQuestionBank> tzQuestionBanks = questionBankMapper.selectList(questionBankQueryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzQuestionBanks));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }

    }

    @Override
    public Map<String, Object> deleteQuestionBank(Map<String, Object> requestMap) {
        try {
            List<String> ids = (List<String>) requestMap.get("ids");

            if (ids == null || ids.size() == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
            }
            UpdateWrapper<TzQuestionBank> questionBankQueryWrapper=new UpdateWrapper<>();
            questionBankQueryWrapper.set("tz_question_bank.del_flag",1).in("tz_question_bank.id",ids);
            int update = questionBankMapper.update(null, questionBankQueryWrapper);
            if (update > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(update));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除失败");
        }

    }
}
