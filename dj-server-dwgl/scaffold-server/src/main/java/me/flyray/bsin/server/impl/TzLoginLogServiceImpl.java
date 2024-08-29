package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.facade.service.TzLoginLogService;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.mapper.TzLoginLogMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyMemberMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class TzLoginLogServiceImpl implements TzLoginLogService {

    @Autowired
    private TzLoginLogMapper tzLoginLogMapper;

    @Autowired
    private JcxfPartyMemberMapper jcxfPartyMemberMapper;

    @Override
    public Map<String, Object> queryLoginLogByPage(Map<String, Object> requestMap) {
        try {
            Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");
            TzLoginLog tzLoginLog = JSON.parseObject(JSON.toJSONString(dataMap.get("tzLoginLog")), TzLoginLog.class);
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(dataMap.get("pageVo")), PageVo.class);
            String startTime = (String) dataMap.get("startTime");
            String endTime = (String) dataMap.get("endTime");

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

            Page<TzLoginLog> pageData = new Page<>(page, limit);
            QueryWrapper<TzLoginLog> queryWrapper = new QueryWrapper<>();
            if (tzLoginLog != null) {
                LikeAllField(queryWrapper, tzLoginLog);
            }

            if (StringUtils.isNotBlank(startTime)) {
                queryWrapper.ge("create_time", startTime);
            }
            if (StringUtils.isNotBlank(endTime)) {
                queryWrapper.ne("create_time", endTime);
            }
            queryWrapper.orderByDesc("create_time");
            IPage<TzLoginLog> tzLoginLogIPage = tzLoginLogMapper.selectPage(pageData, queryWrapper);
            /*for (TzLoginLog log: tzLoginLogIPage.getRecords()) {
                if (log.getPartyMemberId() != null) {
                    JcxfPartyMember partyMember = jcxfPartyMemberMapper.selectById(log.getPartyMemberId());
                    if (partyMember != null) {
                        log.setPartyMember(partyMember);
                    }
                }
            }*/

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzLoginLogIPage));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    private QueryWrapper<TzLoginLog> LikeAllField(QueryWrapper<TzLoginLog> queryWrapper, TzLoginLog log) {
        if (StringUtils.isNotBlank(log.getResult())) {
            queryWrapper.eq("result", log.getResult());
        }

        if (StringUtils.isNotBlank(log.getType())) {
            queryWrapper.eq("type", log.getType());
        }

        if (StringUtils.isNotBlank(log.getPhone())) {
            queryWrapper.like("phone", log.getPhone());
        }
        if (StringUtils.isNotBlank(log.getIdCard())) {
            queryWrapper.like("id_card", log.getIdCard());
        }

        if (StringUtils.isNotBlank(log.getRealName())) {
            queryWrapper.like("real_name", log.getRealName());
        }
        return queryWrapper;
    }

}
