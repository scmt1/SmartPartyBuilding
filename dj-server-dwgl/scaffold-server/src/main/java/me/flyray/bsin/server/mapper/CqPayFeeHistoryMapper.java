package me.flyray.bsin.server.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.flyray.bsin.server.domain.CqPayFeeHistory;
import me.flyray.bsin.server.domain.CqSysOrg;

import java.util.List;

@DS("dj_party")
public interface CqPayFeeHistoryMapper extends BaseMapper<CqPayFeeHistory> {
}
