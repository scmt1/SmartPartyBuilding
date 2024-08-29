package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.facade.service.TzStudyFileService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.mapper.AttachFileMapper;
import me.flyray.bsin.server.mapper.TzStudyFileMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.*;

public class TzStudyFileServiceImpl implements TzStudyFileService {

    @Autowired
    private TzStudyFileMapper tzStudyFileMapper;

    @Autowired
    private AttachFileMapper attachFileMapper;

    @Override
    public Map<String, Object> cancelPostIt(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空，请联系管理员！");
        }
        try {
            QueryWrapper<TzStudyFile> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_study_file.id",id);
            queryWrapper.eq("tz_study_file.del_flag","0");
            TzStudyFile tzStudyFile = tzStudyFileMapper.selectOne(queryWrapper);
            tzStudyFile.setStatus("0");
            tzStudyFile.setPostTime(null);
            int updateById = tzStudyFileMapper.updateById(tzStudyFile);
            if (updateById > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("取消成功"));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"取消失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"操作异常");
        }
    }

    @Override
    public Map<String, Object> postIt(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空，请联系管理员！");
        }
        try {
            QueryWrapper<TzStudyFile> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_study_file.id",id);
            queryWrapper.eq("tz_study_file.del_flag","0");
            TzStudyFile tzStudyFile = tzStudyFileMapper.selectOne(queryWrapper);
            tzStudyFile.setStatus("1");
            tzStudyFile.setPostTime(new Date());
            int updateById = tzStudyFileMapper.updateById(tzStudyFile);
            if (updateById > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("发布成功"));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"发布失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"操作异常");
        }
    }

    @Override
    public Map<String, Object> queryTzTStudyFile(Map<String, Object> requestMap) {
        Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");

        TzStudyFile tzStudyFile = JSON.parseObject(JSON.toJSONString(dataMap.get("tzStudyFile")), TzStudyFile.class);
        SearchVo searchVo = JSON.parseObject(JSON.toJSONString(dataMap.get("searchVo")), SearchVo.class);
        PageVo pageVo = JSON.parseObject(JSON.toJSONString(dataMap.get("pageVo")), PageVo.class);

        try {
            IPage<TzStudyFile> result = queryTzTopicListByPage(tzStudyFile, searchVo, pageVo);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    @DS("dj_party")
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> deleteTStudyFile(Map<String, Object> requestMap) {
        List<String> ids = (List<String>) requestMap.get("ids");

        if (ids == null || ids.size() == 0) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空，请联系管理员！");
        }
        try {
            UpdateWrapper<TzTopic> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("tz_topic.del_flag", 1).in("tz_topic.id", ids);

            QueryWrapper<AttachFile> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("tz_attach_file.foreign_key", ids);
            queryWrapper.eq("tz_attach_file.type", 3);
            queryWrapper.eq("tz_attach_file.table_type", "tz_study_file");
            int remove = attachFileMapper.delete(queryWrapper);
            if(remove < 1){
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除失败");
            }
            int res = tzStudyFileMapper.deleteBatchIds(ids);
            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(res));
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除异常");
        }
    }

    @Override
    @DS("dj_party")
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveStudyFile(Map<String, Object> requestMap) {
        Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

        String json1 = JSON.toJSONString(map.get("attachFile"));
        String json2 = JSON.toJSONString(map.get("tzStudyFile"));
        String deptId = (String) map.get("deptId");
        try {
            AttachFile attachFile = JSON.parseObject(json1, AttachFile.class);
            TzStudyFile tzStudyFile = JSON.parseObject(json2, TzStudyFile.class);

            tzStudyFile.setDeptId(Long.valueOf(deptId));
            if (tzStudyFile.getId() != null) {
                QueryWrapper<AttachFile> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("tz_attach_file.foreign_key", tzStudyFile.getId());
                queryWrapper.eq("tz_attach_file.type", 3);
                queryWrapper.eq("tz_attach_file.table_type", "tz_study_file");
                int remove = attachFileMapper.delete(queryWrapper);
                if(remove < 1){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"修改失败");
                }
                tzStudyFile.setName(attachFile.getFileName());
                tzStudyFile.setStatus("0");
                //tzStudyFile.setUpdateBy(username);
                tzStudyFile.setUpdateTime(new Date());
                int updateById = tzStudyFileMapper.updateById(tzStudyFile);
                if (updateById > 0) {
                    attachFile.setForeignKey(tzStudyFile.getId().toString());
                    attachFile.setTableType("tz_study_file");
                    attachFile.setType(3);
                    attachFile.setUploadTime(new Date());
                    int save = attachFileMapper.insert(attachFile);
                    if (save > 0) {
                        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(updateById));
                    } else {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        throw new BusinessException(String.valueOf(ResponseCode.FAIL),"修改失败");
                    }
                } else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"修改失败");
                }

            } else {
                tzStudyFile.setDelFlag("0");
                tzStudyFile.setName(attachFile.getFileName());
                tzStudyFile.setStatus("0"); //状态为已新建
                //tzStudyFile.setCreateBy(username);
                tzStudyFile.setCreateTime(new Date());
                int res = tzStudyFileMapper.insert(tzStudyFile);
                if (res > 0) {
                    if (tzStudyFile.getId() == null) {
                        throw new BusinessException(String.valueOf(ResponseCode.FAIL),"文件id为空，请联系管理员");
                    }
                    attachFile.setForeignKey(tzStudyFile.getId().toString());
                    attachFile.setTableType("tz_study_file");
                    attachFile.setType(3);
                    attachFile.setUploadTime(new Date());
                    int save = attachFileMapper.insert(attachFile);
                    if (save > 0) {
                        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(save));
                    } else {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        throw new BusinessException(String.valueOf(ResponseCode.FAIL),"修改失败");
                    }
                } else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存异常");
        }
    }

    @Override
    public Map<String, Object> getStudyFile(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");

        if (StringUtils.isBlank(id)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空，请联系管理员！");
        }
        try {
            Map<String ,Object> map=new HashMap<>();
            QueryWrapper<AttachFile> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_attach_file.foreign_key",id);
            queryWrapper.eq("tz_attach_file.type", 3);
            queryWrapper.eq("tz_attach_file.table_type", "tz_study_file");
            List<AttachFile> list = attachFileMapper.selectList(queryWrapper);
            TzStudyFile tzStudyFile = tzStudyFileMapper.selectById(id);
            if (tzStudyFile != null) {
                map.put("attachList",list);
                map.put("tzStudyFile",tzStudyFile);
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    public IPage<TzStudyFile> queryTzTopicListByPage(TzStudyFile tzStudyFile, SearchVo searchVo, PageVo pageVo) {
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

        Page<TzStudyFile> pageData = new Page<>(page, limit);
        QueryWrapper<TzStudyFile> queryWrapper = new QueryWrapper<>();
        if (tzStudyFile != null) {
            queryWrapper = LikeAllField(tzStudyFile, searchVo);
        }
        IPage<TzStudyFile> result = tzStudyFileMapper.selectPage(pageData, queryWrapper);
        return result;
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param tzStudyFile 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<TzStudyFile> LikeAllField(TzStudyFile tzStudyFile, SearchVo searchVo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        QueryWrapper<TzStudyFile> queryWrapper = new QueryWrapper<>();
        if(tzStudyFile.getDeptId() != null){
            queryWrapper.and(i -> i.eq("tz_study_file.dept_id", (tzStudyFile.getDeptId())));
        }
        if (searchVo != null) {
            if (StringUtils.isNotBlank(searchVo.getStartDate()) && StringUtils.isNotBlank(searchVo.getEndDate())) {
                queryWrapper.lambda().and(i -> i.between(TzStudyFile::getCreateTime, searchVo.getStartDate(), searchVo.getEndDate()));
            }
        }
        queryWrapper.lambda().and(i -> i.eq(TzStudyFile::getDelFlag, 0));
        return queryWrapper;
    }


}
