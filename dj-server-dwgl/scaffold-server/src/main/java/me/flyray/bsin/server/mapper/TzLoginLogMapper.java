package me.flyray.bsin.server.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.flyray.bsin.server.domain.TzLoginLog;

@DS(value = "dj_party")
public interface TzLoginLogMapper extends BaseMapper<TzLoginLog> {
}
