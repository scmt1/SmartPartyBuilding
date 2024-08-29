package me.flyray.bsin.server.impl.cq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.facade.service.CqPayFeeHistoryService;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.mapper.CqPayFeeHistoryMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class CqPayFeeHistoryServiceImpl implements CqPayFeeHistoryService {
    @Autowired
    private CqPayFeeHistoryMapper cqPayFeeHistoryMapper;

    @Override
    public Map<String, Object> getPayFeeHistoryList(Map<String, Object> requestMap) {
        Map<String, Object> resultMap = (Map<String, Object>) requestMap.get("data");
        CqPayFeeHistory cqPayFeeHistory = JSON.parseObject(JSON.toJSONString(resultMap.get("cqPayFeeHistory")), CqPayFeeHistory.class);
        String startPayMonth = (String) resultMap.get("startPayMonth");
        String endPayMonth = (String) resultMap.get("endPayMonth");
        SearchVo searchVo = JSON.parseObject(JSON.toJSONString(resultMap.get("searchVo")), SearchVo.class);
        PageVo pageVo = JSON.parseObject(JSON.toJSONString(resultMap.get("pageVo")), PageVo.class);

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
        Page<CqPayFeeHistory> pageData = new Page<>(page, limit);
        QueryWrapper<CqPayFeeHistory> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(startPayMonth)) {
            queryWrapper.ge("pay_for", startPayMonth);
        }
        if (StringUtils.isNotBlank(endPayMonth)) {
            queryWrapper.le("pay_for", endPayMonth);
        }

        if (cqPayFeeHistory.getOrgId() != null) {
            queryWrapper.and(i -> i.like("org_level", "-" + cqPayFeeHistory.getOrgId() + "-").or().eq("org_id", cqPayFeeHistory.getOrgId()));
        }
        if (StringUtils.isNotBlank(cqPayFeeHistory.getUserName())) {
            queryWrapper.like("user_name", cqPayFeeHistory.getUserName());
        }

        queryWrapper.ge("pay_already", 0);
        queryWrapper.orderByDesc("pay_for");
        Page<CqPayFeeHistory> selectPage = cqPayFeeHistoryMapper.selectPage(pageData, queryWrapper);
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(selectPage));
    }
}
