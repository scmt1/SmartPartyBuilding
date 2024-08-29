package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.facade.service.TzPayFeeDetailLogService;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.domain.PageVo;
import me.flyray.bsin.server.domain.SearchVo;
import me.flyray.bsin.server.domain.TzPayFeeDetailLog;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.mapper.TzPayFeeDetailLogMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyMemberMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TzPayFeeDetailLogServiceImpl implements TzPayFeeDetailLogService {

    @Autowired
    private TzPayFeeDetailLogMapper tzPayFeeDetailLogMapper;

    @Autowired
    private JcxfPartyMemberMapper jcxfPartyMemberMapper;

    @Override
    public Map<String, Object> queryTzPayFeeDetailLogPage(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

            PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(map.get("searchVo")), SearchVo.class);
            TzPayFeeDetailLog tzPayFeeDetailLog = JSON.parseObject(JSON.toJSONString(map.get("tzPayFeeDetailLog")), TzPayFeeDetailLog.class);

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

            Page<TzPayFeeDetailLog> pageData = new Page<>(page, limit);
            QueryWrapper<TzPayFeeDetailLog> queryWrapper = new QueryWrapper<>();
            if (tzPayFeeDetailLog != null) {
                queryWrapper = LikeAllField(tzPayFeeDetailLog, searchVo);
            }
            queryWrapper.eq("del_flag", 0);
            queryWrapper.orderByDesc("create_time");

            IPage<TzPayFeeDetailLog> result = tzPayFeeDetailLogMapper.selectPage(pageData, queryWrapper);
            for (TzPayFeeDetailLog log: result.getRecords()) {
                if (log.getPartyMemberId() != null) {
                    JcxfPartyMember member = jcxfPartyMemberMapper.selectById(log.getPartyMemberId());
                    if (member != null) {
                        log.setPartMemberName(member.getRealName());
                    }
                }
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    public QueryWrapper<TzPayFeeDetailLog> LikeAllField(TzPayFeeDetailLog tzPayFeeDetailLog, SearchVo searchVo) {
        QueryWrapper<TzPayFeeDetailLog> queryWrapper = new QueryWrapper<>();
        if (tzPayFeeDetailLog.getPayFeeDetailId() != null) {
            queryWrapper.eq("pay_fee_detail_id", tzPayFeeDetailLog.getPayFeeDetailId());
        }

        if (StringUtils.isNotBlank(tzPayFeeDetailLog.getOrderNo())) {
            queryWrapper.eq("order_no", tzPayFeeDetailLog.getOrderNo());
        }

        if (tzPayFeeDetailLog.getOrderStatus() != null && tzPayFeeDetailLog.getOrderStatus() == 99) {
            List<Integer> statusList = new ArrayList<>();
            statusList.add(1); //1代表支付中
            statusList.add(2); //2代表支付成功
            statusList.add(5); //5代表已退款
            queryWrapper.in("order_status", statusList);
        }
        if (tzPayFeeDetailLog.getOrderStatus() != null && tzPayFeeDetailLog.getOrderStatus() != 99) {
            queryWrapper.eq("order_status", tzPayFeeDetailLog.getOrderStatus());
        }

        return queryWrapper;
    }
}
