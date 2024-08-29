package me.flyray.bsin.server.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.flyray.bsin.server.domain.bsin.BsinSysTenant;
@DS("bsin")
public interface BsinSysTenantMapper extends BaseMapper<BsinSysTenant> {

    BsinSysTenant getSysTenantInfo(String tenantId);
}
