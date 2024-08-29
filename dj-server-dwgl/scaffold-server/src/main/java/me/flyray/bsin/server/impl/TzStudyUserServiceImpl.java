package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.facade.service.TzStudyUserService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.domain.TzStudyUser;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.mapper.TzStudyUserMapper;
import me.flyray.bsin.server.mapper.TzStudyVideoMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyMemberMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class TzStudyUserServiceImpl implements TzStudyUserService {

    @Autowired
    private TzStudyUserMapper jcxfStudyUserMapper;

    @Autowired
    private JcxfPartyMemberMapper jcxfPartyMemberMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private TzStudyVideoMapper tzStudyVideoMapper;

    @Override
    public Map<String, Object> getTzStudyUserListByPage(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

            TzStudyUser tzStudyUser = JSON.parseObject(JSON.toJSONString(map.get("tzStudyUser")), TzStudyUser.class);
            SearchVo searchVo  = JSON.parseObject(JSON.toJSONString(map.get("searchVo")), SearchVo.class);
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);

            IPage<TzStudyUser> result = queryTzStudyUserListByPage(tzStudyUser, searchVo, pageVo);

            //获取党员信息
            for (TzStudyUser studyUser: result.getRecords()) {
                JcxfPartyMember partyMember = jcxfPartyMemberMapper.selectById(studyUser.getUserId());
                JcxfSysDept tzSysDept = jcxfSysDeptMapper.selectById(partyMember.getDeptId());
                if (tzSysDept != null) {
                    partyMember.setDeptName(tzSysDept.getName());
                }
                studyUser.setPartyMember(partyMember);
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> getTzStudyUserList(Map<String, Object> requestMap) {
        try {
            String userId = (String) requestMap.get("id");
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("user_id", userId);
            queryWrapper.orderByDesc("create_time");

            List<TzStudyUser> tzStudyUserList = jcxfStudyUserMapper.selectList(queryWrapper);
            for (TzStudyUser tzStudyUser: tzStudyUserList) {
                tzStudyUser.setTzStudyVideo(tzStudyVideoMapper.selectById(tzStudyUser.getStudyId()));
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzStudyUserList));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    public IPage<TzStudyUser>  queryTzStudyUserListByPage(TzStudyUser tzStudyUser, SearchVo searchVo, PageVo pageVo) {
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

        Page<TzStudyUser> pageData = new Page<>(page, limit);
        QueryWrapper<TzStudyUser> queryWrapper = new QueryWrapper<>();
        if (tzStudyUser != null) {
            queryWrapper = LikeAllField(tzStudyUser,searchVo);
        }

        IPage<TzStudyUser> result = jcxfStudyUserMapper.selectPage(pageData, queryWrapper);
        return result;
    }

    public QueryWrapper<TzStudyUser> LikeAllField(TzStudyUser tzStudyUser, SearchVo searchVo) {
        QueryWrapper<TzStudyUser> queryWrapper = new QueryWrapper<>();
        if (tzStudyUser.getUserId() != null) {
            queryWrapper.eq("user_id", tzStudyUser.getUserId());
        }

        if (tzStudyUser.getStudyId() != null) {
            queryWrapper.eq("study_id", tzStudyUser.getStudyId());
        }

        return queryWrapper;
    }

}
